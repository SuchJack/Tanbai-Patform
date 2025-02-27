package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("微信登录请求")
public class WxLoginDTO {
    @NotBlank(message = "code不能为空")
    @ApiModelProperty("微信登录code")
    private String code;
} 