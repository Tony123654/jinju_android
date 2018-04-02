package com.jinju.android.api;


public class TradeHistory{
	private String mTradeTime;
	private String mStatusDesc;
	private String mGiftAddition;
	private long mTradeValue;
	
	public String getTradeTime() {
		return mTradeTime;
	}
	
	public void setTradeTime(String tradeTime) {
		mTradeTime = tradeTime;
	}
	
	public String getStatusDesc() {
		return mStatusDesc;
	}
	
	public void setStatusDesc(String statusDesc) {
		mStatusDesc = statusDesc;
	}
	
	public long getTradeValue() {
		return mTradeValue;
	}
	
	public void setTradeValue(long tradeValue) {
		mTradeValue = tradeValue;
	}

	public String getGiftAddition() {
		return mGiftAddition;
	}

	public void setGiftAddition(String giftAddition) {
		this.mGiftAddition = giftAddition;
	}
}