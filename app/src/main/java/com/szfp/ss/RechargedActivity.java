package com.szfp.ss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.PagerItem;
import com.szfp.ss.domain.UserInformation;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RechargedActivity extends BaseNoAty {

    @BindView(R.id.recharge)
    SelectButton recharge;
    @BindView(R.id.purchase_time)
    SelectButton purchaseTime;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private UserInformation userInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharged);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this, 10);

        toolbar.setTitle(PagerItem.RECHARGE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @OnClick({R.id.recharge, R.id.purchase_time})
    public void onClick(View view) {
        Intent in = new Intent();
        if (!DataUtils.isEmpty(userInformation)){
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY.CARD_INFO,userInformation);
            in.putExtras(bundle);
        }


        switch (view.getId()) {
            case R.id.recharge://充值
                in.setClass(this,RechargeAty.class);
                startActivity(in);
                break;
            case R.id.purchase_time: //购买时长
                if (DataUtils.isNullString(SPUtils.getString(mContext,KEY.HOUR_FEE))||
                        DataUtils.isNullString(SPUtils.getString(mContext,KEY.DAY_FEE))||
                        DataUtils.isNullString(SPUtils.getString(mContext,KEY.MONTH_FEE))){

                    ToastUtils.error("Please set the fee or download the updated data");

                    in.setClass(this,SettingAty.class);
                    startActivity(in);

                }else {
                    in.setClass(this,PurchaseTimeActivity.class);
                    startActivity(in);
                }

                break;
        }
    }
}
