package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.PositionDetail;
import com.jinju.android.api.Response;
import com.jinju.android.api.TradeHistory;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.LoanManager.OnGetPositionDetailFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的资产详情
 */
public class PositionDetailActivity extends BaseActivity {
	public static final int TRANSFER_CONFIRM = 1;
	
	private TextView mTxtPositionIncome;
	private TextView mTxtYearInterest;
	private TextView mTxtTodayIncome;
	private TextView mTxtCurrentPosition;
	private TextView mTxtEndDate;
	private TextView mTxtReturnDate;
	
	private TextView mTxtName;
	private TextView mTxtProductProtocol;
	
	private LinearLayout mLayoutTradeHistory;

	private Dialog mProgressDialog;
	private PositionDetail mPositionDetail;
	private long mId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_position_detail);
		
		Intent intent = getIntent();
		mId = intent.getLongExtra("id", 0);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.position_detail_title);

		mTxtPositionIncome = (TextView) findViewById(R.id.txt_position_income);
		mTxtYearInterest = (TextView) findViewById(R.id.txt_year_interest);
		mTxtTodayIncome = (TextView) findViewById(R.id.txt_today_income);
		mTxtCurrentPosition = (TextView) findViewById(R.id.txt_current_position);
		mTxtEndDate = (TextView) findViewById(R.id.txt_end_date);
		mTxtReturnDate = (TextView) findViewById(R.id.txt_return_date);
		
		mTxtName = (TextView) findViewById(R.id.txt_name);
		mTxtName.setOnClickListener(mTxtNameOnClickListener);
		
		mTxtProductProtocol = (TextView) findViewById(R.id.txt_product_protocol);
		mTxtProductProtocol.setOnClickListener(mTxtProductProtocolOnClickListener);
		
		mLayoutTradeHistory = (LinearLayout) findViewById(R.id.layout_trade_history);
		
		mProgressDialog = AppUtils.createLoadingDialog(this);
		getPositionDetail();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return super.dispatchTouchEvent(event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == TRANSFER_CONFIRM) {
			finish();
		}
	}
	
	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
			overridePendingTransition(0, R.anim.activity_out_from_right);
		}

	};
	
	private OnClickListener mTxtNameOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(PositionDetailActivity.this, FinancialDetailActivity.class);
			intent.putExtra("id", mPositionDetail.getProductId());
			startActivity(intent);
		}
		
	};
	
	private OnClickListener mTxtProductProtocolOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(PositionDetailActivity.this, BaseJsBridgeWebViewActivity.class);
//			intent.putExtra("title", getString(R.string.product_protocol_title));
			intent.putExtra("url", mPositionDetail.getProductContractUrl());
			startActivity(intent);
		}
	};

	private OnClickListener mTxtTransferProtocolOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(PositionDetailActivity.this, BaseJsBridgeWebViewActivity.class);
//			intent.putExtra("title", getString(R.string.transfer_protocol_title));
			intent.putExtra("url", mPositionDetail.getTransferContractUrl());
			startActivity(intent);
		}

	};
	
	private void getPositionDetail() {
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("productId", mId);
		datas.put("iv", VersionUtils.getVersionName());
		mProgressDialog.show();
		DdApplication.getLoanManager().getPositionDetial(datas, mOnGetPositionDetailFinishedListener);
	}

	private OnGetPositionDetailFinishedListener mOnGetPositionDetailFinishedListener = new OnGetPositionDetailFinishedListener() {

		@Override
		public void onGetPositionDetailFinished(Response response,
				PositionDetail positionDetail) {
			// TODO Auto-generated method stub
			if (response.isSuccess()) {
				mPositionDetail = positionDetail;
				
				mTxtPositionIncome.setText(DataUtils.convertCurrencyFormat(positionDetail.getPositionIncome()));
				mTxtYearInterest.setText(getString(R.string.percent_number, positionDetail.getYearInterest()));
				mTxtTodayIncome.setText(DataUtils.convertCurrencyFormat(PositionDetailActivity.this, positionDetail.getYesterdayIncome()));
				mTxtCurrentPosition.setText(DataUtils.convertCurrencyFormat(PositionDetailActivity.this, positionDetail.getPositionAmount()));
				mTxtEndDate.setText(positionDetail.getGmtEnd());
				mTxtReturnDate.setText(positionDetail.getGmtFundReturn());
				
				mTxtName.setText(positionDetail.getProductName());
				mLayoutTradeHistory.removeAllViews();
				List<TradeHistory> tradeHistoryList = positionDetail.getTradeHistoryList();
				for(int i = 0; i < tradeHistoryList.size(); i++) {
					TradeHistory tradeHistory = tradeHistoryList.get(i);

					View view = View.inflate(PositionDetailActivity.this, R.layout.layout_trade_history_item, null);
					TextView txtTime = (TextView) view.findViewById(R.id.txt_time);
					TextView txtStatus = (TextView) view.findViewById(R.id.txt_status);
					TextView txtAmount = (TextView) view.findViewById(R.id.txt_amount);
					TextView txtWelfareType = (TextView) view.findViewById(R.id.txt_welfare_type);


					txtTime.setText(tradeHistory.getTradeTime());
					txtStatus.setText(tradeHistory.getStatusDesc());
					txtWelfareType.setText(tradeHistory.getGiftAddition());
					txtAmount.setText(DataUtils.convertCurrencyFormat(tradeHistory.getTradeValue()));

					mLayoutTradeHistory.addView(view);
				}
			} else {
				AppUtils.handleErrorResponse(PositionDetailActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};
}