package com.truthgame.service;

public interface ImageSecCheckService {
    /**
     * 检查图片是否包含违规内容
     * @param imageBytes 图片字节数组
     * @return true 如果图片合法，false 如果图片包含违规内容
     */
    boolean checkImage(byte[] imageBytes);
} 