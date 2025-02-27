package com.truthgame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.model.dto.ReplyDTO;
import com.truthgame.model.entity.Answer;
import com.truthgame.model.entity.Question;
import com.truthgame.model.entity.Reply;
import com.truthgame.model.entity.User;
import com.truthgame.exception.BusinessException;
import com.truthgame.mapper.AnswerMapper;
import com.truthgame.mapper.QuestionMapper;
import com.truthgame.mapper.ReplyMapper;
import com.truthgame.mapper.UserMapper;
import com.truthgame.service.ReplyService;
import com.truthgame.utils.SqlUtils;
import com.truthgame.utils.WeChatUtils;
import com.truthgame.model.vo.ReplyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
* @author i7 12700KF
* @description 针对表【reply(回复表)】的数据库操作Service实现
* @createDate 2025-01-10 15:36:14
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {
    
    private static final Logger log = LoggerFactory.getLogger(ReplyServiceImpl.class);
    
    @Resource
    private ReplyMapper replyMapper;
    
    @Resource
    private UserMapper userMapper;

    @Resource
    private AnswerMapper answerMapper;

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private WeChatUtils weChatUtils;

    @Override
    public ReplyVO createReply(ReplyDTO replyDTO) {
        String content = replyDTO.getContent();
        boolean b = SqlUtils.validSortField(content);
        if (!b) {
            throw new BusinessException("内容非法");
        }
        boolean b1 = weChatUtils.verifyContent(content);
        if (!b1) {
            throw new BusinessException("内容非法");
        }
        // 1. 获取回答信息
        Answer answer = answerMapper.selectById(replyDTO.getAnswerId());
        if (answer == null) {
            throw new BusinessException("回答不存在");
        }
        
        // 2. 获取问题信息
        Question question = questionMapper.selectById(answer.getQuestionId());
        if (question == null) {
            throw new BusinessException("问题不存在");
        }
        
        // 3. 验证当前用户是否是问题创建者
        if (!question.getCreatorId().equals(replyDTO.getUserId())) {
            throw new BusinessException("只有问题创建者才能回复");
        }
        
        // 4. 创建并保存回复
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyDTO, reply);
        reply.setCreateTime(LocalDateTime.now());
        reply.setUpdateTime(LocalDateTime.now());
        replyMapper.insert(reply);
        
        // 5. 构建返回VO
        ReplyVO replyVO = new ReplyVO();
        BeanUtils.copyProperties(reply, replyVO);
        
        // 6. 获取回复者信息（问题创建者）
        User user = userMapper.selectById(reply.getUserId());
        if (user != null) {
            replyVO.setUserNickName(user.getNickName());
            replyVO.setUserAvatarUrl(user.getAvatarUrl());
        }
        
        return replyVO;
    }
    
    @Override
    public boolean deleteReply(Long replyId, Long userId) {
        // 1. 查询回复是否存在
        Reply reply = getById(replyId);
        if (reply == null) {
            throw new BusinessException("回复不存在");
        }
        
        // 2. 验证是否是回复创建者
        if (!reply.getUserId().equals(userId)) {
            throw new BusinessException("只有回复创建者才能删除回复");
        }
        
        try {
            // 3. 删除回复
            return removeById(replyId);
        } catch (Exception e) {
            log.error("删除回复失败", e);
            return false;
        }
    }
}




