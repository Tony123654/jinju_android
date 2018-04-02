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

public class ChargeSuccessActivity extends BaseFragmentActivity {
	private TextView mTxtChargeAmount;
	private TextView mTxtPhone;
	private Button mBtnConfirm;
	private String mAmount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charge_success);
		
		Intent intent = getIntent();
		mAmount = intent.getStringExtra("amount");
		RelativeLayout mBtnBack = (RelativeLayout) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView mTxtTitle = (TextView) findViewById(R.id.txt_title);
		mTxtTitle.setText(R.string.charge_success);

		mTxtChargeAmount = (TextView) findViewById(R.id.txt_charge_amount);
		mTxtChargeAmount.setText(DataUtils.convertTwoDecimal(Double.parseDouble(mAmount)));
		
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);
		
		mTxtPhone = (TextView) findViewById(R.id.txt_phone);
		mTxtPhone.setOnClickListener(mTxtPhoneOnClickListener);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			Intent intent = new Intent(ChargeSuccessActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("activityType", MainActivity.TAB_LOAN);
			startActivity(intent);
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

	public OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}

	};
}
