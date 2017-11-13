package com.szfp.ss.domain.result;

import com.szfp.ss.domain.model.MemberBean;
import com.szfp.ss.domain.model.RechargeBean;

/**
 * Created by 戴尔 on 2017/11/13.
 */

public class ResultRechargeBean extends Result {
    private MemberBean memberBean;
    private RechargeBean rechargeBean;

    public MemberBean getMemberBean() {
        return memberBean;
    }

    public void setMemberBean(MemberBean memberBean) {
        this.memberBean = memberBean;
    }

    public RechargeBean getRechargeBean() {
        return rechargeBean;
    }

    public void setRechargeBean(RechargeBean rechargeBean) {
        this.rechargeBean = rechargeBean;
    }
}
