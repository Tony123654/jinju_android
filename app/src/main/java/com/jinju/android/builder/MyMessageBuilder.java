package com.jinju.android.builder;

import com.jinju.android.api.Notice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/6/29.
 */

public class MyMessageBuilder {

    public static Notice build(JSONObject jsonObject) throws JSONException {
        Notice notice = new Notice();
        notice.setMsgId(jsonObject.optInt("msgId"));
        notice.setMsgType(jsonObject.optInt("msgType"));
        notice.setMsgDate(jsonObject.optString("msgDate"));
        notice.setMsgContent(jsonObject.optString("msgContent"));
        notice.setMsgTypeDesc(jsonObject.optString("msgTypeDesc"));

        return notice;
    }
    public static List<Notice> buildList(JSONArray jsonArray) throws JSONException {
        List<Notice> noticeList = new ArrayList<Notice>();
        if (jsonArray == null)
            return noticeList;

        for (int i = 0; i < jsonArray.length(); i++)
            noticeList.add(build(jsonArray.optJSONObject(i)));

        return noticeList;
    }
}
