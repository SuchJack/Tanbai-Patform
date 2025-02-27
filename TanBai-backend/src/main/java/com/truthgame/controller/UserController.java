package com.truthgame.controller;

import com.truthgame.common.Result;
import com.truthgame.model.dto.UserUpdateDTO;
import com.truthgame.service.UserService;
import com.truthgame.model.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Api(tags = "用户管理接口")
@Slf4j
public class UserController {
    
    @Resource
    private UserService userService;
    
    @PutMapping("/info")
    @ApiOperation("更新用户信息")
    public Result<UserVO> updateUserInfo(@RequestBody @Valid UserUpdateDTO updateDTO) {
        return Result.success(userService.updateUserInfo(updateDTO));
    }
} 