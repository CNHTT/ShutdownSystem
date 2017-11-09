package com.szfp.asoriba.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * author：ct on 2017/10/12 16:35
 * email：cnhttt@163.com
 */

@Entity
public class MemberBean implements Serializable {

    static final long serialVersionUID = 42L;
    @Id(autoincrement = true)
    private Long id;
    private String UUID;
    private String chooseType;
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String marital;
    private String phone;
    private String membershipId;
    private String UHF_ID;
    private boolean isBind=false;
    @Generated(hash = 439716216)
    public MemberBean(Long id, String UUID, String chooseType, String title,
            String firstName, String lastName, String gender, String marital,
            String phone, String membershipId, String UHF_ID, boolean isBind) {
        this.id = id;
        this.UUID = UUID;
        this.chooseType = chooseType;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.marital = marital;
        this.phone = phone;
        this.membershipId = membershipId;
        this.UHF_ID = UHF_ID;
        this.isBind = isBind;
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
    public String getUUID() {
        return this.UUID;
    }
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getMarital() {
        return this.marital;
    }
    public void setMarital(String marital) {
        this.marital = marital;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getMembershipId() {
        return this.membershipId;
    }
    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }
    public boolean getIsBind() {
        return this.isBind;
    }
    public void setIsBind(boolean isBind) {
        this.isBind = isBind;
    }
    public String getChooseType() {
        return this.chooseType;
    }
    public void setChooseType(String chooseType) {
        this.chooseType = chooseType;
    }
    public String getUHF_ID() {
        return this.UHF_ID;
    }
    public void setUHF_ID(String UHF_ID) {
        this.UHF_ID = UHF_ID;
    }


}
