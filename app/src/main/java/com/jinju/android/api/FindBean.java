package com.jinju.android.api;

import java.util.List;

/**
 * 类名: FindBean <p>
 * 创建人: mwb <p>
 * 创建时间: 2018/6/7 15:05 <p>
 * 描述:
 * <p>
 * 更新人: <p>
 * 更新时间: <p>
 * 更新描述: <p>
 */
public class FindBean {

    /**
     * code :
     * currentPage : 1
     * findActPage : [{"findDateBegin":"2018.05.15","findDateEnd":"2018.06.01","imgPath":"http://dev.admin.jinjulc.com/img/2c902cdf63aeaec90163aeb1dd770001.png","jumpType":"-1","jumpUrl":"http://dev.activity.jinjulc.com/activity/invite/invite.html","title":"开始的福建省的开了房间卡了"},{"findDateBegin":"2018.05.08","findDateEnd":"2018.06.03","imgPath":"http://dev.admin.jinjulc.com/img/2c902cdf63c99e030163c99ed08b0001.png","jumpType":"0","jumpUrl":"","title":"更换即可了地方"}]
     * findPage : [{"findDateBegin":"长期有效","findDateEnd":"","imgPath":"http://dev.admin.jinjulc.com/img/2c902cdf63c99e030163c9d4e08f0002.jpg","jumpType":"0","jumpUrl":"","title":"第三方斯蒂芬"},{"findDateBegin":"长期有效","findDateEnd":"","imgPath":"http://dev.admin.jinjulc.com/img/2c902cdf63c99e030163c99e03f90000.jpg","jumpType":"0","jumpUrl":"","title":"我的资产页面没有那个圈"},{"findDateBegin":"长期有效","findDateEnd":"","imgPath":"http://dev.admin.jinjulc.com/img/2c902cdf63aeaec90163aeaec9630000.jpg","jumpType":"2","jumpUrl":"","title":"快乐的发给你记得付款了给你"},{"findDateBegin":"长期有效","findDateEnd":"","imgPath":"http://dev.admin.jinjulc.com/img/2c902cdf639557c00163a468f94d0002.jpg","jumpType":"0","jumpUrl":"","title":"000"},{"findDateBegin":"长期有效","findDateEnd":"","imgPath":"http://dev.admin.jinjulc.com/img/2c902cdf634930110163493011050000.jpg","jumpType":"-1","jumpUrl":"http://2312","title":"蜡笔小新的第一次"}]
     * message :
     * noticeList : [{"content":"阿萨德飞老客户就考了多少房间","mark":"阿萨德","noticeDate":"2018-05-24 16:34:40","title":"打发斯蒂芬"},{"content":"有一天 小鱼问大鱼：大～鱼～大～鱼～你～平～常～喜～欢～吃～什～么～丫。 大鱼说：我～喜～欢～吃～说～话～慢～的～小～鱼。 然后小鱼说：喔 酱紫 造了！","mark":"","noticeDate":"2018-05-09 10:46:50","title":"第一个媒体公告"},{"content":"    尊敬的XXX:这有您的还款,请签收!!!","mark":"金桔理财","noticeDate":"2018-05-08 11:32:30","title":"项目还款公告2018-5-8"}]
     * pageSize : 2147483647
     * result : success
     * totalPage : 1
     */

    private String code;
    private int                   currentPage;
    private String                message;
    private int                   pageSize;
    private String                result;
    private int                   totalPage;
    private List<FindActPageBean> findActPage;
    private List<FindPageBean>    findPage;
    private List<NoticeListBean>  noticeList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<FindActPageBean> getFindActPage() {
        return findActPage;
    }

    public void setFindActPage(List<FindActPageBean> findActPage) {
        this.findActPage = findActPage;
    }

    public List<FindPageBean> getFindPage() {
        return findPage;
    }

    public void setFindPage(List<FindPageBean> findPage) {
        this.findPage = findPage;
    }

    public List<NoticeListBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeListBean> noticeList) {
        this.noticeList = noticeList;
    }

    public static class FindActPageBean {
        /**
         * findDateBegin : 2018.05.15
         * findDateEnd : 2018.06.01
         * imgPath : http://dev.admin.jinjulc.com/img/2c902cdf63aeaec90163aeb1dd770001.png
         * jumpType : -1
         * jumpUrl : http://dev.activity.jinjulc.com/activity/invite/invite.html
         * title : 开始的福建省的开了房间卡了
         */

        private String findDateBegin;
        private String findDateEnd;
        private String imgPath;
        private String jumpType;
        private String jumpUrl;
        private String title;

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

    public static class FindPageBean {
        /**
         * findDateBegin : 长期有效
         * findDateEnd :
         * imgPath : http://dev.admin.jinjulc.com/img/2c902cdf63c99e030163c9d4e08f0002.jpg
         * jumpType : 0
         * jumpUrl :
         * title : 第三方斯蒂芬
         */

        private String findDateBegin;
        private String findDateEnd;
        private String imgPath;
        private String jumpType;
        private String jumpUrl;
        private String title;

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

    public static class NoticeListBean {
        /**
         * content : 阿萨德飞老客户就考了多少房间
         * mark : 阿萨德
         * noticeDate : 2018-05-24 16:34:40
         * title : 打发斯蒂芬
         */

        private String content;
        private String mark;
        private String noticeDate;
        private String title;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getNoticeDate() {
            return noticeDate;
        }

        public void setNoticeDate(String noticeDate) {
            this.noticeDate = noticeDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
