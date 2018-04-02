package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.ConfirmOrder;

/**
 * 持有中
 */
public class MyPositionBuilder {

	public static ConfirmOrder build(JSONObject jsonObject) throws JSONException {
		ConfirmOrder mConfirmOrder = new ConfirmOrder();
		mConfirmOrder.setPositionId(jsonObject.optLong("positionId"));
		mConfirmOrder.setProductId(jsonObject.optLong("productId"));
		mConfirmOrder.setProductName(jsonObject.optString("productName"));
		mConfirmOrder.setCurrPosition(jsonObject.optLong("currPosition"));
		mConfirmOrder.setTotalIncome(jsonObject.optLong("totalIncome"));
		mConfirmOrder.setStatus(jsonObject.optString("status"));
		mConfirmOrder.setStatusDesc(jsonObject.optString("statusDesc"));
		mConfirmOrder.setExpectTotalIncome(jsonObject.optLong("expectTotalIncome"));
		mConfirmOrder.setGmtTrade(jsonObject.optString("gmtTrade"));
		mConfirmOrder.setProductStatus(jsonObject.optString("productStatus"));
		mConfirmOrder.setGmtTradeTime(jsonObject.optLong("gmtTradeTime"));
		mConfirmOrder.setType(1);
		return mConfirmOrder;
	}

	public static List<ConfirmOrder> buildList(JSONArray jsonArray) throws JSONException {
		List<ConfirmOrder> myPositionList = new ArrayList<ConfirmOrder>();
		if (jsonArray == null)
			return myPositionList;

		for (int i = 0; i < jsonArray.length(); i++)
			myPositionList.add(build(jsonArray.optJSONObject(i)));

		return myPositionList;
	}

}