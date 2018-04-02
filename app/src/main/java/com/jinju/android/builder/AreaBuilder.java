package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Area;

public class AreaBuilder {

	public static Area build(JSONObject jsonObject) throws JSONException {
		Area area = new Area();
		area.setName(jsonObject.optString("name"));
		area.setValue(jsonObject.optString("value"));
		return area;
	}

	public static List<Area> buildList(JSONArray jsonArray) throws JSONException {
		List<Area> areaList = new ArrayList<Area>();
		if (jsonArray == null)
			return areaList;

		for (int i = 0; i < jsonArray.length(); i++)
			areaList.add(build(jsonArray.optJSONObject(i)));

		return areaList;
	}

}