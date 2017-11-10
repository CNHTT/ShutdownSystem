package com.szfp.ss.domain.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 戴尔 on 2017/11/10.
 */

@Entity
public class ParkingRecordBean  implements Serializable {
    static  final  long serialVersionUID=42L;
    @Id(autoincrement = true)
    private  Long id;
    private  String  tsn;
    private  int  type;
    private  String memberUuid;
    private  String memberName;
    private  String memberLpm;
    private String operateNumber;
    private String operateUuid;
    private String deviceSN;
    private String deviceNumber;
    private String parkingLotName;
    private String parkingUuid;

    private double amount;
    //驶入时间
    private long enterLongTime;
    //驶出时间
    private long exitLongTime;
    private Date enterTime;
    private Date exitTime;
    private Date createTime;
    private long parkingTime;
    private int intTime;
    private int cacheType=0;

    @Generated(hash = 1219315746)
    public ParkingRecordBean(Long id, String tsn, int type, String memberUuid,
            String memberName, String memberLpm, String operateNumber,
            String operateUuid, String deviceSN, String deviceNumber,
            String parkingLotName, String parkingUuid, double amount,
            long enterLongTime, long exitLongTime, Date enterTime, Date exitTime,
            Date createTime, long parkingTime, int intTime, int cacheType) {
        this.id = id;
        this.tsn = tsn;
        this.type = type;
        this.memberUuid = memberUuid;
        this.memberName = memberName;
        this.memberLpm = memberLpm;
        this.operateNumber = operateNumber;
        this.operateUuid = operateUuid;
        this.deviceSN = deviceSN;
        this.deviceNumber = deviceNumber;
        this.parkingLotName = parkingLotName;
        this.parkingUuid = parkingUuid;
        this.amount = amount;
        this.enterLongTime = enterLongTime;
        this.exitLongTime = exitLongTime;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.createTime = createTime;
        this.parkingTime = parkingTime;
        this.intTime = intTime;
        this.cacheType = cacheType;
    }

    @Generated(hash = 1849554042)
    public ParkingRecordBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTsn() {
        return tsn;
    }

    public void setTsn(String tsn) {
        this.tsn = tsn;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMemberUuid() {
        return memberUuid;
    }

    public void setMemberUuid(String memberUuid) {
        this.memberUuid = memberUuid;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberLpm() {
        return memberLpm;
    }

    public void setMemberLpm(String memberLpm) {
        this.memberLpm = memberLpm;
    }

    public String getOperateNumber() {
        return operateNumber;
    }

    public void setOperateNumber(String operateNumber) {
        this.operateNumber = operateNumber;
    }

    public String getOperateUuid() {
        return operateUuid;
    }

    public void setOperateUuid(String operateUuid) {
        this.operateUuid = operateUuid;
    }

    public String getDeviceSN() {
        return deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public String getParkingUuid() {
        return parkingUuid;
    }

    public void setParkingUuid(String parkingUuid) {
        this.parkingUuid = parkingUuid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getEnterLongTime() {
        return enterLongTime;
    }

    public void setEnterLongTime(long enterLongTime) {
        this.enterLongTime = enterLongTime;
    }

    public long getExitLongTime() {
        return exitLongTime;
    }

    public void setExitLongTime(long exitLongTime) {
        this.exitLongTime = exitLongTime;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(long parkingTime) {
        this.parkingTime = parkingTime;
    }

    public int getIntTime() {
        return intTime;
    }

    public void setIntTime(int intTime) {
        this.intTime = intTime;
    }

    public int getCacheType() {
        return this.cacheType;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }
}
