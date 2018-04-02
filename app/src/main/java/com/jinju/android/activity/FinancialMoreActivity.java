package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.FinancialMoreAdapter;
import com.jinju.android.api.Financial;
import com.jinju.android.api.FinancialList;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.LoanManager;
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
 * Created by Libra on 2017/6/6.
 */

public class FinancialMoreActivity extends BaseActivity {

    private ListView mListView;
    private FinancialMoreAdapter mFinancialMoreAdapter;
    private List<Financial> mMoreList;
    private int mCurrentPage = 0;
    private int mTotalPage;
    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_more);
        initView();
    }

    private void initView() {
        mMoreList = new ArrayList<Financial>();
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(this));
        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
        mListView = (ListView) findViewById(R.id.list_view);
        mFinancialMoreAdapter = new FinancialMoreAdapter(this, mMoreList);
        TextView txtTitle = (TextView)findViewById(R.id.txt_title);
        txtTitle.setText("更多");
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置刷新文本
        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();
        LoadTitleUtils.setLoadTxt(mLoadInfoList);

        mListView.setEmptyView(findViewById(R.id.layout_empty));

        mListView.setAdapter(mFinancialMoreAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);


        getMoreList(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void getMoreList(int currentPage) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("currentPage", currentPage);

        DdApplication.getLoanManager().getFinancialMoreList(datas, mOnGetFinancialMoreListFinishedListener);
    }
    private LoanManager.OnGetFinancialMoreListFinishedListener mOnGetFinancialMoreListFinishedListener = new LoanManager.OnGetFinancialMoreListFinishedListener() {
        @Override
        public void onGetFinancialMoreListFinished(Response response, FinancialList financialList) {
            if (response.isSuccess()) {
                mCurrentPage = financialList.getPage().getCurrentPage();
                mTotalPage = financialList.getPage().getTotalPage();
                if (mCurrentPage == 1) {
                    mMoreList.clear();
                    mMoreList.addAll(financialList.getFinancialList());
                } else {
                    mMoreList.addAll(financialList.getFinancialList());
                }

            } else {
                AppUtils.handleErrorResponse(FinancialMoreActivity.this, response);
            }

            mFinancialMoreAdapter.notifyDataSetChanged();
            LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(FinancialMoreActivity.this, FinancialDetailActivity.class);
            intent.putExtra("id", mMoreList.get(position).getId());
            startActivity(intent);
        }

    };

    private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            if(mCurrentPage < mTotalPage) {
                getMoreList(mCurrentPage + 1);
            } else {
                refreshLayout.finishLoadmoreWithNoMoreData();
            }
        }
        @Override

        public void onRefresh(RefreshLayout refreshlayout) {
            refreshLayout.resetNoMoreData();
            getMoreList(1);
        }
    };

}
