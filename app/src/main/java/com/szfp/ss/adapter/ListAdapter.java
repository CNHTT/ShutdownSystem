package com.szfp.ss.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szfp.adapter.BasePullListAdapter;
import com.szfp.ss.R;

import java.util.List;

/**
 * author：ct on 2017/9/27 18:31
 * email：cnhttt@163.com
 */

public class ListAdapter extends BasePullListAdapter<String> {
    public ListAdapter(Context context, List<String> mDataList) {
        super(context, mDataList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = ((Activity) (mContext)).getLayoutInflater().inflate(R.layout.layout_has_no_data, parent, false);
                holder.noDataRootLayout = (LinearLayout) convertView.findViewById(R.id.root_layout);
            } else {
                convertView = ((Activity) (mContext)).getLayoutInflater().inflate(R.layout.item_list_view, parent, false);
                holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
                holder.textView = (TextView) convertView.findViewById(R.id.text_view);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (hasNoData) {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(getScreenWidth(), getScreenHeight() * 2 / 3);
            holder.noDataRootLayout.setLayoutParams(lp);
        } else {
            holder.textView.setText(mDataList.get(position));
        }

        return convertView;
    }
    private static final class ViewHolder {
        ImageView imageView;
        TextView textView;

        LinearLayout noDataRootLayout;
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
