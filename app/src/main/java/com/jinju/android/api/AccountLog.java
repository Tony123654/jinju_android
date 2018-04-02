package com.jinju.android.api;

public class AccountLog {

	private String mGmtCreate;//时间
	private String mTransDesc;//描述
	private long mTransAmount;//金额
	private String mTransType;//交易类型
	private long mCanUseBalance;//可用余额

	public String getGmtCreate() {
		return mGmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		mGmtCreate = gmtCreate;
	}

	public String getTransDesc() {
		return mTransDesc;
	}

	public void setTransDesc(String transDesc) {
		mTransDesc = transDesc;
	}

	public long getTransAmount() {
		return mTransAmount;
	}

	public void setTransAmount(long transAmount) {
		mTransAmount = transAmount;
	}

	public String getTransType() {
		return mTransType;
	}

	public void setTransType(String transType) {
		mTransType = transType;
	}

	public long getCanUseBalance() {
		return mCanUseBalance;
	}

	public void setCanUseBalance(long mCanUseBalance) {
		this.mCanUseBalance = mCanUseBalance;
	}
}