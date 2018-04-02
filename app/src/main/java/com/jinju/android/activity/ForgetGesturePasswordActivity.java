package com.jinju.android.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.VerifyType;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 忘记手势密码
 * Created by Libra on 2017/6/23.
 */

public class ForgetGesturePasswordActivity extends BaseActivity {

    private String phone;
    private EditText mEditCode;
    private TextView mTxtCode;
    private Button mBtnNext;
    private Dialog mProgressDialog;
    private long mCountdownStartTime;
    private Handler mCountdownHandler;
    private CountdownTask mCountdownTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_gesture_password);
        phone = getIntent().getStringExtra("mobile");
        initView();
    }

    private void initView() {

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);

        TextView mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtTitle.setText("忘记密码");

        TextView mTxtMobile = (TextView)findViewById(R.id.txt_mobile);
        if (!TextUtils.isEmpty(phone)) {
            mTxtMobile.setText(Html.fromHtml(getString(R.string.gesture_input_phonenum_verify_code, phone)));
        }
        mEditCode = (EditText) findViewById(R.id.edit_code);
        mEditCode.addTextChangedListener(mEditCodeWatcher);

        mTxtCode = (TextView) findViewById(R.id.txt_code);
        mTxtCode.setOnClickListener(mTxtGetOnClickListener);

        mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnNext.setOnClickListener(mBtnNextOnClickListener);

        mCountdownHandler = new Handler();
        mCountdownTask = new CountdownTask();
        mProgressDialog = AppUtils.createLoadingDialog(this);

        mTxtCode.performClick();
        setNextButton(false);
    }

    View.OnClickListener mBtnBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private TextWatcher mEditCodeWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(6 == s.toString().trim().length()) {
                setNextButton(true);
            } else {
                setNextButton(false);
            }
        }
    };

    //获取验证码
    private View.OnClickListener mTxtGetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, Object> datas = new HashMap<String, Object>();
            datas.put("verifyType", VerifyType.FORGET_GESTURE_PASSWORD);
            datas.put("mobile", phone);

            mProgressDialog.show();
            DdApplication.getCommonManager().sendVerifyCode(datas, mOnSendVerifyCodeFinishedListener);
        }
    };
    private CommonManager.OnSendVerifyCodeFinishedListener mOnSendVerifyCodeFinishedListener = new CommonManager.OnSendVerifyCodeFinishedListener() {

        @Override
        public void onSendVerifyCodeFinished(Response response) {

            if (response.isSuccess()) {
                mTxtCode.setEnabled(false);
                mEditCode.requestFocus();
                mCountdownStartTime = System.currentTimeMillis();
                mCountdownHandler.removeCallbacks(mCountdownTask);
                mCountdownHandler.postDelayed(mCountdownTask, 1000);
                AppUtils.showToast(ForgetGesturePasswordActivity.this, response.getMessage());
            } else {
                AppUtils.handleErrorResponse(ForgetGesturePasswordActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };

    //下一步
    private View.OnClickListener mBtnNextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String code = mEditCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                AppUtils.showToast(ForgetGesturePasswordActivity.this, R.string.verify_code_empty);
                return;
            }

            Map<String, Object> datas = new HashMap<String, Object>();
            datas.put("checkCode", code);
            datas.put("mobile", phone);

            mProgressDialog.show();
            DdApplication.getUserManager().verifyGestureCode(datas, mOnVerifyGestureCodeFinishedListener);
        }
    };
    private UserManager.OnVerifyGestureCodeFinishedListener mOnVerifyGestureCodeFinishedListener = new UserManager.OnVerifyGestureCodeFinishedListener() {

        @Override
        public void OnVerifyGestureCodeFinished(Response response) {
            if (response.isSuccess()) {

                ForgetGesturePasswordActivity.this.setResult(RESULT_OK);
                finish();
            } else {
                AppUtils.handleErrorResponse(ForgetGesturePasswordActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };
    private void setNextButton(boolean enable) {
        if(enable) {
            mBtnNext.setBackgroundResource(R.drawable.btn_orange_solid_bg);
            mBtnNext.setClickable(true);
        } else {
            mBtnNext.setBackgroundResource(R.mipmap.btn_orange_solid_bg_wait);
            mBtnNext.setClickable(false);
        }
    }
    private class CountdownTask implements Runnable {

        public void run() {
            long currentTime = System.currentTimeMillis();
            long duration = currentTime - mCountdownStartTime;
            if (duration < 60000) {
                mTxtCode.setEnabled(false);
                int leftTime = 60 - (int) (duration / 1000);
                mTxtCode.setTextColor(getResources().getColor(R.color.main_red));
                mTxtCode.setText(getString(R.string.count_down, leftTime));
                mCountdownHandler.postDelayed(this, 1000);
            } else {
                mTxtCode.setTextColor(getResources().getColor(R.color.main_blue));
                mTxtCode.setEnabled(true);
                mTxtCode.setText(R.string.get_verify_code);
            }
        }
    }
}
