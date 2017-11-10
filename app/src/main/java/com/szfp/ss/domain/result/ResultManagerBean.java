package com.szfp.ss.domain.result;

import com.szfp.ss.domain.ManagerBean;

/**
 * Created by 戴尔 on 2017/11/10.
 */

public class ResultManagerBean extends Result {
    private ManagerBean data;

    public ManagerBean getData() {
        return data;
    }

    public void setData(ManagerBean data) {
        this.data = data;
    }
}
