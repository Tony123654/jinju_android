package com.jinju.android.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.AutomateRule;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AutomateStatus;
import com.jinju.android.dialog.DoubleChoiceDialog;
import com.jinju.android.dialog.SingleChoiceDialog;
import com.jinju.android.manager.UserManager.OnGetAutomateRuleFinishedListener;
import com.jinju.android.manager.UserManager.OnSetAutomateRuleFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.widget.DrawableCenterButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomateSettingActivity extends BaseActivity {
	private AutomateRule mAutomateRule;

	private LinearLayout mLayoutCycle;
	private LinearLayout mLayoutYearInterest;

	private TextView mTxtMinTimeLimit;
	private TextView mTxtMaxTimeLimit;
	private TextView mTxtYearInterest;
	private EditText mEditMinAmount;
	private EditText mEditRetainAmount;

	private DrawableCenterButton mBtnConfirm;
	private Dialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_automate_setting);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText("设置");

		mAutomateRule = new AutomateRule();
		
		mLayoutCycle = (LinearLayout) findViewById(R.id.layout_cycle);
		mLayoutCycle.setOnClickListener(mLayoutCycleOnClickListener);

		mTxtMinTimeLimit = (TextView) findViewById(R.id.txt_min_time_limit);
		mTxtMaxTimeLimit = (TextView) findViewById(R.id.txt_max_time_limit);
		
		mLayoutYearInterest = (LinearLayout) findViewById(R.id.layout_year_interest);
		mLayoutYearInterest.setOnClickListener(mLayoutYearInterestOnClickListener);
		
		mTxtYearInterest = (TextView) findViewById(R.id.txt_year_interest);
		
		mEditMinAmount = (EditText) findViewById(R.id.edit_min_amount);
		mEditMinAmount.setOnFocusChangeListener(mEditMinAmountOnFocusChangeListener);
		
		mEditRetainAmount = (EditText) findViewById(R.id.edit_retain_amount);
		mEditRetainAmount.setOnFocusChangeListener(mEditRetainAmountOnFocusChangeListener);
		
		mBtnConfirm = (DrawableCenterButton) findViewById(R.id.btn_confirm);
		mBtnConfirm.requestFocus();
		mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);

		mProgressDialog = AppUtils.createLoadingDialog(this);
		
		getAutomateRule();
	}

	@Override
	protected void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditMinAmount);
		viewList.add(mEditRetainAmount);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}
	
	private void getAutomateRule() {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("iv", 1);
		
		mProgressDialog.show();
		DdApplication.getUserManager().getAutomateRule(datas, mOnGetAutomateRuleFinishedListener);
	}

	private OnGetAutomateRuleFinishedListener mOnGetAutomateRuleFinishedListener = new OnGetAutomateRuleFinishedListener() {

		@Override
		public void onGetAutomateRuleFinished(Response response, AutomateRule automateRule) {
			if (response.isSuccess()) {
				mAutomateRule = automateRule;
				
				String minTimeLimitStr;
				if(0 == automateRule.getMinTimeLimit()) {
					minTimeLimitStr = getString(R.string.no_limit);
				} else {
					minTimeLimitStr = getString(R.string.day_number, automateRule.getMinTimeLimit());
				}
				
				String maxTimeLimitStr;
				if(0 == automateRule.getMaxTimeLimit()) {
					maxTimeLimitStr = getString(R.string.no_limit);
				} else {
					maxTimeLimitStr = getString(R.string.day_number, automateRule.getMaxTimeLimit());
				}

				mTxtMinTimeLimit.setText(minTimeLimitStr);
				mTxtMaxTimeLimit.setText(maxTimeLimitStr);
				
				if(TextUtils.equals(automateRule.getYearRateMin(), "0")) {
					mTxtYearInterest.setText(getString(R.string.no_limit));
				} else {
					mTxtYearInterest.setText(getString(R.string.percent_number, automateRule.getYearRateMin()));
				}
				
				if(TextUtils.equals(automateRule.getMinMoneyYuan(), "0")) {
					mEditMinAmount.setText(getString(R.string.no_limit));
				} else {
					mEditMinAmount.setText(getString(R.string.money_number, automateRule.getMinMoneyYuan()));
				}
				
				if(TextUtils.equals(automateRule.getRetainFundYuan(), "0")) {
					mEditRetainAmount.setText(getString(R.string.no_limit));
				} else {
					mEditRetainAmount.setText(getString(R.string.money_number, automateRule.getRetainFundYuan()));
				}
				
			} else {
				AppUtils.handleErrorResponse(AutomateSettingActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};
	
	private OnClickListener mLayoutCycleOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final List<String> firstCycleList = getFirstCycleList();
			final List<String> secondCycleList = getSecondCycleList();
			
			int minTimeLimit = mAutomateRule.getMinTimeLimit();
			String minTimeLimitStr;
			if(0 == minTimeLimit) {
				minTimeLimitStr = getString(R.string.no_limit);
			} else {
				minTimeLimitStr = getString(R.string.day_number, minTimeLimit);
			}
			
			int maxTimeLimit = mAutomateRule.getMaxTimeLimit();
			String maxTimeLimitStr;
			if(0 == maxTimeLimit) {
				maxTimeLimitStr = getString(R.string.no_limit);
			} else {
				maxTimeLimitStr = getString(R.string.day_number, maxTimeLimit);
			}
			
			DoubleChoiceDialog dialog = new DoubleChoiceDialog(AutomateSettingActivity.this, R.style.custom_animation_dialog);
			dialog.setFirstChoiceItems(firstCycleList, minTimeLimitStr);
			dialog.setSecondChoiceItems(secondCycleList, maxTimeLimitStr);
			dialog.setOnCompletedListener(new DoubleChoiceDialog.OnCompletedListener() {
				@Override
				public void onCompletedFinished(int firstPosition,
						int secondPosition) {
					// TODO Auto-generated method stub
					if(secondPosition < (firstPosition - 1)) {
						AppUtils.showToast(AutomateSettingActivity.this, getString(R.string.automate_cycle_error));
						return;
					}
					String minTimeLimitStr = firstCycleList.get(firstPosition);
					String maxTimeLimitStr = secondCycleList.get(secondPosition);

					mTxtMinTimeLimit.setText(minTimeLimitStr);
					mTxtMaxTimeLimit.setText(maxTimeLimitStr);
					
					List<Integer> subTimeOptions = mAutomateRule.getSubTimeOptions();
					if(0 == firstPosition) {
						mAutomateRule.setMinTimeLimit(0);
					} else {
						mAutomateRule.setMinTimeLimit(subTimeOptions.get(firstPosition - 1));
					}
					
					if(subTimeOptions.size() == secondPosition) {
						mAutomateRule.setMaxTimeLimit(0);
					} else {
						mAutomateRule.setMaxTimeLimit(subTimeOptions.get(secondPosition));
					}
				}
			});
			dialog.getWindow().setGravity(Gravity.BOTTOM);
			dialog.show();			
		}
		
	};
	
	private List<String> getFirstCycleList() {
		List<String> firstCycleList = new ArrayList<String>();
		List<Integer> subTimeOptions = mAutomateRule.getSubTimeOptions();
		firstCycleList.add(getString(R.string.no_limit));
		for (int i = 0; i < mAutomateRule.getSubTimeOptions().size(); i++) {
			firstCycleList.add(getString(R.string.day_number, subTimeOptions.get(i)));
		}
		return firstCycleList;
	}
	
	private List<String> getSecondCycleList() {
		List<String> secondCycleList = new ArrayList<String>();
		List<Integer> subTimeOptions = mAutomateRule.getSubTimeOptions();
		for (int i = 0; i < mAutomateRule.getSubTimeOptions().size(); i++) {
			secondCycleList.add(getString(R.string.day_number, subTimeOptions.get(i)));
		}
		secondCycleList.add(getString(R.string.no_limit));
		return secondCycleList;
	}
	
	private OnClickListener mLayoutYearInterestOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String yearInterest = mTxtYearInterest.getText().toString().trim();
			
			final List<String> yearRateList = getYearRateList();
			SingleChoiceDialog dialog = new SingleChoiceDialog(AutomateSettingActivity.this, R.style.custom_animation_dialog);
			dialog.setSingleChoiceItems(yearRateList, yearInterest);
			dialog.setOnCompletedListener(new SingleChoiceDialog.OnCompletedListener() {
				@Override
				public void onCompletedFinished(int position) {
					if (position < yearRateList.size()) {
						mAutomateRule.setYearRateMin(Integer.toString(position));
						mTxtYearInterest.setText(yearRateList.get(position));
					}
				}
			});
			dialog.getWindow().setGravity(Gravity.BOTTOM);
			dialog.show();
		}
	};
	
	private List<String> getYearRateList() {
		List<String> yearRateList = new ArrayList<String>();
		yearRateList.add(getString(R.string.no_limit));
		for (int i = 1; i <= 20; i++)
			yearRateList.add(getString(R.string.percent_number, i));
		return yearRateList;
	}
	
	private OnFocusChangeListener mEditMinAmountOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (hasFocus) {
				mEditMinAmount.setText("");
			} else {
				String amount = mEditMinAmount.getText().toString().trim();
				if(TextUtils.isEmpty(amount) || !DataUtils.isDouble(amount)) {
					mAutomateRule.setMinMoneyYuan("0");;
					mEditMinAmount.setText(getString(R.string.no_limit));
				} else {
					double minAmount = Double.parseDouble(amount);
					if(0 == DataUtils.compareDouble(minAmount, 0)) {
						mAutomateRule.setMinMoneyYuan("0");;
						mEditMinAmount.setText(getString(R.string.no_limit));
					} else {
						mAutomateRule.setMinMoneyYuan(DataUtils.convertTwoDecimalNoFormat(minAmount));
						mEditMinAmount.setText(getString(R.string.money_number, DataUtils.convertTwoDecimalNoFormat(minAmount)));
					}
				}
			}
		}
		
	};
	
	private OnFocusChangeListener mEditRetainAmountOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (hasFocus) {
				mEditRetainAmount.setText("");
			} else {
				String amount = mEditRetainAmount.getText().toString().trim();
				if(TextUtils.isEmpty(amount) || !DataUtils.isDouble(amount)) {
					mAutomateRule.setRetainFundYuan("0");
					mEditRetainAmount.setText(getString(R.string.no_limit));
				} else {
					double retainAmount = Double.parseDouble(amount);
					if(0 == DataUtils.compareDouble(retainAmount, 0)) {
						mAutomateRule.setRetainFundYuan("0");;
						mEditRetainAmount.setText(getString(R.string.no_limit));
					} else {
						mAutomateRule.setRetainFundYuan(DataUtils.convertTwoDecimalNoFormat(retainAmount));
						mEditRetainAmount.setText(getString(R.string.money_number, DataUtils.convertTwoDecimalNoFormat(retainAmount)));
					}
				}
			}
		}
		
	};
	
	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("iv", 1);
			datas.put("status", AutomateStatus.ENABLE);
			datas.put("minMoneyYuan", mAutomateRule.getMinMoneyYuan());
			datas.put("minRate", mAutomateRule.getYearRateMin());
			datas.put("minTimeLimit", mAutomateRule.getMinTimeLimit());
			datas.put("maxTimeLimit", mAutomateRule.getMaxTimeLimit());
			datas.put("retainFundYuan", mAutomateRule.getRetainFundYuan());

			mProgressDialog.show();
			DdApplication.getUserManager().setAutomateRule(datas, mOnSetAutomateRuleFinishedListener);
		}
		
	};
	
	private OnSetAutomateRuleFinishedListener mOnSetAutomateRuleFinishedListener = new OnSetAutomateRuleFinishedListener() {

		@Override
		public void onSetAutomateRuleFinished(Response response) {
			mProgressDialog.dismiss();
			if (response.isSuccess()) {
				AppUtils.showToast(AutomateSettingActivity.this, response.getMessage());
				finish();
			} else {
				AppUtils.handleErrorResponse(AutomateSettingActivity.this, response);
			}
		}
	};
}