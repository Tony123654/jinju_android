package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.AuthCardDetail;

public class AuthCardDetailBuilder {

	public static AuthCardDetail build(JSONObject jsonObject) throws JSONException {
		AuthCardDetail authCardDetail = new AuthCardDetail();
		authCardDetail.setMemberBankId(jsonObject.optLong("memberBankId"));
		authCardDetail.setCardNoTail(jsonObject.optString("cardNoTail"));
		authCardDetail.setTel(jsonObject.optString("tel"));
		authCardDetail.setAccount(jsonObject.optString("account"));
		authCardDetail.setIdCardNumber(jsonObject.optString("idCardNumber"));
		authCardDetail.setBankList(BankBuilder.buildList(jsonObject.optJSONArray("bankList")));
		return authCardDetail;
	}

}