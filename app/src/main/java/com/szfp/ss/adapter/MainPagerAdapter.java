package com.szfp.ss.adapter;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szfp.ss.domain.MainImginfo;

import java.util.ArrayList;

/**
 * author：ct on 2017/8/29 16:38
 * email：cnhttt@163.com
 */

public class MainPagerAdapter  extends PagerAdapter{

    Vibrator vibrator;
    ArrayList<MainImginfo> data;
    Activity activity;
    LayoutParams params;
    int datasize =3;
    int source =0;
    int add =0;
    int[] ids;
    OnItemClickListener listener;
    public MainPagerAdapter(Activity mainActivity, ArrayList<MainImginfo> mData, int[] ids, OnItemClickListener listener) {
        this.activity = mainActivity;
        this.data = mData;
        this.ids = ids;
        this.listener = listener;
        this.handleData();
        this.vibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
    }

    private void handleData() {
        int source = this.data.size();
        if ( source % datasize !=0 )
        {
            this.add = (source /this.datasize+1) *this.datasize -source;
            MainImginfo info = data.get(source-1);
            for (int i = 0; i <add ; i++) {
                this.data.add(source+i,new MainImginfo(source+i,info.getImageMsg(),info.getImageId()));
            }
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int index) {
        View view = this.activity.getLayoutInflater().inflate(this.ids[0],null);
        GridView gridView = (GridView) view.findViewById(this.ids[1]);
        gridView.setNumColumns(1);
        gridView.setVerticalSpacing(20);
        gridView.setHorizontalSpacing(5);
        gridView.setAdapter(new BaseAdapter() {
            public int getCount() {
                return MainPagerAdapter.this.datasize;
            }

            public Object getItem(int position) {
                return Integer.valueOf(position);
            }

            public long getItemId(int position) {
                return (long)position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                View item = LayoutInflater.from(MainPagerAdapter.this.activity).inflate(MainPagerAdapter.this.ids[2], (ViewGroup)null);
                ImageView iv = (ImageView)item.findViewById(MainPagerAdapter.this.ids[3]);
                RelativeLayout relativeLayout = (RelativeLayout)item.findViewById(MainPagerAdapter.this.ids[4]);
                TextView tv = (TextView)item.findViewById(MainPagerAdapter.this.ids[5]);
                Log.v("DESERT", "index = " + index + ",position = " + position);
                iv.setImageResource(MainPagerAdapter.this.data.get(index * MainPagerAdapter.this.datasize + position).getImageId());
                tv.setText((MainPagerAdapter.this.data.get(index * MainPagerAdapter.this.datasize + position)).getImageMsg());
                if(1 == index && "add".equals(tv.getText().toString())) {
                    relativeLayout.setVisibility(View.GONE);
                }

                return item;
            }
        });
        gridView.setOnItemClickListener(this.listener);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.v("DESERT", "item = " + arg2);
                vibrator.vibrate(2500L);
                return true;
            }
        });
        ((ViewPager)container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewGroup) container).removeView((View) object);

        object=null;
    }
}
