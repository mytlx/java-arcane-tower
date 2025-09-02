package com.mytlx.dev.solutions.job.scheduler.aop.exception;

import com.mytlx.dev.solutions.job.scheduler.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 20:31:23
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseVO<?> handleBizException(BizException e) {
        log.error("业务异常: {}", e.getMessage());
        return ResponseVO.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVO<?> handleValidationException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseVO.error(400, "参数校验失败：" + msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseVO<?> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseVO.error(-100, "服务器异常，请联系管理员");
    }

}
