package com.jinju.android.util;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.jinju.android.help.UIHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Libra on 2017/5/5.
 * 处理 朵朵金融自定义协议
 * ddjr://j?random=XXX&t=XXX
 */

public class DDJRCmdUtils {
    private static final String TAG = DDJRCmdUtils.class.getSimpleName();
    public final static String DDJR_OVERRIDE_SCHEMA = "ddjr://";

    /**
     * * xmdd://j?t=l 理财产品列表页
     * * xmdd://j?t=cp 我的红包
     * * xmdd://j?t=rc 充值
     * * xmdd://j?t=wd 提现
     * * xmdd://j?t=mc 我的理财
     * * xmdd://j?t=ab 自动投标
     * * xmdd://j?t=mb 我的银行卡
     * * xmdd://j?t=s&value=40755 某个指定标的， t 固定，后面带某标的标识符
     * * xmdd://j?t=gg 公告
     */


    public static void handleProtocol(Activity context, String url) {
        String text = "";
        try {
            text = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "handleProtocol " + text);
        Uri uri = Uri.parse(text);
        if (uri == null || !uri.isHierarchical() || !uri.isAbsolute()) {
            Log.e(TAG, "uri 解析失败！");
            return;
        }

        if (text.startsWith(DDJR_OVERRIDE_SCHEMA)) {
            // app 内部跳转
            Map<String, String> qm = parseQuery(uri);
            if (qm == null) return;
            //获取t键的值
            String t = qm.get("t");
            if (TextUtils.isEmpty(t)) {
                Log.e(TAG, "未发现命令！" + text);
                return;
            }
            if ("l".equals(t)) {
                UIHelper.showLoanActivity(context);
            } else if ("cp".equals(t)) {
                UIHelper.showRedPacketActivity(context);
            } else if ("rc".equals(t)) {
                UIHelper.showChargeActivity(context);
            } else if ("wd".equals(t)) {
                UIHelper.showWithdrawActivity(context);
            } else if ("mc".equals(t)) {
                UIHelper.showMyFinancialActivity(context);
            } else if ("ab".equals(t)) {
                UIHelper.showAutomateActivity(context);
            } else if ("mb".equals(t)) {
                UIHelper.showManageBankCardActivity(context);
            }else if ("s".equals(t)) {

                if (qm.containsKey("value")) {
                    long value = Long.parseLong(qm.get("value"));
                    Log.d(TAG, "value " + value);
                    UIHelper.showFinancialDetailActivity(context,value);
                }
            } else if ("gg".equals(t)){
                UIHelper.showHomeNoticeActivity(context);
            }else if ("au".equals(t)) {
                UIHelper.showAboutActivity(context);
            }
        }
    }


    private static Map<String, String> parseQuery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String query = uri.getEncodedQuery();
        if (TextUtils.isEmpty(query)) {
            return null;
        }

        String[] querys = query.split("&");
        Map<String, String> params = new HashMap<>();
        for (String s : querys) {
            int pos = s.indexOf("=");
            if (pos != -1) {
                params.put(s.substring(0, pos), s.substring(pos + 1));
            } else {
                params.put(s, null);
            }
        }
        return params;
    }
}
