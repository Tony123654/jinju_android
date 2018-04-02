package com.jinju.android.api;

import java.util.List;

public class PositionDetail {
	private long mPositionId;
	private long mProductId;
	private String mProductName;
	private long mTodayIncome;
	private long mYesterdayIncome;
	private long mPositionIncome;
	private long mPositionAmount;
	private String mYearInterest;
	private String mGmtEnd;
	private String mGmtFundReturn;
	private List<TradeHistory> mTradeHistoryList;
	private String mProductContractUrl;
	private String mTransferContractUrl;
	private boolean mCanTrans;

	public long getYesterdayIncome() {
		return mYesterdayIncome;
	}

	public void setYesterdayIncome(long mYesterdayIncome) {
		this.mYesterdayIncome = mYesterdayIncome;
	}

	public boolean getCanTrans() {
		return mCanTrans;
	}

	public void setCanTrans(boolean mCanTrans) {
		this.mCanTrans = mCanTrans;
	}


	public long getPositionId() {
		return mPositionId;
	}
	
	public void setPositionId(long positionId) {
		mPositionId = positionId;
	}
	
	public long getProductId() {
		return mProductId;
	}
	
	public void setProductId(long productId) {
		mProductId = productId;
	}
	
	public String getProductName() {
		return mProductName;
	}
	
	public void setProductName(String productName) {
		mProductName = productName;
	}
	
	public long getTodayIncome() {
		return mTodayIncome;
	}
	
	public void setTodayIncome(long todayIncome) {
		mTodayIncome = todayIncome;
	}
	
	public long getPositionIncome() {
		return mPositionIncome;
	}
	
	public void setPositionIncome(long positionIncome) {
		mPositionIncome = positionIncome;
	}
	
	public long getPositionAmount() {
		return mPositionAmount;
	}
	
	public void setPositionAmount(long positionAmount) {
		mPositionAmount = positionAmount;
	}
	
	public String getYearInterest() {
		return mYearInterest;
	}
	
	public void setYearInterest(String yearInterest) {
		mYearInterest = yearInterest;
	}
	
	public String getGmtEnd() {
		return mGmtEnd;
	}
	
	public void setGmtEnd(String gmtEnd) {
		mGmtEnd = gmtEnd;
	}
	
	public String getGmtFundReturn() {
		return mGmtFundReturn;
	}
	
	public void setGmtFundReturn(String gmtFundReturn) {
		mGmtFundReturn = gmtFundReturn;
	}
	
	public List<TradeHistory> getTradeHistoryList() {
		return mTradeHistoryList;
	}
	
	public void setTradeHistoryList(List<TradeHistory> tradeHistoryList) {
		mTradeHistoryList = tradeHistoryList;
	}
	
	public String getProductContractUrl() {
		return mProductContractUrl;
	}
	
	public void setProductContractUrl(String productContractUrl) {
		mProductContractUrl = productContractUrl;
	}
	
	public String getTransferContractUrl() {
		return mTransferContractUrl;
	}
	
	public void setTransferContractUrl(String transferContractUrl) {
		mTransferContractUrl = transferContractUrl;
	}
}