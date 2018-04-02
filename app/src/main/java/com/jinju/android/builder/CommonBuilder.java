package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class CommonBuilder {

	public static List<String> buildStringList(JSONArray jsonArray) throws JSONException {
		List<String> stringList = new ArrayList<String>();
		if (jsonArray == null)
			return stringList;

		for (int i = 0; i < jsonArray.length(); i++)
			stringList.add(jsonArray.optString(i));
		return stringList;
	}

	public static List<Integer> buildIntegerList(JSONArray jsonArray) throws JSONException {
		List<Integer> integerList = new ArrayList<Integer>();
		if (jsonArray == null)
			return integerList;

		for (int i = 0; i < jsonArray.length(); i++)
			integerList.add(jsonArray.optInt(i));
		return integerList;
	}

}