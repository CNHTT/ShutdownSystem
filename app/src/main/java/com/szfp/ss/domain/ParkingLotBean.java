package com.szfp.ss.domain;

import java.io.Serializable;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class ParkingLotBean implements Serializable {

    private String name;

    private String number;
    //停车场负责公司
    private String company;
    //Parking lot Address
    private String address;

    //进口地址
    private String entryAddress;

    //负责人
    private String principal;

    //联系电话
    private String contactNumber;

    //停车场面积
    private String area;

    //停车场数量
    private String quantity;

    private String  uuid;

    private String  companyUuid;

    private String addMangerUuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEntryAddress() {
        return entryAddress;
    }

    public void setEntryAddress(String entryAddress) {
        this.entryAddress = entryAddress;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public String getAddMangerUuid() {
        return addMangerUuid;
    }

    public void setAddMangerUuid(String addMangerUuid) {
        this.addMangerUuid = addMangerUuid;
    }
}
