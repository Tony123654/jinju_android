package com.jinju.android.widget.newbie;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.view.View;

/**
 * HoleBean
 */
public class HoleBean {

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_RECTANGLE = TYPE_CIRCLE + 1;
    public static final int TYPE_OVAL = TYPE_RECTANGLE + 1;

    private View mHole;
    private int mType;

    private int x;
    private int y;
    private int viewWidth;
    private int viewHeight;


    private Context mContext;

    public HoleBean(Context context, View hole, int type) {
        this.mContext = context;    //为了调取原生资源
        this.mHole = hole;
        this.mType = type;
    }

    public HoleBean(Context context, int xx, int yy, int viewWidth, int viewHeight, int type) {
        this.mContext = context;    //为了调取原生资源
        x = xx; //屏幕中x位置
        y = yy; //屏幕中y位置
        this.viewWidth = viewWidth;  //高亮区域宽度
        this.viewHeight =viewHeight; //高亮区域高度
        this.mType = type;
    }


    public int getRadius() {
        return mHole != null ? Math.min(mHole.getWidth(), mHole.getHeight()) / 2 : 0;
    }

    public RectF getRectF() {

        int statusBarHeight = ScreenUtils.getStatusBarHeight((Activity) mContext);

        RectF rectF = new RectF();
        if (mHole != null) {
            //有指定控件，直接获取位置信息
            int[] location = new int[2];
            mHole.getLocationOnScreen(location);
            rectF.left = location[0];
            rectF.top = location[1] - statusBarHeight;
            rectF.right = location[0] + mHole.getWidth();
            rectF.bottom = location[1] + mHole.getHeight() - statusBarHeight;
        } else {
            //有指定位置坐标
            if (viewWidth>0||viewHeight>0) {
                rectF.left = x ;
                rectF.top = y - statusBarHeight;
                rectF.right = x + viewWidth;
                rectF.bottom = y + viewHeight - statusBarHeight;
            }

        }
        return rectF;
    }

    public int getType() {
        return mType;
    }

}