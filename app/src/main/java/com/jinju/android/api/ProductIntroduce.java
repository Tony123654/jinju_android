package com.jinju.android.api;

import java.util.List;

/**
 * Created by Libra on 2017/11/6.
 */

public class ProductIntroduce {
    private String proDesc; //产品介绍
    private String loanerDesc; //项目描述
    private String loanUsage; //借款用途
    private String projectTheory; //项目原理url
    private List<String> pledgeFiles;//材料公示
    private String questionUrl;//常见问题
    private List<String> fundReturnSource;//借款来源



    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public String getLoanerDesc() {
        return loanerDesc;
    }

    public void setLoanerDesc(String loanerDesc) {
        this.loanerDesc = loanerDesc;
    }

    public String getLoanUsage() {
        return loanUsage;
    }

    public void setLoanUsage(String loanUsage) {
        this.loanUsage = loanUsage;
    }

    public List<String> getPledgeFiles() {
        return pledgeFiles;
    }

    public void setPledgeFiles(List<String> pledgeFiles) {
        this.pledgeFiles = pledgeFiles;
    }

    public String getProjectTheory() {
        return projectTheory;
    }

    public void setProjectTheory(String projectTheory) {
        this.projectTheory = projectTheory;
    }

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }

    public List<String> getFundReturnSource() {
        return fundReturnSource;
    }

    public void setFundReturnSource(List<String> fundReturnSource) {
        this.fundReturnSource = fundReturnSource;
    }
}
