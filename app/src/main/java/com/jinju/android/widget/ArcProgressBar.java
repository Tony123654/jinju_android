package com.jinju.android.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.jinju.android.R;

/**
 * Created by Libra on 2017/10/11.
 *
 *
 *  圆弧进度 滑动渐变
 */

public class ArcProgressBar extends View {
    private Paint paint;
    private Paint progressPaint;
    /**
     * 圆环的进度宽度
     */
    private float progressWidth;
    /**
     * 圆环背景的颜色
     */
    private int backgroundColor;

    /**
     * 动画进度百分比
     */
    private float animatorPercent;
    /**
     * 圆环进度渐变颜色
     */
//    private int[] doughnutColors = new int[]{getResources().getColor(R.color.main_red),getResources().getColor(R.color.main_tag_text_red),getResources().getColor(R.color.main_tag_text_red)};

    private int[] doughnutColors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};

    private RectF mRectF;
    private ValueAnimator progressAnimator;
    private float progress;
    private static final long DEFAULT_DURATION = 1000;
    private SweepGradient mSweepGradient;
    private Matrix rotateMatrix;

    public ArcProgressBar(Context context) {
        this(context, null);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ArcProgressBar);
        progressWidth = mTypedArray.getDimension(R.styleable.ArcProgressBar_arc_progressWidth, 5);
        backgroundColor = mTypedArray.getColor(R.styleable.ArcProgressBar_arc_backgroundColor,Color.GRAY);
        progress = mTypedArray.getFloat(R.styleable.ArcProgressBar_arc_progress,0);

        //初始背景画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(progressWidth);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);  //消除锯齿
        //当前进度的弧形画笔
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        rotateMatrix = new Matrix();
        mRectF = new RectF();

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        float radius = progressWidth /2;
        float roundWidth = getWidth()-radius;
        //圆弧的背景
        paint.setColor(backgroundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(progressWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿

        mRectF.set(radius,radius,roundWidth,roundWidth);
        canvas.drawArc(mRectF,150,240, false, paint);

        /**
         * 画圆环的进度
         */

        if(animatorPercent > 0){

            progressPaint.setStrokeWidth(progressWidth); //设置圆环的宽度
            progressPaint.setStyle(Paint.Style.STROKE);
            progressPaint.setAntiAlias(true);  //消除锯齿

            //设置渐变色
            if (mSweepGradient == null) {
                mSweepGradient = new SweepGradient(roundWidth/ 2,roundWidth/ 2, doughnutColors, null);
            }
            rotateMatrix.setRotate(130, roundWidth/2,roundWidth/2);
            mSweepGradient.setLocalMatrix(rotateMatrix);
            progressPaint.setShader(mSweepGradient);

            canvas.drawArc(mRectF, 150, 240*animatorPercent , false, progressPaint);  //根据进度画圆弧
        }

        canvas.restore();
    }
    /**
     * 进度条滑动动画
     * @param startProgress
     */
    private void setAnimation(final float startProgress) {

        if (progressAnimator != null) {
            progressAnimator.cancel();
        }

        progressAnimator = ValueAnimator.ofFloat(0, startProgress);
        progressAnimator.setDuration(DEFAULT_DURATION);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }


    public synchronized void setProgress(float progress) {
        this.progress = progress;
        setAnimation(progress);
        postInvalidate();
    }

}
