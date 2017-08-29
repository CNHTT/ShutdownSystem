package com.szfp.ss.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.ss.domain.MainImginfo;
import com.szfp.utils.ContextUtils;

import java.util.List;

/**
 * author：ct on 2017/8/29 17:23
 * email：cnhttt@163.com
 */

public class MainGridViewAdapter extends BaseListAdapter<MainImginfo> {
    private int[] ids;
    private int index;
    public MainGridViewAdapter(Context mContext, List<MainImginfo> mDatas, int[] ids, int index) {
        super(mContext, mDatas);
        this.ids = ids;
        this.index = index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if ( convertView == null)

        {
            convertView= ContextUtils.inflate(mContext,ids[2]);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);



        return convertView;
    }


    private  class ViewHolder{
        ImageView img;
        RelativeLayout relativeLayout;
        TextView textView;


        public ViewHolder(View convertView) {
            img = (ImageView) convertView.findViewById(ids[3]);
            relativeLayout = (RelativeLayout) convertView.findViewById(ids[4]);
            textView = (TextView) convertView.findViewById(ids[5]);
        }

        public void setData(int position) {
            img.setImageResource(mDatas.get(index * 6+position).getImageId());
            textView.setText(mDatas.get(index *6+position).getImageMsg());
            if(1 == index && "add".equals(textView.getText().toString())) {
                relativeLayout.setVisibility(View.GONE);
            }

        }
    }
}
