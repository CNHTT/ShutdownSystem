package com.szfp.ss.domain.result;

import com.szfp.ss.domain.CompanyInfoBean;

/**
 * Created by 戴尔 on 2017/11/10.
 */

public class ResultCompany extends Result {
    private CompanyInfoBean data;

    public CompanyInfoBean getData() {
        return data;
    }

    public void setData(CompanyInfoBean data) {
        this.data = data;
    }
}
