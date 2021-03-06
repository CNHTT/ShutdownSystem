package com.szfp.ss;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.szfp.asynctask.AsyncM1Card;
import com.szfp.ss.adapter.MainPagerAdapter;
import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.MainImginfo;
import com.szfp.ss.utils.CacheData;
import com.szfp.utils.AppManager;
import com.szfp.utils.SPUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;

import java.util.ArrayList;

import android_serialport_api.M1CardAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.ss.domain.PagerItem.ENTER;
import static com.szfp.ss.domain.PagerItem.ENTRY_INFO;
import static com.szfp.ss.domain.PagerItem.OUT_OF;
import static com.szfp.ss.domain.PagerItem.P0;
import static com.szfp.ss.domain.PagerItem.QL;
import static com.szfp.ss.domain.PagerItem.SS;
import static com.szfp.ss.domain.PagerItem.TEMPORARY;

public class MainActivity extends BaseNoAty implements AdapterView.OnItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.b1)
    Button b1;
    @BindView(R.id.b2)
    Button b2;
    @BindView(R.id.b3)
    Button b3;
    @BindView(R.id.b4)
    Button b4;
    @BindView(R.id.b5)
    Button b5;
    @BindView(R.id.b6)
    Button b6;
    @BindView(R.id.b7)
    Button b7;
    @BindView(R.id.b8)
    Button b8;
    private AsyncM1Card reader;


    NfcAdapter nfcAdapter;

    private String[] arrays;
    private int[] ids;
    private ArrayList<MainImginfo> mData;

    private static final String[] cardType = {"S50", "S70"};
    private static final String[] pwdType = {"KEYA", "KEYB"};
    private static final int[] keyType = {M1CardAPI.KEY_A, M1CardAPI.KEY_B};
    private static int NUM = 1;
    private static int mKeyType = M1CardAPI.KEY_A;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucent(this);


        initData();
    }

    private void initData() {
        arrays = getResources().getStringArray(R.array.main);
        ids = new int[]{R.layout.winphone_page, R.id.wp_main_page_content,
                R.layout.winphone_item, R.id.wp_item_iv, R.id.wp_item_rl, R.id.wp_item_tv};
        int[] imgs = {
                R.mipmap.circle_credit_card, R.mipmap.circle_credit_card, R.mipmap.circle_credit_card,
                R.mipmap.circle_credit_card, R.mipmap.circle_credit_card, R.mipmap.circle_credit_card,
                R.mipmap.circle_credit_card};

        mData = new ArrayList<>();

        for (int i = 0; i < imgs.length; i++) {
            mData.add(new MainImginfo(i, arrays[i], imgs[i]));
        }

        viewPager.setAdapter(new MainPagerAdapter(this, mData, ids, this));


        reader = new AsyncM1Card(app.getHandlerThread().getLooper());
        //读取卡号
        reader.setOnReadCardNumListener(new AsyncM1Card.OnReadCardNumListener() {
            @Override
            public void onReadCardNumSuccess(String num) {
                ToastUtils.showToast(num);
                cancleProgressDialog();
            }

            @Override
            public void onReadCardNumFail(int confirmationCode) {
                ToastUtils.showToast("");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String temp = ((TextView) view.findViewById(R.id.wp_item_tv)).getText().toString();

        startTrans(temp);
    }

    private void startTrans(String temp) {

        Bundle bundle = new Bundle();
        bundle.putString(KEY.START_KEY, temp);
        Intent i = new Intent();
        switch (temp) {
            case ENTER:     //车辆驶入
                i.setClass(this, VehicleEntryActivity.class);
                break;
            case OUT_OF:    //车辆离开
                i.setClass(this, ExitVehicleActivity.class);
                break;
            case TEMPORARY: //临时卡
                i.setClass(this, RechargedActivity.class);
                break;
            case ENTRY_INFO://发卡
                i.setClass(this, AddUserAty.class);
                break;
            case P0:        //权限、管理
                i.setClass(this, PrintActivity.class);
                break;
            case QL:        //查询
                i.setClass(this, SearchInformationActivity.class);
                break;
            case SS:        //系统设置
                i.setClass(this, SettingAty.class);
                break;
            default://Other
                return;
        }
        i.putExtras(bundle);
        startActivity(i);

    }

    @OnClick({R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY.START_KEY, "SS");
        Intent i = new Intent();
        switch (view.getId()) {

            case R.id.b1: i.setClass(this, VehicleEntryActivity.class);
                break;
            case R.id.b2: i.setClass(this, ExitVehicleActivity.class);
                break;
            case R.id.b3: i.setClass(this, RechargeAty.class);
                break;
            case R.id.b4: i.setClass(this, AddUserAty.class);
                break;
            case R.id.b5: i.setClass(this, PrintActivity.class);
                break;
            case R.id.b6: i.setClass(this, SearchInformationActivity.class);
                break;
            case R.id.b7: i.setClass(this, SettingAty.class);
                break;
            case R.id.b8: i.setClass(this, LoginAty.class);
                SPUtils.putBoolean(MainActivity.this,CacheData.IsLogin,false);
                AppManager.getAppManager().finishActivity(MainActivity.this);
                break;
        }

        i.putExtras(bundle);
        startActivity(i);
    }
}
