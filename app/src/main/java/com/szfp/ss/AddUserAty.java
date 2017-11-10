package com.szfp.ss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;

import com.szfp.ss.domain.KEY;
import com.szfp.ss.domain.PagerItem;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.domain.model.MemberBean;
import com.szfp.utils.KeyboardUtils;
import com.szfp.utils.Slidr;
import com.szfp.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isNullString;

public class AddUserAty extends BaseNoAty implements View.OnKeyListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_first_name)
    AutoCompleteTextView etFirstName;
    @BindView(R.id.et_email)
    AutoCompleteTextView edEmail;
    @BindView(R.id.et_license_plate_number)
    AutoCompleteTextView etLicensePlateNumber;
    @BindView(R.id.et_telephone_number)
    AutoCompleteTextView etTelephoneNumber;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private String name;
    private String email;
    private String licensePlateNumber;
    private String telephoneNumber;

    private UserInformation userInfo;

    private MemberBean memberBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_aty);
        ButterKnife.bind(this);
        Slidr.attach(this);
        StatusBarUtil.setTranslucent(this,10);
        toolbar.setTitle(PagerItem.ENTRY_INFO);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

    }

    private void initView() {
        etFirstName.setOnKeyListener(this);
        edEmail.setOnKeyListener(this);
        etLicensePlateNumber.setOnKeyListener(this);
        etTelephoneNumber.setOnKeyListener(this);
    }

    @OnClick(R.id.bt_next)
    public void onClick() {
        email = edEmail.getText().toString();
        name = etFirstName.getText().toString();
        licensePlateNumber =etLicensePlateNumber.getText().toString();
        telephoneNumber = etTelephoneNumber.getText().toString();


        if (isNullString(email))
        {
            edEmail.setError("Please Input  Email");return;
        }
        if (isNullString(name))
        {
            etFirstName.setError("Please Input Name");return;
        }
        if (isNullString(licensePlateNumber))
        {
            etLicensePlateNumber.setError("Please Input ");return;
        }
        if (isNullString(telephoneNumber))
        {
            etLicensePlateNumber.setError("Please Input");return;
        }


        userInfo = new UserInformation();
        userInfo.setName(name);
        userInfo.setEmail(email);
        userInfo.setLicensePlateNumber(licensePlateNumber);
        userInfo.setTelephoneNumber(telephoneNumber);

        //
        memberBean = new MemberBean();
        memberBean.setName(name);
        memberBean.setEmail(email);
        memberBean.setLpm(licensePlateNumber);
        memberBean.setPhone(telephoneNumber);


        Intent intent = new Intent();
        if (App.isHF) intent.setClass(AddUserAty.this,EntryCardHFActivity.class);
        else intent.setClass(AddUserAty.this,LuRuKaActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY.INFO,userInfo);
        bundle.putSerializable(KEY.MEMBER,memberBean);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (keyCode) {
            case 66:
                KeyboardUtils.hideSoftInput(this);
                break;
        }
        return false;
    }
}
