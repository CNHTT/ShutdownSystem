package com.szfp.ss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import android_serialport_api.SerialPortManager;

import static com.szfp.ss.App.logger;

/**
 * author：ct on 2017/8/25 16:39
 * email：cnhttt@163.com
 */

public class BaseAty extends AppCompatActivity {
    protected  App app;
    protected HandlerThread handlerThread;
    public BaseAty mContext;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;//屏幕宽度
        int height = dm.heightPixels;//屏幕高度
        logger.debug("---WIDTH:"+width+"  -----HEIGHT:"+height);
        app = (App) getApplicationContext();
        mContext=this;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!SerialPortManager.getInstance().isOpen()) {
            SerialPortManager.getInstance().openSerialPort();
        }
        Log.i("whw", "onResume=" + SerialPortManager.getInstance().isOpen());
        handlerThread = app.getHandlerThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SerialPortManager.getInstance().closeSerialPort();
        handlerThread = null;
    }
    protected void showProgressDialog(int resId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(resId));
        progressDialog.show();
    }

    protected void cancleProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }


    public boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }

}
