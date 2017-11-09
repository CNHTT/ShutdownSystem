package com.szfp.asoriba.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.asoriba.R;
import com.szfp.utils.ContextUtils;
import com.szfp.view.button.SelectButton;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：ct on 2017/10/18 09:35
 * email：cnhttt@163.com
 */

public class TagReadListAdapter extends BaseListAdapter<String> {


    protected static HashMap<String, Integer> number ;
    private int curSelPosition = -1;
    private OnTagEPC callback;

    public void setCallback(OnTagEPC callback) {
        this.callback = callback;
    }

    public TagReadListAdapter(Context mContext, List<String> mDatas, HashMap<String, Integer> number) {
        super(mContext, mDatas);
        this.number = number;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        curSelPosition = position;
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = ContextUtils.inflate(mContext, R.layout.taglist_read_layout);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.epcId.setText(getItem(position));
        viewHolder.readNum.setText(""+number.get(getItem(position)));

        viewHolder.btWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onStrEpc(getItem(position));
            }
        });
        return convertView;
    }

    public  void setNumber(HashMap<String, Integer> number) {
        TagListAdapter.number = number;
    }

    static class ViewHolder {
        @BindView(R.id.epcId)
        TextView epcId;
        @BindView(R.id.readNum)
        TextView readNum;
        @BindView(R.id.bt_write)
        SelectButton btWrite;
        @BindView(R.id.datalist)
        LinearLayout datalist;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnTagEPC{
        void  onStrEpc(String string);
    }
}
