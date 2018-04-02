package com.jinju.android.api;

import java.io.Serializable;

/**
 * Created by Libra on 2017/10/10.
 *
 * 首页公告 跑马灯
 */

public class TrottList implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mTrottId;
    private String mText;
    private String mUrl;

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public int getTrottId() {
        return mTrottId;
    }

    public void setTrottId(int mTrottId) {
        this.mTrottId = mTrottId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}

