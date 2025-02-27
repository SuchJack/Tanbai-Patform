package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.dto.QuestionDTO;
import com.truthgame.model.entity.Question;
import com.truthgame.service.QuestionService;
import com.truthgame.model.vo.QuestionDetailVO;
import com.truthgame.model.vo.QuestionWithAnswerCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/questions")
@Api(tags = "问题管理接口")
@Slf4j
public class QuestionController {
    
    @Resource
    private QuestionService questionService;
    
    @PostMapping
    @ApiOperation("创建问题")
    public Result<Question> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        return Result.success(questionService.createQuestion(questionDTO));
    }
    
    @GetMapping("/my/{creatorId}")
    @ApiOperation("获取我创建的问题列表")
    public Result<List<QuestionWithAnswerCountVO>> getMyQuestions(
        @ApiParam("创建者ID") @PathVariable Long creatorId) {
        return Result.success(questionService.getMyQuestions(creatorId));
    }
    
    @GetMapping("/{questionId}/detail")
    @ApiOperation("获取问题详情")
    public Result<QuestionDetailVO> getQuestionDetail(
        @ApiParam("问题ID") @PathVariable Long questionId,
        @ApiParam("当前用户ID") @RequestParam Long userId) {
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

    /**
     * 生成小程序码
     * @param questionId
     * @param response
     * 官方文档：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/qr-code.html
     */
    @GetMapping("/qrcode/{questionId}")
    @ApiOperation("生成问题二维码")
    public void generateQRCode(@PathVariable Long questionId, HttpServletResponse response) {
        questionService.generateQRCodeByQuestionId(questionId,true, response);
    }

} 