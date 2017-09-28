package com.szfp.ss.inter;

import com.szfp.ss.domain.RechargeRecordBean;

import java.util.List;

/**
 * author：ct on 2017/9/28 14:39
 * email：cnhttt@163.com
 */

public interface OnPrintRechargeListener {
    void success(List<RechargeRecordBean> list);
}
