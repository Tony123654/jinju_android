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
import com.jinju.android.manager.UserManager.OnSetNicknameFinishedListener;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditNicknameActivity extends BaseActivity {
	private EditText mEditName;
	private Dialog mProgressDialog;
	private Button btnSumbit;
	private String nickname = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_nickname);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.setting_nickname);

		btnSumbit = (Button) findViewById(R.id.btn_submit);
		btnSumbit.setOnClickListener(mBtnSumbitOnClickListener);

		mEditName = (EditText) findViewById(R.id.edit_name);
		mEditName.addTextChangedListener(mTextChangedListener);
		
		setSubmitButton(false);
		
		mProgressDialog = AppUtils.createLoadingDialog(this);
	}

	private void setSubmitButton(boolean enable) {
		if(enable) {
			btnSumbit.setBackgroundResource(R.drawable.btn_red_solid_bg);
			btnSumbit.setTextColor(getResources().getColor(R.color.white));
			btnSumbit.setClickable(true);
		} else {
			btnSumbit.setBackgroundResource(R.drawable.button_disabled);
			btnSumbit.setTextColor(getResources().getColor(R.color.btn_text_disabled));
			btnSumbit.setClickable(false);
		}
	}
	
	private TextWatcher mTextChangedListener = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (!TextUtils.isEmpty(s.toString())) {
				setSubmitButton(true);
			} else {
				setSubmitButton(false);
			}
		}
	};

	@Override
	protected void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditName);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};

	private OnClickListener mBtnSumbitOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			nickname = mEditName.getText().toString().trim();
			if (TextUtils.isEmpty(nickname)) {
				AppUtils.showToast(EditNicknameActivity.this, R.string.nickname_empty);
				return;
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("nick", nickname);

			mProgressDialog.show();
			DdApplication.getUserManager().setNickname(datas, mOnSetNicknameFinishedListener);
		}

	};

	private OnSetNicknameFinishedListener mOnSetNicknameFinishedListener = new OnSetNicknameFinishedListener() {

		@Override
		public void onSetNicknameFinished(Response response) {
			if (response.isSuccess()) {
				DdApplication.getConfigManager().setNickName(nickname);
				AppUtils.showToast(EditNicknameActivity.this, response.getMessage());
				finish();
			} else {
				AppUtils.handleErrorResponse(EditNicknameActivity.this, response);
				mProgressDialog.dismiss();
			}
		}

	};

}