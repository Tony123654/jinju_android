package com.jinju.android.builder;

import com.jinju.android.api.JsModalInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/5/10.
 */

public class JsModalInfoBuilder {
    public static JsModalInfo build(JSONObject jsonObject) throws JSONException {
        JsModalInfo jsModalInfo = new JsModalInfo();
        jsModalInfo.setModalId(jsonObject.optString("modalId"));
        jsModalInfo.setTitle(jsonObject.optString("title"));
        jsModalInfo.setText(jsonObject.optString("text"));
        jsModalInfo.setType(jsonObject.optString("type"));

        jsModalInfo.setButtons(buildList(jsonObject.optJSONArray("buttons")));
        return jsModalInfo;
    }
    public static List<JsModalInfo.Buttons> buildList(JSONArray jsonArray) throws JSONException {
        List<JsModalInfo.Buttons> buttonsList = new ArrayList<JsModalInfo.Buttons>();
        if (jsonArray == null)
            return buttonsList;

        for (int i = 0; i < jsonArray.length(); i++) {
            JsModalInfo.Buttons Buttons = new JsModalInfo.Buttons();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Buttons.setText(jsonObject.optString("text"));
            Buttons.setValue(jsonObject.optString("value"));
            buttonsList.add(Buttons);
        }

        return buttonsList;
    }
}
