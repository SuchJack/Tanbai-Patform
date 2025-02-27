package com.truthgame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.truthgame.model.dto.QuestionDTO;
import com.truthgame.model.entity.Question;
import com.truthgame.model.vo.QuestionDetailVO;
import com.truthgame.model.vo.QuestionWithAnswerCountVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface QuestionService extends IService<Question> {
    // 创建问题
    Question createQuestion(QuestionDTO questionDTO);
    
    // 获取我创建的问题列表
    List<QuestionWithAnswerCountVO> getMyQuestions(Long creatorId);
    
    // 获取问题详情
    QuestionDetailVO getQuestionDetail(Long questionId, Long userId);
    
    /**
     * 删除问题
     * @param questionId 问题ID
     * @param userId 当前用户ID
     * @return 是否删除成功
     */
    boolean deleteQuestion(Long questionId, Long userId);
    
    /**
     * 支付查看权限
     * @param questionId 问题ID
     * @param userId 当前用户ID
     * @return 是否支付成功
     */
    boolean payForViewPermission(Long questionId, Long userId);

    byte[] generateQRCodeByQuestionId(Long questionId, boolean ifReturn, HttpServletResponse response);

    /**
     * 用户付费获取查看回复权限
     * @param questionId 问题ID
     * @param userId 用户ID
     * @return 是否支付成功
     */
    boolean payForViewReplyPermission(Long questionId, Long userId);
}