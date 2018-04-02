package com.jinju.android.builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.WithdrawDetail;

import java.util.ArrayList;
import java.util.List;

public class WithdrawDetailBuilder {

	public static WithdrawDetail build(JSONObject jsonObject) throws JSONException {
		WithdrawDetail withdrawDetail = new WithdrawDetail();
		withdrawDetail.setMobile(jsonObject.optString("mobile"));
		withdrawDetail.setCurrBalance(jsonObject.optLong("currBalance"));
		withdrawDetail.setCanUseBalance(jsonObject.optLong("canUseBalance"));
		withdrawDetail.setMemberBankId(jsonObject.optLong("memberBankId"));
		withdrawDetail.setBankName(jsonObject.optString("bankName"));
		withdrawDetail.setCardNoTail(jsonObject.optString("cardNoTail"));
		withdrawDetail.setLogoPath(jsonObject.optString("logoPath"));
		withdrawDetail.setNote(jsonObject.optString("note"));
		withdrawDetail.setPoundageDesc(jsonObject.optString("poundageDesc"));
		withdrawDetail.setPoundageType(jsonObject.optInt("poundageType"));
		withdrawDetail.setPoundageValue(jsonObject.optString("poundageValue"));
		withdrawDetail.setWarmPrompt(buildWarmPromptList(jsonObject.optJSONArray("warmPrompt")));	//温馨提示

		return withdrawDetail;
	}

	public static List<String> buildWarmPromptList(JSONArray jsonArray) throws JSONException{
		List<String> warmPromptList = new ArrayList<String>();

		if (jsonArray == null)
			return warmPromptList;

		for (int i = 0; i < jsonArray.length(); i++)
			warmPromptList.add(jsonArray.optString(i));

		return warmPromptList;
	}

}