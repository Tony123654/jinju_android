package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Category;

public class CategoryBuilder {

	public static Category build(JSONObject jsonObject) throws JSONException {
		Category category = new Category();
		category.setCode(jsonObject.optString("code"));
		category.setName(jsonObject.optString("name"));

		return category;
	}

	public static List<Category> buildList(JSONArray jsonArray) throws JSONException {
		List<Category> categoryList = new ArrayList<Category>();
		if (jsonArray == null)
			return categoryList;

		for (int i = 0; i < jsonArray.length(); i++)
			categoryList.add(build(jsonArray.optJSONObject(i)));

		return categoryList;
	}

}