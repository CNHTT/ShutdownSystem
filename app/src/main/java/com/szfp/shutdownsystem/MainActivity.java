package com.szfp.shutdownsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.szfp.asynctask.AsyncM1Card;
import com.szfp.utils.ToastUtils;

import android_serialport_api.M1CardAPI;
import android_serialport_api.SerialPortManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAty {

    @BindView(R.id.showText)
    TextView showText;
    @BindView(R.id.button)
    Button button;


    private static  final String[]  cardType ={"S50","S70"};
    private static  final String[]  pwdType ={"KEYA","KEYB"};
    private static  final int[]     keyType = {M1CardAPI.KEY_A,M1CardAPI.KEY_B};
    private static  int             NUM =1;
    private static  int             mKeyType =M1CardAPI.KEY_A;
    private AsyncM1Card reader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        
        initData();
    }

    private void initData() {
        reader = new AsyncM1Card(app.getHandlerThread().getLooper());
        //读取卡号
        reader.setOnReadCardNumListener(new AsyncM1Card.OnReadCardNumListener() {
            @Override
            public void onReadCardNumSuccess(String num) {
                showText.setText(num);
                cancleProgressDialog();
            }

            @Override
            public void onReadCardNumFail(int confirmationCode) {
                showText.setText("");
                cancleProgressDialog();
                if (confirmationCode == M1CardAPI.Result.FIND_FAIL) {
                    ToastUtils.error(getString(R.string.no_card_with_data));
                } else if (confirmationCode == M1CardAPI.Result.TIME_OUT) {
                    ToastUtils.error(getString(R.string.no_card_without_data));
                } else if (confirmationCode == M1CardAPI.Result.OTHER_EXCEPTION) {
                    ToastUtils.error(getString(R.string.find_card_exception));
                }
            }
        });
    }



    @OnClick(R.id.button)
    public void onClick() {
        boolean isExit = false;
        if (!SerialPortManager.getInstance().isOpen()
                && !SerialPortManager.getInstance().openSerialPort()) {
            ToastUtils.error(getResources().getString(R.string.open_serial_fail));
            isExit = true;
        }
        if (isExit) {
            return;
        }
        showText.setText("");
        showProgressDialog(R.string.getcard_wait);
        reader.readCardNum();
    }
}
