package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.HomeNoticeBuilder;
import com.jinju.android.api.Index;

public class IndexBuilder {

	public static Index build(JSONObject jsonObject) throws JSONException {
		Index index = new Index();
		index.setAdvertList(AdvertBuilder.buildList(jsonObject.optJSONArray("adList")));
		index.setProductList(ProductBuilder.buildList(jsonObject.optJSONArray("productList")));
		index.setTopButtonList(ButtonListBuilder.buildList(jsonObject.optJSONArray("buttonList")));
		index.setBottomSlideList(ButtonListBuilder.buildList(jsonObject.optJSONArray("bottomSlideList")));
		index.setPopAdList(PopAdListBuilder.buildList(jsonObject.optJSONArray("popADList")));
		index.setNoticeList(HomeNoticeBuilder.buildList(jsonObject.optJSONArray("noticeList")));
		index.setMemberStep(jsonObject.optInt("memberStep"));
		index.setFloatUrl(jsonObject.optString("floatUrl"));
		index.setFloatImg(jsonObject.optString("floatImg"));
		index.setFloatType(jsonObject.optString("floatType"));
		index.setSecurityMessage(jsonObject.optString("securityMessage"));
		index.setTotalFinancial(jsonObject.optJSONObject("statisticalInfo").optString("totalFinancial"));
		index.setTotalInterest(jsonObject.optJSONObject("statisticalInfo").optString("totalInterest"));
		index.setDataLinkUrl(jsonObject.optJSONObject("statisticalInfo").optString("linkUrl"));
		index.setStepType(jsonObject.optInt("stepType"));
		index.setDot(jsonObject.optBoolean("isDot"));
		return index;
	}

}