package com.jinju.android.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Libra on 2018/1/10.
 */

public class FrameAnimationHeader extends RelativeLayout  implements RefreshHeader {
    protected int mPaddingTop = 20;
    protected int mPaddingBottom = 20;
    private TextView mTitleText;
    private ImageView imgDefault;
    private ImageView imgRefreshing;
    private ImageView imgFinish;
    private AnimationDrawable defaultDrawable;
    private AnimationDrawable refreshDrawable;
    private AnimationDrawable finishDrawable;
    private String mTextString;
    public FrameAnimationHeader(Context context) {
        super(context);
        this.initView(context, null);
    }

    public FrameAnimationHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public FrameAnimationHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs);
    }
    private void initView(Context context, AttributeSet attrs) {
        LinearLayout layout = new LinearLayout(context);

        layout.setId(android.R.id.widget_frame);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lpHeaderText = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        mTitleText = new TextView(context);
        mTitleText.setText("下拉刷新");
        FrameLayout frameLayout = new FrameLayout(context);
        //默认动画
        imgDefault = new ImageView(context);
        imgDefault.setImageResource(R.drawable.pull_refresh_down);
        defaultDrawable = (AnimationDrawable) imgDefault.getDrawable();
        //正在刷新动画
        imgRefreshing = new ImageView(context);
        imgRefreshing.setVisibility(VISIBLE);
        imgRefreshing.setImageResource(R.drawable.pull_refreshing);
        refreshDrawable = (AnimationDrawable) imgRefreshing.getDrawable();
        //刷新完成动画
        imgFinish = new ImageView(context);
        imgFinish.setVisibility(GONE);
        imgFinish.setImageResource(R.drawable.pull_refresh_finish);
        finishDrawable = (AnimationDrawable) imgFinish.getDrawable();
        frameLayout.addView(imgDefault);
        frameLayout.addView(imgRefreshing);
        frameLayout.addView(imgFinish);
        layout.addView(frameLayout);
        layout.addView(mTitleText,lpHeaderText);

        //控件水平居中
        LayoutParams lpHeaderLayout = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpHeaderLayout.addRule(CENTER_HORIZONTAL);
        addView(layout,lpHeaderLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            setPadding(getPaddingLeft(), 0, getPaddingRight(), 0);
        } else {
            setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingBottom);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        //去加载数据

    }
    //正在刷新
    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

        imgRefreshing.setVisibility(VISIBLE);
        imgDefault.setVisibility(GONE);
        imgFinish.setVisibility(GONE);
        refreshDrawable.start();
        defaultDrawable.selectDrawable(0);
        defaultDrawable.stop();

    }
    //手指下拉
    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

        imgDefault.setVisibility(VISIBLE);
        imgRefreshing.setVisibility(GONE);
        imgFinish.setVisibility(GONE);
        refreshDrawable.selectDrawable(0);
        finishDrawable.selectDrawable(0);
        refreshDrawable.stop();
        finishDrawable.stop();
        defaultDrawable.start();
    }

    public void onRefreshFinishAnimation(String textTitle){
        setTitleText(textTitle);
                if (TextUtils.isEmpty(mTextString)) {
                    mTitleText.setText("刷新完成");
                }

                finishDrawable.selectDrawable(0);
                imgRefreshing.setVisibility(GONE);
                refreshDrawable.stop();
                imgFinish.setVisibility(VISIBLE);
                finishDrawable.start();
    }

//    @Override
//    public void onRefreshFinishAnimation(String textTitle) {
//
//        setTitleText(textTitle);
//        if (TextUtils.isEmpty(mTextString)) {
//            mTitleText.setText("刷新完成");
//        }
//
//        finishDrawable.selectDrawable(0);
//        imgRefreshing.setVisibility(GONE);
//        refreshDrawable.stop();
//        imgFinish.setVisibility(VISIBLE);
//        finishDrawable.start();
//    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//变换方式必须填写
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:

                if (TextUtils.isEmpty(mTextString)) {
                    mTitleText.setText("下拉刷新");
                } else {
                    mTitleText.setText(mTextString);
                }

                break;
            case PullDownToRefresh:
                if (TextUtils.isEmpty(mTextString)) {
                    mTitleText.setText("下拉刷新");
                }else {
                    mTitleText.setText(mTextString);
                }
                break;
            case Refreshing:
                if (TextUtils.isEmpty(mTextString)) {
                    mTitleText.setText("正在刷新...");
                }
                break;
            case RefreshReleased:
                break;
            case ReleaseToRefresh:
                break;
//            case ReleaseToTwoLevel:
//                break;
            case Loading:
                break;
        }
    }
    public void  setTitleText(String title) {
        mTextString = title;
    }
}
