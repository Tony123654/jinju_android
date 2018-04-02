package com.jinju.android.api;

import java.io.Serializable;
import java.util.List;

public class MemberGift implements Serializable{

	private static final long serialVersionUID = 1L;
	private long mMemberGiftId;
	private long mGiftLimit;

	private long mId;
	private int mGiftType;
	private double mGiftValue;
	private String mGiftName;
	private String mGiftTitle;
	private String mGmtCreate;
	private String mGmtExpire;
	private String mSourceDesc;
	private boolean mCanUse;
	private int mStatus;
	private List<GiftUsePoint>  mGiftUsePointList;

	public String getGiftTitle() {
		return mGiftTitle;
	}

	public void setGiftTitle(String mGiftTitle) {
		this.mGiftTitle = mGiftTitle;
	}

	public long getMemberGiftId() {
		return mMemberGiftId;
	}

	public void setMemberGiftId(long memberGiftId) {
		mMemberGiftId = memberGiftId;
	}

	public long getGiftLimit() {
		return mGiftLimit;
	}

	public void setGiftLimit(long giftLimit) {
		mGiftLimit = giftLimit;
	}

	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

	public int getGiftType() {
		return mGiftType;
	}

	public void setGiftType(int giftType) {
		mGiftType = giftType;
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
		return mCanUse;
	}

	public void setCanUse(boolean canUse) {
		mCanUse = canUse;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int status) {
		mStatus = status;
	}

	public List<GiftUsePoint> getGiftUsePointList() {
		return mGiftUsePointList;
	}

	public void setGiftUsePointList(List<GiftUsePoint> giftUsePointList) {
		mGiftUsePointList = giftUsePointList;
	}
}