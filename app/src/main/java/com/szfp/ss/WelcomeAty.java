package com.szfp.ss;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.szfp.ss.domain.CompanyInfoBean;
import com.szfp.ss.domain.DeviceBean;
import com.szfp.ss.domain.ManagerBean;
import com.szfp.ss.domain.ParkingLotBean;
import com.szfp.ss.utils.CacheData;
import com.szfp.ss.utils.JsonUtil;
import com.szfp.utils.SPUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.szfp.ss.App.logger;

public class WelcomeAty extends BaseAty {

    Disposable dis;

    @Override
    protected void showDisconnecting() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;//屏幕宽度
        int height = dm.heightPixels;//屏幕高度
        logger.debug("---WIDTH:"+width+"  -----HEIGHT:"+height);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_welcome_aty);
        logger.debug("----------LOAD WELCOME-----------");

        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        dis=d;
                    }

                    @Override
                    public void onNext(Long value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        //完成时调用
                        toLogin();

                    }
                });


    }

    private ManagerBean managerBean;
    private ParkingLotBean lot;
    private CompanyInfoBean companyInfoBean;
    private DeviceBean deviceBean;
    private void toLogin() {

        //加载停车场/管理员/公司
        if (SPUtils.getBoolean(this, CacheData.IsLogin)){
            showProgressDialog(R.string.loading);
            managerBean = (ManagerBean) JsonUtil.stringToObject(SPUtils.getString(this,CacheData.LoginManager),ManagerBean.class);
            lot = (ParkingLotBean) JsonUtil.stringToObject(SPUtils.getString(this,CacheData.ParkingLot),ParkingLotBean.class);
            companyInfoBean = (CompanyInfoBean) JsonUtil.stringToObject(SPUtils.getString(this,CacheData.CompanyInfo),CompanyInfoBean.class);
            deviceBean = (DeviceBean) JsonUtil.stringToObject(SPUtils.getString(this,CacheData.DeviceInfo),DeviceBean.class);
            App.companyUUID = managerBean.getCompanyUUID();
            App.operator = managerBean.getNumber();
            App.managerUUID =managerBean.getUUID();

            App.companyName = companyInfoBean.getName();
            App.tel =companyInfoBean.getContactNumber();
            App.website = companyInfoBean.getWebsite();
            App.address = companyInfoBean.getAddress();

            App.terminalNumber =deviceBean.getNumber();
            App.terminalUUID = deviceBean.getUuid();

            App.parkingLotUUID =lot.getUuid();
            App.parkingNumber =lot.getNumber();
            cancleProgressDialog();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(this,LoginAty.class));
            finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancel();
    }

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }

    /**
     * 取消订阅
     */
    public  void cancel(){
        if(dis!=null&&!dis.isDisposed()){
            dis.dispose();
            logger.debug("====定时器取消======");
        }
    }
}
