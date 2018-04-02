package com.jinju.android.api;

import java.io.Serializable;

/**
 * Created by awe on 2018/3/9.
 */

public class VersionUpdate implements Serializable{
    private String downloadUrl;
    private String mContent;
    private String mVersion;
    private String mFilesize;
    private String mForceUpdate;

    public String getForceUpdate() {
        return mForceUpdate;
    }

    public void setForceUpdate(String mForceUpdate) {
        this.mForceUpdate = mForceUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String mVersion) {
        this.mVersion = mVersion;
    }

    public String getFilesize() {
        return mFilesize;
    }

    public void setFilesize(String mFilesize) {
        this.mFilesize = mFilesize;
    }
}
