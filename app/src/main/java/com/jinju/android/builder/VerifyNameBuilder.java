package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.VerifyName;

public class VerifyNameBuilder {

	public static VerifyName build(JSONObject jsonObject) throws JSONException {
		VerifyName verifyName = new VerifyName();
		verifyName.setId(jsonObject.optLong("id"));
		verifyName.setName(jsonObject.optString("name"));
		verifyName.setIdCardNumber(jsonObject.optString("idCardNumber"));
		return verifyName;
	}

}