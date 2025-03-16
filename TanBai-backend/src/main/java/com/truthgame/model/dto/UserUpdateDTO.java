package com.truthgame.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户信息更新请求")
public class UserUpdateDTO {
    
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @ApiModelProperty("昵称")
    private String nickName;
    
    @ApiModelProperty("头像URL")
    private String avatarUrl;
} 