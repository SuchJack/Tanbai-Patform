package com.truthgame.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.truthgame.config.WxConfig;
import com.truthgame.exception.BusinessException;
import com.truthgame.model.entity.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WeChatUtils {

    @Resource
    private WxConfig wxConfig;

    @Resource
    private RestTemplate restTemplate;


    public boolean verifyContent(String content) {
        // 调用微信内容安全检查接口
        String accessToken = this.getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;

        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("content", content);

        try {
            String result = restTemplate.postForObject(url, params, String.class);
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
                wxConfig.getAppId(), wxConfig.getAppSecret());

        String result = restTemplate.getForObject(url, String.class);
        JSONObject json = JSONUtil.parseObj(result);

        if (json.containsKey("access_token")) {
            return json.getStr("access_token");
        } else {
            log.error("获取access_token失败：{}", result);
            throw new BusinessException("获取access_token失败");
        }
    }
}
