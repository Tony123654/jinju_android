package com.jinju.android.api;


/**
 * Created by Libra on 2017/11/7.
 */

public class Discover {

    private String findDateBegin;//活动起始时间
    private String findDateEnd;//活动结束时间
    private String imgPath;//
    private String jumpType;//-1 打开新页面、0 不跳转
    private String jumpUrl;//
    private String title;
    //Discover顶部内容
    private String pic;         //图片
    private String linkUrl;     //H5链接
    private String type;        //类型


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getFindDateBegin() {
        return findDateBegin;
    }

    public void setFindDateBegin(String findDateBegin) {
        this.findDateBegin = findDateBegin;
    }

    public String getFindDateEnd() {
        return findDateEnd;
    }

    public void setFindDateEnd(String findDateEnd) {
        this.findDateEnd = findDateEnd;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
