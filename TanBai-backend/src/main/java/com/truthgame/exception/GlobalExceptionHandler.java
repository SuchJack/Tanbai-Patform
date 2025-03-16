package com.truthgame.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.truthgame.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("参数验证失败：{}", message);
        return Result.error(message);
    }

    @ExceptionHandler(ClientAbortException.class)
    @ResponseBody
    public void handleClientAbortException(ClientAbortException e) {
        // 客户端中断连接，忽略错误
        log.warn("Client aborted connection: {}", e.getMessage());
    }

    /**
     * 权限不足异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<Void> handleNotPermissionException(NotPermissionException e) {
        log.error(e.getMessage(), e);
        return Result.error("权限不足");
    }


    /**
     * 未登录异常
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("未登录异常：", e);
        return Result.error("当前用户未登录或 登录已过期");
    }

    /**
     * 系统异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统错误");
    }
} 