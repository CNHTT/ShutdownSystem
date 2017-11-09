package com.szfp.asoriba.view;

import android.os.Bundle;
import android.widget.ListView;

import com.szfp.asoriba.R;
import com.szfp.asoriba.adapter.ListAdapter;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.inter.OnClickListenerMember;
import com.szfp.asoriba.utils.DbHelper;
import com.szfp.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends BaseAty implements OnClickListenerMember {

    @BindView(R.id.listView)
    ListView listView;
    private List<MemberBean> list;

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        list = DbHelper.getList();
        adapter = new ListAdapter(this,list,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(MemberBean memberBean) {
        ToastUtils.showToast(memberBean.getFirstName());
    }
}
