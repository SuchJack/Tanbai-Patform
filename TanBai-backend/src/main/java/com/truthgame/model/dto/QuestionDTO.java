package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("问题创建请求")
public class QuestionDTO {
    @NotNull(message = "创建者ID不能为空")
    @ApiModelProperty("创建者ID")
    private Long creatorId;
    
    @NotBlank(message = "问题内容不能为空")
    @ApiModelProperty("问题内容")
    private String content;
} 