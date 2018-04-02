package com.jinju.android.api;

import java.util.List;

public class AutomateRule {
	private String mMaxPercent;
	private long mCanUseAmount;
	private String mStatus;
	private String mMyQueueIndex;
	private String mQueueCount;
	private String mMinMoneyYuan;
	private int mMinTimeLimit;
	private int mMaxTimeLimit;
	private int mLimitMin;
	private int mLimitMax;
	private String mRetainFundYuan;
	private String mYearRateMin;
	private String mYearRateMax;
	private List<Integer> mSubTimeOptions;
	
	public String getMaxPercent() {
		return mMaxPercent;
	}

	public void setMaxPercent(String maxPercent) {
		mMaxPercent = maxPercent;
	}

	public long getCanUseAmount() {
		return mCanUseAmount;
	}

	public void setCanUseAmount(long canUseAmount) {
		mCanUseAmount = canUseAmount;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		mStatus = status;
	}

	public String getMyQueueIndex() {
		return mMyQueueIndex;
	}

	public void setMyQueueIndex(String myQueueIndex) {
		mMyQueueIndex = myQueueIndex;
	}

	public String getQueueCount() {
		return mQueueCount;
	}

	public void setQueueCount(String queueCount) {
		mQueueCount = queueCount;
	}

	public String getMinMoneyYuan() {
		return mMinMoneyYuan;
	}

	public void setMinMoneyYuan(String minMoneyYuan) {
		mMinMoneyYuan = minMoneyYuan;
	}

	public int getMinTimeLimit() {
		return mMinTimeLimit;
	}

	public void setMinTimeLimit(int minTimeLimit) {
		mMinTimeLimit = minTimeLimit;
	}

	public int getMaxTimeLimit() {
		return mMaxTimeLimit;
	}

	public void setMaxTimeLimit(int maxTimeLimit) {
		mMaxTimeLimit = maxTimeLimit;
	}

	public int getLimitMin() {
		return mLimitMin;
	}

	public void setLimitMin(int limitMin) {
		mLimitMin = limitMin;
	}

	public int getLimitMax() {
		return mLimitMax;
	}

	public void setLimitMax(int limitMax) {
		mLimitMax = limitMax;
	}

	public String getRetainFundYuan() {
		return mRetainFundYuan;
	}

	public void setRetainFundYuan(String retainFundYuan) {
		mRetainFundYuan = retainFundYuan;
	}

	public String getYearRateMin() {
		return mYearRateMin;
	}

	public void setYearRateMin(String yearRateMin) {
		mYearRateMin = yearRateMin;
	}

	public String getYearRateMax() {
		return mYearRateMax;
	}

	public void setYearRateMax(String yearRateMax) {
		mYearRateMax = yearRateMax;
	}
	
	public List<Integer> getSubTimeOptions() {
		return mSubTimeOptions;
	}
	
	public void setSubTimeOptions(List<Integer> subTimeOptions) {
		mSubTimeOptions = subTimeOptions;
	}

}