package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.WithdrawRecord;

public class WithdrawRecordBuilder {

	public static WithdrawRecord build(JSONObject jsonObject) throws JSONException {
		WithdrawRecord withdrawRecord = new WithdrawRecord();
		withdrawRecord.setBankName(jsonObject.optString("bankName"));
		withdrawRecord.setCardNoTail(jsonObject.optString("cardNoTail"));
		withdrawRecord.setStatusDesc(jsonObject.optString("statusDesc"));
		withdrawRecord.setTransAmount(jsonObject.optLong("transAmount"));
		withdrawRecord.setGmtApply(jsonObject.optString("gmtApply"));
		withdrawRecord.setGmtApplyFormat(jsonObject.optString("gmtApplyFormat"));
		withdrawRecord.setStatus(jsonObject.optString("status"));
		withdrawRecord.setGmtDate(jsonObject.optString("gmtDate"));
		withdrawRecord.setGmtToBank(jsonObject.optString("gmtToBank"));
		return withdrawRecord;
	}

	public static List<WithdrawRecord> buildList(JSONArray jsonArray) throws JSONException {
		List<WithdrawRecord> withdrawRecordList = new ArrayList<WithdrawRecord>();
		if (jsonArray == null)
			return withdrawRecordList;

		for (int i = 0; i < jsonArray.length(); i++)
			withdrawRecordList.add(build(jsonArray.optJSONObject(i)));

		return withdrawRecordList;
	}

}