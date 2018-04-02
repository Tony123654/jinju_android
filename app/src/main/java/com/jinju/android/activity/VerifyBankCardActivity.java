package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
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
import com.jinju.android.api.AuthCardDetail;
import com.jinju.android.api.Bank;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.SrcType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.manager.TradeManager;
import com.jinju.android.manager.TradeManager.OnInitAuthCardFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.Utils;
import com.jinju.android.widget.WithClearEditText;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加银行卡界面
 */
public class VerifyBankCardActivity extends BaseActivity {
	private final static int REQUEST_BANK = 1;
	private static final int VERIFY_CODE = 2;
	private Dialog mProgressDialog;
	
	private ImageView mImgFinish;

	private WithClearEditText mEditName;
	private WithClearEditText mEditCard;
	private EditText mEditBank;
	private TextView mTxtSupportBank;
	private WithClearEditText mEditBankCardNo;
	private WithClearEditText mEditBankTel;

	private TextView mTxtNotice;
	
	private Button mBtnNext;
		
	private long mSrcId;
	private int mSrcType;
	private String mBankId;
	private List<Bank> mBankList;

	private Bank mSelectBank;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_bank_card);
		
		Intent intent = getIntent();
		mSrcId = intent.getLongExtra("srcId", 0);
		mSrcType = intent.getIntExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);
		mBankId = intent.getStringExtra("id");

		mImgFinish = (ImageView) findViewById(R.id.img_finish);
		mImgFinish.setOnClickListener(mImgFinishOnClickListener);

		mEditName = (WithClearEditText) findViewById(R.id.edit_name);
		mEditName.addTextChangedListener(mEditNameWatcher);

		mEditCard = (WithClearEditText) findViewById(R.id.edit_card);
		mEditCard.addTextChangedListener(mEditCardWatcher);

		mEditBank = (EditText) findViewById(R.id.edit_bank);
		//选择银行卡
		mTxtSupportBank = (TextView) findViewById(R.id.txt_support_bank);
		mTxtSupportBank.setOnClickListener(mTxtSupportBankOnClickListener);
		
		mEditBankCardNo = (WithClearEditText) findViewById(R.id.edit_bank_card_no);
		mEditBankCardNo.addTextChangedListener(mEditBankCardNoWatcher);

		mEditBankTel = (WithClearEditText) findViewById(R.id.edit_bank_tel);
		mEditBankTel.addTextChangedListener(mEditBankTelWatcher);
		
		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setOnClickListener(mBtnNextOnClickListener);
		
		mTxtNotice = (TextView) findViewById(R.id.txt_notice);
		mTxtNotice.setText(Html.fromHtml(getString(R.string.verify_bank_card_notice)));

		mProgressDialog = AppUtils.createLoadingDialog(this);
		//银行卡类别
		initAuthCard();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == REQUEST_BANK) {
			mSelectBank = (Bank) data.getSerializableExtra("selectBank");
			mEditBank.setText(mSelectBank.getBankName());

			checkNextButton();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditName);
		viewList.add(mEditCard);
		viewList.add(mEditBankCardNo);
		viewList.add(mEditBankTel);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}
	
	private OnClickListener mImgFinishOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MobclickAgent.onEvent(VerifyBankCardActivity.this, UmengTouchType.RP116_3);
			finish();
		}
		
	};
	
	private void initAuthCard() {
		Map<String, Object> datas = new HashMap<String, Object>();
		if(!TextUtils.isEmpty(mBankId))
			datas.put("memberBankId", mBankId);
		mProgressDialog.show();
		DdApplication.getTradeManager().initAuthCard(datas, mOnInitAuthCardFinishedListener);
	}
	
	private OnInitAuthCardFinishedListener mOnInitAuthCardFinishedListener = new OnInitAuthCardFinishedListener() {

		@Override
		public void onInitAuthCardFinished(Response response,
				AuthCardDetail authCardDetail) {
			// TODO Auto-generated method stub
			if (response.isSuccess()) {
					mBankList = authCardDetail.getBankList();

					mEditName.setText(authCardDetail.getAccount());
					mEditBankCardNo.setText(authCardDetail.getCardNoTail());
					mEditBankTel.setText(authCardDetail.getTel());

					setNextButton(false);
			} else {
				AppUtils.handleErrorResponse(VerifyBankCardActivity.this, response);
			}

			mProgressDialog.dismiss();
		}
		
	};
	
	private OnClickListener mTxtSupportBankOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(VerifyBankCardActivity.this, BankListActivity.class);
			intent.putExtra("banklist", (Serializable)mBankList);
			if(null != mSelectBank) {
				intent.putExtra("selectBank", (Serializable) mSelectBank);
			}
			startActivityForResult(intent, REQUEST_BANK);
		}
		
	};

	private TextWatcher mEditNameWatcher = new TextWatcher() {
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
	
	private TextWatcher mEditBankCardNoWatcher = new TextWatcher() {

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
			checkNextButton();
		}
		
	};
	
	private TextWatcher mEditBankTelWatcher = new TextWatcher() {

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
			checkNextButton();
		}
		
	};
	
	private OnClickListener mBtnNextOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Utils.isFastClick()) {
				String name = mEditName.getText().toString().trim();
				String card = mEditCard.getText().toString().trim();
				String bankCardNo = mEditBankCardNo.getText().toString().trim();
				String bankTel = mEditBankTel.getText().toString().trim();
				Map<String, Object> datas = new HashMap<String, Object>();
				datas.put("bankCode", mSelectBank.getBankCode());
				datas.put("bankCardNo", bankCardNo);
				datas.put("name", name);
				datas.put("idNo", card);
				datas.put("tel", bankTel);
				mProgressDialog.show();
				DdApplication.getTradeManager().sendCode(datas, mOnSendCodeFinishedListener);
			}

		}
		
	};

	private TradeManager.OnSendCodeFinishedListener mOnSendCodeFinishedListener = new TradeManager.OnSendCodeFinishedListener() {

		@Override
		public void onSendCodeFinished(Response response, String memberBankId, String bankCardNo, String tel) {

			// TODO Auto-generated method stub
			if (response.isSuccess()) {
				Intent intent = new Intent(VerifyBankCardActivity.this, VerifyCodeActivity.class);
				intent.putExtra(SrcType.SRC_TYPE, mSrcType);
				intent.putExtra("srcId", mSrcId);
				intent.putExtra("memberBankId", memberBankId);
				intent.putExtra("cardNo", bankCardNo);
				intent.putExtra("tel", tel);
				startActivity(intent);
				finish();
				AppUtils.showToast(VerifyBankCardActivity.this, response.getMessage());
			} else {
				AppUtils.handleErrorResponse(VerifyBankCardActivity.this, response);
			}
			mProgressDialog.dismiss();
		}

	};

	private void checkNextButton() {
		String name = mEditName.getText().toString().trim();
		String card = mEditCard.getText().toString().trim();
		String bankCardNo = mEditBankCardNo.getText().toString().trim();
		String bankTel = mEditBankTel.getText().toString().trim();
		
		if((bankCardNo.length() <= 25 && bankCardNo.length() >= 15) && (11 == bankTel.length())
				&& !name.isEmpty() && (15 == card.length() || 18 == card.length()) && null != mSelectBank) {
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