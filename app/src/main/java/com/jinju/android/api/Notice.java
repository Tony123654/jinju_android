package com.jinju.android.api;

/**
 * Created by Libra on 2017/6/27.
 */

public class Notice {
    private String msgContent;//消息内容
    private String msgDate;//消息时间
    private int msgId;//
    private int msgType;//消息类型
    private String msgTypeDesc;//系统公告|系统通知|交易信息

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgTypeDesc() {
        return msgTypeDesc;
    }

    public void setMsgTypeDesc(String msgTypeDesc) {
        this.msgTypeDesc = msgTypeDesc;
    }
}
