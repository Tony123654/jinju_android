package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.NoticeAdapter;
import com.jinju.android.api.Notice;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
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
 * 公告
 *
 * Created by Libra on 2017/6/27.
 */

public class NoticeActivity extends BaseActivity {

    private RelativeLayout mBtBack;
    private TextView mTxtTitle;
    private ListView mListView;
    private NoticeAdapter mNoticeAdapter;
    private List<Notice> mNoticeList;
    private int mCurrentPage;
    private int mTotalPage;
    private Context mContext;
    private Dialog mProgressDialog;
    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        mContext = this;
        initView();
    }

    private void initView() {
        mBtBack = (RelativeLayout) findViewById(R.id.btn_back);
        mBtBack.setOnClickListener(mBtBackOnClickListener);
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtTitle.setText(R.string.notice_title);
        mNoticeList = new ArrayList<Notice>();
        mNoticeAdapter = new NoticeAdapter(this,mNoticeList);

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(this));
        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
        mListView = (ListView)findViewById(R.id.list_view);
        //设置刷新文本
        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();

        mListView.setEmptyView(findViewById(R.id.layout_empty));
        mListView.setAdapter(mNoticeAdapter);

        mProgressDialog = AppUtils.createLoadingDialog(this);
        getMyMessageData(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();

    }

    private void getMyMessageData(int currentPage) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
        datas.put("currentPage", currentPage);

        mProgressDialog.show();
        DdApplication.getUserManager().getMyMessage(datas,mOnMyMessageFinishedListener);
    }
    private UserManager.OnMyMessageFinishedListener mOnMyMessageFinishedListener = new UserManager.OnMyMessageFinishedListener() {
        @Override
        public void OnMyMessageFinished(Response response,Page page, List<Notice> noticeList) {
            if (response.isSuccess()) {
                mCurrentPage = page.getCurrentPage();
                mTotalPage = page.getTotalPage();

                if (mCurrentPage == 1) {
                    mNoticeList.clear();
                    mNoticeList.addAll(noticeList);
                    mNoticeAdapter.notifyDataSetChanged();
                } else {
                    mNoticeList.addAll(noticeList);
                    mNoticeAdapter.notifyDataSetChanged();
                }

            }else {
                AppUtils.handleErrorResponse(mContext, response);
            }

            mProgressDialog.dismiss();
            LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);

        }
    };
    private View.OnClickListener mBtBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult(RESULT_OK);
            finish();
        }
    };

    private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            if(mCurrentPage < mTotalPage) {
                getMyMessageData(mCurrentPage + 1);
            } else {
                refreshLayout.finishLoadmoreWithNoMoreData();
            }
        }
        @Override

        public void onRefresh(RefreshLayout refreshlayout) {
            refreshLayout.resetNoMoreData();
            getMyMessageData(1);
        }
    };
}
