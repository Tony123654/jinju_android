package com.jinju.android.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import com.jinju.android.R;

/**
 * Created by Libra on 2017/9/18.
 */

public class LoadingView extends View{
    protected static final float DEFAULT_SIZE = 56.0f;
    private static final int OUTER_CIRCLE_ANGLE = 270;
    private static final int INTER_CIRCLE_ANGLE = 90;

    protected static final long ANIMATION_START_DELAY = 333;
    protected static final long mDuration = 1500;


    private float mAllSize;
    private float mViewWidth;
    private float mViewHeight;

    private Paint mStrokePaint;

    private RectF mOuterCircleRectF;
    private RectF mInnerCircleRectF;
    //旋转角度
    private int mRotateAngle;

    private ValueAnimator mFloatValueAnimator;


    public LoadingView(Context context) {
        super(context);
        init(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    private void init(final Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }
        TypedArray typedArray = null;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);

        typedArray.recycle();
        mAllSize = dip2px(context, DEFAULT_SIZE * 0.5f - 10);
        mViewWidth = dip2px(context, DEFAULT_SIZE);
        mViewHeight = dip2px(context, DEFAULT_SIZE);
        initParams();
    }

    private void initParams() {
        //最大尺寸
        float outR = mAllSize;
        //小圆尺寸
        float inR = outR * 0.6f;
        //初始化画笔
        initPaint(inR * 0.4f);
        //旋转角度
        mRotateAngle = 0;
        //圆范围
        mOuterCircleRectF = new RectF();
        mOuterCircleRectF.set(getViewCenterX() - outR, getViewCenterY() - outR, getViewCenterX() + outR, getViewCenterY() + outR);
        mInnerCircleRectF = new RectF();
        mInnerCircleRectF.set(getViewCenterX() - inR, getViewCenterY() - inR, getViewCenterX() + inR, getViewCenterY() + inR);
        initAnimators();
    }

    /**
     * 初始化画笔
     */
    private void initPaint(float lineWidth) {
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(lineWidth);
        mStrokePaint.setColor(Color.WHITE);
        mStrokePaint.setDither(true);
        mStrokePaint.setFilterBitmap(true);
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        mStrokePaint.setStrokeJoin(Paint.Join.ROUND);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置控件的大小
        setMeasuredDimension((int)Math.ceil(mViewWidth),(int)Math.ceil(mViewHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        //外圆
        canvas.drawArc(mOuterCircleRectF, mRotateAngle % 360, OUTER_CIRCLE_ANGLE, false, mStrokePaint);
        //内圆
        canvas.drawArc(mInnerCircleRectF, 270 - mRotateAngle % 360, INTER_CIRCLE_ANGLE, false, mStrokePaint);

        canvas.restore();


    }
    private void initAnimators() {
        mFloatValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mFloatValueAnimator.setRepeatCount(Animation.INFINITE);
        mFloatValueAnimator.setDuration(mDuration);
        mFloatValueAnimator.setStartDelay(ANIMATION_START_DELAY);
        mFloatValueAnimator.setInterpolator(new LinearInterpolator());
        mFloatValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //旋转
                mRotateAngle = (int) (360 *  (float) animation.getAnimatedValue());

                invalidate();
            }

        });


    }

    public void start() {
        mFloatValueAnimator.start();
    }
    public void stop() {
        mFloatValueAnimator.end();
    }

    protected final float getViewCenterX() {
        return mViewWidth * 0.5f;
    }

    protected final float getViewCenterY() {
        return mViewHeight * 0.5f;
    }

    protected static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }

}
