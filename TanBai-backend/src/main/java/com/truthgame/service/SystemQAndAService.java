package com.truthgame.service;

import com.truthgame.model.entity.SystemQAndA;
import java.util.List;

public interface SystemQAndAService {
    // 获取所有常见问题（按排序顺序）
    List<SystemQAndA> getAllQAndA();
    
    // 根据关键词搜索常见问题
    List<SystemQAndA> searchQAndA(String keyword);
} 