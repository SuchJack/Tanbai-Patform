package com.truthgame.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.truthgame.common.Result;
import com.truthgame.model.dto.QuestionDTO;
import com.truthgame.model.entity.Question;
import com.truthgame.model.vo.QuestionDetailVO;
import com.truthgame.model.vo.QuestionWithAnswerCountVO;
import com.truthgame.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 问题管理接口
 */
@RestController
@RequestMapping("/questions")
@Slf4j
public class QuestionController {
    
    @Resource
    private QuestionService questionService;
    
    @PostMapping
    @ApiOperation("创建问题")
    public Result<Question> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        return Result.success(questionService.createQuestion(questionDTO));
    }
    
    @GetMapping("/my")
    @ApiOperation("获取我创建的问题列表")
    public Result<List<QuestionWithAnswerCountVO>> getMyQuestions() {
        long creatorId = StpUtil.getLoginIdAsLong();
        return Result.success(questionService.getMyQuestions(creatorId));
    }
    
    @GetMapping("/{questionId}/detail")
    @ApiOperation("获取问题详情")
    public Result<QuestionDetailVO> getQuestionDetail(@ApiParam("问题ID") @PathVariable Long questionId) {
        long userId = StpUtil.getLoginIdAsLong();
        System.out.println("userId = " + userId);
        return Result.success(questionService.getQuestionDetail(questionId, userId));
    }
    
    @DeleteMapping("/{questionId}")
    @ApiOperation("删除问题")
    public Result<Boolean> deleteQuestion(
        @ApiParam("问题ID") @PathVariable Long questionId,
        @ApiParam("当前用户ID") @RequestParam Long userId) {
        boolean success = questionService.deleteQuestion(questionId, userId);
        return Result.success(success);
    }
    
    @PostMapping("/{questionId}/pay")
    @ApiOperation("支付查看权限")
    public Result<Boolean> payForViewPermission(
        @ApiParam("问题ID") @PathVariable Long questionId,
        @ApiParam("当前用户ID") @RequestParam Long userId) {
        return Result.success(questionService.payForViewPermission(questionId, userId));
    }

    @PostMapping("/{questionId}/pay/reply")
    @ApiOperation("支付查看回复权限")
    public Result<Boolean> payForViewReplyPermission(
            @ApiParam("问题ID") @PathVariable Long questionId,
            @ApiParam("当前用户ID") @RequestParam Long userId) {
        return Result.success(questionService.payForViewReplyPermission(questionId, userId));
    }

} 