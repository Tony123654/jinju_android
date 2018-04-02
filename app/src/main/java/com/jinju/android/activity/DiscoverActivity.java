package com.jinju.android.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jinju.android.R;
import com.jinju.android.api.Discover;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.fragment.PastActFragment;
import com.jinju.android.fragment.WonderfulActFragment;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DDJRCmdUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.util.ViewUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Libra on 2017/11/1.
 * 发现页
 */

public class DiscoverActivity extends AppCompatActivity implements View.OnClickListener {


    //顶部布局内容
    private ImageView discoverImg1;
    private ImageView discoverImg2;
    private ImageView discoverImg3;
    private ImageView discoverImg4;

    private List<Discover> mDiscoverTopImgList; //用来存放Top布局数据
    private List<Fragment> mFragmentList;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private DiscoverViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DdApplication.getInstance().insertActivity(this);

        setContentView(R.layout.activity_discover);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDiscoverList(1);
    }


    @Override
    protected void onDestroy() {
        DdApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    private void initView() {

        //初始化Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTabLayout = (TabLayout) findViewById(R.id.toolbar_tab);
        mViewPager = (ViewPager) findViewById(R.id.main_vp_container);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new WonderfulActFragment());
        mFragmentList.add(new PastActFragment());
        adapter = new DiscoverViewPagerAdapter(getSupportFragmentManager());
        setUpIndicatorWidth();

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (mViewPager));

        mDiscoverTopImgList = new ArrayList<Discover>();

        //Top四张图
        discoverImg1 = (ImageView) findViewById(R.id.discover_top_pic1);
        discoverImg2 = (ImageView) findViewById(R.id.discover_top_pic2);
        discoverImg3 = (ImageView) findViewById(R.id.discover_top_pic3);
        discoverImg4 = (ImageView) findViewById(R.id.discover_top_pic4);
        //Top四布局 and 监听
        discoverImg1.setOnClickListener(this);
        discoverImg2.setOnClickListener(this);
        discoverImg3.setOnClickListener(this);
        discoverImg4.setOnClickListener(this);


        //屏幕宽度
        int headerWidth = ViewUtils.getScreenWidth(DiscoverActivity.this);
        int imageWidth = (headerWidth - ViewUtils.dip2px(DiscoverActivity.this, 36)) / 2;
        ViewGroup.LayoutParams params = discoverImg1.getLayoutParams();
        params.width = imageWidth;
        params.height = imageWidth * 7 / 17;
        discoverImg1.setLayoutParams(params);
        discoverImg2.setLayoutParams(params);
        discoverImg3.setLayoutParams(params);
        discoverImg4.setLayoutParams(params);

    }

    /**
     * 顶部图片展示
     */
    private void setupView() {
        if (mDiscoverTopImgList != null && mDiscoverTopImgList.size() > 0) {
            ImageUtils.discoverDisplayImage(discoverImg1, mDiscoverTopImgList.get(0).getPic());
            ImageUtils.discoverDisplayImage(discoverImg2, mDiscoverTopImgList.get(1).getPic());
            ImageUtils.discoverDisplayImage(discoverImg3, mDiscoverTopImgList.get(2).getPic());
            ImageUtils.discoverDisplayImage(discoverImg4, mDiscoverTopImgList.get(3).getPic());
        }
    }

    /**
     * 加载数据
     */
    private void getDiscoverList(int currentPage) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
        datas.put("currentPage", currentPage);
        datas.put("iv", VersionUtils.getVersionName());


        DdApplication.getLoanManager().getDiscoverList(datas, mOnGetDiscoverListFinishedListener);
    }

    private LoanManager.OnGetDiscoverListFinishedListener mOnGetDiscoverListFinishedListener = new LoanManager.OnGetDiscoverListFinishedListener() {
        @Override
        public void onGetDiscoverListFinished(Response response, Page page, List<Discover> discoverList,
                                              List<Discover> endDiscoverList, List<Discover> mDiscoverTopImg) {

            if (response.isSuccess()) {
                //顶部四图片的数据
                mDiscoverTopImgList.addAll(mDiscoverTopImg);
                setupView();

            } else {
                AppUtils.handleErrorResponse(DiscoverActivity.this, response);
            }

        }
    };

    /**
     * 推进合规、安全保障、新手引导、关于朵朵
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        String url = "";
        switch (v.getId()) {
            case R.id.discover_top_pic1: //推进合规
                if (mDiscoverTopImgList != null && mDiscoverTopImgList.size() > 0) {
                    url = mDiscoverTopImgList.get(0).getLinkUrl();
                }

                if (url.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                    //点击webview上的控件，执行指定跳转
                    DDJRCmdUtils.handleProtocol(DiscoverActivity.this, url);
                } else {
                    intent = new Intent(DiscoverActivity.this, BaseJsBridgeWebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }

                break;
            case R.id.discover_top_pic2: //安全保障
                if (mDiscoverTopImgList != null && mDiscoverTopImgList.size() > 1) {
                    url = mDiscoverTopImgList.get(1).getLinkUrl();
                }

                if (url.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                    DDJRCmdUtils.handleProtocol(DiscoverActivity.this, url);
                } else {
                    intent = new Intent(DiscoverActivity.this, BaseJsBridgeWebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }

                break;
            case R.id.discover_top_pic3: //新手引导
                if (mDiscoverTopImgList != null && mDiscoverTopImgList.size() > 2) {
                    url = mDiscoverTopImgList.get(2).getLinkUrl();
                }

                if (url.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                    DDJRCmdUtils.handleProtocol(DiscoverActivity.this, url);
                } else {
                    intent = new Intent(DiscoverActivity.this, BaseJsBridgeWebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }

                break;
            case R.id.discover_top_pic4: //关于朵朵
                if (mDiscoverTopImgList != null && mDiscoverTopImgList.size() > 3) {
                    url = mDiscoverTopImgList.get(3).getLinkUrl();
                }

                if (url.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                    DDJRCmdUtils.handleProtocol(DiscoverActivity.this, url);
                } else {
                    intent = new Intent(DiscoverActivity.this, BaseJsBridgeWebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                break;
        }
    }

    private void setUpIndicatorWidth() {
        Class<?> tabLayoutClass = mTabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(mTabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(80);
                    params.setMarginEnd(80);
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    class DiscoverViewPagerAdapter extends FragmentPagerAdapter {


        public DiscoverViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }

}
