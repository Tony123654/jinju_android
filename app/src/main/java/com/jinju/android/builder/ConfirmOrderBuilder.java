package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.ConfirmOrder;

/**
 * 	待完成订单
 */
public class ConfirmOrderBuilder {

	public static ConfirmOrder build(JSONObject jsonObject) throws JSONException {
		ConfirmOrder confirmOrder = new ConfirmOrder();
		confirmOrder.setProductId(jsonObject.optLong("productId"));
		confirmOrder.setProductName(jsonObject.optString("productName"));
		confirmOrder.setOrderCount(jsonObject.optLong("orderCount"));
		confirmOrder.setGmetStartInterest(jsonObject.optString("gmtStartInterest"));
		confirmOrder.setOrderAmount(jsonObject.optLong("orderAmount"));
		confirmOrder.setGmtCreate(jsonObject.optString("gmtCreate"));
		confirmOrder.setProductStatus(jsonObject.optString("productStatus"));
		confirmOrder.setGmtCreateTime(jsonObject.optLong("gmtCreateTime"));
		confirmOrder.setType(0);
		return confirmOrder;
	}

	public static List<ConfirmOrder> buildList(JSONArray jsonArray) throws JSONException {
		List<ConfirmOrder> confirmOrderList = new ArrayList<ConfirmOrder>();
		if (jsonArray == null)
			return confirmOrderList;

		for (int i = 0; i < jsonArray.length(); i++)
			confirmOrderList.add(build(jsonArray.optJSONObject(i)));

		return confirmOrderList;
	}

}