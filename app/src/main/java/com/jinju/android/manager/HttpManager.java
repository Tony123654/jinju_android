package com.jinju.android.manager;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jinju.android.R;
import com.jinju.android.base.DdApplication;
import com.jinju.android.builder.ResponseBuilder;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.interfaces.IManager;
import com.jinju.android.network.MultipartRequest;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by wangjw on 2016/8/23.
 */
public class HttpManager implements IManager {
	private RequestQueue mQueue;
	private DdApplication mApplication;
	private Map<String, Object> mMap;
	public HttpManager() {
		mApplication = DdApplication.getInstance();
	}

	@Override
	public void onInit() {
		mQueue = Volley.newRequestQueue(mApplication.getApplicationContext());
	}

	@Override
	public void onExit() {

	}

//	private Map<String,String> getParamsMap(Map<String, Object> datas) {
//		if (null == datas) {
//			return null;
//		}
//
//		Map<String, String> map = new HashMap<String, String>();
//		for (Map.Entry<String, Object> entry : datas.entrySet()) {
//			map.put(entry.getKey(), entry.getValue().toString());
//		}
//		return map;
//	}
	private Map<String,Object> getParamsMap(Map<String, Object> datas) {

		mMap = new HashMap<String, Object>();
		if (datas!=null) {
			for (Map.Entry<String, Object> entry : datas.entrySet()) {
				mMap.put(entry.getKey(), entry.getValue().toString());
			}
		}

		//因后台需求，添加一个全局参数
		String version = DdApplication.getVersionName();
		String imeiCode = DdApplication.getConfigManager().getPhoneIMEICode();
		mMap.put("av",version);//版本号
		mMap.put("dt","android");
		mMap.put("at","ddjrapp");//后台需求,用于区分app是否是旗舰版
		mMap.put("di",imeiCode);//设备唯一号

		return mMap;
	}

	public void sendRequest(final String apiType, final Map<String, Object> map, final int httpMethod, final OnRequestFinishedListener listener) {
		//因后台需求，添加一个全局参数
		Map<String, Object> datas = getParamsMap(map);
		mMap = null;
		if (datas!=null) {
			datas.put("signature",MD5Utils.getSignature(datas));
		}

		String url = AppConstant.APP_WEB_URL + apiType;

		String dealUrl = getUrl(url, datas, httpMethod);
		final String mRequestBody = appendParameter(url, datas, httpMethod);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(httpMethod, dealUrl, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				com.jinju.android.api.Response ddResponse = null;
				try {
					ddResponse = ResponseBuilder.build(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				listener.onRequestFinished(ddResponse);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				error.getMessage();
				com.jinju.android.api.Response ddResponse = AppUtils.obtainErrorResponse(R.string.network_connect_error);
				listener.onRequestFinished(ddResponse);
				Log.d("HttpManager", "volleyError = " + error.getMessage());
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				String token = DdApplication.getConfigManager().getToken();

				Log.d("HttpManager", "token = " + token);

				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Http-X-User-Access-Token", token);

				return headers;
			}

			@Override
			public String getBodyContentType() {
				return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
			}

			@Override
			public byte[] getBody() {
				try {
					return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
				} catch (UnsupportedEncodingException uee) {
					return null;
				}
			}
		};
		/**
		 * 	自己手动调用mQueue.add(request)以及mQueue.start()的次数始终为1，
		 *	但是与服务器联调时，服务器端的确被调用了两三次；谷歌后得知，这算是volley的一个bug吧（具体应该不算）
		 *  解决办法, 对Request设置重试策略时，更改默认超时时间设置稍大一些，默认请求重复次数设置为0.
		 *
		 */
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,//默认超时时间
				0,//默认最大重复次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mQueue.add(jsonObjectRequest);
	}

	private String appendParameter(String url, Map<String, Object> datas, int httpMethod) {
		if (httpMethod == Request.Method.POST && null != datas) {
			Uri uri = Uri.parse(url);
			Uri.Builder builder = uri.buildUpon();
			for (Map.Entry<String, Object> entry : datas.entrySet()) {
				builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());
			}
			return builder.build().getQuery();
		} else {
			return null;
		}
	}

	public String getUrl(String url, Map<String, Object> datas, int httpMethod) {
		String dealUrl = url;
		if (httpMethod == Request.Method.GET && null != datas) {
			StringBuilder stringBuilder = new StringBuilder(dealUrl);
			Iterator<Map.Entry<String, Object>> iterator = datas.entrySet().iterator();
			int i = 1;
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				if (i == 1) {
					stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue().toString());
				} else {
					stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue().toString());
				}
				iterator.remove();
				i++;
			}
			dealUrl = stringBuilder.toString();
		}
		return dealUrl;
	}

	public void uploadMultipart(String apiType, String filePartsName, File file, final OnRequestFinishedListener listener) {
		String url = apiType;
		MultipartRequest request = new MultipartRequest(url, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				com.jinju.android.api.Response ddResponse = AppUtils.obtainErrorResponse(R.string.network_connect_error);
				listener.onRequestFinished(ddResponse);
				Log.d("HttpManager", "volleyError = " + error.toString());
			}
		}, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				com.jinju.android.api.Response ddResponse = null;
				try {
					JSONObject jsonResult = new JSONObject(response);
					ddResponse = ResponseBuilder.build(jsonResult);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				listener.onRequestFinished(ddResponse);
			}
		}, filePartsName, file, null);
		mQueue.add(request);
	}



    public interface OnRequestFinishedListener {

		public void onRequestFinished(com.jinju.android.api.Response ddResponse);

	}
}
