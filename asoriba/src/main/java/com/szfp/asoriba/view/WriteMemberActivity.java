package com.szfp.asoriba.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.common.primitives.Bytes;
import com.hiklife.rfidapi.RadioCtrl;
import com.szfp.asoriba.R;
import com.szfp.asoriba.adapter.TagListAdapter;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.utils.DbHelper;
import com.szfp.utils.DataUtils;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.DialogSureCancel;

import java.lang.ref.WeakReference;

import android_serialport_api.UHFHXAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteMemberActivity extends BaseUHFActivity {


    @BindView(R.id.sw_open)
    ToggleButton swOpen;
    @BindView(R.id.sw_inv)
    ToggleButton swInv;
    @BindView(R.id.bt_singleScan)
    SelectButton btSingleScan;
    @BindView(R.id.bt_setting)
    SelectButton btSetting;
    @BindView(R.id.txtCount)
    TextView txtCount;
    @BindView(R.id.txtTimes)
    TextView txtTimes;
    @BindView(R.id.bt_setting_params)
    SelectButton btSettingParams;
    @BindView(R.id.lv_tagList)
    ListView lvTagList;
    public RadioCtrl myRadio;

    private UHFHXAPI api;

    private MemberBean memberBean;

    @OnClick({R.id.bt_singleScan, R.id.bt_setting, R.id.bt_setting_params})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_singleScan:

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        api.startAutoRead2C(times, code, pwd, sa, dl,
                                new UHFHXAPI.SearchAndRead() {

                                    @Override
                                    public void timeout() {
                                        mhandler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                ToastUtils
                                                        .showToast(
                                                                "超时");
                                            }
                                        });
                                    }

                                    @Override
                                    public void returnData(final byte[] data) {
                                        mhandler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                ToastUtils
                                                        .showToast(
                                                                "data:"
                                                                        + DataUtils
                                                                        .toHexString(data));
                                                ShowEPC(DataUtils
                                                        .toHexString(data));
                                            }
                                        });
                                    }

                                    @Override
                                    public void readFail() {
                                        mhandler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                ToastUtils
                                                        .showToast(
                                                                "读取失败");
                                            }
                                        });
                                    }
                                });
                    }
                }).start();
                break;
            case R.id.bt_setting:
                break;
            case R.id.bt_setting_params:
                break;
        }
    }

    class StartHander extends Handler {
        WeakReference<Activity> mActivityRef;

        StartHander(Activity activity) {
            mActivityRef = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = mActivityRef.get();
            if (activity == null) {
                return;
            }

            switch (msg.what) {
                case MSG_SHOW_EPC_INFO:
                    ShowEPC((String) msg.obj);
                    break;

                case MSG_DISMISS_CONNECT_WAIT_SHOW:
                    cancleProgressDialog();
                    if ((Boolean) msg.obj) {
                        ToastUtils.showToast(R.string.info_connect_success);
                        btSettingParams.setEnabled(true);
                        swInv.setClickable(true);
                    } else {
                        ToastUtils.showToast(R.string.info_connect_fail);
                    }
                    break;
                case INVENTORY_OVER:
                    ToastUtils.showToast(R.string.inventory_over);
                    break;
            }
        }
    }

    private Handler hMsg = new StartHander(this);

    private TagListAdapter tagListAdapter;

    private Handler mhandler;
    private int times = 5000;// 默认超时5秒
    private int code = 1;// 默认读取epc区域
    private int sa = 0;// 默认偏移从0开始
    private int dl = 8;// 默认数据长度8
    private String pwd = "00000000";// 默认访问密码00000000
    private boolean isOnPause;
    private boolean isStop;
    private Runnable task = new Runnable() {

        @Override
        public void run() {
            api.startAutoRead2A(0x22, new byte[]{0x00, 0x01},
                    new UHFHXAPI.AutoRead() {

                        @Override
                        public void timeout() {
                            Log.i("zzd", "timeout");
                        }

                        @Override
                        public void start() {
                            Log.i("zzd", "start");
                        }

                        @Override
                        public void processing(byte[] data) {
                            String epc = DataUtils.toHexString(data).substring(
                                    4);
                            hMsg.obtainMessage(MSG_SHOW_EPC_INFO, epc)
                                    .sendToTarget();
                            Log.i("zzd", "data=" + epc);
                        }

                        @Override
                        public void end() {

                            Log.i("whw", "end");
                            Log.i("whw", "isStop=" + isStop);
                            if (!isStop) {
                                pool.execute(task);
                            } else {
                                hMsg.sendEmptyMessage(INVENTORY_OVER);
                            }
                        }
                    });

        }
    };

    private void ShowEPC(String flagID) {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        } else {
            mediaPlayer.start();
        }
        if (!tagInfoList.contains(flagID)) {
            number.put(flagID, 1);
            tagCount++;
            tagInfoList.add(flagID);

            try {
                txtCount.setText(String.format("%d", tagCount));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            int num = number.get(flagID);
            number.put(flagID, ++num);
            Log.i("whw", "flagID=" + flagID + "   num=" + num);
        }
        if (tagListAdapter == null){
            tagListAdapter = new TagListAdapter(WriteMemberActivity.this,tagInfoList,number);
            tagListAdapter.setCallback(new TagListAdapter.OnTagEPC() {
                @Override
                public void onStrEpc(String string) {
                    write(string);
                }
            });
            lvTagList.setAdapter(tagListAdapter);

        }else {
            tagListAdapter.setNumber(number);
            tagListAdapter.updateItems(tagInfoList);
        }


        tagTimes++;
        try {
            txtTimes.setText(String.format("%d", tagTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把用户信息写入卡中
     * @param string
     */
    private void write(final String string) {
        MemberBean  member= DbHelper.getMember(string);
        if (member ==null){
            //未使用
            WriteTag(string);

        }else {
            //已使用
            if (scDialog==null){
                scDialog = new DialogSureCancel(this);
                scDialog.getTvContent().setText("The card has been "+member.getFirstName()+"."+member.getLastName()+" use, \n whether to replace");
                scDialog.setCancelable(false);
                scDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WriteTag(string);
                        scDialog.cancel();
                    }
                });
                scDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scDialog.cancel();
                    }
                });
            }
            scDialog.show();
        }


    }

    private void WriteTag(String string) {
        String ap = "00000000";
        short epcLength = (short) (string.length() / 2);
        String epc = string;
        byte mb = (byte) 1;
        switch (mb) {
            case 0:
                mb++;
                break;
            case 1:
                mb += 2;
                break;
            default:
                break;
        }
        short sa = Short.parseShort("0");
        short dl = Short.parseShort("8");
        String writeData =memberBean.getUUID() ;
        if (!TextUtils.isEmpty(writeData) && writeData.length() / 4 == dl) {
            byte[] arguments = Bytes.concat(DataUtils.hexStringTobyte(ap),
                    DataUtils.short2byte(epcLength),
                    DataUtils.hexStringTobyte(epc), new byte[] { mb },
                    DataUtils.short2byte(sa), DataUtils.short2byte(dl),
                    DataUtils.hexStringTobyte(writeData));
            String data = writeTag(arguments);
            if (!TextUtils.isEmpty(writeData) && data.equals("00")) {
                ToastUtils.showToast( "Write success！");
                memberBean.setUHF_ID(string);
                memberBean.setIsBind(true);
                DbHelper.upMember(memberBean);
                finish(); startActivity(new Intent(WriteMemberActivity.this,MainActivity.class));
            } else {
                ToastUtils.showToast( "Write failed！");
            }
        }
    }
    public String writeTag(byte[] args) {
        UHFHXAPI.Response response = api
                .writeTypeCTagData(args);
        if (response.result == UHFHXAPI.Response.RESPONSE_PACKET
                && response.data != null) {
            return DataUtils.toHexString(response.data);
        }
        return "";
    }
    /**
     *
     */
    private DialogSureCancel scDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_member);
        ButterKnife.bind(this);
        mhandler = new Handler();
        api = new UHFHXAPI();
        Log.i("UUUUU", TimeUtils.getUUID());
        memberBean  = (MemberBean) getIntent().getExtras().getSerializable("MEMBER");
        /**
         * 打开模块
         */
        swOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                swOpen.setClickable(false);
                if (isChecked) {

                    showProgressDialog("Connecting device, please ...");
                    swInv.setClickable(true);
                    new Thread() {
                        @Override
                        public void run() {
                            Message closemsg = new Message();
                            closemsg.obj = api.open();
                            closemsg.what = MSG_DISMISS_CONNECT_WAIT_SHOW;
                            hMsg.sendMessage(closemsg);
                        }
                    }.start();
                } else {
                    if (!isOnPause) {
                        api.close();
                        btSettingParams.setEnabled(false);
                    }
                    swInv.setClickable(false);
                }

                swOpen.setClickable(true);
            }
        });

        /**
         * 开启连扫
         */
        swInv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                swInv.setClickable(false);
                if (isChecked) {
                    isStop = false;
                    Inv();
                    btSettingParams.setEnabled(false);
                } else {
                    isStop = true;
                    btSettingParams.setEnabled(true);
                }

                swInv.setClickable(true);
            }
        });


    }




    /**
     * 开始扫描
     */
    private void Inv() {
        pool.execute(task);
        tagInfoList.clear();
        tagCount = 0;
        tagTimes = 0;

        try {
            txtCount.setText(String.format("%d", tagCount));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            txtTimes.setText(String.format("%d", tagTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
    }

    @Override
    protected void onPause() {
        isOnPause = true;
        isStop = true;
        if (swInv.isChecked()) {
            swInv.setChecked(false);
            swInv.setClickable(false);
            api.close();
        }
        if (swOpen.isChecked()) {
            swOpen.setChecked(false);
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (swInv.isChecked()) api.close();else
        super.onBackPressed();
    }
}
