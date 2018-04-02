package com.jinju.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jinju.android.R;

/**
 * 百分比数据类型饼状统计图
 */
public class PercentPieView extends View {

    //中心坐标
    private int centerX;                        //圆心x坐标
    private int centerY;                        //圆心y坐标
    private int[] colors;                       //颜色数组
    private double sum;                         //资产总额
    private double angle[];
    private float circleWidth = 26;             //圆圈的宽度
    private float radius;                       //半径
    private RectF rectF;                        //弧形外接矩形
    private Paint mYellowPaint;                 //第二部分的画笔
    private Paint mRedPaint;                    //第一部分的画笔
    private Paint mShadowPaint;                 //阴影画笔
    private Paint mDefaultPaint;

    public PercentPieView(Context context) {
        super(context);
        init();
    }

    public PercentPieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentPieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieView);
        circleWidth = typedArray.getDimension(R.styleable.PieView_circleWidth, circleWidth);
        typedArray.recycle();
        init();
    }

    private void init() {

        //绘制扇形
        mYellowPaint = new Paint();
        mYellowPaint.setAntiAlias(true);           //取消锯齿
        mYellowPaint.setStyle(Paint.Style.STROKE);
        mYellowPaint.setStrokeWidth(26);

        //突出部分画笔
        mRedPaint = new Paint();
        mRedPaint.setAntiAlias(true);
        mRedPaint.setStyle(Paint.Style.STROKE);
        mRedPaint.setStrokeWidth(36);

        //阴影部分画笔
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setStrokeWidth(66);
        mShadowPaint.setColor(getResources().getColor(R.color.backCircle));

        //默认部分画笔
        mDefaultPaint = new Paint();
        mDefaultPaint.setAntiAlias(true);
        mDefaultPaint.setStyle(Paint.Style.STROKE);
        mDefaultPaint.setStrokeWidth(26);
        mDefaultPaint.setColor(getResources().getColor(R.color.amountFrozen));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        //这里设置饼形图的大小，通过设置圆形半径实现
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 3;
        //设置扇形外接矩形
        rectF = new RectF(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateAndDraw(canvas);
    }

    /**
     * 计算比例并且绘制扇形和数据
     */
    private void calculateAndDraw(Canvas canvas) {

        //起始度数
        int startAngle = -90;

        canvas.drawArc(rectF, startAngle, 360, false, mShadowPaint);
        canvas.drawArc(rectF, startAngle, 360, false, mDefaultPaint);

        if (angle == null || angle.length == 0) {
            return;
        }

        // 参数：0 --红色        参数：1 --黄色
        //两种颜色
        if (angle[0] == 0) {                //没红色
            if (angle[1] != 0) {
                if (angle[2] <= 3 & angle[2] > 0) {
                    drawArc(canvas, startAngle, 357, colors[1], 1);
                } else if (angle[1] <= 3) {
                    drawArc(canvas, startAngle, 3, colors[1], 1);
                } else {
                    drawArc(canvas, startAngle, (float) (360 - angle[2]), colors[1], 1);
                }
            }
        } else if (angle[1] == 0) {        //没黄色
            if (angle[0] != 0) {
                if ((angle[0]) < 3 & angle[0] > 0) {
                    drawArc(canvas, startAngle, 3, colors[0], 0);
                } else if (angle[2] < 3 & angle[2] > 0) {
                    drawArc(canvas, startAngle, 357, colors[0], 0);
                } else {
                    drawArc(canvas, startAngle, (float) (360 - angle[1] - angle[2]), colors[0], 0);
                }
            }
        } else if (angle[2] == 0) {         //没灰色
            drawArc(canvas, startAngle, 360, colors[1], 1);
            if (angle[0] != 0) {
                if (angle[0] < 3 & angle[0] > 0) {
                    drawArc(canvas, startAngle, 3, colors[0], 0);
                } else if (angle[1] < 3 & angle[1] > 0) {
                    drawArc(canvas, startAngle, (float) (360 - angle[1] - angle[2] - 3), colors[0], 0);
                } else {
                    drawArc(canvas, startAngle, (float) (360 - angle[1] - angle[2]), colors[0], 0);
                }//黄色
            }
        } else {
            //三种颜色
            if ((float) angle[0] <= 3 & (float) angle[0] > 0) {     //红色<3
                if (angle[1] <= 3 & angle[1] > 0) {
                    drawArc(canvas, startAngle, (float) (360 - angle[2] + 6), colors[1], 1);
                } else if (angle[2] <= 3 & angle[2] > 0) {
                    drawArc(canvas, startAngle, (float) (360 - 3), colors[1], 1);
                } else {
                    drawArc(canvas, startAngle, (float) (360 - angle[2]), colors[1], 1);
                }
                drawArc(canvas, startAngle, 3, colors[0], 0);
            } else if (angle[1] <= 3 & angle[1] > 0) {       //黄色<3
                if (angle[0] <= 3 & angle[0] > 0) {
                    drawArc(canvas, startAngle, 6, colors[1], 1);
                    drawArc(canvas, startAngle, 3, colors[0], 0);
                } else if (angle[2] <= 3 & angle[2] > 0) {                                      //灰色、黄色极限
                    drawArc(canvas, startAngle, 357, colors[1], 1);
                    drawArc(canvas, startAngle, 354, colors[0], 0);
                } else {                                                                        //其他情况
                    drawArc(canvas, startAngle, (float) (360 - angle[2]), colors[1], 1);
                    drawArc(canvas, startAngle, (float) (360 - angle[1] - angle[2] - 3), colors[0], 0);
                }
            } else if (angle[2] <= 3 & angle[2] > 0) {
                drawArc(canvas, startAngle, 357, colors[1], 1);
                if (angle[0] <= 3 & angle[0] > 0) {
                    drawArc(canvas, startAngle, 3, colors[0], 0);
                } else if (angle[1] <= 3 & angle[1] > 0) {
                    drawArc(canvas, startAngle, 354, colors[0], 0);
                } else {
                    drawArc(canvas, startAngle, (float) (360 - angle[1] - angle[2] - 1), colors[0], 0);
                }
            } else {                                                                            //非特殊情况
                drawArc(canvas, startAngle, (float) (360 - angle[2]), colors[1], 1);
                drawArc(canvas, startAngle, (float) (360 - angle[1] - angle[2]), colors[0], 0);
            }
        }
    }

    /**
     * 绘制扇形
     *
     * @param canvas     画布
     * @param startAngle 开始度数
     * @param angle      扇形的度数
     * @param color      颜色
     */
    private void drawArc(Canvas canvas, float startAngle, float angle, int color, int position) {
        mYellowPaint.setColor(color);
        mRedPaint.setColor(color);

        if (position == 0) {
            canvas.drawArc(rectF, startAngle, angle, false, mRedPaint);
        } else {
            canvas.drawArc(rectF, startAngle, angle, false, mYellowPaint);
        }

    }

    /**
     * 设置数据(自定义颜色)
     */
    public void setData(int[] colors, double sum, double[] angle) {

        this.colors = colors;
        this.sum = sum;
        this.angle = angle;

        if (sum != 0) {
            //计算总和数字的宽高
            invalidate();
        }
    }
}
