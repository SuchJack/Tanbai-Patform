package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.model.dto.AnswerDTO;
import com.truthgame.model.entity.Answer;
import com.truthgame.model.entity.Reply;
import com.truthgame.model.entity.Question;
import com.truthgame.model.entity.User;
import com.truthgame.exception.BusinessException;
import com.truthgame.mapper.AnswerMapper;
import com.truthgame.mapper.ReplyMapper;
import com.truthgame.mapper.QuestionMapper;
import com.truthgame.mapper.UserMapper;
import com.truthgame.service.AnswerService;
import com.truthgame.utils.SqlUtils;
import com.truthgame.utils.WeChatUtils;
import com.truthgame.model.vo.AnswerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
    
    private static final Logger log = LoggerFactory.getLogger(AnswerServiceImpl.class);
    
    @Resource
    private AnswerMapper answerMapper;
    
    @Resource
    private ReplyMapper replyMapper;
    
    @Resource
    private QuestionMapper questionMapper;
    
    @Resource
    private UserMapper userMapper;

    @Resource
    private WeChatUtils weChatUtils;
    
    @Override
    public Answer createAnswer(AnswerDTO answerDTO) {
        String content = answerDTO.getContent();
        boolean b = SqlUtils.validSortField(content);
        if (!b) {
            throw new BusinessException("内容非法");
        }
        boolean b1 = weChatUtils.verifyContent(content);
        if (!b1) {
            throw new BusinessException("内容非法");
        }
        Answer answer = new Answer();
        BeanUtils.copyProperties(answerDTO, answer);
        answer.setCreateTime(LocalDateTime.now());
        save(answer);  // 使用 MyBatis-Plus 的 save 方法
        return answer;
    }
    
    @Override
    public List<AnswerVO> getMyAnswers(Long userId) {
        // 1. 获取我的所有回答
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getUserId, userId)
               .orderByDesc(Answer::getCreateTime);
        List<Answer> answers = list(wrapper);
        
        // 2. 构建VO对象
        return answers.stream().map(answer -> {
            AnswerVO vo = new AnswerVO();
            vo.setAnswer(answer);
            
            // 3. 获取问题信息
            Question question = questionMapper.selectById(answer.getQuestionId());
            if (question != null) {
                vo.setQuestionContent(question.getContent());
                vo.setQuestionCreateTime(question.getCreateTime());
                
                // 4. 获取问题创建者信息
                User creator = userMapper.selectById(question.getCreatorId());
                if (creator != null) {
                    vo.setQuestionCreatorNickName(creator.getNickName());
                    vo.setQuestionCreatorAvatarUrl(creator.getAvatarUrl());
                }
            }
            
            return vo;
        }).collect(Collectors.toList());
    }
    
    @Override
    public List<Answer> getAnswersByQuestionId(Long questionId) {
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getQuestionId, questionId)
               .orderByDesc(Answer::getCreateTime);
        return list(wrapper);
    }
    
    @Override
    public List<Answer> getAnswersByQuestion(Long questionId) {
        // 这个方法和 getAnswersByQuestionId 功能相同，为了保持兼容性
        return getAnswersByQuestionId(questionId);
    }
    
    @Override
    public boolean deleteAnswer(Long answerId, Long userId) {
        // 1. 查询回答是否存在
        Answer answer = getById(answerId);
        if (answer == null) {
            throw new BusinessException("回答不存在");
        }
        
        // 2. 查询问题信息，判断当前用户是否是问题创建者
        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question == null) {
            throw new BusinessException("问题不存在");
        }
        
        // 3. 验证权限：只有回答创建者或问题创建者可以删除回答
        boolean isQuestionCreator = question.getCreatorId().equals(userId);
        boolean isAnswerCreator = answer.getUserId().equals(userId);
        
        if (!isQuestionCreator && !isAnswerCreator) {
            throw new BusinessException("没有权限删除此回答");
        }
        
        try {
            // 4. 删除回答相关的所有回复
            LambdaQueryWrapper<Reply> replyWrapper = new LambdaQueryWrapper<>();
            replyWrapper.eq(Reply::getAnswerId, answerId);
            replyMapper.delete(replyWrapper);
            
            // 5. 删除回答
            return removeById(answerId);
        } catch (Exception e) {
            log.error("删除回答失败", e);
            return false;
        }
    }
} 