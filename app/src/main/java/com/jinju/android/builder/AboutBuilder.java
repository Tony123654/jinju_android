package com.jinju.android.builder;

import com.jinju.android.api.About;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AboutBuilder {

	public static About build(JSONObject jsonObject) throws JSONException {
		About about = new About();
		about.setTitle(jsonObject.optString("title"));
		about.setLinkUrl(jsonObject.optString("linkUrl"));
		return about;
	}
	public static List<About> buildList(JSONArray jsonArray) throws JSONException {
		List<About> aboutList = new ArrayList<About>();
		if (jsonArray == null)
			return aboutList;

		for (int i = 0; i < jsonArray.length(); i++)
			aboutList.add(build(jsonArray.optJSONObject(i)));

		return aboutList;
	}

}