package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.szfp.ss.domain.UserInformation;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.StatusBarUtil;
import com.szfp.view.button.SelectButton;
import com.szfp.view.progress.style.Wave;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VehicleEntryActivity extends BaseReadActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bt_ve_read)
    SelectButton btVeRead;
    @BindView(R.id.bt_comm_print)
    SelectButton btCommPrint;
    @BindView(R.id.bt_repeat)
    SelectButton btRepeat;
    @BindView(R.id.bt_stop)
    SelectButton btStop;

    private Wave mWave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_entry);
        ButterKnife.bind(this);
        toolbar.setTitle("Vehicle Entry");
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucent(this, 10);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }


    private void initView() {
        if (!BluetoothService.IsNoConnection()){
            mWave = new Wave();
            mWave.setBounds(0,0,100,100);
            mWave.setColor(getResources().getColor(R.color.wheat));
            btCommPrint.setCompoundDrawables(mWave,null,null,null);
            mWave.start();
            btCommPrint.setText("CONN");
        }
    }

    @Override
    protected void showConnecting() {
        mWave = new Wave();
        mWave.setBounds(0,0,100,100);
        mWave.setColor(getResources().getColor(R.color.baby_blue));
        btCommPrint.setCompoundDrawables(mWave,null,null,null);
        mWave.start();
        btCommPrint.setText("CONN......");
    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {
        btCommPrint.setText("CONN");
    }

    @Override
    protected void showUser(UserInformation userInformation) {

    }

    @Override
    protected void showDisconnecting() {
        btCommPrint.setText("");
    }

    @OnClick({R.id.bt_ve_read, R.id.bt_comm_print, R.id.bt_repeat, R.id.bt_stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ve_read:
                showSlotCard();
                break;
            case R.id.bt_comm_print:
                showDeviceList();
                break;
            case R.id.bt_repeat:
                if (isRepeat){
                    isRepeat=false;
                    btRepeat.setText(getResources().getString(R.string.repeat_off));
                }else {
                    isRepeat = true;
                    btRepeat.setText(getResources().getString(R.string.repeat_on));
                }
                break;
            case R.id.bt_stop:
                isReader =false;
                break;
        }
    }
}
