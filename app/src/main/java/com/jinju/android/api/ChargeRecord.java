package com.jinju.android.api;

public class ChargeRecord {

	private String mBankName;
	private String mStatusDesc;
	private long mPayAmount;
	private String mGmtGroupTime;	//充值年月
	private String mGmtCharge;
	private Boolean mStatuType =false;

	public Boolean getStatuType() {
		return mStatuType;
	}

	public void setStatuType(Boolean mStatuType) {
		this.mStatuType = mStatuType;
	}

	public String getBankName() {
		return mBankName;
	}

	public void setBankName(String bankName) {
		mBankName = bankName;
	}

	public String getStatusDesc() {
		return mStatusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		mStatusDesc = statusDesc;
	}

	public long getPayAmount() {
		return mPayAmount;
	}

	public void setPayAmount(long payAmount) {
		mPayAmount = payAmount;
	}

	public String getGmtCharge() {
		return mGmtCharge;
	}

	public void setGmtCharge(String gmtCharge) {
		mGmtCharge = gmtCharge;
	}

	public String getGmtGroupTime(){return mGmtGroupTime;}

	public void setGmtGroupTime(String gmtGroupTime){mGmtGroupTime = gmtGroupTime;}

}