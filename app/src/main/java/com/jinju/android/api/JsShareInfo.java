package com.jinju.android.api;

import java.util.List;

/**
 * Created by Libra on 2017/5/5.
 */

public class JsShareInfo {


    private String title;
    private String desc;
    private String linkUrl;
    private String imgUrl;
    private String triggerId;
    private List<Integer> buttons;

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<Integer> getButtons() {
        return buttons;
    }

    public void setButtons(List<Integer> buttons) {
        this.buttons = buttons;
    }

    @Override
    public String toString() {
        return "JsShareInfo{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", imgurl='" + imgUrl + '\'' +
                ", buttons=" + buttons +
                '}';
    }

}
