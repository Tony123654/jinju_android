package com.jinju.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinju.android.R;
import com.jinju.android.adapter.DiscoverRecyclerAdapter;
import com.jinju.android.api.Discover;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.widget.FrameAnimationHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Musixiaoge on 2018/1/21.
 */

public class PastActFragment extends BaseFragment {

    private List<Discover> mPastDicoverList;
    private RecyclerView mRecyclerView;
    private DiscoverRecyclerAdapter discoverAdapter;
    private View layout_empty;

    private int mCurrentPage = 0;
    private int mTotalPage;
    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPastDicoverList = new ArrayList<Discover>();
    }

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_recycler, container, false);

        //设置刷新文本
        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(getActivity()));
        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
        layout_empty = view.findViewById(R.id.layout_empty);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        discoverAdapter = new DiscoverRecyclerAdapter(getActivity(), mPastDicoverList, 1);
        discoverAdapter.setOnItemClickListener(new DiscoverRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String type = mPastDicoverList.get(position).getJumpType();
                String linkUrl = mPastDicoverList.get(position).getJumpUrl();
                Intent intent = new Intent(getActivity(), BaseJsBridgeWebViewActivity.class);
                intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH, linkUrl);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(discoverAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isAdded()) {//判断Fragment已经依附Activity
            getDiscoverList(1);
        }
    }

    private void getDiscoverList(int currentPage) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
        datas.put("currentPage", currentPage);
//        datas.put("iv", VersionUtils.getVersionName());
        datas.put("signature","123456");
        /**
         * 加载数据
         */
        DdApplication.getLoanManager().getDiscoverList(datas, mOnGetDiscoverListFinishedListener);
    }

    private LoanManager.OnGetDiscoverListFinishedListener mOnGetDiscoverListFinishedListener = new LoanManager.OnGetDiscoverListFinishedListener() {
        @Override
        public void onGetDiscoverListFinished(Response response, Page page, List<Discover> discoverList, List<Discover> endDiscoverList,
                                              List<Discover> mDiscoverTopImg) {
            if (response.isSuccess()) {

                mCurrentPage = page.getCurrentPage();
                mTotalPage = page.getTotalPage();
                if (mCurrentPage == 1) {
                    mPastDicoverList.clear();
                    mPastDicoverList.addAll(endDiscoverList);
                } else {
                    mPastDicoverList.addAll(endDiscoverList);
                }
                discoverAdapter.notifyDataSetChanged();

                int discoverCount = mPastDicoverList.size();
                if (discoverCount > 0) {
                    layout_empty.setVisibility(View.GONE);
                } else {
                    layout_empty.setVisibility(View.VISIBLE);
                }
            } else {
                AppUtils.handleErrorResponse(getActivity(), response);
            }
            LoadTitleUtils.setRefreshLayout(refreshLayout, mCurrentPage, mLoadInfoList);
        }
    };

    private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener = new OnRefreshLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            if (mCurrentPage < mTotalPage) {
                getDiscoverList(mCurrentPage + 1);
            } else {
                refreshLayout.finishLoadmoreWithNoMoreData();
            }
        }

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            getDiscoverList(1);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
