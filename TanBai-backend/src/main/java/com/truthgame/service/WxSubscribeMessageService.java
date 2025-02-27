package com.truthgame.service;

import com.truthgame.model.wx.ActivityNoticeTemplate;
import com.truthgame.model.wx.CommentNoticeTemplate;

public interface WxSubscribeMessageService {
    
    /**
     * 发送活动通知订阅消息
     * @param openId 用户openId
     * @param template 消息模板数据
     * @return 是否发送成功
     */
    boolean sendActivityNotice(String openId, ActivityNoticeTemplate template);

    /**
     * 发送留言通知订阅消息
     * @param openId 用户openId
     * @param template 消息模板数据
     * @return 是否发送成功
     */
    boolean sendCommentNotice(String openId, CommentNoticeTemplate template);
} 