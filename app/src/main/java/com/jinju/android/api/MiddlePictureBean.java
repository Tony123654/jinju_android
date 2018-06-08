package com.jinju.android.api;

import java.util.List;

/**
 * 类名: MiddlePictureBean <p>
 * 创建人: mwb <p>
 * 创建时间: 2018/5/29 17:37 <p>
 * 描述:
 * <p>
 * 更新人: <p>
 * 更新时间: <p>
 * 更新描述: <p>
 */
public class MiddlePictureBean {

    private String               code;
    private String               message;
    private String               result;
    private List<MiddleListBean> middleList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<MiddleListBean> getMiddleList() {
        return middleList;
    }

    public void setMiddleList(List<MiddleListBean> middleList) {
        this.middleList = middleList;
    }

    public static class MiddleListBean {
        /**
         * linkUrl : http://dev.admin.jinjulc.com/apphtml/web/noviceBoot/index.html
         * name : 新手指引
         * pic : http://dev.admin.jinjulc.com/resource/images/newbie.png
         */

        private String linkUrl;
        private String name;
        private String pic;

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
