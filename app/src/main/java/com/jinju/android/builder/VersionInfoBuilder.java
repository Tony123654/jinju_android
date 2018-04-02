package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.VersionInfo;

public class VersionInfoBuilder {

	public static VersionInfo build(JSONObject jsonObject) throws JSONException {
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setNeedUpdate(jsonObject.optBoolean("needUpdate"));
		versionInfo.setLastVersion(jsonObject.optString("lastVersion"));
		versionInfo.setDownloadUrl(jsonObject.optString("downloadUrl"));
		versionInfo.setOsType(jsonObject.optString("osType"));
		versionInfo.setGmtModify(jsonObject.optString("gmtModify"));
		versionInfo.setForceUpdate(jsonObject.optString("forceUpdate"));
		versionInfo.setFileSize(jsonObject.optString("fileSize"));
		versionInfo.setChangeLogs(CommonBuilder.buildStringList(jsonObject.optJSONArray("changeLogs")));
		return versionInfo;
	}

}