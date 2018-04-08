package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinju.android.api.Remind;
import com.jinju.android.constant.BroadcastType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.util.ViewUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.R;
import com.jinju.android.adapter.LoanFinancialAdapter;
import com.jinju.android.api.Financial;
import com.jinju.android.api.FinancialList;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.widget.FrameAnimationHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 理财Activity
 */
public class LoanActivity extends BaseActivity{

	private String mOperate;

	private int mCurrentPage = 0;
	private int mTotalPage;
	private LoanFinancialAdapter mFinancialAdapter;
	private List<Financial> mFinancialList;
	private int mFinancialOutCount = 0;//已售罄数量
	private int mActivityCount = 0;//活动标数量
	private int mPrivilegeCount = 0;//特权数量
	private int mChoicenessCount = 0;//精选数量

	private ListView listView;
	private List<Long> mSellTimeList = new ArrayList<Long>();
	private Timer timer;
	private TimerTask task;
	private TextView txtNameType;
	private String regId = "";
	private List<Remind> remindList = new ArrayList<Remind>();
	private String floatImg;
	private String floatUrl;
	private String floatType;
	private ImageView mImgFloatWindow;
	private TextView mTxtProductSellOut;
	private TextView mTxtProductRepayment;
	private List<String> mLoadInfoList;
	private SmartRefreshLayout refreshLayout;
	private Intent mIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	 	super.onCreate(savedInstanceState);

	 	setContentView(R.layout.activity_loan);
		//接收广播与MainActivity关联
		mIntent = new Intent(BroadcastType.MAIN_BROADCASTRECEIVER);
	 	initView();

	}

	public void initView() {

		txtNameType = (TextView) findViewById(R.id.txt_name_type);
		mFinancialList = new ArrayList<Financial>();
		mFinancialAdapter = new LoanFinancialAdapter(this, mFinancialList,mIntent);

		refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setRefreshHeader(new FrameAnimationHeader(this));
		refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
		//底部控件
		View bottomView = LayoutInflater.from(this).inflate(R.layout.layout_loan_bottom_view,null);
		mTxtProductSellOut = (TextView) bottomView.findViewById(R.id.txt_product_sell_out);
		mTxtProductRepayment = (TextView) bottomView.findViewById(R.id.txt_product_repayment);

		//设置刷新文本
		mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();
		listView = (ListView) findViewById(R.id.list_view);
		listView.setEmptyView(findViewById(R.id.layout_empty));
		listView.setAdapter(mFinancialAdapter);
		listView.setOnItemClickListener(mOnItemClickListener);
		listView.setOnScrollListener(mOnScrollListener);
		listView.addFooterView(bottomView);
		//浮窗
		mImgFloatWindow = (ImageView) findViewById(R.id.img_float_window);
		mImgFloatWindow.setOnClickListener(mImgFloatWindowOnClickListener);

		regId = MiPushClient.getRegId(LoanActivity.this);

	}

	@Override
 	protected void onResume() {
		super.onResume();
		getFinancialList(1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer!=null) {
			timer.cancel();
		}
		if (task!=null) {
			task.cancel();
		}
	}

	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if (resultCode == RESULT_OK && requestCode == LOAN_LOGIN) {
//			getFinancialList(1);
//		}
//	}

	public void getFinancialList(int currentPage) {
		if (TextUtils.isEmpty(regId)) {
			//有的手机获取不到regId，自定义一个regId传给后台，进行开售提醒。
			regId = "regidandroidlolcncomwcg";
		} else {
			//由于服务端url无法转换加号，所以进行转换
			regId = regId.replaceAll("\\+","");
		}
		Map<String, Object> datas = new HashMap<String, Object>();

		datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
		datas.put("currentPage", currentPage);
		datas.put("regId",regId);
		datas.put("iv", VersionUtils.getVersionName());

		DdApplication.getLoanManager().getFinancialList(datas, mOnGetFinancialListFinishedListener);
	}

	private LoanManager.OnGetFinancialListFinishedListener mOnGetFinancialListFinishedListener = new LoanManager.OnGetFinancialListFinishedListener() {

		@Override
		public void onGetFinancialListFinished(Response response, FinancialList financialList) {
			if (response.isSuccess()) {
				mCurrentPage = financialList.getPage().getCurrentPage();
				mTotalPage = financialList.getPage().getTotalPage();
				floatImg = financialList.getFloatImg();
				floatUrl = financialList.getFloatUrl();
				floatType = financialList.getFloatType();
				//浮窗
				if (!TextUtils.isEmpty(floatImg)) {
					mImgFloatWindow.setMaxWidth(ViewUtils.dip2px(LoanActivity.this,60));

					ImageUtils.displayImage(mImgFloatWindow, floatImg);
					mImgFloatWindow.setVisibility(View.VISIBLE);
				}else {
					mImgFloatWindow.setVisibility(View.GONE);
				}
				mTxtProductSellOut.setText(Html.fromHtml(getString(R.string.loan_product_sell_out,financialList.getSellOut())));
				mTxtProductRepayment.setText(Html.fromHtml(getString(R.string.loan_product_repayment,financialList.getRepayment())));

				mFinancialList.clear();

				//特权标
				mPrivilegeCount = financialList.getPrivilegeItems().size();
				mFinancialList.addAll(financialList.getPrivilegeItems());
				//活动标
				mActivityCount = financialList.getActivityItems().size();
				mFinancialList.addAll(financialList.getActivityItems());
				//精选标
				mChoicenessCount = financialList.getFinancialList().size();
				mFinancialList.addAll(financialList.getFinancialList());
				//已售罄
				mFinancialOutCount = financialList.getFinancialOutList().size();
				mFinancialList.addAll(financialList.getFinancialOutList());

				//倒计时时间
				mSellTimeList.clear();
				for (int i = 0; i < mFinancialList.size(); i++) {

					long time = mFinancialList.get(i).getBeginDuration();
					if (time < 1000) {
						time = 0;
					}
					mSellTimeList.add(time);
				}

				//保存开关提醒在本地
				remindList.clear();
				for (int i = 0;i< mFinancialList.size();i++) {
					Remind remind = new Remind();
					remind.setRemindFlag(mFinancialList.get(i).getIsRemindFlag());
					remindList.add(remind);
				}
				if (remindList!=null) {
					DdApplication.getConfigManager().setRemindList(remindList);
				}

				restart();

			} else {
				AppUtils.handleErrorResponse(LoanActivity.this, response);
			}

			LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);

		}

	};
	private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
		@Override
		public void onLoadmore(RefreshLayout refreshlayout) {
			refreshLayout.finishLoadmore();
		}

		@Override
		public void onRefresh(RefreshLayout refreshlayout) {
			getFinancialList(1);
		}
	};

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position < mActivityCount) {
				MobclickAgent.onEvent(LoanActivity.this, UmengTouchType.RP102_1);
			} else {
				MobclickAgent.onEvent(LoanActivity.this, UmengTouchType.RP102_2);
			}
			if (position<mFinancialList.size()) {//防止底部的控件被点击
				Intent intent = new Intent(LoanActivity.this, FinancialDetailActivity.class);
				intent.putExtra("id", mFinancialList.get(position).getId());
				startActivity(intent);
			}
		}

	};
	AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			if(firstVisibleItem < mPrivilegeCount) {
				txtNameType.setText(R.string.loan_privilege_project);
			} else {
				if (firstVisibleItem < mPrivilegeCount + mActivityCount) {
					txtNameType.setText(R.string.loan_activity_project);
				} else {
					if (firstVisibleItem < mPrivilegeCount+ mActivityCount+ mChoicenessCount) {
						txtNameType.setText(R.string.loan_choiceness_project);
					}else {
						txtNameType.setText(R.string.loan_choiceness_project);
					}
				}

			}

		}
	};


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
	}
	private View.OnClickListener mImgFloatWindowOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (floatType.equals("-1")) {
				if (!TextUtils.isEmpty(floatUrl)) {
					Intent intent = new Intent(LoanActivity.this, BaseJsBridgeWebViewActivity.class);
					intent.putExtra("url",floatUrl);
					startActivity(intent);
				}
			}

			if (floatType.equals("2")) {
				//刷新
				getFinancialList(1);
			}
		}
	};
	/**
	 * 重新开始倒计时
	 */
	public void restart() {

		if (timer!=null) {
			timer.cancel();
			timer = null;
		}
		if (task!=null) {
			task.cancel();
			task = null;
		}
		start();
	}

	/**
	 * 开始倒计时
	 */
	public void start() {


		if (timer==null) {
			timer = new Timer();
		}
		if (task==null) {
			// UI thread
			task = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {      // UI thread
						@Override
						public void run() {
							countDownTime();
						}
					});
				}
			};
		}
		//每隔1000毫秒执行一次
		timer.schedule(task, 0, 1000);
	}
	/**
	 * 倒计时数据刷新
	 */
	private boolean countDown = true;
	public void countDownTime() {
		//是否开启倒计时
		for (int i = 0; i < mSellTimeList.size(); i++) {
			if (mSellTimeList.get(i) > 1000) {
				countDown = true;
			}
		}
		if (countDown) {
			//开始倒计时
			for (int i = 0; i < mSellTimeList.size(); i++) {
				mSellTimeList.set(i, mSellTimeList.get(i) - 1000);
			}
			mFinancialAdapter.notifyChanged(mFinancialList,mPrivilegeCount,mActivityCount,mChoicenessCount,mFinancialOutCount,mSellTimeList,regId);
			countDown = false;

		} else {
			//停止倒计时
			if (timer!=null) {
				timer.cancel();
				timer = null;
			}
			if (task!=null) {
				task.cancel();
				task = null;
			}
			//停止倒计时刷新一次
			mFinancialAdapter.notifyChanged(mFinancialList,mPrivilegeCount,mActivityCount,mChoicenessCount,mFinancialOutCount,mSellTimeList,regId);
		}
	}
}