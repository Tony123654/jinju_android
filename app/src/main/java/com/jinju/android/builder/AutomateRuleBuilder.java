package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.AutomateRule;

public class AutomateRuleBuilder {

	public static AutomateRule build(JSONObject jsonObject) throws JSONException {
		AutomateRule automateRule = new AutomateRule();
		automateRule.setMaxPercent(jsonObject.optString("maxPercent"));
		automateRule.setCanUseAmount(jsonObject.optLong("canUseAmount"));
		automateRule.setStatus(jsonObject.optString("status"));
		automateRule.setMyQueueIndex(jsonObject.optString("myQueueIndex"));
		automateRule.setQueueCount(jsonObject.optString("queueCount"));
		automateRule.setMinMoneyYuan(jsonObject.optString("minMoneyYuan"));
		automateRule.setMinTimeLimit(jsonObject.optInt("minTimeLimit"));
		automateRule.setMaxTimeLimit(jsonObject.optInt("maxTimeLimit"));
		automateRule.setLimitMin(jsonObject.optInt("limitMin"));
		automateRule.setLimitMax(jsonObject.optInt("limitMax"));
		automateRule.setRetainFundYuan(jsonObject.optString("retainFundYuan"));
		automateRule.setYearRateMin(jsonObject.optString("yearRateMin"));
		automateRule.setYearRateMax(jsonObject.optString("yearRateMax"));
		automateRule.setSubTimeOptions(buildSubTimeOptionsList(jsonObject.optJSONArray("subTimeOptions")));
		return automateRule;
	}
	
	public static List<Integer> buildSubTimeOptionsList(JSONArray jsonArray) throws JSONException {
		List<Integer> subTimeOptionsList = new ArrayList<Integer>();
		if (jsonArray == null)
			return subTimeOptionsList;

		for (int i = 0; i < jsonArray.length(); i++)
			subTimeOptionsList.add(jsonArray.optInt(i));

		return subTimeOptionsList;
	}

}