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
import com.jinju.android.activity.lock.CreateGestureActivity;
import com.jinju.android.api.Function;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.constant.SrcType;
import com.jinju.android.constant.VerifyType;
import com.jinju.android.manager.CommonManager.OnSendVerifyCodeFinishedListener;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.manager.UserManager;
import com.jinju.android.manager.UserManager.OnRegisterFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.LoginUtils;
import com.jinju.android.util.Utils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterSubmitActivity extends BaseActivity {
	private static final int REQ_CREATE_PATTERN = 0;
	private String mPhoneNumber;
	private TextView mTxtRegisterNotify;

	private EditText mEditPassword;
	private TextView mTxtProtocol;

	private Button mBtnSubmit;
	
	private Handler mCountdownHandler;
	private CountdownTask mCountdownTask;
	private long mCountdownStartTime;
	private Dialog mProgressDialog;
	private EditText mEditCode;
	private TextView mTxtCode;
    
	private String mWxUnionId;
	private long mSrcId;
	private int mSrcType;
	private Response mResponse;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_submit);

		Intent intent = getIntent();
		mWxUnionId = intent.getStringExtra("unionid");
		mPhoneNumber = intent.getStringExtra("mobile");
		mSrcId = intent.getLongExtra("srcId", 0);
		mSrcType = intent.getIntExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.register_title);

		mTxtProtocol = (TextView) findViewById(R.id.txt_protocol);
		mTxtProtocol.setOnClickListener(mBtnProtocolOnClickListener);
		mTxtRegisterNotify = (TextView) findViewById(R.id.txt_register_notify);

		mEditCode = (EditText) findViewById(R.id.edit_code);
		mEditCode.addTextChangedListener(mTextChangedListener);

		mTxtCode = (TextView) findViewById(R.id.txt_code);
		mTxtCode.setOnClickListener(mTxtGetOnClickListener);

		mEditPassword = (EditText) findViewById(R.id.edit_password);
		mEditPassword.addTextChangedListener(mTextChangedListener);

		mBtnSubmit = (Button) findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(mBtnSubmitOnClickListener);
		setSubmitButton(false);

		setRegisterNotify();
		
		mCountdownHandler = new Handler();
        mCountdownTask = new CountdownTask();
        mProgressDialog = AppUtils.createLoadingDialog(this);
		
		mTxtCode.performClick();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == REQ_CREATE_PATTERN) {
			//创建手势成功保存手势
			byte[] pattern = data.getByteArrayExtra("pattern");
			if (pattern != null) {
				StringBuffer buffer = new StringBuffer();
				for (byte c : pattern) {
					buffer.append(c);
				}
				ConfigManager mConfigManager = DdApplication.getConfigManager();
				//保存锁开启状态
				mConfigManager.setLockStatus(true);
				//保存创建的手势图
				mConfigManager.setLockPassword(buffer.toString());
				AppUtils.showToast(this, "手势密码设置成功");
			}
		}
		LoginUtils.loginSuccess(RegisterSubmitActivity.this, mResponse, SrcType.SRC_REGISTER, mSrcId, MemberStep.VERIFY_BANK_CARD);
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
		viewList.add(mEditPassword);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}

    protected OnClickListener mBtnBackOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }

    };

	private OnClickListener mBtnProtocolOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(RegisterSubmitActivity.this, BaseJsBridgeWebViewActivity.class);
			intent.putExtra("url", AppConstant.AGREEMENT_WEB_URL);
			startActivity(intent);
		}

	};
	
	private OnClickListener mTxtGetOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (TextUtils.isEmpty(mPhoneNumber)) {
				AppUtils.showToast(RegisterSubmitActivity.this, R.string.phone_empty);
				return;
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("verifyType", VerifyType.REGISTER);
			datas.put("mobile", mPhoneNumber);

			mProgressDialog.show();
			DdApplication.getCommonManager().sendVerifyCode(datas, mOnSendVerifyCodeFinishedListener);
		}

	};

	private OnSendVerifyCodeFinishedListener mOnSendVerifyCodeFinishedListener = new OnSendVerifyCodeFinishedListener() {

        @Override
        public void onSendVerifyCodeFinished(Response response) {
            if (response.isSuccess()) {
                mTxtCode.setEnabled(false);
                mEditCode.requestFocus();
                mCountdownStartTime = System.currentTimeMillis();
                mCountdownHandler.removeCallbacks(mCountdownTask);
                mCountdownHandler.postDelayed(mCountdownTask, 1000);
                AppUtils.showToast(RegisterSubmitActivity.this, response.getMessage());
            } else {
                AppUtils.handleErrorResponse(RegisterSubmitActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };

	private void setRegisterNotify(){
		mTxtRegisterNotify.setText(this.getString(R.string.input_phonenum_code_notify, mPhoneNumber));
	}

	private OnClickListener mBtnSubmitOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			if (Utils.isFastClick()) {//多次点击限制

				String password = mEditPassword.getText().toString().trim();
				String authCode = mEditCode.getText().toString().trim();
				if (TextUtils.isEmpty(authCode)){
					AppUtils.showToast(RegisterSubmitActivity.this, R.string.verify_code_empty);
					return;
				}

				if (TextUtils.isEmpty(password)) {
					AppUtils.showToast(RegisterSubmitActivity.this, R.string.password_empty);
					return;
				}

				Map<String, Object> datas = new HashMap<String, Object>();
				//获取渠道信息
				String channelName = VersionUtils.getChannelName();
				if(!TextUtils.isEmpty(mWxUnionId)) {
					datas.put("unionId", mWxUnionId);
				}
				datas.put("account", mPhoneNumber);
				datas.put("regVerifyCode", authCode);
				datas.put("pwd", password);
				datas.put("osType", AppConstant.OS_TYPE);
				datas.put("channel", channelName);
				mProgressDialog.show();
				DdApplication.getUserManager().register(datas, mOnRegisterFinishedListener);

			}
		}

	};


	private UserManager.OnRegisterFinishedListener mOnRegisterFinishedListener = new OnRegisterFinishedListener() {
		@Override
		public void onRegisterFinished(Response response, List<Function> functionList) {
			if (response.isSuccess()) {
				//注册成功后将手机号保存在本地
				DdApplication.getConfigManager().setPhone(mPhoneNumber);
				//不管是否有手势锁，状态都是关闭
				DdApplication.getConfigManager().setLockStatus(false);
				DdApplication.getConfigManager().setFirstRegister(true);
				//设置手势
				mResponse = response;
				createGestureLock();
			} else {
				AppUtils.handleErrorResponse(RegisterSubmitActivity.this, response);
				mProgressDialog.dismiss();
			}
		}
	}; 

	
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
		String ps = mEditPassword.getText().toString().trim();
		String code = mEditCode.getText().toString().trim();

		if(ps.length() >= 6 && code.length() == 6) {
			setSubmitButton(true);
		} else {
			setSubmitButton(false);
		}
	}
	public void createGestureLock() {
		//设置手势
		Intent intent = new Intent(RegisterSubmitActivity.this, CreateGestureActivity.class);
		startActivityForResult(intent,REQ_CREATE_PATTERN);
	}
}