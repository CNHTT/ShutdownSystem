package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.szfp.ss.domain.KEY;
import com.szfp.utils.AndroidBug5497Workaround;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.DialogEditSureCancel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isNullString;

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

    private String inputStr;

    private int feeType;

    @Override
    protected void showDisconnecting() {

    }

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

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

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
        feeType = i;
        if (dialog == null)
            dialog = new DialogEditSureCancel(mContext);

        dialog.getEditText().setText("");
        switch (i){
            case 1:
                dialog. getTvTitle().setText("HOUR INPUT");
                hourFee = SPUtils.getString(mContext, KEY.HOUR_FEE);
                if (!isNullString(hourFee))
                    dialog.getEditText().setHint(DataUtils.format2Decimals(hourFee));
                else
                    dialog.getEditText().setHint(DataUtils.format2Decimals("0"));
                break;
            case 2:
                dialog. getTvTitle().setText("DAY INPUT");
                dayFee = SPUtils.getString(mContext, KEY.DAY_FEE);
                if (!isNullString(dayFee))dialog.getEditText().setHint(DataUtils.format2Decimals(dayFee));
                else
                    dialog.getEditText().setHint(DataUtils.format2Decimals("0"));
                break;
            case 3:

                dialog. getTvTitle().setText("MONTH INPUT");
                monthFee = SPUtils.getString(mContext, KEY.MONTH_FEE);
                if (!isNullString(monthFee))
                    dialog.getEditText().setHint(DataUtils.format2Decimals(monthFee));
                else
                    dialog.getEditText().setHint(DataUtils.format2Decimals("0"));
                break;
            default:
        }
        dialog.getTvSure().setOnClickListener(OnClickListener);
        dialog.getTvCancel().setOnClickListener(OnClickListener);
        dialog.show();
    }


    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R. id .tv_sure:
                    inputStr = dialog.getEditText().getText().toString();
                    switch (feeType){
                        case  1:
                            SPUtils.putString(mContext,KEY.HOUR_FEE,inputStr);
                            break;
                        case  2:
                            SPUtils.putString(mContext,KEY.DAY_FEE,inputStr);
                            break;
                        case  3:
                            SPUtils.putString(mContext,KEY.MONTH_FEE,inputStr);
                            break;
                    }



                    if (dialog != null) dialog.cancel();
                    break;

                case R.id.tv_cancel:
                    if (dialog != null) dialog.cancel();

                    break;
            }
        }
    };
}
