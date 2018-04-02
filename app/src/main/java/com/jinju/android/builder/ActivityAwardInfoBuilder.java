package com.jinju.android.builder;

import com.jinju.android.api.ActivityAwardInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Libra on 2017/11/15.
 */

public class ActivityAwardInfoBuilder {
    public static ActivityAwardInfo build(JSONObject jsonObject) throws JSONException {
        ActivityAwardInfo mAwardInfo = new ActivityAwardInfo();
        mAwardInfo.setRichDesc(jsonObject.optString("richDesc"));
        mAwardInfo.setEndingDesc(jsonObject.optString("endingDesc"));
        return mAwardInfo;
    }

}
