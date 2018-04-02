package com.jinju.android.api;

import java.io.Serializable;
import java.util.List;

public class Financial implements Serializable{
	private static final long serialVersionUID = 1L;
	private long mId;
	private String mName;
	private int mProductStatus;
	private String mYearInterest;//年化利率（实际年化+补贴年化）
	private String mActualInterestRate;//实际年化
	private String mSubsidyInterestRate;//补贴年化
	private String mLoanPeriodDesc;
	private int mHasPercent;
	private long mHasFundsAmount;
	private String mType;
	private boolean mIsOnlyNewer;
	private String mShowStatus;
	private List<Tag> mTagList;
	private long mBeginDuration;//距离开售时长
	private boolean mIsRemindFlag;//是否开启开售提醒
	private String tsStartSubscribe;//设置开售时间（时间戳）
	private String mActivityTag;

	public String getTsStartSubscribe() {
		return tsStartSubscribe;
	}

	public void setTsStartSubscribe(String tsStartSubscribe) {
		this.tsStartSubscribe = tsStartSubscribe;
	}

	public boolean getIsRemindFlag() {
		return mIsRemindFlag;
	}

	public void setIsRemindFlag(boolean mIsRemindFlag) {
		this.mIsRemindFlag = mIsRemindFlag;
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

	public String getYearInterest() {
		return mYearInterest;
	}

	public void setYearInterest(String yearInterest) {
		mYearInterest = yearInterest;
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
	
	public String getType() {
		return mType;
	}
	
	public void setType(String type) {
		mType = type;
	}
	
	public boolean getIsOnlyNewer() {
		return mIsOnlyNewer;
	}
	
	public void setIsOnlyNewer(boolean isOnlyNewer) {
		mIsOnlyNewer = isOnlyNewer;
	}

	public String getShowStatus() {
		return mShowStatus;
	}

	public void setShowStatus(String showStatus) {
		mShowStatus = showStatus;
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

	public long getBeginDuration() {
		return mBeginDuration;
	}

	public void setBeginDuration(long mBeginDuration) {
		this.mBeginDuration = mBeginDuration;
	}

	@Override
	public String toString() {
		return "Financial{" +
				"mId=" + mId +
				", mName='" + mName + '\'' +
				", mProductStatus=" + mProductStatus +
				", mYearInterest='" + mYearInterest + '\'' +
				", mActualInterestRate='" + mActualInterestRate + '\'' +
				", mSubsidyInterestRate='" + mSubsidyInterestRate + '\'' +
				", mLoanPeriodDesc='" + mLoanPeriodDesc + '\'' +
				", mHasPercent=" + mHasPercent +
				", mHasFundsAmount=" + mHasFundsAmount +
				", mType='" + mType + '\'' +
				", mIsOnlyNewer=" + mIsOnlyNewer +
				", mShowStatus='" + mShowStatus + '\'' +
				", mTagList=" + mTagList +
				", mBeginDuration=" + mBeginDuration +
				", mIsRemindFlag=" + mIsRemindFlag +
				", tsStartSubscribe='" + tsStartSubscribe + '\'' +
				", mActivityTag='" + mActivityTag + '\'' +
				'}';
	}
}