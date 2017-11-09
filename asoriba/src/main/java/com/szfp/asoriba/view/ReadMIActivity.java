package com.szfp.asoriba.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szfp.asoriba.R;
import com.szfp.asoriba.adapter.NFCMemberAdapter;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.utils.DbHelper;
import com.szfp.asynctask.AsyncM1Card;
import com.szfp.utils.AppManager;
import com.szfp.utils.DataUtils;
import com.szfp.utils.SoundUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.progress.style.CubeGrid;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.M1CardAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * author：ct on 2017/10/20 16:52
 * email：cnhttt@163.com
 */

public class ReadMIActivity extends BaseAty {

    @BindView(R.id.card_pat_wv)
    WebView cardPatWv;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.rl_pat_card)
    RelativeLayout rlPatCard;
    @BindView(R.id.bt_ve_read)
    SelectButton btVeRead;
    @BindView(R.id.bt_clear)
    SelectButton btClear;
    @BindView(R.id.ll_list)
    ListView llList;

    private boolean vibrate;
    private String cardId;
    private AsyncM1Card reader;
    private Flowable<String> flowable;
    private Subscriber<String> subscriber;
    private Disposable disposable;
    private CubeGrid mChasingDotsDrawable;
    private SoundUtils soundUtils;
    private DialogSureCancel dialogSureCancel = null;
    private static final long VIBRATE_DURATION = 200L;

    private static NFCMemberAdapter adapter;
    private MemberBean memberBean;
    private List<MemberBean> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);
        setContentView(R.layout.activity_read_nfc);
        ButterKnife.bind(this);
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


        reader.setOnReadAtPositionListener(new AsyncM1Card.OnReadAtPositionListener() {

            @Override
            public void onReadAtPositionSuccess(String cardNum, byte[][] data) {

                playBeepSoundAndVibrate();

                if (data != null && data.length != 0) {
                    String UUID=DataUtils.toHexString(data[0]);
                    memberBean = DbHelper.getMemberUUID(UUID);
                    if (memberBean!=null)
                    if (!list.contains(memberBean)){
                            list.add(memberBean);
                            adapter =  new NFCMemberAdapter(ReadMIActivity.this,list);
                            llList.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onReadAtPositionFail(int comfirmationCode) {
                cancleProgressDialog();
               ToastUtils.showToast(R.string.reading_fail);
            }
        });
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

    @OnClick({R.id.bt_ve_read, R.id.bt_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ve_read:
                reader.read(2, M1CardAPI.KEY_A, 1, "ffffffffffff", "ffffffffffff");
                break;
            case R.id.bt_clear:

               if(adapter!=null) adapter.cleanItems();
                list.clear();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

