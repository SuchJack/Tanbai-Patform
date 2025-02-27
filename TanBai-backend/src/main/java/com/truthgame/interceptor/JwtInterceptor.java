package com.truthgame.interceptor;

import com.truthgame.Properties.JwtProperties;
import com.truthgame.annotation.NoAuth;
import com.truthgame.constant.JwtClaimsConstant;
import com.truthgame.exception.BusinessException;
import com.truthgame.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        log.info("进入拦截器"); // 调试
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
        // 如果不是映射到方法，直接通过
            return true;
        }
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        
        // 检查是否有NoAuth注解，有则跳过认证
        if (method.isAnnotationPresent(NoAuth.class) || 
            handlerMethod.getBeanType().isAnnotationPresent(NoAuth.class)) {
            return true;
        }
        
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            throw new BusinessException("未登录，请先登录");
        }
        
        // 如果token以"Bearer "开头，去掉这个前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 验证token
//        if (!JwtUtil.validateToken(token)) {
//            throw new BusinessException("token无效或已过期");
//        }

        try{
            log.info("jwt校验:{}", token);
            // 将用户ID存入request属性中，方便后续使用
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户的id：{}", userId);
            request.getSession().setAttribute("userId", userId);
//            request.setAttribute("userId", userId);
            return true;
        }catch (Exception e){
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
} 