package com.jinju.android.activity;

import android.app.Dialog;
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
import com.jinju.android.manager.UserManager.OnChangePasswordFinishedListener;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyPasswordActivity extends BaseActivity {

	private EditText mEditOld;
	private EditText mEditPassword;
	private EditText mEditConfirm;
	private Dialog mProgressDialog;
	private Button mBtnConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_password);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.modify_password);

		mEditOld = (EditText) findViewById(R.id.edit_old);
		mEditOld.addTextChangedListener(mTextChangedListener);
		
		mEditPassword = (EditText) findViewById(R.id.edit_password);
		mEditPassword.addTextChangedListener(mTextChangedListener);
		
		mEditConfirm = (EditText) findViewById(R.id.edit_confirm);
		mEditConfirm.addTextChangedListener(mTextChangedListener);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);
		
		setNextButton(false);
		mProgressDialog = AppUtils.createLoadingDialog(this);
	}
	
	private void setNextButton(boolean enable) {
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
	
	private TextWatcher mTextChangedListener=new TextWatcher() {
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
	
	private void checkNextButton() {
		String old = mEditOld.getText().toString().trim();
		String ps = mEditPassword.getText().toString().trim();
		String confirm = mEditConfirm.getText().toString().trim();

		if(old.length() >= 6 && ps.length() >= 6 && confirm.length()>=6) {
			setNextButton(true);
		} else {
			setNextButton(false);
		}

	}
	
	@Override
	protected void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditOld);
		viewList.add(mEditPassword);
		viewList.add(mEditConfirm);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);

	}

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};

	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			String old = mEditOld.getText().toString().trim();
			if (TextUtils.isEmpty(old)) {
				AppUtils.showToast(ModifyPasswordActivity.this, R.string.password_empty);
				return;
			}

			String password = mEditPassword.getText().toString().trim();
			if (TextUtils.isEmpty(password)) {
				AppUtils.showToast(ModifyPasswordActivity.this, R.string.password_empty);
				return;
			}

			String confirm = mEditConfirm.getText().toString().trim();
			if (!TextUtils.equals(password, confirm)) {
				AppUtils.showToast(ModifyPasswordActivity.this, R.string.password_no_match);
				return;
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("oldPwd", old);
			datas.put("newPwd", password);
			datas.put("newPwdConfirm", password);

			mProgressDialog.show();
			DdApplication.getUserManager().changePassword(datas, mOnChangePasswordFinishedListener);
		}

	};

	private OnChangePasswordFinishedListener mOnChangePasswordFinishedListener = new OnChangePasswordFinishedListener() {

		@Override
		public void onChangePasswordFinished(Response response) {
			if (response.isSuccess()) {
				AppUtils.showToast(ModifyPasswordActivity.this, response.getMessage());
				finish();
			} else {
				AppUtils.handleErrorResponse(ModifyPasswordActivity.this, response);
				mProgressDialog.dismiss();
			}
		}

	};

}