package com.jinju.android.api;

/**
 * Created by miyun-8767 on 2016/1/21.
 */
public class Info {
    private String headImg;
    private String nick;
    private long memberId;
    private String name;
    private int authStatus;
    private String idCardNumber;
    private String mobile;

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadImg() {
        return headImg;
    }

    public String getNick() {
        return nick;
    }

    public long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public String getMobile() {
        return mobile;
    }
}
