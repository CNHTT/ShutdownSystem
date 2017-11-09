package com.szfp.asoriba.view;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szfp.asoriba.R;
import com.szfp.asoriba.adapter.NFCMemberAdapter;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.utils.DbHelper;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;
import com.szfp.view.progress.style.CubeGrid;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android_serialport_api.NFC.NdefMessageParser;
import android_serialport_api.NFC.ParsedNdefRecord;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteNFCActivity extends AppCompatActivity {
    private static final DateFormat TIME_FORMAT = SimpleDateFormat
            .getDateTimeInstance();
    @BindView(R.id.card_pat_wv)
    WebView cardPatWv;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.rl_pat_card)
    RelativeLayout rlPatCard;
    @BindView(R.id.bt_clear)
    SelectButton btClear;
    @BindView(R.id.ll_list)
    ListView llList;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private AlertDialog mDialog;
    private CubeGrid mChasingDotsDrawable;

    private static NFCMemberAdapter adapter;
    private List<MemberBean> list=new ArrayList<>();
    private MemberBean memberBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nfc);
        ButterKnife.bind(this);

        resolveIntent(getIntent());

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null)
                .create();
        // 获取默认的NFC控制器
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        //拦截系统级的NFC扫描，例如扫描蓝牙
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord("",
                Locale.ENGLISH, true)});


        cardPatWv.loadDataWithBaseURL(null, "<HTML><body bgcolor='#FFF'><div align=center>" +
                "<img width=\"207\" height=\"207\" src='file:///android_asset/gif/dyn_pat.gif'/></div></body></html>", "text/html", "UTF-8", null);

        mChasingDotsDrawable = new CubeGrid();
        mChasingDotsDrawable.setColor(Color.MAGENTA);
        image.setImageDrawable(mChasingDotsDrawable);
        image.setBackgroundColor(Color.parseColor("#00000000"));
        try {
            memberBean = (MemberBean) getIntent().getExtras().getSerializable("MEMBER");
        } catch (Exception e) {
            memberBean = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter == null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }

            showMessage(R.string.error, R.string.no_nfc);
            ToastUtils.error("设备不支持NFC！");
            return;
        }
        if (!mAdapter.isEnabled()) {
            ToastUtils.error("请在系统设置中先启用NFC功能！");
            return;
        }

        if (mAdapter != null) {
            //隐式启动
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }

        mChasingDotsDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            //隐式启动
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mChasingDotsDrawable.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mAdapter!=null)mAdapter=null;
    }

    //16进制字符串转换为String
    private String hexString = "0123456789ABCDEF";

    public String decode(String bytes) {
        if (bytes.length() != 30) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    // 字符序列转换为16进制字符串
    private static String bytesToHexString(byte[] src, boolean isPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isPrefix == true) {
            stringBuilder.append("0x");
        }
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.toUpperCase(Character.forDigit(
                    (src[i] >>> 4) & 0x0F, 16));
            buffer[1] = Character.toUpperCase(Character.forDigit(src[i] & 0x0F,
                    16));
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    private NdefRecord newTextRecord(String text, Locale locale,
                                     boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(
                Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
                .forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
                new byte[0], data);
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        builder.create().show();
        return;
    }

    //初步判断是什么类型NFC卡
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent
                        .getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
                        empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }
            // Setup the views
            buildTagViews(msgs);
        }
    }

    //一般公家卡，扫描的信息
    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();


        sb.append(getHex(id));//append("Tag ID (hex): ")
        return sb.toString();
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    //显示NFC扫描的数据
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        // Parse the first message in the list
        // Build views for all of the sub records
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String fid = record.getViewText().replace(" ", "").replace("\n", "");

            if (memberBean != null) {
                memberBean.setUUID(fid);
                DbHelper.upMember(memberBean);
                finish();
                ToastUtils.success("SUCCESS");
            } else {
                memberBean = DbHelper.getMemberUUID(fid);
                if (memberBean!=null){
                    if (!list.contains(memberBean)){
                        list.add(memberBean);
                        adapter =  new NFCMemberAdapter(WriteNFCActivity.this,list);
                        llList.setAdapter(adapter);
                    }else {ToastUtils.showToast(memberBean.getFirstName()+"-"+memberBean.getLastName());
                    }memberBean=null;
                }else {
                    ToastUtils.showToast("This Card No User");
                }

            }
        }
    }


    //获取系统隐式启动的
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    @OnClick(R.id.bt_clear)
    public void onClick() {
    }
}
