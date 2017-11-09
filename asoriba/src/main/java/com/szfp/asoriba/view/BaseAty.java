package com.szfp.asoriba.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.szfp.asoriba.App;
import com.szfp.asynctask.AsyncFingerprint;
import com.szfp.utils.AppManager;

import android_serialport_api.SerialPortManager;

import static com.szfp.asoriba.App.logger;

/**
 * author：ct on 2017/10/9 17:29
 * email：cnhttt@163.com
 */

public class BaseAty extends AppCompatActivity {

    protected App app;
    protected HandlerThread handlerThread;
    public BaseAty mContext;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        AppManager.getAppManager().addActivity(this);
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;//屏幕宽度
        int height = dm.heightPixels;//屏幕高度
        logger.debug("---WIDTH:"+width+"  -----HEIGHT:"+height);
        app = (App) getApplicationContext();
        mContext=this;
    }


    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        handlerThread = app.getHandlerThread();

        if (!SerialPortManager.getInstance().isOpen()){
            SerialPortManager.getInstance().openSerialPort();
        }
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
    protected void showProgressDialog(int resId, final AsyncFingerprint asyncFingerprint) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(resId));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    asyncFingerprint.setStop(true);
                }
                return false;
            }
        });
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
