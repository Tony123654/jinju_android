package com.jinju.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.widget.FrameAnimationHeader;
import com.jinju.android.R;
import com.jinju.android.adapter.ChargeRecordAdapter;
import com.jinju.android.api.ChargeRecord;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.manager.TradeManager.OnGetChargeRecordFinishedListener;
import com.jinju.android.util.AppUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

/**
 * 充值记录
 */
public class ChargeRecordActivity extends BaseActivity {

	private int mCurrentPage = 0;
	private int mTotalPage;
	
	private ListView mListView;
	private ChargeRecordAdapter mChargeRecordAdapter;
	private List<ChargeRecord> mChargeRecordList;
	private Dialog mProgressDialog;
	private List<String> mLoadInfoList;
	private SmartRefreshLayout refreshLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charge_record);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.charge_record_title);

		mChargeRecordList = new ArrayList<ChargeRecord>();
		mChargeRecordAdapter = new ChargeRecordAdapter(this, mChargeRecordList);

		refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setRefreshHeader(new FrameAnimationHeader(this));
		refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
		mListView = (ListView) findViewById(R.id.list_view);
		//设置刷新文本
		mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();

		mListView.setEmptyView(findViewById(R.id.layout_empty));
		mListView.setAdapter(mChargeRecordAdapter);

		mProgressDialog = AppUtils.createLoadingDialog(this);
		getChargeRecord(1);
	}

	@Override
	public void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}
	
	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
		
	};

	private void getChargeRecord(int currentPage) {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
		datas.put("currentPage", currentPage);

		mProgressDialog.show();
		DdApplication.getTradeManager().getChargeRecord(datas, mOnGetChargeRecordFinishedListener);
	}

	private OnGetChargeRecordFinishedListener mOnGetChargeRecordFinishedListener = new OnGetChargeRecordFinishedListener() {

		@Override
		public void onGetChargeRecordFinished(Response response, Page page, List<ChargeRecord> chargeRecordList) {
			if (response.isSuccess()) {

				mCurrentPage = page.getCurrentPage();
				mTotalPage = page.getTotalPage();

				if (mCurrentPage == 1) {
					mChargeRecordList.clear();
					mChargeRecordList.addAll(chargeRecordList);
					mChargeRecordAdapter.notifyDataSetInvalidated();
				} else {
					mChargeRecordList.addAll(chargeRecordList);
					mChargeRecordAdapter.notifyDataSetChanged();
				}
			} else {
				AppUtils.handleErrorResponse(ChargeRecordActivity.this, response);
			}

			mProgressDialog.dismiss();
			LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);

		}

	};

	private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
		@Override
		public void onLoadmore(RefreshLayout refreshlayout) {
			if(mCurrentPage < mTotalPage) {
				getChargeRecord(mCurrentPage + 1);
			} else {
				refreshLayout.finishLoadmoreWithNoMoreData();
			}
		}
		@Override

		public void onRefresh(RefreshLayout refreshlayout) {
			refreshLayout.resetNoMoreData();
			getChargeRecord(1);
		}
	};
}