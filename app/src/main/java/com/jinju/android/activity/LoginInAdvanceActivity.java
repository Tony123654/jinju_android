package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.PerLogin;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.SrcType;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.NumberUtils;
import com.jinju.android.widget.WithClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginInAdvanceActivity extends BaseActivity{
    public static final int REQUEST_CODE = 1;
    
    private TextView mTxtPhone;
    private WithClearEditText mEditPhone;
    private Dialog mProgressDialog;
    private Button mBtnNext;

    private int mSrcType;
    private long mSrcId;
    private TextView mTxtRegisterRedPacket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in_advance);
        Intent intent = getIntent();

        mSrcId = intent.getLongExtra("srcId", 0);
        mSrcType = intent.getIntExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.login_in_advance_title);

        mTxtRegisterRedPacket = (TextView) findViewById(R.id.txt_register_red_packet);
        mTxtRegisterRedPacket.setText(Html.fromHtml(getString(R.string.login_register_red_packet)));
        mEditPhone = (WithClearEditText) findViewById(R.id.edit_phone);
        mEditPhone.addTextChangedListener(mEditPhoneTextWatcher);

        mTxtPhone = (TextView) findViewById(R.id.txt_phone);
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        mTxtPhone.measure(w, h);

        mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnNext.setOnClickListener(mBtnNextOnClickListener);
        setNextBtn(false);
        
        mProgressDialog = AppUtils.createLoadingDialog(this);
    }
    
    @Override
    protected void onDestroy() {
        mProgressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        List<View> viewList = new ArrayList<View>();
        viewList.add(mEditPhone);
        AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
        return super.dispatchTouchEvent(event);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            setResult(RESULT_OK);
            finish();
        }
    }
    
    private View.OnClickListener mBtnBackOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }

    };


    private View.OnClickListener mBtnNextOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String phoneStr = mEditPhone.getText().toString().trim();
            String phoneNumber = phoneStr.replaceAll(" ", "");

            if (TextUtils.isEmpty(phoneNumber)) {
                AppUtils.showToast(LoginInAdvanceActivity.this, R.string.phone_empty);
                return;
            }

            if(!NumberUtils.PhoneNumValid(phoneNumber)){
                AppUtils.showToast(LoginInAdvanceActivity.this, R.string.phone_number_invalid);
                return;
            }
            mProgressDialog.show();

            checkMobileLoginOrRegister(phoneNumber);
        }

    };

    private void checkMobileLoginOrRegister(String phoneNumber) {

        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("mobile", phoneNumber);
        DdApplication.getUserManager().checkMobileLoginOrRegister(datas, mOnCheckMobileLoginOrRegisterFinishedListener);

    }
    
    private UserManager.OnCheckMobileLoginOrRegisterFinishedListener mOnCheckMobileLoginOrRegisterFinishedListener=new UserManager.OnCheckMobileLoginOrRegisterFinishedListener() {
        @Override
        public void onCheckMobileLoginOrRegisterFinished(Response response, PerLogin perLogin) {
            if (response.isSuccess() && perLogin != null) {
                Intent intent = new Intent();
                /**
                 * reg用来判断账号是否注册过
                 */
                if (TextUtils.equals("reg", perLogin.getType())) {
                    //注册
                    intent.setClass(LoginInAdvanceActivity.this, RegisterSubmitActivity.class);
                } else if (TextUtils.equals("login", perLogin.getType())) {
                    //登录
                    intent.setClass(LoginInAdvanceActivity.this, LoginActivity.class);
                }
                intent.putExtra("mobile", perLogin.getMobile());
                intent.putExtra("showName", perLogin.getShowName());
                intent.putExtra(SrcType.SRC_TYPE, mSrcType);
                intent.putExtra("srcId", mSrcId);
                startActivityForResult(intent, REQUEST_CODE);
            }
            mProgressDialog.dismiss();
        }
    };

    private TextWatcher mEditPhoneTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //手机号码追加空格

            if (s == null || s.length() == 0) return;
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < s.length(); i++) {
                if (i != 3 && i != 8&& s.charAt(i) == ' ') {
                    continue;
                } else {
                    sb.append(s.charAt(i));
                    sb.charAt(sb.length() - 1);
                    if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                        sb.insert(sb.length() - 1, ' ');
                    }
                }
            }
            if (!sb.toString().equals(s.toString())) {
                int index = start + 1;
                if (sb.charAt(start) == ' ') {
                    if (before == 0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }
                mEditPhone.setText(sb.toString());
                mEditPhone.setSelection(index);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            String phoneNumber = mEditPhone.getText().toString();
            //光标总是最后
            mEditPhone.setSelection(phoneNumber.length());

            if (phoneNumber.length()>0) {
                mEditPhone.setClickable(false);
            }else {
                mEditPhone.setClickable(true);
            }
            if(13 == phoneNumber.length()) {
                setNextBtn(true);
            } else {
                setNextBtn(false);
            }

        }
    };


    protected void setNextBtn(boolean enable) {
        if(enable) {
            mBtnNext.setBackgroundResource(R.drawable.btn_red_solid_bg);
            mBtnNext.setTextColor(getResources().getColor(R.color.white));
            mBtnNext.setClickable(true);
        } else {
            mBtnNext.setBackgroundResource(R.drawable.button_disabled);
            mBtnNext.setTextColor(getResources().getColor(R.color.btn_text_disabled));
            mBtnNext.setClickable(false);
        }
    }
}
