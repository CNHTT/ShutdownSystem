package com.szfp.ss;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.szfp.ss.adapter.ParkingLotAdapter;
import com.szfp.ss.domain.CompanyInfoBean;
import com.szfp.ss.domain.DeviceBean;
import com.szfp.ss.domain.ManagerBean;
import com.szfp.ss.domain.ParkingLotBean;
import com.szfp.ss.domain.result.Result;
import com.szfp.ss.domain.result.ResultCompany;
import com.szfp.ss.domain.result.ResultManagerBean;
import com.szfp.ss.retrofit.HttpBuilder;
import com.szfp.ss.retrofit.HttpUtil;
import com.szfp.ss.utils.CacheData;
import com.szfp.ss.utils.JsonUtil;
import com.szfp.utils.AlertAnimateUtil;
import com.szfp.utils.AndroidBug5497Workaround;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.KeyboardUtils;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.dialog.DialogSure;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.ss.App.logger;
import static com.szfp.utils.Utils.getContext;

/**
 * A login screen that offers login via email/password.
 */
public class LoginAty extends BaseAty {


    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.iv_clean_phone)
    ImageView ivCleanPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.clean_password)
    ImageView cleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView ivShowPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private String name;
    private String pass;


    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private float scale = 0.6f; //logo缩放比例
    private int height = 0;
    private ManagerBean managerBean;

    private BaseDialog parkLotDialog;
    private ListView listView;
    private ParkingLotAdapter lotAdapter;
    private ParkingLotBean lot ;


    @Override
    protected void showDisconnecting() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_aty);
        ButterKnife.bind(this);

        StatusBarUtil.setTransparent(this);

        if (isFullScreen(this))
            AndroidBug5497Workaround.assistActivity(this);

        initView();
        initEvent();

    }

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }

    private void initEvent() {
        dialogSure = new DialogSure(mContext);
        dialogSure.setCancelable(false);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ivCleanPhone.getVisibility() == View.GONE) {
                    ivCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivCleanPhone.setVisibility(View.GONE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && cleanPassword.getVisibility() == View.GONE) {
                    cleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    cleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    ToastUtils.error("Please enter a number or letter");
                    s.delete(temp.length() - 1, temp.length());
                    etPassword.setSelection(s.length());
                }
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        scrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    Log.e("wenzhihao", "up------>" + (oldBottom - bottom));
                    int dist = content.getBottom() - bottom;
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        AlertAnimateUtil.zoomIn(logo, 0.6f, dist);
                    }

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
                    if ((content.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", content.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                        AlertAnimateUtil.zoomOut(logo, 0.6f);
                    }
                }
            }
        });

    }

    private void initView() {
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3;//弹起高度为屏幕高度的1/3
    }

    @OnClick({R.id.logo, R.id.iv_clean_phone, R.id.clean_password, R.id.iv_show_pwd, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logo:
                break;
            case R.id.iv_clean_phone:
                etMobile.setText("");
                break;
            case R.id.clean_password:
                etPassword.setText("");
                break;
            case R.id.iv_show_pwd:
                if (etPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowPwd.setImageResource(R.mipmap.pass_visuable);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowPwd.setImageResource(R.mipmap.pass_gone);
                }
                String pwd = etPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etPassword.setSelection(pwd.length());
                break;
            case R.id.btn_login:
                showProgressDialog(R.string.net_load);
                KeyboardUtils.hideSoftInput(mContext);
                name  =etMobile .getText() .toString();
                pass =  etPassword.getText().toString();

                new HttpBuilder(AppUrl.LOGIN)
                        .params("name",name)
                        .params("pwd",pass)
                        .tag(this).success( s ->{
                            cancleProgressDialog();
                            logger.info("s:"+s.toString());
                            ResultManagerBean result= (ResultManagerBean) JsonUtil.stringToObject(s,ResultManagerBean.class);
                            if (result.getCode() ==Result.OK){
                                ManagerBean managerBean = result.getData();
                                App.companyUUID = managerBean.getCompanyUUID();
                                App.operator = managerBean.getNumber();
                                App.managerUUID =managerBean.getUUID();
                                //保存最后一次登陆的会员信息
                                SPUtils.putString(getContext(), CacheData.LoginManager,JsonUtil.objectToString(result.getData()));
                                //获取停车场信息
                                loadParkingLot(managerBean.getCompanyUUID(),managerBean.getUUID(),managerBean.getParkingUuid());


                            }else {
                                showDialogToast(result.getMsg());
                            }

                        }).error( e ->{
                            cancleProgressDialog();
                            logger.info("e"+e.toString());
                            showDialogToast("Please check network!!");
                        }).post();




                break;
        }
    }


    /**
     * 获取 停车场.公司信息
     * @param companyUUID    公司
     * @param uuid          用户
     * @param parkingUuid   管理的停车场
     */
    private void loadParkingLot(String companyUUID, String uuid, String parkingUuid) {
        try {
            String sn = "VA3005";
            showProgressDialog(R.string.net_load_data);
            new HttpBuilder(AppUrl.COMPANY)
                    .params("sn",sn)
                    .params("companyUUID",companyUUID)
                    .params("uuid",uuid)
                    .params("parkingUuid",parkingUuid)
                    .tag(this)
                    .success(  s ->{
                        cancleProgressDialog();
                        logger.info(s);
                        ResultCompany result = (ResultCompany) JsonUtil.stringToObject(s,ResultCompany.class);
                        if (result.getCode()==1){
                            initData(result.getData());
                        }else {
                            showDialogToast(result.getMsg());
                        }
                    })
                    .error( e -> {
                        cancleProgressDialog();
                        logger.info(e.toString());
                        showDialogToast(e.toString());
                    })
                    .post();
        }catch (Exception e){
            showDialogToast("please check data!!!");
        }




    }

    /**
     * 初始化加载数据
     * @param data
     */
    private void initData(CompanyInfoBean data) {
        //加载设备数据
        DeviceBean deviceBean = data.getDevice();

        SPUtils.putString(this,CacheData.DeviceInfo,JsonUtil.objectToString(deviceBean));

        /**
         * 加载公司信息
         */
        SPUtils.putString(this,CacheData.CompanyInfo,JsonUtil.objectToString(data));
        App.companyName = data.getName();
        App.tel =data.getContactNumber();
        App.website = data.getWebsite();
        App.address = data.getAddress();

        App.terminalNumber =deviceBean.getNumber();
        App.terminalUUID = deviceBean.getUuid();
        //加载停车场信息
        List<ParkingLotBean> lotList =data.getLotList();
        if (lotList.size()==1){
            lot = lotList.get(0);
            loadMainActivity();

        }else {
            if (parkLotDialog == null ){
                View view = ContextUtils.inflate(this,R.layout.dialog_exit_list);
                parkLotDialog = new BaseDialog(this,R.style.AlertDialogStyle);
                parkLotDialog.setContentView(view);
                listView = (ListView) view.findViewById(R.id.lv_no);
                view.findViewById(R.id.tv_cancel).setOnClickListener( new OnExitClickListener());
                view.findViewById(R.id.tv_sure).setOnClickListener( new OnExitClickListener());
                lotAdapter = new ParkingLotAdapter(this,lotList);
                listView.setAdapter(lotAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        lot = lotList.get(0);
                        loadMainActivity();
                    }
                });

                parkLotDialog.setCancelable(false);

            }else lotAdapter.updateItems(lotList);

            parkLotDialog.show();


        }




    }

    private void loadMainActivity() {
        SPUtils.putString(LoginAty.this, CacheData.ParkingLot, JsonUtil.objectToString(lot));
        SPUtils.putBoolean(this,CacheData.IsLogin,true);
        App.parkingLotUUID =lot.getUuid();
        App.parkingNumber =lot.getNumber();
        startActivity(new Intent(LoginAty.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(this);
    }

    private class OnExitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            parkLotDialog.cancel();
        }
    }
}

