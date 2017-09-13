package com.szfp.ss;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.szfp.asynctask.AsyncM1Card;
import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.utils.DbHelper;
import com.szfp.utils.AppManager;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SoundUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.view.dialog.DialogSure;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.progress.style.CubeGrid;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Date;

import android_serialport_api.M1CardAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;

import static com.szfp.ss.App.logger;

public class LuRuKaActivity extends BaseAty {

    @BindView(R.id.card_pat_wv)
    WebView cardPatWv;


    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_card_Id)
    TextView tvCardId;
    @BindView(R.id.tv_first_name)
    TextView tvFirstName;
    @BindView(R.id.tv_last_name)
    TextView tvLastName;
    @BindView(R.id.tv_lpn)
    TextView tvLpn;
    @BindView(R.id.tv_tn)
    TextView tvTn;
    @BindView(R.id.bt_cancel)
    Button btCancel;
    @BindView(R.id.bt_sava)
    Button btSava;
    @BindView(R.id.sl_result)
    ScrollView slResult;
    @BindView(R.id.rl_pat_card)
    RelativeLayout rlPatCard;
    private static int NUM = 1;
    private static int mKeyType = M1CardAPI.KEY_A;
    @BindView(R.id.textView)
    TextView textView;
    private String cardId;

    private boolean vibrate;
    private AsyncM1Card reader;
    private Flowable<String> flowable;
    private Subscriber<String> subscriber;
    private Disposable disposable;
    private CubeGrid mChasingDotsDrawable;
    private SoundUtils soundUtils;
    private UserInformation userInformation;
    private DialogSureCancel dialogSureCancel=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lu_ru_ka);
        ButterKnife.bind(this);
        toolbar.setTitle("PAT CARD PLS");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StatusBarUtil.setTranslucent(this, 10);
        userInformation = (UserInformation) getIntent().getExtras().getSerializable(KEY.INFO);
        initView();
    }

    private void initView() {

        mChasingDotsDrawable = new CubeGrid();
        mChasingDotsDrawable.setColor(Color.MAGENTA);
        image.setImageDrawable(mChasingDotsDrawable);
        image.setBackgroundColor(Color.parseColor("#00000000"));

        cardPatWv.loadDataWithBaseURL(null, "<HTML><body bgcolor='#FFF'><div align=center>" +
                "<img width=\"207\" height=\"207\" src='file:///android_asset/gif/dyn_pat.gif'/></div></body></html>", "text/html", "UTF-8", null);

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
                cardId = num.replace("0x", "").replace(",", "");
                if (DataUtils.isEmpty(DbHelper.selectCardIdForUserList(cardId)))
                showView();
                else {
                    final DialogSure dialogSure = new DialogSure(mContext);
                    dialogSure.getTvContent().setText("The card has been used");
                    dialogSure.setCancelable(false);
                    dialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reader.readCardNum();
                            dialogSure.cancel();
                        }
                    });
                    dialogSure.show();
                }
            }

            @Override
            public void onReadCardNumFail(int comfirmationCode) {
                logger.debug("Read the card failure");
//                reader.readCardNum();
                flowable.subscribe(subscriber);

            }
        });

        reader.readCardNum();
    }

    private void showView() {
        rlPatCard.setVisibility(View.GONE);
        slResult.setVisibility(View.VISIBLE);

        tvCardId.setText(cardId);
        tvFirstName.setText(userInformation.getFirstName());
        tvLastName.setText(userInformation.getLastName());
        tvLpn.setText(userInformation.getLicensePlateNumber());
        tvTn.setText(userInformation.getTelephoneNumber());
        userInformation.setCardId(cardId);
        userInformation.setCreateTime(new Date().getTime());
        userInformation.setUUID(TimeUtils.getUUID());
    }


    private static final long VIBRATE_DURATION = 200L;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        initBeepSound();
        mChasingDotsDrawable.start();
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
    protected void onStop() {
        super.onStop();
        mChasingDotsDrawable.stop();
    }

    @OnClick({R.id.bt_cancel, R.id.bt_sava})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                AppManager.getAppManager().finishAllActivityAndExit(this);
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.bt_sava:

                if (DbHelper.insertUserInfo(userInformation)){

                    if (dialogSureCancel == null)
                        dialogSureCancel = new DialogSureCancel(mContext);

                    dialogSureCancel.setCancelable(false);
                    dialogSureCancel.getTvContent().setText("Whether to register \rsuccessfully to be recharged");
                    dialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppManager.getAppManager().finishActivity(LuRuKaActivity.class);
                            AppManager.getAppManager().finishActivity(AddUserAty.class);
                            Intent intent = new Intent();
                            intent.setClass(LuRuKaActivity.this,RechargedActivity.class);
                            startActivity(intent);

                        }
                    });

                    dialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppManager.getAppManager().finishActivity(LuRuKaActivity.class);
                            AppManager.getAppManager().finishActivity(AddUserAty.class);
                        }
                    });
                    dialogSureCancel.show();
                }else {
                    if (dialogSureCancel == null)
                        dialogSureCancel = new DialogSureCancel(mContext);


                    dialogSureCancel.setCancelable(false);
                    dialogSureCancel.getTvContent().setText("Please try again \nif you fail to register");
                    dialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });

                    dialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppManager.getAppManager().finishActivity(LuRuKaActivity.class);
                            AppManager.getAppManager().finishActivity(AddUserAty.class);
                        }
                    });
                    dialogSureCancel.show();
                }
                break;
        }
    }
}
