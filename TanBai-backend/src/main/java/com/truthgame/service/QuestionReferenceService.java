package com.truthgame.service;

import com.truthgame.model.entity.QuestionReference;
import java.util.List;

public interface QuestionReferenceService {
    // 获取所有参考问题
    List<QuestionReference> getAllReferences();
    
    // 根据类别获取参考问题
    List<QuestionReference> getReferencesByCategory(String category);
    
    // 随机获取指定数量的参考问题
    List<QuestionReference> getRandomReferences(Integer count);
} 