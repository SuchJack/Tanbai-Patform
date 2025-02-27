package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("question")
@ApiModel(description = "问题实体")
public class Question {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("问题ID")
    private Long id;
    
    @ApiModelProperty("创建者ID")
    private Long creatorId;
    
    @ApiModelProperty("问题内容")
    private String content;
    
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
    @ApiModelProperty("是否已支付查看权限")
    private Boolean isPaid;
} 