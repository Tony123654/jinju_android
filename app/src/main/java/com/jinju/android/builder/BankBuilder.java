package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Bank;

public class BankBuilder {

	public static Bank build(JSONObject jsonObject) throws JSONException {
		Bank bank = new Bank();
		bank.setBankCode(jsonObject.optString("bankCode"));
		bank.setBankName(jsonObject.optString("bankName"));
		bank.setLogoPath(jsonObject.optString("logoPath"));
		bank.setPayLimitOnce(jsonObject.optLong("payLimitOnce"));
		bank.setPayLimitDay(jsonObject.optLong("payLimitDay"));
		bank.setPayLimitMonth(jsonObject.optLong("payLimitMonth"));
		return bank;
	}

	public static List<Bank> buildList(JSONArray jsonArray) throws JSONException {
		List<Bank> bankList = new ArrayList<Bank>();
		if (jsonArray == null)
			return bankList;

		for (int i = 0; i < jsonArray.length(); i++)
			bankList.add(build(jsonArray.optJSONObject(i)));

		return bankList;
	}

}