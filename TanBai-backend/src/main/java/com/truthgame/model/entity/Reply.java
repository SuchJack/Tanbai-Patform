package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("reply")
@ApiModel(description = "回复实体")
public class Reply {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("回复ID")
    private Long id;
    
    @ApiModelProperty("回答ID")
    private Long answerId;
    
    @ApiModelProperty("回复者ID")
    private Long userId;
    
    @ApiModelProperty("回复内容")
    private String content;
    
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 