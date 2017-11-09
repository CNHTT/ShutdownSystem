package com.szfp.asoriba.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.szfp.asoriba.App;
import com.szfp.asoriba.R;
import com.szfp.asynctask.AsyncFingerprint;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.LoadingAlertDialog;

import android_serialport_api.FingerprintAPI;
import android_serialport_api.SerialPortManager;

import static com.szfp.asoriba.App.logger;

/**
 * author：ct on 2017/10/11 14:24
 * email：cnhttt@163.com
 */

public abstract class BaseActivity extends AppCompatActivity {
    private LoadingAlertDialog loadingAlertDialog;
    protected App app;
    protected HandlerThread handlerThread;


    protected AsyncFingerprint asyncFingerprint;
    protected byte[] baseModel;


    private ProgressDialog progressDialog;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  AsyncFingerprint.SHOW_PROGRESSDIALOG:
                    cancleProgressDialog();
                    showProgressDialog((Integer) msg.obj);
                    break;
                case AsyncFingerprint.SHOW_FINGER_IMAGE:
                    showFingerImage(msg.arg1,(byte[])msg.obj);
                    break;
                case  AsyncFingerprint.SHOW_FINGER_MODEL:
                    baseModel = (byte[]) msg.obj;
                    if (baseModel!=null){
                        logger.debug("WhW " + baseModel.length);

                    }
                    cancleProgressDialog();
                    break;
                case  AsyncFingerprint.REGISTER_SUCCESS:
                    cancleProgressDialog();
                    if (msg.obj!=null){
                        Integer id = (Integer) msg.obj;
                        ToastUtils.showToast(getString(R.string.register_success)+" PID ="+id);
                        showRegisterSuccess(id);
                    }else {
                        ToastUtils.showToast(R.string.register_success);
                    }
                    break;
                case  AsyncFingerprint.REGISTER_FAIL:
                    cancleProgressDialog();
                    ToastUtils.showToast(R.string.register_fail);
                    break;
                case AsyncFingerprint.VALIDATE_RESULT1:
                    cancleProgressDialog();
                    showValidateResult((Boolean) msg.obj);
                    break;
                case  AsyncFingerprint.VALIDATE_RESULT2:
                    cancleProgressDialog();
                    Integer r = (Integer) msg.obj;
                    if (r != -1) {
                        ToastUtils.showToast(getString(R.string.verifying_through) +"PID=" + r);
                        showValidateResult(r);
                    } else {
                        showValidateResult(false);
                    }
                    break;
                case AsyncFingerprint.UP_IMAGE_RESULT:
                    cancleProgressDialog();
                    ToastUtils.showToast( (Integer) msg.obj);
                    // failTime++;
                    // upfail.setText("上传成功：" + imageNum + "\n" + "上传失败：" +
                    // failTime+ "\n" + "解析出错：" + missPacket);
                    break;
                default:
                    break;
            }
        }
    };

    protected abstract void showValidateResult(Integer r);

    /**
     * 验证结果
     * @param obj
     */
    protected abstract void showValidateResult(Boolean obj);

    protected abstract void showRegisterSuccess(Integer id);

    /**
     * 显示指纹图片
     * @param arg1
     * @param obj
     */
    protected abstract void showFingerImage(int arg1, byte[] obj);

    private void cancleProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    private void showProgressDialog(int resId) {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        app = (App) getApplicationContext();



    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SerialPortManager.getInstance().isOpen()){
            SerialPortManager.getInstance().openSerialPort();
        }
        Log.i("whw", "onResume=" + SerialPortManager.getInstance().isOpen());
        handlerThread = app.getHandlerThread();

        initData();
    }

    private void initData() {
        asyncFingerprint = new AsyncFingerprint(handlerThread.getLooper(),mHandler);
        asyncFingerprint.setFingerprintType(FingerprintAPI.BIG_FINGERPRINT_SIZE);
        asyncFingerprint.setOnEmptyListener(new AsyncFingerprint.OnEmptyListener() {

            @Override
            public void onEmptySuccess() {
                ToastUtils.showToast(
                        R.string.clear_flash_success);

            }

            @Override
            public void onEmptyFail() {
                ToastUtils.showToast(
                        R.string.clear_flash_fail);

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        SerialPortManager.getInstance().closeSerialPort();
        handlerThread = null;
    }

    public   void showLoadingDialog(){
        if (loadingAlertDialog==null)
            loadingAlertDialog = new LoadingAlertDialog(this);
        loadingAlertDialog.show("加载中...");
    }

    public void dismissLoadingDialog(){
        if (loadingAlertDialog!=null)loadingAlertDialog.dismiss();
    }

    public void stopAsy(AsyncFingerprint asyncFingerprint){
        asyncFingerprint.setStop(true);
    };
}
