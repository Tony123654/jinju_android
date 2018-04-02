package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Position;


public class PositionBuilder {

	public static Position build(JSONObject jsonObject) throws JSONException {
		Position position = new Position();
		position.setStockName(jsonObject.optString("stockName"));
		position.setStockCode(jsonObject.optString("stockCode"));
		position.setStockAmount(jsonObject.optLong("stockAmount"));
		position.setEnableAmount(jsonObject.optLong("enableAmount"));
		position.setCostPrice(jsonObject.optDouble("costPrice"));
		position.setLastPrice(jsonObject.optDouble("lastPrice"));
		position.setStockAsset(jsonObject.optDouble("stockAsset"));
		position.setIncomeBalance(jsonObject.optDouble("incomeBalance"));
		position.setFloatPercent(jsonObject.optDouble("floatPercent"));
		position.setOpenEntrust(false);
		return position;
	}

	public static List<Position> buildList(JSONArray jsonArray) throws JSONException {
		List<Position> positionList = new ArrayList<Position>();
		if (jsonArray == null)
			return positionList;

		for (int i = 0; i < jsonArray.length(); i++)
			positionList.add(build(jsonArray.optJSONObject(i)));

		return positionList;
	}

}