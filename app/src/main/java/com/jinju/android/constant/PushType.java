package com.jinju.android.constant;

import com.jinju.android.BuildConfig;

/**
 * Created by Libra on 2017/5/8.
 */

public class PushType {
    // AppID
    public static final String APP_ID;

    // AppKey
    public static final String APP_KEY;
    static {
        /** debug **/
        if (BuildConfig.DEBUG) {
            APP_ID = "2882303761517569976";
            APP_KEY = "5901756934976";
        } else {
            APP_ID = "2882303761517569976";
            APP_KEY = "5901756934976";
        }
    }
}
