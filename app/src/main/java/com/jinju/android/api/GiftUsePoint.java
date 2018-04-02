package com.jinju.android.api;

import java.io.Serializable;

public class GiftUsePoint implements Serializable{

	private String mCode;
	private String mDesc;
	private String mUseCondition;

	public String getmUseCondition() {
		return mUseCondition;
	}

	public void setmUseCondition(String mUseCondition) {
		this.mUseCondition = mUseCondition;
	}

	public String getCode() {
		return mCode;
	}

	public void setCode(String code) {
		mCode = code;
	}

	public String getDesc() {
		return mDesc;
	}

	public void setDesc(String desc) {
		mDesc = desc;
	}

	@Override
	public String toString() {
		return "GiftUsePoint{" +
				"mCode='" + mCode + '\'' +
				", mDesc='" + mDesc + '\'' +
				", mUseCondition='" + mUseCondition + '\'' +
				'}';
	}
}