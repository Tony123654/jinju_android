package com.jinju.android.api;

import java.io.Serializable;

public class Bank implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String mBankCode;
	private String mBankName;
	private String mLogoPath;
	private long mPayLimitOnce;
	private long mPayLimitDay;
	private long mPayLimitMonth;

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

	public String getLogoPath() {
		return mLogoPath;
	}

	public void setLogoPath(String logoPath) {
		mLogoPath = logoPath;
	}
	
	public long getPayLimitOnce() {
		return mPayLimitOnce;
	}
	
	public void setPayLimitOnce(long payLimitOnce) {
		mPayLimitOnce = payLimitOnce;
	}
	
	public long getPayLimitDay() {
		return mPayLimitDay;
	}
	
	public void setPayLimitDay(long payLimitDay) {
		mPayLimitDay = payLimitDay;
	}
	
	public long getPayLimitMonth() {
		return mPayLimitMonth;
	}
	
	public void setPayLimitMonth(long payLimitMonth) {
		mPayLimitMonth = payLimitMonth;
	}

}