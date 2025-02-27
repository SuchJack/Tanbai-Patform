package com.truthgame.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import com.truthgame.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;

@Slf4j
public class WxDataDecryptUtil {
    
    /**
     * 签名校验
     * @param sessionKey 会话密钥
     * @param rawData 原始数据
     * @param signature 签名
     * @return 是否校验通过
     */
    public static boolean checkSignature(String sessionKey, String rawData, String signature) {
        if (!StringUtils.hasText(sessionKey) || !StringUtils.hasText(rawData) || !StringUtils.hasText(signature)) {
            return false;
        }
        
        // 签名校验
        String signatureGen = SecureUtil.sha1(rawData + sessionKey);
        return signature.equals(signatureGen);
    }
    
    /**
     * 解密数据
     * @param sessionKey 会话密钥
     * @param encryptedData 加密数据
     * @param iv 初始向量
     * @return 解密后的数据
     */
    public static String decrypt(String sessionKey, String encryptedData, String iv) {
        try {
            if (!StringUtils.hasText(sessionKey) || !StringUtils.hasText(encryptedData) || !StringUtils.hasText(iv)) {
                throw new BusinessException("解密参数不能为空");
            }
            
            // Base64解码
            byte[] keyBytes = Base64.decode(sessionKey);
            byte[] ivBytes = Base64.decode(iv);
            byte[] dataBytes = Base64.decode(encryptedData);
            
            // 初始化密钥
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            // 初始化向量
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(ivBytes));
            
            // 解密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, params);
            byte[] decryptedData = cipher.doFinal(dataBytes);
            
            // 返回解密后的字符串
            return new String(decryptedData, StandardCharsets.UTF_8);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("微信数据解密失败", e);
            throw new BusinessException("数据解密失败");
        }
    }
    
    /**
     * 校验数据水印
     * @param decryptedData 解密后的数据
     * @param appId 小程序appId
     * @return 是否校验通过
     */
    public static boolean checkWatermark(String decryptedData, String appId) {
        try {
            // 解密数据示例：
            // {
            //     "openId": "OPENID",
            //     "nickName": "NICKNAME",
            //     ...
            //     "watermark": {
            //         "appid":"APPID",
            //         "timestamp":TIMESTAMP
            //     }
            // }
            return decryptedData.contains("\"appid\":\"" + appId + "\"");
        } catch (Exception e) {
            return false;
        }
    }
} 