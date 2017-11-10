package com.szfp.ss.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.ss.R;
import com.szfp.ss.domain.ParkingLotBean;
import com.szfp.utils.ContextUtils;

import java.util.List;

/**
 * Created by 戴尔 on 2017/11/10.
 */

public class ParkingLotAdapter extends BaseListAdapter<ParkingLotBean> {
    public ParkingLotAdapter(Context context) {
        super(context);
    }

    public ParkingLotAdapter(Context mContext, List<ParkingLotBean> mDatas) {
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
        viewHolder.tvContent.setText(getItem(position).getName()+ " ("+getItem(position).getNumber()+")");


        return convertView;
    }
    private class ViewHolder {
        TextView tvContent;
        public ViewHolder(View convertView) {
            tvContent = (TextView) convertView.findViewById(R.id.title);
        }


    }
}
