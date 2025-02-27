package com.truthgame.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.truthgame.config.WxConfig;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.truthgame.service.impl.UserServiceImpl.WX_LOGIN;

public class UserServiceImplTest {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private WxConfig wxConfig;

    @Test
    public void testRegister() {
        String code = "0f36xM200AoSyT1WVD000RtHyh16xM26";
        Map<String, Object> map = new HashMap<>();
        map.put("appid", "xxx");
        map.put("secret", "xxx");
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpUtil.get(WX_LOGIN, map);

        JSONObject jsonObject = JSONUtil.parseObj(json);
        String openid = jsonObject.getStr("openid");
        System.out.println("openid = " + openid);
    }
  
}