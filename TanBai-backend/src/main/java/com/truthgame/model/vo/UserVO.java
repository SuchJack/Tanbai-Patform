package com.truthgame.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户信息")
public class UserVO {
    
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @ApiModelProperty("昵称")
    private String nickName;
    
    @ApiModelProperty("头像URL")
    private String avatarUrl;
    
    @ApiModelProperty("认证token")
    private String token;
} 