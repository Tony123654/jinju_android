package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.PayResult;
import com.jinju.android.constant.LoanType;

/**
 * 购买成功界面
 */
public class LoanSuccessActivity extends BaseActivity {
	private TextView mTxtName;
	private TextView mTxtAmount;
	private TextView mTxtCoupon;
	private TextView mTxtSubscribeTime;
	private TextView mTxtStartDate;
	
	private View mViewCouponSeparator;
	private LinearLayout mLayoutCoupon;
	
	private Button mBtnConfirm;
	
	private int mLoanType;
	private PayResult mPayResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan_success);
		
		Intent intent = getIntent();
		mLoanType = intent.getIntExtra(LoanType.LOAN_TYPE, LoanType.FINANCIAL);
		mPayResult = (PayResult)intent.getSerializableExtra("result");

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.loan_result);

		mTxtName = (TextView) findViewById(R.id.txt_name);
		mTxtAmount = (TextView) findViewById(R.id.txt_amount);
		mTxtCoupon = (TextView) findViewById(R.id.txt_coupon);
		mTxtSubscribeTime = (TextView) findViewById(R.id.txt_subscribe_time);
		mTxtStartDate = (TextView) findViewById(R.id.txt_start_date);
		
		mViewCouponSeparator = (View) findViewById(R.id.view_coupon_separator);
		mLayoutCoupon = (LinearLayout) findViewById(R.id.layout_coupon);
		
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return super.dispatchTouchEvent(event);
	}

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			setResultAndFinish();
		}

	};
	

	
	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(LoanSuccessActivity.this, MyFinancialActivity.class);
			startActivity(intent);
			setResultAndFinish();
		}
		
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	setResultAndFinish();
        	return true;
         }
         return super.onKeyDown(keyCode, event);
     }
	
	private void setResultAndFinish(){
		setResult(RESULT_OK);
		finish();
	}

}