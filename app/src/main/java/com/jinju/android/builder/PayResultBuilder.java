package com.jinju.android.builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.PayResult;

import java.util.ArrayList;
import java.util.List;

public class PayResultBuilder {

	public static PayResult build(JSONObject jsonObject) throws JSONException {
		PayResult payResult = new PayResult();
		payResult.setActivityDesc(buildList(jsonObject.optJSONArray("activityDesc")));
		payResult.setActivityUrl(jsonObject.optString("activityUrl"));
		payResult.setShareUrl(jsonObject.optString("shareUrl"));
		return payResult;
	}
	private static List<String> buildList(JSONArray jsonArray) {
		List<String>  list = new ArrayList<String>();
		if (jsonArray == null)
			return list;

		for (int i = 0; i < jsonArray.length(); i++)
			list.add((jsonArray.optJSONObject(i).optString("activityMsg")));

		return list;
	}
}