package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.truthgame.model.entity.SystemQAndA;
import com.truthgame.mapper.SystemQAndAMapper;
import com.truthgame.service.SystemQAndAService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemQAndAServiceImpl implements SystemQAndAService {
    
    @Resource
    private SystemQAndAMapper systemQAndAMapper;
    
    @Override
    public List<SystemQAndA> getAllQAndA() {
        LambdaQueryWrapper<SystemQAndA> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SystemQAndA::getSortOrder);
        return systemQAndAMapper.selectList(wrapper);
    }
    
    @Override
    public List<SystemQAndA> searchQAndA(String keyword) {
        LambdaQueryWrapper<SystemQAndA> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(SystemQAndA::getQuestion, keyword)
               .or()
               .like(SystemQAndA::getAnswer, keyword)
               .orderByAsc(SystemQAndA::getSortOrder);
        return systemQAndAMapper.selectList(wrapper);
    }
} 