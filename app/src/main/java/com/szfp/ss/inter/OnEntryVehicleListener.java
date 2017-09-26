package com.szfp.ss.inter;

import com.szfp.ss.domain.ParkingRecordReportBean;

/**
 * author：ct on 2017/9/25 11:05
 * email：cnhttt@163.com
 */

public interface OnEntryVehicleListener {
    void  success(ParkingRecordReportBean reportBean);
    void  error(String str);
}
