package com.jinju.android.api;

public class MyAsset {

	private long mCurrentBalance;//账户余额
	private long mCurrentPosition;				//理财金额
	private long mTotalPosition;//理财资产
	private long mNetAssets;//我的总资产
	private long mCanUseBalance;				//可用余额
	private long mPositionFrezen;//投标冻结
	private long mOtherFrezen;//其他冻结
	private long mFrozenBalance;				//冻结金额

	public long getFrozenBalance() {
		return mFrozenBalance;
	}

	public void setFrozenBalance(long mFrozenBalance) {
		this.mFrozenBalance = mFrozenBalance;
	}

	public long getTotalPosition() {
		return mTotalPosition;
	}

	public void setTotalPosition(long mTotalPosition) {
		this.mTotalPosition = mTotalPosition;
	}

	public long getCurrentBalance() {
		return mCurrentBalance;
	}

	public void setCurrentBalance(long currentBalance) {
		mCurrentBalance = currentBalance;
	}

	public long getCurrentPosition() {
		return mCurrentPosition;
	}

	public void setCurrentPosition(long currentPosition) {
		mCurrentPosition = currentPosition;
	}

	public long getNetAssets() {
		return mNetAssets;
	}

	public void setNetAssets(long netAssets) {
		mNetAssets = netAssets;
	}

	public long getCanUseBalance() {
		return mCanUseBalance;
	}

	public void setCanUseBalance(long canUseBalance) {
		mCanUseBalance = canUseBalance;
	}

	public long getPositionFrezen() {
		return mPositionFrezen;
	}

	public void setPositionFrezen(long positionFrezen) {
		mPositionFrezen = positionFrezen;
	}

	public long getOtherFrezen() {
		return mOtherFrezen;
	}

	public void setOtherFrezen(long otherFrezen) {
		mOtherFrezen = otherFrezen;
	}
	


}