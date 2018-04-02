package com.jinju.android.webview;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.TextView;

import com.jinju.android.util.DDJRCmdUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Libra on 2017/4/27.
 */

public class MyWebViewClient extends BridgeWebViewClient {
    private Activity mContext;
    private TextView mTxtTitle;
    public MyWebViewClient(BridgeWebView webView, Activity context, TextView txtTitle) {
        super(webView);
        mContext = context;
        mTxtTitle = txtTitle;
    }
    public MyWebViewClient(BridgeWebView webView, FragmentActivity context, TextView txtTitle) {
        super(webView);
        mContext = context;
        mTxtTitle = txtTitle;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //在当前的webview中跳转到新的界面
        if (url.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {

            //点击webview上的控件，执行指定跳转

            DDJRCmdUtils.handleProtocol(mContext,url);
            return true;
        }
        //拨打链接电话
        if (url.contains("tel:")) {
            linkCallPhone(url);
            return true;
        }
        return super.shouldOverrideUrlLoading(view,url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mTxtTitle!=null) {
            mTxtTitle.setText(view.getTitle());
        }

    }

    private void linkCallPhone(String url) {
        String mobile = url.replace("-","");
        Uri data= Uri.parse(mobile);
        Intent intent =  new Intent(Intent.ACTION_CALL);
        intent.setData(data);
        //6.0获取电话权限
        if(EasyPermissions.hasPermissions(mContext,Manifest.permission.CALL_PHONE)) {
            mContext.startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);

        }
    }


}
