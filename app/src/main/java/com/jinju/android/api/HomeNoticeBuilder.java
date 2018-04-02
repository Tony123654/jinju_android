package com.jinju.android.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/10/11.
 */

public class HomeNoticeBuilder {
    public static HomeNotice build(JSONObject jsonObject) throws JSONException {
        HomeNotice homeNotice = new HomeNotice();
        homeNotice.setTitle(jsonObject.optString("title"));
        homeNotice.setMark(jsonObject.optString("mark"));
        homeNotice.setContent(jsonObject.optString("content"));
        homeNotice.setNoticeDate(jsonObject.optString("noticeDate"));
        homeNotice.setNoticeUrl(jsonObject.optString("noticeUrl"));
        return homeNotice;
    }
    public static List<HomeNotice> buildList(JSONArray jsonArray) throws JSONException {
        List<HomeNotice> mHomeNoticeList = new ArrayList<HomeNotice>();
        if (jsonArray == null)
            return mHomeNoticeList;

        for (int i = 0; i < jsonArray.length(); i++)
            mHomeNoticeList.add(build(jsonArray.optJSONObject(i)));

        return mHomeNoticeList;
    }
}
