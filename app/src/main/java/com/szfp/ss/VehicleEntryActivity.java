package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.inter.OnEntryVehicleListener;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.PrintUtils;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.progress.style.Wave;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isNullString;

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
    @BindView(R.id.bt_input_number)
    SelectButton btInputNumber;

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
        if (!BluetoothService.IsNoConnection()) {
            mWave = new Wave();
            mWave.setBounds(0, 0, 100, 100);
            mWave.setColor(getResources().getColor(R.color.wheat));
            btCommPrint.setCompoundDrawables(mWave, null, null, null);
            mWave.start();
            btCommPrint.setText("CONN");
        }


        if (SPUtils.getBoolean(this, KEY.NUMBERPLATE)){
            btInputNumber.setVisibility(View.VISIBLE);
        }else  btInputNumber.setVisibility(View.GONE);
    }

    @Override
    protected void showConnecting() {
        mWave = new Wave();
        mWave.setBounds(0, 0, 100, 100);
        mWave.setColor(getResources().getColor(R.color.baby_blue));
        btCommPrint.setCompoundDrawables(mWave, null, null, null);
        mWave.start();
        btCommPrint.setText("CONN......");
    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {
        btCommPrint.setText("CONNECTION");
    }

    /**
     * @param userInformation
     */
    @Override
    protected void showUser(UserInformation userInformation) {
        ParkingRecordReportBean reportBean = new ParkingRecordReportBean();
        reportBean.setSerialNumber(TimeUtils.generateSequenceNo());
        reportBean.setUUID(userInformation.getUUID());
        reportBean.setCardId(userInformation.getCardId());
        reportBean.setLicensePlateNumber(userInformation.getLicensePlateNumber());
        reportBean.setCardNumber(isNullString(userInformation.getCardNumber()) ? userInformation.getCardNumber() : "0000000000");
        reportBean.setLastName(userInformation.getName());
        reportBean.setFirstName(userInformation.getName());
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
                if (BluetoothService.IsNoConnection()) {
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
        btCommPrint.setText("Bluetooth Disconnect");
        mWave.stop();
    }

    @OnClick({R.id.bt_ve_read, R.id.bt_comm_print, R.id.bt_repeat, R.id.bt_stop, R.id.bt_input_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ve_read:
                showSlotCard();
                break;
            case R.id.bt_comm_print:
                showDeviceList();
                break;
            case R.id.bt_repeat:
                if (isRepeat) {
                    isRepeat = false;
                    btRepeat.setText(getResources().getString(R.string.repeat_off));
                } else {
                    isRepeat = true;
                    btRepeat.setText(getResources().getString(R.string.repeat_on));
                }
                break;
            case R.id.bt_stop:
                isReader = false;
                break;
            case R.id.bt_input_number:
                showInputNumber();
                break;
        }
    }

    /**
     * 手动输入车牌
     */
    private BaseDialog baseDialog;
    private EditText editNumber;
    private String  licenseNumber;
    private void showInputNumber() {
        if (baseDialog == null){
            View view = ContextUtils.inflate(VehicleEntryActivity.this,R.layout.layout_input_license);
            editNumber = (EditText) view.findViewById(R.id.editText);
            view.findViewById(R.id.tv_cancel).setOnClickListener( new OnExitClickListener());
            view.findViewById(R.id.tv_sure).setOnClickListener( new OnExitClickListener());
            baseDialog = new BaseDialog(VehicleEntryActivity.this,R.style.AlertDialogStyle);
            baseDialog.setContentView(view);
            baseDialog.setCancelable(false);
        }
        baseDialog.show();

    }

    private class OnExitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancel:
                    editNumber.setText("");
                    baseDialog.cancel();
                    break;
                case R.id.tv_sure:
                    licenseNumber = editNumber.getText().toString();
                    if(isNullString(licenseNumber)) {
                        ToastUtils.error(getResources().getString(R.string.error_no)); return;
                    }
                    addReportList(licenseNumber);
                    editNumber.setText("");
                    baseDialog.cancel();
                    break;
            }
        }
    }

    private void addReportList(String licenseNumber) {


        ParkingRecordReportBean reportBean = new ParkingRecordReportBean();
        reportBean.setSerialNumber(TimeUtils.generateSequenceNo());
        reportBean.setUUID("SNAP00000000");
        reportBean.setCardId("SNAP00000000");
        reportBean.setLicensePlateNumber(licenseNumber);
        reportBean.setCardNumber("SNAP00000000");
        reportBean.setLastName("User nformation");
        reportBean.setFirstName("No ");
        reportBean.setPType(1);
        reportBean.setAdminId(1);
        reportBean.setAdminNumber(App.operator);
        reportBean.setTerminalNumber(App.terminalNumber);
        reportBean.setParkingNumber(App.parkingNumber);
        reportBean.setEnterTime(TimeUtils.getCurTimeMills());
        reportBean.setCreateTime(TimeUtils.getCurTimeMills());
        reportBean.setPType(0);
        reportBean.setCreateDayTime(TimeUtils.getCrateDayTime());


        DbHelper.insertEntryVehicle(reportBean, new OnEntryVehicleListener() {
            @Override
            public void success(ParkingRecordReportBean reportBean) {
                if (BluetoothService.IsNoConnection()) {
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
}
