package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.entity.SystemQAndA;
import com.truthgame.service.SystemQAndAService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 常见问题管理接口
 */
@RestController
@RequestMapping("/qa")
public class SystemQAndAController {
    
    @Resource
    private SystemQAndAService systemQAndAService;
    
    @GetMapping
    @ApiOperation("获取所有常见问题")
    public Result<List<SystemQAndA>> getAllQAndA() {
        return Result.success(systemQAndAService.getAllQAndA());
    }
    
    @GetMapping("/search")
    @ApiOperation("搜索常见问题")
    public Result<List<SystemQAndA>> searchQAndA(
        @ApiParam("搜索关键词") @RequestParam String keyword) {
        return Result.success(systemQAndAService.searchQAndA(keyword));
    }
} 