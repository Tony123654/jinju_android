package com.jinju.android.api;

public class MyBank {

	private String mId;
	private String mBankCode;
	private String mBankName;
	private String mStatus;
	private String mStatusDesc;
	private String mCardNoTail;
	private String mLogoPath;
	private int mFastPayStatus;
	private String onceLimit;//单笔限额
	private String dayLimit;//每日限额
	private String name;//姓名

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDayLimit() {
		return dayLimit;
	}

	public void setDayLimit(String dayLimit) {
		this.dayLimit = dayLimit;
	}

	public String getOnceLimit() {
		return onceLimit;
	}

	public void setOnceLimit(String onceLimit) {
		this.onceLimit = onceLimit;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getBankCode() {
		return mBankCode;
	}

	public void setBankCode(String bankCode) {
		mBankCode = bankCode;
	}

	public String getBankName() {
		return mBankName;
	}

	public void setBankName(String bankName) {
		mBankName = bankName;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		mStatus = status;
	}

	public String getStatusDesc() {
		return mStatusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		mStatusDesc = statusDesc;
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
	
	public int getFastPayStatus() {
		return mFastPayStatus;
	}
	
	public void setFastPayStatus(int fastPayStatus) {
		mFastPayStatus = fastPayStatus;
	}

}