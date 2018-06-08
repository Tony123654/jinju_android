package com.jinju.android.api;

import java.util.List;

/**
 * 类名: ReportListBean <p>
 * 创建人: mwb <p>
 * 创建时间: 2018/6/5 16:34 <p>
 * 描述:
 * <p>
 * 更新人: <p>
 * 更新时间: <p>
 * 更新描述: <p>
 */
public class ReportListBean {


    /**
     * code :
     * currentPage : 1
     * mediaList : [{"content":"<div> \t<p> \t\t大约是七零年，我大伯去当兵。我爷爷觉得去部队当兵，要讲卫生，不然一脱衣服，袜子上，内裤上都是世界地图，会被笑话的。<br /> 作为村长的他，怎么能忍受自己的儿子被笑话（︶︿︶）。<br /> 于是连夜把大伯的内裤和袜子全洗了。怕大伯不够穿，把自己的内裤和袜子也洗了一些，准备给大伯带上。<br /> 但是第二天接新兵的车队开到村口，内裤和袜子依然可以拧出水。这可怎么办，大伯的大红花都带好了，准备登车了。<br /> 事实证明，我爷爷是很有创造力的。<br /> 他折了一个大树叉，把袜子和内裤叮叮当当的全挂在树枝上。我一米八的爷爷，一路上举着挂满内衣裤的树枝，奔跑在送行的人群里─=≡Σ((( つ\u2022̀ω\u2022́)。<br /> 那场景肯定很美。。。<br /> 跑了几里地，硬是让风把内衣吹干了 \t<\/p > 一直没问，大伯在车上看着漫天飞舞的内衣裤是什么心情 <\/div> <br /> <br />","mark":"","noticeDate":"2018-05-16 14:09:37","showPic":"http://dev.admin.jinjulc.com/img/2c902cdf6367849701636791a45d0003.jpg","title":"我爸告诉我的事。"}]
     * message :
     * pageSize : 20
     * result : success
     * totalPage : 1
     */

    private String code;
    private int                 currentPage;
    private String              message;
    private int                 pageSize;
    private String              result;
    private int                 totalPage;
    private List<MediaListBean> mediaList;

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

    public List<MediaListBean> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<MediaListBean> mediaList) {
        this.mediaList = mediaList;
    }

    public static class MediaListBean {
        /**
         * content : <div> 	<p> 		大约是七零年，我大伯去当兵。我爷爷觉得去部队当兵，要讲卫生，不然一脱衣服，袜子上，内裤上都是世界地图，会被笑话的。<br /> 作为村长的他，怎么能忍受自己的儿子被笑话（︶︿︶）。<br /> 于是连夜把大伯的内裤和袜子全洗了。怕大伯不够穿，把自己的内裤和袜子也洗了一些，准备给大伯带上。<br /> 但是第二天接新兵的车队开到村口，内裤和袜子依然可以拧出水。这可怎么办，大伯的大红花都带好了，准备登车了。<br /> 事实证明，我爷爷是很有创造力的。<br /> 他折了一个大树叉，把袜子和内裤叮叮当当的全挂在树枝上。我一米八的爷爷，一路上举着挂满内衣裤的树枝，奔跑在送行的人群里─=≡Σ((( つ•̀ω•́)。<br /> 那场景肯定很美。。。<br /> 跑了几里地，硬是让风把内衣吹干了 	</p > 一直没问，大伯在车上看着漫天飞舞的内衣裤是什么心情 </div> <br /> <br />
         * mark :
         * noticeDate : 2018-05-16 14:09:37
         * showPic : http://dev.admin.jinjulc.com/img/2c902cdf6367849701636791a45d0003.jpg
         * title : 我爸告诉我的事。
         */

        private String content;
        private String mark;
        private String noticeDate;
        private String showPic;
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

        public String getShowPic() {
            return showPic;
        }

        public void setShowPic(String showPic) {
            this.showPic = showPic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
