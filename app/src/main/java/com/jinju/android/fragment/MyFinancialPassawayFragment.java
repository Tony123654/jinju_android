package com.jinju.android.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jinju.android.activity.PositionDetailActivity;
import com.jinju.android.api.ConfirmOrder;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.widget.FrameAnimationHeader;

import com.jinju.android.R;
import com.jinju.android.activity.MyFinancialActivity;
import com.jinju.android.adapter.MyFinancialPassawayAdapter;
import com.jinju.android.api.MyPositionList;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.manager.LoanManager.OnGetMyPositionListFinishedListener;
import com.jinju.android.util.AppUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.umeng.analytics.MobclickAgent;

public class MyFinancialPassawayFragment extends BaseFragment {

	private int mCurrentPage = 0;
	private int mTotalPage;
	private ListView mListView;
	private MyFinancialPassawayAdapter mMyFinancialPassawayAdapter;
	private List<ConfirmOrder> mMyFinancialPassawayList;
	private MyFinancialActivity mActivity;
	private List<String> mLoadInfoList;
	private Context mContext;
	private SmartRefreshLayout refreshLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);

		mContext = getActivity();
		mMyFinancialPassawayList = new ArrayList<ConfirmOrder>();
		mMyFinancialPassawayAdapter = new MyFinancialPassawayAdapter(mContext, mMyFinancialPassawayList);

		refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
		refreshLayout.setRefreshHeader(new FrameAnimationHeader(mContext));
		refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
		mListView = (ListView) view.findViewById(R.id.list_view);
		//设置刷新文本
		mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();
		mListView.setOnItemClickListener(mOnItemClickListener);
		mListView.setEmptyView(view.findViewById(R.id.layout_empty));
		mListView.setAdapter(mMyFinancialPassawayAdapter);
		
		mActivity = (MyFinancialActivity) getActivity();

		TextView mTxtEmptyContent = (TextView) view.findViewById(R.id.txt_empty_content);
		mTxtEmptyContent.setText(getResources().getString(R.string.my_financial_empty_passaway_content));
		//去投资
		Button mBtnView = (Button) view.findViewById(R.id.btn_view);
		mBtnView.setVisibility(View.GONE);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getMyFinancialPassawayList(1);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void getMyFinancialPassawayList(int currentPage) {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("status", "passaway");
		datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
		datas.put("currentPage", currentPage);
		datas.put("iv", DdApplication.getVersionName());
		DdApplication.getLoanManager().getMyPositionList(datas, mOnGetMyPositionListFinishedListener);
	}

	private OnGetMyPositionListFinishedListener mOnGetMyPositionListFinishedListener = new OnGetMyPositionListFinishedListener() {

		@Override
		public void onGetMyPositionListFinished(Response response,
				MyPositionList myPositionList) {
			// TODO Auto-generated method stub
			if (response.isSuccess()) {
				mCurrentPage = myPositionList.getPage().getCurrentPage();
				mTotalPage = myPositionList.getPage().getTotalPage();

				if (mCurrentPage == 1) {
					mMyFinancialPassawayList.clear();
					mMyFinancialPassawayList.addAll(myPositionList.getMyPositionList());
					Collections.sort(mMyFinancialPassawayList,Collections.reverseOrder());
					mMyFinancialPassawayAdapter.notifyDataSetInvalidated();
				} else {
					mMyFinancialPassawayList.addAll(myPositionList.getMyPositionList());
					Collections.sort(mMyFinancialPassawayList,Collections.reverseOrder());
					mMyFinancialPassawayAdapter.notifyDataSetChanged();
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
				getMyFinancialPassawayList(mCurrentPage + 1);
			} else {
				refreshLayout.finishLoadmoreWithNoMoreData();
			}
		}
		@Override

		public void onRefresh(RefreshLayout refreshlayout) {
			refreshLayout.resetNoMoreData();
			getMyFinancialPassawayList(1);
		}
	};
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MobclickAgent.onEvent(mActivity, UmengTouchType.RP114_3);

			if(position < mMyFinancialPassawayList.size()) {

				Intent intent = new Intent(mActivity, PositionDetailActivity.class);
				intent.putExtra("id", mMyFinancialPassawayList.get(position).getProductId());
				mActivity.startActivity(intent);
			}
		}

	};
}