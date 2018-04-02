package com.jinju.android.builder;

import com.jinju.android.api.Financial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/6/5.
 */

public class FinancialOutBuilder {
    public static Financial build(JSONObject jsonObject) throws JSONException {
        Financial financial = new Financial();
        financial.setId(jsonObject.optLong("id"));
        financial.setName(jsonObject.optString("name"));
        financial.setProductStatus(jsonObject.optInt("productStatus"));
        financial.setYearInterest(jsonObject.optString("yearInterest"));
        financial.setActualInterestRate(jsonObject.optString("actualInterestRate"));
        financial.setSubsidyInterestRate(jsonObject.optString("subsidyInterestRate"));
        financial.setLoanPeriodDesc(jsonObject.optString("loanPeriodDesc"));
        financial.setHasPercent(jsonObject.optInt("hasPercent"));
        financial.setHasFundsAmount(jsonObject.optLong("hasFundsAmount"));
        financial.setType(jsonObject.optString("type"));
        financial.setIsOnlyNewer(jsonObject.optBoolean("isOnlyNewer"));
        financial.setShowStatus(jsonObject.optString("showStatus"));
        financial.setBeginDuration(jsonObject.optLong("beginDuration"));
        financial.setTagList(TagBuilder.buildList(jsonObject.optJSONArray("tagList")));
        return financial;
    }
    public static List<Financial> buildList(JSONArray jsonArray) throws JSONException {
        List<Financial> financialList = new ArrayList<Financial>();
        if (jsonArray == null)
            return financialList;

        for (int i = 0; i < jsonArray.length(); i++)
            financialList.add(build(jsonArray.optJSONObject(i)));

        return financialList;
    }
}
