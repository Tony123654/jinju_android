package com.jinju.android.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.util.DataUtils;

public class WithdrawSuccessActivity extends BaseFragmentActivity {
	private TextView mTxtWithdrawAmount;
	private String mAmount;
	private Button mBtnConfirm;
	private TextView mTxtPhone;
	
	private TextView mTxtGetMoneyDate;
	
	private String mGetMoneyDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_success);
		
		Intent intent = getIntent();
		mAmount = intent.getStringExtra("amount");
		mGetMoneyDate = intent.getStringExtra("getMoneyDate");

		RelativeLayout mBtnBack = (RelativeLayout) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView mTxtTitle = (TextView) findViewById(R.id.txt_title);
		mTxtTitle.setText(R.string.withdraw_success);

		mTxtGetMoneyDate = (TextView) findViewById(R.id.txt_get_money_date);
		mTxtGetMoneyDate.setText(getString(R.string.get_money_date, mGetMoneyDate));

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);
		
		mTxtPhone = (TextView) findViewById(R.id.txt_phone);
		mTxtPhone.setOnClickListener(mTxtPhoneOnClickListener);

		mTxtWithdrawAmount = (TextView) findViewById(R.id.txt_withdraw_amount);
		mTxtWithdrawAmount.setText(DataUtils.convertTwoDecimal(Double.parseDouble(mAmount)));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setResult(RESULT_OK);
			finish();
		}
		
	};

	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setResult(RESULT_OK);
			finish();
		}
		
	};
	
	private OnClickListener mTxtPhoneOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				Uri uri = Uri.parse("tel://" + getString(R.string.charge_withdraw_phone));
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	};
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();

	}
}
