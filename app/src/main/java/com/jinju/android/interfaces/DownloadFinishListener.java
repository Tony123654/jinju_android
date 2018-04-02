package com.jinju.android.interfaces;

/**
 * Created by Libra on 2017/8/18.
 * 下载完成接口
 */

public interface DownloadFinishListener {
    void DownloadFinished();
    void DownloadFailed();
    void DownloadCancel();
}
