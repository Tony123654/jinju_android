package com.jinju.android.api;

public class FastPay{
	private long mMemberBankId;
	private String mBankName;
	private String mBankCardNum;
	private String mLogoPath;
	private long mOnceLimit;
	private long mMonthLimit;
	private long mDayLimit;
	private String mCanUseBalance;

	public String getCanUseBalance() {
		return mCanUseBalance;
	}

	public void setCanUseBalance(String mCanUseBalance) {
		this.mCanUseBalance = mCanUseBalance;
	}
	public long getMemberBankId() {
		return mMemberBankId;
	}

	public void setMemberBankId(long memberBankId) {
		mMemberBankId = memberBankId;
	}

	public String getBankName() {
		return mBankName;
	}

	public void setBankName(String bankName) {
		mBankName = bankName;
	}

	public String getBankCardNum() {
		return mBankCardNum;
	}

	public void setBankCardNum(String bankCardNum) {
		mBankCardNum = bankCardNum;
	}

	public String getLogoPath() {
		return mLogoPath;
	}

	public void setLogoPath(String logoPath) {
		mLogoPath = logoPath;
	}

	public long getOnceLimit() {
		return mOnceLimit;
	}

	public void setOnceLimit(long onceLimit) {
		mOnceLimit = onceLimit;
	}

	public long getMonthLimit() {
		return mMonthLimit;
	}

	public void setMonthLimit(long monthLimit) {
		mMonthLimit = monthLimit;
	}

	public long getDayLimit() {
		return mDayLimit;
	}

	public void setDayLimit(long mDayLimit) {
		this.mDayLimit = mDayLimit;
	}
}

