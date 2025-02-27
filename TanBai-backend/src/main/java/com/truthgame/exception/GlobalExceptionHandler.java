package com.truthgame.exception;

import com.truthgame.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> handleException(Exception ex, HttpServletResponse response) throws IOException {
        log.error("系统异常：", ex);
        
        // 如果是图片响应，返回一个默认的错误图片
        if (response.getContentType() != null && response.getContentType().startsWith("image/")) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            // 这里可以返回一个默认的错误图片
            return null;
        }
        
        return Result.error("系统异常，请稍后重试");
    }
} 