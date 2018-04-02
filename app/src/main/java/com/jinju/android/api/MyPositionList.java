package com.jinju.android.api;

import java.util.List;

public class MyPositionList {
	private long mTotalAsset;
	private long mTodayIncome;//今日收益
	private long mTotalOrderAmount;//累计投资
	private long mTotalIncome;//累计收益
	private List<ConfirmOrder> mConfirmOrderList;
	private List<ConfirmOrder> mMyPositionList;
	private Page mPage;
	
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
	
	public List<ConfirmOrder> getConfirmOrderList() {
		return mConfirmOrderList;
	}
	
	public void setConfirmOrderList(List<ConfirmOrder> confirmOrderList) {
		mConfirmOrderList = confirmOrderList;
	}

	public List<ConfirmOrder> getMyPositionList() {
		return mMyPositionList;
	}

	public void setMyPositionList(List<ConfirmOrder> myPositionList) {
		mMyPositionList = myPositionList;
	}

	public Page getPage() {
		return mPage;
	}
	
	public void setPage(Page page) {
		mPage = page;
	}

	public long getTotalOrderAmount() {
		return mTotalOrderAmount;
	}

	public void setTotalOrderAmount(long mTotalOrderAmount) {
		this.mTotalOrderAmount = mTotalOrderAmount;
	}
}