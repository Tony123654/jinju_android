package com.jinju.android.builder;

import com.jinju.android.api.Discover;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/11/7.
 */

public class DiscoverListBuilder {
    public static Discover build(JSONObject jsonObject) throws JSONException {
        Discover mDiscover = new Discover();
        //findActPage、findPage
        mDiscover.setFindDateBegin(jsonObject.optString("findDateBegin"));
        mDiscover.setFindDateEnd(jsonObject.optString("findDateEnd"));
        mDiscover.setImgPath(jsonObject.optString("imgPath"));
        mDiscover.setJumpType(jsonObject.optString("jumpType"));
        mDiscover.setJumpUrl(jsonObject.optString("jumpUrl"));
        mDiscover.setTitle(jsonObject.optString("title"));
        //slidList
        mDiscover.setLinkUrl(jsonObject.optString("linkUrl"));
        mDiscover.setPic(jsonObject.optString("pic"));
        mDiscover.setType(jsonObject.optString("type"));
        return mDiscover;
    }
    public static List<Discover> buildList(JSONArray jsonArray) throws JSONException {

        List<Discover> mDiscoverList = new ArrayList<Discover>();
        if (jsonArray == null)
            return mDiscoverList;

        for (int i = 0; i < jsonArray.length(); i++)
            mDiscoverList.add(build(jsonArray.optJSONObject(i)));

        return mDiscoverList;
    }
}
