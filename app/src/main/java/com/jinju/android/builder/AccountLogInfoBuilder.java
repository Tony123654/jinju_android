package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.AccountLogInfo;

public class AccountLogInfoBuilder {

	public static AccountLogInfo build(JSONObject jsonObject) throws JSONException {
		AccountLogInfo accountLogInfo = new AccountLogInfo();
		accountLogInfo.setPage(PageBuilder.build(jsonObject));

		accountLogInfo.setSubTransCodes(CategoryBuilder.buildList(jsonObject.optJSONArray("subTransCodes")));
		accountLogInfo.setAccountLogList(AccountLogBuilder.buildList(jsonObject.optJSONArray("logList")));
		return accountLogInfo;
	}

}