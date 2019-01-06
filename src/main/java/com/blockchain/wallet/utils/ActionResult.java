package com.blockchain.wallet.utils;

/**
 * @author QiShuo
 * @version 1.0
 * @create 2018/9/17 下午2:53
 */
public class ActionResult<T> {
    private Integer code;
    private String error;
    private T data;

    public static <T> ActionResult<T> New(int code, String message) {
        ActionResult<T> ret = new ActionResult<>();
        ret.setCode(code);
        ret.setError(message);
        return ret;
    }

    public static <T> ActionResult<T> New(T data) {
        ActionResult<T> ret = new ActionResult<>();
        ret.setData(data);
        ret.setCode(0);
        return ret;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
