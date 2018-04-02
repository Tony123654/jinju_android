package com.jinju.android.api;

import java.io.Serializable;

public class Advert implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mCode;
	private String mImageUrl;
	private String mLinkUrl;
	private String mType;

	public String getCode() {
		return mCode;
	}

	public void setCode(String code) {
		mCode = code;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}

	public String getLinkUrl() {
		return mLinkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		mLinkUrl = linkUrl;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		mType = type;
	}

}