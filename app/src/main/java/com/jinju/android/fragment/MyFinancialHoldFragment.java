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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jinju.android.activity.MainActivity;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.widget.FrameAnimationHeader;
import com.jinju.android.R;
import com.jinju.android.activity.FinancialDetailActivity;
import com.jinju.android.activity.MyFinancialActivity;
import com.jinju.android.activity.PositionDetailActivity;
import com.jinju.android.adapter.MyFinancialHoldAdapter;
import com.jinju.android.api.ConfirmOrder;
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

public class MyFinancialHoldFragment extends BaseFragment {

	private int mCurrentPage = 0;
	private int mTotalPage;
	private ListView mListView;
	private MyFinancialHoldAdapter mMyFinancialHoldAdapter;
	private List<ConfirmOrder> mMyFinancialAllList;

	private MyFinancialActivity mActivity;
	private List<String> mLoadInfoList;
	private SmartRefreshLayout refreshLayout;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);

		mMyFinancialAllList = new ArrayList<ConfirmOrder>();
		mMyFinancialHoldAdapter = new MyFinancialHoldAdapter(mContext,mMyFinancialAllList);
		refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
		refreshLayout.setRefreshHeader(new FrameAnimationHeader(mContext));
		refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
		mListView = (ListView)view.findViewById(R.id.list_view);
		//设置刷新文本
		mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();

		mListView.setOnItemClickListener(mOnItemClickListener);
		mListView.setEmptyView(view.findViewById(R.id.layout_empty));
		mListView.setAdapter(mMyFinancialHoldAdapter);

		//去投资
		Button mBtnView = (Button) view.findViewById(R.id.btn_view);
		mBtnView.setOnClickListener(mBtnViewOnClickListener);
		mActivity = (MyFinancialActivity) getActivity();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getMyFinancialHoldList(1);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void getMyFinancialHoldList(int currentPage) {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("status", "hold");
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
				mActivity.setAssets(myPositionList.getTotalAsset(), myPositionList.getTotalOrderAmount(), myPositionList.getTotalIncome());


				mMyFinancialAllList.clear();

				mMyFinancialAllList.addAll(myPositionList.getConfirmOrderList());//募集中
				mMyFinancialAllList.addAll(myPositionList.getMyPositionList());//持仓


				//排序
				Collections.sort(mMyFinancialAllList,Collections.reverseOrder());

				mMyFinancialHoldAdapter.notifyDataSetInvalidated(mMyFinancialAllList);


			} else {
				AppUtils.handleErrorResponse(getActivity(), response);
			}

			LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);
		}

	};


	private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
		@Override
		public void onLoadmore(RefreshLayout refreshlayout) {
//			if(mCurrentPage < mTotalPage) {
//				getMyFinancialHoldList(mCurrentPage + 1);
//			} else {
//				refreshLayout.finishLoadmoreWithNoMoreData();
//			}
			refreshLayout.finishLoadmoreWithNoMoreData();
		}
		@Override

		public void onRefresh(RefreshLayout refreshlayout) {
			refreshLayout.resetNoMoreData();
			getMyFinancialHoldList(1);
		}
	};
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MobclickAgent.onEvent(mActivity, UmengTouchType.RP114_3);

			int type = mMyFinancialAllList.get(position).getType();
			if (type == 0) {
				Intent intent = new Intent(mActivity, FinancialDetailActivity.class);
				intent.putExtra("id", mMyFinancialAllList.get(position).getProductId());
				mActivity.startActivity(intent);
			} else {
				Intent intent = new Intent(mActivity, PositionDetailActivity.class);
				intent.putExtra("id",mMyFinancialAllList.get(position).getProductId());
				mActivity.startActivity(intent);
			}

		}

	};

	/**
	 * 点击去投资
	 */
	private View.OnClickListener mBtnViewOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("activityType", MainActivity.TAB_LOAN);
			mContext.startActivity(intent);
			getActivity().finish();
		}
	};
}