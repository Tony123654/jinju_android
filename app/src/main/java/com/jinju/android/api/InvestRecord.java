package com.jinju.android.api;

/**
 * Created by Libra on 2017/11/6.
 */

public class InvestRecord {
    private String gmtCreate;
    private String mobile;
    private String payAmount;

    private String topGmtCreate;
    private String topMobile;
    private String topPayAmount;


    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getTopGmtCreate() {
        return topGmtCreate;
    }

    public void setTopGmtCreate(String topGmtCreate) {
        this.topGmtCreate = topGmtCreate;
    }

    public String getTopMobile() {
        return topMobile;
    }

    public void setTopMobile(String topMobile) {
        this.topMobile = topMobile;
    }

    public String getTopPayAmount() {
        return topPayAmount;
    }

    public void setTopPayAmount(String topPayAmount) {
        this.topPayAmount = topPayAmount;
    }
}
