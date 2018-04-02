package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.base.DdApplication;
import com.jinju.android.util.AppUtils;

import java.util.HashMap;
import java.util.Map;

public class WebActivity extends BaseActivity {
	private Dialog mProgressDialog;
	private WebView webView;
	private TextView txtTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
//		Intent intent = getIntent();
//		String title = intent.getStringExtra("title");

		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);

		txtTitle = (TextView) findViewById(R.id.txt_title);
		

		mProgressDialog = AppUtils.createLoadingDialog(this);
		
		webView = (WebView) findViewById(R.id.web_view);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setDownloadListener(mDownloadListener);
		webView.setWebChromeClient(mWebchromeClient);
		webView.setWebViewClient(mWebViewClient);

		String token = DdApplication.getConfigManager().getToken();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Http-X-User-Access-Token", token);
		Log.d("WebActivity", token);
		
		String url = getIntent().getStringExtra("url");
		webView.loadUrl(url, headers);
	}

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}

	};
	
	/*private String dealUrl(String url) {
		String dc = AppUtils.getAppDc(this);
		String av = AppUtils.getAppAv();
		String dt = AppUtils.getAppDt();
		String di = AppUtils.getAppDi(this);
		String fv = AppUtils.getAppFv();
		
		String dealUrl = url;
		
		if(url.indexOf("?") != -1) {
			dealUrl = dealUrl + "&dc=" + dc + "&av=" + av + "&dt=" + dt + "&di=" + di + "&fv=" + fv;
		} else {
			dealUrl = dealUrl + "?dc=" + dc + "&av=" + av + "&dt=" + dt + "&di=" + di + "&fv=" + fv;
		}
		
		return dealUrl;
	}*/

	WebChromeClient mWebchromeClient = new WebChromeClient(){
		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
//			txtTitle.setText(title == null ? "" : title);
			if (title.contains("www")) {
				txtTitle.setText("");
			} else {
				txtTitle.setText(title == null ? "" : title);

			}
		}
	};

	private WebViewClient mWebViewClient = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mProgressDialog.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {


			mProgressDialog.dismiss();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

		}

	};



	private DownloadListener mDownloadListener = new DownloadListener() {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		}

	};
	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			super.onBackPressed();
		}
	}
}