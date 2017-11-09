package com.szfp.asoriba.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.asoriba.R;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.utils.ContextUtils;

import java.util.List;

/**
 * author：ct on 2017/10/20 17:08
 * email：cnhttt@163.com
 */

public class NFCMemberAdapter extends BaseListAdapter<MemberBean> {
    public NFCMemberAdapter(Context mContext, List<MemberBean> mDatas) {
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
        viewHolder.tvContent.setText(getItem(position).getFirstName()+"."+getItem(position).getLastName());


        return convertView;
    }

    private class ViewHolder {
        TextView tvContent;
        public ViewHolder(View convertView) {
            tvContent = (TextView) convertView.findViewById(R.id.title);
        }


    }
}
