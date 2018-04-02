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
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.VerifyType;
import com.jinju.android.manager.CommonManager.OnSendVerifyCodeFinishedListener;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 确定修改绑定手机
 */
public class ModifyNewPhoneActivity extends BaseActivity {
	private String mMobile;
	private String mOldCode;
	private String mOldCodeSign;

	private TextView mTxtVoiceCode;
	private Button mBtnBack;
	private EditText mEditCode;
	private TextView mTxtCode;
	private Button mBtnNext;
	
	private Handler mCountdownHandler;
    private CountdownTask mCountdownTask;
    private long mCountdownStartTime;
	
	private Dialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_new_phone);
		
		Intent intent = getIntent();
		mMobile = intent.getStringExtra("mobile");
		mOldCode = intent.getStringExtra("oldCode");
		mOldCodeSign = intent.getStringExtra("oldCodeSign");

		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(mBtnBackOnClickListener);

		TextView txtMobile = (TextView) findViewById(R.id.txt_mobile);
		txtMobile.setText(getString(R.string.input_phonenum_verify_code, mMobile));
		
		mEditCode = (EditText) findViewById(R.id.edit_code);
		mEditCode.addTextChangedListener(mEditCodeWatcher);
		
		mTxtCode = (TextView) findViewById(R.id.txt_code);
		mTxtCode.setOnClickListener(mTxtGetOnClickListener);
		
		mTxtVoiceCode = (TextView) findViewById(R.id.txt_voice_code);
		mTxtVoiceCode.setOnClickListener(mTxtGetOnClickListener);

		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setOnClickListener(mBtnConfirmOnClickListener);
		
		setNextButton(false);
		
		mCountdownHandler = new Handler();
        mCountdownTask = new CountdownTask();
        mProgressDialog = AppUtils.createLoadingDialog(this);
        
		mTxtCode.performClick();
	}

	@Override
	protected void onDestroy() {
		mCountdownHandler.removeCallbacks(mCountdownTask);
		mProgressDialog.dismiss();
		super.onDestroy();
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

	protected View.OnClickListener mBtnBackOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
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

	private OnClickListener mTxtGetOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("verifyType", VerifyType.MOBILE_NEW_PHONE);
			datas.put("mobile", mMobile);
			if (view.getId() == R.id.txt_voice_code) {
				datas.put("sendType", 1);
			}
			mProgressDialog.show();
			DdApplication.getCommonManager().sendVerifyCode(datas, mOnSendVerifyCodeFinishedListener);
		}

	};

	protected OnSendVerifyCodeFinishedListener mOnSendVerifyCodeFinishedListener = new OnSendVerifyCodeFinishedListener() {

        @Override
        public void onSendVerifyCodeFinished(Response response) {
            if (response.isSuccess()) {
                mTxtCode.setEnabled(false);
                mEditCode.requestFocus();
                mCountdownStartTime = System.currentTimeMillis();
                mCountdownHandler.removeCallbacks(mCountdownTask);
                mCountdownHandler.postDelayed(mCountdownTask, 1000);
                AppUtils.showToast(ModifyNewPhoneActivity.this, response.getMessage());
            } else {
                AppUtils.handleErrorResponse(ModifyNewPhoneActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };

	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			String code = mEditCode.getText().toString().trim();
			if (TextUtils.isEmpty(code)) {
				AppUtils.showToast(ModifyNewPhoneActivity.this, R.string.verify_code_empty);
				return;
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("oldCode", mOldCode);
			datas.put("oldCodeSign", mOldCodeSign);
			datas.put("newMobile", mMobile);
			datas.put("authCode", code);

			mProgressDialog.show();
			DdApplication.getUserManager().changeMobile(datas, mOnChangeMobileFinishedListener);
		}

	};
	//确定修改绑定
	private UserManager.OnChangeMobileFinishedListener mOnChangeMobileFinishedListener = new UserManager.OnChangeMobileFinishedListener() {

		@Override
		public void onChangeMobileFinished(Response response) {
			if (response.isSuccess()) {
				//将修改的手机号保存在本地
				DdApplication.getConfigManager().setPhone(mMobile);
				AppUtils.showToast(ModifyNewPhoneActivity.this, response.getMessage());
				setResult(RESULT_OK);
				finish();
			} else {
				AppUtils.handleErrorResponse(ModifyNewPhoneActivity.this, response);
				mProgressDialog.dismiss();
			}
		}

	};
	
	protected void setNextButton(boolean enable) {
        if(enable) {
            mBtnNext.setBackgroundResource(R.drawable.btn_orange_solid_bg);
            mBtnNext.setClickable(true);
        } else {
            mBtnNext.setBackgroundResource(R.mipmap.btn_orange_solid_bg_wait);
            mBtnNext.setClickable(false);
        }
    }

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
    }

}