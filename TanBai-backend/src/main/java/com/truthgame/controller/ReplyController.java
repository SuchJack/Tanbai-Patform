package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.dto.ReplyDTO;
import com.truthgame.model.entity.Answer;
import com.truthgame.model.entity.User;
import com.truthgame.model.wx.CommentNoticeTemplate;
import com.truthgame.service.AnswerService;
import com.truthgame.service.ReplyService;
import com.truthgame.service.UserService;
import com.truthgame.service.WxSubscribeMessageService;
import com.truthgame.model.vo.ReplyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/replies")
@Api(tags = "回复管理接口")
public class ReplyController {
    
    @Resource
    private ReplyService replyService;
    @Resource
    private AnswerService answerService;
    @Resource
    private UserService userService;
    @Resource
    private WxSubscribeMessageService wxSubscribeMessageService;
    
    @PostMapping
    @ApiOperation("创建回复")
    public Result<ReplyVO> createReply(@Valid @RequestBody ReplyDTO replyDTO) {
        // 1. 创建回复
        ReplyVO replyVO = replyService.createReply(replyDTO);
        
        // 2. 获取回答信息
        Answer answer = answerService.getById(replyDTO.getAnswerId());
        if (answer != null) {
            // 3. 获取回答者信息
            User answerer = userService.getById(answer.getUserId());
            if (answerer != null && answerer.getOpenId() != null) {
                // 4. 获取回复者（问题创建者）信息
                User replier = userService.getById(replyDTO.getUserId());
                String replierName = replier != null ? replier.getNickName() : "匿名用户";

                // 判断：如果回复自己的回答时，不用发送订阅消息
                assert replier != null;
                if (replier.getId().equals(answer.getUserId())){
                    return Result.success(replyVO);
                }
                // 5. 发送订阅消息
                CommentNoticeTemplate template = CommentNoticeTemplate.build(
                    String.format("pages/question/detail?id=%d", answer.getQuestionId()),  // 跳转路径
                    replyDTO.getContent(),  // 留言内容
                    "你收到了一条回复，快去看看吧！"  // 温馨提示
                );
                wxSubscribeMessageService.sendCommentNotice(answerer.getOpenId(), template);
            }
        }
        
        return Result.success(replyVO);
    }
    
    @DeleteMapping("/{replyId}")
    @ApiOperation("删除回复")
    public Result<Boolean> deleteReply(
            @ApiParam("回复ID") @PathVariable Long replyId,
            @ApiParam("当前用户ID") @RequestParam Long userId) {
        boolean success = replyService.deleteReply(replyId, userId);
        return Result.success(success);
    }
} 