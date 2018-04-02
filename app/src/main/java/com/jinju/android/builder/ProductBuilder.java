package com.jinju.android.builder;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.Product;


public class ProductBuilder {

	public static Product build(JSONObject jsonObject) throws JSONException {
		Product product = new Product();
		product.setProductId(jsonObject.optLong("productId"));
		product.setInterestRate(jsonObject.optDouble("interestRate"));
		product.setActualInterestRate(jsonObject.optString("actualInterestRate"));
		product.setSubsidyInterestRate(jsonObject.optString("subsidyInterestRate"));
		product.setName(jsonObject.optString("name"));
		product.setLoanPeriodDesc(jsonObject.optString("loanPeriodDesc"));
		product.setHasFundsAmount(jsonObject.optLong("hasFundsAmount"));
		product.setTagList(TagBuilder.buildList(jsonObject.optJSONArray("tagList")));
		product.setActivityTag(jsonObject.optString("activityTag"));
		product.setHasPercent(jsonObject.optInt("hasPercent"));
		product.setIsOnlyNewer(jsonObject.optBoolean("isOnlyNewer"));
		product.setBeginDuration(jsonObject.optLong("beginDuration"));
		product.setShowStatus(jsonObject.optString("showStatus"));
		product.setHasPercent(jsonObject.optInt("hasPercent"));
		product.setLeastBuy(jsonObject.optLong("leastBuy"));
		return product;
	}
	
	public static List<Product> buildList(JSONArray jsonArray) throws JSONException {
		List<Product> productList = new ArrayList<Product>();
		if (jsonArray == null)
			return productList;

		for (int i = 0; i < jsonArray.length(); i++)
			productList.add(build(jsonArray.optJSONObject(i)));

		return productList;
	}

}