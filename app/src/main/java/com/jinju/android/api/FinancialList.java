package com.jinju.android.api;

import java.util.List;

public class FinancialList {

	private int mSubCount;
	private int mTransCount;
	private long mTotalAsset;
	private long mTodayIncome;
	private long mTotalIncome;
	private String floatUrl;
	private String floatImg;
	private String floatType;
	private Page mPage;
	private List<Financial> mFinancialList;//精选标
	private List<Financial> mFinancialOutList;//已售罄标
	private List<Financial> mActivityItems;//活动标
	private List<Financial> privilegeItems;//特权标
	private int repayment;//已还款数量
	private int sellOut;//已售罄数量

	public int getRepayment() {
		return repayment;
	}

	public void setRepayment(int repayment) {
		this.repayment = repayment;
	}

	public int getSellOut() {
		return sellOut;
	}

	public void setSellOut(int sellOut) {
		this.sellOut = sellOut;
	}

	public String getFloatType() {
		return floatType;
	}

	public void setFloatType(String floatType) {
		this.floatType = floatType;
	}

	public String getFloatUrl() {
		return floatUrl;
	}

	public void setFloatUrl(String floatUrl) {
		this.floatUrl = floatUrl;
	}

	public String getFloatImg() {
		return floatImg;
	}

	public void setFloatImg(String floatImg) {
		this.floatImg = floatImg;
	}

	public int getSubCount() {
		return mSubCount;
	}

	public void setSubCount(int subCount) {
		mSubCount = subCount;
	}

	public int getTransCount() {
		return mTransCount;
	}

	public void setTransCount(int transCount) {
		mTransCount = transCount;
	}
	
	public long getTotalAsset() {
		return mTotalAsset;
	}
	
	public void setTotalAsset(long totalAsset) {
		mTotalAsset = totalAsset;
	}
	
	public long getTodayIncome() {
		return mTodayIncome;
	}
	
	public void setTodayIncome(long todayIncome) {
		mTodayIncome = todayIncome;
	}
	
	public long getTotalIncome() {
		return mTotalIncome;
	}
	
	public void setTotalIncome(long totalIncome) {
		mTotalIncome = totalIncome;
	}

	public Page getPage() {
		return mPage;
	}

	public void setPage(Page page) {
		mPage = page;
	}

	public List<Financial> getFinancialList() {
		return mFinancialList;
	}

	public void setFinancialList(List<Financial> financialList) {
		mFinancialList = financialList;
	}

	public List<Financial> getFinancialOutList() {
		return mFinancialOutList;
	}

	public void setFinancialOutList(List<Financial> mFinancialOutList) {
		this.mFinancialOutList = mFinancialOutList;
	}

	public List<Financial> getActivityItems() {
		return mActivityItems;
	}

	public void setActivityItems(List<Financial> mActivityItems) {
		this.mActivityItems = mActivityItems;
	}

	public List<Financial> getPrivilegeItems() {
		return privilegeItems;
	}

	public void setPrivilegeItems(List<Financial> privilegeItems) {
		this.privilegeItems = privilegeItems;
	}
}