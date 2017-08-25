package com.szfp.shutdownsystem;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android_serialport_api.SerialPortManager;

/**
 * author：ct on 2017/8/25 16:39
 * email：cnhttt@163.com
 */

public class BaseAty extends AppCompatActivity {
    protected  App app;
    protected HandlerThread handlerThread;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App) getApplicationContext();
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



}
