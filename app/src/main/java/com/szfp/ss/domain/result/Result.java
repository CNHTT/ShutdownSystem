package com.szfp.ss.domain.result;

import com.szfp.utils.ToastUtils;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class Result {
    public final static int OK = 1, FAILED = 0, EMPUTY = -1;
    private  int code ;
    private  String msg;

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


    public boolean checkCode(){

        if (code == 1)
        {
            ToastUtils.success(msg);
            return true;
        }else {
            ToastUtils.error(msg);
            return false;
        }
    }
}
