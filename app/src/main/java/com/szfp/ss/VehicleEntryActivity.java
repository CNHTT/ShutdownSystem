package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.inter.OnEntryVehicleListener;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.PrintUtils;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.DataUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
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

    /**
     *
     * @param userInformation
     */
    @Override
    protected void showUser(UserInformation userInformation) {
        ParkingRecordReportBean reportBean = new ParkingRecordReportBean();
        reportBean.setSerialNumber(TimeUtils.generateSequenceNo());
        reportBean.setUUID(userInformation.getUUID());
        reportBean.setCardId(userInformation.getCardId());
        reportBean.setLicensePlateNumber(userInformation.getLicensePlateNumber());
        reportBean.setCardNumber(DataUtils.isNullString(userInformation.getCardNumber())?userInformation.getCardNumber():"0000000000");
        reportBean.setLastName(userInformation.getLastName());
        reportBean.setFirstName(userInformation.getFirstName());
        reportBean.setPType(1);
        reportBean.setAdminId(1);
        reportBean.setAdminNumber(App.operator);
        reportBean.setTerminalNumber(App.terminalNumber);
        reportBean.setParkingNumber(App.parkingNumber);
        reportBean.setEnterTime(TimeUtils.getCurTimeMills());
        reportBean.setCreateTime(TimeUtils.getCurTimeMills());
        reportBean.setCreateDayTime(TimeUtils.getCrateDayTime());

        DbHelper.insertEntryVehicle(reportBean, new OnEntryVehicleListener() {
            @Override
            public void success(ParkingRecordReportBean reportBean) {
                if (BluetoothService.IsNoConnection()){
                    ToastUtils.showToast(R.string.not_connected);
                }

                PrintUtils.printEntryVehicle(reportBean);

            }

            @Override
            public void error(String str) {
                ToastUtils.error(str);

            }
        });



    }

    @Override
    protected void showDisconnecting() {
        btCommPrint.setText("Bluetooth Disconnect");mWave.stop();
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
