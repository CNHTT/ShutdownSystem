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
public class RechargeBean implements Serializable {
    static  final  long serialVersionUID=42L;
    @Id(autoincrement = true)
    private Long id;
    private String memberUuid;
    private String memberLPM;
    private String memberName;


    private String  tsn;

    private double rAmount;
    private double bAmount;
    private double aAmount;
    private String operateNumber;
    private String operateUuid;
    private  String deviceSN;
    private String deviceNumber;
    private String companyUuid;
    private Date createTime;
    @Generated(hash = 1713284252)

    private int cacheType=0;
    public RechargeBean(Long id, String memberUuid, String memberLPM,
            String memberName, String tsn, double rAmount, double bAmount,
            double aAmount, String operateNumber, String operateUuid,
            String deviceSN, String deviceNumber, String companyUuid,
            Date createTime) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.memberLPM = memberLPM;
        this.memberName = memberName;
        this.tsn = tsn;
        this.rAmount = rAmount;
        this.bAmount = bAmount;
        this.aAmount = aAmount;
        this.operateNumber = operateNumber;
        this.operateUuid = operateUuid;
        this.deviceSN = deviceSN;
        this.deviceNumber = deviceNumber;
        this.companyUuid = companyUuid;
        this.createTime = createTime;
    }
    @Generated(hash = 585457968)
    public RechargeBean() {
    }
    @Generated(hash = 1946951144)
    public RechargeBean(Long id, String memberUuid, String memberLPM,
            String memberName, String tsn, double rAmount, double bAmount,
            double aAmount, String operateNumber, String operateUuid,
            String deviceSN, String deviceNumber, String companyUuid,
            Date createTime, int cacheType) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.memberLPM = memberLPM;
        this.memberName = memberName;
        this.tsn = tsn;
        this.rAmount = rAmount;
        this.bAmount = bAmount;
        this.aAmount = aAmount;
        this.operateNumber = operateNumber;
        this.operateUuid = operateUuid;
        this.deviceSN = deviceSN;
        this.deviceNumber = deviceNumber;
        this.companyUuid = companyUuid;
        this.createTime = createTime;
        this.cacheType = cacheType;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMemberUuid() {
        return this.memberUuid;
    }
    public void setMemberUuid(String memberUuid) {
        this.memberUuid = memberUuid;
    }
    public String getMemberLPM() {
        return this.memberLPM;
    }
    public void setMemberLPM(String memberLPM) {
        this.memberLPM = memberLPM;
    }
    public String getMemberName() {
        return this.memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public String getTsn() {
        return this.tsn;
    }
    public void setTsn(String tsn) {
        this.tsn = tsn;
    }
    public double getRAmount() {
        return this.rAmount;
    }
    public void setRAmount(double rAmount) {
        this.rAmount = rAmount;
    }
    public double getBAmount() {
        return this.bAmount;
    }
    public void setBAmount(double bAmount) {
        this.bAmount = bAmount;
    }
    public double getAAmount() {
        return this.aAmount;
    }
    public void setAAmount(double aAmount) {
        this.aAmount = aAmount;
    }
    public String getOperateNumber() {
        return this.operateNumber;
    }
    public void setOperateNumber(String operateNumber) {
        this.operateNumber = operateNumber;
    }
    public String getOperateUuid() {
        return this.operateUuid;
    }
    public void setOperateUuid(String operateUuid) {
        this.operateUuid = operateUuid;
    }
    public String getDeviceSN() {
        return this.deviceSN;
    }
    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }
    public String getDeviceNumber() {
        return this.deviceNumber;
    }
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
    public String getCompanyUuid() {
        return this.companyUuid;
    }
    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public int getCacheType() {
        return this.cacheType;
    }
    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }
}
