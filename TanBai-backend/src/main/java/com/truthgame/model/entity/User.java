package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 微信unionId
     */
    private String unionId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 会话密钥
     */
    private String sessionKey;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 
     */
    private String ip;

    /**
     * 
     */
    private String ipLocation;

    /**
     * 
     */
    private String os;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 
     */
    private String browser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}