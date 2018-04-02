package com.jinju.android.api;

import java.util.List;

public class AccountLogInfo {

	private Page mPage;
	private List<AccountLog> mAccountLogList;
	private List<Category> mSubTransCodes;//交易类型列表

	public Page getPage() {
		return mPage;
	}

	public void setPage(Page page) {
		mPage = page;
	}

	public List<AccountLog> getAccountLogList() {
		return mAccountLogList;
	}

	public void setAccountLogList(List<AccountLog> accountLogList) {
		mAccountLogList = accountLogList;
	}

	public List<Category> getSubTransCodes() {
		return mSubTransCodes;
	}

	public void setSubTransCodes(List<Category> subTransCodes) {
		mSubTransCodes = subTransCodes;
	}
}