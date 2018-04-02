package com.jinju.android.api;

import java.io.Serializable;

public class Function implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String mFuncNo;
	private String mTitle;
	private String mSubTitle;
	private int mOrder;

	public void setFuncNo(String funcNo){
		mFuncNo = funcNo;
	}
	
	public String getFuncNo(){
		return mFuncNo;
	}
	
	public void setTitle(String title){
		mTitle = title;
	}
	
	public String getTitle(){
		return mTitle;
	}
	
	public void setSubTitle(String subTitle){
		mSubTitle = subTitle;
	}
	
	public String getSubTitle(){
		return mSubTitle;
	}
	
	public void setOrder(int order){
		mOrder = order;
	}
	
	public int getOrder(){
		return mOrder;
	}
}