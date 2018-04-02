package com.jinju.android.help;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.jinju.android.activity.AutomateActivity;
import com.jinju.android.activity.ChargeActivity;
import com.jinju.android.activity.AboutActivity;
import com.jinju.android.activity.FinancialDetailActivity;
import com.jinju.android.activity.HomeNoticeActivity;
import com.jinju.android.activity.LoginInAdvanceActivity;
import com.jinju.android.activity.MainActivity;
import com.jinju.android.activity.ManageBankCardActivity;
import com.jinju.android.activity.MyFinancialActivity;
import com.jinju.android.activity.RedPacketActivity;
import com.jinju.android.activity.WithdrawActivity;
import com.jinju.android.base.DdApplication;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.util.AppUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Libra on 2017/4/28.
 */

public class UIHelper {
    private static final int PERMISSION_CALL_PHONE = 123;

    /**
     * 调用登录 界面
     */
    public static void showLogin(Context context) {
        Intent intent = new Intent(context, LoginInAdvanceActivity.class);
        context.startActivity(intent);
    }
    public static void showJsBridgeWebViewActivity(Context context,String url) {
        Intent intent = new Intent(context, BaseJsBridgeWebViewActivity.class);
        intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,url);
        context.startActivity(intent);
    }

    /**
     * 理财产品列表
     *
     */
    public static void showLoanActivity(Activity activity) {
        Intent intent = new Intent(activity,MainActivity.class);
        intent.putExtra("activityType", MainActivity.TAB_LOAN);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 我的红包
     * @param context
     */
    public static void showRedPacketActivity(Context context) {
        if (DdApplication.getConfigManager().isLogined()) {
            Intent intent = new Intent(context,RedPacketActivity.class);
            context.startActivity(intent);
        } else {
            showLogin(context);
        }

    }

    /**
     *首页
     * @param context
     */
    public static void showHomeActivity(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 主页
     * @param context
     */
    public static void showMainActivity(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 充值
     * @param context
     */
    public static void showChargeActivity(Context context) {
        if (DdApplication.getConfigManager().isLogined()) {
            Intent intent = new Intent(context,ChargeActivity.class);
            context.startActivity(intent);
        } else {
            showLogin(context);
        }

    }

    /**
     * 提现
     * @param context
     */
    public static void showWithdrawActivity(Context context) {
        if (DdApplication.getConfigManager().isLogined()) {
            Intent intent = new Intent(context,WithdrawActivity.class);
            context.startActivity(intent);
        } else {
            showLogin(context);
        }

    }

    /**
     * 我的理财
     * @param context
     */
    public static void showMyFinancialActivity(Context context) {
        if (DdApplication.getConfigManager().isLogined()) {
            Intent intent = new Intent(context,MyFinancialActivity.class);
            context.startActivity(intent);
        } else {
            showLogin(context);
        }

    }

    /**
     * 自动投标
     * @param context
     */
    public static void showAutomateActivity(Context context) {
        if (DdApplication.getConfigManager().isLogined()) {
            Intent intent = new Intent(context,AutomateActivity.class);
            context.startActivity(intent);
        } else {
            showLogin(context);
        }

    }

    /**
     * 我的银行卡
     * @param context
     */
    public static void showManageBankCardActivity(Context context) {
        if (DdApplication.getConfigManager().isLogined()) {
            Intent intent = new Intent(context,ManageBankCardActivity.class);
            context.startActivity(intent);
        } else {
            showLogin(context);
        }

    }

    /**
     * 跳转到公告
     * @param context
     */
    public static void showHomeNoticeActivity(Context context) {
        Intent intent = new Intent(context,HomeNoticeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 关于我们
     * @param context
     */
    public static void showAboutActivity(Context context) {

        Intent intent = new Intent(context,AboutActivity.class);
        if (context.getClass().getSimpleName().equals("SplashActivity")) { //启动页
            intent.putExtra("advert",true);
        }
        context.startActivity(intent);
    }

    //跳转到指定标的详情页
    public static void showFinancialDetailActivity(Context context,Long value) {

        Intent intent = new Intent(context,FinancialDetailActivity.class);
        intent.putExtra("id",value);
        context.startActivity(intent);

    }
    /**
     * 调用 打电话
     *
     * @param activity
     * @param fromMessage, serviceNumber
     */
    public static void callPhoneWindow(final Activity activity, String fromMessage, final String serviceNumber) {
        String message = fromMessage + "：" + serviceNumber;
        callPhoneWindow(activity, message, serviceNumber, 0);
    }
    public static void callPhoneWindow(final FragmentActivity activity, String fromMessage, final String serviceNumber) {
        String message = fromMessage + "：" + serviceNumber;
        callPhoneWindow(activity, message, serviceNumber, 0);
    }
    /***
     * 调用 打电话
     *
     * @param activity      activity
     * @param fromMessage   提示内容文案
     * @param serviceNumber 拨打的电话号码
     * @param type          默认为有头部样式 1为无头部 无标题
     */
    public static void callPhoneWindow(final Activity activity, String fromMessage,
                                       final String serviceNumber, int type) {
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.CALL_PHONE)) {

            switch (type) {
                case 1:

                    break;
                default:
                    final CustomRoundDialog customRoundDialog = new CustomRoundDialog(activity,2);
                    customRoundDialog.setContent(fromMessage);
                    customRoundDialog.show();
                    customRoundDialog.setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customRoundDialog.dismiss();
                        }
                    });
                    customRoundDialog.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UIHelper.callPhone(activity, serviceNumber);
                            customRoundDialog.dismiss();
                        }
                    });
                    break;
            }

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CALL_PHONE);
        }
    }
        /**
         * 调用 打电话
         *
         * @param activity
         * @param phoneNumber
         */
    public static void callPhone(Activity activity, String phoneNumber) {
        if (activity == null || TextUtils.isEmpty(phoneNumber)) {
            return;
        }

        if (EasyPermissions.hasPermissions(activity, Manifest.permission.CALL_PHONE)) {
            String permName = "android.permission.CALL_PHONE";
            String pkgName = activity.getPackageName();
            // 结果为0则表示使用了该权限，-1则表求没有使用该权限
            int result = activity.getPackageManager().checkPermission(permName, pkgName);
            if (result == 0 && hasPermission(activity, permName)) {
                Uri data = Uri.parse("tel:"+phoneNumber.replaceAll("-", ""));
                Intent intent = new Intent(Intent.ACTION_CALL, data);
                activity.startActivity(intent);
            } else {
                AppUtils.showToast(activity,"没有拨打电话的权限");
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},PERMISSION_CALL_PHONE);
        }

    }   /**
     * 检测是否具有某种权限
     *
     * @param context
     * @return
     */
    private static boolean hasPermission(Context context, String permission) {
        int hasPermission = context.checkCallingOrSelfPermission(permission);
        return hasPermission == PackageManager.PERMISSION_GRANTED;
    }

}
