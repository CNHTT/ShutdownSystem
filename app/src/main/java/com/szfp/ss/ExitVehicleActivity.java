package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.szfp.ss.adapter.SerialNumberAdapter;
import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.inter.OnExitVehicleListener;
import com.szfp.ss.utils.DbHelper;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.progress.style.Wave;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.Utils.getContext;

/**
 * author：ct on 2017/9/21 17:31
 * email：cnhttt@163.com
 */

public class ExitVehicleActivity  extends BaseReadActivity {


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
            mWave.setBounds(0,0,60,60);
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
    private ParkingRecordReportBean reportBean;
    private BaseDialog dialog;
    private ListView listView;
    private SerialNumberAdapter adapter;
    @Override
    protected void showUser(final UserInformation userInformation) {


        DbHelper.getParkingExitList(userInformation, new OnExitVehicleListener() {
            @Override
            public void success(final List<ParkingRecordReportBean> list) {
                //查询到的数据
                if(list.size() ==1)
                {//只有一条数据的时候
                    reportBean = list.get(0);
                    addReportBean(userInformation);
                }else
                {//
                    if (dialog == null ){
                        View view = ContextUtils.inflate(ExitVehicleActivity.this,R.layout.dialog_exit_list);
                        dialog = new BaseDialog(ExitVehicleActivity.this,R.style.AlertDialogStyle);
                        dialog.setContentView(view);
                        listView = (ListView) view.findViewById(R.id.lv_no);
                        view.findViewById(R.id.tv_cancel).setOnClickListener( new OnExitClickListener());
                        view.findViewById(R.id.tv_sure).setOnClickListener( new OnExitClickListener());
                        adapter = new SerialNumberAdapter(ExitVehicleActivity.this,list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                reportBean = list.get(position);
                                addReportBean(userInformation);
                                dialog.cancel();
                            }
                        });

                        dialog.setCancelable(false);

                    }else adapter.updateItems(list);

                    dialog.show();


                }

            }

            @Override
            public void error(String str) {
                ToastUtils.error(str);
            }
        });




//        ParkingRecordReportBean reportBean = new ParkingRecordReportBean();
//        reportBean.setSerialNumber(TimeUtils.generateSequenceNo());
//        reportBean.setUUID(userInformation.getUUID());
//        reportBean.setCardId(userInformation.getCardId());
//        reportBean.setLicensePlateNumber(userInformation.getLicensePlateNumber());
//        reportBean.setCardNumber(DataUtils.isNullString(userInformation.getCardNumber())?userInformation.getCardNumber():"0000000000");
//        reportBean.setLastName(userInformation.getLastName());
//        reportBean.setFirstName(userInformation.getFirstName());
//        reportBean.setPType(1);
//        reportBean.setAdminId(1);
//        reportBean.setAdminNumber(App.operator);
//        reportBean.setTerminalNumber(App.terminalNumber);
//        reportBean.setParkingNumber(App.parkingNumber);
//        reportBean.setEnterTime(TimeUtils.getCurTimeMills());
//        reportBean.setCreateTime(TimeUtils.getCurTimeMills());
//        reportBean.setCreateDayTime(TimeUtils.getCrateDayTime());
//
//        DbHelper.insertEntryVehicle(reportBean, new OnEntryVehicleListener() {
//            @Override
//            public void success(ParkingRecordReportBean reportBean) {
//                if (BluetoothService.IsNoConnection()){
//                    ToastUtils.showToast(R.string.not_connected);
//                }
//
//                PrintUtils.printEntryVehicle(reportBean);
//
//            }
//
//            @Override
//            public void error(String str) {
//                ToastUtils.error(str);
//
//            }
//        });



    }

    private void addReportBean(UserInformation userInformation) {
        reportBean.setType(1);
        long exitTime = TimeUtils.getCurTimeMills();
        long parkingTime = exitTime - reportBean.getEnterTime();
        //小时
        int  intTime = (int) (parkingTime/60/60/1000);
        reportBean.setPreTradeBalance(userInformation.getBalance());
        reportBean.setExitTime(exitTime);
        reportBean.setExitCreateTime(TimeUtils.getCurTimeMills());
        reportBean.setCreateDayTime(TimeUtils.getCrateDayTime());
        reportBean.setParkingTime(parkingTime);
        reportBean.setIntTime(intTime);
        String hour = SPUtils.getString(getContext(), KEY.HOUR_FEE);
        double hourFee  = Double.parseDouble(hour);
        if (userInformation.getParkingTimeIsValidEnd()>exitTime){   //购买的时长大于最后停车时长

            DbHelper.updateExitVehicle(reportBean,userInformation);

        }else {
            reportBean.setDeductionMethod(1);
            if (userInformation.getParkingTimeIsValidEnd()>reportBean.getEnterTime())
            {   //BuyTime >   ExitTime

                long endParkingTime = exitTime-userInformation.getParkingTimeIsValidEnd();
                long hourTime= 60*60*1000;
                int endIntTime = (int) (endParkingTime%hourTime==0?endParkingTime/hourTime:endParkingTime/hourTime+1);
                double parkingAmount = hourFee*endIntTime;

                reportBean.setParkingAmount(parkingAmount);
                if (userInformation.getBalance()>parkingAmount){   //余额大于停车费用
                    reportBean.setCardAmount(parkingAmount);
                    userInformation.setBalance(userInformation.getBalance()-parkingAmount);
                    DbHelper.updateExitVehicle(reportBean,userInformation);
                }else {
                    reportBean.setCashAmount(hourFee -userInformation.getBalance());
                    reportBean.setCardAmount(userInformation.getBalance());
                    userInformation.setBalance(0);
                    reportBean.setPostTradeBalance(userInformation.getBalance());
                    DbHelper.updateExitVehicle(reportBean,userInformation);
                }

            }
            else
            {
                long endParkingTime = parkingTime;
                long hourTime= 60*60*1000;
                int endIntTime = (int) (endParkingTime%hourTime==0?endParkingTime/hourTime:endParkingTime/hourTime+1);
                double parkingAmount = hourFee*endIntTime;

                reportBean.setParkingAmount(parkingAmount);
                if (userInformation.getBalance()>parkingAmount){   //余额大于停车费用
                    reportBean.setCardAmount(parkingAmount);
                    userInformation.setBalance(userInformation.getBalance()-parkingAmount);
                    reportBean.setPostTradeBalance(userInformation.getBalance());
                    DbHelper.updateExitVehicle(reportBean,userInformation);
                }else {
                    reportBean.setCashAmount(hourFee -userInformation.getBalance());
                    reportBean.setCardAmount(userInformation.getBalance());
                    userInformation.setBalance(0);
                    reportBean.setPostTradeBalance(userInformation.getBalance());
                    DbHelper.updateExitVehicle(reportBean,userInformation);
                }
            }

        }
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

    private class OnExitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dialog.cancel();
        }
    }
}
