package com.jinju.android.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jinju.android.R;
import com.jinju.android.manager.MyWindowManager;

/**
 * Created by Libra on 2017/10/19.
 *  浮动窗口 (6.0以上需要手动设置权限)
 */

public class FloatNormalView extends LinearLayout {
    private Context context = null;
    private View view = null;
    private ImageView ivShowControlView = null;
    private WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    private static WindowManager windowManager;

    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private boolean initViewPlace = false;
    private MyWindowManager myWindowManager;
    private boolean isControlViewShowing = false;

    public FloatNormalView(Context context) {
        super(context);
        this.context = context;
        myWindowManager = MyWindowManager.getInstance();
        LayoutInflater.from(context).inflate(R.layout.layout_float_window, this);
        view = findViewById(R.id.ll_float_normal);
        ivShowControlView = (ImageView) findViewById(R.id.iv_show_control_view);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        initLayoutParams();
        initEvent();
    }


    /**
     * 初始化参数
     */
    private void initLayoutParams() {
        //屏幕宽高
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();

        //总是出现在应用程序窗口之上。
        lp.type = WindowManager.LayoutParams.TYPE_PHONE;

        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按,不设置这个flag的话，home页的划屏会有问题
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        //悬浮窗默认显示的位置
        lp.gravity = Gravity.START | Gravity.TOP;
        //指定位置
        lp.x = screenWidth - view.getLayoutParams().width * 2;
        lp.y = screenHeight / 2 + view.getLayoutParams().height * 2;
        //悬浮窗的宽高
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.format = PixelFormat.TRANSPARENT;
        windowManager.addView(this, lp);
    }

    /**
     * 设置悬浮窗监听事件
     */
    private void initEvent() {
        ivShowControlView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isControlViewShowing) {
                    myWindowManager.createNormalView(context);
                    isControlViewShowing = true;
                } else {
                    myWindowManager.removeNormalView(context);
                    isControlViewShowing = false;
                }
                return true;
            }
        });
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!initViewPlace) {
                            initViewPlace = true;
                            //获取初始位置
                            mTouchStartX += (event.getRawX() - lp.x);
                            mTouchStartY += (event.getRawY() - lp.y);
                        } else {
                            //根据上次手指离开的位置与此次点击的位置进行初始位置微调
                            mTouchStartX += (event.getRawX() - x);
                            mTouchStartY += (event.getRawY() - y);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取相对屏幕的坐标，以屏幕左上角为原点
                        x = event.getRawX();
                        y = event.getRawY();
                        updateViewPosition();
                        break;

                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 更新浮动窗口位置
     */
    private void updateViewPosition() {
        lp.x = (int) (x - mTouchStartX);
        lp.y = (int) (y - mTouchStartY);
        windowManager.updateViewLayout(this, lp);
    }
}
