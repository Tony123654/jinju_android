package com.jinju.android.manager;

import com.jinju.android.R;
import com.jinju.android.api.About;
import com.jinju.android.api.Account;
import com.jinju.android.api.AccountLogInfo;
import com.jinju.android.api.AutomateRule;
import com.jinju.android.api.FastPay;
import com.jinju.android.api.Function;
import com.jinju.android.api.Gift;
import com.jinju.android.api.Info;
import com.jinju.android.api.MyAsset;
import com.jinju.android.api.Notice;
import com.jinju.android.api.Page;
import com.jinju.android.api.PerLogin;
import com.jinju.android.api.Response;
import com.jinju.android.api.Setting;
import com.jinju.android.api.VerifyName;
import com.jinju.android.base.DdApplication;
import com.jinju.android.builder.AboutBuilder;
import com.jinju.android.builder.AccountBuilder;
import com.jinju.android.builder.AccountLogInfoBuilder;
import com.jinju.android.builder.AutomateRuleBuilder;
import com.jinju.android.builder.FastPayBuilder;
import com.jinju.android.builder.FunctionBuilder;
import com.jinju.android.builder.GiftBuilder;
import com.jinju.android.builder.InfoBuilder;
import com.jinju.android.builder.MyAssetBuilder;
import com.jinju.android.builder.MyMessageBuilder;
import com.jinju.android.builder.PageBuilder;
import com.jinju.android.builder.SettingBuilder;
import com.jinju.android.builder.VerifyNameBuilder;
import com.jinju.android.constant.ApiType;
import com.jinju.android.constant.AuthStatus;
import com.jinju.android.constant.HttpMethod;
import com.jinju.android.interfaces.IManager;
import com.jinju.android.manager.HttpManager.OnRequestFinishedListener;
import com.jinju.android.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class UserManager implements IManager {

	private HttpManager mHttpManager;
	private ConfigManager mConfigManager;

	@Override
	public void onInit() {
		mHttpManager = DdApplication.getHttpManager();
		mConfigManager = DdApplication.getConfigManager();
	}

	@Override
	public void onExit() {

	}

	public void login(final Map<String, Object> datas, final OnLoginFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.LOGIN, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());

						String token = jsonObject.optString("token");
						String authStatus = jsonObject.optString("authStatus");
						int memberStep = jsonObject.optInt("memberStep");
						String memberId = jsonObject.optString("memberId");
						String nick = jsonObject.optString("nick");
						jsonObject.optString("");
						mConfigManager.setToken(token);
						mConfigManager.setLogined(true);
						mConfigManager.setAuthStatus(authStatus);
						mConfigManager.setMemberId(memberId);
						mConfigManager.setNickName(nick);
						listener.onLoginFinished(response, memberStep);
					} else {
						listener.onLoginFinished(response, -1);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onLoginFinished(response, -1);
				}
			}

		});
	}

	public void checkMobileAvailable(final Map<String, Object> datas, final OnCheckMobileAvailableFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CHECK_MOBILE_AVAILABLE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onCheckMobileAvailableFinished(response);
			}
		});	
	}

	public void checkMobileLoginOrRegister(final Map<String, Object> datas, final OnCheckMobileLoginOrRegisterFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CHECK_MOBILE_LOGIN_OR_REGISTER, datas, HttpMethod.GET, new OnRequestFinishedListener() {
			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String mobile = jsonObject.optString("mobile");
						String showName = jsonObject.optString("showName");
						String type = jsonObject.optString("type");
						PerLogin perLogin = new PerLogin();
						perLogin.setMobile(mobile);
						perLogin.setShowName(showName);
						perLogin.setType(type);

						listener.onCheckMobileLoginOrRegisterFinished(response, perLogin);
					} else {
						listener.onCheckMobileLoginOrRegisterFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onCheckMobileLoginOrRegisterFinished(response, null);
				}
			}
		});
	}

	public void register(final Map<String, Object> datas, final OnRegisterFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.REGISTER, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String token = jsonObject.optString("token");
						String memberId = jsonObject.optString("memberId");
						mConfigManager.setToken(token);
						mConfigManager.setMemberId(memberId);
						mConfigManager.setLogined(true);
						mConfigManager.setAuthStatus(AuthStatus.NONE);
						List<Function> functionList = FunctionBuilder.buildList(jsonObject.optJSONArray("functions"));
						listener.onRegisterFinished(response, functionList);
					} else {
						listener.onRegisterFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onRegisterFinished(response, null);
				}
			}

		});
	}

	public void logout(final OnLogoutFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.LOGOUT, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				if (response.isSuccess()) {
					mConfigManager.setToken("");
					mConfigManager.setLogined(false);
					mConfigManager.setAuthStatus(AuthStatus.NONE);
					mConfigManager.setMemberId("");
					mConfigManager.setNickName("");
					mConfigManager.setPhone("");
					listener.onLogoutFinished(response);
				} else {
					listener.onLogoutFinished(response);
				}
			}

		});
	}

	public void getAccount(final Map<String, Object> datas,final OnGetAccountFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_ACCOUNT, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Account account = AccountBuilder.build(jsonObject);
						listener.onGetAccountFinished(response, account);
					} else {
						listener.onGetAccountFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetAccountFinished(response, null);
				}
			}

		});
	}

	public void getSetting(final Map<String, Object> datas, final OnGetSettingFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_SETTING, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Setting setting = SettingBuilder.build(jsonObject);
						listener.onGetSettingFinished(response, setting);
					} else {
						listener.onGetSettingFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetSettingFinished(response, null);
				}
			}

		});
	}

	public void getInfo(final OnGetInfoFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_INFO, null, HttpMethod.GET, new OnRequestFinishedListener() {
			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Info info = InfoBuilder.build(jsonObject);
						listener.onGetInfoFinished(response, info);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetInfoFinished(response, null);
				}
			}
		});
	}

	public void getMyAsset(final Map<String, Object> datas,final OnGetMyAssetFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_MY_ASSET, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						MyAsset myAsset = MyAssetBuilder.build(jsonObject);
						listener.onGetMyAssetFinished(response, myAsset);
					} else {
						listener.onGetMyAssetFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetMyAssetFinished(response, null);
				}
			}

		});
	}

	public void getFundAccountLogList(final Map<String, Object> datas, final OnGetFundAccountLogListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_FUND_ACCOUNT_LOG, datas, HttpMethod.GET, new OnRequestFinishedListener() {


			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						AccountLogInfo accountLogInfo = AccountLogInfoBuilder.build(jsonObject);
						listener.onGetFundAccountLogListFinished(response, accountLogInfo);
					} else {
						listener.onGetFundAccountLogListFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetFundAccountLogListFinished(response, null);
				}
			}

		});
	}

	public void getMyCouponList(final Map<String, Object> datas, final OnGetMyCouponListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_MY_COUPON_LIST, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Page page = PageBuilder.build(jsonObject);
						String couponRule = jsonObject.optString("couponRule");
						List<Gift> giftList = GiftBuilder.buildList(jsonObject.optJSONArray("giftList"));
						listener.onGetMyCouponListFinished(response, page, giftList,couponRule);
					} else {
						listener.onGetMyCouponListFinished(response, null, null,null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetMyCouponListFinished(response, null, null,null);
				}
			}

		});
	}

	public void couponCharge(final Map<String, Object> datas, final OnCouponChargeFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.COUPON_CHARGE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onCouponChargeFinished(response);
			}

		});
	}

	public void setNickname(final Map<String, Object> datas, final OnSetNicknameFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.SET_NICKNAME, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String token = jsonObject.optString("token");
						mConfigManager.setToken(token);
						mConfigManager.setLogined(true);
						listener.onSetNicknameFinished(response);
					} else {
						listener.onSetNicknameFinished(response);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onSetNicknameFinished(response);
				}
			}

		});
	}

	public void changeVaildate(final Map<String, Object> datas, final OnChangeVaildateFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CHANGE_VALIDATE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String oldCode = jsonObject.optString("oldCode");
						String oldCodeSign = jsonObject.optString("oldCodeSign");
						listener.onChangeVaildateFinished(response, oldCode, oldCodeSign);
					} else {
						listener.onChangeVaildateFinished(response, null, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onChangeVaildateFinished(response, null, null);
				}
			}

		});
	}
	public void changeMobile(final Map<String, Object> datas, final OnChangeMobileFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CHANGE_MOBILE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String token = jsonObject.optString("token");
						mConfigManager.setToken(token);
						mConfigManager.setLogined(true);
						listener.onChangeMobileFinished(response);
					} else {
						listener.onChangeMobileFinished(response);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onChangeMobileFinished(response);
				}
			}

		});
	}
	//验证手势短信验证码
	public void verifyGestureCode(final Map<String, Object> datas,final OnVerifyGestureCodeFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.VERIFY_GESTURE_CODE, datas, HttpMethod.POST, new OnRequestFinishedListener() {
			@Override
			public void onRequestFinished(Response response) {
				listener.OnVerifyGestureCodeFinished(response);
			}
		});
	}
	public void changePassword(final Map<String, Object> datas, final OnChangePasswordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CHANGE_PASSWORD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onChangePasswordFinished(response);
			}

		});
	}

	public void resetPassword(final Map<String, Object> datas, final OnResetPasswordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.RESET_PASSWORD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onResetPasswordFinished(response);
			}

		});
	}
	
	public void validateOldWithdrawPassword(final Map<String, Object> datas, final OnValidateOldWithdrawPasswordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.VALIDATE_OLD_WITHDRAW_PASSWORD, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onValidateOldWithdrawPasswordFinished(response);
			}

		});		
	}

	public void editWithdrawPassword(final Map<String, Object> datas, final OnEditWithdrawPasswordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.EDIT_WITHDRAW_PASSWORD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onEditWithdrawPasswordFinished(response);
			}

		});
	}

	public void forgetWithdrawPassword(final Map<String, Object> datas, final OnForgetWithdrawPasswordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.FORGET_WITHDRAW_PASSWORD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String idCard = jsonObject.optString("idCard");
						listener.onForgetWithdrawPasswordFinished(response, idCard);
					} else {
						listener.onForgetWithdrawPasswordFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onForgetWithdrawPasswordFinished(response, null);
				}
			}

		});
	}

	public void resetWithdrawPassword(final Map<String, Object> datas, final OnResetWithdrawPasswordFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.RESET_WITHDRAW_PASSWORD, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onResetWithdrawPasswordFinished(response);
			}

		});
	}
	
	public void validateFindTransPwd(final Map<String, Object> datas, final OnValidateFindTransPwdFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.VALIDATE_FIND_TRANS_PWD, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.OnValidateFindTransPwdFinished(response);
			}

		});
	}

	public void getAutomateRule(final Map<String, Object> datas, final OnGetAutomateRuleFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.AUTOMATE_RULE, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						AutomateRule automateRule = AutomateRuleBuilder.build(jsonObject);
						listener.onGetAutomateRuleFinished(response, automateRule);
					} else {
						listener.onGetAutomateRuleFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetAutomateRuleFinished(response, null);
				}
			}

		});
	}

	public void setAutomateRule(final Map<String, Object> datas, final OnSetAutomateRuleFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.AUTOMATE_RULE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onSetAutomateRuleFinished(response);
			}

		});
	}

	public void initEdit(final OnInitEditFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.INIT_EDIT, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						VerifyName verifyName = VerifyNameBuilder.build(jsonObject);
						listener.onInitEditFinished(response, verifyName);
					} else {
						listener.onInitEditFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onInitEditFinished(response, null);
				}
			}

		});
	}

	public void saveAuth(final Map<String, Object> datas, final OnSaveAuthFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.SAVE_AUTH, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String token = jsonObject.optString("token");
						mConfigManager.setToken(token);
						mConfigManager.setLogined(true);
						mConfigManager.setAuthStatus(AuthStatus.AUTH);
						List<Function> functionList = FunctionBuilder.buildList(jsonObject.optJSONArray("functions"));
						listener.onSaveAuthFinished(response, functionList);
					} else {
						listener.onSaveAuthFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onSaveAuthFinished(response, null);
				}
			}

		});
	}
	
	public void initFastPay(final Map<String, Object> datas, final OnInitFastPayFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.INIT_FAST_PAY, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						FastPay fastPay = FastPayBuilder.build(jsonObject);
						listener.OnInitFastPayFinished(response, fastPay);
					} else {
						listener.OnInitFastPayFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.OnInitFastPayFinished(response, null);
				}
			}
		});	
	}
	
	public void fastPay(final Map<String, Object> datas, final OnFastPayFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.FAST_PAY, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.OnFastPayFinished(response);
			}
		});	
	}
	public void submitFeedback(final Map<String, Object> datas,final OnSubmitFeedbackFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.SUBMIT_FEEDBACK, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.OnSubmitFeedbackFinished(response);
			}
		});
	}

	public void getAbout(final OnGetAboutFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_ABOUT, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						List<About> aboutList = AboutBuilder.buildList(jsonObject.optJSONArray("aboutUsButtons"));
						listener.OnGetAboutFinished(response, aboutList);
					} else {
						listener.OnGetAboutFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.OnGetAboutFinished(response, null);
				}
			}
		});	
	}

	/**
	 * 我的消息
	 */
	public void getMyMessage(final Map<String, Object> datas,final OnMyMessageFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.MY_MESSAGE,datas,HttpMethod.POST,new OnRequestFinishedListener() {
			@Override
			public void onRequestFinished(Response response) {

				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Page page = PageBuilder.build(jsonObject);
						List<Notice> noticeList = MyMessageBuilder.buildList(jsonObject.optJSONArray("msgList"));
						listener.OnMyMessageFinished(response,page,noticeList);
					} else {
						listener.OnMyMessageFinished(response,null, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.OnMyMessageFinished(response,null, null);
				}
			}
		});
	}

	public interface OnLoginFinishedListener {



		public void onLoginFinished(Response response, int memberStep);

	}
	
	public interface OnCheckMobileAvailableFinishedListener {
		public void onCheckMobileAvailableFinished(Response response);
	}

	public interface OnCheckMobileLoginOrRegisterFinishedListener {
		public void onCheckMobileLoginOrRegisterFinished(Response response, PerLogin perLogin);

	}
	public interface OnRegisterFinishedListener {

		public void onRegisterFinished(Response response, List<Function> functionList);

	}

	public interface OnLogoutFinishedListener {

		public void onLogoutFinished(Response response);

	}

	public interface OnGetAccountFinishedListener {

		public void onGetAccountFinished(Response response, Account account);

	}

	public interface OnGetSettingFinishedListener {

		public void onGetSettingFinished(Response response, Setting setting);

	}
	public interface OnGetInfoFinishedListener {

		public void onGetInfoFinished(Response response, Info info);

	}
	public interface OnGetMyAssetFinishedListener {

		public void onGetMyAssetFinished(Response response, MyAsset myAsset);

	}

	public interface OnGetFundAccountLogListFinishedListener {

		public void onGetFundAccountLogListFinished(Response response, AccountLogInfo accountLogInfo);

	}

	public interface OnGetMyCouponListFinishedListener {

		public void onGetMyCouponListFinished(Response response, Page page, List<Gift> giftList,String couponRule);

	}

	public interface OnCouponChargeFinishedListener {

		public void onCouponChargeFinished(Response response);

	}

	public interface OnSetNicknameFinishedListener {

		public void onSetNicknameFinished(Response response);

	}

	public interface OnChangeVaildateFinishedListener {

		public void onChangeVaildateFinished(Response response, String oldCode, String oldCodeSign);

	}

	public interface OnChangeMobileFinishedListener {

		public void onChangeMobileFinished(Response response);

	}

	public interface OnChangePasswordFinishedListener {

		public void onChangePasswordFinished(Response response);

	}

	public interface OnResetPasswordFinishedListener {

		public void onResetPasswordFinished(Response response);

	}
	
	public interface OnValidateOldWithdrawPasswordFinishedListener {
		
		public void onValidateOldWithdrawPasswordFinished(Response response);
		
	}

	public interface OnEditWithdrawPasswordFinishedListener {

		public void onEditWithdrawPasswordFinished(Response response);

	}

	public interface OnForgetWithdrawPasswordFinishedListener {

		public void onForgetWithdrawPasswordFinished(Response response, String idCard);

	}

	public interface OnResetWithdrawPasswordFinishedListener {

		public void onResetWithdrawPasswordFinished(Response response);

	}
	
	public interface OnValidateFindTransPwdFinishedListener {
		
		public void OnValidateFindTransPwdFinished(Response response);
		
	}

	public interface OnGetAutomateRuleFinishedListener {

		public void onGetAutomateRuleFinished(Response response, AutomateRule automateRule);

	}

	public interface OnSetAutomateRuleFinishedListener {

		public void onSetAutomateRuleFinished(Response response);

	}

	public interface OnInitEditFinishedListener {

		public void onInitEditFinished(Response response, VerifyName verifyName);

	}

	public interface OnSaveAuthFinishedListener {

		public void onSaveAuthFinished(Response response, List<Function> functionList);

	}
	
	public interface OnInitFastPayFinishedListener {
		
		public void OnInitFastPayFinished(Response response, FastPay fastPay);
	}
	
	public interface OnFastPayFinishedListener {
		
		public void OnFastPayFinished(Response response);
	}
	
	public interface OnGetAboutFinishedListener {
		
		public void OnGetAboutFinished(Response response,List<About> aboutList);
		
	}
	public interface OnMyMessageFinishedListener {

		public void OnMyMessageFinished(Response response,Page page,List<Notice> noticeList);

	}
	public interface  OnVerifyGestureCodeFinishedListener {
		public void OnVerifyGestureCodeFinished(Response response);
	}
	public interface  OnSubmitFeedbackFinishedListener {
		public void OnSubmitFeedbackFinished(Response response);
	}


}