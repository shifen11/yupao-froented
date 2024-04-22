package com.shifen.usercenter.common;

import lombok.Data;

/**
 * 通用返回类
 * @author shifen
 * @version 1.0.1
 * @date 2024/4/19 19:37
 */
@Data
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;
    private String description;
    public BaseResponse(int code,T data, String message ,String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }
    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }
    public BaseResponse(int code, T data, String message) {
        this(code,null,message,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
