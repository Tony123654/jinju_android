package com.jinju.android.builder;

import com.jinju.android.api.JsShareInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/5/18.
 */

public class JsShareInfoBuilder {
    public static JsShareInfo build(JSONObject jsonObject) throws JSONException {
        JsShareInfo jsShareInfo = new JsShareInfo();
        jsShareInfo.setLinkUrl(jsonObject.optString("linkUrl"));
        jsShareInfo.setTitle(jsonObject.optString("title"));
        jsShareInfo.setDesc(jsonObject.optString("desc"));
        jsShareInfo.setImgUrl(jsonObject.optString("imgUrl"));
        jsShareInfo.setTriggerId(jsonObject.optString("triggerId"));
        jsShareInfo.setButtons(buildList(jsonObject.optJSONArray("buttons")));
        return jsShareInfo;
    }
    public static List<Integer> buildList(JSONArray jsonArray) throws JSONException {
        List<Integer> buttonsList = new ArrayList<Integer>();

        if (jsonArray == null)
            return buttonsList;

        for (int i = 0; i < jsonArray.length(); i++) {
            Integer integer = jsonArray.getInt(i);
            buttonsList.add(integer);
        }

        return buttonsList;
    }
}
