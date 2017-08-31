package com.szfp.view;

import android.content.Context;
import android.view.View;

/**
 * author：ct on 2017/8/31 16:08
 * email：cnhttt@163.com
 */

public class DialogSure extends BaseDialog {



    public DialogSure(Context context, int themeResId, View view) {
        super(context, themeResId);
        initView(view);
    }

    private void initView(View view) {
        setContentView(view);
    }

    public DialogSure(Context context, boolean cancelable, OnCancelListener cancelListener, View view) {
        super(context, cancelable, cancelListener);
        initView(view);
    }

    public DialogSure(Context context, View view) {
        super(context);
        initView(view);
    }

    public DialogSure(Context context, float alpha, int gravity, View view) {
        super(context, alpha, gravity);
        initView(view);
    }
}
