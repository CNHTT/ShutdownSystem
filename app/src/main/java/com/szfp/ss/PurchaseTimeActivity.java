package com.szfp.ss;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.szfp.asynctask.AsyncM1Card;
import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.PagerItem;
import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.inter.OnRechargeRecordListener;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.PrintUtils;
import com.szfp.utils.AppManager;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SPUtils;
import com.szfp.utils.SoundUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.NiceSpinner;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.progress.style.CubeGrid;
import com.szfp.view.progress.style.Wave;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import static com.szfp.ss.App.logger;

public class PurchaseTimeActivity extends BaseAty {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;
    @BindView(R.id.tv_unit_amount)
    TextView tvUnitAmount;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.bt_sub)
    SelectButton btSub;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.bt_add)
    SelectButton btAdd;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_sava)
    Button btSava;
    @BindView(R.id.bt_comm_print)
    SelectButton btCommPrint;


    private int type;
    private String unitAmount;
    private String numStr;
    private int num;
    private int totalAmount;
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
        setContentView(R.layout.activity_purchase_time);
        ButterKnife.bind(this);
        toolbar.setTitle("PURCHASE TIME");
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucent(this, 10);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();


    }

    private void initView() {
        unitAmount = SPUtils.getString(mContext, KEY.HOUR_FEE);
        tvUnitAmount.setText(DataUtils.getAmountValue(unitAmount));
        List<String> dataset = new LinkedList<>(Arrays.asList(PagerItem.HOUR, PagerItem.DAY, PagerItem.MONTH));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               type = position;
                switch (position) {
                    case 0:
                        unitAmount = SPUtils.getString(mContext, KEY.HOUR_FEE);
                        tvUnitAmount.setText(DataUtils.getAmountValue(unitAmount));
                        totalAmount = (int) (num * Double.parseDouble(unitAmount));
                        tvTotalAmount.setText(DataUtils.getAmountValue(totalAmount));
                        break;
                    case 1:
                        unitAmount = SPUtils.getString(mContext, KEY.DAY_FEE);
                        tvUnitAmount.setText(DataUtils.getAmountValue(unitAmount));
                        totalAmount = (int) (num * Double.parseDouble(unitAmount));
                        tvTotalAmount.setText(DataUtils.getAmountValue(totalAmount));
                        break;
                    case 2:
                        unitAmount = SPUtils.getString(mContext, KEY.MONTH_FEE);
                        tvUnitAmount.setText(DataUtils.getAmountValue(unitAmount));
                        totalAmount = (int) (num * Double.parseDouble(unitAmount));
                        tvTotalAmount.setText(DataUtils.getAmountValue(totalAmount));
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
            public void onReadCardNumSuccess(String cardNum) {
                playBeepSoundAndVibrate();
                cardId = cardNum.replace("0x", "").replace(",", "").replace("\n","");
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

                    if (userInformation.getBalance()<totalAmount){
                        if (dialogSureCancel==null){
                            dialogSureCancel = new DialogSureCancel(mContext);
                            dialogSureCancel.getTvContent().setText("Insufficient balance\n" +
                                    "Whether to continue");
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
                        }else dialogSureCancel.getTvContent().setText("Insufficient balance\n" +
                                "Whether to continue");
                        dialogSureCancel.show();

                    }else

                    DbHelper.insertPurchaseTime(userInformation,num,totalAmount,type, new OnRechargeRecordListener() {
                        @Override
                        public void success(final UserInformation uInfo, final RechargeRecordBean recordBean) {

                                dialogSureCancel = new DialogSureCancel(mContext);
                                dialogSureCancel.getTvContent().setText(uInfo.getPurchtimeStr());
                                dialogSureCancel.setCancelable(false);
                                dialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PrintUtils.printRechargeRecord(uInfo,recordBean);
                                        dialogSureCancel.cancel();
                                    }
                                });
                                dialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogSureCancel.cancel();
                                    }
                                });

                            dialogSureCancel.show();
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

    @OnClick({R.id.bt_sub, R.id.bt_add, R.id.bt_cancel, R.id.bt_sava, R.id.bt_comm_print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sub:
                numStr = tvNum.getText().toString();
                num = Integer.parseInt(numStr);
                if (num == 0) return;
                num--;
                tvNum.setText(num + "");
                totalAmount = (int) (num * Double.parseDouble(unitAmount));
                tvTotalAmount.setText(DataUtils.getAmountValue(totalAmount));
                break;
            case R.id.bt_add:
                numStr = tvNum.getText().toString();
                num = Integer.parseInt(numStr);
                num++;
                tvNum.setText(num + "");
                totalAmount = (int) (num * Double.parseDouble(unitAmount));
                tvTotalAmount.setText(DataUtils.getAmountValue(totalAmount));
                break;
            case R.id.bt_cancel:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.bt_sava:
                if (totalAmount == 0) {
                    ToastUtils.error("please input");
                    return;
                }

                showSlotCard();
                break;
            case R.id.bt_comm_print:
                showDeviceList();
                break;
        }
    }
}
