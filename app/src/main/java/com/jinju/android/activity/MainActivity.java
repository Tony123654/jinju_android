package com.jinju.android.activity;

import android.Manifest;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TabHost;

import com.jinju.android.R;
import com.jinju.android.api.Index;
import com.jinju.android.api.PopAd;
import com.jinju.android.api.Response;
import com.jinju.android.api.VersionInfo;
import com.jinju.android.api.VersionUpdate;
import com.jinju.android.api.WidgetLocation;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.constant.BroadcastType;
import com.jinju.android.constant.SrcType;
import com.jinju.android.dialog.ActionDialog;
import com.jinju.android.dialog.UpdateDialog;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.MD5Utils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.widget.SegmentBar;
import com.jinju.android.widget.newbie.HoleBean;
import com.jinju.android.widget.newbie.NewbieGuideManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends TabActivity {
    private static final int HOME_NEWER_GUIDE = 10000;
    private static final int READ_PHONE_IMEI = 10086;

    public final static int TAB_HOME = 0;
    public final static int TAB_LOAN = 1;
    public final static int TAB_DISCOVER = 2;
    public final static int TAB_PROFILE = 3;

    private long mClickTime = 0;
    private TabHost mTabHost;
    private SegmentBar mSegmentBar;
    private Index mIndex;
    private boolean isNeedUpdateDialog = false;
    private String forceUpdate;
    private MyBroadcastReceiver mMyBroadcast;
    private int mTabType;
    private ImageView mImgTabHome;
    private ImageView mImgTabLoan;
    private ImageView mImgTabDiscover;
    private ImageView mImgTabProfile;
    private WidgetLocation homeWidgetLocation;

    private List<VersionUpdate> versionUpdateList = new ArrayList<>();
    private UpdateDialog mUpdateDialog;
    private ConfigManager mConfigManager;
    /**
     * 退出登录时的不进行检查更新
     */
    private boolean isLogout;
    /**
     * 遮盖阴影层
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HOME_NEWER_GUIDE:
                    //首页新手引导遮层
                    String oldVersion = DdApplication.getConfigManager().getFirstInstall();
                    if (!oldVersion.equals(DdApplication.getVersionName())) {
                        setHomeNewbieView();
                    }

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConfigManager = DdApplication.getConfigManager();

        //底部Tab替换类型
        mTabType = mConfigManager.getBottomTab();
        replaceTab();
        //注册广播
        mMyBroadcast = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastType.MAIN_BROADCASTRECEIVER);
        registerReceiver(mMyBroadcast, intentFilter);

        Intent intent = getIntent();
        isLogout = intent.getBooleanExtra("logout", false);
        mIndex = (Index) intent.getSerializableExtra("index");

        Intent homeIntent = new Intent(this, HomeActivity.class);            //推荐
        if (null != mIndex) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("index", mIndex);
            homeIntent.putExtras(bundle);
        }

        Intent loanIntent = new Intent(this, LoanActivity.class);            //理财
        loanIntent.putExtra("operate", "refresh");

        Intent discoverIntent = new Intent(this, DiscoverActivity.class);
        discoverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        profileIntent.putExtra("operate", "refresh");//是否刷新

        mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("Home").setIndicator("").setContent(homeIntent));
        mTabHost.addTab(mTabHost.newTabSpec("Loan").setIndicator("").setContent(loanIntent));
        mTabHost.addTab(mTabHost.newTabSpec("Discover").setIndicator("").setContent(discoverIntent));
        mTabHost.addTab(mTabHost.newTabSpec("Profile").setIndicator("").setContent(profileIntent));

        mSegmentBar = (SegmentBar) findViewById(R.id.segment_bar);
        mSegmentBar.setTabSelectionListener(mOnTabSelectionChanged);
        mSegmentBar.setTabSelectionCheckListener(mOnTabSelectionCheck);

        //检查版本更新
        if (!isLogout) {
            checkUpdate();
        }
        //菜单页首次加载
        int type = intent.getIntExtra("activityType", -1);//从启动页进入理财页
        if (type == TAB_HOME) {
            mSegmentBar.setCurrentTab(0);
        } else if (type == TAB_LOAN) {
            mSegmentBar.setCurrentTab(1);
        } else if (type == TAB_PROFILE) {
            mSegmentBar.setCurrentTab(3);
        }

        //获取IMEI
        if (EasyPermissions.hasPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE})) {
            //下载apk，并自动安装
            String imeiCode = VersionUtils.getPhoneIMEI(this);
            if (!TextUtils.isEmpty(imeiCode)) {
                DdApplication.getConfigManager().setPhoneIMEICode(MD5Utils.numberMD5(imeiCode));
            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_IMEI);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpdateDialog != null) {
            mUpdateDialog.dismiss();

        }
        unregisterReceiver();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //这是做跳转使用的
        int type = intent.getIntExtra("activityType", -1);
        if (type == TAB_LOAN) {
            mSegmentBar.setCurrentTab(1);
        } else if (type == TAB_PROFILE) {
            mSegmentBar.setCurrentTab(3);
        } else {
            mSegmentBar.setCurrentTab(0);
        }

    }

    @Override

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() != KeyEvent.KEYCODE_BACK || event.getAction() != KeyEvent.ACTION_DOWN)
            return super.dispatchKeyEvent(event);

        if ((System.currentTimeMillis() - mClickTime) > 2000) {
            AppUtils.showToast(MainActivity.this, R.string.exit_app_tip);
            mClickTime = System.currentTimeMillis();
            return true;
        } else {
            DdApplication.getInstance().exit();
        }

        return super.dispatchKeyEvent(event);
    }

    private SegmentBar.OnTabSelectionCheck mOnTabSelectionCheck = new SegmentBar.OnTabSelectionCheck() {

        @Override
        public boolean onTabSelectionCheck(int index) {

            if (index != TAB_PROFILE || mConfigManager.isLogined()) {
                /*SystemBarTintManager tintManager = new SystemBarTintManager(MainActivity.this);
                tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintResource(R.color.main_red);*/

                return true;
            }

            Intent intent = new Intent(MainActivity.this, LoginInAdvanceActivity.class);
            intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_MAIN);
            startActivity(intent);
            return false;
        }

    };

    private SegmentBar.OnTabSelectionChanged mOnTabSelectionChanged = new SegmentBar.OnTabSelectionChanged() {

        @Override
        public void onTabSelectionChanged(int index) {
            mTabHost.setCurrentTab(index);

        }

    };

    /**
     * 检查版本更新
     */
    private void checkUpdate() {

        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("osType", AppConstant.OS_TYPE);
        datas.put("version", AppUtils.getApplicationVersion());

        DdApplication.getCommonManager().checkUpdate(datas, mOnCheckUpdateFinishedListener);
    }

    private CommonManager.OnCheckUpdateFinishedListener mOnCheckUpdateFinishedListener = new CommonManager.OnCheckUpdateFinishedListener() {

        @Override
        public void onCheckUpdateFinished(Response response, VersionInfo versionInfo) {

            String version = "";
            if (versionInfo!=null) {
               version = versionInfo.getLastVersion();
            }

            if (TextUtils.isEmpty(version)){
                mConfigManager.setCheckVersion("");
            }
            if (!response.isSuccess() || versionInfo.isNeedUpdate()) {

                if (versionInfo != null) {

                    List<String> changeLogs = versionInfo.getChangeLogs();
                    forceUpdate = versionInfo.getForceUpdate();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < changeLogs.size(); i++) {
                        stringBuilder.append(i == 0 ? "" : "\n").append(changeLogs.get(i));
                    }
                    final String url = versionInfo.getDownloadUrl();
                    final String content = stringBuilder.toString();

                    String fileSize = versionInfo.getFileSize();

                    //运行到这里是检查到新版本需要更新，传递给我的界面
                    mConfigManager.setVersionUpdate(true);
                    VersionUpdate versionUpdate = new VersionUpdate();
                    versionUpdate.setDownloadUrl(url);
                    versionUpdate.setContent(content);
                    versionUpdate.setVersion(version);
                    versionUpdate.setFilesize(fileSize);
                    versionUpdate.setForceUpdate(forceUpdate);
                    versionUpdateList.add(versionUpdate);
                    mConfigManager.setVersionUpdateList(versionUpdateList);

                    if (forceUpdate.equals("Y")) {//强制更新必须 弹窗
                        showUpdateDialog(url, version, content, forceUpdate, fileSize);
                    }

                    //普通更新窗口提示一次弹窗，强制更新窗口总提示弹窗
                    String mMemoryVersion = mConfigManager.getCheckVersion();
                    if (!TextUtils.isEmpty(mMemoryVersion)){
                        int memoryVersion = Integer.valueOf(mMemoryVersion.replaceAll("\\.", ""));
                        int newVersion = Integer.valueOf(version.replaceAll("\\.", ""));
                        if (newVersion > memoryVersion){
                            //版本更新弹窗
                            showUpdateDialog(url, version, content, forceUpdate, fileSize);
                        }else {
                            isNeedUpdateDialog = false;
                            mConfigManager.setCheckVersion(version);
                        }
                    } else {
                        showUpdateDialog(url, version, content, forceUpdate, fileSize);
                    }

                }
            } else {
                DdApplication.getConfigManager().setVersionUpdate(false);
            }

            //活动广告弹窗,无更新窗口时 显示弹窗广告
            if (!isNeedUpdateDialog&&!"Y".equals(forceUpdate)) {
                //新老版本对比，不是第一次安装才弹出弹框。第一次安装时优先弹出首页新手引导。
                String oldVersion = mConfigManager.getFirstInstall();
                if (oldVersion.equals(DdApplication.getVersionName())) {
                    actionDialog();
                }
            }

        }

    };

    /**
     * 活动弹窗
     */
    public void actionDialog() {
        if (mIndex != null) {
            List<PopAd> mPopAdList = mIndex.getPopAdList();

            if (mPopAdList.size() > 0 && !TextUtils.isEmpty(mPopAdList.get(0).getImgUrl())) {
                ActionDialog mActionDialog = new ActionDialog(this, mSegmentBar, mPopAdList);
                mActionDialog.showDialog();
            }
        }
    }

    /**
     * 广播
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean redPacket = intent.getBooleanExtra("redPacket", false);    //红包点
            homeWidgetLocation = (WidgetLocation) intent.getSerializableExtra("homeWidgetFrame");
            boolean permissions = intent.getBooleanExtra("permissions", false);//开售提醒获取日历权限
            if (permissions) {
                if (!EasyPermissions.hasPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR})) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALENDAR}, 100);
                }
            }

            if (redPacket) {
                if (mTabType == 0) {
                    mImgTabProfile.setImageResource(R.drawable.icon_tab_profile_dot);
                }
                if (mTabType == 1) {
                    mImgTabProfile.setImageResource(R.drawable.icon_tab_new_profile_dot);
                }

            } else {
                if (mTabType == 0) {
                    mImgTabProfile.setImageResource(R.drawable.icon_tab_profile);
                }
                if (!mConfigManager.getVersionUpdate()) {
                    mImgTabProfile.setImageResource(R.drawable.icon_tab_profile);
                } else {
                    mImgTabProfile.setImageResource(R.drawable.icon_tab_profile_dot);
                }
                if (mTabType == 1) {
                    mImgTabProfile.setImageResource(R.drawable.icon_tab_new_profile);
                }
            }


            //首页引导  遮盖层 白色区域的位置信息

            if (homeWidgetLocation != null && homeWidgetLocation.getViewWidth() > 0 && homeWidgetLocation.getViewHeight() > 0) {
                handler.sendEmptyMessage(HOME_NEWER_GUIDE);
            }

        }
    }

    /**
     * 注销广播
     */
    public void unregisterReceiver() {

        if (mMyBroadcast != null) {
            unregisterReceiver(mMyBroadcast);
            mMyBroadcast = null;
        }
    }

    /**
     * 首页了解金桔蒙层
     */
    private void setHomeNewbieView() {
        NewbieGuideManager newbieGuideManager = new NewbieGuideManager(MainActivity.this, NewbieGuideManager.TYPE_HOME_GUIDE, new NewbieGuideManager.OnGuideRemoveListener() {
            @Override
            public void guideShow() {
                //保存设置最新版本
                mConfigManager.setFirstInstall(DdApplication.getVersionName());
            }

            @Override
            public void guideRemove() {
                Intent intent = new Intent(MainActivity.this, BaseJsBridgeWebViewActivity.class);
                intent.putExtra("url", "http://www.duoduojr.cn/apphtml/web/about-us-long-page/index.html");
                startActivity(intent);
            }

        });
        newbieGuideManager.addLocationView(homeWidgetLocation, HoleBean.TYPE_RECTANGLE);
        newbieGuideManager.show();

    }

    /**
     * 替换底部tab
     */
    public void replaceTab() {
        mImgTabHome = (ImageView) findViewById(R.id.img_tab_home);
        mImgTabLoan = (ImageView) findViewById(R.id.img_tab_loan);
        mImgTabDiscover = (ImageView) findViewById(R.id.img_tab_discover);
        mImgTabProfile = (ImageView) findViewById(R.id.img_tab_profile);
        //0代表本地tab，1代表替换成 新春快乐tab
        if (mTabType == 0) {
            mImgTabHome.setImageResource(R.drawable.icon_tab_home);
            mImgTabLoan.setImageResource(R.drawable.icon_tab_loan);
            mImgTabDiscover.setImageResource(R.drawable.icon_tab_discover);
            mImgTabProfile.setImageResource(R.drawable.icon_tab_profile);
        }
        if (mTabType == 1) {
            mImgTabHome.setImageResource(R.drawable.icon_tab_new_home);
            mImgTabLoan.setImageResource(R.drawable.icon_tab_new_loan);
            mImgTabDiscover.setImageResource(R.drawable.icon_tab_new_discover);
            mImgTabProfile.setImageResource(R.drawable.icon_tab_new_profile);
        }
    }

    /**
     * 版本更新窗口
     * @param url
     * @param version
     * @param content
     * @param forceUpdate
     * @param fileSize
     */
    private void showUpdateDialog(String url,String version,String content,String forceUpdate,String fileSize) {
        mUpdateDialog = new UpdateDialog(MainActivity.this);
        mUpdateDialog.initUpdateDialog(url, version,content, forceUpdate, fileSize);
        mUpdateDialog.show();
        mConfigManager.setCheckVersion(version);
        isNeedUpdateDialog = true;
        //有更新时 不弹出首页新手引导
        mConfigManager.setFirstInstall(DdApplication.getVersionName());
    }

}