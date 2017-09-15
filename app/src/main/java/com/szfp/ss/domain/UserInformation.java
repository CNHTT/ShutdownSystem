package com.szfp.ss.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

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
    private String lastName;
    private String firstName;
    private String licensePlateNumber;
    private String telephoneNumber;


    @Index(unique = true)
    private String cardId;
    private long createTime;
    private long createDayTime;
    private String UUID;


    private double balance;


    @Generated(hash = 1473684631)
    public UserInformation(Long id, String lastName, String firstName,
            String licensePlateNumber, String telephoneNumber, String cardId,
            long createTime, long createDayTime, String UUID, double balance) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.licensePlateNumber = licensePlateNumber;
        this.telephoneNumber = telephoneNumber;
        this.cardId = cardId;
        this.createTime = createTime;
        this.createDayTime = createDayTime;
        this.UUID = UUID;
        this.balance = balance;
    }


    @Generated(hash = 1920987651)
    public UserInformation() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
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


    
}
