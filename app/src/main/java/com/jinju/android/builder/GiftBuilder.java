package com.jinju.android.builder;

import com.jinju.android.api.Gift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GiftBuilder {

	public static Gift build(JSONObject jsonObject) throws JSONException {
		Gift gift = new Gift();
		gift.setMemberGiftId(jsonObject.optLong("memberGiftId"));
		gift.setmStatus(jsonObject.optInt("status"));
		gift.setGiftValue(jsonObject.optDouble("giftValue"));
		gift.setGiftName(jsonObject.optString("giftName"));
		gift.setGiftTitle(jsonObject.optString("giftTitle"));
		gift.setGmtCreate(jsonObject.optString("gmtCreate"));
		gift.setGmtExpire(jsonObject.optString("gmtExpire"));
		gift.setSourceDesc(jsonObject.optString("sourceDesc"));
		gift.setCanUse(jsonObject.optBoolean("canUse"));
		gift.setmGiftType(jsonObject.optInt("giftType"));
		gift.setGiftUsePointList(GiftUsePointBuilder.buildList(jsonObject.optJSONArray("giftUsePoints")));
		return gift;
	}

	public static List<Gift> buildList(JSONArray jsonArray) throws JSONException {
		List<Gift> giftList = new ArrayList<Gift>();
		if (jsonArray == null)
			return giftList;

		for (int i = 0; i < jsonArray.length(); i++)
			giftList.add(build(jsonArray.optJSONObject(i)));

		return giftList;
	}

}