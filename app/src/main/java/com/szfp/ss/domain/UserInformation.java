package com.szfp.ss.domain;

import com.szfp.ss.domain.result.RechargeRecordBean;
import com.szfp.utils.DataUtils;
import com.szfp.utils.TimeUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * author：ct on 2017/8/30 16:22
 * email：cnhttt@163.com
 */
@Entity
public class UserInformation  implements Serializable{
    static  final  long serialVersionUID=42L;
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String licensePlateNumber;
    private String telephoneNumber;
    private String email;
    private String cardNumber;
    private String cardId;
    private long createTime;
    private long createDayTime;
    private String UUID;
    private double balance;
    private long parkingTimeIsValidEnd;

    @Generated(hash = 604965853)
    public UserInformation(Long id, String name, String licensePlateNumber, String telephoneNumber,
            String email, String cardNumber, String cardId, long createTime, long createDayTime,
            String UUID, double balance, long parkingTimeIsValidEnd) {
        this.id = id;
        this.name = name;
        this.licensePlateNumber = licensePlateNumber;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.cardNumber = cardNumber;
        this.cardId = cardId;
        this.createTime = createTime;
        this.createDayTime = createDayTime;
        this.UUID = UUID;
        this.balance = balance;
        this.parkingTimeIsValidEnd = parkingTimeIsValidEnd;
    }

    @Generated(hash = 1920987651)
    public UserInformation() {
    }

    public String getPurchaseTimeStr(RechargeRecordBean recordBean) {
        StringBuffer sb = new StringBuffer();
        sb.append("Transaction ID:"+recordBean.getUUID()+"\n");
        sb.append("Name:" +name+"\n");
        sb.append("Balance:"+ DataUtils.getAmountValue(balance-recordBean.getTwoAmount())+"\n");
        sb.append("telephoneNumber:"+telephoneNumber+"\n");
        sb.append("PayCardAmount:"+DataUtils.getAmountValue(recordBean.getTwoCardAmount())+"\n");
        sb.append("* PayCashAmount:"+DataUtils.getAmountValue(recordBean.getTwoCashAmount())+"\n");
        sb.append("BuyType:"+recordBean.getTwoBuyName()+"\n");
        sb.append("BuyNum:" + recordBean.getTwoBuyNum()+"\n");
        sb.append("ParkingTimeIsValidEnd"+ TimeUtils.milliseconds2String(parkingTimeIsValidEnd)+"\n");
        sb.append("CreateTime:"+TimeUtils.milliseconds2String(recordBean.getCreateTime()));
        return sb.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePlateNumber() {
        return this.licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
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

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getParkingTimeIsValidEnd() {
        return this.parkingTimeIsValidEnd;
    }

    public void setParkingTimeIsValidEnd(long parkingTimeIsValidEnd) {
        this.parkingTimeIsValidEnd = parkingTimeIsValidEnd;
    }


}
