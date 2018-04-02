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
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.constant.SrcType;
import com.jinju.android.manager.TradeManager.OnConfirmBankCardFinishedListener;
import com.jinju.android.manager.TradeManager.OnSendCodeFinishedListener;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行卡短信验证
 */
public class VerifyCodeActivity extends BaseActivity {
	private ImageView mImgFinish;
	private TextView mTxtNotice;
	
	private String mCardNo;
	private String mTel;
	private long mSrcId;
	private int mSrcType;
	private String mId = "";
	
	protected Handler mCountdownHandler;
	protected CountdownTask mCountdownTask;
	protected long mCountdownStartTime;
	protected Dialog mProgressDialog;
	
	protected EditText mEditCode;
	protected TextView mTxtCode;
	protected Button mBtnNext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_code);

		Intent intent = getIntent();
		mSrcId = intent.getLongExtra("srcId", 0);
		mSrcType = intent.getIntExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);
		mCardNo = intent.getStringExtra("cardNo");
		mTel = intent.getStringExtra("tel");
		mId = intent.getStringExtra("memberBankId");
		
		mImgFinish = (ImageView) findViewById(R.id.img_finish);
		mImgFinish.setOnClickListener(mImgFinishOnClickListener);
		
		mTxtNotice = (TextView) findViewById(R.id.txt_notice);
		//输入的验证码
		mEditCode = (EditText) findViewById(R.id.edit_code);
		mEditCode.addTextChangedListener(mEditCodeWatcher);
		
		mTxtCode = (TextView) findViewById(R.id.txt_code);
		mTxtCode.setOnClickListener(mTxtGetVerifyCodeOnClickListener);
		
		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setOnClickListener(mBtnNextOnClickListener);
		
		mCountdownHandler = new Handler();
		mCountdownTask = new CountdownTask();
		mProgressDialog = AppUtils.createLoadingDialog(this);

		if(!TextUtils.isEmpty(mTel)) {
			mTxtNotice.setText(getString(R.string.input_code_notify, mTel));
			StartCountdown();
		} else {
			sendCode();
		}
		

		setNextButton(false);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mCountdownHandler.removeCallbacks(mCountdownTask);
		mProgressDialog.dismiss();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditCode);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}

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
	
	private void setNextButton(boolean enable) {
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
	
	private OnClickListener mImgFinishOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
		
	};

	private void sendCode() {
		Map<String, Object> datas = new HashMap<String, Object>();
		mProgressDialog.show();
		DdApplication.getTradeManager().sendCode(datas, mOnSendCodeFinishedListener);
	}
	
	private OnClickListener mTxtGetVerifyCodeOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sendCode();
		}
		
	};
	
	private OnSendCodeFinishedListener mOnSendCodeFinishedListener = new OnSendCodeFinishedListener() {

		@Override
		public void onSendCodeFinished(Response response, String memberBankId, String bankCardNo, String tel) {
			// TODO Auto-generated method stub
			if (response.isSuccess()) {

				mId = memberBankId;
				StartCountdown();
				AppUtils.showToast(VerifyCodeActivity.this, response.getMessage());
			} else {
				AppUtils.handleErrorResponse(VerifyCodeActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};

	private void StartCountdown() {
		mTxtCode.setEnabled(false);
		mEditCode.requestFocus();
		mCountdownStartTime = System.currentTimeMillis();
		mCountdownHandler.removeCallbacks(mCountdownTask);
		mCountdownHandler.postDelayed(mCountdownTask, 1000);
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
	
	private OnClickListener mBtnNextOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String verifyCode = mEditCode.getText().toString();

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("memberBankId", mId);
			datas.put("validateCode", verifyCode);

			mProgressDialog.show();
			DdApplication.getTradeManager().confirmBankCard(datas, mOnConfirmBankCardFinishedListener);
		}
		
	};
	
	private OnConfirmBankCardFinishedListener mOnConfirmBankCardFinishedListener = new OnConfirmBankCardFinishedListener() {

		@Override
		public void onConfirmBankCardFinished(Response response, int memberStep) {
			// TODO Auto-generated method stub
			mProgressDialog.dismiss();
			
			if (response.isSuccess()) {
				if(memberStep == MemberStep.COMPLETE) {
					switch(mSrcType) {
					case SrcType.SRC_NORMAL:
						finish();
						break;
					case SrcType.SRC_FINANCIAL:{
						Intent intent = new Intent(VerifyCodeActivity.this, FinancialConfirmActivity.class);
						intent.putExtra("srcId", mSrcId);
						startActivity(intent);
						finish();
						break;
						}
					case SrcType.SRC_WITHDRAW:{
						Intent intent = new Intent(VerifyCodeActivity.this, WithdrawPasswordActivity.class);
						startActivity(intent);
						finish();
						break;
						}
					}
				} else {
					Intent intent = new Intent(VerifyCodeActivity.this, WithdrawPasswordSetActivity.class);
					intent.putExtra(SrcType.SRC_TYPE, mSrcType);
					intent.putExtra("srcId", mSrcId);
					startActivity(intent);
					finish();
				}
			} else {
				AppUtils.handleErrorResponse(VerifyCodeActivity.this, response);
			}
		}
		
	};

	
} 