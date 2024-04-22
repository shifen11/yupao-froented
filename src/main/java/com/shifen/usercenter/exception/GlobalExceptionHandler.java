package com.shifen.usercenter.exception;


import com.shifen.usercenter.common.BaseResponse;
import com.shifen.usercenter.common.ErrorCode;
import com.shifen.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author shifen
 * @version 1.0.1
 * @date 2024/4/22 3:09
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException"+e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getDescription(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse businessExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "", e.getMessage());
    }
}
