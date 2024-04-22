package com.shifen.usercenter.common;

/**
 * 返回工具类
 *
 * @author shifen
 */
public class ResultUtils {
    /**
     * 成功返回
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse(0,data,"ok");
    }

    /**
     * 失败返回
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description, String message){
        return new BaseResponse(errorCode.getCode(),null,message,description);
    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description){
        return new BaseResponse(errorCode.getCode(),null,errorCode.getMessage(),description);
    }
    public static <T> BaseResponse<T> error(int code, String description,String message){
        return new BaseResponse(code,null,message,description);
    }
}
