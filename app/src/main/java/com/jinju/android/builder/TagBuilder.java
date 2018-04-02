package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Tag;


public class TagBuilder {

	public static Tag build(JSONObject jsonObject) throws JSONException {
		Tag tag = new Tag();
		tag.setTagName(jsonObject.optString("tagName"));
		tag.setTagColor(jsonObject.optString("tagColor"));
		tag.setTagDescription(jsonObject.optString("tagDescription"));
		tag.setTagImg(jsonObject.optString("tagImg"));
		tag.setTagType(jsonObject.optInt("tagType"));
		return tag;
	}

	public static List<Tag> buildList(JSONArray jsonArray) throws JSONException {
		List<Tag> tagList = new ArrayList<Tag>();
		if (jsonArray == null)
			return tagList;

		for (int i = 0; i < jsonArray.length(); i++)
			tagList.add(build(jsonArray.optJSONObject(i)));

		return tagList;
	}

}