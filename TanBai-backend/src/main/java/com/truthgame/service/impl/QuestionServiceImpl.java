package com.truthgame.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.exception.BusinessException;
import com.truthgame.mapper.*;
import com.truthgame.model.dto.QuestionDTO;
import com.truthgame.model.entity.*;
import com.truthgame.model.vo.*;
import com.truthgame.service.AnswerService;
import com.truthgame.service.QuestionService;
import com.truthgame.service.UserService;
import com.truthgame.utils.SqlUtils;
import com.truthgame.utils.WeChatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.truthgame.constant.CommentAndReplyConstant.DEFAULT_REPLY;

@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    
    private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);
    
    @Resource
    private QuestionMapper questionMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private AnswerMapper answerMapper;

    @Resource
    private AnswerService answerService;

    @Resource
    private UserService userService;

    @Resource
    private WeChatUtils weChatUtils;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private QuestionViewPermissionMapper questionViewPermissionMapper;

    private static final String DEFAULT_AVATAR = "https://img2.baidu.com/it/u=29885366,3592956385&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1736701200&t=0b7528d14e43ff76c37e9b62895090ec";
    private static final String DEFAULT_NICKNAME = "匿名用户";

    @Override
    public Question createQuestion(QuestionDTO questionDTO) {
        String content = questionDTO.getContent();
        boolean b = SqlUtils.validSortField(content);
        if (!b) {
            throw new BusinessException("内容非法");
        }
        // 调用微信内容安全检查接口
        String accessToken = weChatUtils.getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
        
        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("content", questionDTO.getContent());
        
        try {
            String result = restTemplate.postForObject(url, params, String.class);
            JSONObject json = JSONUtil.parseObj(result);
            
            // 检查返回结果
            if (json.getInt("errcode") != 0) {
                throw new BusinessException("内容非法：" + json.getStr("errmsg"));
            }
            
            // 内容检查通过，继续创建问题
            Question question = new Question();
            BeanUtils.copyProperties(questionDTO, question);
            question.setCreateTime(LocalDateTime.now());
            question.setUpdateTime(LocalDateTime.now());
            questionMapper.insert(question);
            return question;
        } catch (Exception e) {
            log.error("内容安全检查失败", e);
            throw new BusinessException("内容安全检查失败，请稍后重试");
        }
    }
    
    @Override
    public List<QuestionWithAnswerCountVO> getMyQuestions(Long creatorId) {
        // 获取我创建的问题列表
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getCreatorId, creatorId)
               .orderByDesc(Question::getCreateTime);
        List<Question> questions = questionMapper.selectList(wrapper);
        
        // 构建带回答数量的问题列表
        return questions.stream().map(question -> {
            QuestionWithAnswerCountVO vo = new QuestionWithAnswerCountVO();
            vo.setQuestion(question);
            
            // 查询该问题的回答数量
            LambdaQueryWrapper<Answer> answerWrapper = new LambdaQueryWrapper<>();
            answerWrapper.eq(Answer::getQuestionId, question.getId());
            Long answerCount = answerMapper.selectCount(answerWrapper);
            vo.setAnswerCount(answerCount);
            
            return vo;
        }).collect(Collectors.toList());
    }
    
    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId, Long userId) {
        // 获取问题信息
        Question question = getById(questionId);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }
        
        // 获取创建者信息
        User creator = userService.getById(question.getCreatorId());
        
        // 构建QuestionVO
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        if (creator != null) {
            questionVO.setCreatorNickName(creator.getNickName());
            questionVO.setCreatorAvatarUrl(creator.getAvatarUrl());
        }
        
        // 获取回答列表
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        List<AnswerWithRepliesVO> answerVOs = answers.stream().map(answer -> {
            AnswerWithRepliesVO vo = new AnswerWithRepliesVO();
            vo.setAnswer(answer);
            
            // 获取回答者信息
            User user = userMapper.selectById(answer.getUserId());
            if (user != null) {
                // 判断是否显示真实用户信息：
                // 1. 如果当前用户不是问题创建者，不显示真实信息
                // 2. 如果是问题创建者且已支付，显示真实信息
                // 3. 如果是问题创建者但未支付：
                //    - 如果是自己的回答，显示真实信息
                //    - 如果是他人的回答，显示匿名信息
                if (!question.getCreatorId().equals(userId)) {
                    // 非创建者可以看到创建问题主人自己的留言和自己的留言
                    if (question.getCreatorId().equals(answer.getUserId()) || answer.getUserId().equals(userId)){
                        vo.setUserNickName(user.getNickName());
                        vo.setUserAvatarUrl(user.getAvatarUrl());
                    }else {
                        // 非创建者不可以看到真实信息
                        vo.setUserNickName(DEFAULT_NICKNAME);
                        vo.setUserAvatarUrl(DEFAULT_AVATAR);
                    }
                } else if (Boolean.TRUE.equals(question.getIsPaid())) {
                    // 创建者已支付，可以看到真实信息
                    vo.setUserNickName(user.getNickName());
                    vo.setUserAvatarUrl(user.getAvatarUrl());
                } else if (answer.getUserId().equals(question.getCreatorId())) {
                    // 创建者查看自己的回答，显示真实信息
                    vo.setUserNickName(user.getNickName());
                    vo.setUserAvatarUrl(user.getAvatarUrl());
                } else {
                    // 创建者未支付查看他人回答，显示匿名信息
                    vo.setUserNickName(DEFAULT_NICKNAME);
                    vo.setUserAvatarUrl(DEFAULT_AVATAR);
                }
            }
            
            // 获取该回答的所有回复（只有问题创建者的回复）
            LambdaQueryWrapper<Reply> replyWrapper = new LambdaQueryWrapper<>();
            replyWrapper.eq(Reply::getAnswerId, answer.getId())
                       .eq(Reply::getUserId, question.getCreatorId())  // 只查询问题创建者的回复
                       .orderByAsc(Reply::getCreateTime);
            List<Reply> replies = replyMapper.selectList(replyWrapper);
            
            // 构建回复VO列表
            List<ReplyVO> replyVOs = replies.stream().map(reply -> {
                ReplyVO replyVO = new ReplyVO();
                BeanUtils.copyProperties(reply, replyVO);
                
                // 获取回复者信息（这里一定是问题创建者）
                User replyUser = userMapper.selectById(reply.getUserId());
                if (replyUser != null) {
                    replyVO.setUserNickName(replyUser.getNickName());
                    replyVO.setUserAvatarUrl(replyUser.getAvatarUrl());
                }

                // 假设：A创建问题、B回答问题、A回复B的回答、C为用户
                // C用户视角：看不到回复的内容，但可以看回复的头像

                // 检查用户是否有权限查看回复
                boolean hasPermission = false;
                if (question.getCreatorId().equals(userId)) {
                    // 问题创建者直接有权限
                    hasPermission = true;
                } else {
                    assert user != null;
                    if (user.getId().equals(userId)) {
                        // 问题留言者直接有权限
                        hasPermission = true;
                    } else {
                        // 检查是否付费
                        LambdaQueryWrapper<QuestionViewPermission> permissionWrapper = new LambdaQueryWrapper<>();
                        permissionWrapper.eq(QuestionViewPermission::getQuestionId, questionId)
                                .eq(QuestionViewPermission::getUserId, userId);
                        QuestionViewPermission permission = questionViewPermissionMapper.selectOne(permissionWrapper);
    //                    System.out.println("permission = " + permission); // 调试
                        hasPermission = permission != null;
                    }
                }

                // 在处理回复时判断权限
                if (!hasPermission) {
                    replyVO.setContent(DEFAULT_REPLY);
                }
                
                return replyVO;
            }).collect(Collectors.toList());
            
            vo.setReplies(replyVOs);
            
            return vo;
        }).collect(Collectors.toList());
        
        // 构建问题详情
        QuestionDetailVO detailVO = new QuestionDetailVO();
        detailVO.setQuestion(questionVO);
        detailVO.setAnswers(answerVOs);
        
        return detailVO;
    }
    
    @Override
    public boolean deleteQuestion(Long questionId, Long userId) {
        // 1. 查询问题是否存在
        Question question = getById(questionId);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }
        
        // 2. 验证是否是问题创建者
        if (!question.getCreatorId().equals(userId)) {
            throw new BusinessException("只有问题创建者才能删除问题");
        }
        
        try {
            // 3. 删除问题相关的所有回复
            LambdaQueryWrapper<Answer> answerWrapper = new LambdaQueryWrapper<>();
            answerWrapper.eq(Answer::getQuestionId, questionId);
            List<Answer> answers = answerMapper.selectList(answerWrapper);
            
            for (Answer answer : answers) {
                // 删除回答相关的回复
                LambdaQueryWrapper<Reply> replyWrapper = new LambdaQueryWrapper<>();
                replyWrapper.eq(Reply::getAnswerId, answer.getId());
                replyMapper.delete(replyWrapper);
            }
            
            // 4. 删除问题的所有回答
            answerMapper.delete(answerWrapper);
            
            // 5. 删除问题
            removeById(questionId);
            
            return true;
        } catch (Exception e) {
            log.error("删除问题失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payForViewPermission(Long questionId, Long userId) {
        // 1. 查询问题是否存在
        Question question = getById(questionId);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }
        
        // 2. 验证是否是问题创建者
        if (!question.getCreatorId().equals(userId)) {
            throw new BusinessException("只有问题创建者才能支付查看权限");
        }
        
        // 3. 检查是否已支付
        if (Boolean.TRUE.equals(question.getIsPaid())) {
            throw new BusinessException("已经支付过了");
        }
        
        // 4. 更新支付状态
        question.setIsPaid(true);
        question.setUpdateTime(LocalDateTime.now());
        
        return updateById(question);
    }

    @Override
    public byte[] generateQRCodeByQuestionId(Long questionId, boolean ifReturn, HttpServletResponse response) {
        try {
            // 1. 获取 access_token
            String accessToken = weChatUtils.getAccessToken();

            // 2. 调用微信接口生成小程序码
            // 使用 getwxacodeunlimit 接口，它支持 scene 参数
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

            // 3. 构建参数
            Map<String, Object> params = new HashMap<>();
            params.put("scene", questionId.toString());     // 场景值，用于传递参数
            params.put("page", "pages/question/detail");    // 必须是已经发布的小程序存在的页面
            params.put("check_path", false);                // 不检查 page 是否存在
            params.put("env_version", "release");           // 要打开的小程序版本。正式版："release"体验版："trial"开发版："develop" todo
            params.put("width", 280);                       // 二维码宽度
            params.put("auto_color", true);                 // 自动配置线条颜色
            params.put("is_hyaline", false);                // 是否需要透明底色

            // 4. 发送请求获取二维码图片
            byte[] qrCodeBytes = restTemplate.postForObject(url, params, byte[].class);

            // 5. 检查返回结果是否是错误信息
            if (isErrorResponse(qrCodeBytes)) {
                log.error("生成小程序码失败：{}", new String(qrCodeBytes));
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return qrCodeBytes;
            }

            if (ifReturn){
                // 6. 设置响应头并返回图片
                response.setContentType("image/png");
                response.setHeader("Cache-Control", "max-age=2592000"); // 缓存 30 天
                response.getOutputStream().write(qrCodeBytes);
                response.getOutputStream().flush();
            }else {
                return qrCodeBytes;
            }

        } catch (Exception e) {
            log.error("生成二维码失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payForViewReplyPermission(Long questionId, Long userId) {
        // 1. 检查问题是否存在
        Question question = getById(questionId);
        if (question == null) {
            throw new BusinessException("问题不存在");
        }

        // 2. 检查是否已经付费
        LambdaQueryWrapper<QuestionViewPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionViewPermission::getQuestionId, questionId)
                .eq(QuestionViewPermission::getUserId, userId);
        if (questionViewPermissionMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已经付费过了");
        }

        // 3. 创建权限记录
        QuestionViewPermission permission = new QuestionViewPermission();
        permission.setQuestionId(questionId);
        permission.setUserId(userId);
        permission.setPaidTime(LocalDateTime.now());
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());

        return questionViewPermissionMapper.insert(permission) > 0;
    }

    /**
     * 检查返回的字节数组是否是错误信息
     */
    private boolean isErrorResponse(byte[] response) {
        try {
            // 尝试解析为 JSON，如果成功则说明是错误信息
            String result = new String(response);
            return result.contains("errcode");
        } catch (Exception e) {
            return false;
        }
    }
} 