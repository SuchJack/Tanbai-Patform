package com.truthgame.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.truthgame.config.WxMpConfiguration;
import com.truthgame.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 */
@Component
@Slf4j
public class WeChatUtils {

    @Resource
    private WxMpConfiguration wxMpConfiguration;

    /**
     * 内容安全检查
     * @param content 需要检查的内容
     * @return 内容是否安全
     */
    public boolean verifyContent(String content) {
        // 调用微信内容安全检查接口
        String accessToken = this.getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;

        // 构建请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);

        try {
            // 使用Hutool的HttpUtil发送POST请求
            String result = HttpUtil.post(url, JSONUtil.toJsonStr(params));
            JSONObject json = JSONUtil.parseObj(result);
            if (json.getInt("errcode") == 0) {
                return true;
            }
            // 检查返回结果
            if (json.getInt("errcode") != 0) {
                throw new BusinessException("内容非法：" + json.getStr("errmsg"));
            }
        } catch (Exception e) {
            log.error("内容安全检查失败", e);
            throw new BusinessException("内容安全检查失败，请稍后重试");
        }
        return false;
    }

    /**
     * 获取微信接口调用凭证
     */
    public String getAccessToken() {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                wxMpConfiguration.getAppId(), wxMpConfiguration.getAppSecret());

        // 使用Hutool的HttpUtil发送GET请求
        String result = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(result);

        if (json.containsKey("access_token")) {
            return json.getStr("access_token");
        } else {
            log.error("获取access_token失败：{}", result);
            throw new BusinessException("获取access_token失败");
        }
    }

    /**
     * 生成小程序码
     * @param sceneId 场景值，用于传递参数
     * @param page 跳转的页面路径
     * @param width 二维码宽度
     * @return 小程序码图片字节数组
     */
    public byte[] generateWxaCode(String sceneId, String page, int width) {
        // 1. 获取access_token
        String accessToken = getAccessToken();

        // 2. 构建请求URL
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

        // 3. 构建参数
        Map<String, Object> params = new HashMap<>();
        params.put("scene", sceneId);                // 场景值，用于传递参数
        params.put("page", page);                    // 必须是已经发布的小程序存在的页面
        params.put("check_path", false);             // 不检查 page 是否存在
        params.put("env_version", wxMpConfiguration.getEnvVersion());        // 要打开的小程序版本。正式版："release"体验版："trial"开发版："develop"
        params.put("width", width);                  // 二维码宽度
        params.put("auto_color", true);              // 自动配置线条颜色
        params.put("is_hyaline", false);             // 是否需要透明底色

        // 4. 发送请求获取二维码图片
        try {
            byte[] qrCodeBytes = HttpRequest.post(url)
                    .body(JSONUtil.toJsonStr(params))
                    .execute()
                    .bodyBytes();

            // 5. 检查返回结果是否是错误信息
            if (isErrorResponse(qrCodeBytes)) {
                log.error("生成小程序码失败：{}", new String(qrCodeBytes));
                throw new BusinessException("生成小程序码失败");
            }

            return qrCodeBytes;
        } catch (Exception e) {
            log.error("生成小程序码异常", e);
            throw new BusinessException("生成小程序码失败，请稍后重试");
        }
    }

    /**
     * 检查返回的字节数组是否是错误信息
     */
    private boolean isErrorResponse(byte[] response) {
        try {
            // 尝试解析为 JSON，如果成功则说明是错误信息
            String result = new String(response);
            return result.contains("errcode");
        } catch (Exception e) {
            return false;
        }
    }
}
