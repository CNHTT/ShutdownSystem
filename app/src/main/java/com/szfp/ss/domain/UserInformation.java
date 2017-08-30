package com.szfp.ss.domain;

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
    private long id;
    private String lastName;
    private String firstName;
    private String licensePlateNumber;
    private String telephoneNumber;
    @Generated(hash = 1322418318)
    public UserInformation(long id, String lastName, String firstName,
            String licensePlateNumber, String telephoneNumber) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.licensePlateNumber = licensePlateNumber;
        this.telephoneNumber = telephoneNumber;
    }
    @Generated(hash = 1920987651)
    public UserInformation() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
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
    
}
