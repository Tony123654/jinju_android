package com.jinju.android.api;

import java.util.List;

public class WithdrawDetail {

	private long mCurrBalance;
	private long mCanUseBalance;
	private String mMobile;
	private long mMemberBankId;
	private String mBankName;
	private String mCardNoTail;
	private String mLogoPath;
	private String mNote;
	private String mPoundageDesc;
	private int mPoundageType;
	private String mPoundageValue;
	private List<String> mWarmPrompt;

	public List<String> getWarmPrompt() {
		return mWarmPrompt;
	}

	public void setWarmPrompt(List<String> mWarmPrompt) {
		this.mWarmPrompt = mWarmPrompt;
	}

	public String getPoundageDesc() {
		return mPoundageDesc;
	}

	public void setPoundageDesc(String mPoundageDesc) {
		this.mPoundageDesc = mPoundageDesc;
	}

	public int getPoundageType() {
		return mPoundageType;
	}

	public void setPoundageType(int mPoundageType) {
		this.mPoundageType = mPoundageType;
	}

	public String getPoundageValue() {
		return mPoundageValue;
	}

	public void setPoundageValue(String mPoundageValue) {
		this.mPoundageValue = mPoundageValue;
	}

	public long getCurrBalance() {
		return mCurrBalance;
	}

	public void setCurrBalance(long currBalance) {
		mCurrBalance = currBalance;
	}

	public long getCanUseBalance() {
		return mCanUseBalance;
	}

	public void setCanUseBalance(long canUseBalance) {
		mCanUseBalance = canUseBalance;
	}

	public String getMobile() {
		return mMobile;
	}

	public void setMobile(String mobile) {
		mMobile = mobile;
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
	
	public String getCardNoTail() {
		return mCardNoTail;
	}
	
	public void setCardNoTail(String cardNoTail) {
		mCardNoTail = cardNoTail;
	}
	
	public String getLogoPath() {
		return mLogoPath;
	}
	
	public void setLogoPath(String logoPath) {
		mLogoPath = logoPath;
	}

	public String getNote() {
		return mNote;
	}

	public void setNote(String mNote) {
		this.mNote = mNote;
	}
}