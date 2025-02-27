package com.truthgame.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.truthgame.model.wx.ActivityNoticeTemplate;
import com.truthgame.model.wx.CommentNoticeTemplate;
import com.truthgame.service.WxSubscribeMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class WxSubscribeMessageServiceImpl implements WxSubscribeMessageService {

    private static final String ACTIVITY_NOTICE_TEMPLATE_ID = "s-ge-X2pNg0FDJTICfr-AeHLFUpbzxYSy-aNzg7998o";
    private static final String COMMENT_NOTICE_TEMPLATE_ID = "r10K00ltI42x0K3dkCW46tpmYgmLeipRr-La835wnJo";
    private static final String SUBSCRIBE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";

    @Resource
    private WxMpService wxMpService;

    @Override
    public boolean sendActivityNotice(String openId, ActivityNoticeTemplate template) {
        try {
            // 构建请求数据
            JSONObject messageJson = new JSONObject();
            messageJson.put("touser", openId);
            messageJson.put("template_id", ACTIVITY_NOTICE_TEMPLATE_ID);
            messageJson.put("page", template.getPath());
            messageJson.put("miniprogram_state", "formal"); // developer为开发版；trial为体验版；formal为正式版
            
            // 构建模板数据
            JSONObject data = new JSONObject();
            
            // 活动标题
            JSONObject thing16 = new JSONObject();
            thing16.put("value", template.getTitle().getValue());
            data.put("thing16", thing16);
            
            // 活动名称
            JSONObject thing11 = new JSONObject();
            thing11.put("value", template.getName().getValue());
            data.put("thing11", thing11);
            
            // 活动内容
            JSONObject thing4 = new JSONObject();
            thing4.put("value", template.getContent().getValue());
            data.put("thing4", thing4);
            
            messageJson.put("data", data);
            
            // 使用SDK发送订阅消息
            String responseContent = wxMpService.post(SUBSCRIBE_MSG_URL, messageJson.toJSONString());
            
            // 解析响应
            JSONObject response = JSONObject.parseObject(responseContent);
            int errcode = response.getIntValue("errcode");
            
            if (errcode == 0) {
                log.info("发送活动通知订阅消息成功，openId: {}", openId);
                return true;
            } else {
                log.error("发送活动通知订阅消息失败，openId: {}, errcode: {}, errmsg: {}", 
                    openId, errcode, response.getString("errmsg"));
                return false;
            }
            
        } catch (WxErrorException e) {
            log.error("发送活动通知订阅消息失败，openId: {}, error: {}", openId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendCommentNotice(String openId, CommentNoticeTemplate template) {
        try {
            // 构建请求数据
            JSONObject messageJson = new JSONObject();
            messageJson.put("touser", openId);
            messageJson.put("template_id", COMMENT_NOTICE_TEMPLATE_ID);
            messageJson.put("page", template.getPath());
            messageJson.put("miniprogram_state", "formal"); // developer为开发版；trial为体验版；formal为正式版
            
            // 构建模板数据
            JSONObject data = new JSONObject();
            
            // 留言内容
            JSONObject thing3 = new JSONObject();
            thing3.put("value", template.getContent().getValue());
            data.put("thing3", thing3);
            
            // 温馨提示
            JSONObject thing4 = new JSONObject();
            thing4.put("value", template.getTips().getValue());
            data.put("thing4", thing4);
            
            messageJson.put("data", data);
            
            // 使用SDK发送订阅消息
            String responseContent = wxMpService.post(SUBSCRIBE_MSG_URL, messageJson.toJSONString());
            
            // 解析响应
            JSONObject response = JSONObject.parseObject(responseContent);
            int errcode = response.getIntValue("errcode");
            
            if (errcode == 0) {
                log.info("发送留言通知订阅消息成功，openId: {}", openId);
                return true;
            } else {
                log.error("发送留言通知订阅消息失败，openId: {}, errcode: {}, errmsg: {}", 
                    openId, errcode, response.getString("errmsg"));
                return false;
            }
            
        } catch (WxErrorException e) {
            log.error("发送留言通知订阅消息失败，openId: {}, error: {}", openId, e.getMessage(), e);
            return false;
        }
    }
} 