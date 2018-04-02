package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.api.Setting;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AuthStatus;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.constant.SrcType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.activity.lock.CreateGestureActivity;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.manager.UserManager.OnGetSettingFinishedListener;
import com.jinju.android.manager.UserManager.OnLogoutFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataCleanUtils;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends BaseActivity {
	private static final int REQ_CREATE_PATTERN = 1;

	private RelativeLayout mLayoutModifyPassword;
	private RelativeLayout mLayoutWithdrawPassword;
	private RelativeLayout mLayoutGestureLock;
	private RelativeLayout mLayoutClearCache;


	private TextView mTxtNoSetting;
	private TextView mCacheSize;

	private Dialog mProgressDialog;
	
	private Setting mSetting;
	private ConfigManager mConfigManager;
	private String lockPassword;
	private String mLockMatchingCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.profile_setting);

		mLayoutModifyPassword = (RelativeLayout) findViewById(R.id.layout_modify_password);
		mLayoutModifyPassword.setOnClickListener(mLayoutModifyPasswordOnClickListner);

		mLayoutWithdrawPassword = (RelativeLayout) findViewById(R.id.layout_withdraw_password);
		mLayoutWithdrawPassword.setOnClickListener(mLayoutWithdrawPasswordOnClickListener);

		mLayoutGestureLock = (RelativeLayout) findViewById(R.id.layout_gesture_lock);
		mLayoutGestureLock.setOnClickListener(mLayoutGestureLockOnClickListener);
		mTxtNoSetting = (TextView) findViewById(R.id.txt_no_setting);

		mLayoutClearCache = (RelativeLayout) findViewById(R.id.layout_clear_cache);
		mLayoutClearCache.setOnClickListener(mLayoutClearCacheOnClickListener);
		mCacheSize = (TextView) findViewById(R.id.cache_size);


		TextView mTxtVersion = (TextView) findViewById(R.id.txt_version);
		mTxtVersion.setText("V"+DdApplication.getVersionName());

		Button btnLogout = (Button) findViewById(R.id.btn_logout);
		btnLogout.setOnClickListener(mBtnLogoutOnClickListener);

		mProgressDialog = AppUtils.createLoadingDialog(this);

		try {
			// 获取本地缓存大小
			String size = DataCleanUtils.getTotalCacheSize(SettingActivity.this);
			mCacheSize.setText(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mConfigManager = DdApplication.getConfigManager();

	}

	@Override
	protected void onResume() {
		lockPassword = mConfigManager.getLockPassword();
		//手势锁对应码
		mLockMatchingCode = mConfigManager.getLockMatchingCode();
		getSetting();

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};

	//实名认证
	private OnClickListener mLayoutVerifyNameOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if(mSetting.getAuthStatus() == AuthStatus.AUTHED) {
				return;
			} else if(mSetting.getAuthStatus() == AuthStatus.NO_AUTH) {
				MobclickAgent.onEvent(SettingActivity.this, UmengTouchType.RP106_2);
				Intent intent = new Intent(SettingActivity.this, VerifyBankCardActivity.class);
				startActivity(intent);
			}
		}

	};

	//修改登录密码
	private OnClickListener mLayoutModifyPasswordOnClickListner = new OnClickListener() {

		@Override
		public void onClick(View view) {
			MobclickAgent.onEvent(SettingActivity.this, UmengTouchType.RP106_3);

			Intent intent = new Intent(SettingActivity.this, ModifyPasswordActivity.class);
			startActivity(intent);
		}

	};
	//支付密码
	private OnClickListener mLayoutWithdrawPasswordOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch(mSetting.getMemberStep()) {
				case MemberStep.COMPLETE: {
					MobclickAgent.onEvent(SettingActivity.this, UmengTouchType.RP106_4);

					Intent intent = new Intent(SettingActivity.this, WithdrawPasswordActivity.class);
					startActivity(intent);
					break;
				}
				case MemberStep.VERIFY_BANK_CARD: {
					Intent intent = new Intent(SettingActivity.this, VerifyBankCardActivity.class);
					intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_WITHDRAW);
					startActivity(intent);
					break;
				}
				case MemberStep.SEND_CODE: {
					Intent intent = new Intent(SettingActivity.this, VerifyCodeActivity.class);
					intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_WITHDRAW);
					startActivity(intent);
					break;
				}
				case MemberStep.SET_TRANS_PWD: {
					Intent intent = new Intent(SettingActivity.this, WithdrawPasswordSetActivity.class);
					intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_WITHDRAW);
					startActivity(intent);
					break;
				}
			}
		}

	};

	private OnClickListener mBtnLogoutOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			MobclickAgent.onEvent(SettingActivity.this, UmengTouchType.RP106_7);

			DdApplication.getUserManager().logout(mOnLogoutFinishedListener);
		}
	};
	/**
	 * 手势密码
	 */
	private OnClickListener mLayoutGestureLockOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			MobclickAgent.onEvent(SettingActivity.this, UmengTouchType.RP106_5);

			if (mConfigManager.getPhone().equals(mLockMatchingCode)&&!TextUtils.isEmpty(lockPassword)) {

				//手势密码开关界面
				Intent intent = new Intent(SettingActivity.this, GestureLockActivity.class);
				startActivity(intent);
			} else {
				//设置手势
				Intent intent = new Intent(SettingActivity.this, CreateGestureActivity.class);
				startActivityForResult(intent,REQ_CREATE_PATTERN);
			}
		}

	};
	/**
	 * 清除缓存
	 */
	private OnClickListener mLayoutClearCacheOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			MobclickAgent.onEvent(SettingActivity.this, UmengTouchType.RP106_6);

			final CustomRoundDialog customRoundDialog = new CustomRoundDialog(SettingActivity.this, 2);
			customRoundDialog.setMessageTitle(R.string.setting_clear_cache);
			String txtCache= mCacheSize.getText().toString();
			customRoundDialog.setContent("缓存"+txtCache+",是否删除缓存");
			customRoundDialog.setPositiveButton(R.string.confirm, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mProgressDialog.show();
					DataCleanUtils.clearAllCache(SettingActivity.this);
					AppUtils.showToast(SettingActivity.this,"清除缓存成功");
					mCacheSize.setText("0K");
					mProgressDialog.dismiss();
					customRoundDialog.dismiss();
				}
			});
			customRoundDialog.setNegativeButton(R.string.cancel, new OnClickListener() {
				@Override
				public void onClick(View v) {
					customRoundDialog.dismiss();
				}
			});
			customRoundDialog.show();
		}

	};
	/**
	 * 安全退出
	 */
	private OnLogoutFinishedListener mOnLogoutFinishedListener = new OnLogoutFinishedListener() {

		@Override
		public void onLogoutFinished(Response response) {
			if (response.isSuccess()) {
				//销毁ProfileActivity重新创建
				DdApplication.getInstance().destoryActivity("activity.ProfileActivity");
				AppUtils.showToast(SettingActivity.this, response.getMessage());
				Intent intent = new Intent(SettingActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("activityType", MainActivity.TAB_HOME);
				intent.putExtra("logout",true);
				startActivity(intent);
				finish();
			} else {
				AppUtils.handleErrorResponse(SettingActivity.this, response);
				mProgressDialog.dismiss();
			}
		}

	};

	private void getSetting() {
		mProgressDialog.show();
		
		final Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("iv", DdApplication.getVersionName());
		DdApplication.getUserManager().getSetting(datas, mOnGetSettingFinishedListener);
	}

	private OnGetSettingFinishedListener mOnGetSettingFinishedListener = new OnGetSettingFinishedListener() {

		@Override
		public void onGetSettingFinished(Response response, Setting setting) {
			if (response.isSuccess()) {
				mSetting = setting;
				//1.2版本没有setPhone，在这里将手机号存在本地
				mConfigManager.setPhone(mSetting.getAccount());
				if (mSetting!=null) {
					mTxtNoSetting.setVisibility(mSetting.getAccount().equals(mLockMatchingCode)? View.GONE : View.VISIBLE);
				}

				if(!TextUtils.isEmpty(setting.getNick())){
					mConfigManager.setNickName(setting.getNick());
				}

			} else {
				AppUtils.handleErrorResponse(SettingActivity.this, response);
			}
			mProgressDialog.dismiss();
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

			case REQ_CREATE_PATTERN://创建手势

				if (resultCode == RESULT_OK) {
					//创建手势成功保存手势
					byte[] pattern = data.getByteArrayExtra("pattern");
					if (pattern != null) {
						StringBuffer buffer = new StringBuffer();
						for (byte c : pattern) {
							buffer.append(c);
						}
						//保存锁开启状态
						mConfigManager.setLockStatus(true);
						//将手机号保存在本地
						mConfigManager.setPhone(mSetting.getAccount());
						//保存创建的手势图
						mConfigManager.setLockPassword(buffer.toString());
						AppUtils.showToast(this, "手势密码设置成功");
					}

				}
				break;
		}
	}

}