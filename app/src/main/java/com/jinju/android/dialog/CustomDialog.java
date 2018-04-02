package com.jinju.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jinju.android.R;

/**
 * Created by awe on 2018/1/10.
 */

public class CustomDialog extends Dialog {

    private Context context;
    private double height, width;
    private boolean cancelTouchout ;//触摸空白是否可以消失 默认不消失
    private View view;
    private boolean cancelBack = false;//点击返回键是否可以消失 默认消失
    private int location = Gravity.CENTER;
    private boolean hasAnimation = true;
    private CustomDialog(Builder builder) {
        super(builder.context);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        location = builder.location;
        view = builder.view;
        cancelBack = builder.cancelBack;
        cancelTouchout = builder.canTouchout;
        hasAnimation = builder.hasAnimation;
    }

    private CustomDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        location = builder.location;
        view = builder.view;
        cancelBack = builder.cancelBack;
        cancelTouchout = builder.canTouchout;
        hasAnimation = builder.hasAnimation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(view);

        setCanceledOnTouchOutside(cancelTouchout);

        setOnKeyListener(mListener);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        // 最好放在外面控制显示位置,放在里面高度控制会变成偏移量---设 置大小及显示位置
        // 设置dialog 的窗口大小
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        lp.gravity = location;
        //lp.y = DeviceUtil.dp2px(context,50);
        if (height == 0.0) {
            lp.height = (int) (d.getHeight() * 0.5); //
            lp.width = (int) (d.getWidth() * 0.65);
        } else {
            lp.height = (int) (d.getHeight() * height); //
            lp.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的
        }
        win.setAttributes(lp);

        if (hasAnimation){
            win.setWindowAnimations(R.style.PopupAnimation);
        }

    }

    /**
     * 返回键是否可以消失
     */
    OnKeyListener mListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            return cancelBack;
        }
    };

    //
    public <T extends View> T getView(@IdRes int resId) {
        return (T) view.findViewById(resId);
    }

    public static class Builder {

        private Context context;
        private double height, width;
        private int location;
        private boolean canTouchout;
        private View view;
        private int resStyle = -1;
        public boolean cancelBack;
        public boolean hasAnimation;


        public Builder(Context context) {
            this.context = context;
        }

        public Builder view(int resView) {
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        /**
         * 设置dialog弹出的位置 BOOTOM(下) CENTER(中) TOP(上) LEFT(左) RIGHT(右)
         *
         * @return
         */

        public Builder location(int location) {
            this.location = location;
            return this;
        }

        /**
         * 设置dialog的位置大小1.设置dialog高度的屏占比2.设置dialog宽度的屏占比
         *
         * @param heightScale
         * @param widthScale
         * @return
         */
        public Builder size(double heightScale, double widthScale) {
            this.height = heightScale;
            this.width = widthScale;
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }


        public Builder animation(boolean hasAnimation) {
            this.hasAnimation = hasAnimation;
            return this;
        }

        /**
         * 触摸空白是否可以消失1.true 消失 2.false 不消失3.不传默认为false
         *
         * @param val
         * @return
         */
        public Builder canTouchout(boolean val) {
            canTouchout = val;
            return this;
        }


        /**
         * 点击返回键dialog是否消失1.false 消失 2.true 不消失3.不传默认为false
         *
         * @param val
         * @return
         */
        public Builder cancelBackPress(boolean val) {
            cancelBack = val;
            return this;
        }


        public CustomDialog build( ) {
            if (resStyle != -1) {
                return new CustomDialog(this, resStyle);
            } else {
                return new CustomDialog(this);
            }
        }

    }
}
