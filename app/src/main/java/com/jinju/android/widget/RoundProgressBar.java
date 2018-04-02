package com.jinju.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.jinju.android.R;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * @author xiaanming
 *
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int percentTextColor;
    private int symbolTextColor;
    private int notifyTextColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float percentTextSize;
    private float symbolTextSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    private String percentText;//文本数字
    private String hintText;//单位

    private int textOffset;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        percentTextColor = mTypedArray.getColor(R.styleable.RoundProgressBar_percentTextColor, Color.GREEN);
        symbolTextColor = mTypedArray.getColor(R.styleable.RoundProgressBar_symbolTextColor, Color.GREEN);
        notifyTextColor = mTypedArray.getColor(R.styleable.RoundProgressBar_notifyTextColor, Color.GREEN);
        percentTextSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_percentTextSize, 18);
        symbolTextSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_symbolTextSize, 16);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        percentText = null;

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centre = getWidth()/2; //获取圆心的x坐标
        int radius = (int) (centre - roundWidth/2); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        //画中间的字符

        if(percentText != null){
            Paint percentTextPaint = new Paint();
            percentTextPaint.setStrokeWidth(0);
            percentTextPaint.setTextSize(percentTextSize);
            percentTextPaint.setColor(percentTextColor);
            percentTextPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
            percentTextPaint.setAntiAlias(true);
            float percentTextWidth = percentTextPaint.measureText(percentText);
            float percentTextHeight = percentTextWidth/percentText.length();

//            Paint symbolTextPaint = new Paint();
//            symbolTextPaint.setStrokeWidth(0);
//            symbolTextPaint.setTextSize(symbolTextSize);
//            symbolTextPaint.setColor(symbolTextColor);
//            symbolTextPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体

//           float symbolTextWidth = symbolTextPaint.measureText("%");
//            //这是进度
//            float percentTextX = centre - (percentTextWidth + symbolTextWidth) / 2;
//            float percentTextY = centre + percentTextHeight/2;
//            canvas.drawText(percentText, percentTextX, percentTextY, percentTextPaint); //画出进度百分比
//            //这是“元”字体
//            float symbolTextX = percentTextX + percentTextWidth;
//            float symbolTextY = percentTextY;
//            canvas.drawText("%", symbolTextX, symbolTextY, symbolTextPaint); //画出进度百分比

//            float percentTextX = centre - (percentTextWidth) / 2;
//            float percentTextY = centre + percentTextHeight/2;
//            canvas.drawText(percentText, percentTextX, percentTextY, percentTextPaint); //画出进度百分比
            //中间内容的数字和“元”的单位
            if(TextUtils.isEmpty(hintText)) {
//
                float percentTextX = centre - (percentTextWidth) / 2;
                float percentTextY = centre + percentTextHeight/2;
                canvas.drawText(percentText, percentTextX, percentTextY, percentTextPaint); //画文本
             } else {
                float percentTextX = centre - (percentTextWidth) / 2;
                float percentTextY = centre - percentTextHeight/2;
                canvas.drawText(percentText, percentTextX, percentTextY, percentTextPaint); ////画文本
            }

            //中间内容的“万元”单位
            if(hintText != null && !TextUtils.isEmpty(hintText)){
                Paint hintTextPaint = new Paint();
                hintTextPaint.setStrokeWidth(0);
                hintTextPaint.setTextSize(percentTextSize);
                hintTextPaint.setColor(percentTextColor);
                hintTextPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
                float hintTextWidth = hintTextPaint.measureText(hintText);
                float hintTextHeight = hintTextWidth/hintText.length();

                float hintTextX = centre - (hintTextWidth) / 2;
                float hintTextY = centre + hintTextHeight;
                canvas.drawText(hintText, hintTextX, hintTextY, hintTextPaint);
            }

        }

        /**
         * 画圆弧 ，画圆环的进度
         */

        if(progress != 0){
            paint.setStrokeWidth(roundWidth); //设置圆环的宽度
            paint.setColor(roundProgressColor);  //设置进度的颜色
            paint.setAntiAlias(true);  //消除锯齿

            RectF oval = new RectF(centre - radius, centre - radius, centre
                    + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(oval, 270, 360 * progress / max  , false, paint);  //根据进度画圆弧
        }

    }

    public synchronized void initProgressBar() {
        if(progress != 0 ){
            progress = 0;
            postInvalidate();
        }
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }

    }


    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public void setPercentText(String percentText){
        this.percentText = percentText;
        postInvalidate();
    }

    public String getPercentText(){
        return percentText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        postInvalidate();
    }

    public void setText(String percentText){
        this.percentText = percentText;
        postInvalidate();
    }

    public void setTextOffset(int textOffset){
        this.textOffset = textOffset;
    }

    public int getTextOffset(){
        return textOffset;
    }
}
