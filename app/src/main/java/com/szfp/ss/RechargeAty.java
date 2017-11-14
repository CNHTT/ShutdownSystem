package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.domain.model.MemberBean;
import com.szfp.ss.domain.model.RechargeBean;
import com.szfp.ss.domain.result.Result;
import com.szfp.ss.domain.result.ResultMember;
import com.szfp.ss.inter.OperationSuccess;
import com.szfp.ss.retrofit.HttpBuilder;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.JsonUtil;
import com.szfp.ss.utils.PrintUtils;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.DataUtils;
import com.szfp.utils.NetworkUtil;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.progress.style.Wave;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.ss.App.logger;

public class RechargeAty extends BaseHFActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_recharge_amount)
    EditText etRechargeAmount;
    @BindView(R.id.bt_comm_print)
    SelectButton btCommPrint;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_sava)
    Button btSava;

    private boolean isPrint=false;
    private Wave mWave;
    private String amount;
    private BaseDialog dialog=null;
    private String cardId;
    private UserInformation userInformation;

    private MemberBean memberBean;
    private RechargeBean rechargeBean;

    private String tsn;

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialog!=null) dialog.cancel();
            isReader=false;
            mCubeGrid.stop();
        }
    };


    @Override
    protected void showDisconnecting() {
        btCommPrint.setText("Bluetooth Disconnect");
        isPrint =false;mWave.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_aty);
        ButterKnife.bind(this);
        toolbar.setTitle("RECHARGE");
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucent(this, 10);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void showWriteSuccess(String num) {

    }


    private BaseDialog memberDialog;
    private TextView mTv_title;
    private TextView mTv_amount;
    private TextView mTv_name;
    private TextView mTv_lpm;
    private TextView mTv_balance;
    private TextView mTv_email;
    private TextView mTv_sure;
    private TextView mTv_cancel;
    @Override
    protected void showReadCard(String cardId, String uuid) {
        if (NetworkUtil.isNetworkAvailable(this)){
            showProgressDialog(R.string.loading);
            new HttpBuilder(AppUrl.CHECKMEMBER)
                    .params("uuid",uuid)
                    .tag(this)
                    .success( s -> {
                        logger.info(s.toString());
                        cancleProgressDialog();
                        ResultMember result= (ResultMember) JsonUtil.stringToObject(s,ResultMember.class);
                        if (result.getCode()==1){
                            memberBean = result.getData();
                            if (memberDialog == null)
                            {
                                View view = ContextUtils.inflate(RechargeAty.this,R.layout.member_info);
                                mTv_title = (TextView) view.findViewById(R.id.tv_title);
                                mTv_amount = (TextView) view.findViewById(R.id.tv_amount);
                                mTv_name = (TextView) view.findViewById(R.id.tv_name);
                                mTv_lpm = (TextView)view. findViewById(R.id.tv_lpm);
                                mTv_balance = (TextView)view. findViewById(R.id.tv_balance);
                                mTv_email = (TextView)view. findViewById(R.id.tv_email);
                                mTv_sure = (TextView) view.findViewById(R.id.tv_sure);
                                mTv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
                                memberDialog = new BaseDialog(RechargeAty.this,R.style.AlertDialogStyle);
                                memberDialog.setContentView(view);
                            }

                            mTv_amount.setText(amount);
                            mTv_title.setText(memberBean.getName());
                            mTv_name.setText(memberBean.getName());
                            mTv_lpm.setText(memberBean.getLpm());
                            mTv_balance.setText(String.valueOf(memberBean.getBalance()));
                            mTv_email.setText(memberBean.getEmail());

                            memberDialog.setCancelable(false);
                            memberDialog.show();

                            mTv_sure.setOnClickListener( v ->{
                                memberDialog.cancel();
                                recharge();
                            });

                            mTv_cancel.setOnClickListener(  v ->{
                                memberDialog.cancel();
                            });


                        }else {

                            showDialogToast("This Card is No User");

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

    private void recharge() {
        rechargeBean = new RechargeBean();
        showProgressDialog(R.string.loading);
        tsn = TimeUtils.generateSequenceNo();
        rechargeBean.setMemberUuid(memberBean.getUuid());
        rechargeBean .setMemberLPM(memberBean.getLpm());
        rechargeBean .setMemberName(memberBean.getName());
        rechargeBean.setTsn(tsn);
        rechargeBean.setRAmount(Double.parseDouble(amount));
        rechargeBean.setAAmount(memberBean.getBalance());
        memberBean.setBalance(memberBean.getBalance()+Double.parseDouble(amount));
        rechargeBean.setBAmount(memberBean.getBalance());
        rechargeBean.setOperateNumber(App.operator);
        rechargeBean.setOperateUuid(App.managerUUID);
        rechargeBean.setDeviceSN(App.terminalUUID);
        rechargeBean.setDeviceNumber(App.terminalNumber);
        rechargeBean.setCompanyUuid(App.companyUUID);
        rechargeBean.setCreateTime(new Date());
        new HttpBuilder(AppUrl.RECHARGE)
                    .params("data",JsonUtil.objectToString(rechargeBean))
                    .params("amount",amount)
                    .tag(this)
                    .success(  s ->{
                        logger.info( "Recharge :" +s);
                        cancleProgressDialog();
                        Result result = (Result) JsonUtil.stringToObject(s,Result.class);
                        if (result.checkCode()){
                            /**
                             * 保存数据库并打印
                             */

                            DbHelper.insertRechargeBean(rechargeBean,memberBean, new OperationSuccess() {
                                @Override
                                public void success() {
                                    PrintUtils.printRechargeRecord(rechargeBean,memberBean);
                                    etRechargeAmount.setText("");
                                }
                            });
                        }
                    })
                    .error( e  ->{
                        cancleProgressDialog();
                        logger.info(e.toString());
                        showDialogToast(e.toString());

                    })
                    .post();
//



    }


    @Override
    protected void showConnecting() {
        btCommPrint.setText("comm......");
        mWave = new Wave();
        mWave.setBounds(0, 0, 100, 100);
        //noinspection deprecation
        mWave.setColor(getResources().getColor(R.color.baby_blue));
        btCommPrint.setCompoundDrawables(mWave, null, null, null);
        mWave.start();
    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {
        btCommPrint.setText("Connection");
    }



    @OnClick({R.id.bt_comm_print, R.id.bt_cancel, R.id.bt_sava})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_comm_print:
                showDeviceList();
                break;
            case R.id.bt_cancel:
                break;
            case R.id.bt_sava:
                if (mChatService.getState() != BluetoothService.STATE_CONNECTED){
                    ToastUtils.error("The printer is not connected");
                    return;
                }
                amount = etRechargeAmount.getText().toString();
                if (DataUtils.isNullString(amount)){
                    ToastUtils.error("Please Enter Amount");
                    return;
                }
                showSlotCard(false);
                break;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
