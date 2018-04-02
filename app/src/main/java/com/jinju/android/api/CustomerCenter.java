package com.jinju.android.api;

/**
 * Created by Libra on 2017/12/20.
 */

public class CustomerCenter {
    private String couWithInst;//红包类
    private String logWithReg;//登录类
    private String payWithDraw;
    private String product;
    private String sendWithCollect;

    public String getCouWithInst() {
        return couWithInst;
    }

    public void setCouWithInst(String couWithInst) {
        this.couWithInst = couWithInst;
    }

    public String getLogWithReg() {
        return logWithReg;
    }

    public void setLogWithReg(String logWithReg) {
        this.logWithReg = logWithReg;
    }

    public String getPayWithDraw() {
        return payWithDraw;
    }

    public void setPayWithDraw(String payWithDraw) {
        this.payWithDraw = payWithDraw;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSendWithCollect() {
        return sendWithCollect;
    }

    public void setSendWithCollect(String sendWithCollect) {
        this.sendWithCollect = sendWithCollect;
    }
}
