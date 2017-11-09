package com.szfp.asoriba.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.szfp.asoriba.R;
import com.szfp.asoriba.adapter.StringAdapter;
import com.szfp.utils.PopupWindowUtil;
import com.szfp.utils.StatusBarUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.bt_dashboard)
    Button btDashboard;
    @BindView(R.id.bt_fingerprint)
    Button btFingerprint;
    @BindView(R.id.bt_reports)
    Button btReports;
    @BindView(R.id.bt_membership)
    Button btMembership;
    @BindView(R.id.bt_nfc)
    Button btNfc;
    @BindView(R.id.bt_settings)
    Button btSettings;
    @BindView(R.id.bt_communication)
    Button btCommunication;
    @BindView(R.id.bt_huf)
    Button btHuf;
    private PopupWindow mPopupWindow;
    private ListView dialogListView;
    private StringAdapter stringAdapter;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        StatusBarUtil.setTranslucent(this, 80);
    }


    private void showMembership(View view) {
        View contentView = getPopupWindowContentView();
        dialogListView = (ListView) contentView.findViewById(R.id.listView);
        data = Arrays.asList(getResources().getStringArray(R.array.membership_list));
        stringAdapter = new StringAdapter(this, data);
        dialogListView.setAdapter(stringAdapter);
        dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, ReadMemberActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, AddMembersActivity.class));
                        break;
                    case 2:

                        startActivity(new Intent(MainActivity.this, WriteNFCActivity.class));
                        break;
                }
            }
        });
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(view, contentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos[0] -= xOff;
        mPopupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    public View getPopupWindowContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_main_view, null);
        return view;
    }

    @OnClick({R.id.bt_dashboard, R.id.bt_fingerprint, R.id.bt_reports, R.id.bt_membership, R.id.bt_nfc, R.id.bt_settings, R.id.bt_communication, R.id.bt_huf})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_fingerprint:startActivity(new Intent(MainActivity.this,FingerprintActivity.class));
                break;
            case R.id.bt_reports:
                break;
            case R.id.bt_nfc:startActivity(new Intent(MainActivity.this,
                    WriteNFCActivity.class));
                break;
            case R.id.bt_settings:
                break;
            case R.id.bt_communication:
                break;

            case R.id.bt_dashboard:
                startActivity(new Intent(MainActivity.this,DashboardActivity.class));
                break;
            case R.id.bt_membership:
                showMembership(view);
                break;
        }
    }
}
