package com.truthgame.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HttpClient4Utils {

    /**
     * 发送POST请求
     * @param url 请求URL
     * @param jsonParams JSON格式的请求参数
     * @param httpClient HTTP客户端
     * @return 响应内容
     */
    public static String doPost(String url, String jsonParams, CloseableHttpClient httpClient) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpPost.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        try {
            StringEntity entity = new StringEntity(jsonParams, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);
            
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                return EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("HTTP POST request failed: ", e);
            throw new RuntimeException("HTTP请求失败", e);
        }
    }
} 