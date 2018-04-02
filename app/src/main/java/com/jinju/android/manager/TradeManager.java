package com.jinju.android.manager;

import com.jinju.android.R;
import com.jinju.android.api.AuthCardDetail;
import com.jinju.android.api.BankDetail;
import com.jinju.android.api.ChargeRecord;
import com.jinju.android.api.MyBank;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.api.WithdrawDetail;
import com.jinju.android.api.WithdrawRecord;
import com.jinju.android.base.DdApplication;
import com.jinju.android.builder.AuthCardDetailBuilder;
import com.jinju.android.builder.BankDetailBuilder;
import com.jinju.android.builder.ChargeRecordBuilder;
import com.jinju.android.builder.MyBankBuilder;
import com.jinju.android.builder.PageBuilder;
import com.jinju.android.builder.WithdrawDetailBuilder;
import com.jinju.android.builder.WithdrawRecordBuilder;
import com.jinju.android.constant.ApiType;
import com.jinju.android.constant.HttpMethod;
import com.jinju.android.interfaces.IManager;
import com.jinju.android.manager.HttpManager.OnRequestFinishedListener;
import com.jinju.android.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class TradeManager implements IManager {

	private HttpManager mHttpManager;

	@Override
	public void onInit() {
		mHttpManager = DdApplication.getHttpManager();
	}

	@Override
	public void onExit() {

	}

	public void initWithdraw(final Map<String, Object> datas, final OnInitWithdrawFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.WITHDRAW, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						WithdrawDetail withdrawDetail = WithdrawDetailBuilder.build(jsonObject);
						listener.onInitWithdrawFinished(response, withdrawDetail);
					} else {
						listener.onInitWithdrawFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onInitWithdrawFinished(response, null);
				}
			}

		});
	}

	public void withdraw(final Map<String, Object> datas, final OnWithdrawFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.WITHDRAW, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String getMoneyDate = jsonObject.optString("getMoneyDate");
						listener.onWithdrawFinished(response, getMoneyDate);
					} else {
						listener.onWithdrawFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onWithdrawFinished(response, null);
				}
			}

		});
	}

	public void getBankList(final OnGetBankListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_BANK_LIST, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						boolean hasFastPayBank = jsonObject.optBoolean("hasFastPayBank");
						List<MyBank> myBankList = MyBankBuilder.buildList(jsonObject.optJSONArray("myBanks"));
						int memberStep = jsonObject.optInt("memberStep");
						listener.onGetBankListFinished(response, hasFastPayBank, myBankList,memberStep);
					} else {
						listener.onGetBankListFinished(response, false, null, -1);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetBankListFinished(response, false, null, -1);
				}
			}

		});
	}

	public void initBankEdit(final Map<String, Object> datas, final OnInitBankEditFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.BANK_EDIT, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						BankDetail bankDetail = BankDetailBuilder.build(jsonObject);
						listener.onInitBankEditFinished(response, bankDetail);
					} else {
						listener.onInitBankEditFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onInitBankEditFinished(response, null);
				}
			}

		});
	}

	/**
	 * 银行卡类别
	 * @param datas
	 * @param listener
     */
	public void initAuthCard(final Map<String, Object> datas, final OnInitAuthCardFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.AUTH_CARD, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						AuthCardDetail authCardDetail = AuthCardDetailBuilder.build(jsonObject);
						listener.onInitAuthCardFinished(response, authCardDetail);
					} else {
						listener.onInitAuthCardFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onInitAuthCardFinished(response, null);
				}
			}

		});
	}
	
	public void sendCode(final Map<String, Object> datas, final OnSendCodeFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.SEND_CODE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					JSONObject jsonObject = new JSONObject(response.getBody());
					String memberBankId = jsonObject.optString("memberBankId");
					String bankCardNo = jsonObject.optString("bankCardNo");
					String tel = jsonObject.optString("tel");
					listener.onSendCodeFinished(response, memberBankId, bankCardNo, tel);
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onSendCodeFinished(response, null, null, null);
				}
			}

		});
	}
	
	public void confirmBankCard(final Map<String, Object> datas, final OnConfirmBankCardFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CONFIRM_BANK_CARD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					JSONObject jsonObject = new JSONObject(response.getBody());
					int memberStep = jsonObject.optInt("memberStep");
					listener.onConfirmBankCardFinished(response, memberStep);
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onConfirmBankCardFinished(response, -1);
				}
			}

		});
	}

	public void saveBank(final Map<String, Object> datas, final OnSaveBankFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.BANK_SAVE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onSaveBankFinished(response);
			}

		});
	}
	
	public void getChargeRecord(final Map<String, Object> datas, final OnGetChargeRecordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_CHARGE_RECORD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Page page = PageBuilder.build(jsonObject);
						List<ChargeRecord> chargeRecordList = ChargeRecordBuilder.buildList(jsonObject.optJSONArray("items"));
						listener.onGetChargeRecordFinished(response, page, chargeRecordList);
					} else {
						listener.onGetChargeRecordFinished(response, null, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetChargeRecordFinished(response, null, null);
				}
			}

		});
	}
	
	public void getWithdrawRecord(final Map<String, Object> datas, final OnGetWithdrawRecordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_WITHDRAW_RECORD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Page page = PageBuilder.build(jsonObject);
						List<WithdrawRecord> withdrawRecordList = WithdrawRecordBuilder.buildList(jsonObject.optJSONArray("withdrawList"));
						listener.onGetWithdrawRecordFinished(response, page, withdrawRecordList);
					} else {
						listener.onGetWithdrawRecordFinished(response, null, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetWithdrawRecordFinished(response, null, null);
				}
			}

		});
	}

	public interface OnInitWithdrawFinishedListener {

		public void onInitWithdrawFinished(Response response, WithdrawDetail withdrawDetail);

	}

	public interface OnWithdrawFinishedListener {

		public void onWithdrawFinished(Response response, String getMoneyDate);

	}

	public interface OnGetBankListFinishedListener {

		public void onGetBankListFinished(Response response, boolean hasFastPayBank, List<MyBank> myBankList, int memberStep);

	}
	
	public interface OnInitBankEditFinishedListener {

		public void onInitBankEditFinished(Response response, BankDetail bankDetail);

	}
	
	public interface OnInitAuthCardFinishedListener {
		
		public void onInitAuthCardFinished(Response response, AuthCardDetail authCardDetail);
		
	}
	
	public interface OnSendCodeFinishedListener {
		
		public void onSendCodeFinished(Response response, String memberBankId, String bankCardNo, String tel);
		
	}
	
	public interface OnConfirmBankCardFinishedListener {
		
		public void onConfirmBankCardFinished(Response response, int memberStep);
		
	}

	public interface OnSaveBankFinishedListener {

		public void onSaveBankFinished(Response response);

	}
	
	public interface OnGetChargeRecordFinishedListener {

		public void onGetChargeRecordFinished(Response response, Page page, List<ChargeRecord> chargeRecordList);

	}
	
	public interface OnGetWithdrawRecordFinishedListener {

		public void onGetWithdrawRecordFinished(Response response, Page page, List<WithdrawRecord> withdrawRecordList);

	}

}