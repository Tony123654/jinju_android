package com.jinju.android.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jinju.android.R;
import com.jinju.android.base.DdApplication;
import com.jinju.android.util.PublicStaticClassUtils;
import com.jinju.android.widget.MyDetailScrollView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Libra on 2017/11/1.
 */

public class SafetyGuaranteeFragment extends BaseFragment {

    private View mView;
    private WebView webView;

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_safety_guarantee, container, false);
        initView();
        return mView;
    }

    private void initView() {
        webView = (WebView) mView.findViewById(R.id.web_view);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setDownloadListener(mDownloadListener);
        webView.setWebChromeClient(mWebchromeClient);
        webView.setWebViewClient(mWebViewClient);

        String token = DdApplication.getConfigManager().getToken();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Http-X-User-Access-Token", token);
        Log.d("WebActivity", token);

//      String url = getIntent().getStringExtra("url");
        webView.loadUrl("http://www.duoduojr.cn/apphtml/web/security/index.html", headers);
        MyDetailScrollView oneScrollView= (MyDetailScrollView) mView.findViewById(R.id.twoScrollview);
        oneScrollView.setScrollListener(new MyDetailScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if (scrollY == 0) {
                    PublicStaticClassUtils.IsTop = true;
                } else {
                    PublicStaticClassUtils.IsTop = false;
                }
            }

            @Override
            public void notBottom() {

            }

        });
    }
    WebChromeClient mWebchromeClient = new WebChromeClient(){
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//			txtTitle.setText(title == null ? "" : title);

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

        }

        @Override
        public void onPageFinished(WebView view, String url) {

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

}
