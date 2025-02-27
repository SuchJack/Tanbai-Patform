package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("user")
@ApiModel(description = "用户实体")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("用户ID")
    private Long id;
    
    @ApiModelProperty("微信openId")
    private String openId;
    
    @ApiModelProperty("微信unionId")
    private String unionId;
    
    @ApiModelProperty("昵称")
    private String nickName;
    
    @ApiModelProperty("头像URL")
    private String avatarUrl;
    
    @ApiModelProperty("会话密钥")
    private String sessionKey;
    
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 