package com.jinju.android.api;

public class Position {
	private String mStockName;
	private String mStockCode;
	private long mStockAmount;
	private long mEnableAmount;
	private double mCostPrice;
	private double mLastPrice;
	private double mStockAsset;
	private double mIncomeBalance;
	private double mFloatPercent;
	private boolean mOpenEntrust;
	
	public String getStockName() {
		return mStockName;
	}
	
	public void setStockName(String stockName) {
		mStockName = stockName;
	}
	
	public String getStockCode() {
		return mStockCode;
	}
	
	public void setStockCode(String stockCode) {
		mStockCode = stockCode;
	}
	
	public long getStockAmount() {
		return mStockAmount;
	}
	
	public void setStockAmount(long stockAmount) {
		mStockAmount = stockAmount;
	}
	
	public long getEnableAmount() {
		return mEnableAmount;
	}
	
	public void setEnableAmount(long enableAmount) {
		mEnableAmount = enableAmount;
	}
	
	public double getCostPrice() {
		return mCostPrice;
	}
	
	public void setCostPrice(double costPrice) {
		mCostPrice = costPrice;
	}
	
	public double getLastPrice() {
		return mLastPrice;
	}
	
	public void setLastPrice(double lastPrice) {
		mLastPrice = lastPrice;
	}
	
	public double getStockAsset() {
		return mStockAsset;
	}
	
	public void setStockAsset(double stockAsset) {
		mStockAsset = stockAsset;
	}
	
	public double getIncomeBalance() {
		return mIncomeBalance;
	}
	
	public void setIncomeBalance(double incomeBalance) {
		mIncomeBalance = incomeBalance;
	}

	public double getFloatPercent() {
		return mFloatPercent;
	}

	public void setFloatPercent(double floatPercent) {
		mFloatPercent = floatPercent;
	}

	public boolean isOpenEntrust() {
		return mOpenEntrust;
	}

	public void setOpenEntrust(boolean openEntrust) {
		mOpenEntrust = openEntrust;
	}
	
	public void toggleOpen() {
		mOpenEntrust = !mOpenEntrust;
	}

}