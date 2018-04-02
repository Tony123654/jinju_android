package com.jinju.android.builder;

import com.jinju.android.api.ButtonList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/5/15.
 */

public class ButtonListBuilder {

    public static ButtonList build(JSONObject jsonObject) throws JSONException {
        ButtonList buttons = new ButtonList();
        buttons.setDesc(jsonObject.optString("desc"));
        buttons.setPic(jsonObject.optString("pic"));
        buttons.setTitle(jsonObject.optString("title"));
        buttons.setLinkUrl(jsonObject.optString("linkUrl"));
        buttons.setType(jsonObject.optString("type"));
        buttons.setFlag(jsonObject.optString("flag"));
        return buttons;
    }

    public static List<ButtonList> buildList(JSONArray jsonArray) throws JSONException {

        List<ButtonList> buttonList = new ArrayList<ButtonList>();
        if (jsonArray == null)
            return buttonList;

        for (int i = 0; i < jsonArray.length(); i++)
            buttonList.add(build(jsonArray.optJSONObject(i)));

        return buttonList;
    }
}
