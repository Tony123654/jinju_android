package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.constant.UmengTouchType;
import com.umeng.analytics.MobclickAgent;


public class WithdrawPasswordActivity extends BaseActivity{

	private RelativeLayout mLayoutModifyWithdraw;
	private RelativeLayout mLayoutFindWithdraw;
	
	public static final int REQUEST_MODIFY_WITHDRAW = 1;
	public static final int REQUEST_FIND_WITHDRAW = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_password);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.withdraw_password);

		mLayoutModifyWithdraw = (RelativeLayout) findViewById(R.id.layout_modify_withdraw);
		mLayoutModifyWithdraw.setOnClickListener(mLayoutModifyWithdrawOnClickListener);
		
		mLayoutFindWithdraw = (RelativeLayout) findViewById(R.id.layout_find_withdraw);
		mLayoutFindWithdraw.setOnClickListener(mLayoutFindWithdrawOnClickListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == REQUEST_MODIFY_WITHDRAW) {
			finish();
		}
		
		if (resultCode == RESULT_OK && requestCode == REQUEST_FIND_WITHDRAW) {
			finish();
		}
	}
	
	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
		
	};

	private OnClickListener mLayoutModifyWithdrawOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			MobclickAgent.onEvent(WithdrawPasswordActivity.this, UmengTouchType.RP109_1);

			Intent intent = new Intent(WithdrawPasswordActivity.this, WithdrawPasswordModifyActivity.class);
			startActivityForResult(intent, REQUEST_MODIFY_WITHDRAW);
		}
		
	};
	
	private OnClickListener mLayoutFindWithdrawOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MobclickAgent.onEvent(WithdrawPasswordActivity.this, UmengTouchType.RP109_2);

			Intent intent = new Intent(WithdrawPasswordActivity.this, WithdrawPasswordFindActivity.class);
			startActivityForResult(intent, REQUEST_FIND_WITHDRAW);
		}
		
	};

}