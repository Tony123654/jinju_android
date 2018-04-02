package com.jinju.android.builder;

import com.jinju.android.api.PositionDetail;

import org.json.JSONException;
import org.json.JSONObject;


public class PositionDetailBuilder {

	public static PositionDetail build(JSONObject jsonObject) throws JSONException {
		PositionDetail positionDetail = new PositionDetail();
		positionDetail.setPositionId(jsonObject.optLong("positionId"));
		positionDetail.setProductId(jsonObject.optLong("productId"));
		positionDetail.setProductName(jsonObject.optString("productName"));
		positionDetail.setTodayIncome(jsonObject.optLong("todayIncome"));
		positionDetail.setYesterdayIncome(jsonObject.optLong("yesterdayIncome"));
		positionDetail.setPositionAmount(jsonObject.optLong("positionAmount"));
		positionDetail.setPositionIncome(jsonObject.optLong("positionIncome"));
		positionDetail.setYearInterest(jsonObject.optString("yearInterest"));
		positionDetail.setGmtEnd(jsonObject.optString("gmtEnd"));
		positionDetail.setGmtFundReturn(jsonObject.optString("gmtFundReturn"));
		positionDetail.setTradeHistoryList(TradeHistoryBuilder.buildList(jsonObject.optJSONArray("tradeList")));
		positionDetail.setProductContractUrl(jsonObject.optString("productContractUrl"));
		positionDetail.setTransferContractUrl(jsonObject.optString("transferContractUrl"));
		positionDetail.setCanTrans(jsonObject.optBoolean("canTrans"));
		return positionDetail;
	}
}