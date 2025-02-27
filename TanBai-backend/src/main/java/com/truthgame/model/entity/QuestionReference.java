package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("question_reference")
@ApiModel(description = "问题参考实体")
public class QuestionReference {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("参考问题ID")
    private Long id;
    
    @ApiModelProperty("问题内容")
    private String content;
    
    @ApiModelProperty("问题类别")
    private String category;
    
    @ApiModelProperty("排序顺序")
    private Integer sortOrder;
    
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 