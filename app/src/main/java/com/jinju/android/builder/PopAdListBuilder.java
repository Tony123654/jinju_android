package com.jinju.android.builder;

import com.jinju.android.api.PopAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/6/7.
 */

public class PopAdListBuilder {
    public static PopAd build(JSONObject jsonObject) throws JSONException {
        PopAd popAd = new PopAd();
        popAd.setCode(jsonObject.optString("code"));
        popAd.setImgUrl(jsonObject.optString("imgUrl"));
        popAd.setLinkUrl(jsonObject.optString("linkUrl"));
        popAd.setType(jsonObject.optString("type"));

        return popAd;
    }

    public static List<PopAd> buildList(JSONArray jsonArray) throws JSONException {

        List<PopAd> popAdList = new ArrayList<PopAd>();
        if (jsonArray == null)
            return popAdList;

        for (int i = 0; i < jsonArray.length(); i++)
            popAdList.add(build(jsonArray.optJSONObject(i)));

        return popAdList;
    }
}
