package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPasswordActivity extends BaseActivity {
	private TextView mTxtCode;
	private EditText mEditCode;
	
	private TextView mTxtFindPasswordNotify;
	private EditText mEditResetPassword;
	private Button mBtnSubmit;
	
    protected Handler mCountdownHandler;
    protected CountdownTask mCountdownTask;
    protected long mCountdownStartTime;
    protected Dialog mProgressDialog;
	
	private String mMobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		Intent intent = getIntent();
		mMobile = intent.getStringExtra("mobile");

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.find_password);

		mEditCode = (EditText) findViewById(R.id.edit_code);
		mEditCode.addTextChangedListener(mTextChangedListener);
		
		mTxtCode = (TextView) findViewById(R.id.txt_code);
		mTxtCode.setOnClickListener(mBtnGetOnClickListener);
		
		mEditResetPassword = (EditText) findViewById(R.id.edit_reset_password);
		mEditResetPassword.addTextChangedListener(mTextChangedListener);
		
		mTxtFindPasswordNotify = (TextView) findViewById(R.id.txt_find_password_notify);
		mTxtFindPasswordNotify.setText(getString(R.string.input_phonenum_code_notify, mMobile));

		
		mBtnSubmit = (Button) findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(mBtnConfirmOnClickListener);
		setSubmitButton(false);
		
		mCountdownHandler = new Handler();
        mCountdownTask = new CountdownTask();
        mProgressDialog = AppUtils.createLoadingDialog(this);
        
        mTxtCode.performClick();
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountdownHandler.removeCallbacks(mCountdownTask);
        mProgressDialog.dismiss();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditResetPassword);
		viewList.add(mEditCode);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}

    protected View.OnClickListener mBtnBackOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }

    };
    
	private TextWatcher mTextChangedListener = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			checkNextButton();
		}
	};
	
	protected void setSubmitButton(boolean enable) {
		if(enable) {
			mBtnSubmit.setBackgroundResource(R.drawable.btn_red_solid_bg);
			mBtnSubmit.setTextColor(getResources().getColor(R.color.white));
			mBtnSubmit.setClickable(true);
		} else {
			mBtnSubmit.setBackgroundResource(R.drawable.button_disabled);
			mBtnSubmit.setTextColor(getResources().getColor(R.color.btn_text_disabled));
			mBtnSubmit.setClickable(false);
		}
	}

	private void checkNextButton() {
		String ps = mEditResetPassword.getText().toString().trim();
		String code = mEditCode.getText().toString().trim();

		if(ps.length() >= 6 && code.length() == 6) {
			setSubmitButton(true);
		} else {
			setSubmitButton(false);
		}
	}

	
	private OnClickListener mBtnGetOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("verifyType", VerifyType.FIND_PASSWORD);
			datas.put("mobile", mMobile);

			mProgressDialog.show();
			DdApplication.getCommonManager().sendVerifyCode(datas, mOnSendVerifyCodeFinishedListener);
		}

	};
	
    protected CommonManager.OnSendVerifyCodeFinishedListener mOnSendVerifyCodeFinishedListener = new CommonManager.OnSendVerifyCodeFinishedListener() {

        @Override
        public void onSendVerifyCodeFinished(Response response) {
            if (response.isSuccess()) {
                mTxtCode.setEnabled(false);
                mEditCode.requestFocus();
                mCountdownStartTime = System.currentTimeMillis();
                mCountdownHandler.removeCallbacks(mCountdownTask);
                mCountdownHandler.postDelayed(mCountdownTask, 1000);
                AppUtils.showToast(FindPasswordActivity.this, response.getMessage());
            } else {
                AppUtils.handleErrorResponse(FindPasswordActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };

	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			String code = mEditCode.getText().toString().trim();
			if (TextUtils.isEmpty(code)) {
				AppUtils.showToast(FindPasswordActivity.this, R.string.verify_code_empty);
				return;
			}
			String resetPassword = mEditResetPassword.getText().toString().trim();
			//  isEmpty(resetPassword)
			if (TextUtils.isEmpty(code)) {
				AppUtils.showToast(FindPasswordActivity.this, R.string.password_empty);
				return;
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("account", mMobile);
			datas.put("checkCode", code);
			datas.put("newPwd", resetPassword);

			mProgressDialog.show();
			DdApplication.getUserManager().resetPassword(datas, mOnResetPasswordFinishedListener);

		}

	};

	private UserManager.OnResetPasswordFinishedListener mOnResetPasswordFinishedListener = new UserManager.OnResetPasswordFinishedListener() {

		@Override
		public void onResetPasswordFinished(Response response) {
			if (response.isSuccess()) {
				AppUtils.showToast(FindPasswordActivity.this, response.getMessage());
				setResult(RESULT_OK);
				finish();
			} else {
				AppUtils.handleErrorResponse(FindPasswordActivity.this, response);
				mProgressDialog.dismiss();
			}
		}

	};
	
    protected class CountdownTask implements Runnable {

        public void run() {
            long currentTime = System.currentTimeMillis();
            long duration = currentTime - mCountdownStartTime;
            if (duration < 60000) {
                mTxtCode.setEnabled(false);
                int leftTime = 60 - (int) (duration / 1000);
                mTxtCode.setTextColor(getResources().getColor(R.color.gray));
                mTxtCode.setText(getString(R.string.count_down, leftTime));
                mCountdownHandler.postDelayed(this, 1000);
            } else {
                mTxtCode.setTextColor(getResources().getColor(R.color.main_blue));
                mTxtCode.setEnabled(true);
                mTxtCode.setText(R.string.get_verify_code);
            }
        }
    };

}