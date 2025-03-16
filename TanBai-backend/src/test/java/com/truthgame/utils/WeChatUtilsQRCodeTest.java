package com.truthgame.utils;

import com.truthgame.config.WxConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * WeChatUtils 小程序码生成测试类
 */
class WeChatUtilsQRCodeTest {

    private WeChatUtils weChatUtils;
    private WxConfig wxConfig;

    @BeforeEach
    void setUp() {
        // 手动创建对象
        weChatUtils = new WeChatUtils();
        wxConfig = new WxConfig();
        
        // 设置 AppID 和 AppSecret（请替换为你的测试值）
        wxConfig.setAppId("xxx");
        wxConfig.setAppSecret("xxx");
        
        // 手动注入依赖
        ReflectionTestUtils.setField(weChatUtils, "wxConfig", wxConfig);
    }

    @Test
    void testGenerateWxaCode() {
        try {
            // 测试生成小程序码
            byte[] qrCodeBytes = weChatUtils.generateWxaCode(
                    "2339",                  // 场景值，这里用问题ID
                    "pages/question/detail",   // 页面路径
                    280                        // 宽度
            );
            
            // 验证返回的字节数组不为空且长度大于0
            assert qrCodeBytes != null && qrCodeBytes.length > 0 : "生成的小程序码不应为空";
            
            // 将生成的小程序码保存到文件，方便查看
            saveQrCodeToFile(qrCodeBytes, "test_qrcode.jpg");
            
            System.out.println("小程序码生成成功，已保存到 test_qrcode.jpg");
        } catch (Exception e) {
            System.err.println("生成小程序码失败: " + e.getMessage());
            e.printStackTrace();
            assert false : "生成小程序码失败: " + e.getMessage();
        }
    }
    
    /**
     * 将二维码字节数组保存到文件
     */
    private void saveQrCodeToFile(byte[] qrCodeBytes, String fileName) throws IOException {
        // 创建测试输出目录
        Path testOutputDir = Paths.get("test-output");
        if (!Files.exists(testOutputDir)) {
            Files.createDirectories(testOutputDir);
        }
        
        // 保存文件
        Path filePath = testOutputDir.resolve(fileName);
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(qrCodeBytes);
        }
        
        System.out.println("小程序码已保存到: " + filePath.toAbsolutePath());
    }
} 