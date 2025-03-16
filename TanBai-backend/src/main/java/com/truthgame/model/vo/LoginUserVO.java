package com.truthgame.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@ApiModel("用户信息")
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVO {
    
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @ApiModelProperty("昵称")
    private String nickName;
    
    @ApiModelProperty("头像URL")
    private String avatarUrl;

    @ApiModelProperty("用户角色")
    private String userRole;

    @ApiModelProperty("用户openid")
    private String openId;
    
    @ApiModelProperty("认证token")
    private String tokenValue;
} 