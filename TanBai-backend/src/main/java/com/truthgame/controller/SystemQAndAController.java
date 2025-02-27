package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.entity.SystemQAndA;
import com.truthgame.service.SystemQAndAService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/qa")
@Api(tags = "常见问题管理接口")
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