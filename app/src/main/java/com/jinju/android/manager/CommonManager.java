package com.jinju.android.manager;

import com.jinju.android.R;
import com.jinju.android.api.Advert;
import com.jinju.android.api.Area;
import com.jinju.android.api.CustomerCenter;
import com.jinju.android.api.HomeNotice;
import com.jinju.android.api.HomeNoticeBuilder;
import com.jinju.android.api.Index;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.api.ShareInfo;
import com.jinju.android.api.VersionInfo;
import com.jinju.android.base.DdApplication;
import com.jinju.android.builder.AdvertBuilder;
import com.jinju.android.builder.AreaBuilder;
import com.jinju.android.builder.CustomerCenterBuilder;
import com.jinju.android.builder.IndexBuilder;
import com.jinju.android.builder.PageBuilder;
import com.jinju.android.builder.ShareInfoBuilder;
import com.jinju.android.builder.VersionInfoBuilder;
import com.jinju.android.constant.ApiType;
import com.jinju.android.constant.HttpMethod;
import com.jinju.android.interfaces.IManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.manager.HttpManager.OnRequestFinishedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonManager implements IManager {

	private HttpManager mHttpManager;

	@Override
	public void onInit() {
		mHttpManager = DdApplication.getHttpManager();
	}

	@Override
	public void onExit() {

	}
	
	public void loadImage(final OnLoadImageFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.LOAD_IMAGE, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						JSONObject adObject = jsonObject.optJSONObject("ad");
						Advert advert = AdvertBuilder.build(adObject);
						listener.onLoadImageFinished(response, advert);
					} else {
						listener.onLoadImageFinished(response, null);
					}
				} catch (JSONException e) {
					listener.onLoadImageFinished(response, null);
				}
			}

		});
	}

	public void getIndex(final Map<String,Object> datas, final OnGetIndexFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_INDEX, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Index index = IndexBuilder.build(jsonObject);
						listener.onGetIndexFinished(response, index);
					} else {
						listener.onGetIndexFinished(response, null);
					}
				} catch (JSONException e) {
					listener.onGetIndexFinished(response, null);
				}
			}

		});
	}

	public void checkUpdate(final Map<String, Object> datas, final OnCheckUpdateFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.CHECK_UPDATE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {

				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						VersionInfo versionInfo = VersionInfoBuilder.build(jsonObject);
						listener.onCheckUpdateFinished(response, versionInfo);
					} else {
						listener.onCheckUpdateFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onCheckUpdateFinished(response, null);
				}
			}

		});
	}

	/**
	 * 获取分享信息
	 * @param datas
	 * @param listener
     */
	public void getShareInfo(final Map<String, Object> datas, final OnShareInfoFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_SHARE_INFO, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {

				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						ShareInfo shareInfo = ShareInfoBuilder.build(jsonObject);
						listener.onShareInfoFinished(response, shareInfo);
					} else {
						listener.onShareInfoFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onShareInfoFinished(response, null);
				}
			}

		});
	}
	public void getCustomerCenter(final OnCustomerCenterFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_CUSTOMER_CENTER, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {

				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						CustomerCenter customerCenter = CustomerCenterBuilder.build(jsonObject);
						listener.onCustomerCenterFinished(response, customerCenter);
					} else {
						listener.onCustomerCenterFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onCustomerCenterFinished(response, null);
				}
			}

		});
	}

	public void sendVerifyCode(final Map<String, Object> datas, final OnSendVerifyCodeFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.SEND_VERIFY_CODE, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onSendVerifyCodeFinished(response);
			}

		});
	}


	public void sendVerifyCodeLogined(final Map<String, Object> datas, final OnSendVerifyCodeLoginedFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.SEND_VERIFY_CODE_LOGINED, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onSendVerifyCodeLoginedFinished(response);
			}

		});
	}
	//上传图片的URL是动态的
	public void sendUploadPicture(String url,String filePartsName, File file, final onSendUploadPictureFinishedListener listener) {
		mHttpManager.uploadMultipart(url,filePartsName,file, new OnRequestFinishedListener() {
			@Override
			public void onRequestFinished(Response response) {
					if (response.isSuccess()) {
						listener.onSendUploadPictureFinished(response);
					}
			}

		});
	}

	public void getProvinceList(final OnGetProvinceListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_PROVINCE, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						List<Area> provinceList = AreaBuilder.buildList(jsonObject.optJSONArray("provinceList"));
						listener.onGetProvinceListFinished(response, provinceList);
					} else {
						listener.onGetProvinceListFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetProvinceListFinished(response, null);
				}
			}

		});
	}

	public void getCityList(final Map<String, Object> datas, final OnGetCityListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_CITY, datas, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						List<Area> cityList = AreaBuilder.buildList(jsonObject.optJSONArray("cityList"));
						listener.onGetCityListFinished(response, cityList);
					} else {
						listener.onGetCityListFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetCityListFinished(response, null);
				}
			}

		});
	}
	//上传头像
	public void uploadAvatar(String filePartsName, File file, final OnUploadAvatarFinishedListener listener) {
		mHttpManager.uploadMultipart(ApiType.UPLOAD_AVATAR, filePartsName, file, new OnRequestFinishedListener() {
			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						String imgUrl = jsonObject.optString("headImg");
						listener.onUploadAvatarFinished(response, imgUrl);
					} else {
						listener.onUploadAvatarFinished(response, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onUploadAvatarFinished(response, null);
				}
			}
		});
	}
	//记录regId
	public void recordRegId (final Map<String, Object> datas,final OnRecordRegIdListener listener) {
		mHttpManager.sendRequest(ApiType.RECORD_REGID, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				listener.onRecordRegIdFinished(response);
			}

		});
	}
	public void getHomeNoticeList(final Map<String, Object> datas,final OnGetHomeNoticeListFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.HOME_NOTICE_LIST, datas, HttpMethod.POST, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {

				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						Page page = PageBuilder.build(jsonObject);
						List<HomeNotice> homeNoticeList = HomeNoticeBuilder.buildList(jsonObject.optJSONArray("noticeList"));
						listener.onGetHomeNoticeListFinished(response,page,homeNoticeList);
					} else {
						listener.onGetHomeNoticeListFinished(response,null, null);
					}
				} catch (JSONException e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onGetHomeNoticeListFinished(response,null, null);
				}
			}

		});
	}
	public void getRefreshInfo(final OnRefreshInfoFinishedListener listener) {
		mHttpManager.sendRequest(ApiType.GET_REFRESH_INFO, null, HttpMethod.GET, new OnRequestFinishedListener() {

			@Override
			public void onRequestFinished(Response response) {
				try {
					if (response.isSuccess()) {
						JSONObject jsonObject = new JSONObject(response.getBody());
						JSONArray jsonArray = jsonObject.optJSONArray("lodingTips");
						int bottomType = jsonObject.optInt("isNewYear");
						List<String> refreshInfoList = new ArrayList<String> ();
						for (int i =0;i< jsonArray.length();i++) {
							String refreshInfo = jsonArray.get(i).toString();
							refreshInfoList.add(refreshInfo);
						}
                        listener.onRefreshInfoFinished(response,refreshInfoList,bottomType);
                    } else {
                        listener.onRefreshInfoFinished(response,null,0);
                    }
				} catch (Exception e) {
					response = AppUtils.obtainErrorResponse(R.string.json_parse_error);
					listener.onRefreshInfoFinished(response,null,0);
				}

			}

		});
	}
	public interface  OnGetHomeNoticeListFinishedListener {
		public void onGetHomeNoticeListFinished(Response response,Page page,List<HomeNotice> lists);
	}
	public interface OnRecordRegIdListener {
		public void onRecordRegIdFinished(Response response);
	}

	public interface OnLoadImageFinishedListener {
		
		public void onLoadImageFinished(Response response, Advert advert);
		
	}
	
	public interface OnGetIndexFinishedListener {

		public void onGetIndexFinished(Response response, Index index);

	}
	
	public interface OnCheckUpdateFinishedListener {

		public void onCheckUpdateFinished(Response response, VersionInfo versionInfo);

	}
	public interface OnShareInfoFinishedListener {

		public void onShareInfoFinished(Response response, ShareInfo shareInfo);

	}
	public interface OnCustomerCenterFinishedListener {

		public void onCustomerCenterFinished(Response response, CustomerCenter customerCenter);

	}

	public interface OnSendVerifyCodeFinishedListener {

		public void onSendVerifyCodeFinished(Response response);

	}


	public interface OnSendVerifyCodeLoginedFinishedListener {

		public void onSendVerifyCodeLoginedFinished(Response response);

	}

	public interface OnGetProvinceListFinishedListener {

		public void onGetProvinceListFinished(Response response, List<Area> provinceList);

	}

	public interface OnGetCityListFinishedListener {

		public void onGetCityListFinished(Response response, List<Area> cityList);

	}

	public interface OnUploadAvatarFinishedListener {

		public void onUploadAvatarFinished(Response response, String imgUrl);

	}
	public interface onSendUploadPictureFinishedListener {

		public void onSendUploadPictureFinished(Response response);

	}
	public interface OnRefreshInfoFinishedListener {

		public void onRefreshInfoFinished(Response response, List<String> refreshInfoList,int bottomType);

	}

}