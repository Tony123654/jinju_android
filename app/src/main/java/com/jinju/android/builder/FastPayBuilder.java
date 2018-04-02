package com.jinju.android.builder;

import com.jinju.android.api.FastPay;

import org.json.JSONException;
import org.json.JSONObject;

public class FastPayBuilder {

	public static FastPay build(JSONObject jsonObject) throws JSONException {
		FastPay fastPay = new FastPay();
		fastPay.setMemberBankId(jsonObject.optLong("memberBankId"));
		fastPay.setBankName(jsonObject.optString("bankName"));
		fastPay.setBankCardNum(jsonObject.optString("bankCardNum"));
		fastPay.setLogoPath(jsonObject.optString("logoPath"));
		fastPay.setOnceLimit(jsonObject.optLong("onceLimit"));
		fastPay.setMonthLimit(jsonObject.optLong("monthLimit"));
		fastPay.setDayLimit(jsonObject.optLong("dayLimit"));
		fastPay.setCanUseBalance(jsonObject.optString("canUseBalance"));
		return fastPay;
	}
}