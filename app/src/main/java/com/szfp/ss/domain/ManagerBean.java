package com.szfp.ss.domain;

import java.io.Serializable;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class ManagerBean implements Serializable {
    private Long id;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String UUID;
    private String companyUUID;
    private  String number;
    private  String parkingUuid;

    public String getParkingUuid() {
        return parkingUuid;
    }

    public void setParkingUuid(String parkingUuid) {
        this.parkingUuid = parkingUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getCompanyUUID() {
        return companyUUID;
    }

    public void setCompanyUUID(String companyUUID) {
        this.companyUUID = companyUUID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
