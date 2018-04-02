package com.jinju.android.builder;

import com.jinju.android.api.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseBuilder {

	public static Response build(JSONObject jsonObject) throws JSONException {
		Response response = new Response();
		response.setCode(jsonObject.optString("code"));
		response.setMessage(jsonObject.optString("message"));
		response.setResult(jsonObject.optString("result"));
		response.setBody(jsonObject.toString());
		return response;
	}

}