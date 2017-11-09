package com.szfp.asoriba.view;

import android.app.Activity;
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
import com.szfp.asoriba.adapter.TagReadListAdapter;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.utils.DbHelper;
import com.szfp.utils.DataUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.dialog.DialogSureCancel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android_serialport_api.UHFHXAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadMemberActivity extends BaseUHFActivity {


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
    boolean isFirstShow = true;
    private UHFHXAPI api;

    private MemberBean memberBean;

    @OnClick({R.id.bt_singleScan, R.id.bt_setting, R.id.bt_setting_params})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_singleScan:
                showProgressDialog("read  ......");
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        api.startAutoRead2C(5000, 2, "00000000", 0, 8,
                                new UHFHXAPI.SearchAndRead() {

                                    @Override
                                    public void timeout() {
                                        cancleProgressDialog();
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
                                    public void returnData(final byte[] data) {cancleProgressDialog();
                                        mhandler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                ToastUtils
                                                        .showToast(
                                                                "data:"
                                                                        + DataUtils
                                                                        .toHexString(data));


                                                if (tagListAdapter!= null) tagListAdapter.cleanItems();



                                                    memberBean = DbHelper.getMemberEpc(DataUtils
                                                            .toHexString(data));
                                                    if (memberBean !=null){
                                                        name.add(memberBean.getFirstName()+"."+memberBean.getLastName());
                                                        number.put(memberBean.getFirstName()+"."+memberBean.getLastName(), 1);
                                                    }else {
                                                        number.put("The card is not used", 1);
                                                        name.add("The card is not used");
                                                    }
                                                if (tagListAdapter == null) {
                                                    tagListAdapter = new TagReadListAdapter(ReadMemberActivity.this, name, number);
                                                    tagListAdapter.setCallback(new TagReadListAdapter.OnTagEPC() {
                                                        @Override
                                                        public void onStrEpc(String string) {

                                                        }
                                                    });
                                                    lvTagList.setAdapter(tagListAdapter);

                                                } else {
                                                    tagListAdapter.setNumber(number);
                                                    tagListAdapter.updateItems(name);
                                                }






                                            }
                                        });
                                    }

                                    @Override
                                    public void readFail() {cancleProgressDialog();
                                        mhandler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                ToastUtils
                                                        .showToast(
                                                                "read fail");
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
                String[] dbmStr = "30.5".split("\\.");
                int dbm = Integer.parseInt(dbmStr[0] + dbmStr[1]);
//                int dbm = 305;
                byte[] data = { 0x00, (byte) dbm };
                UHFHXAPI.Response response = api
                        .setTxPowerLevel(data);
                if (response.result == UHFHXAPI.Response.RESPONSE_PACKET) {
                    if (response.data[0] == 0x00) {
                        ToastUtils.showToast(
                                "Update success!");
                        return;
                    }
                }
                ToastUtils.showToast(
                        "Update fail!");
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

    private TagReadListAdapter tagListAdapter;

    private Handler mhandler;
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
    String   text = null  ;
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

            String ap = "00000000";
            short epcLength = (short) (flagID.length() / 2);
            String epc = flagID;
            byte mb = (byte) 2;
            mb++;
            byte[] arguments = Bytes.concat(DataUtils.hexStringTobyte(ap),
                    DataUtils.short2byte(epcLength),
                    DataUtils.hexStringTobyte(epc), new byte[] { mb },
                    DataUtils.short2byte((short) 0), DataUtils.short2byte((short) 8));
            String data = readTag(arguments);
            if (!TextUtils.isEmpty(data)) {

                name.add(flagID);


            } else {
                ToastUtils.showToast( "fail！");
            }


            number.put(flagID, 1);
            tagCount++;
            tagInfoList.add(flagID);
            nameInfoList.put(flagID,text);

            try {
                txtCount.setText(String.format("%d", tagCount));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            text = nameInfoList.get(flagID);
            int num = number.get(flagID);
            number.put(flagID, ++num);
            Log.i("whw", "flagID=" + flagID + "   num=" + num);
        }
        if (tagListAdapter == null) {
            tagListAdapter = new TagReadListAdapter(ReadMemberActivity.this, name, number);
            tagListAdapter.setCallback(new TagReadListAdapter.OnTagEPC() {
                @Override
                public void onStrEpc(String string) {
                    ToastUtils.showToast("operate");
                }
            });
            lvTagList.setAdapter(tagListAdapter);

        } else {
            tagListAdapter.setNumber(number);
            tagListAdapter.updateItems(name);
        }


        tagTimes++;
        try {
            txtTimes.setText(String.format("%d", tagTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String readTag(byte[] args) {
        UHFHXAPI.Response response = api
                .readTypeCTagData(args);
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
        setContentView(R.layout.activity_read_member);
        ButterKnife.bind(this);
        mhandler = new Handler();
        api = new UHFHXAPI();
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
                    if (tagListAdapter !=null)tagListAdapter.cleanItems();
                    isStop = false;
                    Inv();
                    btSettingParams.setEnabled(false);
                } else {
                    isStop = true;
                    btSettingParams.setEnabled(true);
                    showProgressDialog("loading......");
                    name =new ArrayList<String>();
                    for (int i = 0; i <tagInfoList.size() ; i++) {
                        String ap = "00000000";
                        short epcLength = (short) (tagInfoList.get(i).length() / 2);
                        String epc = tagInfoList.get(i);
                        byte mb = (byte) 2;
                        mb++;
                        byte[] arguments = Bytes.concat(DataUtils.hexStringTobyte(ap),
                                DataUtils.short2byte(epcLength),
                                DataUtils.hexStringTobyte(epc), new byte[] { mb },
                                DataUtils.short2byte((short) 0), DataUtils.short2byte((short) 8));
                        String data = readTag(arguments);
                        if (!TextUtils.isEmpty(data)) {
                            memberBean = DbHelper.getMemberEpc(data);
                            if (memberBean !=null){
                                text = memberBean.getFirstName()+"."+memberBean.getLastName();
                                name.add(text);
                            }else {
                                text = tagInfoList.get(i)+ "The card is not used";
                                name.add(text);
                            }
                        } else {
                            ToastUtils.showToast( "fail！");
                        }

                    }
                }
                cancleProgressDialog();

                tagListAdapter = new TagReadListAdapter(ReadMemberActivity.this, name, number);
                tagListAdapter.setCallback(new TagReadListAdapter.OnTagEPC() {
                    @Override
                    public void onStrEpc(String string) {
                        ToastUtils.showToast("operate");
                    }
                });
                lvTagList.setAdapter(tagListAdapter);
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
    public void onBackPressed() {
        if (swInv.isChecked()) api.close();else
            super.onBackPressed();
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
}