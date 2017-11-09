package com.szfp.asoriba.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szfp.adapter.BaseListAdapter;
import com.szfp.asoriba.R;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.inter.OnClickListenerMember;
import com.szfp.utils.ContextUtils;
import com.szfp.view.button.SelectButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：ct on 2017/10/13 09:25
 * email：cnhttt@163.com
 */

public class ListAdapter extends BaseListAdapter<MemberBean> {
    public OnClickListenerMember listener;

    public ListAdapter(Context mContext, List<MemberBean> mDatas,OnClickListenerMember listener) {
        super(mContext, mDatas);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = ContextUtils.inflate(mContext, R.layout.layout_list);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(getItem(position), position);

        return convertView;
    }

     class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_fingerprint)
        TextView tvFingerprint;
        @BindView(R.id.tc_isCard)
        TextView tcIsCard;
        @BindView(R.id.bt_show)
        SelectButton btShow;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setData(final MemberBean item, int position) {
            tvName.setText(item.getFirstName()+"·"+item.getLastName());
            tvFingerprint.setText(item.getChooseType());
            tcIsCard.setText(item.getIsBind()?"Yes":"N0");
            btShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }
}
