package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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
import com.jinju.android.manager.CommonManager.OnSendVerifyCodeLoginedFinishedListener;
import com.jinju.android.manager.UserManager.OnForgetWithdrawPasswordFinishedListener;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawPasswordCodeActivity extends BaseActivity {
	private static final int REQUEST_CODE = 1;
	
	private String mCard;
	private String mPhone;
	private TextView mTxtMobile;
	
    private Handler mCountdownHandler;
    private CountdownTask mCountdownTask;
    private long mCountdownStartTime;
    private Dialog mProgressDialog;
    private EditText mEditCode;
    private TextView mTxtCode;
    private Button mBtnNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_password_code);

		Intent intent = getIntent();
		mCard = intent.getStringExtra("card");
		mPhone = intent.getStringExtra("phone");

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.find_withdraw_password);

		mTxtMobile = (TextView) findViewById(R.id.txt_mobile);
		mTxtMobile.setText(getString(R.string.input_phonenum_code_notify, mPhone));
		
		mEditCode = (EditText) findViewById(R.id.edit_code);
		mEditCode.addTextChangedListener(mEditCodeWatcher);
		
		mTxtCode = (TextView) findViewById(R.id.txt_code);
		mTxtCode.setOnClickListener(mOnSendVerifyCodeLoginedOnClickListener);
		
		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setOnClickListener(mBtnNextOnClickListener);
		
		setNextButton(false);
		
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
		viewList.add(mEditCode);

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
	
    protected OnClickListener mBtnBackOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }

    };
    
    protected TextWatcher mEditCodeWatcher = new TextWatcher() {

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

	private OnClickListener mOnSendVerifyCodeLoginedOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("verifyType", VerifyType.FIND_WITHDRAW_PASSWORD);

			mProgressDialog.show();
			DdApplication.getCommonManager().sendVerifyCodeLogined(datas, mOnSendVerifyCodeLoginedFinishedListener);
		}

	};
	
	protected OnSendVerifyCodeLoginedFinishedListener mOnSendVerifyCodeLoginedFinishedListener = new OnSendVerifyCodeLoginedFinishedListener() {

        @Override
        public void onSendVerifyCodeLoginedFinished(Response response) {
            if (response.isSuccess()) {
                mTxtCode.setEnabled(false);
                mEditCode.requestFocus();
                mCountdownStartTime = System.currentTimeMillis();
                mCountdownHandler.removeCallbacks(mCountdownTask);
                mCountdownHandler.postDelayed(mCountdownTask, 1000);
                AppUtils.showToast(WithdrawPasswordCodeActivity.this, response.getMessage());
            } else {
                AppUtils.handleErrorResponse(WithdrawPasswordCodeActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };

	private OnClickListener mBtnNextOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			String code = mEditCode.getText().toString().trim();

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("idCard", mCard);
			datas.put("authCode", code);
			datas.put("iv", 1);

			mProgressDialog.show();
			DdApplication.getUserManager().forgetWithdrawPassword(datas, mOnForgetWithdrawPasswordFinishedListener);
		}

	};

	private OnForgetWithdrawPasswordFinishedListener mOnForgetWithdrawPasswordFinishedListener = new OnForgetWithdrawPasswordFinishedListener() {

		@Override
		public void onForgetWithdrawPasswordFinished(Response response, String idCard) {
			if (response.isSuccess()) {
				Intent intent = new Intent(WithdrawPasswordCodeActivity.this, WithdrawPasswordNewActivity.class);
				intent.putExtra("idCard", idCard);
				intent.putExtra("type", "find");
				startActivityForResult(intent, REQUEST_CODE);
			} else {
				AppUtils.handleErrorResponse(WithdrawPasswordCodeActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};
	
	protected void setNextButton(boolean enable) {
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

	private class CountdownTask implements Runnable {

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