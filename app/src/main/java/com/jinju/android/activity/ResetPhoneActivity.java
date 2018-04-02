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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 修改綁定手机号
 */
public class ResetPhoneActivity extends BaseActivity {
	public static final int REQUEST_RESET = 1;
	
	private TextView mTxtPhone;
	private EditText mEditPhone;
	private Dialog mProgressDialog;
	private Button mBtnNext;
	
	private int mTxtPhoneHeight;
	private String mOldCode;
	private String mOldCodeSign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_phone);
		
		Intent intent = getIntent();
		mOldCode = intent.getStringExtra("oldCode");
		mOldCodeSign = intent.getStringExtra("oldCodeSign");
		
		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		
		mEditPhone = (EditText) findViewById(R.id.edit_phone);
		mEditPhone.addTextChangedListener(mEditPhoneTextWatcher);

		mTxtPhone = (TextView) findViewById(R.id.txt_phone);
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		mTxtPhone.measure(w, h);
		mTxtPhoneHeight = mTxtPhone.getMeasuredHeight();

		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setOnClickListener(mBtnNextOnClickListener);
		
		setNextButton(false);
		
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
		viewList.add(mEditPhone);
		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == REQUEST_RESET) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
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
	
	private TextWatcher mEditPhoneTextWatcher=new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String PhoneNumber = mEditPhone.getText().toString().trim();
			if (TextUtils.isEmpty(PhoneNumber)) {
				mTxtPhone.setVisibility(View.GONE);
				Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f,mTxtPhoneHeight,0f);
				translateAnimation.setDuration(500);
				mBtnNext.startAnimation(translateAnimation);
			} else {
				mTxtPhone.setVisibility(View.VISIBLE);
				mTxtPhone.setText(NumberUtils.formatPhoneNum(PhoneNumber));
			}
			if(11 == s.toString().trim().length()) {
				setNextButton(true);
			} else {
				setNextButton(false);
			}
		}
	};
	
	private OnClickListener mBtnNextOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			String phoneNumber = mEditPhone.getText().toString().trim();

			if (TextUtils.isEmpty(phoneNumber)) {
				AppUtils.showToast(ResetPhoneActivity.this, R.string.phone_empty);
				return;
			}

			if(!NumberUtils.PhoneNumValid(phoneNumber)){
				AppUtils.showToast(ResetPhoneActivity.this, R.string.phone_number_invalid);
				return;
			}
			checkMobileAvailableAndSubmit(phoneNumber);
		}

	};

	private void checkMobileAvailableAndSubmit(String phoneNum){
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("mobile", phoneNum);
		DdApplication.getUserManager().checkMobileAvailable(datas, mOnCheckMobileAvailableFinishedListener);
	}

	private UserManager.OnCheckMobileAvailableFinishedListener mOnCheckMobileAvailableFinishedListener = new UserManager.OnCheckMobileAvailableFinishedListener(){

		@Override
		public void onCheckMobileAvailableFinished(Response response) {
			// TODO Auto-generated method stub
			String phoneNumber = mEditPhone.getText().toString().trim();
			if(response.isSuccess()){
				Intent intent = new Intent(ResetPhoneActivity.this, ModifyNewPhoneActivity.class);
				intent.putExtra("mobile", phoneNumber);
				intent.putExtra("oldCode",mOldCode);
				intent.putExtra("oldCodeSign",mOldCodeSign);
				startActivityForResult(intent, REQUEST_RESET);
			} else {
				AppUtils.showToast(ResetPhoneActivity.this, getString(R.string.mobile_already_register, phoneNumber));
			}
		}

	};

}