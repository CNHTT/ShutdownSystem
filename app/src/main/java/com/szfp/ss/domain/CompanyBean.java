package com.szfp.ss.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 戴尔 on 2017/11/9.
 */

public class CompanyBean implements Serializable{
    private Long id;                                                   //公司ＩＤ
    private String name;                                        // 公司名称
    private String contactNumber;                     // 联系电话
    private String PIC;                                        // 公司负责人
    private String website;                               // 公司网站
    private String email;                                 // 公司邮箱
    private String address;                           //公司地址
    private String managerUUID;              //第一创建人UUID
    private String UUID;                            //公司UUID
    private List<ParkingLotBean> lotList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPIC() {
        return PIC;
    }

    public void setPIC(String PIC) {
        this.PIC = PIC;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManagerUUID() {
        return managerUUID;
    }

    public void setManagerUUID(String managerUUID) {
        this.managerUUID = managerUUID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public List<ParkingLotBean> getLotList() {
        return lotList;
    }

    public void setLotList(List<ParkingLotBean> lotList) {
        this.lotList = lotList;
    }
}
