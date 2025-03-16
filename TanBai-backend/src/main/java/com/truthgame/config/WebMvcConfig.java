package com.truthgame.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
//    @Resource
//    private JwtInterceptor jwtInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/api/auth/wx/login",  // 登录接口不需要验证
//                        "/doc.html",           // knife4j接口文档
//                        "/webjars/**",         // knife4j接口文档
//                        "/v2/api-docs",        // knife4j接口文档
//                        "/swagger-resources/**", // knife4j接口文档
//                        "/qa",
////                        "/wx/subscribe-message/send-activity-notice",
////                        "/wx/subscribe-message/send-comment-notice",
//                        "/customer-service"
//                );
//    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
} 