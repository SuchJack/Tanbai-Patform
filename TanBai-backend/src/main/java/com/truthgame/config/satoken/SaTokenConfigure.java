package com.truthgame.config.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/auth/wx/login",  // 登录接口不需要验证
                        "/doc.html",           // knife4j接口文档
                        "/webjars/**",         // knife4j接口文档
                        "/v2/api-docs",        // knife4j接口文档
                        "/swagger-resources/**", // knife4j接口文档
                        "/qa",
                        "/customer-service",
                        "/posters/**"  //TODO 需要拦截
                );
    }
}
