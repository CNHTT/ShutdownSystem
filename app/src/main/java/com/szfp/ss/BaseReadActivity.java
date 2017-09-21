package com.szfp.ss;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.szfp.asynctask.AsyncM1Card;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.utils.DbHelper;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SoundUtils;
import com.szfp.view.dialog.BaseDialog;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.progress.style.CubeGrid;
import com.szfp.view.progress.style.Wave;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import static com.szfp.ss.App.logger;

/**
 * author：ct on 2017/9/19 18:50
 * email：cnhttt@163.com
 */

public abstract class BaseReadActivity extends BaseAty {
    protected boolean isReader=true;
    protected Wave mWave;
    protected boolean vibrate;
    protected AsyncM1Card reader;
    protected String amount;
    protected WebView webView;
    protected ImageView imageView;
    protected BaseDialog dialog=null;
    protected CubeGrid mCubeGrid;
    protected ImageView ivClear;
    protected Flowable<String> flowable;
    protected Subscriber<String> subscriber;
    protected SoundUtils soundUtils;
    protected String cardId;
    protected static final long VIBRATE_DURATION = 200L;
    protected UserInformation userInformation;
    protected boolean isRepeat=true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
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
                    showUser(userInformation);
                }

            }

            @Override
            public void onReadCardNumFail(int comfirmationCode) {
                logger.debug("Read the card failure");
//                reader.readCardNum();
                if (isRepeat){
                    if (isReader)flowable.subscribe(subscriber);
                }else {
                    dialog.cancel();
                }


            }
        });

    }

    protected abstract void showUser(UserInformation userInformation);

    /**
     *
     */
    private DialogSureCancel dialogSureCancel;
    protected void showNoUser() {
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
    public void showSlotCard() {
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
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialog!=null) dialog.cancel();
            isReader=false;
            mCubeGrid.stop();
        }
    };

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  android.R.id.home:
                onBackPressed();
                // 处理返回逻辑
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
