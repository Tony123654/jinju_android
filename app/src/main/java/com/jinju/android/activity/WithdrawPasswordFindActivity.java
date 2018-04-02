package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.jinju.android.manager.UserManager.OnValidateFindTransPwdFinishedListener;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawPasswordFindActivity extends BaseActivity {
	public static final int REQUEST_FIND_WITHDRAW = 2;

	private EditText mEditUserName;
	private EditText mEditCard;
	private EditText mEditPhone;
	
	private Button mBtnNext;

	private Dialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_password_find);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.find_withdraw_password);

		mEditUserName = (EditText) findViewById(R.id.edit_user_name);
		mEditUserName.addTextChangedListener(mEditUserNameWatcher);

		mEditCard = (EditText) findViewById(R.id.edit_card);
		mEditCard.addTextChangedListener(mEditCardWatcher);
		
		mEditPhone = (EditText) findViewById(R.id.edit_phone);
		mEditPhone.addTextChangedListener(mEditPhoneWatcher);
		
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
		viewList.add(mEditCard);
		viewList.add(mEditPhone);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == REQUEST_FIND_WITHDRAW) {
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

	private TextWatcher mEditUserNameWatcher = new TextWatcher() {
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
	
	private TextWatcher mEditCardWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			checkNextButton();
		}
		
	};
	
	private TextWatcher mEditPhoneWatcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			checkNextButton();
		}
		
	};

	private OnClickListener mBtnNextOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			String idNumber = mEditCard.getText().toString().trim().toUpperCase();
			String phone = mEditPhone.getText().toString().trim();

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("idCardNumber", idNumber);
			datas.put("mobile", phone);

			mProgressDialog.show();
			DdApplication.getUserManager().validateFindTransPwd(datas, mOnValidateFindTransPwdFinishedListener);
		}

	};

	private OnValidateFindTransPwdFinishedListener mOnValidateFindTransPwdFinishedListener = new OnValidateFindTransPwdFinishedListener() {
		@Override
		public void OnValidateFindTransPwdFinished(Response response) {
			// TODO Auto-generated method stub
			if (response.isSuccess()) {
				String card = mEditCard.getText().toString().trim().toUpperCase();
				String phone = mEditPhone.getText().toString().trim();
				
				Intent intent = new Intent(WithdrawPasswordFindActivity.this, WithdrawPasswordCodeActivity.class);
				intent.putExtra("card", card);
				intent.putExtra("phone", phone);
				startActivityForResult(intent, REQUEST_FIND_WITHDRAW);
			} else {
				AppUtils.handleErrorResponse(WithdrawPasswordFindActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};
	
	private void checkNextButton() {
		String userName = mEditUserName.getText().toString().trim();
		String card = mEditCard.getText().toString().trim();
		String phone = mEditPhone.getText().toString().trim();
		
		if(userName.length() >= 0 && (card.length() <= 25 && card.length() >= 15) && (11 == phone.length())) {
			setNextButton(true);
		} else {
			setNextButton(false);
		}
			
	}
	
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

}