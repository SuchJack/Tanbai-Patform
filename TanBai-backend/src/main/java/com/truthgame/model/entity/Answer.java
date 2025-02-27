package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("answer")
@ApiModel(description = "回答实体")
public class Answer {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("回答ID")
    private Long id;
    
    @ApiModelProperty("问题ID")
    private Long questionId;
    
    @ApiModelProperty("回答者ID")
    private Long userId;
    
    @ApiModelProperty("回答内容")
    private String content;
    
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 