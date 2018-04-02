package com.jinju.android.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jinju.android.base.DdApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Libra on 2017/4/10.
 *
 */

public class VersionUtils {


    /**
     * 获取版本name
     * @return 当前应用的版本name
     */
    public static String getVersionName() {
        PackageManager pm = DdApplication.getInstance().getPackageManager();
        try {
            PackageInfo pInfo = pm.getPackageInfo(DdApplication.getInstance().getPackageName(), 0);

            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取版本号code
     * @return 当前应用的版本号
     */
    public static int getVersionCode() {
        int version = -1;
        try {
            version = DdApplication.getInstance().getPackageManager().getPackageInfo(DdApplication.getInstance().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return version;
    }

    /** 获取渠道名
     *
    * @return 如果没有获取成功，那么返回值为空
    */
    public static String getChannelName() {

        String channelName = "";
        try {
            PackageManager packageManager = DdApplication.getInstance().getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(DdApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 获取文件夹中的apk的版本号
     * @param filePath
     * @return
     */
    public static List<String> getApkVersion(Context context,String filePath) {
        List lists = null;
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] files = dir.listFiles();

        if (files.length>0) {

            lists = new ArrayList<String>();

            for (File file :files) {
                if (!file.isDirectory()) {
                    String fileName = file.getName();
                    if (fileName.contains("jinju")) {

                        int beginIndex =  fileName.indexOf("_")+1;
                        int endIndex =  fileName.indexOf(".apk");
                        //如果是正确的安装包添加到集合
                        Boolean apkInfo = getUninatllApkInfo(context, filePath + fileName);
                        if (apkInfo) {
                            lists.add(fileName.substring(beginIndex,endIndex));
                        } else {
                            //不是正确的安装包，删除
                            file.delete();
                        }


                    }
                }
            }
        }
        return lists;
    }

    /**
     * 判断安装包是否是正确的安装包
     * @param context
     * @param filePath
     * @return
     */
    public static boolean getUninatllApkInfo(Context context, String filePath) {
        boolean result = false;
        try {
            PackageManager pm = context.getPackageManager();
            Log.e("archiveFilePath", filePath);
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            String packageName = null;
            if (info != null) {
                result = true;
            }
        } catch (Exception e) {
            //解析未安装的 apk 出现异常
            result = false;
        }
        return result;
    }
    /**
     * 获取 手机唯一标识 IMEI
     */
    public static String getPhoneIMEI(Context context) {
        String imei;

            //下载apk，并自动安装
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();

        return imei;
    }
    /**
     * 判断是否安装微信
     * @return
     */
    public static boolean isWeixinAvilible() {
        final PackageManager packageManager = DdApplication.getInstance().getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 判断是否安装QQ
     * @return
     */
    public static boolean isQQClientAvailable() {
        final PackageManager packageManager = DdApplication.getInstance().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 打开第三方应用
     * @param packageName
     */
    public static void openApp(Context mContext,String packageName) {
        Intent mainIntent  = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPackageManager = mContext.getPackageManager();
        List<ResolveInfo> mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));
        for(ResolveInfo res : mAllApps){
            //该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;

            // 打开QQ pkg中包含"qq"，打开微信，pkg中包含"mm"
            if(pkg.contains(packageName)){
                ComponentName component = new ComponentName(pkg, cls);
                Intent intent = new Intent();
                intent.setComponent(component);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }

    }
}
