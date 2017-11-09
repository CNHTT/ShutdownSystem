package com.szfp.asoriba.view;

import org.litepal.crud.DataSupport;

/**
 * author：ct on 2017/10/20 15:11
 * email：cnhttt@163.com
 */

public class FingerModels extends DataSupport {
    private String model_ID;
    private byte[] model;

    public String getId() {
        return model_ID;
    }

    public void setId(String id) {
        this.model_ID = id;
    }

    public byte[] getModel() {
        return model;
    }

    public void setModel(byte[] model) {
        this.model = model;
    }
}
