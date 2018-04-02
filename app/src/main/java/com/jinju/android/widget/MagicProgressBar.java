package com.jinju.android.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.jinju.android.R;

/**
 * Created by Libra on 2017/9/13.
 *
 * 水平滑动渐变
 */

public class MagicProgressBar extends View  {
    /**
     * 填充色
     */
    private int fillColor;

    /**
     * 渐变开始色
     */
    private int startColor;
    /**
     * 渐变结束色
     */
    private int endColor;
    /**
     * 背景色
     */
    private int backgroundColor;
    /**
     * 字体色
     */
    private int textColor;
    /**
     * 顶部气泡字体色
     */
    private int bubbleTextColor;
    /**
     * 进度百分比
     */
    private float percent;
    /**
     * 动画进度百分比
     */
    private float animatorPercent;
    /**
     * 两边是否圆角
     */
    private boolean isFlat;
    private int duration;

    private Paint fillPaint;
    private Paint backgroundPaint;
    private Paint textPaint;
    private Paint bubbleTextPaint;

    private ValueAnimator progressAnimator;
    private LinearGradient lg;
    private PaintFlagsDrawFilter mDrawFilter;

    private int maxPercentTextWidth = 0;
    private float textPaddingLeft;//字体距左
    private float progressHeight;//进度条的高
    private float maxPercentTextHeight;


    public MagicProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public MagicProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MagicProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MagicProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    private void init(final Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.MagicProgressBar);
            percent = typedArray.getFloat(R.styleable.MagicProgressBar_mpb_percent, 0);
            fillColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_fillColor, Color.BLACK);
            backgroundColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_backgroundColor, Color.GRAY);
            //两边是否圆角
            isFlat = typedArray.getBoolean(R.styleable.MagicProgressBar_mpb_flat, false);
            textPaddingLeft = typedArray.getDimension(R.styleable.MagicProgressBar_mpb_textPaddingLeft,0);
            progressHeight = typedArray.getDimension(R.styleable.MagicProgressBar_mpb_progressHeight,5);
            //开始和结束颜色默认是填充色
            startColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_startColor,Color.BLACK);
            endColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_endColor,Color.BLACK);
            textColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_textColor,Color.BLACK);
            bubbleTextColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_bubbleTextColor,Color.BLACK);
            duration = typedArray.getColor(R.styleable.MagicProgressBar_mpb_duration,1000);

        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        //填充色画笔
        fillPaint = new Paint();
        fillPaint.setColor(fillColor);
        fillPaint.setStrokeWidth(progressHeight);
        fillPaint.setAntiAlias(true);


        //背景色
        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStrokeWidth(progressHeight);
        backgroundPaint.setAntiAlias(true);


        //右边百分比text
//        textPaint = new Paint();
//        textPaint.setColor(textColor);
//        textPaint.setTextSize(25);
//        textPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
//        textPaint.setAntiAlias(true);
//        //当100%时字体的宽高
//        maxPercentTextWidth = (int)(textPaint.measureText("100%"));
//        maxPercentTextHeight = getTextHeight("100%",textPaint);

        bubbleTextPaint = new Paint();
        bubbleTextPaint.setColor(bubbleTextColor);
        bubbleTextPaint.setTextSize(25);
        bubbleTextPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        bubbleTextPaint.setAntiAlias(true);

    }
    //矩形精度
    private final RectF rectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float drawPercent = animatorPercent;

        canvas.save();


        //整个控件的高
        progressHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        //背景进度条的宽
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - maxPercentTextWidth - (int)textPaddingLeft;

        //填充色的宽
        float fillWidth = drawPercent * width;
        final float radius = progressHeight / 2.0f;

        //progressHeight是进度条的高
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = width;
        rectF.bottom = progressHeight;

        //bubbleText

            //乘以1000/10 ,避免乘法运算数据结果误差

//            String textBubble = "已售"+(int)(animatorPercent*1000/10)+"%";

//            int bubbleTextWidth = (int)(bubbleTextPaint.measureText(textBubble));
//            if (bubbleTextWidth/2 < fillWidth) {
//                if (fillWidth < width-bubbleTextWidth/2) {
//                    canvas.drawText(textBubble, fillWidth - bubbleTextWidth/2, 30, bubbleTextPaint);
//                } else {
//                    canvas.drawText(textBubble, fillWidth - bubbleTextWidth, 30, bubbleTextPaint);
//                }
//            } else {
//                canvas.drawText(textBubble, fillWidth, 30, bubbleTextPaint);
//            }



        //draw text
//        float textPaddingTop = height /2.0f + (maxPercentTextHeight/2.0f);
//        String text = (int)(animatorPercent*100) +"%";
//        canvas.drawText(text, width + textPaddingLeft, textPaddingTop, textPaint);

        //渐变色
//        if (lg == null&&percent>0) {
//            lg = new LinearGradient(0,0,percent*width,percent*width, startColor,endColor, Shader.TileMode.MIRROR);
//            fillPaint.setShader(lg);
//        }

        // draw background
        if (backgroundColor != 0) {
            canvas.drawRoundRect(rectF, radius, radius, backgroundPaint);
        }

        // draw fill
        try {
            
            if (fillColor != 0 && fillWidth > 0) {
                //有锯齿, 无奈放弃
//            fillPath.reset();
//            rectF.right = fillWidth;
//            fillPath.addRect(rectF, Path.Direction.CW);
//            fillPath.close();
//            canvas.clipPath(regionPath);
//            canvas.drawPath(fillPath, fillPaint);
                if (fillWidth == width) {
                    rectF.right = fillWidth;
                    canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    return;
                }

                if (isFlat) {

                    // draw left semicircle
                    canvas.save();
                    rectF.right = fillWidth > radius ? radius : fillWidth;
                    canvas.clipRect(rectF);
                    rectF.right = radius * 2;
                    canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    canvas.restore();

                    if (fillWidth <= radius) {
                        return;
                    }

                    float leftAreaWidth = width - radius;
                    // draw center
                    float centerX = fillWidth > leftAreaWidth ? leftAreaWidth : fillWidth;
                    rectF.left = radius;
                    rectF.right = centerX;
                    canvas.drawRect(rectF, fillPaint);
                    if (fillWidth <= leftAreaWidth) {
                        return;
                    }

                    // draw right semicircle
                    rectF.left = leftAreaWidth - radius;

                    rectF.right = fillWidth;
                    canvas.clipRect(rectF);

                    rectF.right = width;
                    canvas.drawArc(rectF, -90, 180, true, fillPaint);

                } else {

                    if (fillWidth <= radius * 2) {
                        rectF.right = fillWidth;
                        canvas.clipRect(rectF);
                        rectF.right = radius * 2;
                        canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    } else {
                        rectF.right = fillWidth;
                        canvas.drawRoundRect(rectF, radius, radius, fillPaint);
                    }
                }

            }
        } finally {
            canvas.restore();
        }
    }

    /**
     * 进度条滑动
     * @param startProgress
     */
    private void setAnimation(final float startProgress) {

        if (progressAnimator != null) {
            progressAnimator.cancel();
        }

        progressAnimator = ValueAnimator.ofFloat(0, startProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatorPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }
    /**
     * @param percent FloatRange(from = 0.0, to = 1.0)
     */
    public synchronized void setPercent(float percent) {

        if (this.percent != percent) {
            this.percent = percent;
        }
        if (percent< 0) {
            this.percent = 0;
        }
        if (percent > 1) {
            this.percent = 1;
        }

        setAnimation(percent);
        invalidate();
    }


    /**
     * @param fillColor ColorInt
     */
    public void setFillColor(final int fillColor) {
        if (this.fillColor != fillColor) {
            this.fillColor = fillColor;
            this.fillPaint.setColor(fillColor);
            invalidate();
        }

    }

    /**
     * @param backgroundColor ColorInt
     */
    public void setBackgroundColor(final int backgroundColor) {
        if (this.backgroundColor != backgroundColor) {
            this.backgroundColor = backgroundColor;
            this.backgroundPaint.setColor(backgroundColor);
            invalidate();
        }
    }

    /**
     * 字体距左的距离
     * @param textLeft
     * @return
     */
    public void setTextLeft(int textLeft) {
        this.textPaddingLeft = textLeft;
        invalidate();
    }


    public int getFillColor() {
        return this.fillColor;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public float getPercent() {
        return this.percent;
    }

    /**
     * 获取字体的高
     * @param text
     * @param paint
     * @return
     */
    public int getTextHeight(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }


}
