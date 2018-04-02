package com.jinju.android.api;

public class ConfirmOrder implements Comparable<ConfirmOrder>{
	//募集中
	private long mProductId;
	private String mProductName;
	private long mOrderCount;
	private String mGmtStartInterest;
	private long mOrderAmount;
	private String mGmtCreate;//该产品最后一笔订单创建时间
	private long mGmtCreateTime;


	//持仓中
	private long mPositionId;
	private long mCurrPosition;
	private long mTotalIncome;//已收利息
	private long mExpectTotalIncome;//预计收益
	private String mStatus;
	private String mStatusDesc;
	private String mGmtTrade;//产品持仓时间
	private long mGmtTradeTime;
	private String mProductStatus;//产品投资状态

	private int type;//判断是什么类型，0是募集中，1是 即将到期


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getGmtCreateTime() {
		return mGmtCreateTime;
	}

	public void setGmtCreateTime(long mGmtCreateTime) {
		this.mGmtCreateTime = mGmtCreateTime;
	}

	public String getProductStatus() {
		return mProductStatus;
	}

	public void setProductStatus(String mProductStatus) {
		this.mProductStatus = mProductStatus;
	}

	public String getGmtCreate() {
		return mGmtCreate;
	}

	public void setGmtCreate(String mGmtCreate) {
		this.mGmtCreate = mGmtCreate;
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
	
	public long getOrderCount() {
		return mOrderCount;
	}
	
	public void setOrderCount(long orderCount) {
		mOrderCount = orderCount;
	}
	
	public String getGmtStartInterest() {
		return mGmtStartInterest;
	}
	
	public void setGmetStartInterest(String gmtStartInterest) {
		mGmtStartInterest = gmtStartInterest;
	}
	
	public long getOrderAmount() {
		return mOrderAmount;
	}
	
	public void setOrderAmount(long orderAmount) {
		mOrderAmount = orderAmount;
	}


	public long getGmtTradeTime() {
		return mGmtTradeTime;
	}

	public void setGmtTradeTime(long mGmtTradeTime) {
		this.mGmtTradeTime = mGmtTradeTime;
	}


	public String getGmtTrade() {
		return mGmtTrade;
	}

	public void setGmtTrade(String mGmtTrade) {
		this.mGmtTrade = mGmtTrade;
	}

	public long getPositionId() {
		return mPositionId;
	}

	public void setPositionId(long positionId) {
		mPositionId = positionId;
	}


	public long getCurrPosition() {
		return mCurrPosition;
	}

	public void setCurrPosition(long currPosition) {
		mCurrPosition = currPosition;
	}

	public long getTotalIncome() {
		return mTotalIncome;
	}

	public void setTotalIncome(long totalIncome) {
		mTotalIncome = totalIncome;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		mStatus = status;
	}

	public String getStatusDesc() {
		return mStatusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		mStatusDesc = statusDesc;
	}

	public long getExpectTotalIncome() {
		return mExpectTotalIncome;
	}

	public void setExpectTotalIncome(long mExpectTotalIncome) {
		this.mExpectTotalIncome = mExpectTotalIncome;
	}

	@Override
	public int compareTo(ConfirmOrder o) {

		long time1 = this.getType()==0?this.getGmtCreateTime():this.getGmtTradeTime();
		long time2 = o.getType()==0?o.getGmtCreateTime():o.getGmtTradeTime();


		String day1 = this.getType()==0?this.getGmtCreate():this.getGmtTrade();
		String day2 = o.getType()==0?o.getGmtCreate():o.getGmtTrade();

		if (day1.equals(day2)) {//如果同一天 ，位置不变
			if (this.getType()==0&&o.getType()==0) {//同一天，同一个类型 按时间排序
				return time1 > time2?1 : -1;//按时间排序
			}else if (this.getType()==0&&o.getType()==1) {
				return 0;
			} else if (this.getType()==1&&o.getType()==1) {
				return time1 > time2?1 : -1;//按时间排序
			} else if (this.getType()==1&&o.getType()==0) {
				return 0;//按时间排序
			}

		}
		return time1 > time2?1 : -1;//按时间排序

	}
}