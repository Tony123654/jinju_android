package com.jinju.android.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Area;
import com.jinju.android.api.BankDetail;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.dialog.SingleChoiceDialog;
import com.jinju.android.manager.CommonManager.OnGetCityListFinishedListener;
import com.jinju.android.manager.CommonManager.OnGetProvinceListFinishedListener;
import com.jinju.android.manager.TradeManager.OnInitBankEditFinishedListener;
import com.jinju.android.manager.TradeManager.OnSaveBankFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditBankActivity extends BaseActivity {

	private String mBankId;
	private String mBankCode;
	private String mProvince;
	private String mCity;

	private List<Area> mProvinceList;
	private List<Area> mCityList;

	private ImageView mImgBankLogo;
	private TextView mTxtBankName;
	private TextView mTxtBankCard;
	private TextView mTxtAccount;
	private TextView mTxtProvince;
	private TextView mTxtCity;
	private EditText mEditBranch;
	private Dialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_bank);

		mBankId = getIntent().getStringExtra("id");

		mProvinceList = new ArrayList<Area>();
		mCityList = new ArrayList<Area>();

		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);

		Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
		btnConfirm.setOnClickListener(mBtnConfirmOnClickListener);

		mImgBankLogo = (ImageView) findViewById(R.id.img_bank_logo);
		mTxtBankName = (TextView) findViewById(R.id.txt_bank_name);
		mTxtBankCard = (TextView) findViewById(R.id.txt_bank_card);
		mTxtAccount = (TextView) findViewById(R.id.txt_account);
		
		mTxtProvince = (TextView) findViewById(R.id.txt_province);
		mTxtProvince.setOnClickListener(mTxtProvinceOnClickListener);
		
		mTxtCity = (TextView) findViewById(R.id.txt_city);
		mTxtCity.setOnClickListener(mTxtCityOnClickListener);
		
		mEditBranch = (EditText) findViewById(R.id.edit_branch);

		mProgressDialog = AppUtils.createLoadingDialog(this);

		initBankEdit();
	}

	@Override
	protected void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditBranch);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}

	private void initBankEdit() {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("id", mBankId);
		datas.put("iv", 1);

		mProgressDialog.show();
		DdApplication.getTradeManager().initBankEdit(datas, mOnInitBankEditFinishedListener);
	}

	private OnInitBankEditFinishedListener mOnInitBankEditFinishedListener = new OnInitBankEditFinishedListener() {

		@Override
		public void onInitBankEditFinished(Response response, BankDetail bankDetail) {
			if (response.isSuccess()) {
				mBankCode = bankDetail.getBankCode();
				mProvince = bankDetail.getBankProvinceCode();
				mCity = bankDetail.getBankCityCode();

				ImageUtils.displayImage(mImgBankLogo, bankDetail.getLogoPath());
				mTxtBankName.setText(bankDetail.getBankName());
				mTxtBankCard.setText(getString(R.string.bank_card_num, bankDetail.getCardNoTail()));
				mTxtAccount.setText(bankDetail.getAccount());
				mTxtProvince.setText(bankDetail.getBankProvinceName());
				mTxtCity.setText(bankDetail.getBankCityName());
				mEditBranch.setText(bankDetail.getBankBranchName());
			} else {
				AppUtils.handleErrorResponse(EditBankActivity.this, response);
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

	private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (TextUtils.isEmpty(mBankCode)) {
				AppUtils.showToast(EditBankActivity.this, R.string.bank_name_hint);
				return;
			}

			if (TextUtils.isEmpty(mProvince)) {
				AppUtils.showToast(EditBankActivity.this, R.string.bank_province_hint);
				return;
			}

			if (TextUtils.isEmpty(mCity)) {
				AppUtils.showToast(EditBankActivity.this, R.string.bank_city_hint);
				return;
			}

			String branch = mEditBranch.getText().toString().trim();

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("id", mBankId);
			datas.put("bankCode", mBankCode);
			datas.put("bankLocationProvince", mProvince);
			datas.put("bankLocationCity", mCity);
			datas.put("bankBranchName", branch);
			datas.put("iv", 1);

			mProgressDialog.show();
			DdApplication.getTradeManager().saveBank(datas, mOnSaveBankFinishedListener);
		}

	};

	private OnSaveBankFinishedListener mOnSaveBankFinishedListener = new OnSaveBankFinishedListener() {

		@Override
		public void onSaveBankFinished(Response response) {
			if (response.isSuccess()) {
				AppUtils.showToast(EditBankActivity.this, response.getMessage());
				setResult(RESULT_OK);
				finish();
			} else {
				AppUtils.handleErrorResponse(EditBankActivity.this, response);
				mProgressDialog.dismiss();
			}
		}

	};

	private OnClickListener mTxtProvinceOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			mProgressDialog.show();
			DdApplication.getCommonManager().getProvinceList(mOnGetProvinceListFinishedListener);
		}

	};

	private OnGetProvinceListFinishedListener mOnGetProvinceListFinishedListener = new OnGetProvinceListFinishedListener() {

		@Override
		public void onGetProvinceListFinished(Response response, List<Area> provinceList) {
			if (response.isSuccess()) {
				mProvinceList = provinceList;

				final List<String> provinceNameList = getProvinceNameList();
				SingleChoiceDialog dialog = new SingleChoiceDialog(EditBankActivity.this, R.style.custom_animation_dialog);
				dialog.setSingleChoiceItems(provinceNameList);
				dialog.setOnCompletedListener(new SingleChoiceDialog.OnCompletedListener() {
					@Override
					public void onCompletedFinished(int position) {
						if (position < mProvinceList.size()) {
							mCity = null;
							mTxtCity.setText(null);
							mProvince = mProvinceList.get(position).getValue();
							mTxtProvince.setText(provinceNameList.get(position));
						}
					}
				});
				dialog.getWindow().setGravity(Gravity.BOTTOM);
				dialog.show();
			} else {
				AppUtils.handleErrorResponse(EditBankActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};

	private List<String> getProvinceNameList() {
		List<String> provinceNameList = new ArrayList<String>();
		for (int i = 0; i < mProvinceList.size(); i++)
			provinceNameList.add(mProvinceList.get(i).getName());
		return provinceNameList;
	}

	private OnClickListener mTxtCityOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (TextUtils.isEmpty(mProvince)) {
				AppUtils.showToast(EditBankActivity.this, R.string.bank_province_hint);
				return;
			}

			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("province", mProvince);

			mProgressDialog.show();
			DdApplication.getCommonManager().getCityList(datas, mOnGetCityListFinishedListener);
		}

	};

	private OnGetCityListFinishedListener mOnGetCityListFinishedListener = new OnGetCityListFinishedListener() {

		@Override
		public void onGetCityListFinished(Response response, List<Area> cityList) {
			if (response.isSuccess()) {
				mCityList = cityList;

				final List<String> cityNameList = getCityNameList();
				SingleChoiceDialog dialog = new SingleChoiceDialog(EditBankActivity.this, R.style.custom_animation_dialog);
				dialog.setSingleChoiceItems(cityNameList);
				dialog.setOnCompletedListener(new SingleChoiceDialog.OnCompletedListener() {
					@Override
					public void onCompletedFinished(int position) {
						if (position < mCityList.size()) {
							mCity = mCityList.get(position).getValue();
							mTxtCity.setText(cityNameList.get(position));
						}
					}
				});
				dialog.getWindow().setGravity(Gravity.BOTTOM);
				dialog.show();
			} else {
				AppUtils.handleErrorResponse(EditBankActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};

	private List<String> getCityNameList() {
		List<String> cityNameList = new ArrayList<String>();
		for (int i = 0; i < mCityList.size(); i++)
			cityNameList.add(mCityList.get(i).getName());
		return cityNameList;
	}

}