package com.truthgame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.truthgame.model.dto.AnswerDTO;
import com.truthgame.model.entity.Answer;
import com.truthgame.model.vo.AnswerVO;
import java.util.List;

public interface AnswerService extends IService<Answer> {
    
    /**
     * 创建回答
     */
    Answer createAnswer(AnswerDTO answerDTO);
    
    /**
     * 获取我参与回答的问题列表
     */
    List<AnswerVO> getMyAnswers(Long userId);
    
    /**
     * 获取问题的所有回答
     */
    List<Answer> getAnswersByQuestionId(Long questionId);
    
    /**
     * 获取问题的所有回答（按问题ID查询）
     */
    List<Answer> getAnswersByQuestion(Long questionId);
    
    /**
     * 删除回答
     * @param answerId 回答ID
     * @param userId 当前用户ID
     * @return 是否删除成功
     */
    boolean deleteAnswer(Long answerId, Long userId);
} 