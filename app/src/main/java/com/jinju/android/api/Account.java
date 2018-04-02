package com.jinju.android.api;

public class Account {

	private String mAccount;
	private String mMemberId;
	private String mAuthStatus;
	private int mWithCount;
	private int mGiftCount;
	private int mPositionCount;
	private long mCurrBalance;
	private long mNetAssets;
	private long mPositionAssets;
	private int mMemberStep;
	private String mAboutusUrl;
	private long mTodayIncome;
	private long mTotalIncome;
	private long mCanUseBalance;
	private int newMessageFlag;
	private String inviteLocation;
	private String redPacketDesc;//红包描述
	private long mYesterdayIncome;	//昨日收益
	private String mInviteRecord;		//邀请记录
	private int isValid;//类别

	public long getYesterdayIncome() {
		return mYesterdayIncome;
	}

	public void setYesterdayIncome(long mYesterdayIncome) {
		this.mYesterdayIncome = mYesterdayIncome;
	}

	public String getInviteRecord() {
		return mInviteRecord;
	}

	public void setInviteRecord(String mInviteRecord) {
		this.mInviteRecord = mInviteRecord;
	}

	public String getRedPacketDesc() {
		return redPacketDesc;
	}

	public void setRedPacketDesc(String redPacketDesc) {
		this.redPacketDesc = redPacketDesc;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public String getInviteLocation() {
		return inviteLocation;
	}

	public void setInviteLocation(String inviteLocation) {
		this.inviteLocation = inviteLocation;
	}

	public int getNewMessageFlag() {
		return newMessageFlag;
	}

	public void setNewMessageFlag(int newMessageFlag) {
		this.newMessageFlag = newMessageFlag;
	}

	public long getTotalIncome() {
		return mTotalIncome;
	}

	public void setTotalIncome(long totalIncome) {
		mTotalIncome = totalIncome;
	}

	public long getTodayIncome() {
		return mTodayIncome;
	}

	public void setTodayIncome(long todayIncome) {
		mTodayIncome = todayIncome;
	}

	public String getAccount() {
		return mAccount;
	}

	public void setAccount(String account) {
		mAccount = account;
	}

	public String getMemberId() {
		return mMemberId;
	}

	public void setMemberId(String memberId) {
		mMemberId = memberId;
	}

	public String getAuthStatus() {
		return mAuthStatus;
	}

	public void setAuthStatus(String authStatus) {
		mAuthStatus = authStatus;
	}

	public int getWithCount() {
		return mWithCount;
	}

	public void setWithCount(int withCount) {
		mWithCount = withCount;
	}

	public int getGiftCount() {
		return mGiftCount;
	}

	public void setGiftCount(int giftCount) {
		mGiftCount = giftCount;
	}

	public int getPositionCount() {
		return mPositionCount;
	}

	public void setPositionCount(int positionCount) {
		mPositionCount = positionCount;
	}

	public long getCurrBalance() {
		return mCurrBalance;
	}

	public void setCurrBalance(long currBalance) {
		mCurrBalance = currBalance;
	}

	public long getNetAssets() {
		return mNetAssets;
	}

	public void setNetAssets(long netAssets) {
		mNetAssets = netAssets;
	}
	
	public long getPositionAssets() {
		return mPositionAssets;
	}
	
	public void setPositionAssets(long positionAssets) {
		mPositionAssets = positionAssets;
	} 
	
	public int getMemberStep() {
		return mMemberStep;
	}
	
	public void setMemberStep(int memberStep) {
		mMemberStep = memberStep;
	}

	public String getAboutusUrl() {
		return mAboutusUrl;
	}
	
	public void setAboutsuUrl(String aboutusUrl) {
		mAboutusUrl = aboutusUrl;
	}
	
	public long getCanUseBalance() {
		return mCanUseBalance;
	}
	
	public void setCanUseBalance(long canUseBalance) {
		mCanUseBalance = canUseBalance;
	}
}