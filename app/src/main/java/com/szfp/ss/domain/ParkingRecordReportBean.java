package com.szfp.ss.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * author：ct on 2017/9/21 18:45
 * email：cnhttt@163.com
 */

@Entity
public class ParkingRecordReportBean implements Serializable {
    static  final  long serialVersionUID=42L;
    @Id(autoincrement = true)
    private long id;

    private long userId;
    private String lastName;
    private String firstName;

    /**
     * 0 临时卡    1普通卡
     */
    private int pType= 1;
    private long adminId;
    private String  adminNumber;//操作人员编号、

    private long enterTime;


    private long createTime;
    private long createDayTime;
    @Generated(hash = 1209389573)
    public ParkingRecordReportBean(long id, long userId, String lastName,
            String firstName, int pType, long adminId, String adminNumber,
            long enterTime, long createTime, long createDayTime) {
        this.id = id;
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.pType = pType;
        this.adminId = adminId;
        this.adminNumber = adminNumber;
        this.enterTime = enterTime;
        this.createTime = createTime;
        this.createDayTime = createDayTime;
    }
    @Generated(hash = 318527304)
    public ParkingRecordReportBean() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public int getPType() {
        return this.pType;
    }
    public void setPType(int pType) {
        this.pType = pType;
    }
    public long getAdminId() {
        return this.adminId;
    }
    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }
    public String getAdminNumber() {
        return this.adminNumber;
    }
    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
    }
    public long getEnterTime() {
        return this.enterTime;
    }
    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getCreateDayTime() {
        return this.createDayTime;
    }
    public void setCreateDayTime(long createDayTime) {
        this.createDayTime = createDayTime;
    }


}
