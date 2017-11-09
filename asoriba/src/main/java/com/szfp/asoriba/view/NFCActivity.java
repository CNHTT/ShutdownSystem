package com.szfp.asoriba.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.szfp.asoriba.App;
import com.szfp.asoriba.R;
import com.szfp.asynctask.AsyncM1Card;
import com.szfp.utils.DataUtils;
import com.szfp.utils.RegexUtils;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;

import android_serialport_api.M1CardAPI;
import android_serialport_api.SerialPortManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NFCActivity extends BaseAty {

    @BindView(R.id.spinner_card_type)
    Spinner spinnerCardType;
    @BindView(R.id.spinner_pwd_type)
    Spinner spinnerPwdType;
    @BindView(R.id.tv_card_name)
    TextView tvCardName;
    @BindView(R.id.ed_pwd_a)
    EditText edPwdA;
    @BindView(R.id.ed_pwd_b)
    EditText edPwdB;
    @BindView(R.id.ed_block_num)
    EditText edBlockNum;
    @BindView(R.id.ed_write_block)
    EditText edWriteBlock;
    @BindView(R.id.ed_write_pwd)
    EditText edWritePwd;
    @BindView(R.id.ed_read_block)
    TextView edReadBlock;
    @BindView(R.id.func_title)
    TextView funcTitle;
    @BindView(R.id.btn_getCardNum)
    SelectButton btnGetCardNum;
    @BindView(R.id.btn_sendCardPwd)
    SelectButton btnSendCardPwd;
    @BindView(R.id.btn_validatePwd)
    SelectButton btnValidatePwd;
    @BindView(R.id.io_func_title)
    TextView ioFuncTitle;
    @BindView(R.id.btn_write)
    SelectButton btnWrite;
    @BindView(R.id.btn_update)
    SelectButton btnUpdate;
    @BindView(R.id.btn_read)
    SelectButton btnRead;
    @BindView(R.id.tips)
    TextView tips;

    private App app;


    private ArrayAdapter<String> mAdapterCardType,mAdapterPwdType;
    private static final  String[] cardType = {"S50","S70"};
    private static final  String[] pwdType  = {"KEY-A","KEY-B"};
    private static final  int[]    keyType  = {M1CardAPI.KEY_A,M1CardAPI.KEY_B};
    private static int      NUM  =1;
    private static int  mKeyType = M1CardAPI.KEY_A;
    private String defaultKeyA = "ffffffffffff";//默认密码Ａ
    private String defaultKeyB = "ffffffffffff";//默认密码B


    private AsyncM1Card reader;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        mAdapterCardType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cardType);
        mAdapterCardType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉列表的风格
        spinnerCardType.setAdapter(mAdapterCardType);

        mAdapterPwdType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, pwdType);
        mAdapterPwdType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// 设置下拉列表的风格
        spinnerPwdType.setAdapter(mAdapterPwdType);
        spinnerPwdType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                mKeyType = keyType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }
    private void initData() {
        edPwdA.setText(defaultKeyA);
        edPwdB.setText(defaultKeyB);

        app = (App) getApplicationContext();
        reader = new AsyncM1Card(app.getHandlerThread().getLooper());
        reader.setOnReadCardNumListener(new AsyncM1Card.OnReadCardNumListener() {
            @Override
            public void onReadCardNumSuccess(String num) {

                Log.d("jokey", "num:"+num);
                tvCardName.setText(num);
                tips.setText(R.string.m1_str_get_success);
                cancleProgressDialog();
            }

            @Override
            public void onReadCardNumFail(int confirmationCode) {
                tvCardName.setText("");
                cancleProgressDialog();
                if (confirmationCode == M1CardAPI.Result.FIND_FAIL) {
                    tips.setText(R.string.no_card_with_data);
                } else if (confirmationCode == M1CardAPI.Result.TIME_OUT) {
                    tips.setText(R.string.no_card_without_data);
                } else if (confirmationCode == M1CardAPI.Result.OTHER_EXCEPTION) {
                    tips.setText(R.string.find_card_exception);
                }
            }
        });

        reader.setOnReadAtPositionListener(new AsyncM1Card.OnReadAtPositionListener() {

            @Override
            public void onReadAtPositionSuccess(String cardNum, byte[][] data) {
                cancleProgressDialog();
                tvCardName.setText(cardNum);
                if (data != null && data.length != 0) {
                    edReadBlock.setText(DataUtils.toHexString(data[0]));
                }
                tips.setText(R.string.reading_success);ToastUtils.showToast(R.string.reading_success);
            }

            @Override
            public void onReadAtPositionFail(int comfirmationCode) {
                cancleProgressDialog();
                tips.setText(R.string.reading_fail);ToastUtils.showToast(R.string.reading_fail);
            }
        });

        reader.setOnWriteAtPositionListener(new AsyncM1Card.OnWriteAtPositionListener() {

            @Override
            public void onWriteAtPositionSuccess(String num) {
                cancleProgressDialog();
                tvCardName.setText(num);
                tips.setText(R.string.writing_success);ToastUtils.showToast(R.string.writing_success);
            }

            @Override
            public void onWriteAtPositionFail(int comfirmationCode) {
                cancleProgressDialog();
                tips.setText(R.string.writing_fail);ToastUtils.showToast(R.string.writing_fail);
            }
        });
        reader.setOnUpdatePwdListener(new AsyncM1Card.OnUpdatePwdListener() {
            @Override
            public void onUpdatePwdSuccess(String num) {
                cancleProgressDialog();
                tvCardName.setText(num);
                tips.setText(R.string.m1_str_update_pwd_success);
                ToastUtils.showToast(R.string.m1_str_update_pwd_success);
            }

            @Override
            public void onUpdatePwdFail(int comfirmationCode) {
                cancleProgressDialog();
                ToastUtils.showToast(R.string.m1_str_update_pwd_failure);
                tips.setText(R.string.m1_str_update_pwd_failure);
            }
        });

    }


    @OnClick({R.id.btn_getCardNum, R.id.btn_sendCardPwd, R.id.btn_validatePwd, R.id.btn_write, R.id.btn_update, R.id.btn_read})
    public void onClick(View view) {
        boolean isExit = false;
        if (!SerialPortManager.getInstance().isOpen()
                && !SerialPortManager.getInstance().openSerialPort()) {
            ToastUtils.showToast(this, R.string.open_serial_fail);
            isExit = true;
        }
        if (isExit) {
            return;
        }
        int block;
        String keyA = "";
        String keyB = "";
        String data = "";
        switch (view.getId()) {
            case R.id.btn_getCardNum:

                tvCardName.setText("");
                showProgressDialog(R.string.getcard_wait);
                reader.readCardNum();
                break;
            case R.id.btn_sendCardPwd:
                edWriteBlock.setText(TimeUtils.getUUID());
                break;
            case R.id.btn_validatePwd:
                break;
            case R.id.btn_write:
                tvCardName.setText("");
                edReadBlock.setText("");
                if (TextUtils.isEmpty(edBlockNum.getText().toString())) {
                    ToastUtils.showToast(this, R.string.m1_str_block_not_empty);
                    return;
                }
                block = Integer.parseInt(edBlockNum.getText().toString());
                keyA = edPwdA.getText().toString();
                keyB = edPwdB.getText().toString();
                data = edWriteBlock.getText().toString();
                if (TextUtils.isEmpty(keyA) || TextUtils.isEmpty(keyB)
                        || TextUtils.isEmpty(data)) {
                    ToastUtils.showToast(this, R.string.m1_str_all_not_empty);
                    return;
                }
                if (RegexUtils.isCheckPwd(keyA) && RegexUtils.isCheckPwd(keyB)
                        && RegexUtils.isCheckWriteData(data)) {
                    showProgressDialog(R.string.writing_wait);
                    reader.write(block, mKeyType, NUM, keyA, keyB, data);
                } else
                    ToastUtils.showToast(this, R.string.m1_str_all_not_validate);
                break;
            case R.id.btn_read:
                tvCardName.setText("");
                edReadBlock.setText("");
                if (TextUtils.isEmpty(edBlockNum.getText().toString())) {
                    ToastUtils.showToast(this, R.string.m1_str_block_not_empty);
                    return;
                }
                block = Integer.parseInt(edBlockNum.getText().toString());
                keyA = edPwdA.getText().toString();
                keyB = edPwdB.getText().toString();
                if (TextUtils.isEmpty(keyA) || TextUtils.isEmpty(keyB)) {
                    ToastUtils.showToast(this, R.string.m1_str_not_empty);
                    return;
                }
                showProgressDialog(R.string.reading_wait);
                reader.read(block, mKeyType, NUM, keyA, keyB);
                break;
            case R.id.btn_update:
                tvCardName.setText("");
                edReadBlock.setText("");
                if (TextUtils.isEmpty(edBlockNum.getText().toString()))// 另外块号需要校验
                {
                    ToastUtils.showToast(this, R.string.m1_str_block_not_empty);
                    return;
                }
                block = Integer.parseInt(edBlockNum.getText().toString());
                keyA = edPwdA.getText().toString();
                keyB = edPwdB.getText().toString();
                data = edWritePwd.getText().toString();
                if (TextUtils.isEmpty(keyA) || TextUtils.isEmpty(keyB)
                        || TextUtils.isEmpty(data)) {
                    ToastUtils.showToast(this, R.string.m1_str_all_not_empty);
                    return;
                }
                if (RegexUtils.isCheckPwd(keyA) && RegexUtils.isCheckPwd(keyB)
                        && RegexUtils.isCheckPwd(data)) {
                    showProgressDialog(R.string.updatepwding_wait);
                    reader.updatePwd(block, mKeyType, NUM, keyA, keyB, data);
                } else
                    ToastUtils.showToast(this, R.string.m1_str_all_not_validate);
                break;
            default:
                break;
        }
    }
}
