package com.szfp.ss.inter;

import com.szfp.ss.domain.ParkingRecordReportBean;

import java.util.List;

/**
 * author：ct on 2017/9/28 18:26
 * email：cnhttt@163.com
 */

public interface OnPrintParkListener {
    void successPark(List<ParkingRecordReportBean> list);
}

