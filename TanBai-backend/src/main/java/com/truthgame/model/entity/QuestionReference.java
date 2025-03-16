package com.truthgame.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题参考表
 * @TableName question_reference
 */
@TableName(value ="question_reference")
@Data
public class QuestionReference implements Serializable {
    /**
     * 参考问题ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 问题类别（如：爱情、友情等）
     */
    private String category;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}