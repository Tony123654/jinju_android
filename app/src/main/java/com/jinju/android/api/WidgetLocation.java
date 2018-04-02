package com.jinju.android.api;

import java.io.Serializable;

/**
 * Created by Libra on 2018/1/31.
 */

public class WidgetLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    private int viewX;//X坐标
    private int viewY;//Y坐标
    private int viewWidth;//宽
    private int viewHeight;//高

    public int getViewX() {
        return viewX;
    }

    public void setViewX(int viewX) {
        this.viewX = viewX;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewY() {
        return viewY;
    }

    public void setViewY(int viewY) {
        this.viewY = viewY;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }
}
