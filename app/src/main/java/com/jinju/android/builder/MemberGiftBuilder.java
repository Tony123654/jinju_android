package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.MemberGift;

public class MemberGiftBuilder {

	public static MemberGift build(JSONObject jsonObject) throws JSONException {
		MemberGift memberGift = new MemberGift();
		memberGift.setId(jsonObject.optLong("id"));
		memberGift.setGiftLimit(jsonObject.optLong("giftLimit"));
		memberGift.setMemberGiftId(jsonObject.optLong("memberGiftId"));
		memberGift.setGiftType(jsonObject.optInt("giftType"));
		memberGift.setGiftValue(jsonObject.optDouble("giftValue"));
		memberGift.setGiftName(jsonObject.optString("giftName"));
		memberGift.setGiftTitle(jsonObject.optString("giftTitle"));
		memberGift.setGmtCreate(jsonObject.optString("gmtCreate"));
		memberGift.setGmtExpire(jsonObject.optString("gmtExpire"));
		memberGift.setSourceDesc(jsonObject.optString("sourceDesc"));
		memberGift.setCanUse(jsonObject.optBoolean("canUse"));
		memberGift.setStatus(jsonObject.optInt("status"));
		memberGift.setGiftUsePointList(GiftUsePointBuilder.buildList(jsonObject.optJSONArray("giftUsePoints")));
		return memberGift;
	}

	public static List<MemberGift> buildList(JSONArray jsonArray) throws JSONException {
		List<MemberGift> memberGiftList = new ArrayList<MemberGift>();
		if (jsonArray == null)
			return memberGiftList;

		for (int i = 0; i < jsonArray.length(); i++)
			memberGiftList.add(build(jsonArray.optJSONObject(i)));

		return memberGiftList;
	}

}