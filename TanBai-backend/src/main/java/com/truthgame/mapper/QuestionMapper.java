package com.truthgame.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.truthgame.model.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
} 