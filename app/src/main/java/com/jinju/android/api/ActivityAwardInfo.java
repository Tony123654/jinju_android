package com.jinju.android.api;

/**
 * Created by Libra on 2017/11/15.
 *  详情页 活动奖励信息
 */

public class ActivityAwardInfo {
    private String richDesc;//土豪奖
    private String endingDesc;//收官奖

    public String getRichDesc() {
        return richDesc;
    }

    public void setRichDesc(String richDesc) {
        this.richDesc = richDesc;
    }

    public String getEndingDesc() {
        return endingDesc;
    }

    public void setEndingDesc(String endingDesc) {
        this.endingDesc = endingDesc;
    }
}
