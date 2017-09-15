package com.szfp.ss.inter;

import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.domain.UserInformation;

/**
 * author：ct on 2017/9/15 15:39
 * email：cnhttt@163.com
 */

public interface OnRechargeRecordListener {
    void success(UserInformation uInfo, RechargeRecordBean recordBean);
    void error (String str);
}
