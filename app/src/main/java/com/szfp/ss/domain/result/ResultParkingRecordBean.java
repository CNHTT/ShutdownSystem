package com.szfp.ss.domain.result;

import com.szfp.ss.domain.model.ParkingRecordBean;

import java.util.ArrayList;

/**
 * Created by 戴尔 on 2017/11/14.
 */

public class ResultParkingRecordBean  extends Result{
    ArrayList<ParkingRecordBean> data;

    public ArrayList<ParkingRecordBean> getData() {
        return data;
    }

    public void setData(ArrayList<ParkingRecordBean> data) {
        this.data = data;
    }
}
