package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.entity.AnswerReference;
import com.truthgame.service.AnswerReferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/answer/reference")
@Api(tags = "回答参考接口")
public class AnswerReferenceController {
    
    @Resource
    private AnswerReferenceService answerReferenceService;
    
    @GetMapping
    @ApiOperation("获取所有参考回答")
    public Result<List<AnswerReference>> getAllReferences() {
        return Result.success(answerReferenceService.getAllReferences());
    }
    
    @GetMapping("/category/{category}")
    @ApiOperation("根据类别获取参考回答")
    public Result<List<AnswerReference>> getReferencesByCategory(
            @ApiParam("回答类别") @PathVariable String category) {
        return Result.success(answerReferenceService.getReferencesByCategory(category));
    }
    
    @GetMapping("/random")
    @ApiOperation("随机获取参考回答")
    public Result<List<AnswerReference>> getRandomReferences(
            @ApiParam("获取数量") @RequestParam(defaultValue = "5") Integer count) {
        return Result.success(answerReferenceService.getRandomReferences(count));
    }
} 