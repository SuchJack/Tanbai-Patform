package com.truthgame.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tan.jwt")
@Data
public class JwtProperties {

    public String getAdminTokenName;
    /**
     * 管理端员工生成jwt令牌相关配置
     */
//    private String adminSecretKey;
//    private long adminTtl;
//    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
