package com.jinju.android.builder;

import com.jinju.android.api.GiftUsePoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiftUsePointBuilder {

	public static GiftUsePoint build(JSONObject jsonObject) throws JSONException {
		GiftUsePoint giftUsePoint = new GiftUsePoint();
		giftUsePoint.setCode(jsonObject.optString("code"));
		giftUsePoint.setDesc(jsonObject.optString("desc"));
		giftUsePoint.setmUseCondition(jsonObject.optString("useCondition"));
		return giftUsePoint;
	}

	public static List<GiftUsePoint> buildList(JSONArray jsonArray) throws JSONException {
		List<GiftUsePoint> giftUsePointList = new ArrayList<GiftUsePoint>();
		if (jsonArray == null)
			return giftUsePointList;

		for (int i = 0; i < jsonArray.length(); i++)
			giftUsePointList.add(build(jsonArray.optJSONObject(i)));

		return giftUsePointList;
	}

}