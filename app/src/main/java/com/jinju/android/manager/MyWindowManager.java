package com.jinju.android.manager;

import android.content.Context;
import android.view.WindowManager;

import com.jinju.android.widget.FloatNormalView;

/**
 * Created by Libra on 2017/10/19.
 */

public class MyWindowManager {
    private FloatNormalView normalView;
//    private FloatControlView controlView;

    private static MyWindowManager instance;

    private MyWindowManager() {
    }

    public static MyWindowManager getInstance() {
        if (instance == null)
            instance = new MyWindowManager();
        return instance;
    }


    /**
     * 创建小型悬浮窗
     */
    public void createNormalView(Context context) {
        if (normalView == null)
            normalView = new FloatNormalView(context);
    }

    /**
     * 移除悬浮窗
     *
     * @param context
     */
    public void removeNormalView(Context context) {
        if (normalView != null) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.removeView(normalView);
            normalView = null;
        }
    }

    /**
     * 创建小型悬浮窗
     */
//    public void createControlView(Context context) {
//        if (controlView == null)
//            controlView = new FloatControlView(context);
//    }

    /**
     * 移除悬浮窗
     *
     * @param context
     */
//    public void removeControlView(Context context) {
//        if (controlView != null) {
//            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            windowManager.removeView(controlView);
//            controlView = null;
//        }
//    }
}
