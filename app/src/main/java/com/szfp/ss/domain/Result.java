package com.szfp.ss.domain;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class Result<T> {
    public final static int OK = 1, FAILED = 0, EMPUTY = -1;
    private  int code ;
    private  String msg;
    private  T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
