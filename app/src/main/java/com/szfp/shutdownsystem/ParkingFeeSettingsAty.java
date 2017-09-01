package com.szfp.shutdownsystem;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.szfp.ss.BaseAty;
import com.szfp.ss.R;
import com.szfp.utils.AndroidBug5497Workaround;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.DialogEditSureCancel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParkingFeeSettingsAty extends BaseAty {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cd_hour)
    CardView cdHour;
    @BindView(R.id.cd_day)
    CardView cdDay;
    @BindView(R.id.cd_month)
    CardView cdMonth;
    @BindView(R.id.cd_other)
    CardView cdOther;

    private String hourFee;
    private String dayFee;
    private String monthFee;

    private int feeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_fee_settings_aty);
        ButterKnife.bind(this);
        toolbar.setTitle(getResources().getString(R.string.pfs));
        setSupportActionBar(toolbar);


        StatusBarUtil.setTransparent(this);

        if (isFullScreen(this))
            AndroidBug5497Workaround.assistActivity(this);
    }



    @OnClick({R.id.cd_hour, R.id.cd_day, R.id.cd_month, R.id.cd_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cd_hour:
                showFeeEdit(1);
                break;
            case R.id.cd_day:
                showFeeEdit(2);
                break;
            case R.id.cd_month:
                showFeeEdit(3);
                break;
            case R.id.cd_other:
                ToastUtils.success("other");
                break;
        }
    }



    private DialogEditSureCancel dialog;
    private void showFeeEdit(int i) {
        if (dialog == null)
            dialog = new DialogEditSureCancel(mContext);

        switch (i){
            case 1:

                break;
            case 2:

                break;
            case 3:
                break;
            default:
        }

        dialog.getEditText().setText("0");
        dialog.getTvSure().setOnClickListener(OnClickListener);
        dialog.getTvCancel().setOnClickListener(OnClickListener);
        dialog.show();
    }


    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R. id .tv_sure:

                    if (dialog != null) dialog.cancel();
                    break;

                case R.id.tv_cancel:
                    if (dialog != null) dialog.cancel();

                    break;
            }
        }
    };
}
