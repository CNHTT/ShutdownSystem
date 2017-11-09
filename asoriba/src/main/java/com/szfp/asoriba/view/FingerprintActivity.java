package com.szfp.asoriba.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.szfp.asoriba.R;
import com.szfp.asynctask.AsyncFingerprint;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android_serialport_api.FingerprintAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FingerprintActivity extends BaseAty {

    @BindView(R.id.register)
    SelectButton register;
    @BindView(R.id.validate)
    SelectButton validate;
    @BindView(R.id.register2)
    SelectButton register2;
    @BindView(R.id.validate2)
    SelectButton validate2;
    @BindView(R.id.clear_flash)
    SelectButton clearFlash;
    @BindView(R.id.calibration)
    SelectButton calibration;
    @BindView(R.id.bt_registe)
    SelectButton btRegiste;
    @BindView(R.id.bt_save)
    SelectButton btSave;
    @BindView(R.id.et_fingerId)
    EditText etFingerId;
    @BindView(R.id.bt_compares)
    SelectButton btCompares;
    @BindView(R.id.bt_clear)
    SelectButton btClear;
    @BindView(R.id.upfail)
    TextView upfail;
    @BindView(R.id.fingerprintImage)
    ImageView fingerprintImage;


    private String[] m;

    private AsyncFingerprint asyncFingerprint;

    private byte[] model;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AsyncFingerprint.SHOW_PROGRESSDIALOG:
                    cancleProgressDialog();
                    showProgressDialog((Integer) msg.obj,asyncFingerprint);
                    break;
                case AsyncFingerprint.SHOW_FINGER_IMAGE:
                    // imageNum++;
                    // upfail.setText("上传成功：" + imageNum + "\n" + EAR"上传失败：" +
                    // failTime+ "\n" + "解析出错：" + missPacket);
                    showFingerImage(msg.arg1, (byte[]) msg.obj);
                    break;
                case AsyncFingerprint.SHOW_FINGER_MODEL:
                    FingerprintActivity.this.model = (byte[]) msg.obj;
                    if (FingerprintActivity.this.model != null) {
                        Log.i("whw", "#################model.length=" + FingerprintActivity.this.model.length);
                    }
                    cancleProgressDialog();
                    // ToastUtil.showToast(FingerprintActivity.this,
                    // "pageId="+msg.arg1+" store="+msg.arg2);
                    break;
                case AsyncFingerprint.REGISTER_SUCCESS:
                    cancleProgressDialog();
                    if (msg.obj != null) {
                        Integer id = (Integer) msg.obj;
                        ToastUtils.showToast(
                                getString(R.string.register_success) + "  pageId="
                                        + id);
                    } else {
                        ToastUtils.showToast(
                                R.string.register_success);
                    }

                    break;
                case AsyncFingerprint.REGISTER_FAIL:
                    cancleProgressDialog();
                    ToastUtils.showToast(FingerprintActivity.this, R.string.register_fail);
                    break;
                case AsyncFingerprint.VALIDATE_RESULT1:
                    cancleProgressDialog();
                    showValidateResult((Boolean) msg.obj);
                    break;
                case AsyncFingerprint.VALIDATE_RESULT2:
                    cancleProgressDialog();
                    Integer r = (Integer) msg.obj;
                    if (r != -1) {
                        ToastUtils.showToast(
                                getString(R.string.verifying_through) + "  pageId="
                                        + r);
                    } else {
                        showValidateResult(false);
                    }
                    break;
                case AsyncFingerprint.UP_IMAGE_RESULT:
                    cancleProgressDialog();
                    ToastUtils
                            .showToast( (Integer) msg.obj);
                    // failTime++;
                    // upfail.setText("上传成功：" + imageNum + "\n" + "上传失败：" +
                    // failTime+ "\n" + "解析出错：" + missPacket);
                    break;
                default:
                    break;
            }
        }

    };

    private void showValidateResult(boolean matchResult) {
        if (matchResult) {
            ToastUtils.showToast( R.string.verifying_through);
        } else {
            ToastUtils.showToast(R.string.verifying_fail);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        ButterKnife.bind(this);
    }
    private String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private void writeToFile(byte[] data) {
        String dir = rootPath + "/fingerprint_image";
        File dirPath = new File(dir);
        if (!dirPath.exists()) {
            dirPath.mkdir();
        }

        String filePath = dir + "/" + System.currentTimeMillis() + ".bmp";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showFingerImage(int fingerType, byte[] data) {
        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
        // saveImage(data);
        fingerprintImage.setBackgroundDrawable(new BitmapDrawable(image));
        writeToFile(data);
    }

    @OnClick({R.id.register, R.id.validate, R.id.register2, R.id.validate2, R.id.clear_flash, R.id.calibration, R.id.bt_registe, R.id.bt_save, R.id.bt_compares, R.id.bt_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                asyncFingerprint.setStop(false);
                asyncFingerprint.register();
                break;
            case R.id.validate:
                if (model != null) {
                    asyncFingerprint.validate(model);
                } else {
                    ToastUtils.showToast(R.string.first_register);
                }
                break;
            case R.id.register2:
                asyncFingerprint.register2();
                break;
            case R.id.validate2:
                asyncFingerprint.validate2();
                break;
            case R.id.clear_flash:
                asyncFingerprint.PS_Empty();
                break;
            case R.id.calibration:
                Log.i("whw", "calibration start");
                asyncFingerprint.PS_Calibration();
                break;
            case R.id.bt_registe:
                asyncFingerprint.setStop(false);
                asyncFingerprint.register();
                break;
            case R.id.bt_save:
                FingerModels fingerModels = new FingerModels();
                String id = etFingerId.getText().toString();
                if (!TextUtils.isEmpty(id)) {
                    fingerModels.setId(id);
                    fingerModels.setModel(model);
                    fingerModels.save();
                    if (fingerModels.save()) {
                        ToastUtils.showToast("save success");
                    } else {
                        ToastUtils.showToast("save fail");
                    }
                } else {
                    ToastUtils.showToast(R.string.noID);
                }
                break;
            case R.id.bt_compares:
                if (TextUtils.isEmpty(etFingerId.getText())) {
                    ToastUtils.showToast( R.string.noID);
                } else {
                    List<FingerModels> list = DataSupport.select("model").where("model_ID = ?", etFingerId.getText().toString())
                            .find(FingerModels.class);
                    if (list.size() > 0) {
                        asyncFingerprint.validate(list.get(0).getModel());
                    } else {
                        ToastUtils.showToast( "no model");
                    }
                }
                break;
            case R.id.bt_clear:
                DataSupport.deleteAll(FingerModels.class);
                ToastUtils.showToast(R.string.clear_flash_success);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        cancleProgressDialog();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("whw", "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("whw", "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData2();
        Log.i("whw", "onResume");
    }

    private void initData2() {

        asyncFingerprint = new AsyncFingerprint(handlerThread.getLooper(),
                mHandler);
        asyncFingerprint.setOnEmptyListener(new AsyncFingerprint.OnEmptyListener() {

            @Override
            public void onEmptySuccess() {
                ToastUtils.showToast( R.string.clear_flash_success);

            }

            @Override
            public void onEmptyFail() {
                ToastUtils.showToast( R.string.clear_flash_fail);

            }
        });

        asyncFingerprint.setOnCalibrationListener(new AsyncFingerprint.OnCalibrationListener() {

            @Override
            public void onCalibrationSuccess() {
                Log.i("whw", "onCalibrationSuccess");
                ToastUtils.showToast(R.string.calibration_success);
            }

            @Override
            public void onCalibrationFail() {
                Log.i("whw", "onCalibrationFail");
                ToastUtils.showToast(R.string.calibration_fail);

            }
        });
        asyncFingerprint.setFingerprintType(FingerprintAPI.BIG_FINGERPRINT_SIZE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancleProgressDialog();
        asyncFingerprint.setStop(true);
        Log.i("whw", "onPause");
    }
}
