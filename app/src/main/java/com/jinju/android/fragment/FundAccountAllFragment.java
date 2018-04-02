package com.jinju.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jinju.android.R;
import com.jinju.android.activity.FundAccountActivity;
import com.jinju.android.adapter.FundAccountAdapter;
import com.jinju.android.api.AccountLog;
import com.jinju.android.api.AccountLogInfo;
import com.jinju.android.api.Category;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AccountLogType;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.interfaces.FundAccountAllCallBack;
import com.jinju.android.manager.UserManager;
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
 * Created by Libra on 2018/3/8.
 * 全部
 */

public class FundAccountAllFragment extends BaseFragment {
    private String mLogType = AccountLogType.ALL;

    private View mView;
    private ListView listView;
    private Context mContext;
    private int mCodeSelectedIndex = -1;
    private int mCurrentPage = 0;
    private int mTotalPage;

    private List<Category> mCodeList;
    private List<AccountLog> mAccountLogList;
    private FundAccountAdapter mFundAccountAdapter;
    private SmartRefreshLayout refreshLayout;
    private List<String> mLoadInfoList;
    private FundAccountAllCallBack fundAccountAllCallBack;
    private RelativeLayout layoutTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();
        mContext = getActivity();
        mAccountLogList = new ArrayList<AccountLog>();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fundAccountAllCallBack = (FundAccountAllCallBack)context;
        ((FundAccountActivity)context).setOnAllSelectTagListener(mOnAllSelectTagListener);
    }
    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fund_account, container, false);

        refreshLayout = (SmartRefreshLayout)mView.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(mContext));
        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);

        layoutTitle = (RelativeLayout) mView.findViewById(R.id.layout_title);
        listView = (ListView) mView.findViewById(R.id.list_view);
        mFundAccountAdapter = new FundAccountAdapter(mContext,mAccountLogList);

        listView.setEmptyView(mView.findViewById(R.id.layout_empty));
        listView.setAdapter(mFundAccountAdapter);
        return mView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {//判断Fragment已经依附Activity
            getFundAccountLogList(1);

        }
    }
    private void getFundAccountLogList(int currentPage) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
        datas.put("currentPage", currentPage);
        if (mCodeSelectedIndex >0 && mCodeSelectedIndex == mCodeList.size()+1) {//筛选标签  其他
            mLogType = AccountLogType.FREEZE;
            datas.put("accountLogType", mLogType);
        } else {
            mLogType = AccountLogType.ALL;
            datas.put("accountLogType", mLogType);
        }

        if (mCodeSelectedIndex >0&&mCodeSelectedIndex <= mCodeList.size()){//筛选标签  不是其他
            datas.put("subTransCode", mCodeList.get(mCodeSelectedIndex-1).getCode());
        }

        DdApplication.getUserManager().getFundAccountLogList(datas, mOnGetFundAccountLogListFinishedListener);

    }
    private UserManager.OnGetFundAccountLogListFinishedListener mOnGetFundAccountLogListFinishedListener = new UserManager.OnGetFundAccountLogListFinishedListener() {

        @Override
        public void onGetFundAccountLogListFinished(Response response, AccountLogInfo accountLogInfo) {
            if (response.isSuccess()) {
                final List<AccountLog> accountLogList = accountLogInfo.getAccountLogList();
                mCurrentPage = accountLogInfo.getPage().getCurrentPage();
                mTotalPage = accountLogInfo.getPage().getTotalPage();


                if (mLogType == AccountLogType.ALL) {
                    mCodeList = accountLogInfo.getSubTransCodes();
                }
                //回调
                fundAccountAllCallBack.fundAccountAllValue(mCodeList);

                if (mCurrentPage == 1) {
                    mAccountLogList.clear();
                    mAccountLogList.addAll(accountLogList);
                    mFundAccountAdapter.notifyDataSetInvalidated();
                } else {
                    mAccountLogList.addAll(accountLogList);
                    mFundAccountAdapter.notifyDataSetChanged();
                }

                if (mAccountLogList.size()>0) {
                    layoutTitle.setVisibility(View.VISIBLE);
                }else {
                    layoutTitle.setVisibility(View.GONE);
                }
            } else {
                AppUtils.handleErrorResponse(getActivity(), response);
            }

            LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);

        }

    };
    private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            if(mCurrentPage < mTotalPage) {
                getFundAccountLogList(mCurrentPage + 1);
            } else {
                refreshLayout.finishLoadmoreWithNoMoreData();
            }
        }
        @Override

        public void onRefresh(RefreshLayout refreshlayout) {
            refreshLayout.resetNoMoreData();
            //初始化数据
            getFundAccountLogList(1);
        }
    };


    private FundAccountActivity.OnAllSelectTagListener mOnAllSelectTagListener= new FundAccountActivity.OnAllSelectTagListener() {
        @Override
        public void onAllSelectTag(int selectTag) {
            mCodeSelectedIndex = selectTag;

            refreshLayout.resetNoMoreData();
            getFundAccountLogList(1);
        }
    };
}
