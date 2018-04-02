package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.AccountLog;

public class AccountLogBuilder {

	public static AccountLog build(JSONObject jsonObject) throws JSONException {
		AccountLog accountLog = new AccountLog();
		accountLog.setGmtCreate(jsonObject.optString("gmtCreate"));
		accountLog.setTransDesc(jsonObject.optString("transDesc"));
		accountLog.setTransAmount(jsonObject.optLong("transAmount"));
		accountLog.setTransType(jsonObject.optString("transType"));
		accountLog.setCanUseBalance(jsonObject.optLong("canUseBalance"));
		return accountLog;
	}

	public static List<AccountLog> buildList(JSONArray jsonArray) throws JSONException {
		List<AccountLog> accountLogList = new ArrayList<AccountLog>();
		if (jsonArray == null)
			return accountLogList;

		for (int i = 0; i < jsonArray.length(); i++)
			accountLogList.add(build(jsonArray.optJSONObject(i)));

		return accountLogList;
	}

}