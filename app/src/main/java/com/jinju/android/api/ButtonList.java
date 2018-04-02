package com.jinju.android.api;

import java.io.Serializable;

/**
 * Created by Libra on 2017/5/15.
 */

public class ButtonList implements Serializable{
    private static final long serialVersionUID = 1L;
    private String desc;
    private String pic;
    private String title;
    private String linkUrl;
    private String type;
    private String flag;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ButtonList{" +
                "pic='" + pic + '\'' +
                ", title='" + title + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", type='" + type + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
