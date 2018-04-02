package com.jinju.android.api;

import java.io.Serializable;

/**
 * Created by Libra on 2017/6/7.
 */

public class PopAd implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;//广告代码
    private String imgUrl;//广告图片地址
    private String linkUrl;//链接地址
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    @Override
    public String toString() {
        return "PopAd{" +
                "code='" + code + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
