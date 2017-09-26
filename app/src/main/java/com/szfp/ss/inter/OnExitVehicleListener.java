package com.szfp.ss.inter;

import com.szfp.ss.domain.ParkingRecordReportBean;

import java.util.List;

/**
 * author：ct on 2017/9/25 16:54
 * email：cnhttt@163.com
 */

public interface OnExitVehicleListener {
    void success(List<ParkingRecordReportBean> list);
    void error(String str);
}
