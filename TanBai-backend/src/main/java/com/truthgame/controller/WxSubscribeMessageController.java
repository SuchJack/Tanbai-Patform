package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.wx.ActivityNoticeTemplate;
import com.truthgame.model.wx.CommentNoticeTemplate;
import com.truthgame.service.WxSubscribeMessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wx/subscribe-message")
public class WxSubscribeMessageController {

    @Resource
    private WxSubscribeMessageService wxSubscribeMessageService;

    @PostMapping("/send-activity-notice")
    public Result<Boolean> sendActivityNotice(
            @RequestParam String openId,
            @RequestParam String title,
            @RequestParam String name,
            @RequestParam String content) {
        
        ActivityNoticeTemplate template = ActivityNoticeTemplate.build("pages/index/index",title, name, content);
        boolean success = wxSubscribeMessageService.sendActivityNotice(openId, template);
        
        return Result.success(success);
    }

    @PostMapping("/send-comment-notice")
    public Result<Boolean> sendCommentNotice(
            @RequestParam String openId,
            @RequestParam String content,
            @RequestParam String tips) {
        
        CommentNoticeTemplate template = CommentNoticeTemplate.build("pages/index/index",content, tips);
        boolean success = wxSubscribeMessageService.sendCommentNotice(openId, template);
        
        return Result.success(success);
    }
} 