package com.jinju.android.api;

import java.util.List;

public class VersionInfo {

	private boolean mNeedUpdate;
	private String mForceUpdate;
	private String mLastVersion;
	private String mDownloadUrl;
	private String mFileSize;//更新包大小
	private String mOsType;
	private String mGmtModify;
	private List<String> mChangeLogs;

	public String getFileSize() {
		return mFileSize;
	}

	public void setFileSize(String fileSize) {
		this.mFileSize = fileSize;
	}

	public String getForceUpdate() {
		return mForceUpdate;
	}

	public void setForceUpdate(String mForceUpdate) {
		this.mForceUpdate = mForceUpdate;
	}

	public boolean ismNeedUpdate() {
		return mNeedUpdate;
	}

	public void setmNeedUpdate(boolean mNeedUpdate) {
		this.mNeedUpdate = mNeedUpdate;
	}

	public boolean isNeedUpdate() {
		return mNeedUpdate;
	}
	public void setNeedUpdate(boolean needUpdate) {
		mNeedUpdate = needUpdate;
	}

	public String getLastVersion() {
		return mLastVersion;
	}

	public void setLastVersion(String lastVersion) {
		mLastVersion = lastVersion;
	}

	public String getDownloadUrl() {
		return mDownloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		mDownloadUrl = downloadUrl;
	}

	public String getOsType() {
		return mOsType;
	}

	public void setOsType(String osType) {
		mOsType = osType;
	}

	public String getGmtModify() {
		return mGmtModify;
	}

	public void setGmtModify(String gmtModify) {
		mGmtModify = gmtModify;
	}

	public List<String> getChangeLogs() {
		return mChangeLogs;
	}

	public void setChangeLogs(List<String> changeLogs) {
		mChangeLogs = changeLogs;
	}

}