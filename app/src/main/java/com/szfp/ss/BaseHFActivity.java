package com.szfp.ss;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import com.szfp.asynctask.AsyncM1Card;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.domain.model.MemberBean;
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
 * Created by 戴尔 on 2017/11/10.
 */

public abstract class BaseHFActivity extends BaseAty{
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
    protected MemberBean memberBean;
    protected boolean isRepeat=true;
    protected ProgressDialog loadDialog;


    protected boolean isWrite =false;

    protected String uuid;

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
                if (isWrite) reader.write(App.block,App.KeyType,App.num,App.DefaultKeyA,App.DefaultKeyB,uuid);
                else reader.read(App.block,App.KeyType,App.num,App.DefaultKeyA,App.DefaultKeyB);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                subscription.cancel();
            }
        };


        reader.setOnReadAtPositionListener(new AsyncM1Card.OnReadAtPositionListener() {
            @Override
            public void onReadAtPositionSuccess(String cardNum, byte[][] data) {
                if (dialog!=null) dialog.cancel();
                playBeepSoundAndVibrate();
                showReadCard(cardNum.replace("0x", "").replace(",", "").replace("\n","")
                , DataUtils.toHexString(data[0]));
            }

            @Override
            public void onReadAtPositionFail(int comfirmationCode) {
                isWrite =false;
                if (isRepeat){
                    if (isReader)flowable.subscribe(subscriber);
                }else {
                    dialog.cancel();
                }
            }
        });

        reader.setOnWriteAtPositionListener(new AsyncM1Card.OnWriteAtPositionListener() {

            @Override
            public void onWriteAtPositionSuccess(String num) {
                if (dialog!=null) dialog.cancel();
                playBeepSoundAndVibrate();
                showWriteSuccess(num.replace("0x", "").replace(",", "").replace("\n",""));
            }

            @Override
            public void onWriteAtPositionFail(int comfirmationCode) {
                logger.debug("Write the card failure");
                isWrite =true;
                if (isRepeat){
                    if (isReader)flowable.subscribe(subscriber);
                }else {
                    dialog.cancel();
                }
            }
        });
    }

    protected abstract void showWriteSuccess(String num);

    protected abstract void showReadCard(String cardId, String uuid);




    private void initDialog() {
        loadDialog = new ProgressDialog(this);
        loadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadDialog.setCanceledOnTouchOutside(false);
        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadDialog.setMessage("Connecting device...");
    }

    protected void showLoadDialog(){
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }
    protected void showLoadDismiss(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public void showSlotCard(boolean is) {
        isWrite =is;
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
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (KeyEvent.KEYCODE_BACK == keyCode) {
                        isReader=false;
                    }
                    return false;
                }
            });


        }
        mCubeGrid.start();
        dialog.show();
        if (is) reader.write(App.block,App.KeyType,App.num,App.DefaultKeyA,App.DefaultKeyB,uuid);
        else reader.read(App.block,App.KeyType,App.num,App.DefaultKeyA,App.DefaultKeyB);
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

    private DialogSureCancel dialogSureCancel;
    protected void showNoUser() {
        if (dialogSureCancel==null){
            dialogSureCancel = new DialogSureCancel(this);
            dialogSureCancel.getTvContent().setText("No user found\nplease try again");
            dialogSureCancel.setCancelable(false);
            dialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSlotCard(false);
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
