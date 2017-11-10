package com.szfp.ss.domain.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 *  会员信息
 * Created by 戴尔 on 2017/11/10.
 */

@Entity
public class MemberBean implements Serializable {
    static  final  long serialVersionUID=42L;
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int sex=1;
    private String number;
    private String phone;
    private String email;
    private String contactAddress;
    private int status;
    private String lpm;

    private int is_del;
    private double balance;
    private String companyUuid;
    private String  uuid;
    private String  addManagerUuid;
    private Date createTime;
    private String cardId;
    private int cacheType=0;

    @Generated(hash = 535996210)
    public MemberBean(Long id, String name, int sex, String number, String phone,
            String email, String contactAddress, int status, String lpm, int is_del,
            double balance, String companyUuid, String uuid, String addManagerUuid,
            Date createTime, String cardId, int cacheType) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.number = number;
        this.phone = phone;
        this.email = email;
        this.contactAddress = contactAddress;
        this.status = status;
        this.lpm = lpm;
        this.is_del = is_del;
        this.balance = balance;
        this.companyUuid = companyUuid;
        this.uuid = uuid;
        this.addManagerUuid = addManagerUuid;
        this.createTime = createTime;
        this.cardId = cardId;
        this.cacheType = cacheType;
    }
    @Generated(hash = 1592035565)
    public MemberBean() {
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
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContactAddress() {
        return this.contactAddress;
    }
    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getLpm() {
        return this.lpm;
    }
    public void setLpm(String lpm) {
        this.lpm = lpm;
    }
    public int getIs_del() {
        return this.is_del;
    }
    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }
    public double getBalance() {
        return this.balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public String getCompanyUuid() {
        return this.companyUuid;
    }
    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getAddManagerUuid() {
        return this.addManagerUuid;
    }
    public void setAddManagerUuid(String addManagerUuid) {
        this.addManagerUuid = addManagerUuid;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getCardId() {
        return this.cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public int getCacheType() {
        return this.cacheType;
    }
    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

}
