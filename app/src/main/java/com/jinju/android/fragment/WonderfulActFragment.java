package com.jinju.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jinju.android.R;
import com.jinju.android.adapter.DiscoverRecyclerAdapter;
import com.jinju.android.adapter.WonderfulAdapter;
import com.jinju.android.api.Discover;
import com.jinju.android.api.FindBean;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.util.VersionUtils;
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

public class WonderfulActFragment extends BaseFragment {

    private  ArrayList<FindBean>    findBeans;
    private List<Discover>          mWonderfulDiscoverList;
    private RecyclerView            mRecyclerView;
    private DiscoverRecyclerAdapter discoverAdapter;
    private View                    layout_empty;
    private ListView                wonderfulList;
    private WonderfulAdapter wonderfulAdapter;

    private int mCurrentPage = 0;
    private int mTotalPage;
    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWonderfulDiscoverList = new ArrayList<Discover>();
        findBeans = new ArrayList<FindBean>();
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
        discoverAdapter = new DiscoverRecyclerAdapter(getActivity(), mWonderfulDiscoverList, 0);
        discoverAdapter.setOnItemClickListener(new DiscoverRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String type = mWonderfulDiscoverList.get(position).getJumpType();
                String linkUrl = mWonderfulDiscoverList.get(position).getJumpUrl();
                Intent intent = new Intent(getActivity(), BaseJsBridgeWebViewActivity.class);
                intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH, linkUrl);
                startActivity(intent);
            }
        });

//        FindWonderfulAdapter findWonderfulAdapter = new FindWonderfulAdapter();
//        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();
//        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
//        refreshLayout.setRefreshHeader(new FrameAnimationHeader(getActivity()));
//        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
//        layout_empty = view.findViewById(R.id.layout_empty);
//         wonderfulList = (ListView) view.findViewById(R.id.lv_wonderful_list);
//
//
//         wonderfulAdapter = new WonderfulAdapter(getActivity(),wonderfulList);
//        wonderfulList.setAdapter(wonderfulAdapter);

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
        datas.put("iv", VersionUtils.getVersionName());

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

                mWonderfulDiscoverList.clear();
                mWonderfulDiscoverList.addAll(discoverList);

                discoverAdapter.notifyDataSetChanged();

                int discoverCount = mWonderfulDiscoverList.size();
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
            //所有精彩活动一次性加载，无加载更多
            refreshLayout.finishLoadmoreWithNoMoreData();
        }

        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            getDiscoverList(1);
        }
    };


}
