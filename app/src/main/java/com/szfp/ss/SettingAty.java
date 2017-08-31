package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.szfp.utils.ContextUtils;
import com.szfp.view.BaseDialog;
import com.szfp.view.SSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingAty extends BaseAty implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bt_parameterDownload)
    TextView btParameterDownload;
    @BindView(R.id.sw_data_sync)
    SwitchCompat swDataSync;
    @BindView(R.id.sw_manual_upload)
    SwitchCompat swManualUpload;
    @BindView(R.id.ll_data_sync)
    LinearLayout llDataSync;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_cache_data_size)
    TextView tvCacheDataSize;
    private TextView tvTitle;
    private SSeekBar sbDataSize;
    private TextView tvSure;
    private TextView tvCancel;


    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;
    private View.OnClickListener OnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_sure:
                    if (dialog!=null)  dialog.cancel();
                    break;
                case R.id.tv_cancel:
                    if (dialog!=null)  dialog.cancel();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_aty);
        ButterKnife.bind(this);


        toolbar.setTitle("Setting");
        setSupportActionBar(toolbar);

        initEvent();

    }

    private void initEvent() {
        swDataSync.setOnCheckedChangeListener(this);
        swManualUpload.setOnCheckedChangeListener(this);
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.sw_data_sync:

                if (isChecked) {
                    if (llDataSync.getVisibility() == View.VISIBLE) {
//                        llDataSync.startAnimation(mHiddenAction);
                        llDataSync.setVisibility(View.GONE);
                    }


                } else {
                    if (llDataSync.getVisibility() == View.GONE) {
//                        llDataSync.startAnimation(mShowAction);
                        llDataSync.setVisibility(View.VISIBLE);
                    }
                }


                break;

            case R.id.sw_manual_upload:

                break;
        }


    }

    @OnClick({R.id.bt_parameterDownload, R.id.ll_cache_data_size,R.id.tv_sure, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_parameterDownload:
                break;
            case R.id.ll_cache_data_size:
                showDataSizeDialog();
                break;
            case R.id.tv_sure:
                if (dialog!=null)  dialog.cancel();
                break;
            case R.id.tv_cancel:
                if (dialog!=null)  dialog.cancel();
                break;
        }
    }



    private BaseDialog dialog;
    private void showDataSizeDialog() {
        View view = ContextUtils.inflate(this, R.layout.dialog_data_size);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        sbDataSize = (SSeekBar) view.findViewById(R.id.sb_data_size);
        tvSure     = (TextView) view.findViewById(R.id.tv_sure);
        tvCancel   = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(OnClickListener);
        tvSure.setOnClickListener(OnClickListener);

        dialog = new BaseDialog(mContext, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.show();
        sbDataSize.setOnRangeChangedListener(new SSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(SSeekBar view, float min, float max, boolean isFromUser) {
                if (isFromUser){
                    view.setProgressDescription((int)min+" ");
                }
            }
        });

    }

}
