package com.szfp.ss;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.szfp.asynctask.AsyncM1Card;
import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.inter.OnRechargeRecordListener;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.PrintUtils;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SoundUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.progress.style.CubeGrid;
import com.szfp.view.progress.style.Wave;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import static com.szfp.ss.App.logger;

public class RechargeAty extends BaseAty {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_recharge_amount)
    EditText etRechargeAmount;
    @BindView(R.id.bt_comm_print)
    SelectButton btCommPrint;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_sava)
    Button btSava;

    private boolean isReader=true;
    private boolean isPrint=false;
    private Wave mWave;
    private boolean vibrate;
    private AsyncM1Card reader;
    private String amount;
    private WebView webView;
    private ImageView imageView;
    private BaseDialog dialog=null;
    private CubeGrid mCubeGrid;
    private ImageView ivClear;
    private Flowable<String> flowable;
    private Subscriber<String> subscriber;
    private SoundUtils soundUtils;
    private String cardId;
    private static final long VIBRATE_DURATION = 200L;
    private UserInformation userInformation;

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialog!=null) dialog.cancel();
            isReader=false;
            mCubeGrid.stop();
        }
    };


    @Override
    protected void showDisconnecting() {
        btCommPrint.setText("Bluetooth Disconnect");
        isPrint =false;mWave.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_aty);
        ButterKnife.bind(this);
        toolbar.setTitle("RECHARGE");
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucent(this, 10);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        reader = new AsyncM1Card(app.getHandlerThread().getLooper());
        flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                if (!e.isCancelled()) {
                    e.onNext("READ");
                    e.onComplete();
                }
            }
        }, BackpressureStrategy.DROP);
        subscriber = new Subscriber<String>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                subscription.request(1);
            }

            @Override
            public void onNext(String s) {
                reader.readCardNum();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                subscription.cancel();
            }
        };


        reader.setOnReadCardNumListener(new AsyncM1Card.OnReadCardNumListener() {
            @Override
            public void onReadCardNumSuccess(String num) {
                playBeepSoundAndVibrate();
                cardId = num.replace("0x", "").replace(",", "").replace("\n","");
                logger.debug("READER CARD ID "+cardId );

                if (dialog!=null) dialog.cancel();
                isReader=false;
                mCubeGrid.stop();
                userInformation = DbHelper.selectCardIdForUserList(cardId);

                if(DataUtils.isEmpty(userInformation)){
                    //用户信息为空 重新刷卡
                    showNoUser();

                }else {
                    //查询成功进行充值

                    DbHelper.insertRechargeRecordBean(userInformation,amount, new OnRechargeRecordListener() {
                        @Override
                        public void success(UserInformation uInfo, RechargeRecordBean recordBean) {
                            PrintUtils.printRechargeRecord(uInfo,recordBean);
                        }

                        @Override
                        public void error(String str) {
                            ToastUtils.error(str);
                        }
                    });


                }

            }

            @Override
            public void onReadCardNumFail(int comfirmationCode) {
                logger.debug("Read the card failure");
//                reader.readCardNum();
               if (isReader)flowable.subscribe(subscriber);

            }
        });


    }

    /**
     *
     */
    private DialogSureCancel dialogSureCancel;
    private void showNoUser() {
        if (dialogSureCancel==null){
            dialogSureCancel = new DialogSureCancel(this);
            dialogSureCancel.getTvContent().setText("No user found\nplease try again");
            dialogSureCancel.setCancelable(false);
            dialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSlotCard();
                    dialogSureCancel.cancel();
                }
            });
            dialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSureCancel.cancel();
                }
            });
        }
        dialogSureCancel.show();
    }

    @Override
    protected void showConnecting() {
        btCommPrint.setText("comm......");
        mWave = new Wave();
        mWave.setBounds(0, 0, 100, 100);
        //noinspection deprecation
        mWave.setColor(getResources().getColor(R.color.baby_blue));
        btCommPrint.setCompoundDrawables(mWave, null, null, null);
        mWave.start();
    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {
        btCommPrint.setText("COMM");
    }



    @OnClick({R.id.bt_comm_print, R.id.bt_cancel, R.id.bt_sava})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_comm_print:
                showDeviceList();
                break;
            case R.id.bt_cancel:
                break;
            case R.id.bt_sava:
                if (mChatService.getState() != BluetoothService.STATE_CONNECTED){
                    ToastUtils.error("The printer is not connected");
                    return;
                }
                amount = etRechargeAmount.getText().toString();
                if (DataUtils.isNullString(amount)){
                    ToastUtils.error("Please Enter Amount");
                    return;
                }
                showSlotCard();
                break;
        }
    }

    private void showSlotCard() {
        if (dialog == null){
            View view = ContextUtils.inflate(this,R.layout.dialog_slot_card);
            webView = (WebView) view.findViewById(R.id.card_pat_wv);
            imageView = (ImageView) view.findViewById(R.id.image);
            mCubeGrid = new CubeGrid();
            mCubeGrid.setColor(Color.MAGENTA);
            imageView.setImageDrawable(mCubeGrid);
            imageView.setBackgroundColor(Color.parseColor("#00000000"));
            webView.loadDataWithBaseURL(null, "<HTML><body bgcolor='#FFF'><div align=center>" +
                    "<img width=\"140\" height=\"140\" src='file:///android_asset/gif/dyn_pat.gif'/></div></body></html>", "text/html", "UTF-8", null);
            ivClear = (ImageView) view.findViewById(R.id.iv_clear);
            ivClear.setOnClickListener(onClickListener);
            dialog = new BaseDialog(mContext,R.style.AlertDialogStyle);
            dialog.setContentView(view);
            dialog.setCancelable(false);


        }
        mCubeGrid.start();
        dialog.show();
        reader.readCardNum();
        isReader=true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        initBeepSound();
        vibrate = false;

    }
    private void initBeepSound() {
        if (soundUtils == null) {
            soundUtils = new SoundUtils(this, SoundUtils.RING_SOUND);
            soundUtils.putSound(0, R.raw.beep);
        }
    }

    private void playBeepSoundAndVibrate() {
        if (soundUtils != null) {
            soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isReader=false;
    }
}
