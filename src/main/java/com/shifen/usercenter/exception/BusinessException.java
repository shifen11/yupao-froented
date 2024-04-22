package com.shifen.usercenter.exception;

import com.shifen.usercenter.common.ErrorCode;

/**
 * 自定义异常
 *
 * @author shifen
 * @version 1.0.1
 * @date 2024/4/22 2:24
 */
public class BusinessException extends RuntimeException {

    private int code;

    private String description;

    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
    public int getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }

}
