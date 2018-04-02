package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.BankDetail;

public class BankDetailBuilder {

	public static BankDetail build(JSONObject jsonObject) throws JSONException {
		BankDetail bankDetail = new BankDetail();
		bankDetail.setId(jsonObject.optString("id"));
		bankDetail.setAccount(jsonObject.optString("account"));
		bankDetail.setBankCode(jsonObject.optString("bankCode"));
		bankDetail.setBankName(jsonObject.optString("bankName"));
		bankDetail.setLogoPath(jsonObject.optString("logoPath"));
		bankDetail.setCardNoTail(jsonObject.optString("cardNoTail"));
		bankDetail.setBankProvinceCode(jsonObject.optString("bankProvinceCode"));
		bankDetail.setBankProvinceName(jsonObject.optString("bankProvinceName"));
		bankDetail.setBankCityCode(jsonObject.optString("bankCityCode"));
		bankDetail.setBankCityName(jsonObject.optString("bankCityName"));
		bankDetail.setBankBranchName(jsonObject.optString("bankBranchName"));
		bankDetail.setIdCardNumber(jsonObject.optString("idCardNumber"));
		return bankDetail;
	}

}