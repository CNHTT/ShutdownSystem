package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.PagerItem;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SPUtils;
import com.szfp.view.NiceSpinner;
import com.szfp.view.button.SelectButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseTimeActivity extends BaseAty {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;
    @BindView(R.id.tv_unit_amount)
    TextView tvUnitAmount;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.bt_sub)
    SelectButton btSub;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.bt_add)
    SelectButton btAdd;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_sava)
    Button btSava;


    private int type;
    private String  unitAmount;
    private String  numStr;
    private int num;
    @Override
    protected void showDisconnecting() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_time);
        ButterKnife.bind(this);
        List<String> dataset = new LinkedList<>(Arrays.asList(PagerItem.HOUR, PagerItem.DAY, PagerItem.MONTH));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 0:
                       unitAmount = SPUtils.getString(mContext, KEY.HOUR_FEE);
                       tvUnitAmount.setText(DataUtils.getAmountValue(unitAmount));
                       break;
                   case 1:
                       unitAmount = SPUtils.getString(mContext, KEY.DAY_FEE);
                       tvUnitAmount.setText(DataUtils.getAmountValue(unitAmount));
                       break;
                   case 2:
                       unitAmount = SPUtils.getString(mContext, KEY.MONTH_FEE);
                       tvUnitAmount.setText(DataUtils.getAmountValue(unitAmount));
                       break;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }

    @OnClick({R.id.bt_sub, R.id.bt_add, R.id.bt_cancel, R.id.bt_sava})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sub:
                break;
            case R.id.bt_add:
                break;
            case R.id.bt_cancel:
                break;
            case R.id.bt_sava:
                break;
        }
    }
}
