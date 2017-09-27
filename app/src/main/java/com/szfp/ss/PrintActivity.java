package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.szfp.ss.domain.UserInformation;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.listview.PullListView;
import com.szfp.view.listview.PullToRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintActivity extends BaseReadActivity implements PullToRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_print)
    ImageView ivPrint;
    @BindView(R.id.bt_select_recharge)
    Button btSelectRecharge;
    @BindView(R.id.bt_select_purchase)
    Button btSelectPurchase;
    @BindView(R.id.bt_select_Enter)
    Button btSelectEnter;
    @BindView(R.id.bt_select_Exit)
    Button btSelectExit;
    @BindView(R.id.mPullListView)
    PullListView mPullListView;
    @BindView(R.id.mRefreshLayout)
    PullToRefreshLayout mRefreshLayout;

    @Override
    protected void showDisconnecting() {
        showLoadDismiss();
        ivPrint.setBackgroundResource(R.drawable.ic_print_no_conn);
        ToastUtils.error("The printer connection is disconnected");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        toolbar.setTitle("PRINT");
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucent(this, 10);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!BluetoothService.IsNoConnection())
            ivPrint.setBackgroundResource(R.drawable.ic_print_conn);
        mRefreshLayout.setOnRefreshListener(this);


    }

    @Override
    protected void showConnecting() {
        showLoadDialog();

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {
        ivPrint.setBackgroundResource(R.drawable.ic_print_conn);
        showLoadDismiss();

    }

    @Override
    protected void showUser(UserInformation userInformation) {

    }

    @OnClick({R.id.conn_print, R.id.bt_select_recharge, R.id.bt_select_purchase, R.id.bt_select_Enter, R.id.bt_select_Exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.conn_print:
                showDeviceList();
                break;
            case R.id.bt_select_recharge:
                break;
            case R.id.bt_select_purchase:
                break;
            case R.id.bt_select_Enter:
                break;
            case R.id.bt_select_Exit:
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                mRefreshLayout.refreshFinish(true);
            }
        }, 2000); // 2秒后刷新
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.loadMoreFinish(true);

            }
        }, 2000); // 2秒后刷新
    }
}
