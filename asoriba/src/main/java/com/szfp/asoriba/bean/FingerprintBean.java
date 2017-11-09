package com.szfp.asoriba.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * author：ct on 2017/10/12 17:53
 * email：cnhttt@163.com
 */

@Entity
public class FingerprintBean  implements Serializable {

    static final long serialVersionUID = 42L;
    @Id(autoincrement = true)
    private Long id;
    private String UUID;
    private byte[] model;
    @Generated(hash = 447540996)
    public FingerprintBean(Long id, String UUID, byte[] model) {
        this.id = id;
        this.UUID = UUID;
        this.model = model;
    }
    @Generated(hash = 1309389135)
    public FingerprintBean() {
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
    public byte[] getModel() {
        return this.model;
    }
    public void setModel(byte[] model) {
        this.model = model;
    }
}
