package com.truthgame.utils;

import com.truthgame.config.WxMpConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * WeChatUtils 测试类
 * 使用手动注入而不是 Mockito 来避免版本兼容性问题
 */
class WeChatUtilsTest {

    private WeChatUtils weChatUtils;
    private WxMpConfiguration wxMpConfiguration;

    @BeforeEach
    void setUp() {
        // 手动创建对象
        weChatUtils = new WeChatUtils();
        wxMpConfiguration = new WxMpConfiguration();
        
        // 设置 AppID 和 AppSecret（请替换为你的测试值）
        wxMpConfiguration.setAppId("xxxx");
        wxMpConfiguration.setAppSecret("xxxx");
        
        // 手动注入依赖
        ReflectionTestUtils.setField(weChatUtils, "wxMpConfiguration", wxMpConfiguration);
    }

    @Test
    void testGetAccessToken() {
        try {
            String accessToken = weChatUtils.getAccessToken();
            System.out.println("获取到的access_token: " + accessToken);
            
            // 简单验证 accessToken 不为空
            assert accessToken != null && !accessToken.isEmpty() : "access_token 不应为空";
        } catch (Exception e) {
            System.err.println("获取access_token失败: " + e.getMessage());
            e.printStackTrace();
            assert false : "获取access_token失败: " + e.getMessage();
        }
    }

    @Test
    void testVerifyContent() {
        try {
            // 测试正常内容
            boolean result = weChatUtils.verifyContent("这是一段正常的内容");
            System.out.println("正常内容检查结果: " + result);
            
            // 简单验证结果为 true
            assert result : "内容检查应返回 true";
        } catch (Exception e) {
            System.err.println("内容检查失败: " + e.getMessage());
            e.printStackTrace();
            assert false : "内容检查失败: " + e.getMessage();
        }
    }
} 