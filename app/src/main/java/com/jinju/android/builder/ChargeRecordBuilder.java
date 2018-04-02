package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.ChargeRecord;

public class ChargeRecordBuilder {

	public static ChargeRecord build(JSONObject jsonObject) throws JSONException {
		ChargeRecord chargeRecord = new ChargeRecord();
		chargeRecord.setBankName(jsonObject.optString("bankName"));
		chargeRecord.setStatusDesc(jsonObject.optString("statusDesc"));
		chargeRecord.setPayAmount(jsonObject.optLong("payAmount"));
		chargeRecord.setGmtCharge(jsonObject.optString("gmtCharge"));
		chargeRecord.setGmtGroupTime(jsonObject.optString("gmtGroupTime"));		//充值年月
		return chargeRecord;
	}

	public static List<ChargeRecord> buildList(JSONArray jsonArray) throws JSONException {
		List<ChargeRecord> chargeRecordList = new ArrayList<ChargeRecord>();
		if (jsonArray == null)
			return chargeRecordList;

		for (int i = 0; i < jsonArray.length(); i++)
			chargeRecordList.add(build(jsonArray.optJSONObject(i)));

		return chargeRecordList;
	}

}