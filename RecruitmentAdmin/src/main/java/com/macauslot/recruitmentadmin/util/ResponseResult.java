package com.macauslot.recruitmentadmin.util;

import java.io.Serializable;

/**
 * 服务器端向客户端返回响应的结果
 *
 * @param <T>
 * @author Administrator
 */
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 6614607077905824388L;
    private Integer stateCode;
    private String message;
    private T data;

    public ResponseResult(Integer stateCode) {
        super();
        setStateCode(stateCode);
    }

    public ResponseResult() {
        super();
    }

    public ResponseResult(Integer stateCode, T data) {
        this(stateCode);
        setData(data);
    }

    public ResponseResult(Integer stateCode, String message) {
        this(stateCode);
        setMessage(message);
    }

    public ResponseResult(Integer stateCode, Exception e) {
        this(stateCode, e.getMessage());
    }

    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult [stateCode=" + stateCode + ", message=" + message + ", data=" + data + "]";
    }


}
