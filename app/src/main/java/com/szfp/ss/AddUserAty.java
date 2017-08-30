package com.szfp.ss;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;

import com.szfp.ss.domain.PagerItem;
import com.szfp.ss.domain.UserInformation;
import com.szfp.utils.Slidr;
import com.szfp.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.utils.DataUtils.isNullString;

public class AddUserAty extends BaseAty {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_first_name)
    AutoCompleteTextView etFirstName;
    @BindView(R.id.et_last_name)
    AutoCompleteTextView etLastName;
    @BindView(R.id.et_license_plate_number)
    AutoCompleteTextView etLicensePlateNumber;
    @BindView(R.id.et_telephone_number)
    AutoCompleteTextView etTelephoneNumber;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private String lastName;
    private String firstName;
    private String licensePlateNumber;
    private String telephoneNumber;

    private UserInformation userInfo;

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

    }

    @OnClick(R.id.bt_next)
    public void onClick() {
        lastName = etLastName.getText().toString();
        firstName = etFirstName.getText().toString();
        licensePlateNumber =etLicensePlateNumber.getText().toString();
        telephoneNumber = etTelephoneNumber.getText().toString();


        if (isNullString(firstName))
        {
            etFirstName.setError("Please Input First Name");return;
        }
        if (isNullString(lastName))
        {
            etLastName.setError("Please Input Last Name");return;
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
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        userInfo.setLicensePlateNumber(licensePlateNumber);
        userInfo.setTelephoneNumber(telephoneNumber);


    }
}
