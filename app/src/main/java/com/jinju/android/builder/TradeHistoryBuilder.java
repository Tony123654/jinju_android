package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.TradeHistory;


public class TradeHistoryBuilder {

	public static TradeHistory build(JSONObject jsonObject) throws JSONException {
		TradeHistory tradeHistory = new TradeHistory();
		tradeHistory.setTradeTime(jsonObject.optString("tradeTime"));
		tradeHistory.setStatusDesc(jsonObject.optString("statusDesc"));
		tradeHistory.setTradeValue(jsonObject.optLong("tradeValue"));
		tradeHistory.setGiftAddition(jsonObject.optString("giftAddition"));

		return tradeHistory;
	}

	public static List<TradeHistory> buildList(JSONArray jsonArray) throws JSONException {
		List<TradeHistory> tradeHistoryList = new ArrayList<TradeHistory>();
		if (jsonArray == null)
			return tradeHistoryList;

		for (int i = 0; i < jsonArray.length(); i++)
			tradeHistoryList.add(build(jsonArray.optJSONObject(i)));

		return tradeHistoryList;
	}

}