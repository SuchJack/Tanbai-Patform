package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("system_q_and_a")
@ApiModel(description = "系统常见问题实体")
public class SystemQAndA {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Long id;
    
    @ApiModelProperty("问题内容")
    private String question;
    
    @ApiModelProperty("答案内容")
    private String answer;
    
    @ApiModelProperty("排序顺序")
    private Integer sortOrder;
    
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 