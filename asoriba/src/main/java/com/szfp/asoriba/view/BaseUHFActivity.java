package com.szfp.asoriba.view;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

import com.szfp.asoriba.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPortManager;

/**
 * author：ct on 2017/10/13 11:11
 * email：cnhttt@163.com
 */

public class BaseUHFActivity  extends AppCompatActivity{

    protected static final int MSG_SHOW_EPC_INFO = 1;
    protected static final int MSG_DISMISS_CONNECT_WAIT_SHOW = 2;
    protected static final int INVENTORY_OVER = 3;
    protected static final int INFO_INV_SUCCESS = 4;
    protected static final int INFO_INV_FAIL = 5;
    protected static final int INFO_STOPINV_SUCCESS = 6;
    protected static final int INFO_STOPINV_FAIL = 7;
    protected static final int INFO_DISCONNECT_SUCCESS = 8;
    protected static final int INFO_DISCONNECT_FAIL = 9;
    protected static int tagCount = 0;
    protected static int tagTimes = 0;
    protected static List<String> tagInfoList = new ArrayList<String>();
    protected static List<String> name = new ArrayList<String>();
    protected static HashMap<String,String> nameInfoList = new HashMap<String,String>();
    protected int exitcount = 1;
    protected static HashMap<String, Integer> number = new HashMap<String, Integer>();
    protected static MediaPlayer mediaPlayer = null;
    protected ExecutorService pool;

    private ProgressDialog progressDialog;
    @Override
    protected void onResume() {
        pool = Executors.newSingleThreadExecutor();
        mediaPlayer = MediaPlayer.create(this, R.raw.sfz);
        super.onResume();
        if (!SerialPortManager.getInstance().isOpen()){
            SerialPortManager.getInstance().openSerialPort();
        }
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        mediaPlayer = null;
        pool.shutdown();
        pool = null;
        super.onPause();
        SerialPortManager.getInstance().closeSerialPort();
    }
    protected void showProgressDialog(String resId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(resId);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void cancleProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }
}
