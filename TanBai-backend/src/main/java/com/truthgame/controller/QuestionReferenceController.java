package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.entity.QuestionReference;
import com.truthgame.service.QuestionReferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/references")
@Api(tags = "问题参考管理接口")
public class QuestionReferenceController {
    
    @Resource
    private QuestionReferenceService questionReferenceService;
    
    @GetMapping
    @ApiOperation("获取所有参考问题")
    public Result<List<QuestionReference>> getAllReferences() {
        return Result.success(questionReferenceService.getAllReferences());
    }
    
    @GetMapping("/category/{category}")
    @ApiOperation("根据类别获取参考问题")
    public Result<List<QuestionReference>> getReferencesByCategory(
        @ApiParam("问题类别") @PathVariable String category) {
        return Result.success(questionReferenceService.getReferencesByCategory(category));
    }
    
    @GetMapping("/random")
    @ApiOperation("随机获取参考问题")
    public Result<List<QuestionReference>> getRandomReferences(
            @ApiParam("获取数量") @RequestParam(defaultValue = "5") Integer count) {
        return Result.success(questionReferenceService.getRandomReferences(count));
    }
} 