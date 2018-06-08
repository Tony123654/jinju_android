package com.jinju.android.manager;

import com.jinju.android.R;
import com.jinju.android.api.Discover;
import com.jinju.android.api.FinancialDetail;
import com.jinju.android.api.FinancialList;
import com.jinju.android.api.InvestRecord;
import com.jinju.android.api.MyPositionList;
import com.jinju.android.api.Order;
import com.jinju.android.api.Page;
import com.jinju.android.api.PayResult;
import com.jinju.android.api.PositionDetail;
import com.jinju.android.api.ProductIntroduce;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.builder.DiscoverListBuilder;
import com.jinju.android.builder.FinancialDetailBuilder;
import com.jinju.android.builder.FinancialListBuilder;
import com.jinju.android.builder.InvestRecordListBuilder;
import com.jinju.android.builder.MyPositionListBuilder;
import com.jinju.android.builder.OrderBuilder;
import com.jinju.android.builder.PageBuilder;
import com.jinju.android.builder.PayResultBuilder;
import com.jinju.android.builder.PositionDetailBuilder;
import com.jinju.android.builder.ProductIntroduceBuilder;
import com.jinju.android.constant.ApiType;
import com.jinju.android.constant.HttpMethod;
import com.jinju.android.interfaces.IManager;
import com.jinju.android.manager.HttpManager.OnRequestFinishedListener;
import com.jinju.android.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class LoanManager implements IManager {

	private HttpManager mHttpManager;

	@Override
	public void onInit() {
		mHttpManager = DdApplication.getHttpManager();
	}

	@Override
	public void onExit() {

	}

	public void getFinancialList(final Map<String, Object> datas, final OnGetFinancialListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_FINANCIAL_LIST, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {

					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						FinancialList financialList = FinancialListBuilder.build(jsonObject);

						listener.onGetFinancialListFinished(response, financialList);
					} else {
						listener.onGetFinancialListFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetFinancialListFinished(response, null);
				}
			}

		});
	}

	//发现页
	public void getDiscoverList(final Map<String, Object> datas, final OnGetDiscoverListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_DISCOVER_LIST, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Page page = PageBuilder.build(jsonObject);
						List<Discover> mDiscoverList = DiscoverListBuilder.buildList(jsonObject.optJSONArray("findPage"));
						List<Discover> mEndDiscoverList = DiscoverListBuilder.buildList(jsonObject.optJSONArray("findActPage"));
						List<Discover> mDiscoverTopImg = DiscoverListBuilder.buildList(jsonObject.optJSONArray("buttonList"));
						listener.onGetDiscoverListFinished(response,page, mDiscoverList,mEndDiscoverList,mDiscoverTopImg);
					} else {
						listener.onGetDiscoverListFinished(response,null, null,null,null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetDiscoverListFinished(response,null, null,null,null);
				}
			}

		});
	}
	//产品介绍
	public void getProductIntroduce(final Map<String, Object> datas, final OnGetProductIntroduceFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.PRODUCT_INTRODUCE, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						ProductIntroduce financialList = ProductIntroduceBuilder.build(jsonObject);
						listener.onGetProductIntroduceFinished(response,financialList);
					} else {
						listener.onGetProductIntroduceFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetProductIntroduceFinished(response,null);
				}
			}

		});
	}
	//投资记录
	public void getInvestRecord(final Map<String, Object> datas, final OnGetInvestRecordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.INVEST_RECORD, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Page page = PageBuilder.build(jsonObject);
						int countRecord = jsonObject.optInt("count");
						List<InvestRecord> orderList = InvestRecordListBuilder.buildList(jsonObject.optJSONArray("orderList"));
						List<InvestRecord> topThreeList = InvestRecordListBuilder.buildList(jsonObject.optJSONArray("techinca"));
						listener.onGetInvestRecordFinished(response,countRecord,page,orderList,topThreeList);
					} else {
						listener.onGetInvestRecordFinished(response,0,null,null,null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetInvestRecordFinished(response,0,null,null,null);
				}
			}

		});
	}
	public void getFinancialDetail(final Map<String, Object> datas, final OnGetFinancialDetailFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_FINANCIAL_DETAIL, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {

						JSONObject jsonObject = new JSONObject(response.getBody());
						FinancialDetail financialDetail = FinancialDetailBuilder.build(jsonObject);
						listener.onGetFinancialDetailFinished(response, financialDetail);
					} else {
						listener.onGetFinancialDetailFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetFinancialDetailFinished(response, null);
				}
			}

		});
	}

	//已售罄更多
	public void getFinancialMoreList(final Map<String, Object> datas, final OnGetFinancialMoreListFinishedListener listener)  {
		mHttpManager.sendRequest(ApiType.GET_MORE_FINANCIAL_LIST, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						FinancialList financialList = FinancialListBuilder.build(jsonObject);
						listener.onGetFinancialMoreListFinished(response, financialList);
					} else {
						listener.onGetFinancialMoreListFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetFinancialMoreListFinished(response, null);
				}
			}

		});
	}
	public void confirmFinancialOrder(final Map<String, Object> datas, final OnConfirmFinancialOrderFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.FINANCIAL_CONFIRM_ORDER, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Order order = OrderBuilder.build(jsonObject);
						listener.onConfirmFinancialOrderFinished(response, order);
					} else {
						listener.onConfirmFinancialOrderFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onConfirmFinancialOrderFinished(response, null);
				}
			}

		});
	}
	
	public void payFinancial(final Map<String, Object> datas, final OnPayFinancialFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.FINANCIAL_PAY, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						PayResult payResult = PayResultBuilder.build(jsonObject);
						listener.onPayFinancialFinished(response, payResult);
					} else {
						listener.onPayFinancialFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onPayFinancialFinished(response, null);
				}
			}

		});
	}

	public void getMyPositionList(final Map<String, Object> datas, final OnGetMyPositionListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_MY_FINANCIAL_POSITION, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						MyPositionList myPositionList = MyPositionListBuilder.build(jsonObject);
						listener.onGetMyPositionListFinished(response, myPositionList);
					} else {
						listener.onGetMyPositionListFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetMyPositionListFinished(response, null);
				}
			}

		});
	}
	
	public void getPositionDetial(final Map<String, Object> datas, final OnGetPositionDetailFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_FINANCIAL_POSITION_DETAIL, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());

						PositionDetail positionDetail = PositionDetailBuilder.build(jsonObject);
						listener.onGetPositionDetailFinished(response, positionDetail);
					} else {
						listener.onGetPositionDetailFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetPositionDetailFinished(response, null);
				}
			}

		});
	}
	//设置开售提醒
	public void setSaleRemind(final Map<String, Object> datas,final int position, final OnSetSaleRemindFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.SET_SALE_REMIND, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {

				listener.onSetSaleRemindFinished(response,position);

			}

		});
	}
	//取消开售提醒
	public void cancelSaleRemind(final Map<String, Object> datas,final int position, final OnCancelSaleRemindFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CANCEL_SALE_REMIND, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {

				listener.OnCancelSaleRemindFinished(response,position);

			}

		});
	}
	public interface OnSetSaleRemindFinishedListener {

		public void onSetSaleRemindFinished(Response response,int position);

	}
	public interface OnCancelSaleRemindFinishedListener {

		public void OnCancelSaleRemindFinished(Response response,int position);

	}
	public interface OnGetFinancialListFinishedListener {

		public void onGetFinancialListFinished(Response response, FinancialList financialList);

	}
	public interface OnGetDiscoverListFinishedListener {

		public void onGetDiscoverListFinished(Response response,Page page, List<Discover> discoverList,
											  List<Discover> endDiscoverList,List<Discover> mDiscoverTopImg);

	}
	public interface OnGetProductIntroduceFinishedListener {

		public void onGetProductIntroduceFinished(Response response, ProductIntroduce productIntroduce);

	}
	public interface OnGetInvestRecordFinishedListener {

		public void onGetInvestRecordFinished(Response response,int countRecord,Page page,List<InvestRecord> orderList,List<InvestRecord> topThreeList);

	}
	public interface OnGetFinancialMoreListFinishedListener {
		public void onGetFinancialMoreListFinished(Response response, FinancialList financialList);
	}
	public interface OnGetFinancialDetailFinishedListener {

		public void onGetFinancialDetailFinished(Response response, FinancialDetail financialDetail);

	}



	public interface OnGetMyPositionListFinishedListener {

		public void onGetMyPositionListFinished(Response response, MyPositionList myPositionList);

	}
	
	public interface OnGetPositionDetailFinishedListener {
		
		public void onGetPositionDetailFinished(Response response, PositionDetail positionDetail);
		
	}
	
	public interface OnConfirmFinancialOrderFinishedListener {
		
		public void onConfirmFinancialOrderFinished(Response response, Order order);
		
	}
	
	public interface OnPayFinancialFinishedListener {
		
		public void onPayFinancialFinished(Response response, PayResult payResult);
		
	}

}