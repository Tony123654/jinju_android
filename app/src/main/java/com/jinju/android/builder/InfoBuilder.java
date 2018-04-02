package com.jinju.android.builder;

import com.jinju.android.api.Info;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by miyun-8767 on 2016/1/21.
 */
public class InfoBuilder {
    public static Info build(JSONObject jsonObject) throws JSONException {
        Info info = new Info();
        info.setHeadImg(jsonObject.optString("headImg"));
        info.setNick(jsonObject.optString("nick"));
        info.setMemberId(jsonObject.optLong("memberId"));
        info.setName(jsonObject.optString("name"));
        info.setAuthStatus(jsonObject.optInt("authStatus"));
        info.setIdCardNumber(jsonObject.optString("idCardNumber"));
        info.setMobile(jsonObject.optString("mobile"));
        return info;
    }
}
