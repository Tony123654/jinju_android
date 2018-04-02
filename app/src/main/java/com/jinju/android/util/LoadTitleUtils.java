package com.jinju.android.util;


import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

/**
 * Created by Libra on 2018/1/5.
 */

public class LoadTitleUtils {
    private static int loadingNumber = 0;
    private static RefreshLayout mRefreshLayout;


    public static String setLoadTxt(List<String> mLoadInfoList){
        String title = "";
        if(mLoadInfoList!=null&&mLoadInfoList.size()>0) {
            if (loadingNumber<mLoadInfoList.size()) {
               title =  mLoadInfoList.get(loadingNumber);
            }else {
                loadingNumber = 0;
                title = mLoadInfoList.get(loadingNumber);
            }
            ++loadingNumber;
        }

        return title;
    }
  public static void setRefreshLayout(final RefreshLayout refreshLayout,int mCurrentPage,List<String> mLoadInfoList) {

      refreshLayout.finishRefreshAnimation(setLoadTxt(mLoadInfoList));

      mRefreshLayout = refreshLayout;
      if (mCurrentPage<=1) {//执行的是下拉刷新动作

          mRefreshLayout.finishRefresh();
      } else {
          //执行的是上拉加载动作
          refreshLayout.finishLoadmore();
      }
  }
}
