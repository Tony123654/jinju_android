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
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.VerifyType;
import com.jinju.android.manager.CommonManager.OnSendVerifyCodeLoginedFinishedListener;
import com.jinju.android.manager.UserManager.OnChangeVaildateFinishedListener;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改绑定手机
 */
public class ModifyPhoneActivity extends BaseActivity {
	private static final int REQUEST_CODE = 1;
	
	private Handler mCountdownHandler;
	private CountdownTask mCountdownTask;
	private long mCountdownStartTime;
	private EditText mEditCode;
	private TextView mTxtCode;
	private Button mBtnNext;
	
	private Dialog mProgressDialog;

	private String mMobile;

	private TextView mTxtVoiceCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_phone);

		Intent intent = getIntent();
		mMobile = intent.getStringExtra("mobile");

		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);

		TextView txtMobile = (TextView) findViewById(R.id.txt_mobile);
		txtMobile.setText(getString(R.string.input_phonenum_verify_code, mMobile));
		
		mEditCode = (EditText) findViewById(R.id.edit_code);
		mEditCode.addTextChangedListener(mEditCodeWatcher);
		
		mTxtCode = (TextView) findViewById(R.id.txt_code);
		mTxtCode.setOnClickListener(mTxtGetOnClickListener);
		
		mTxtVoiceCode = (TextView) findViewById(R.id.txt_voice_code);
		mTxtVoiceCode.setOnClickListener(mTxtGetOnClickListener);

		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setOnClickListener(mBtnNextOnClickListener);
		
		setNextButton(false);
		
		mCountdownHandler = new Handler();
        mCountdownTask = new CountdownTask();
        mProgressDialog = AppUtils.createLoadingDialog(this);
		mTxtCode.performClick();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountdownHandler.removeCallbacks(mCountdownTask);
        mProgressDialog.dismiss();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditCode);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
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
    
    private TextWatcher mEditCodeWatcher = new TextWatcher() {

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
            mBtnNext.setBackgroundResource(R.drawable.btn_orange_solid_bg);
            mBtnNext.setClickable(true);
        } else {
            mBtnNext.setBackgroundResource(R.mipmap.btn_orange_solid_bg_wait);
            mBtnNext.setClickable(false);
        }
    }

	private OnClickListener mTxtGetOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("verifyType", VerifyType.MOBILE_OLE_PHONE);
			if (view.getId() == R.id.txt_voice_code) {
				datas.put("sendType", 1);
			}
			mProgressDialog.show();
			DdApplication.getCommonManager().sendVerifyCodeLogined(datas, mOnSendVerifyCodeLoginedFinishedListener);
		}

	};
	
	private OnSendVerifyCodeLoginedFinishedListener mOnSendVerifyCodeLoginedFinishedListener = new OnSendVerifyCodeLoginedFinishedListener() {

		@Override
		public void onSendVerifyCodeLoginedFinished(Response response) {
			if (response.isSuccess()) {
				mTxtCode.setEnabled(false);
				mTxtVoiceCode.setEnabled(false);
				mEditCode.requestFocus();
				mCountdownStartTime = System.currentTimeMillis();
				mCountdownHandler.removeCallbacks(mCountdownTask);
				mCountdownHandler.postDelayed(mCountdownTask, 1000);
				AppUtils.showToast(ModifyPhoneActivity.this, response.getMessage());
			} else {
				AppUtils.handleErrorResponse(ModifyPhoneActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};

	private OnClickListener mBtnNextOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			String code = mEditCode.getText().toString().trim();
			if (TextUtils.isEmpty(code)) {
				AppUtils.showToast(ModifyPhoneActivity.this, R.string.verify_code_empty);
				return;
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("authCode", code);

			mProgressDialog.show();
			DdApplication.getUserManager().changeVaildate(datas, mOnChangeVaildateFinishedListener);
		}

	};

	private OnChangeVaildateFinishedListener mOnChangeVaildateFinishedListener = new OnChangeVaildateFinishedListener() {

		@Override
		public void onChangeVaildateFinished(Response response, String oldCode, String oldCodeSign) {
			if (response.isSuccess()) {
				Intent intent = new Intent(ModifyPhoneActivity.this, ResetPhoneActivity.class);
				intent.putExtra("oldCode", oldCode);
				intent.putExtra("oldCodeSign", oldCodeSign);
				startActivityForResult(intent, REQUEST_CODE);
			} else {
				AppUtils.handleErrorResponse(ModifyPhoneActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};
	
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
    }
}