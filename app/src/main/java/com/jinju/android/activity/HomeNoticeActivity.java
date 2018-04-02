package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.HomeNoticeAdapter;
import com.jinju.android.api.HomeNotice;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.widget.FrameAnimationHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Libra on 2017/10/10.
 */

public class HomeNoticeActivity extends BaseActivity {
    private Dialog mProgressDialog;
    private ListView mListView;
    private List<HomeNotice> mHomeNoticeList;
    private HomeNoticeAdapter mHomeNoticeAdapter;
    private Context mContext;
    private int mCurrentPage;
    private int mTotalPage;
    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_notice);
        mContext = this;
        initView();
    }
    private void initView() {

        mProgressDialog = AppUtils.createLoadingDialog(this);

        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.home_notice_title);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(this));
        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);

        //设置刷新文本
        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();

        mListView = (ListView) findViewById(R.id.list_view);
        mHomeNoticeList = new ArrayList<HomeNotice>();
        mHomeNoticeAdapter = new HomeNoticeAdapter(this, mHomeNoticeList);

        mListView.setAdapter(mHomeNoticeAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setEmptyView(findViewById(R.id.layout_empty));
        getHomeNoticeList(1);
    }


    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent mIntent = new Intent(HomeNoticeActivity.this,HomeNoticeDetailActivity.class);
            mIntent.putExtra("noticeDetail",mHomeNoticeList.get(position));
            startActivity(mIntent);
        }

    };
    private void getHomeNoticeList(int currentPage) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
        datas.put("currentPage", currentPage);

        mProgressDialog.show();

        DdApplication.getCommonManager().getHomeNoticeList(datas, mOnGetHomeNoticeListFinishedListener);
    }
    private CommonManager.OnGetHomeNoticeListFinishedListener mOnGetHomeNoticeListFinishedListener  = new CommonManager.OnGetHomeNoticeListFinishedListener() {
        @Override
        public void onGetHomeNoticeListFinished(Response response,Page page, List<HomeNotice> lists) {

            if (response.isSuccess()) {

                mCurrentPage = page.getCurrentPage();
                mTotalPage = page.getTotalPage();
                if (mCurrentPage == 1) {
                    mHomeNoticeList.clear();
                    mHomeNoticeList.addAll(lists);
                } else {
                    mHomeNoticeList.addAll(lists);
                }
                mHomeNoticeAdapter.notifyDataSetChanged();
            }else {
                AppUtils.handleErrorResponse(mContext, response);
            }

            mProgressDialog.dismiss();
            LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);
        }
    };
    private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            if(mCurrentPage < mTotalPage) {
                getHomeNoticeList(mCurrentPage + 1);
            } else {
                refreshLayout.finishLoadmoreWithNoMoreData();
            }
        }
        @Override

        public void onRefresh(RefreshLayout refreshlayout) {
            refreshLayout.resetNoMoreData();
            getHomeNoticeList(1);
        }
    };
}
