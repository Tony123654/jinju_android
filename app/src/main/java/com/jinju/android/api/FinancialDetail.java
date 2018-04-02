package com.jinju.android.api;

import java.util.List;

public class FinancialDetail {

	private long mId;
	private String mName;
	private int mProductStatus;
	private long mProductAmount;
	private String mYearInterest;//年化利率（实际年化+补贴年化）
	private String mActualInterestRate;//实际年化;
	private String mSubsidyInterestRate;//补贴年化
	private long mProductPerPayInterest;
	private String mLoanPeriodDesc;
	private int mHasPercent;
	private long mHasFundsAmount;
	private long mAvaliableBalance;
	private long mAsset;
	private long mActualAmount;
	private long mDeposit;
	private long mLeastBuy;
	private long mMultiple;
	private long mMostBuy;
	private long mMemberGiftId;
	private List<MemberGift> mMemberGiftList;//普通红包
	private List<MemberGift> interestList;//加息红包
	private String mInterestPayTypeDesc;
	private String mType;
	private String mStartDate;
	private boolean mIsOnlyNewer;
	private String mSubscribeBeginTime;
	private String mSubscribeEndTime;
	private String mEndDate;
	private String mFundReturnDate;
	private int mLoanPeriodDays;
	private double mDayInterestRate;
	private String mSecurityDescUrl;
	private String mIntroductionUrl;
	private String mContractUrl;
	private String mAuditUrl;
	private String mSubscribeUrl;
	private String mFundReturnTypeDesc;
	private long mMaxSubscribe;
	private String mContractTitle;
	private String mShowStatus;
	private int mMemberStep;
	private List<Tag> mTagList;
	private String mActivityTag;
	private String mInterestRateUnit;
	private long mBeginDuration;
	private String mToday;//今日投资
	private String investUrl;//投资有礼
	private ActivityAwardInfo activityAwardInfo;//活动奖励信息
	private int orderCount;//投资记录总数
	private boolean joinActivity0011;//是否包标

	public boolean isJoinActivity0011() {
		return joinActivity0011;
	}

	public void setJoinActivity0011(boolean joinActivity0011) {
		this.joinActivity0011 = joinActivity0011;
	}

	public List<MemberGift> getInterestList() {
		return interestList;
	}

	public void setInterestList(List<MemberGift> interestList) {
		this.interestList = interestList;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public String getInvestUrl() {
		return investUrl;
	}

	public void setInvestUrl(String investUrl) {
		this.investUrl = investUrl;
	}

	public ActivityAwardInfo getActivityAwardInfo() {
		return activityAwardInfo;
	}

	public void setActivityAwardInfo(ActivityAwardInfo activityAwardInfo) {
		this.activityAwardInfo = activityAwardInfo;
	}

	public String getToday() {
		return mToday;
	}

	public void setToday(String mToday) {
		this.mToday = mToday;
	}

	public String getSubsidyInterestRate() {
		return mSubsidyInterestRate;
	}

	public void setSubsidyInterestRate(String mSubsidyInterestRate) {
		this.mSubsidyInterestRate = mSubsidyInterestRate;
	}

	public String getActualInterestRate() {
		return mActualInterestRate;
	}

	public void setActualInterestRate(String mActualInterestRate) {
		this.mActualInterestRate = mActualInterestRate;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getProductStatus() {
		return mProductStatus;
	}

	public void setProductStatus(int productStatus) {
		mProductStatus = productStatus;
	}

	public long getProductAmount() {
		return mProductAmount;
	}

	public void setProductAmount(long productAmount) {
		mProductAmount = productAmount;
	}

	public String getYearInterest() {
		return mYearInterest;
	}

	public void setYearInterest(String yearInterest) {
		mYearInterest = yearInterest;
	}

	public long getProductPerPayInterest() {
		return mProductPerPayInterest;
	}

	public void setProductPerPayInterest(long productPerPayInterest) {
		mProductPerPayInterest = productPerPayInterest;
	}

	public String getLoanPeriodDesc() {
		return mLoanPeriodDesc;
	}

	public void setLoanPeriodDesc(String loanPeriodDesc) {
		mLoanPeriodDesc = loanPeriodDesc;
	}

	public int getHasPercent() {
		return mHasPercent;
	}

	public void setHasPercent(int hasPercent) {
		mHasPercent = hasPercent;
	}

	public long getHasFundsAmount() {
		return mHasFundsAmount;
	}

	public void setHasFundsAmount(long hasFundsAmount) {
		mHasFundsAmount = hasFundsAmount;
	}

	public long getAvaliableBalance() {
		return mAvaliableBalance;
	}

	public void setAvaliableBalance(long avaliableBalance) {
		mAvaliableBalance = avaliableBalance;
	}

	public long getAsset() {
		return mAsset;
	}

	public void setAsset(long asset) {
		mAsset = asset;
	}

	public long getActualAmount() {
		return mActualAmount;
	}

	public void setActualAmount(long actualAmount) {
		mActualAmount = actualAmount;
	}

	public long getDeposit() {
		return mDeposit;
	}

	public void setDeposit(long deposit) {
		mDeposit = deposit;
	}

	public long getLeastBuy() {
		return mLeastBuy;
	}

	public void setLeastBuy(long leastBuy) {
		mLeastBuy = leastBuy;
	}

	public long getMultiple() {
		return mMultiple;
	}

	public void setMultiple(long multiple) {
		mMultiple = multiple;
	}

	public long getMostBuy() {
		return mMostBuy;
	}

	public void setMostBuy(long mostBuy) {
		mMostBuy = mostBuy;
	}

	public long getMemberGiftId() {
		return mMemberGiftId;
	}

	public void setMemberGiftId(long memberGiftId) {
		mMemberGiftId = memberGiftId;
	}

	public List<MemberGift> getMemberGiftList() {
		return mMemberGiftList;
	}

	public void setMemberGiftList(List<MemberGift> memberGiftList) {
		mMemberGiftList = memberGiftList;
	}

	public String getInterestPayTypeDesc() {
		return mInterestPayTypeDesc;
	}

	public void setInterestPayTypeDesc(String interestPayTypeDesc) {
		mInterestPayTypeDesc = interestPayTypeDesc;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

	public String getStartDate() {
		return mStartDate;
	}

	public void setStartDate(String startDate) {
		mStartDate = startDate;
	}

	public boolean getIsOnlyNewer() {
		return mIsOnlyNewer;
	}

	public void setIsOnlyNewer(boolean isOnlyNewer) {
		mIsOnlyNewer = isOnlyNewer;
	}

	public String getSubscribeBeginTime() {
		return mSubscribeBeginTime;
	}

	public void setSubscribeBeginTime(String subscribeBeginTime) {
		mSubscribeBeginTime = subscribeBeginTime;
	}

	public String getSubscribeEndTime() {
		return mSubscribeEndTime;
	}

	public void setSubscribeEndTime(String subscribeEndTime) {
		mSubscribeEndTime = subscribeEndTime;
	}
	
	public String getEndDate() {
		return mEndDate;
	}
	
	public void setEndDate(String endDate) {
		mEndDate = endDate;
	}
	
	public String getFundReturnDate() {
		return mFundReturnDate;
	}
	
	public void setFundReturnDate(String fundReturnDate) {
		mFundReturnDate = fundReturnDate;
	}
	
	public int getLoanPeriodDays() {
		return mLoanPeriodDays;
	}
	
	public void setLoanPeriodDays(int loanPeriodDays) {
		mLoanPeriodDays = loanPeriodDays;
	}
	
	public double getDayInterestRate() {
		return mDayInterestRate;
	}
	
	public void setDayInterestRate(double dayInterestRate) {
		mDayInterestRate = dayInterestRate;
	}
	
	public String getSecurityDescUrl() {
		return mSecurityDescUrl;
	}

	public void setSecurityDescUrl(String securityDescUrl) {
		mSecurityDescUrl = securityDescUrl;
	}

	public String getIntroductionUrl() {
		return mIntroductionUrl;
	}

	public void setIntroductionUrl(String introductionUrl) {
		mIntroductionUrl = introductionUrl;
	}

	public String getContractUrl() {
		return mContractUrl;
	}

	public void setContractUrl(String contractUrl) {
		mContractUrl = contractUrl;
	}
	
	public String getAuditUrl() {
		return mAuditUrl;
	}
	
	public void setAuditUrl(String auditUrl) {
		mAuditUrl = auditUrl;
	}
	
	public String getSubscribeUrl() {
		return mSubscribeUrl;
	}
	
	public void setSubscribeUrl(String subscribeUrl) {
		mSubscribeUrl = subscribeUrl;
	}

	public String getFundReturnTypeDesc() {
		return mFundReturnTypeDesc;
	}

	public void setFundReturnTypeDesc(String fundReturnTypeDesc) {
		mFundReturnTypeDesc = fundReturnTypeDesc;
	}
	
	public long getMaxSubscribe() {
		return mMaxSubscribe;
	}
	
	public void setMaxSubscribe(long maxSubscribe) {
		mMaxSubscribe = maxSubscribe;
	}

	public String getContractTitle() {
		return mContractTitle;
	}

	public void setContractTitle(String contractTitle) {
		mContractTitle = contractTitle;
	}

	public String getShowStatus() {
		return mShowStatus;
	}

	public void setShowStatus(String showStatus) {
		mShowStatus = showStatus;
	}
	
	public int getMemberStep() {
		return mMemberStep;
	}
	
	public void setMemberStep(int memberStep) {
		mMemberStep = memberStep;
	}
	
	public List<Tag> getTagList() {
		return mTagList;
	}
	
	public void setTagList(List<Tag> tagList) {
		mTagList = tagList;
	}
	
	public String getActivityTag() {
		return mActivityTag;
	}

	public void setActivityTag(String activityTag) {
		mActivityTag = activityTag;
	}
	
	public String getInterestRateUnit() {
		return mInterestRateUnit;
	}
	
	public void setInterestRateUnit(String interestRateUnit) {
		mInterestRateUnit = interestRateUnit;
	}

	public long getBeginDuration() {
		return mBeginDuration;
	}

	public void setBeginDuration(long mBeginDuration) {
		this.mBeginDuration = mBeginDuration;
	}
}