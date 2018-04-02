package com.jinju.android.api;

import java.io.Serializable;
import java.util.List;

public class PayResult implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<String> activityDesc;
	private String activityUrl;
	private String shareUrl;

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public List<String> getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(List<String> activityDesc) {
		this.activityDesc = activityDesc;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}


}