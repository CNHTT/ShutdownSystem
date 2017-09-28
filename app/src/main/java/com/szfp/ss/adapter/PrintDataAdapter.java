package com.szfp.ss.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szfp.adapter.BasePullListAdapter;
import com.szfp.ss.R;
import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.inter.OnPrintRechargeClickListener;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.DataUtils;
import com.szfp.utils.TimeUtils;

import java.util.List;

/**
 * author：ct on 2017/9/27 18:50
 * email：cnhttt@163.com
 */

public class PrintDataAdapter extends BasePullListAdapter<RechargeRecordBean> {

    private OnPrintRechargeClickListener listener;

    public PrintDataAdapter(Context context, List<RechargeRecordBean> mDataList, OnPrintRechargeClickListener listener) {
        super(context, mDataList);
        this.listener = listener;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            if (getItemViewType(position)==0){
                convertView = ContextUtils.inflate(mContext, R.layout.layout_has_no_data);
                viewHolder.noDataRootLayout = (LinearLayout) convertView.findViewById(R.id.root_layout);
            }else
            {
                convertView = ContextUtils.inflate(mContext,R.layout.layout_print_recharge);
                viewHolder.initView(convertView);

            }
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (hasNoData) {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(getScreenWidth(), getScreenHeight() * 2 / 3);
            viewHolder.noDataRootLayout.setLayoutParams(lp);
        } else {
            viewHolder.setData(getItem(position));

        }


        return convertView;
    }

    private class ViewHolder{
        private LinearLayout noDataRootLayout;



        // Content View Elements

        private TextView mTv_serial_number;
        private TextView mTv_name;
        private TextView mTv_card_number;
        private LinearLayout mLl_view_recharge;
        private TextView mTv_recharge_amount;
        private TextView mTv_time;
        private LinearLayout mLl_view_purchase;
        private LinearLayout mLlView;
        private TextView mTv_num;
        private TextView mTv_type;
        private TextView mTv_cash_amount;
        private TextView mTv_card_amount;
        private TextView mTv_pay_before;
        private TextView mTv_pay_after;

        public void initView(View convertView) {
            mTv_serial_number = (TextView) convertView.findViewById(R.id.tv_serial_number);
            mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mTv_card_number = (TextView) convertView.findViewById(R.id.tv_card_number);
            mLl_view_recharge = (LinearLayout) convertView.findViewById(R.id.ll_view_recharge);
            mTv_recharge_amount = (TextView) convertView.findViewById(R.id.tv_recharge_amount);
            mTv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mLl_view_purchase = (LinearLayout) convertView.findViewById(R.id.ll_view_purchase);
            mLlView = (LinearLayout) convertView.findViewById(R.id.ll_view);
            mTv_num = (TextView) convertView.findViewById(R.id.tv_num);
            mTv_type = (TextView) convertView.findViewById(R.id.tv_type);
            mTv_cash_amount = (TextView) convertView.findViewById(R.id.tv_cash_amount);
            mTv_card_amount = (TextView) convertView.findViewById(R.id.tv_card_amount);
            mTv_pay_before = (TextView) convertView.findViewById(R.id.tv_pay_before);
            mTv_pay_after = (TextView) convertView.findViewById(R.id.tv_pay_after);
        }

        public void setData(final RechargeRecordBean data) {
            if (data.getTradeType().equals("1")){
                mLlView.setBackgroundColor(mContext.getResources().getColor(R.color.shallowgrey));
                mLl_view_purchase.setVisibility(View.GONE);
                mTv_serial_number.setText(data.getSerialNumber());
                mTv_name.setText(data.getFirstName() + " " +data.getLastName());
                mTv_card_number.setText(data.getCardNumber());
                mTv_recharge_amount.setText(data.getRechargeAmount());
                mTv_time.setText(TimeUtils.milliseconds2String(data.getCreateTime()));
                mTv_pay_before.setText(DataUtils.getAmountValue(data.getPrepaidAmount()));
                mTv_pay_after.setText(DataUtils.getAmountValue(data.getAfterAmount()));

            }else {

                mLlView.setBackgroundColor(mContext.getResources().getColor(R.color.ghostwhite));
                mLl_view_purchase.setVisibility(View.VISIBLE);
                mTv_serial_number.setText(data.getSerialNumber());
                mTv_name.setText(data.getFirstName() + " " +data.getLastName());
                mTv_card_number.setText(data.getCardNumber());
                mTv_recharge_amount.setText(DataUtils.getAmountValue(data.getTwoAmount()));
                mTv_time.setText(TimeUtils.milliseconds2String(data.getCreateTime()));
                mTv_pay_before.setText(DataUtils.getAmountValue(data.getPrepaidAmount()));
                mTv_pay_after.setText(DataUtils.getAmountValue(data.getAfterAmount()));
                mTv_num.setText(data.getTwoBuyNum()+"");
                mTv_type.setText(data.getTwoBuyName());
                mTv_cash_amount.setText(DataUtils.getAmountValue(data.getTwoCashAmount()));
                mTv_card_amount.setText(DataUtils.getAmountValue(data.getTwoCardAmount()));

            }
            mLlView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.callBack(data);
                }
            });


        }
    }
    private int getScreenWidth() {
        DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
        return displayMetric.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
        return displayMetric.heightPixels;
    }
}
