package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.jinju.android.api.Function;

public class FunctionBuilder {

	public static Function build(JSONObject jsonObject) throws JSONException {
		Function function = new Function();
		function.setFuncNo(jsonObject.optString("funcNo"));
		function.setTitle(jsonObject.optString("title"));
		function.setSubTitle(jsonObject.optString("subTitle"));
		function.setOrder(jsonObject.optInt("order"));
		return function;
	}

	public static List<Function> buildList(JSONArray jsonArray) throws JSONException {
		List<Function> functionList = new ArrayList<Function>();
		if (jsonArray == null)
			return functionList;
		
		Log.d("functionbuilder", "length = " + jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++){
			Log.d("functionbuilder", "funcNo = " + jsonArray.optJSONObject(i).optString("funcNo"));
			functionList.add(build(jsonArray.optJSONObject(i)));
		}
		return functionList;
	}

}