package com.jinju.android.api;

public class Page {

	private int mPageSize;//每页数据条数
	private int mCurrentPage;//当前页数
	private int mTotalPage;//总页数

	public int getPageSize() {
		return mPageSize;
	}

	public void setPageSize(int pageSize) {
		mPageSize = pageSize;
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public void setCurrentPage(int currentPage) {
		mCurrentPage = currentPage;
	}

	public int getTotalPage() {
		return mTotalPage;
	}

	public void setTotalPage(int totalPage) {
		mTotalPage = totalPage;
	}

}