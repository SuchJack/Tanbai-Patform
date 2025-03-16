package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 客服信息表
 * @TableName customer_service
 */
@TableName(value ="customer_service")
@Data
public class CustomerService implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 客服微信
     */
    private String wechat;

    /**
     * 客服邮箱
     */
    private String email;

    /**
     * 工作时间
     */
    private String workingHours;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}