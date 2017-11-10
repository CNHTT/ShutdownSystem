package com.szfp.ss.domain.result;

import com.szfp.ss.domain.model.MemberBean;

/**
 * Created by 戴尔 on 2017/11/10.
 */

public class ResultMember extends Result {
    private MemberBean data;

    public MemberBean getData() {
        return data;
    }

    public void setData(MemberBean data) {
        this.data = data;
    }
}
