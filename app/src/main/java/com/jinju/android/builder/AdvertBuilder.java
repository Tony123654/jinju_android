package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Advert;

public class AdvertBuilder {

	public static Advert build(JSONObject jsonObject) throws JSONException {
		Advert advert = new Advert();
		advert.setCode(jsonObject.optString("code"));
		advert.setImageUrl(jsonObject.optString("imgUrl"));
		advert.setLinkUrl(jsonObject.optString("linkUrl"));
		advert.setType(jsonObject.optString("type"));
		return advert;
	}

	public static List<Advert> buildList(JSONArray jsonArray) throws JSONException {
		List<Advert> advertList = new ArrayList<Advert>();
		if (jsonArray == null)
			return advertList;

		for (int i = 0; i < jsonArray.length(); i++)
			advertList.add(build(jsonArray.optJSONObject(i)));

		return advertList;
	}

}