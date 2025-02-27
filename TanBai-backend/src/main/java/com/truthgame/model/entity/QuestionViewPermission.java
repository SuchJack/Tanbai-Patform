package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("question_view_permission")
public class QuestionViewPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long questionId;
    
    private Long userId;
    
    private LocalDateTime paidTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}