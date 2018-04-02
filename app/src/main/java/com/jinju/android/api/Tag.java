package com.jinju.android.api;

import java.io.Serializable;

public class Tag implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mTagName;
	private String mTagColor;
	private String mTagDescription;
	private String mTagImg;//标签图片（当为tagType为1时出现）
	private int mTagType;//标签类型（1：角标 2：标签）


	public String getTagImg() {
		return mTagImg;
	}

	public void setTagImg(String tagImg) {
		this.mTagImg = tagImg;
	}

	public int getTagType() {
		return mTagType;
	}

	public void setTagType(int tagType) {
		this.mTagType = tagType;
	}

	public String getTagName() {
		return mTagName;
	}
	
	public void setTagName(String tagName) {
		mTagName = tagName;
	}
	
	public String getTagColor() {
		return mTagColor;
	}
	
	public void setTagColor(String tagColor) {
		mTagColor = tagColor;
	}
	
	public String getTagDescription() {
		return mTagDescription;
	}
	
	public void setTagDescription(String tagDescription) {
		mTagDescription = tagDescription;
	}

	@Override
	public String toString() {
		return "Tag{" +
				"mTagName='" + mTagName + '\'' +
				", mTagColor='" + mTagColor + '\'' +
				", mTagDescription='" + mTagDescription + '\'' +
				", mTagImg='" + mTagImg + '\'' +
				", mTagType=" + mTagType +
				'}';
	}
}