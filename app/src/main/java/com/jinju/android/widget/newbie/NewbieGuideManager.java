package com.jinju.android.widget.newbie;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.jinju.android.R;
import com.jinju.android.api.WidgetLocation;


/**
 * NewbieGuideManager
 */
public class NewbieGuideManager {

    private static final String TAG = "newbie_guide";
    public static final int TYPE_PROFIRE_GUIDE = 0;//button
    public static final int TYPE_HOME_GUIDE = 1;//了解朵朵

    private Activity mActivity;
    private NewbieGuide mNewbieGuide;
    private int mType;
    public NewbieGuideManager(Activity activity, int type,final OnGuideRemoveListener mGuideRemoveListener) {
        mActivity = activity;
        mNewbieGuide = new NewbieGuide(activity);

        mType = type;
        mNewbieGuide.setOnGuideChangedListener(new NewbieGuide.OnGuideChangedListener() {
            @Override
            public void onShowed() {
                mGuideRemoveListener.guideShow();
            }

            @Override
            public void onRemoved() {
                mGuideRemoveListener.guideRemove();
            }
        });
    }

    /**
     * 有指定控件 添加高亮
     */
    public NewbieGuideManager addView(View view, int shape) {
        mNewbieGuide.addHighLightView(view, shape);
        return this;
    }
    /**
     * 有 控件指定坐标 添加高亮
     */
    public NewbieGuideManager addLocationView(WidgetLocation widgetLocation, int shape){

        mNewbieGuide.addHighLightView2(widgetLocation,shape);
        return this;
    }

    public void show() {
        show(0);
    }

    public void show(int delayTime) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_PROFIRE_GUIDE:
                        mNewbieGuide.setEveryWhereTouchable(false)
                                .addRedPacketImg(R.mipmap.img_gift_bg, ScreenUtils.dpToPx(mActivity, 80))
                                .addBtnRedPacketImg(R.mipmap.img_gift_look_btn, ScreenUtils.dpToPx(mActivity,300))
                                .show();
                        break;
                    case TYPE_HOME_GUIDE:
                        //添加一个View,样式设置透明，添加点击事件
                        mNewbieGuide.setEveryWhereTouchable(false)
                                .addIntroduceTipsImg(R.mipmap.img_home_understand, ScreenUtils.dpToPx(mActivity, 130), ScreenUtils.dpToPx(mActivity, 310))
                                .addKnowTipsImg(R.mipmap.img_home_know, ScreenUtils.dpToPx(mActivity, 220), ScreenUtils.dpToPx(mActivity, 400))
                                .show();
                        break;
                }
            }
        }, delayTime);
    }


    /**
     * 判断新手引导也是否已经显示了
     */
    public static boolean isNeverShowed(Activity activity, int type) {
        return activity.getSharedPreferences(TAG, Activity.MODE_PRIVATE).getBoolean(TAG + type, true);
    }

    public interface OnGuideRemoveListener {
        void guideShow();
        void guideRemove();
    }

}

