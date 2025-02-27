package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.dto.AnswerDTO;
import com.truthgame.model.entity.Answer;
import com.truthgame.model.entity.Question;
import com.truthgame.model.entity.User;
import com.truthgame.model.wx.CommentNoticeTemplate;
import com.truthgame.service.AnswerService;
import com.truthgame.service.QuestionService;
import com.truthgame.service.UserService;
import com.truthgame.service.WxSubscribeMessageService;
import com.truthgame.model.vo.AnswerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/answers")
@Api(tags = "回答管理接口")
public class AnswerController {
    
    @Resource
    private AnswerService answerService;
    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    private WxSubscribeMessageService wxSubscribeMessageService;
    
    @PostMapping
    @ApiOperation("创建回答")
    public Result<Answer> createAnswer(@Valid @RequestBody AnswerDTO answerDTO) {
        // 1. 创建回答
        Answer answer = answerService.createAnswer(answerDTO);

        // 2. 获取问题信息
        Question question = questionService.getById(answerDTO.getQuestionId());
        if (question != null) {
            // 3. 获取问题创建者信息
            User questionCreator = userService.getById(question.getCreatorId());
            if (questionCreator != null && questionCreator.getOpenId() != null) {
                // 4. 获取回答者信息
                User answerer = userService.getById(answerDTO.getUserId());
                String answererName = answerer != null ? answerer.getNickName() : "匿名用户";

                // 判断：如果回答自己的问题时，不用发送订阅消息
                assert answerer != null;
                if (answerer.getId().equals(question.getCreatorId())){
                    return Result.success(answer);
                }

                // 5. 发送订阅消息
                CommentNoticeTemplate template = CommentNoticeTemplate.build(
                    String.format("pages/question/detail?id=%d", question.getId()),  // 跳转路径
                    answerDTO.getContent(),  // 留言内容
                    "你收到了一个回答，快去看看吧！"  // 温馨提示
                );
                wxSubscribeMessageService.sendCommentNotice(questionCreator.getOpenId(), template);
            }
        }
        
        return Result.success(answer);
    }
    
    @GetMapping("/my/{userId}")
    @ApiOperation("获取我参与回答的问题列表")
    public Result<List<AnswerVO>> getMyAnswers(
        @ApiParam("用户ID") @PathVariable Long userId) {
        return Result.success(answerService.getMyAnswers(userId));
    }
    
    @GetMapping("/question/{questionId}")
    @ApiOperation("获取问题的所有回答")
    public Result<List<Answer>> getAnswersByQuestion(
        @ApiParam("问题ID") @PathVariable Long questionId) {
        return Result.success(answerService.getAnswersByQuestion(questionId));
    }
    
    @DeleteMapping("/{answerId}")
    @ApiOperation("删除回答")
    public Result<Boolean> deleteAnswer(
            @ApiParam("回答ID") @PathVariable Long answerId,
            @ApiParam("当前用户ID") @RequestParam Long userId) {
        boolean success = answerService.deleteAnswer(answerId, userId);
        return Result.success(success);
    }
} 