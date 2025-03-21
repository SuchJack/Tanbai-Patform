package com.truthgame.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.truthgame.common.Result;
import com.truthgame.model.dto.WxLoginDTO;
import com.truthgame.model.entity.User;
import com.truthgame.model.vo.LoginUserVO;
import com.truthgame.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private UserService userService;

    @SaIgnore
    @PostMapping("/wx/login")
    @ApiOperation("微信登录")
    public Result<LoginUserVO> wxLogin(@Valid @RequestBody WxLoginDTO loginDTO) {
        log.info("收到微信登录code：{}", loginDTO.getCode());

        User user = userService.wxLogin(loginDTO);

        StpUtil.login(user.getId());

        String tokenValue = StpUtil.getTokenValue();

        LoginUserVO userVO = LoginUserVO.builder()
                .userId(user.getId())
                .openId(user.getOpenId())
                .tokenValue(tokenValue)
                .avatarUrl(user.getAvatarUrl())
                .nickName(user.getNickName())
                .build();
        return Result.success(userVO);
    }
    
    /**
     * 获取当前登录用户
     * @return LoginUserVO
     */
    @GetMapping("/get/login")
    @ApiOperation("获取当前登录用户")
    public Result<LoginUserVO> getLoginUserVO() {
        LoginUserVO loginUserVO = userService.getLoginUserVO();
        return Result.success(loginUserVO);
    }
} 