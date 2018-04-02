package com.jinju.android.api;

/**
 * Created by miyun-8767 on 2016/3/2.
 */
public class PerLogin {

    private String mobile;
    private String showName;
    private String type;
    private String result;
    private String message;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobile() {
        return mobile;
    }

    public String getShowName() {
        return showName;
    }

    public String getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
