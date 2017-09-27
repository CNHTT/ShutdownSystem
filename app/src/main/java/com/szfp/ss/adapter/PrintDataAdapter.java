package com.szfp.ss.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.szfp.adapter.BasePullListAdapter;

import java.util.List;
import java.util.Objects;

/**
 * author：ct on 2017/9/27 18:50
 * email：cnhttt@163.com
 */

public class PrintDataAdapter extends BasePullListAdapter<Objects> {
    public PrintDataAdapter(Context context, List<Objects> mDataList) {
        super(context, mDataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        
        return super.getView(position, convertView, parent);
    }
}
