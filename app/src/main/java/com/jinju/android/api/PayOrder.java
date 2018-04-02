package com.jinju.android.api;

public class PayOrder {
	private int mPayType;
	private long mLeftPay;
	private long mBalancePay;
	private long mBankPay;
	private String mProductName;
	private long mOnceLimit;
	private long mDayLimit;
	private long mMonthLimit;
	private String mBankLogo;
	private String mBankName;
	private String mBankTailNum;
	
	public int getPayType() {
		return mPayType;
	}
	
	public void setPayType(int payType) {
		mPayType = payType;
	}
	
	public long getLeftPay() {
		return mLeftPay;
	}
	
	public void setLeftPay(long leftPay) {
		mLeftPay = leftPay;
	}
	
	public long getBalancePay() {
		return mBalancePay;
	}
	
	public void setBalancePay(long balancePay) {
		mBalancePay = balancePay;
	}
	
	public long getBankPay() {
		return mBankPay;
	}
	
	public void setBankPay(long bankPay) {
		mBankPay = bankPay;
	}
	
	public String getBankLogo() {
		return mBankLogo;
	}
	
	public void setBankLogo(String bankLogo) {
		mBankLogo = bankLogo;
	}
	
	public String getBankName() {
		return mBankName;
	}
	
	public void setBankName(String bankName) {
		mBankName = bankName;
	}
	
	public String getBankTailNum() {
		return mBankTailNum;
	}
	
	public void setBankTailNum(String bankTailNum) {
		mBankTailNum = bankTailNum;
	}

	public String getProductName() {
		return mProductName;
	}

	public void setProductName(String productName) {
		mProductName = productName;
	}

	public long getOnceLimit() {
		return mOnceLimit;
	}

	public void setOnceLimit(long onceLimit) {
		mOnceLimit = onceLimit;
	}

	public long getDayLimit() {
		return mDayLimit;
	}

	public void setDayLimit(long dayLimit) {
		mDayLimit = dayLimit;
	}

	public long getMonthLimit() {
		return mMonthLimit;
	}

	public void setMonthLimit(long monthLimit) {
		mMonthLimit = monthLimit;
	}
}