package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录响应")
public class LoginResponseDTO {
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @ApiModelProperty("token")
    private String token;
    
    @ApiModelProperty("是否新用户")
    private Boolean isNewUser;
} 