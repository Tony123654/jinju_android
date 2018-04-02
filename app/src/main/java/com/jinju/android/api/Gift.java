package com.jinju.android.api;

import java.util.List;

public class Gift {

	private long mMemberGiftId;
	private double mGiftValue;
	private String mGiftName;
	private String mGiftTitle;
	private String mGmtCreate;
	private String mGmtExpire;
	private String mSourceDesc;
	private boolean mIsCanUse;


	private boolean mIsOpen;
	private int mGiftType;
	private int mStatus;
	private List<GiftUsePoint> mGiftUsePointList;

	public String getGiftTitle() {
		return mGiftTitle;
	}

	public void setGiftTitle(String mGiftTitle) {
		this.mGiftTitle = mGiftTitle;
	}

	public int getmGiftType() {
		return mGiftType;
	}

	public void setmGiftType(int mGiftType) {
		this.mGiftType = mGiftType;
	}
	public long getMemberGiftId() {
		return mMemberGiftId;
	}
	public int getmStatus() {
		return mStatus;
	}

	public void setmStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	public void setMemberGiftId(long memberGiftId) {
		mMemberGiftId = memberGiftId;
	}
	public double getGiftValue() {
		return mGiftValue;
	}

	public void setGiftValue(double giftValue) {
		mGiftValue = giftValue;
	}

	public String getGiftName() {
		return mGiftName;
	}

	public void setGiftName(String giftName) {
		mGiftName = giftName;
	}

	public String getGmtCreate() {
		return mGmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		mGmtCreate = gmtCreate;
	}

	public String getGmtExpire() {
		return mGmtExpire;
	}

	public void setGmtExpire(String gmtExpire) {
		mGmtExpire = gmtExpire;
	}

	public String getSourceDesc() {
		return mSourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		mSourceDesc = sourceDesc;
	}

	public boolean isCanUse() {
		return mIsCanUse;
	}

	public void setCanUse(boolean isCanUse) {
		mIsCanUse = isCanUse;
	}

	public boolean isOpen() {
		return mIsOpen;
	}

	public void setOpen(boolean isOpen) {
		mIsOpen = isOpen;
	}

	public void toggleOpen() {
		mIsOpen = !mIsOpen;
	}

	public List<GiftUsePoint> getGiftUsePointList() {
		return mGiftUsePointList;
	}

	public void setGiftUsePointList(List<GiftUsePoint> giftUsePointList) {
		mGiftUsePointList = giftUsePointList;
	}

}