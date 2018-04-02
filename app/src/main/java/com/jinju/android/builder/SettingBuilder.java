package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Setting;

public class SettingBuilder {

	public static Setting build(JSONObject jsonObject) throws JSONException {
		Setting setting = new Setting();
		setting.setHeadImg(jsonObject.optString("headImg"));
		setting.setNick(jsonObject.optString("nick"));
		setting.setMemberId(jsonObject.optLong("memberId"));
		setting.setName(jsonObject.optString("name"));
		setting.setAuthStatus(jsonObject.optInt("authStatus"));
		setting.setMemberStep(jsonObject.optInt("memberStep"));
		setting.setMobile(jsonObject.optString("mobile"));
		setting.setAccount(jsonObject.optString("account"));
		return setting;
	}

}