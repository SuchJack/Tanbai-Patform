package com.truthgame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.truthgame.model.entity.AnswerReference;
import java.util.List;

public interface AnswerReferenceService extends IService<AnswerReference> {
    /**
     * 获取所有参考回答
     */
    List<AnswerReference> getAllReferences();
    
    /**
     * 根据类别获取参考回答
     */
    List<AnswerReference> getReferencesByCategory(String category);
    
    /**
     * 随机获取参考回答
     */
    List<AnswerReference> getRandomReferences(Integer count);
} 