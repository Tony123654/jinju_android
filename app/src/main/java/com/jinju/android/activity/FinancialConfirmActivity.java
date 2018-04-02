package com.jinju.android.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.FinancialDetail;
import com.jinju.android.api.MemberGift;
import com.jinju.android.api.Order;
import com.jinju.android.api.PayOrder;
import com.jinju.android.api.PayResult;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.PayType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.PayDialog;
import com.jinju.android.dialog.SelectRedPacketDialog;
import com.jinju.android.manager.LoanManager.OnConfirmFinancialOrderFinishedListener;
import com.jinju.android.manager.LoanManager.OnGetFinancialDetailFinishedListener;
import com.jinju.android.manager.LoanManager.OnPayFinancialFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.util.PayUtils;
import com.jinju.android.util.PayUtils.OnDealPayErrorFinishedListener;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.widget.LastInputEditText;
import com.jinju.android.widget.PayView;
import com.jinju.android.widget.PayView.OnPayListener;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 		购买界面
 */

public class FinancialConfirmActivity extends BaseFragmentActivity{

	public static final String MEMBER_GIFT_LIST = "memberGiftList";
	public static final String INTEREST_GIFT_LIST = "InterestGiftList";
	public static final String SELECT_POSITION = "selectPosition";
	public static final String SELECT_Type = "selectType";

	private TextView mTxtName;
	private TextView mTxtYearInterest;
	private TextView mTxtLoanPeriod;
	private TextView mTxtLoanPeriodUnit;
	private TextView mTxtHasFundsAmount;
	private TextView mTxtHasFundsAmountUnit;
	private TextView mTxtCoupon;
	private TextView mTxtFinancialIncome;
	private TextView mTxtCanUseBalance;
	private TextView mTxtProtocol;
	private LinearLayout mLayoutCoupon;
	private Button mBtnNext;
	
	private LastInputEditText mEditFinancialAmount;
	
	private String mIncome = "0.00";
	
	private List<MemberGift> mMemberGiftList;//普通红包
	private List<MemberGift> mInterestList;//加息红包
	private List<MemberGift> usableMemberList;
	private List<MemberGift> usableInterestList;

	private FinancialDetail mFinancialDetail;
	
	private boolean mHasCoupon = false;//是否有可用红包
	private boolean mCouponEnable = false;//是否选中红包
	private ImageView mImgCouponEnable;
	private MemberGift mSelectMemberGift;//已选中的红包
		
	private long mFinancialId;
	private Dialog mProgressDialog;
	private PayOrder mPayOrder;
	private PayDialog mPayDialog;
	private Boolean isTouch = true;
	private int selectRedPosition = -1;//选中的红包
	private int giftType = 0;//计算收益类型
	private int selectType = -1;//选中券的类型，0是加息券，1是红包
	private long hasFundsAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_financial_confirm);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.financial_confirm_title);

		Intent intent = getIntent();
		
		mFinancialId = intent.getLongExtra("srcId", 0);

		usableMemberList = new ArrayList<MemberGift>();//可用普通红包
		usableInterestList = new ArrayList<MemberGift>();//可用加息红包

		mTxtName = (TextView) findViewById(R.id.txt_name);
		mTxtYearInterest = (TextView) findViewById(R.id.txt_year_interest);
		mTxtLoanPeriod = (TextView) findViewById(R.id.txt_loan_period);
		mTxtLoanPeriodUnit = (TextView) findViewById(R.id.txt_loan_period_unit);
		mTxtHasFundsAmount = (TextView) findViewById(R.id.txt_has_funds_amount);
		mTxtHasFundsAmountUnit = (TextView) findViewById(R.id.txt_has_funds_amount_unit);
		mTxtFinancialIncome = (TextView) findViewById(R.id.txt_financial_income);
		mTxtCanUseBalance = (TextView) findViewById(R.id.txt_can_use_balance);

		mLayoutCoupon = (LinearLayout) findViewById(R.id.layout_coupon);
		mLayoutCoupon.setOnClickListener(mLayoutCouponOnClickListener);
		mImgCouponEnable = (ImageView) findViewById(R.id.img_coupon_enable);

		mTxtCoupon = (TextView) findViewById(R.id.txt_coupon);
		
		mTxtProtocol = (TextView) findViewById(R.id.txt_protocol);
		mTxtProtocol.setOnClickListener(mTxtProtocolOnClickListener);
		
		mBtnNext = (Button) findViewById(R.id.btn_next);
		mBtnNext.setOnClickListener(mBtnNextOnClickListener);
		setNextBtn(false);
		
		mEditFinancialAmount = (LastInputEditText) findViewById(R.id.edit_financial_amount);
		mEditFinancialAmount.addTextChangedListener(mEditFinancialAmountWatcher);

		TextView txtSafeMessage = (TextView) findViewById(R.id.txt_safe_message);
		String message = DdApplication.getConfigManager().getSecurityMessage();
		txtSafeMessage.setText(message);
		
		mPayOrder = new PayOrder();

		mProgressDialog = AppUtils.createLoadingDialog(this);
		//默认
		mTxtCoupon.setText(R.string.financial_no_coupon);
		mLayoutCoupon.setClickable(false);
		getFinancialDetail();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		List<View> viewList = new ArrayList<View>();
		viewList.add(mEditFinancialAmount);

		AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};
	
	private TextWatcher mEditFinancialAmountWatcher = new TextWatcher() {

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


			String amountStr = mEditFinancialAmount.getText().toString().trim();
			if(TextUtils.isEmpty(amountStr)) {
				amountStr = "0";
			}
			long amount = Long.parseLong(amountStr);
			if (amount>(hasFundsAmount/100)) {
				mEditFinancialAmount.setText(""+(hasFundsAmount/100));
				amount = hasFundsAmount/100;
			}
			//筛选满足条件红包
			selectGift();
			//选择时，初始化清空数据
			selectRedPosition = -1;
			selectType = -1;
			mTxtCoupon.setTextColor(getResources().getColor(R.color.gray));
			giftType = 0;
			mSelectMemberGift = null;
			if (amount > 0) {
				int usableMemberGiftCount = 0;
				int usableInterestCount = 0;
				if (usableMemberList!=null) {
					usableMemberGiftCount = usableMemberList.size();
				}
				if (usableInterestList!=null) {
					usableInterestCount = usableInterestList.size();
				}
				if ((usableMemberGiftCount+usableInterestCount)>0) {
					mHasCoupon = true;
					mTxtCoupon.setText(getString(R.string.financial_coupon_count,usableMemberGiftCount+usableInterestCount));
					mTxtCoupon.setTextColor(getResources().getColor(R.color.main_red));
					mLayoutCoupon.setClickable(true);//可选择
				}else {
					mHasCoupon = false;
					mTxtCoupon.setText(R.string.financial_no_coupon);
					mLayoutCoupon.setClickable(false);//不能选择
				}

			} else {
				mHasCoupon = false;
				mTxtCoupon.setText(R.string.financial_no_coupon);
				mLayoutCoupon.setClickable(false);//不能选择
			}

			setFinancialIncome();
			setPayAmount();
		}
		
	};
	/**
	 *     点击我的红包事件
	 */
	private OnClickListener mLayoutCouponOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			MobclickAgent.onEvent(FinancialConfirmActivity.this, UmengTouchType.RP104_1);
			String amountStr = mEditFinancialAmount.getText().toString().trim();
			if(TextUtils.isEmpty(amountStr)) {
				amountStr = "0";
			}
			long amount = Long.parseLong(amountStr);
			SelectRedPacketDialog mSelectRedPacketDialog  = new SelectRedPacketDialog();
			mSelectRedPacketDialog.setOnSelectItemListener(mOnSelectItemListener);

			Bundle bundle = new Bundle();
			bundle.putSerializable(MEMBER_GIFT_LIST,(Serializable)usableMemberList);
			bundle.putSerializable(INTEREST_GIFT_LIST,(Serializable)usableInterestList);
			bundle.putInt(SELECT_POSITION,selectRedPosition);
			bundle.putInt(SELECT_Type,selectType);
			bundle.putLong("currentAmount",amount);
			mSelectRedPacketDialog.setArguments(bundle);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			mSelectRedPacketDialog.show(ft,"");

		}
		
	};
	private SelectRedPacketDialog.OnSelectItemListener mOnSelectItemListener = new SelectRedPacketDialog.OnSelectItemListener() {
		@Override
		public void onItemSelect(int type, int position) {
			//0是加息券，1是红包
			selectType = type;
			selectRedPosition = position;

			if (selectRedPosition < 0) {
				mSelectMemberGift = null;
				mTxtCoupon.setText(R.string.financial_no_use_coupon);
				mTxtCoupon.setTextColor(getResources().getColor(R.color.gray));
				mCouponEnable = false;
			} else {

				if (selectType == 0) {	//加息券
					mSelectMemberGift = usableInterestList.get(selectRedPosition);
				}else {//红包
					mSelectMemberGift = usableMemberList.get(selectRedPosition);
				}

				String giftValue = DataUtils.convertNumberFormat(mSelectMemberGift.getGiftValue() / 100);
				giftType = mSelectMemberGift.getGiftType();
				if (giftType == 3) {//加息红包
					String ratesValue = DataUtils.convertNumberFormat(mSelectMemberGift.getGiftValue());
					mTxtCoupon.setText("已加息"+ratesValue+"%");
				} else {
					mTxtCoupon.setText(getString(R.string.financial_use_coupon,giftValue));
				}

				mTxtCoupon.setTextColor(getResources().getColor(R.color.main_red));
				String amountStr = mEditFinancialAmount.getText().toString().trim();
				if(TextUtils.isEmpty(amountStr)) {
					amountStr = "0";
				}
				long amount = Long.parseLong(amountStr);

				if(amount * 100 >= mSelectMemberGift.getGiftLimit()) {
					mCouponEnable = true;
				} else {
					mCouponEnable = false;
				}
			}

			//设置预估利息收益
			setFinancialIncome();
			setPayAmount();
		}
	};
	private OnClickListener mTxtProtocolOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mFinancialDetail!=null) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FinancialConfirmActivity.this, BaseJsBridgeWebViewActivity.class);
				intent.putExtra("url", mFinancialDetail.getContractUrl());
				startActivity(intent);
			}
		}
		
	};

	
	private void getFinancialDetail() {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("financialId", mFinancialId);
		datas.put("iv", DdApplication.getVersionName());//目的是获取红包信息

		mProgressDialog.show();
		DdApplication.getLoanManager().getFinancialDetail(datas, mOnGetFinancialDetailFinishedListener);
	}
	//红包数据
	private OnGetFinancialDetailFinishedListener mOnGetFinancialDetailFinishedListener = new OnGetFinancialDetailFinishedListener() {

		@Override
		public void onGetFinancialDetailFinished(Response response, FinancialDetail financialDetail) {
			if (response.isSuccess()) {
				mFinancialDetail = financialDetail;
				mMemberGiftList = financialDetail.getMemberGiftList();
				mInterestList = financialDetail.getInterestList();

				mTxtName.setText(financialDetail.getName());
				mTxtYearInterest.setText(financialDetail.getYearInterest());
				
				String loanPeriodDesc = financialDetail.getLoanPeriodDesc();
				String regEx="[0-9^]"; 
				Pattern p = Pattern.compile(regEx); 
				Matcher m = p.matcher(loanPeriodDesc);
				
				String loanPeriodUnit = m.replaceAll("");
				String loanPeriod = loanPeriodDesc.substring(0, loanPeriodDesc.length() - loanPeriodUnit.length());
				
				mTxtLoanPeriod.setText(loanPeriod);
				mTxtLoanPeriodUnit.setText(loanPeriodUnit);
				hasFundsAmount = financialDetail.getHasFundsAmount();

				if(financialDetail.getHasFundsAmount() >= 100000000) {
					mTxtHasFundsAmount.setText(DataUtils.convertMillion(financialDetail.getHasFundsAmount()));
					mTxtHasFundsAmountUnit.setText(getString(R.string.million_money_unit));
				} else {
					mTxtHasFundsAmount.setText(DataUtils.convertToYuan(financialDetail.getHasFundsAmount()));
					mTxtHasFundsAmountUnit.setText(getString(R.string.money_unit));
				}
				
				mTxtCanUseBalance.setText(DataUtils.convertCurrencyFormat(financialDetail.getAvaliableBalance()));
				mEditFinancialAmount.setHint(getString(R.string.financial_mode_detail, DataUtils.convertToYuan(financialDetail.getLeastBuy()), DataUtils.convertToYuan(financialDetail.getMaxSubscribe())));

				setFinancialIncome();

			} else {
				AppUtils.handleErrorResponse(FinancialConfirmActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};
		
	private OnClickListener mBtnNextOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			confirmOrder();
		}
		
	};

	/**
	 * 确认支付
	 */
	private void confirmOrder() {
		MobclickAgent.onEvent(FinancialConfirmActivity.this, UmengTouchType.RP104_2);

		String amount = mEditFinancialAmount.getText().toString().trim();
		if (!checkAmount(amount)){
			return;
		}

		final Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("productId", mFinancialId);
		datas.put("amount", amount);

		if (mSelectMemberGift != null) {
			if(mCouponEnable) {
				datas.put("memberGiftId", mSelectMemberGift.getMemberGiftId());
			}
		}
		
		mProgressDialog.show();
		DdApplication.getLoanManager().confirmFinancialOrder(datas, mOnConfirmFinancialOrderFinishedListener);
	}
	
	private OnConfirmFinancialOrderFinishedListener mOnConfirmFinancialOrderFinishedListener = new OnConfirmFinancialOrderFinishedListener() {

		@Override
		public void onConfirmFinancialOrderFinished(Response response, Order order) {
			// TODO Auto-generated method stub
			mProgressDialog.dismiss();
			if (response.isSuccess()) {
				mPayOrder.setPayType(PayType.PAY);
				mPayOrder.setLeftPay(order.getLeftPay());
				mPayOrder.setBalancePay(order.getBalancePay());
				mPayOrder.setBankPay(order.getBankPay());
				mPayOrder.setBankName(order.getBankName());
				mPayOrder.setBankTailNum(order.getBankTailNum());
				mPayOrder.setBankLogo(order.getBankLogo());
				mPayOrder.setProductName(mFinancialDetail.getName());
				mPayOrder.setOnceLimit(order.getOnceLimit());
				mPayOrder.setDayLimit(order.getDayLimit());
				mPayOrder.setMonthLimit(order.getMonthLimit());
				//防止按鈕被连续点击两次
				if (isTouch) {
					isTouch = false;
					showPayDialog();
				}
			} else {
				AppUtils.handleErrorResponse(FinancialConfirmActivity.this, response);
			}
		}
		
	};
	
	protected void showPayDialog() {
		mPayDialog = new PayDialog(FinancialConfirmActivity.this, getDecorViewDialog());
		mPayDialog.setOnKeyListener(keyListener);
		mPayDialog.show();
	}
	
	protected View getDecorViewDialog() {
		// TODO Auto-generated method stub
		return PayView.getInstance(mPayOrder, this, new OnPayListener() {
			
			@Override
			public void onSurePay(String password) {
				// TODO Auto-generated method stub
				mPayDialog.dismiss();
				isTouch = true;
				mPayDialog=null;

				pay(password);
			}
			
			@Override
			public void onCancelPay() {
				// TODO Auto-generated method stub
				mPayDialog.dismiss();
				isTouch = true;
				mPayDialog=null;
			}
		}).getView();
	}
	//支付
	private void pay(String password) {
		String amount = mEditFinancialAmount.getText().toString().trim();

		final Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("productId", mFinancialId);
		datas.put("amount", amount);
		datas.put("transPwd", password);

		if (mSelectMemberGift != null) {
			if(mCouponEnable) {
				datas.put("memberGiftId", mSelectMemberGift.getMemberGiftId());
			}
		}
		
		mProgressDialog.show();
		DdApplication.getLoanManager().payFinancial(datas, mOnPayFinancialFinishedListener);
	}
	
	private OnPayFinancialFinishedListener mOnPayFinancialFinishedListener = new OnPayFinancialFinishedListener() {

		@Override
		public void onPayFinancialFinished(Response response, PayResult payResult) {
			// TODO Auto-generated method stub
			mProgressDialog.dismiss();
			if (response.isSuccess()) {

				Intent intent = new Intent(FinancialConfirmActivity.this,MyFinancialActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//创建新的界面
				intent.putExtra("payResult",payResult);
				startActivity(intent);
				finish();
			} else {
				PayUtils.dealPayError(FinancialConfirmActivity.this, response, mOnDealPayErrorFinishedListener);
			}
		}
		
	};
	
	private OnDealPayErrorFinishedListener mOnDealPayErrorFinishedListener = new OnDealPayErrorFinishedListener() {

		@Override
		public void OnDealPayErrorFinished(String dealCode) {
			// TODO Auto-generated method stub
			if(TextUtils.equals(dealCode, "tryAgain")) {
				showPayDialog();
			}
		}
		
	};
	
	private boolean checkAmount(String amountStr) {
		if(TextUtils.isEmpty(amountStr)) {
			AppUtils.showToast(this, R.string.loan_financial_amount_hint);
			return false;
		}

		Double amount = Double.parseDouble(amountStr);
		
		if(amount * 100 < mFinancialDetail.getLeastBuy()) {
			AppUtils.showToast(this, getString(R.string.loan_least_buy_hint, DataUtils.convertCurrencyFormat(mFinancialDetail.getLeastBuy())));
			return false;
		}
		
		if(amount * 100 > mFinancialDetail.getMaxSubscribe()) {
			AppUtils.showToast(this, getString(R.string.loan_max_subscribe, DataUtils.convertCurrencyFormat(mFinancialDetail.getMaxSubscribe())));
			return false;
		}
		
		if(0 != amount % mFinancialDetail.getMultiple()) {
			AppUtils.showToast(this, getString(R.string.loan_multiple_hint, mFinancialDetail.getMultiple()));
			return false;
		}
		return true;
	}

	/**
	 * 预估利息
	 */
	private void setFinancialIncome() {
		if (mFinancialDetail!=null) {
			double yearInterestValue = Double.parseDouble(mFinancialDetail.getYearInterest())/100;
			if (mSelectMemberGift!=null) {
				if (giftType == 3) {//加息红包
					//加息
					yearInterestValue = yearInterestValue + mSelectMemberGift.getGiftValue()/100;

				}
			}
			String amountStr = mEditFinancialAmount.getText().toString().trim();
			if(TextUtils.isEmpty(amountStr)) {
				amountStr = "0";
			}
			BigDecimal yearInterest = new BigDecimal(yearInterestValue);
			BigDecimal mAmount = new BigDecimal(Double.parseDouble(amountStr));
			BigDecimal year = new BigDecimal(365);
			BigDecimal days = new BigDecimal(mFinancialDetail.getLoanPeriodDays());
			mIncome = DataUtils.convertTwoDecimal(yearInterest.multiply(mAmount).divide(year,2).multiply(days).doubleValue());

		}

		if(mHasCoupon){
			String couponValue = "0";
			if(mSelectMemberGift != null) {
				if(mCouponEnable) {
					couponValue = DataUtils.convertCurrencyFormat(mSelectMemberGift.getGiftValue());
				}
			}
			//红包收益已包含在内
			mTxtFinancialIncome.setText(Html.fromHtml(getString(R.string.financial_income_coupon, mIncome)));
		} else {
			mTxtFinancialIncome.setText(Html.fromHtml(getString(R.string.financial_income, mIncome)));
		}
	}

	/**
	 *支付按钮
	 */
	private void setPayAmount() {
		String amountStr = mEditFinancialAmount.getText().toString().trim();
		
		if(TextUtils.isEmpty(amountStr)) {
			amountStr = "0";
			setNextBtn(false);
		} else {
			setNextBtn(true);
		}

		long amount = Long.parseLong(amountStr);
		double coupon = 0;
		
		if(mHasCoupon) {
			if(mSelectMemberGift != null) {
				if(mCouponEnable) {//红包选中
					if (giftType != 3) {//非加息红包
						coupon = mSelectMemberGift.getGiftValue();
					}
				}
			}
		}

		double leftPay = amount * 100 - coupon;
		if(leftPay < 0) {
			leftPay = 0;
		}

		mBtnNext.setText(getString(R.string.financial_pay, DataUtils.convertCurrencyFormat(leftPay)));

	}

	private void setNextBtn(boolean enable) {
		if(enable) {
			mBtnNext.setBackgroundResource(R.drawable.btn_red_solid_radius_bg);
			mBtnNext.setTextColor(getResources().getColor(R.color.white));
			mBtnNext.setClickable(true);
		} else {
			mBtnNext.setBackgroundResource(R.drawable.button_radius_disabled);
			mBtnNext.setTextColor(getResources().getColor(R.color.btn_text_disabled));
			mBtnNext.setClickable(false);
		}
	}
	private void selectGift() {
		String amountStr = mEditFinancialAmount.getText().toString().trim();
		if(TextUtils.isEmpty(amountStr)) {
			amountStr = "0";
		}
		long amount = Long.parseLong(amountStr);
		if (mMemberGiftList!=null) {
			usableMemberList.clear();
			for (MemberGift mMemberGift: mMemberGiftList) {
				long giftLimit = mMemberGift.getGiftLimit()/100;
				if (amount >= giftLimit) {
					usableMemberList.add(mMemberGift);
				}
			}
		}
		if (mInterestList!=null) {
			usableInterestList.clear();
			for (MemberGift mMemberGift: mInterestList) {
				long giftLimit = mMemberGift.getGiftLimit()/100;
				if (amount >= giftLimit) {
					usableInterestList.add(mMemberGift);
				}
			}
		}
	}

	/**
	 * dialog窗口返回键监听
	 */
	private DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
				isTouch = true;
			}
			return false;
		}
	};
}