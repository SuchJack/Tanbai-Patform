package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 问题表
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     * 问题ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否已支付查看权限(0-未支付 1-已支付)
     */
    private Integer isPaid;

    /**
     * 是否已支付查看回复权限(0-未支付 1-已支付)
     */
    private Integer seeReply;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}