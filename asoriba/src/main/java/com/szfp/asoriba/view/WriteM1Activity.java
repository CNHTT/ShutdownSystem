package com.szfp.asoriba.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szfp.asoriba.R;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asynctask.AsyncM1Card;
import com.szfp.utils.AppManager;
import com.szfp.utils.SoundUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.progress.style.CubeGrid;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import android_serialport_api.M1CardAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;

import static com.szfp.asoriba.App.logger;

public class WriteM1Activity extends BaseAty {

    @BindView(R.id.card_pat_wv)
    WebView cardPatWv;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.rl_pat_card)
    RelativeLayout rlPatCard;

    private boolean vibrate;
    private String cardId;
    private AsyncM1Card reader;
    private Flowable<String> flowable;
    private Subscriber<String> subscriber;
    private Disposable disposable;
    private CubeGrid mChasingDotsDrawable;
    private SoundUtils soundUtils;
    private MemberBean memberBean;
    private DialogSureCancel dialogSureCancel=null;
    private static final long VIBRATE_DURATION = 200L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_write_nfc);
        ButterKnife.bind(this);
        memberBean = (MemberBean) getIntent().getExtras().getSerializable("MEMBER");
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
                reader.write(2, M1CardAPI.KEY_A,1,"ffffffffffff","ffffffffffff",memberBean.getUUID());
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                subscription.cancel();
            }
        };

        reader.setOnWriteAtPositionListener(new AsyncM1Card.OnWriteAtPositionListener() {

            @Override
            public void onWriteAtPositionSuccess(String num) {
                cancleProgressDialog();
                cardId = num.replace("0x", "").replace(",", "").replace("\n","");
                //预先判断用户是否使用
                showView();
            }

            @Override
            public void onWriteAtPositionFail(int comfirmationCode) {
                cancleProgressDialog();
                logger.debug("Read the card failure");
                flowable.subscribe(subscriber);
            }
        });

        reader.write(2, M1CardAPI.KEY_A,1,"ffffffffffff","ffffffffffff",memberBean.getUUID());
    }

    private void showView() {
        playBeepSoundAndVibrate();
        ToastUtils.showToast("success");
        AppManager.getAppManager().finishAllActivityAndExit(this);
        startActivity(new Intent(this,MainActivity.class));

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
    protected void onResume() {
        super.onResume();
        initBeepSound();
        mChasingDotsDrawable.start();
        vibrate = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mChasingDotsDrawable.stop();
    }

}
