package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.mapper.AnswerReferenceMapper;
import com.truthgame.model.entity.AnswerReference;
import com.truthgame.service.AnswerReferenceService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AnswerReferenceServiceImpl extends ServiceImpl<AnswerReferenceMapper, AnswerReference> 
        implements AnswerReferenceService {

    @Override
    public List<AnswerReference> getAllReferences() {
        return list();
    }

    @Override
    public List<AnswerReference> getReferencesByCategory(String category) {
        LambdaQueryWrapper<AnswerReference> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AnswerReference::getCategory, category);
        return list(wrapper);
    }

    @Override
    public List<AnswerReference> getRandomReferences(Integer count) {
        List<AnswerReference> allReferences = list();
        List<AnswerReference> randomReferences = new ArrayList<>();
        Random random = new Random();
        
        // 如果请求数量大于总数，返回所有
        if (count >= allReferences.size()) {
            return allReferences;
        }
        
        // 随机选择指定数量的回答
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(allReferences.size());
            randomReferences.add(allReferences.get(index));
            allReferences.remove(index);
        }
        
        return randomReferences;
    }
} 