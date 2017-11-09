package com.szfp.asoriba.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.szfp.asoriba.R;
import com.szfp.asoriba.adapter.StringAdapter;
import com.szfp.asoriba.bean.FingerprintBean;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.inter.OnSuccessCallBack;
import com.szfp.asoriba.utils.DbHelper;
import com.szfp.asynctask.AsyncFingerprint;
import com.szfp.utils.AppManager;
import com.szfp.utils.PopupWindowUtil;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;
import com.szfp.view.button.SelectButton;

import java.util.Arrays;
import java.util.List;

import android_serialport_api.FingerprintAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.asoriba.App.logger;
import static com.szfp.utils.DataUtils.isNullString;

public class AddMembersActivity extends BaseAty {

    @BindView(R.id.bt_basic)
    TextView btBasic;
    @BindView(R.id.bt_contacts)
    TextView btContacts;
    @BindView(R.id.bt_church)
    TextView btChurch;
    @BindView(R.id.bt_family)
    TextView btFamily;
    @BindView(R.id.bt_work_edu)
    TextView btWorkEdu;
    @BindView(R.id.sp_choose_type)
    Spinner spChooseType;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.sp_gender)
    Spinner spGender;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_enter_finger)
    SelectButton etEnterFinger;
    @BindView(R.id.sp_title)
    Spinner spTitle;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_membership_ID)
    EditText etMembershipID;
    @BindView(R.id.bt_create_membership_ID)
    SelectButton btCreateMembershipID;
    @BindView(R.id.et_save_basic)
    SelectButton etSaveBasic;
    @BindView(R.id.ll_view_basic)
    LinearLayout llViewBasic;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.sp_marital)
    Spinner spMarital;
    @BindView(R.id.tv_basic_data)
    TextView tvBasicData;


    private String chooseType;
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String marital;
    private String phone;
    private String membershipId;
    private String UUID;

    private MemberBean memberBean;
    private FingerprintBean fingerprintBean;


    private AsyncFingerprint asyncFingerprint;
    private byte[] model;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AsyncFingerprint.SHOW_PROGRESSDIALOG:
                    cancleProgressDialog();
                    showProgressDialog((Integer) msg.obj,asyncFingerprint);
                    break;
                case AsyncFingerprint.SHOW_FINGER_IMAGE:
                    // imageNum++;
                    // upfail.setText("上传成功：" + imageNum + "\n" + "上传失败：" +
                    // failTime+ "\n" + "解析出错：" + missPacket);
//                    showFingerImage(msg.arg1, (byte[]) msg.obj);
                    break;
                case AsyncFingerprint.SHOW_FINGER_MODEL:
                    AddMembersActivity.this.model = (byte[]) msg.obj;
                    if (AddMembersActivity.this.model != null) {
                        etEnterFinger.setText("registered");
                        Log.i("whw", "#################model.length="
                                + AddMembersActivity.this.model.length);
                    }
                    cancleProgressDialog();
                    // ToastUtil.showToast(FingerprintActivity.this,
                    // "pageId="+msg.arg1+"  store="+msg.arg2);
                    break;
                case AsyncFingerprint.REGISTER_SUCCESS:
                    cancleProgressDialog();
                    if (msg.obj != null) {
                        Integer id = (Integer) msg.obj;
                        ToastUtils.showToast(
                                getString(R.string.register_success) + "  pageId="
                                        + id);
                    } else {
                        etEnterFinger.setText("registered");
                        ToastUtils.showToast(
                                R.string.register_success);
                    }

                    break;
                case AsyncFingerprint.REGISTER_FAIL:
                    cancleProgressDialog();

                    model = null;
                    etEnterFinger.setText("unregistered");
                    ToastUtils.showToast(
                            R.string.register_fail);
                    break;
                case AsyncFingerprint.VALIDATE_RESULT1:
                    cancleProgressDialog();
                    showValidateResult((Boolean) msg.obj);
                    break;
                case AsyncFingerprint.VALIDATE_RESULT2:
                    cancleProgressDialog();
                    Integer r = (Integer) msg.obj;
                    if (r != -1) {
                        ToastUtils.showToast(
                                getString(R.string.verifying_through) + "  pageId="
                                        + r);
                    } else {
                        showValidateResult(false);
                    }
                    break;
                case AsyncFingerprint.UP_IMAGE_RESULT:
                    cancleProgressDialog();
                    ToastUtils
                            .showToast( (Integer) msg.obj);
                    // failTime++;
                    // upfail.setText("上传成功：" + imageNum + "\n" + "上传失败：" +
                    // failTime+ "\n" + "解析出错：" + missPacket);
                    break;
                default:
                    break;
            }
        }

    };

    private void showValidateResult(boolean matchResult) {
        if (matchResult) {
            ToastUtils.showToast(
                    R.string.verifying_through);
        } else {

            ToastUtils.showToast(
                    R.string.verifying_fail);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        asyncFingerprint = new AsyncFingerprint(handlerThread.getLooper(),
                mHandler);
        asyncFingerprint.setFingerprintType(FingerprintAPI.BIG_FINGERPRINT_SIZE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        ButterKnife.bind(this);
        initView();

        AppManager.getAppManager().addActivity(AddMembersActivity.this);
        UUID = TimeUtils.getUUID();
        memberBean = new MemberBean();
        fingerprintBean = new FingerprintBean();
        memberBean.setUUID(UUID);
        fingerprintBean.setUUID(UUID);
        logger.debug("MODEL"+ Build.MODEL);
    }

    private void initView() {




        spChooseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chooseType = getResources().getStringArray(R.array.choose_type_ls)[position];
                tvBasicData.setText(chooseType +" Basic Data");
                if (chooseType.equals("--Choose One--"))  tvBasicData.setText("Member Basic Data");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick({R.id.et_enter_finger, R.id.bt_create_membership_ID, R.id.et_save_basic, R.id.ll_view_basic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_enter_finger:
                asyncFingerprint.setStop(false);
                asyncFingerprint.register();
                break;
            case R.id.bt_create_membership_ID:
//                if (model!=null)
//                    asyncFingerprint.validate(model);
                membershipId = TimeUtils.getMembershipID(DbHelper.getCount());
                etMembershipID.setText(membershipId);
                break;
            case R.id.et_save_basic:
                saveData(view);
                break;
            case R.id.ll_view_basic:
                break;
        }
    }

    private void saveData(final View view) {
        title = spTitle.getSelectedItem().toString();
        gender      = spGender.getSelectedItem().toString();
        marital     = spMarital.getSelectedItem().toString();

        firstName   = etFirstName .getText().toString();
        phone       = etPhone.getText().toString();
        membershipId = etMembershipID .getText().toString();
        lastName    = etLastName.getText().toString();


        if (chooseType.equals("--Choose One--")&&title.equals("--Choose One--")&&marital.equals("--Choose One--")&&gender.equals("--Choose One--")){
            ToastUtils.showToast("please choose One");return;
        }

        if (isNullString(firstName)&&isNullString(phone)&&isNullString(membershipId)&&isNullString(lastName)){
            ToastUtils.showToast("Please fill in the information with *");return;
        }

        if (model == null) {ToastUtils.showToast("Please enter the fingerprint");return;}

        memberBean.setFirstName(firstName);
        memberBean.setGender(gender);
        memberBean.setChooseType(chooseType);
        memberBean.setTitle(title);
        memberBean.setLastName(lastName);
        memberBean.setMarital(marital);
        memberBean.setPhone(phone);
        memberBean.setMembershipId(membershipId);

        fingerprintBean.setModel(model);

        DbHelper.saveInfo(memberBean,fingerprintBean, new OnSuccessCallBack() {
            @Override
            public void success() {

                ToastUtils.showToast("success");
                showSelectCardType(view);

            }



            @Override
            public void error() {
                ToastUtils.showToast("error");

            }
        });
    }

    private PopupWindow mPopupWindow;
    private ListView dialogListView;
    private StringAdapter stringAdapter;
    private List<String> data;
    private void showSelectCardType(View view) {
        View contentView = getPopupWindowContentView();
        dialogListView = (ListView) contentView.findViewById(R.id.listView);
        data = Arrays.asList(getResources().getStringArray(R.array.card_type));
        stringAdapter = new StringAdapter(this, data);
        dialogListView.setAdapter(stringAdapter);
        dialogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();
                switch (position) {

                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(AddMembersActivity.this,WriteNFCActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("MEMBER",memberBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:

                        Intent intent1 = new Intent();
                        intent1.setClass(AddMembersActivity.this,WriteMemberActivity.class);
                        Bundle bundl1e1 = new Bundle();
                        bundl1e1.putSerializable("MEMBER",memberBean);
                        intent1.putExtras(bundl1e1);
                        startActivity(intent1);
                        break;
                    case 2:
                        break;

                }
                finish();
            }
        });
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(view, contentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos[0] -= xOff;
        mPopupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    public View getPopupWindowContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_main_view, null);
        return view;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancleProgressDialog();
        asyncFingerprint.setStop(true);
    }
}
