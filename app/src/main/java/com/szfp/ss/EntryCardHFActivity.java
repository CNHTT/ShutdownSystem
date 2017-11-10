package com.szfp.ss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.model.MemberBean;
import com.szfp.ss.domain.result.Result;
import com.szfp.ss.retrofit.HttpBuilder;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.JsonUtil;
import com.szfp.utils.AppManager;
import com.szfp.utils.NetworkUtil;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.DialogSureCancel;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.ss.App.logger;

public class EntryCardHFActivity extends BaseHFActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_card_Id)
    TextView tvCardId;
    @BindView(R.id.tv_first_name)
    TextView tvFirstName;
    @BindView(R.id.tv_last_name)
    TextView tvLastName;
    @BindView(R.id.tv_lpn)
    TextView tvLpn;
    @BindView(R.id.tv_tn)
    TextView tvTn;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_sava)
    Button btSava;
    @BindView(R.id.sl_result)
    ScrollView slResult;


    private MemberBean memberBean;
    private String cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_card_hf);
        ButterKnife.bind(this);
        slResult.setVisibility(View.GONE);
        toolbar.setTitle("PAT CARD PLS");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StatusBarUtil.setTranslucent(this, 10);
        uuid = TimeUtils.getUUID();
        memberBean = (MemberBean) getIntent().getExtras().getSerializable(KEY.MEMBER);
        initView();
    }

    private void initView() {
        showSlotCard(false);
    }

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }
    @Override
    protected void showDisconnecting() {

    }

    @Override
    protected void showWriteSuccess(String num) {
        slResult.setVisibility(View.VISIBLE);
        tvCardId.setText(num);
        tvFirstName.setText(memberBean.getName());
        tvLastName.setText(memberBean.getEmail());
        tvLpn.setText(memberBean.getLpm());
        tvTn.setText(memberBean.getPhone());

        memberBean.setAddManagerUuid(App.managerUUID);
        memberBean.setCompanyUuid(App.companyUUID);
        memberBean.setUuid(uuid);
        memberBean.setCreateTime(new Date());
        memberBean.setCardId(num);

    }

    @Override
    protected void showReadCard(String cardId, String uuid) {
        logger.info("CARDID :" +cardId + "UUID: " +uuid );

        if (NetworkUtil.isNetworkAvailable(this)){
            showProgressDialog(R.string.loading);
            new HttpBuilder(AppUrl.CHECKMEMBER)
                    .params("uuid",uuid)
                    .tag(this)
                    .success( s -> {
                        logger.info(s.toString());
                        cancleProgressDialog();
                        Result result= (Result) JsonUtil.stringToObject(s,Result.class);
                        if (result.getCode()==1){
                            DialogSureCancel dialogSure = new DialogSureCancel(mContext);
                            dialogSure.getTvContent().setText("The card has been used \n Yes: Replace\n NO: Continue");
                            dialogSure.setCancelable(false);
                            dialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadWriteCard();
                                    dialogSure.cancel();
                                }
                            });
                            dialogSure.getTvCancel().setOnClickListener(v ->{
                                dialogSure.cancel();
                            });
                            dialogSure.show();
                        }else {
                            loadWriteCard();
                        }

                    })
                    .error(e ->{
                        logger.info(e.toString());
                        cancleProgressDialog();
                        showDialogToast(e.toString());
                    })
                    .post();


        }




        slResult.setVisibility(View.VISIBLE);

    }

    private void loadWriteCard() {
        showSlotCard(true);
    }

    @OnClick({R.id.bt_cancel, R.id.bt_sava})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                AppManager.getAppManager().finishActivity(EntryCardHFActivity.class);
                AppManager.getAppManager().finishActivity(AddUserAty.class);
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.bt_sava:

                if (NetworkUtil.isNetworkAvailable(this)){
                    showProgressDialog(R.string.loading);
                    new HttpBuilder(AppUrl.AddMember)
                            .params("data",JsonUtil.objectToString(memberBean))
                            .tag(true)
                            .success( s -> {
                                cancleProgressDialog();
                                logger.info( s);
                                Result  result = (Result) JsonUtil.stringToObject(s,Result.class);
                                if (result.getCode()==1){
                                    ToastUtils.success("ADD  SUCCESS!!!");
                                    if (DbHelper.insertMember(memberBean));
                                    logger.info("save local success!" );


                                    AppManager.getAppManager().finishActivity(EntryCardHFActivity.class);
                                    AppManager.getAppManager().finishActivity(AddUserAty.class);
                                    startActivity(new Intent(this,MainActivity.class));
                                }else  showDialogToast(result.getMsg());

                            })
                            .error(  e ->{
                                cancleProgressDialog();
                                showDialogToast(e.toString());
                            })
                            .post();
                }

                break;
        }
    }
}
