package com.jinju.android.api;

import java.util.List;

/**
 * 类名: InvestList <p>
 * 创建人: mwb <p>
 * 创建时间: 2018/6/6 16:35 <p>
 * 描述:
 * <p>
 * 更新人: <p>
 * 更新时间: <p>
 * 更新描述: <p>
 */
public class InvestList {

    /**
     * code :
     * count : 4
     * currentPage : 1
     * message :
     * orderList : [{"gmtCreate":"2018-05-25 15:06","mobile":"187*****605","payAmount":"79,400.00"},{"gmtCreate":"2018-05-25 15:05","mobile":"187*****605","payAmount":"20,000.00"},{"gmtCreate":"2018-05-25 15:01","mobile":"187*****605","payAmount":"500.00"},{"gmtCreate":"2018-05-25 11:40","mobile":"152*****020","payAmount":"100.00"}]
     * pageSize : 10000
     * result : success
     * techinca : [{"topGmtCreate":"2018-05-25 15:06","topMobile":"187*****605","topPayAmount":"99,900.00元"},{"topGmtCreate":"2018-05-25 11:40","topMobile":"152*****020","topPayAmount":"100.00元"}]
     * totalPage : 1
     */

    private String code;
    private int                 count;
    private int                 currentPage;
    private String              message;
    private int                 pageSize;
    private String              result;
    private int                 totalPage;
    private List<OrderListBean> orderList;
    private List<TechincaBean>  techinca;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public List<TechincaBean> getTechinca() {
        return techinca;
    }

    public void setTechinca(List<TechincaBean> techinca) {
        this.techinca = techinca;
    }

    public static class OrderListBean {
        /**
         * gmtCreate : 2018-05-25 15:06
         * mobile : 187*****605
         * payAmount : 79,400.00
         */

        private String gmtCreate;
        private String mobile;
        private String payAmount;

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
    }

    public static class TechincaBean {
        /**
         * topGmtCreate : 2018-05-25 15:06
         * topMobile : 187*****605
         * topPayAmount : 99,900.00元
         */

        private String topGmtCreate;
        private String topMobile;
        private String topPayAmount;

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
}
