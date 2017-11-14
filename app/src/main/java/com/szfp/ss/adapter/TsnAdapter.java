package com.szfp.ss.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.ss.R;
import com.szfp.ss.domain.model.ParkingRecordBean;
import com.szfp.utils.ContextUtils;

import java.util.List;

/**
 * Created by 戴尔 on 2017/11/14.
 */

public class TsnAdapter extends BaseListAdapter<ParkingRecordBean> {
    public TsnAdapter(Context context) {
        super(context);
    }

    public TsnAdapter(Context mContext, List<ParkingRecordBean> mDatas) {
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
        viewHolder.tvContent.setText(position+1+":"+getItem(position).getTsn());


        return convertView;
    }

    private class ViewHolder {
        TextView tvContent;
        public ViewHolder(View convertView) {
            tvContent = (TextView) convertView.findViewById(R.id.title);
        }

    }
}
