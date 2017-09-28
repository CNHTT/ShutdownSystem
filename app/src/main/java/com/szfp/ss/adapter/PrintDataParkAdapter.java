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
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.inter.OnPrintParkClickListener;
import com.szfp.utils.ContextUtils;
import com.szfp.utils.DataUtils;
import com.szfp.utils.TimeUtils;

import java.util.List;

/**
 * author：ct on 2017/9/28 09:57
 * email：cnhttt@163.com
 */

public class PrintDataParkAdapter extends BasePullListAdapter<ParkingRecordReportBean> {

    private OnPrintParkClickListener listener;

    public PrintDataParkAdapter(Context context, List<ParkingRecordReportBean> mDataList,OnPrintParkClickListener listener) {
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
                convertView = ContextUtils.inflate(mContext,R.layout.layout_print_parking);
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

        private LinearLayout mLl_view;
        private TextView mTv_serial_number;
        private TextView mTv_name;
        private TextView mTv_card_number;
        private TextView mTv_park_number;
        private TextView mTv_park_entry;
        private LinearLayout mLl_view_park;
        private TextView mTv_time;
        private TextView mTv_park_exit;
        private TextView mTv_amount;
        private TextView mTv_over_time;
        private TextView mTv_cash_amount;
        private TextView mTv_card_amount;
        private TextView mTv_pay_before;
        private TextView mTv_pay_after;




        public void initView(View convertView) {
            mLl_view = (LinearLayout) convertView.findViewById(R.id.ll_view);
            mTv_serial_number = (TextView) convertView.findViewById(R.id.tv_serial_number);
            mTv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mTv_card_number = (TextView)convertView. findViewById(R.id.tv_card_number);
            mTv_park_number = (TextView) convertView.findViewById(R.id.tv_park_number);
            mTv_park_entry = (TextView)convertView. findViewById(R.id.tv_park_entry);

            mLl_view_park = (LinearLayout) convertView.findViewById(R.id.ll_view_park);
            mTv_time = (TextView)convertView. findViewById(R.id.tv_time);
            mTv_park_exit = (TextView) convertView.findViewById(R.id.tv_park_exit);
            mTv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            mTv_over_time = (TextView) convertView.findViewById(R.id.tv_over_time);
            mTv_cash_amount = (TextView) convertView.findViewById(R.id.tv_cash_amount);
            mTv_card_amount = (TextView) convertView.findViewById(R.id.tv_card_amount);
            mTv_pay_before = (TextView) convertView.findViewById(R.id.tv_pay_before);
            mTv_pay_after = (TextView) convertView.findViewById(R.id.tv_pay_after);
        }

        /**
         * 显示数据
         * @param data
         */
        public void setData(final ParkingRecordReportBean data) {
            mTv_serial_number .setText(data.getSerialNumber());
            mTv_name .setText(data.getFirstName()+" "+data.getLastName());
            mTv_card_number.setText(data.getLicensePlateNumber());
            mTv_park_number.setText(data.getParkingNumber());
            mTv_park_entry.setText(TimeUtils.milliseconds2String(data.getEnterTime()));
            if (data.getType()==1)
            {
                mLl_view_park.setVisibility(View.VISIBLE);
                mTv_time.setText(TimeUtils.formatParkTime(data.getParkingTime()));
                mTv_park_exit.setText(TimeUtils.milliseconds2String(data.getExitTime()));
                mTv_amount.setText(DataUtils.getAmountValue(data.getParkingAmount()));
                if (data.getDeductionMethod()==0)
                mTv_over_time.setText(TimeUtils.milliseconds2String(data.getParkingTimeIsValidEnd()));
                else mTv_over_time.setText("0000-00-00 00:00:00");
                mTv_cash_amount.setText(DataUtils.getAmountValue(data.getCashAmount()));
                mTv_card_amount.setText( DataUtils.getAmountValue(data.getCardAmount()));

                mTv_pay_before.setText(DataUtils.getAmountValue(data.getPreTradeBalance()));
                mTv_pay_after.setText(DataUtils.getAmountValue(data.getPostTradeBalance()));



            }
            else mLl_view_park.setVisibility(View.GONE);

            mLl_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.successOnClickPark(data);
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
