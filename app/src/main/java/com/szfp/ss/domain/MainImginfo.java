package com.szfp.ss.domain;

/**
 * author：ct on 2017/8/29 16:25
 * email：cnhttt@163.com
 */

public class MainImginfo {
    private  int id;
    private  String imageMsg;
    private  int imageId;

    public MainImginfo(int id, String imageMsg, int imageId) {
        this.id = id;
        this.imageMsg = imageMsg;
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageMsg() {
        return imageMsg;
    }

    public void setImageMsg(String imageMsg) {
        this.imageMsg = imageMsg;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
