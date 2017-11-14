package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.szfp.ss.adapter.SerialNumberAdapter;
import com.szfp.ss.adapter.TsnAdapter;
import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.domain.model.ParkingRecordBean;
import com.szfp.ss.domain.result.ResultMember;
import com.szfp.ss.domain.result.ResultParkingRecordBean;
import com.szfp.ss.inter.OnExitVehicleListener;
import com.szfp.ss.retrofit.HttpBuilder;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.JsonUtil;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.NetworkUtil;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.progress.style.Wave;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.ss.App.logger;
import static com.szfp.utils.DataUtils.isNullString;
import static com.szfp.utils.Utils.getContext;

/**
 * author：ct on 2017/9/21 17:31
 * email：cnhttt@163.com
 */

public class ExitVehicleActivity  extends BaseHFActivity {


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

    private ParkingRecordBean parkingRecordBean;
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

    @Override
    protected void showWriteSuccess(String num) {

    }

    @Override
    protected void showReadCard(String cardId, String uuid) {
        if (NetworkUtil.isNetworkAvailable(this)){
            showProgressDialog(R.string.loading);
            new HttpBuilder(AppUrl.CHECKMEMBER)
                    .params("uuid",uuid)
                    .tag(this)
                    .success( s -> {
                        logger.info(s.toString());

                        ResultMember result= (ResultMember) JsonUtil.stringToObject(s,ResultMember.class);
                        if (result.getCode()==1){
                            memberBean = result.getData();
                            loadParkingRecordList();
                        }else {
                            cancleProgressDialog();
                            showNoUser();
                        }

                    })
                    .error(e ->{
                        logger.info(e.toString());
                        cancleProgressDialog();
                        showDialogToast(e.toString());
                    })
                    .post();
        }else {

        }




    }

    private TsnAdapter tsnAdapter;
    private void loadParkingRecordList() {

        new HttpBuilder(AppUrl.LOADPARKINGLIST)
                .params("memberUUid",memberBean.getUuid())
                .tag(this)
                .success(  s  ->{
                    cancleProgressDialog();
                    logger.info(s);
                    ResultParkingRecordBean result  = (ResultParkingRecordBean) JsonUtil.stringToObject(s,ResultParkingRecordBean.class);
                    if (result.checkCode()){
                        ArrayList<ParkingRecordBean> list = result.getData();
                        //查询到的数据
                        if(list.size() ==1)
                        {//只有一条数据的时候
                            parkingRecordBean = list.get(0);
//                            addReportLicenseBean();
                        }else
                        {//
                            View view = ContextUtils.inflate(ExitVehicleActivity.this,R.layout.dialog_exit_list);
                            dialog = new BaseDialog(ExitVehicleActivity.this,R.style.AlertDialogStyle);
                            dialog.setContentView(view);
                            listView = (ListView) view.findViewById(R.id.lv_no);
                            view.findViewById(R.id.tv_cancel).setOnClickListener( new OnExitClickListener());
                            view.findViewById(R.id.tv_sure).setOnClickListener( new OnExitClickListener());
                            tsnAdapter = new TsnAdapter(ExitVehicleActivity.this,list);
                            listView.setAdapter(tsnAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    parkingRecordBean = list.get(position);
                                    dialog.cancel();
                                }
                            });

                            dialog.setCancelable(false);


                            dialog.show();


                        }





                    }
                })
                .error(   e ->{
                    cancleProgressDialog();
                    logger.info(e.toString());
                })
                .post();
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


        if (SPUtils.getBoolean(this, KEY.NUMBERPLATE)){
            btInputNumber.setVisibility(View.VISIBLE);
        }else  btInputNumber.setVisibility(View.GONE);
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
        btCommPrint.setText("CONNECTION");
    }

    /**
     *
     * @param userInformation
     */
    private ParkingRecordReportBean reportBean;
    private BaseDialog dialog;
    private ListView listView;
    private SerialNumberAdapter adapter;
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
                    reportBean.setCashAmount(parkingAmount -userInformation.getBalance());
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

    @OnClick({R.id.bt_ve_read, R.id.bt_comm_print, R.id.bt_repeat, R.id.bt_stop, R.id.bt_input_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ve_read:
                showSlotCard(false);
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
            View view = ContextUtils.inflate(ExitVehicleActivity.this,R.layout.layout_input_license);
            editNumber = (EditText) view.findViewById(R.id.editText);
            view.findViewById(R.id.tv_cancel).setOnClickListener( new OnExitAClickListener());
            view.findViewById(R.id.tv_sure).setOnClickListener( new OnExitAClickListener());
            baseDialog = new BaseDialog(ExitVehicleActivity.this,R.style.AlertDialogStyle);
            baseDialog.setContentView(view);
            baseDialog.setCancelable(false);
        }
        baseDialog.show();

    }

    private class OnExitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

                    dialog.cancel();
            }
    }

    private void addReportList(String licenseNumber) {
        DbHelper.getParkingExitStrList(licenseNumber, new OnExitVehicleListener() {
            @Override
            public void success(final List<ParkingRecordReportBean> list) {
                //查询到的数据
                if(list.size() ==1)
                {//只有一条数据的时候
                    reportBean = list.get(0);
                    addReportLicenseBean();
                }else
                {//
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
                                addReportLicenseBean();
                                dialog.cancel();
                            }
                        });

                        dialog.setCancelable(false);


                    dialog.show();


                }

            }

            @Override
            public void error(String str) {
                ToastUtils.error(str);
            }
        });
    }

    private void addReportLicenseBean() {
        reportBean.setType(1);
        long exitTime = TimeUtils.getCurTimeMills();
        long parkingTime = exitTime - reportBean.getEnterTime();
        //小时
        int  intTime = (int) (parkingTime/60/60/1000);
        reportBean.setPreTradeBalance(0);
        reportBean.setExitTime(exitTime);
        reportBean.setExitCreateTime(TimeUtils.getCurTimeMills());
        reportBean.setCreateDayTime(TimeUtils.getCrateDayTime());
        reportBean.setParkingTime(parkingTime);
        reportBean.setIntTime(intTime);
        String hour = SPUtils.getString(getContext(), KEY.HOUR_FEE);
        double hourFee  = Double.parseDouble(hour);

                long endParkingTime = parkingTime;
                long hourTime= 60*60*1000;
                int endIntTime = (int) (endParkingTime%hourTime==0?endParkingTime/hourTime:endParkingTime/hourTime+1);
                double parkingAmount = hourFee*endIntTime;

                reportBean.setParkingAmount(parkingAmount);
                reportBean.setCashAmount(parkingAmount);
                reportBean.setCardAmount(0);
                reportBean.setPostTradeBalance(0);
                DbHelper.updateExitLicenseVehicle(reportBean);


        }


    private  class OnExitAClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                    editNumber.setText("");
                    baseDialog.cancel();
                    break;
                case R.id.tv_sure:
                    licenseNumber = editNumber.getText().toString();
                    if (isNullString(licenseNumber)) {
                        ToastUtils.error(getResources().getString(R.string.error_no));
                        return;
                    }
                    addReportList(licenseNumber);
                    editNumber.setText("");
                    baseDialog.cancel();
                    break;
            }
        }
    }
}
