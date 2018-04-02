package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.MyBank;

public class MyBankBuilder {

	public static MyBank build(JSONObject jsonObject) throws JSONException {
		MyBank myBank = new MyBank();
		myBank.setId(jsonObject.optString("id"));
		myBank.setBankCode(jsonObject.optString("bankCode"));
		myBank.setBankName(jsonObject.optString("bankName"));
		myBank.setStatus(jsonObject.optString("status"));
		myBank.setStatusDesc(jsonObject.optString("statusDesc"));
		myBank.setCardNoTail(jsonObject.optString("cardNoTail"));
		myBank.setLogoPath(jsonObject.optString("logoPath"));
		myBank.setFastPayStatus(jsonObject.optInt("fastPayStatus"));
		myBank.setName(jsonObject.optString("name"));
		myBank.setOnceLimit(jsonObject.optString("onceLimit"));
		myBank.setDayLimit(jsonObject.optString("dayLimit"));
		return myBank;
	}

	public static List<MyBank> buildList(JSONArray jsonArray) throws JSONException {
		List<MyBank> myBankList = new ArrayList<MyBank>();
		if (jsonArray == null)
			return myBankList;

		for (int i = 0; i < jsonArray.length(); i++)
			myBankList.add(build(jsonArray.optJSONObject(i)));

		return myBankList;
	}

}