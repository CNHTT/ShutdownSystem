package com.szfp.ss;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.szfp.utils.BluetoothService;
import com.szfp.utils.DataUtils;
import com.szfp.utils.StatusBarUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.listview.PullListView;
import com.szfp.view.listview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchInformationActivity extends BaseReadActivity implements PullToRefreshLayout.OnRefreshListener, OnPrintRechargeListener, OnPrintRechargeClickListener, OnPrintParkListener, OnPrintParkClickListener {

    @BindView(R.id.iv_print)
    ImageView ivPrint;
    @BindView(R.id.conn_print)
    LinearLayout connPrint;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bt_ve_read)
    Button btVeRead;
    @BindView(R.id.bt_select_recharge)
    Button btSelectRecharge;
    @BindView(R.id.bt_select_Enter)
    Button btSelectEnter;
    @BindView(R.id.mPullListView)
    PullListView mPullListView;
    @BindView(R.id.mRefreshLayout)
    PullToRefreshLayout mRefreshLayout;

    private Calendar cal;
    private int year,month,day;

    private UserInformation user;
    private long time;


    private LinearLayout viewHead;

    private int dataNum =0;

    private boolean is =true;

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
        setContentView(R.layout.activity_search_information);
        ButterKnife.bind(this);
        toolbar.setTitle("SQ");
        setSupportActionBar(toolbar);
        StatusBarUtil.setTranslucent(this, 10);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!BluetoothService.IsNoConnection())
            ivPrint.setBackgroundResource(R.drawable.ic_print_conn);
        mRefreshLayout.setOnRefreshListener(this);
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        Log.i("wxy","year"+year);
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);


        initViewHead();

        showLoadDialog();
        DbHelper.getRechargeList(dataNum, this);
    }





    private TextView mTv_serial_number;
    private TextView mTv_name;
    private TextView mTv_card_number;
    private TextView mTv_balance;
    private TextView mTv_over_time;

    private void initViewHead() {
        viewHead = (LinearLayout) findViewById(R.id.ll_view);
        mTv_serial_number = (TextView) findViewById(R.id.tv_serial_number);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_card_number = (TextView) findViewById(R.id.tv_card_number);
        mTv_balance = (TextView) findViewById(R.id.tv_balance);
        mTv_over_time = (TextView) findViewById(R.id.tv_over_time);
    }

    @Override
    protected void showConnecting() {

    }

    @Override
    protected void showConnectedDeviceName(String mConnectedDeviceName) {

    }

    @Override
    protected void showUser(UserInformation userInformation) {
        mTv_serial_number.setText(userInformation.getUUID());
        mTv_name .setText(userInformation.getFirstName()+" "+userInformation.getLastName());
        mTv_card_number.setText(userInformation.getLicensePlateNumber());
        mTv_balance.setText(DataUtils.getAmountValue(userInformation.getBalance()));
        if (userInformation.getParkingTimeIsValidEnd()!=0)
            mTv_over_time.setText(TimeUtils.milliseconds2String(userInformation.getParkingTimeIsValidEnd()));
        else mTv_over_time.setText("0000-00-00 00:00:00");
        viewHead.setVisibility(View.VISIBLE);
        user = userInformation;
        DbHelper.getSearchRechargeList(user,time,dataNum,this);

    }

    @OnClick({R.id.iv_print, R.id.bt_ve_read, R.id.bt_select_recharge, R.id.bt_select_Enter,R.id.sq_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_print:
                showDeviceList();
                break;
            case R.id.bt_ve_read:
                showSlotCard();
                break;
            case R.id.bt_select_recharge:
                dataNum=0;is =true;

                DbHelper.getSearchRechargeList(user,time,dataNum,this);
                break;
            case R.id.bt_select_Enter:
                dataNum=0;is =false;
                DbHelper.getSearchParkList(user,time,dataNum,this);
                break;
            case R.id.sq_time:
                showTime();
                break;
        }
    }

    private void showTime() {
        DatePickerDialog date1PickerDialog = new DatePickerDialog(SearchInformationActivity.this, 0,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ++month;
                        if (month>10)time= TimeUtils.stringMillis(year+"-"+month+"-"+dayOfMonth);
                        else time= TimeUtils.stringMillis(year+"-0"+month+"-"+dayOfMonth);

                        Log.d("Time", time+"");
                    }
                },year,month,day);
        date1PickerDialog.show();
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        dataNum=0;
        if (is){
            DbHelper.getSearchRechargeList(user,time,dataNum,this);
        }else {
            DbHelper.getSearchParkList(user,time,dataNum,this);
        }
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                mRefreshLayout.refreshFinish(true);
            }
        }, 2000); // 2秒后刷新
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (is){
            DbHelper.getSearchRechargeList(user, time, dataNum, new OnPrintRechargeListener() {
                @Override
                public void success(List<RechargeRecordBean> list) {
                    if (list.size()==0) ToastUtils.showToast(R.string.no_data);else{
                        adapter.updateListView(list);dataNum++;
                    }

                }
            });
        }else {
            DbHelper.getSearchParkList(user, time, dataNum, new OnPrintParkListener() {
                @Override
                public void successPark(List<ParkingRecordReportBean> list) {
                    if (list.size()==0) ToastUtils.showToast(R.string.no_data);else {
                        parkAdapter.updateListView(list); dataNum++;
                    }

                }
            });
        }

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
        adapter = new PrintDataAdapter(SearchInformationActivity.this,rechargeList,this);
        mPullListView.setAdapter(adapter);
    }

    @Override
    public void callBack(RechargeRecordBean recordBean) {
    }

    @Override
    public void successPark(List<ParkingRecordReportBean> list) {
        showLoadDismiss();
        parkingList = list;
        parkAdapter = new PrintDataParkAdapter(SearchInformationActivity.this,parkingList,this);
        mPullListView.setAdapter(parkAdapter);

    }

    @Override
    public void successOnClickPark(ParkingRecordReportBean bean) {
    }
}
