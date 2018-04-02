package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Order;

public class OrderBuilder {

	public static Order build(JSONObject jsonObject) throws JSONException {
		Order order = new Order();
		order.setProductId(jsonObject.optLong("productId"));
		order.setTransferInfoId(jsonObject.optLong("transferInfoId"));
		order.setBuyAmount(jsonObject.optLong("buyAmount"));
		order.setOrderAmount(jsonObject.optLong("orderAmount"));
		order.setProductName(jsonObject.optString("productName"));
		order.setLoanPeriodDesc(jsonObject.optString("loanPeriodDesc"));
		order.setHasFundsAmount(jsonObject.optLong("hasFundsAmount"));
		order.setAmount(jsonObject.optLong("amount"));
		order.setGiftId(jsonObject.optLong("giftId"));
		order.setGiftName(jsonObject.optString("giftName"));
		order.setExceptIncome(jsonObject.optLong("exceptIncome"));
		order.setAvaliableBalance(jsonObject.optLong("avaliableBalance"));
		order.setLeftPay(jsonObject.optLong("leftPay"));
		order.setBalancePay(jsonObject.optLong("balancePay"));
		order.setBankPay(jsonObject.optLong("bankPay"));
		order.setBankLogo(jsonObject.optString("bankLogo"));
		order.setBankName(jsonObject.optString("bankName"));
		order.setBankTailNum(jsonObject.optString("bankTailNum"));
		order.setOnceLimit(jsonObject.optLong("onceLimit"));
		order.setDayLimit(jsonObject.optLong("dayLimit"));
		order.setMonthLimit(jsonObject.optLong("monthLimit"));
		return order;
	}
}