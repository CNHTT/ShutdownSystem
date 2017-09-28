package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.szfp.ss.adapter.PrintDataAdapter;
import com.szfp.ss.adapter.PrintDataParkAdapter;
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.inter.OnPrintParkClickListener;
import com.szfp.ss.inter.OnPrintParkListener;
import com.szfp.ss.inter.OnPrintRechargeClickListener;
import com.szfp.ss.inter.OnPrintRechargeListener;
import com.szfp.ss.utils.DbHelper;
import com.szfp.ss.utils.PrintUtils;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.ToastUtils;
import com.szfp.view.dialog.DialogSureCancel;
import com.szfp.view.listview.PullListView;
import com.szfp.view.listview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintActivity extends BaseReadActivity implements PullToRefreshLayout.OnRefreshListener, OnPrintRechargeListener, OnPrintRechargeClickListener, OnPrintParkListener, OnPrintParkClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_print)
    ImageView ivPrint;
    @BindView(R.id.bt_select_recharge)
    Button btSelectRecharge;
    @BindView(R.id.bt_select_Enter)
    Button btSelectEnter;
    @BindView(R.id.mPullListView)
    PullListView mPullListView;
    @BindView(R.id.mRefreshLayout)
    PullToRefreshLayout mRefreshLayout;

    private int dataNum =0;

    private PrintDataAdapter adapter;
    private List<RechargeRecordBean>  rechargeList=new ArrayList<>();

    private PrintDataParkAdapter parkAdapter;
    private List<ParkingRecordReportBean> parkingList = new ArrayList<>();

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

        showLoadDialog();
        DbHelper.getRechargeList(dataNum, this);





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

    @OnClick({R.id.conn_print, R.id.bt_select_recharge, R.id.bt_select_Enter})
    public void onClick(View view) {
        dataNum=0;
        switch (view.getId()) {
            case R.id.conn_print:
                showDeviceList();
                break;
            case R.id.bt_select_recharge:
                showLoadDialog();
                DbHelper.getRechargeList(dataNum,this);
                break;
            case R.id.bt_select_Enter:
                showLoadDialog();
                DbHelper.getParkRechargeList(dataNum,this);
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

    @Override
    public void success(List<RechargeRecordBean> list) {
        showLoadDismiss();
        rechargeList = list;
        adapter = new PrintDataAdapter(PrintActivity.this,rechargeList,this);
        mPullListView.setAdapter(adapter);
    }

    @Override
    public void callBack(RechargeRecordBean recordBean) {
        if (BluetoothService.IsNoConnection()){
            ToastUtils.showToast(R.string.not_connected);
        }else {
            showPrint(recordBean);
        }
    }


    /**
     *
     */
    private DialogSureCancel printDialog;
    private void showPrint(final RechargeRecordBean recordBean) {
        if (printDialog==null){
            printDialog = new DialogSureCancel(this);
            printDialog.getTvContent().setText("Whether you need to print again");
            printDialog.setCancelable(false);
            printDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrintUtils.printRechargePrint(recordBean);
                    printDialog.cancel();
                }
            });
            printDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printDialog.cancel();
                }
            });
        }
        printDialog.show();
    }



    @Override
    public void successPark(List<ParkingRecordReportBean> list) {

        showLoadDismiss();
        parkingList = list;
        parkAdapter = new PrintDataParkAdapter(PrintActivity.this,parkingList,this);
        mPullListView.setAdapter(parkAdapter);
    }

    @Override
    public void successOnClickPark(ParkingRecordReportBean bean) {
        if (BluetoothService.IsNoConnection()){
            ToastUtils.showToast(R.string.not_connected);
        }else {
            showPrintPark(bean);
        }
    }

    private void showPrintPark(final ParkingRecordReportBean bean) {
        if (printDialog==null){
            printDialog = new DialogSureCancel(this);
            printDialog.getTvContent().setText("Whether you need to print again");
            printDialog.setCancelable(false);
        }
            printDialog.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrintUtils.printParkPrint(bean);
                    printDialog.cancel();
                }
            });
            printDialog.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printDialog.cancel();
                }
            });

        printDialog.show();
    }
}
