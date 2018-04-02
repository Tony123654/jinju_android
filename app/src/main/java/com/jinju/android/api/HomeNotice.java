package com.jinju.android.api;

import java.io.Serializable;

/**
 * Created by Libra on 2017/10/11.
 */

public class HomeNotice implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;//标题
    private String content;//公告内容
    private String mark;//标签
    private String noticeDate;//公告发布时间
    private String noticeUrl;

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }
}
