package com.jinju.android.base;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.jinju.android.R;
import com.jinju.android.constant.PushType;
import com.jinju.android.interfaces.IManager;
import com.jinju.android.activity.lock.LockPatternUtils;
import com.jinju.android.activity.lock.UnlockGestureActivity;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.manager.HttpManager;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.manager.TradeManager;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.VersionUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by wangjw on 2017/1/13.
 */

public class DdApplication extends Application{
    //SmartRefreshLayout下拉刷新和上拉加载配置
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
    private static final String TAG = DdApplication.class.getSimpleName();
    private static DdApplication mInstance;
    private Map<String, IManager> mManagerMap;
    private Map<String, Object> mMap;
    private static Context mContext;

    private LockPatternUtils mLockPatternUtils;
    private int countActivedActivity = -1;
    private boolean mBackgroundEver = false;//是否已经显示锁界面
    private String lockPassword;
    private ConfigManager mConfigManager;
    private Map<String, Activity> destoryMap;

    //配置友盟分享appId 和 appKey
    {
        Config.DEBUG = true;//开启友盟debug模式
        //配置 appId
        PlatformConfig.setWeixin("wxae9c1cb989e38227", "15b7af4f8ab2b7fe86a2f31d8f2aa90f");
        PlatformConfig.setQQZone("1105969333","YlXsnVTNYngMjaTY");
    }
    /**
     * Activity容器
     */
    private List<Activity> mActivityList;
    private static String versionName;

    public DdApplication() {
        mInstance = this;
        mManagerMap = new HashMap<String, IManager>();
        mMap = new HashMap<String, Object>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DdApplication.mContext = getApplicationContext();
        //因后台需求，每次网络请求添加一个全局参数（版本号）
        versionName = VersionUtils.getVersionName();

        initManagers();
        initImageLoader(getApplicationContext());

        //友盟分享初始化
        UMShareAPI.get(this);

        mActivityList = new LinkedList<>();
        destoryMap = new HashMap<>();
        openLock();

        //注册小米推送
        registerXiaoMiPush();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        exit();
    }

    public void exit() {
        finishAllActivity();
        exitManagers();
        System.exit(0);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
    public static String getVersionName(){
        return versionName;
    }

    public static Context getAppContext() {
        return DdApplication.mContext;
    }

    public static DdApplication getInstance() {
        return mInstance;
    }

    public void setParam(String key, Object object) {
        mMap.put(key, object);
    }

    public Object getParam(String key) {
        return mMap.get(key);
    }


    public LockPatternUtils getLockPatternUtils() {
        return mLockPatternUtils;
    }

    private void initManagers() {
        mManagerMap.put(ConfigManager.class.getSimpleName(), new ConfigManager());
        mManagerMap.put(HttpManager.class.getSimpleName(), new HttpManager());
        mManagerMap.put(CommonManager.class.getSimpleName(), new CommonManager());
        mManagerMap.put(LoanManager.class.getSimpleName(), new LoanManager());
        mManagerMap.put(TradeManager.class.getSimpleName(), new TradeManager());
        mManagerMap.put(UserManager.class.getSimpleName(), new UserManager());
        for (IManager manager : mManagerMap.values())
            manager.onInit();
    }

    private void exitManagers() {
        for (IManager manager : mManagerMap.values())
            manager.onExit();
        mManagerMap.clear();
    }

    public IManager getManager(Class<?> managerClass) {
        return mManagerMap.get(managerClass.getSimpleName());
    }

    public static ConfigManager getConfigManager() {
        return (ConfigManager) getInstance().getManager(ConfigManager.class);
    }

    public static HttpManager getHttpManager() {
        return (HttpManager) getInstance().getManager(HttpManager.class);
    }

    public static CommonManager getCommonManager() {
        return (CommonManager) getInstance().getManager(CommonManager.class);
    }

    public static LoanManager getLoanManager() {
        return (LoanManager) getInstance().getManager(LoanManager.class);
    }

    public static TradeManager getTradeManager() {
        return (TradeManager) getInstance().getManager(TradeManager.class);
    }

    public static UserManager getUserManager() {
        return (UserManager) getInstance().getManager(UserManager.class);
    }

    /**
     * 添加activity到栈
     * @param activity
     */
    public void insertActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivityList.remove(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (mActivityList.size() > 0)
            return mActivityList.get(mActivityList.size()-1);
        return null;
    }
    private void finishAllActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
        mActivityList.clear();
    }
    //销毁指定activity
    public void destoryActivity(String activityName) {
        for (Activity activity : mActivityList) {
            if (activityName.equals(activity.getLocalClassName())) {
                activity.finish();
            }
        }
    }
    private void registerXiaoMiPush() {

        if(shouldInit()) {

            MiPushClient.registerPush(this, PushType.APP_ID, PushType.APP_KEY);
        }
    }
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
    //切到前后台进行解锁操作
    public void openLock() {
        countActivedActivity = 0;
        mConfigManager = getConfigManager();
        //手势锁开启状态
        mLockPatternUtils = new LockPatternUtils(this);


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

                if (countActivedActivity == 0 && mBackgroundEver == true) {
                    Log.v(TAG, "切到前台  lifecycle");
                    //登录状态下
                    if (!TextUtils.isEmpty(mConfigManager.getToken())) {
                        if (mConfigManager.getLockStatus()) {
                            timeOutCheck();
                        }
                    }
                }
                countActivedActivity++;

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                countActivedActivity--;

                if (countActivedActivity == 0) {
                    Log.v(TAG, "切到后台  lifecycle");
                    if (!TextUtils.isEmpty(mConfigManager.getToken())) {//登录状态下
                        //如果开启了手势锁，保存开始后台运行的当前时间
                        if (mConfigManager.getLockStatus()&& !TextUtils.isEmpty(mConfigManager.getLockPassword())) {
                            mConfigManager.setStartTime(System.currentTimeMillis());
                        }
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mBackgroundEver = true;
            }
        });
    }

    private void timeOutCheck() {
        long endTime = System.currentTimeMillis();
        if (endTime - getStartTime() >= 10000) {//后台运行10秒自动锁屏
            lockPassword = mConfigManager.getLockPassword();
            if (!TextUtils.isEmpty(lockPassword)) {
                Intent intent = new Intent(this, UnlockGestureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mBackgroundEver = false;
            }
        }
    }
    public long getStartTime() {
        long startTime = 0;
        try {
            startTime = mConfigManager.getStartTime();
        } catch (Exception e) {
            startTime = 0;
        }
        return startTime;
    }

}
