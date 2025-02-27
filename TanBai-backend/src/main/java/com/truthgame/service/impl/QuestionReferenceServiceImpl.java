package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.truthgame.model.entity.QuestionReference;
import com.truthgame.mapper.QuestionReferenceMapper;
import com.truthgame.service.QuestionReferenceService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
public class QuestionReferenceServiceImpl implements QuestionReferenceService {
    
    @Resource
    private QuestionReferenceMapper questionReferenceMapper;
    
    @Override
    public List<QuestionReference> getAllReferences() {
        LambdaQueryWrapper<QuestionReference> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(QuestionReference::getSortOrder);
        return questionReferenceMapper.selectList(wrapper);
    }
    
    @Override
    public List<QuestionReference> getReferencesByCategory(String category) {
        LambdaQueryWrapper<QuestionReference> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuestionReference::getCategory, category)
               .orderByAsc(QuestionReference::getSortOrder);
        return questionReferenceMapper.selectList(wrapper);
    }
    
    @Override
    public List<QuestionReference> getRandomReferences(Integer count) {
        // 1. 先获取 sort_order = 1 的问题
        LambdaQueryWrapper<QuestionReference> priorityWrapper = new LambdaQueryWrapper<>();
        priorityWrapper.eq(QuestionReference::getSortOrder, 1);
        List<QuestionReference> priorityQuestions = questionReferenceMapper.selectList(priorityWrapper);
        
        // 2. 获取其他问题
        LambdaQueryWrapper<QuestionReference> otherWrapper = new LambdaQueryWrapper<>();
        otherWrapper.ne(QuestionReference::getSortOrder, 1);
        List<QuestionReference> otherQuestions = questionReferenceMapper.selectList(otherWrapper);
        
        // 如果请求数量小于等于0，返回空列表
        if (count <= 0) {
            return Collections.emptyList();
        }
        
        // 如果优先问题加其他问题的总数小于等于请求数量，返回所有问题（优先问题在前）
        if (priorityQuestions.size() + otherQuestions.size() <= count) {
            priorityQuestions.addAll(otherQuestions);
            return priorityQuestions;
        }
        
        // 3. 随机选择其他问题
        Random random = new Random();
        int remainingCount = count - priorityQuestions.size();
        if (remainingCount > 0) {
            // 随机打乱其他问题列表
            for (int i = 0; i < remainingCount; i++) {
                int j = i + random.nextInt(otherQuestions.size() - i);
                QuestionReference temp = otherQuestions.get(i);
                otherQuestions.set(i, otherQuestions.get(j));
                otherQuestions.set(j, temp);
            }
            // 添加随机选择的其他问题
            priorityQuestions.addAll(otherQuestions.subList(0, remainingCount));
        }
        
        return priorityQuestions;
    }
} 