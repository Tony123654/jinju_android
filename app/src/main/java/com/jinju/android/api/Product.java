package com.jinju.android.api;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable{
	private static final long serialVersionUID = 1L;
	private long mProductId;
	private double mInterestRate;//年化利率（实际年化+补贴年化）
	private String mActualInterestRate;//实际年化
	private String mSubsidyInterestRate;//补贴年化
	private String mName;
	private String mLoanPeriodDesc;
	private long mHasFundsAmount;
	private List<Tag> mTagList;
	private String mActivityTag;
	private int mHasPercent;
	private boolean mIsOnlyNewer;
	private long mBeginDuration;//距离开售时长
	private String mShowStatus;
	private long leastBuy;

	public long getLeastBuy() {
		return leastBuy;
	}

	public void setLeastBuy(long leastBuy) {
		this.leastBuy = leastBuy;
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

	public long getProductId() {
		return mProductId;
	}
	
	public void setProductId(long productId) {
		mProductId = productId;
	}
	
	public double getInterestRate() {
		return mInterestRate;
	}
	
	public void setInterestRate(double interestRate) {
		mInterestRate = interestRate;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getLoanPeriodDesc() {
		return mLoanPeriodDesc;
	}
	
	public void setLoanPeriodDesc(String loanPeriodDesc) {
		mLoanPeriodDesc = loanPeriodDesc;
	}
	
	public long getHasFundsAmount() {
		return mHasFundsAmount;
	}
	
	public void setHasFundsAmount(long hasFundsAmount) {
		mHasFundsAmount = hasFundsAmount;
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

	public int getHasPercent() {
		return mHasPercent;
	}

	public void setHasPercent(int hasPercent) {
		mHasPercent = hasPercent;
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

	public long getBeginDuration() {
		return mBeginDuration;
	}

	public void setBeginDuration(long mBeginDuration) {
		this.mBeginDuration = mBeginDuration;
	}
}