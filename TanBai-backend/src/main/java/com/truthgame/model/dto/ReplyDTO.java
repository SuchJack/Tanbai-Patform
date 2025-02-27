package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("回复创建请求")
public class ReplyDTO {
    
    @NotNull(message = "回答ID不能为空")
    @ApiModelProperty("回答ID")
    private Long answerId;
    
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @NotBlank(message = "回复内容不能为空")
    @ApiModelProperty("回复内容")
    private String content;
} 