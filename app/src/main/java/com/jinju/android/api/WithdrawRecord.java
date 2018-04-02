package com.jinju.android.api;

public class WithdrawRecord {

	private String mBankName;
	private String mCardNoTail;
	private String mStatusDesc;
	private long mTransAmount;
	private String mGmtApply;//提现日期（待处理）
	private String mGmtToBank;//提交到银行时间
	private String mGmtApplyFormat;
	private Boolean mStateType =false;//判断是否显示 条目 正在的进行状态
	private String mStatus;
	private String mGmtDate;//提现完成时间/失败时间/取消时间

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public String getGmtDate() {
		return mGmtDate;
	}

	public void setGmtDate(String mGmtDate) {
		this.mGmtDate = mGmtDate;
	}

	public String getGmtToBank() {
		return mGmtToBank;
	}

	public void setGmtToBank(String mGmtToBank) {
		this.mGmtToBank = mGmtToBank;
	}

	public Boolean getStateType() {
		return mStateType;
	}

	public void setStateType(Boolean mStateType) {
		this.mStateType = mStateType;
	}

	public String getBankName() {
		return mBankName;
	}

	public void setBankName(String bankName) {
		mBankName = bankName;
	}

	public String getCardNoTail() {
		return mCardNoTail;
	}

	public void setCardNoTail(String cardNoTail) {
		mCardNoTail = cardNoTail;
	}

	public String getStatusDesc() {
		return mStatusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		mStatusDesc = statusDesc;
	}

	public long getTransAmount() {
		return mTransAmount;
	}

	public void setTransAmount(long transAmount) {
		mTransAmount = transAmount;
	}

	public String getGmtApply() {
		return mGmtApply;
	}

	public void setGmtApply(String gmtApply) {
		mGmtApply = gmtApply;
	}

	public String getGmtApplyFormat(){return mGmtApplyFormat;}

	public void setGmtApplyFormat(String gmtApplyFormat){mGmtApplyFormat = gmtApplyFormat;}

}