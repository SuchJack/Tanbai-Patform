package com.truthgame.controller;

import com.truthgame.Properties.JwtProperties;
import com.truthgame.common.Result;
import com.truthgame.constant.JwtClaimsConstant;
import com.truthgame.model.dto.WxLoginDTO;
import com.truthgame.model.entity.User;
import com.truthgame.model.entity.UserVO;
import com.truthgame.service.UserService;
import com.truthgame.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import com.truthgame.annotation.NoAuth;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Api(tags = "认证接口")
@Slf4j
public class AuthController {
    
    @Resource
    private UserService userService;

    @Resource
    private JwtProperties jwtProperties;
    
    @NoAuth
    @PostMapping("/wx/login")
    @ApiOperation("微信登录")
    public Result<UserVO> wxLogin(@Valid @RequestBody WxLoginDTO loginDTO) {
        log.info("微信用户登录：{}",loginDTO.getCode());

        User user = userService.wxLogin(loginDTO);

        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .openId(user.getOpenId())
                .token(token)
                .avatarUrl(user.getAvatarUrl())
                .nickName(user.getNickName())
                .build();
        return Result.success(userVO);
    }
} 