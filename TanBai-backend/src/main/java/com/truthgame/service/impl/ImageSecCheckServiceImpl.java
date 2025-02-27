package com.truthgame.service.impl;

import cn.hutool.json.JSONUtil;
import com.truthgame.service.ImageSecCheckService;
import com.truthgame.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@Slf4j
public class ImageSecCheckServiceImpl implements ImageSecCheckService {

    @Resource
    private WeChatUtils weChatUtils;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean checkImage(byte[] imageBytes) {
        String accessToken = weChatUtils.getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken;
        
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 设置请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("media", new org.springframework.core.io.ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "temp.jpg";
            }
        });

        // 发送请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        
        try {
            ResponseEntity<WxResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                WxResponse.class
            );

            WxResponse wxResponse = response.getBody();
            log.info("图片安全检测结果: {}", JSONUtil.toJsonStr(wxResponse));
            if (wxResponse != null) {
                // 返回0表示图片正常
                return wxResponse.getErrcode() == 0;
            }
        } catch (Exception e) {
            log.error("图片安全检测失败", e);
        }
        
        return false;
    }

    private static class WxResponse {
        private int errcode;
        private String errmsg;

        public int getErrcode() {
            return errcode;
        }

        public void setErrcode(int errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }
    }
} 