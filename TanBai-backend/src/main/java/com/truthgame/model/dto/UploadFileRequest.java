package com.truthgame.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 */
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;

    /**
     * 用户标识
     */
    private String openId;

    private static final long serialVersionUID = 1L;
}