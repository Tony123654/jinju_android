package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.jinju.android.constant.SrcType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.manager.UserManager.OnLoginFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.LoginUtils;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {

	private TextView mTxtLoginWelcome;
	private EditText mEditPassword;
	private TextView mTxtLoginForget;
	private Button mBtnConfirm;
	
	private String mWxUnionId;
	private String mMobile;
	private String mShowName;
	private int mSrcType;
	private long mSrcId;
	
	private Dialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Intent intent = getIntent();
		mWxUnionId = intent.getStringExtra("unionid");
		mSrcId = intent.getLongExtra("srcId", 0);
		mSrcType = intent.getIntExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);
		mMobile = intent.getStringExtra("mobile");
		mShowName = intent.getStringExtra("showName");


		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText("登录");

		mTxtLoginWelcome = (TextView) findViewById(R.id.txt_login_welcome);
		mTxtLoginWelcome.setText(getString(R.string.login_welcome, mShowName));
		
		mEditPassword = (EditText) findViewById(R.id.edit_password);
		mEditPassword.addTextChangedListener(mEditPasswordTextWatcher);
		
		mTxtLoginForget = (TextView) findViewById(R.id.txt_login_forget);
		mTxtLoginForget.setOnClickListener(mBtnForgetOnClickListener);
		
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(mBtnLoginOnClickListener);
		setConfirmBtn(false);

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
		viewList.add(mEditPassword);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}

	private OnClickListener mBtnLoginOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			String password = mEditPassword.getText().toString().trim();
			if (TextUtils.isEmpty(password)) {
				AppUtils.showToast(LoginActivity.this, R.string.password_empty);
				return;
			}
			//获取小米推送的RegId，登陆后传给服务端
			String regId = MiPushClient.getRegId(LoginActivity.this);
			if (regId == null) {
				regId = "";
			} else {
				//由于服务端url无法转换加号，所以进行转换
				regId = regId.replaceAll("\\+","");
			}
			Map<String, Object> datas = new HashMap<String, Object>();
			if(!TextUtils.isEmpty(mWxUnionId)) {
				datas.put("unionId", mWxUnionId);
			}
			datas.put("account", mMobile);
			datas.put("password", password);
			datas.put("errorNum", "0");
			datas.put("checkCode", "");
			datas.put("osType", "android");
			datas.put("regId", regId);

			mProgressDialog.show();
			DdApplication.getUserManager().login(datas, mOnLoginFinishedListener);
		}

	};
	//登录
	private OnLoginFinishedListener mOnLoginFinishedListener = new OnLoginFinishedListener() {
		@Override
		public void onLoginFinished(Response response, int memberStep) {
			if (response.isSuccess()) {
				MobclickAgent.onEvent(LoginActivity.this, UmengTouchType.RP108_1);

				//获取本地手势账号对应码，
				ConfigManager mConfigManager= DdApplication.getConfigManager();
				String mLockMatchingCode = mConfigManager.getLockMatchingCode();

				//登录成功后如果有手势锁，新的账号是否与手势锁对应，对应手势锁开启，不对应关闭手势锁
				if (mLockMatchingCode.equals(mMobile)) {
					mConfigManager.setLockStatus(true);
				} else {
					mConfigManager.setLockStatus(false);
				}
				//登陆成功后将新的手机号码保存在本地
				DdApplication.getConfigManager().setPhone(mMobile);
				LoginUtils.loginSuccess(LoginActivity.this, response, mSrcType, mSrcId, memberStep);
			} else {
				AppUtils.handleErrorResponse(LoginActivity.this, response);
				mProgressDialog.dismiss();
			}
		}

	};

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};

	private OnClickListener mBtnForgetOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
			intent.putExtra("mobile", mMobile);
			startActivity(intent);
		}

	};

	private TextWatcher mEditPasswordTextWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (!TextUtils.isEmpty(s.toString()) && s.toString().length() >= 6) {
				setConfirmBtn(true);
			} else {
				setConfirmBtn(false);
			}
		}
	};

	protected void setConfirmBtn(boolean enable) {
		if(enable) {
			mBtnConfirm.setBackgroundResource(R.drawable.btn_red_solid_bg);
			mBtnConfirm.setTextColor(getResources().getColor(R.color.white));
			mBtnConfirm.setClickable(true);
		} else {
			mBtnConfirm.setBackgroundResource(R.drawable.button_disabled);
			mBtnConfirm.setTextColor(getResources().getColor(R.color.btn_text_disabled));
			mBtnConfirm.setClickable(false);
		}
	}

}