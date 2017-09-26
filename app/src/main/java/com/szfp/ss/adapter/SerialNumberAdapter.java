package com.szfp.ss.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.ss.R;
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.utils.ContextUtils;

import java.util.List;

/**
 * author：ct on 2017/9/26 12:03
 * email：cnhttt@163.com
 */

public class SerialNumberAdapter  extends BaseListAdapter<ParkingRecordReportBean>{
    public SerialNumberAdapter(Context context) {
        super(context);
    }

    public SerialNumberAdapter(Context mContext, List<ParkingRecordReportBean> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView == null){
            convertView = ContextUtils.inflate(mContext, R.layout.simple_list_item_1);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvContent.setText(position+1+getItem(position).getSerialNumber());


        return convertView;
    }

    private class ViewHolder {
        TextView tvContent;
        public ViewHolder(View convertView) {
            tvContent = (TextView) convertView.findViewById(R.id.title);
        }


    }
}
