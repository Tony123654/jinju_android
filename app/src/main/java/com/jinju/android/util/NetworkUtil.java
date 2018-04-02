package com.jinju.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Libra on 2017/5/2.
 */

public class NetworkUtil {


    /**
    *
    * 获取网络类型
    *
    */
    public static int getNetType(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);

        if (networkInfo == null) {
            return -1;
        } else
            return networkInfo.getType();
    }
    /**
     * 获取可用的网络信息
     *
     * @param context
     * @return
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo();
        } catch (Exception e) {
            return null;
        }
    }

}
