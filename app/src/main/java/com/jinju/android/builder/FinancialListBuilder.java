package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.FinancialList;

public class FinancialListBuilder {

	public static FinancialList build(JSONObject jsonObject) throws JSONException {
		FinancialList financialList = new FinancialList();
		financialList.setSubCount(jsonObject.optInt("subCount"));
		financialList.setTransCount(jsonObject.optInt("transCount"));
		financialList.setTotalAsset(jsonObject.optLong("totalAsset"));
		financialList.setTodayIncome(jsonObject.optLong("todayIncome"));
		financialList.setTotalIncome(jsonObject.optLong("totalIncome"));
		financialList.setFloatImg(jsonObject.optString("floatImg"));
		financialList.setFloatUrl(jsonObject.optString("floatUrl"));
		financialList.setFloatType(jsonObject.optString("floatType"));
		financialList.setPage(PageBuilder.build(jsonObject));
		financialList.setFinancialList(FinancialBuilder.buildList(jsonObject.optJSONArray("items")));//精选标
		financialList.setFinancialOutList(FinancialOutBuilder.buildList(jsonObject.optJSONArray("invalidItems")));//已售罄
		financialList.setActivityItems(FinancialBuilder.buildList(jsonObject.optJSONArray("activityItems")));//活动标
		financialList.setPrivilegeItems(FinancialBuilder.buildList(jsonObject.optJSONArray("privilegeItems")));
		financialList.setRepayment(jsonObject.optInt("repayMent"));
		financialList.setSellOut(jsonObject.optInt("sellOut"));
		return financialList;
	}

}