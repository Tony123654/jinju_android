package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.constant.CouponStatus;
import com.jinju.android.fragment.MyRedPacketFragment;
import com.jinju.android.fragment.RateRedPacketFragment;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class RedPacketActivity extends BaseFragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;

    private TextView mTxtMyRedPacket;
    private TextView mTxtHistoryRedPacket;

    private MyRedPacketFragment mMyRedPacketFragment;
    private RateRedPacketFragment mRateRedPacketFragment;

    private List<Fragment> mFragmentList;

    private static final int MY_RED_PACKET_FRAGMENT = 0;
    private static final int RATE_RED_PACKET_FRAGMENT = 1;
    private LinearLayout mLayoutCheckRedPacket;
    private TranslateAnimation animation;
    private String mCouponRules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet);

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.profile_coupon);
        ImageView mImgRight = (ImageView) findViewById(R.id.img_right);
        mImgRight.setVisibility(View.VISIBLE);
        mImgRight.setImageResource(R.mipmap.icon_coupon_rules);
        mImgRight.setOnClickListener(mImgRightOnClickListener);
        //查看可用紅包
        mLayoutCheckRedPacket = (LinearLayout) findViewById(R.id.layout_check_red_packet);
        mLayoutCheckRedPacket.setOnClickListener(mLayoutCheckRedPacketOnClickListener);
        slideView(80,0);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTxtMyRedPacket = (TextView) findViewById(R.id.txt_my_red_packet);
        mTxtHistoryRedPacket = (TextView) findViewById(R.id.txt_history_red_packet);

        mTxtMyRedPacket.setOnClickListener(this);
        mTxtHistoryRedPacket.setOnClickListener(this);


        mMyRedPacketFragment = new MyRedPacketFragment();
        mRateRedPacketFragment = new RateRedPacketFragment();

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(mMyRedPacketFragment);
        mFragmentList.add(mRateRedPacketFragment);


        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mMyRedPacketFragment.setOnMyRedPacketListener(mOnMyRedPacketListener);
        mMyRedPacketFragment.setOnGetCouponRuleListener(mOnGetCouponRuleListener);
        mRateRedPacketFragment.setOnRateRedPacketListener(mOnRateRedPacketListener);
    }
    //可用
    private View.OnClickListener mLayoutCheckRedPacketOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentItem = mViewPager.getCurrentItem();
            changeView(currentItem);
            mLayoutCheckRedPacket.setVisibility(View.GONE);
            mMyRedPacketFragment.refreshMyRedPacket(CouponStatus.CAN_USE);
            mRateRedPacketFragment.refreshRate(CouponStatus.CAN_USE);
        }
    };
    private View.OnClickListener mBtnBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private View.OnClickListener mImgRightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(mCouponRules)) {
                Intent intent = new Intent(RedPacketActivity.this, BaseJsBridgeWebViewActivity.class);
                intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mCouponRules);
                startActivity(intent);
            }

        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_my_red_packet:
                changeView(MY_RED_PACKET_FRAGMENT);
                break;
            case R.id.txt_history_red_packet:
                changeView(RATE_RED_PACKET_FRAGMENT);
                break;
            default:
                break;
        }
    }

    class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
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


        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            int currentItem = mViewPager.getCurrentItem();
            switch (currentItem) {
                case MY_RED_PACKET_FRAGMENT:
                    mTxtMyRedPacket.setTextColor(getResources().getColor(R.color.light_red));
                    mTxtMyRedPacket.setBackgroundResource(R.mipmap.bg_loan_tab);
                    mTxtHistoryRedPacket.setTextColor(getResources().getColor(R.color.word_black));
                    mTxtHistoryRedPacket.setBackgroundResource(R.color.white);
                    break;
                case RATE_RED_PACKET_FRAGMENT:
                    mTxtMyRedPacket.setTextColor(getResources().getColor(R.color.word_black));
                    mTxtMyRedPacket.setBackgroundResource(R.color.white);
                    mTxtHistoryRedPacket.setTextColor(getResources().getColor(R.color.light_red));
                    mTxtHistoryRedPacket.setBackgroundResource(R.mipmap.bg_loan_tab);
                    break;
                default:
                    break;
            }
        }
    }

    private void changeView(int desTab) {
        mViewPager.setCurrentItem(desTab, true);
    }

    /**
     * 红包界面切换监听
     */
    private MyRedPacketFragment.OnMyRedPacketListener mOnMyRedPacketListener = new MyRedPacketFragment.OnMyRedPacketListener() {

        @Override
        public void onCurrentView(String type,String couponRules) {
            mCouponRules = couponRules;
            if (type.equals(CouponStatus.CAN_USE)) {
                mLayoutCheckRedPacket.setVisibility(View.GONE);
//                mMyRedPacketFragment.refreshMyRedPacket(CouponStatus.CAN_USE);
//                mRateRedPacketFragment.refreshRate(CouponStatus.CAN_USE);
            }
            if (type.equals(CouponStatus.HISTORY)) {

                mLayoutCheckRedPacket.setVisibility(View.VISIBLE);
                mLayoutCheckRedPacket.startAnimation(animation);
                mMyRedPacketFragment.refreshMyRedPacket(CouponStatus.HISTORY);
                mRateRedPacketFragment.refreshRate(CouponStatus.HISTORY);
            }
        }

    };

    /**
     * 加息券界面切换监听
     */
    private RateRedPacketFragment.OnRateRedPacketListener mOnRateRedPacketListener = new RateRedPacketFragment.OnRateRedPacketListener() {

        @Override
        public void onCurrentView(String type,String couponRules) {
            mCouponRules = couponRules;
            if (type.equals(CouponStatus.CAN_USE)) {
                mLayoutCheckRedPacket.setVisibility(View.GONE);
            }
            if (type.equals(CouponStatus.HISTORY)) {

                mLayoutCheckRedPacket.setVisibility(View.VISIBLE);
                mLayoutCheckRedPacket.startAnimation(animation);
                mMyRedPacketFragment.refreshMyRedPacket(CouponStatus.HISTORY);
                mRateRedPacketFragment.refreshRate(CouponStatus.HISTORY);
            }
            changeView(RATE_RED_PACKET_FRAGMENT);
        }

    };
    /**
     * 红包页获取红包规则地址
     */
    private MyRedPacketFragment.OnGetCouponRuleListener mOnGetCouponRuleListener = new MyRedPacketFragment.OnGetCouponRuleListener() {
        @Override
        public void getCouponRule(String couponRule) {
            mCouponRules = couponRule;
        }
    };
    private void slideView(final float p1,final float p2) {

        animation = new TranslateAnimation(p1,p2,0, 0);
        animation.setDuration(1000);//设置动画持续时间
        animation.setRepeatCount(0);//设置重复次数
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (p1 > 0) {//左移 显示按钮
                    mLayoutCheckRedPacket.clearAnimation();
                }
                if (p2 > 0 ) {//右移  隐藏按钮
                    mLayoutCheckRedPacket.clearAnimation();
                    mLayoutCheckRedPacket.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
