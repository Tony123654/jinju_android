package com.jinju.android.api;

import java.util.List;

public class AuthCardDetail {

	private long mMemberBankId;
	private String mCardNoTail;
	private String mTel;
	private String mAccount;
	private String mIdCardNumber;
	private List<Bank> mBankList;
	
	public long getMemberBankId() {
		return mMemberBankId;
	}
	
	public void setMemberBankId(long memberBankId) {
		mMemberBankId = memberBankId;
	}
	
	public String getCardNoTail() {
		return mCardNoTail;
	}
	
	public void setCardNoTail(String cardNoTail) {
		mCardNoTail = cardNoTail;
	}
	
	public String getTel() {
		return mTel;
	}
	
	public void setTel(String tel) {
		mTel = tel;
	}
	
	public String getAccount() {
		return mAccount;
	}
	
	public void setAccount(String account) {
		mAccount = account;
	}
	
	public String getIdCardNumber() {
		return mIdCardNumber;
	}
	
	public void setIdCardNumber(String idCardNumber) {
		mIdCardNumber = idCardNumber;
	}
	
	public List<Bank> getBankList() {
		return mBankList;
	}

	public void setBankList(List<Bank> bankList) {
		mBankList = bankList;
	}
}