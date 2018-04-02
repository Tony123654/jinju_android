package com.jinju.android.api;

import java.io.Serializable;
import java.util.List;

public class Index implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Advert> mAdvertList;
	private List<Product> mProductList;
	private List<ButtonList> mTopButtonList;//顶部icon信息
	private List<ButtonList> mBottomSlideList;//底部icon信息
	private List<PopAd> mPopAdList;
	private List<HomeNotice> mNoticeList;
	private int mMemberStep;
	private String mSecurityMessage;//安全信息
	private String floatUrl;//浮动窗口url
	private String floatImg;
	private String floatType;
	private String totalFinancial;//平台交易金额（万）
	private String totalInterest;//用户累计赚取(万)
	private String dataLinkUrl;//平台数据
	private int stepType;//首页步骤 第一步（1.未注册。2未绑卡）第二步（3.理财列表）第三部（4模块消失）
	private boolean isDot;//是否有红包消息提示

	public boolean isDot() {
		return isDot;
	}

	public void setDot(boolean dot) {
		isDot = dot;
	}

	public String getDataLinkUrl() {
		return dataLinkUrl;
	}

	public void setDataLinkUrl(String dataLinkUrl) {
		this.dataLinkUrl = dataLinkUrl;
	}

	public int getStepType() {
		return stepType;
	}

	public void setStepType(int stepType) {
		this.stepType = stepType;
	}

	public String getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(String totalInterest) {
		this.totalInterest = totalInterest;
	}

	public String getTotalFinancial() {
		return totalFinancial;
	}

	public void setTotalFinancial(String totalFinancial) {
		this.totalFinancial = totalFinancial;
	}

	public List<ButtonList> getTopButtonList() {
		return mTopButtonList;
	}

	public void setTopButtonList(List<ButtonList> mTopButtonList) {
		this.mTopButtonList = mTopButtonList;
	}

	public List<ButtonList> getBottomSlideList() {
		return mBottomSlideList;
	}

	public void setBottomSlideList(List<ButtonList> mBottomSlideList) {
		this.mBottomSlideList = mBottomSlideList;
	}

	public String getFloatType() {
		return floatType;
	}

	public void setFloatType(String floatType) {
		this.floatType = floatType;
	}

	public String getFloatImg() {
		return floatImg;
	}

	public void setFloatImg(String floatImg) {
		this.floatImg = floatImg;
	}

	public String getFloatUrl() {
		return floatUrl;
	}

	public void setFloatUrl(String floatUrl) {
		this.floatUrl = floatUrl;
	}

	public List<HomeNotice> getNoticeList() {
		return mNoticeList;
	}

	public void setNoticeList(List<HomeNotice> noticeList) {
		mNoticeList = noticeList;
	}

	public String getSecurityMessage() {
		return mSecurityMessage;
	}

	public void setSecurityMessage(String mSecurityMessage) {
		this.mSecurityMessage = mSecurityMessage;
	}

	public List<Advert> getAdvertList() {
		return mAdvertList;
	}

	public void setAdvertList(List<Advert> advertList) {
		mAdvertList = advertList;
	}
	
	public void setProductList(List<Product> productList){
		mProductList = productList;
	}
	
	public List<Product> getProductList(){
		return mProductList;
	}
	
	public void setMemberStep(int memberStep) {
		mMemberStep = memberStep;
	}
	
	public int getMemberStep() {
		return mMemberStep;
	}

	public List<PopAd> getPopAdList() {
		return mPopAdList;
	}

	public void setPopAdList(List<PopAd> mPopAdList) {
		this.mPopAdList = mPopAdList;
	}

	@Override
	public String toString() {
		return "Index{" +
				"mAdvertList=" + mAdvertList +
				", mProductList=" + mProductList +
				", mPopAdList=" + mPopAdList +
				", mMemberStep=" + mMemberStep +
				'}';
	}
}