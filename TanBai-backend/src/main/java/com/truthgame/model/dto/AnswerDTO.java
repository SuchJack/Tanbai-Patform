package com.truthgame.model.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("回答创建请求")
public class AnswerDTO {
    @NotNull(message = "问题ID不能为空")
    @ApiModelProperty("问题ID")
    private Long questionId;
    
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @NotBlank(message = "回答内容不能为空")
    @ApiModelProperty("回答内容")
    private String content;
} 