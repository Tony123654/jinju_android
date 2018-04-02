package com.jinju.android.api;

public class Setting {
	private String mHeadImg;
	private String account;//账号（手机号）
	private String mNick;
	private long mMemberId;
	private String mName;
	private int mAuthStatus;
	private int mMemberStep;
	private String mMobile;//手机号（简略）

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getHeadImg() {
		return mHeadImg;
	}
	
	public void setHeadImg(String headImg) {
		mHeadImg = headImg;
	}
	
	public String getNick() {
		return mNick;
	}
	
	public void setNick(String nick) {
		mNick = nick;
	}
	
	public long getMemberId() {
		return mMemberId;
	}
	
	public void setMemberId(long memberId) {
		mMemberId = memberId;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public int getAuthStatus() {
		return mAuthStatus;
	}
	
	public void setAuthStatus(int authStatus) {
		mAuthStatus = authStatus;
	}
	
	public int getMemberStep() {
		return mMemberStep;
	}
	
	public void setMemberStep(int memberStep) {
		mMemberStep = memberStep;
	}
	
	public String getMobile() {
		return mMobile;
	}
	
	public void setMobile(String mobile) {
		mMobile = mobile;
	}
}