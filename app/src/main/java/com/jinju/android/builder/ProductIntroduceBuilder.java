package com.jinju.android.builder;

import com.jinju.android.api.ProductIntroduce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/11/6.
 */

public class ProductIntroduceBuilder {
    public static ProductIntroduce build(JSONObject jsonObject) throws JSONException {
        ProductIntroduce product = new ProductIntroduce();
        product.setProDesc(jsonObject.optString("proDesc"));
        product.setLoanerDesc(jsonObject.optString("loanerDesc"));
        product.setFundReturnSource(returnSourceBuildList(jsonObject.optJSONArray("fundReturnSource")));
        product.setLoanUsage(jsonObject.optString("loanUsage"));
        product.setPledgeFiles(buildList(jsonObject.optJSONArray("pledgeFiles")));
        product.setQuestionUrl(jsonObject.optString("questionUrl"));
        product.setProjectTheory(jsonObject.optString("projectTheory"));
        return product;
    }
    public static List<String> buildList(JSONArray jsonArray) throws JSONException {

        List<String> imgUrlList = new ArrayList<String>();
        if (jsonArray == null)
            return imgUrlList;

        for (int i = 0; i < jsonArray.length(); i++)
            imgUrlList.add((jsonArray.optJSONObject(i).optString("imgUrl")));

        return imgUrlList;
    }
    public static List<String> returnSourceBuildList(JSONArray jsonArray) throws JSONException {

        List<String> refundList = new ArrayList<String>();
        if (jsonArray == null)
            return refundList;

        for (int i = 0; i < jsonArray.length(); i++)
            refundList.add((jsonArray.optJSONObject(i).optString("fundDesc")));

        return refundList;
    }
}
