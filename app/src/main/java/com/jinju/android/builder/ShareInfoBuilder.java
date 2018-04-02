package com.jinju.android.builder;

import com.jinju.android.api.ShareInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Libra on 2017/12/20.
 */

public class ShareInfoBuilder {
    public static ShareInfo build(JSONObject jsonObject) throws JSONException {
        ShareInfo mShareInfo = new ShareInfo();
        mShareInfo.setShareDesc(jsonObject.optString("shareDesc"));
        mShareInfo.setShareUrl(jsonObject.optString("shareUrl"));
        mShareInfo.setShareImg(jsonObject.optString("shareImg"));
        mShareInfo.setShareTitle(jsonObject.optString("shareTitle"));

        return mShareInfo;
    }
}
