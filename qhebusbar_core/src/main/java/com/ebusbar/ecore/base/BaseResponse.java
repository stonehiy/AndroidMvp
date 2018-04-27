package com.ebusbar.ecore.base;

/**
 */

public class BaseResponse<T> {

    private String msg;
    private int code;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
