package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.MyPositionList;

public class MyPositionListBuilder {

	public static MyPositionList build(JSONObject jsonObject) throws JSONException {
		MyPositionList myPositionList = new MyPositionList();
		myPositionList.setTotalAsset(jsonObject.optLong("totalAsset"));
		myPositionList.setTodayIncome(jsonObject.optLong("todayIncome"));
		myPositionList.setTotalOrderAmount(jsonObject.optLong("totalOrderAmount"));
		myPositionList.setTotalIncome(jsonObject.optLong("totalIncome"));
		myPositionList.setConfirmOrderList(ConfirmOrderBuilder.buildList(jsonObject.optJSONArray("confirmOrders")));
		myPositionList.setMyPositionList(MyPositionBuilder.buildList(jsonObject.optJSONArray("positionList")));
		myPositionList.setPage(PageBuilder.build(jsonObject));
		return myPositionList;
	}
}
