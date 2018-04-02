package com.jinju.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jinju.android.R;
import com.jinju.android.api.PopAd;
import com.jinju.android.util.DDJRCmdUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.widget.SegmentBar;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Libra on 2017/5/23.
 */

public class ActionDialog {
    private  Dialog dialog;
    private  View view;
    private  Activity mActivity;
    private  ImageView ivCancel;
    private  ImageView ivActionDialog;
    private  Display display;
    private  String mLinkType;
    private  String mLinkUrl;
    private  SegmentBar mSegmentBar;
    private  List<PopAd> mPopAdList;


    public ActionDialog(Activity activity, SegmentBar segmentBar,List<PopAd> popAdList) {
        mActivity = activity;
        mSegmentBar = segmentBar;
        mPopAdList = popAdList;
        initDialog();
    }
    public void initDialog() {

        view = LayoutInflater.from(mActivity).inflate(
                R.layout.layout_home_action_dialog, null);
        dialog =  new Dialog(mActivity,R.style.custome_round_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        Window window =dialog.getWindow();

        WindowManager windowManager = mActivity.getWindowManager();
        display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();

        dialog.getWindow().setAttributes(params);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);

        initClick();
    }

    private void initClick() {
        ivCancel = (ImageView) view.findViewById(R.id.iv_cancel);
        ivActionDialog = (ImageView) view.findViewById(R.id.iv_action_dialog);
        ivActionDialog.setMaxHeight((int) (display.getHeight() * 0.65));//最大高度
        ivActionDialog.setMaxWidth((int) (display.getWidth() * 0.8));

        String mImgUrl = mPopAdList.get(0).getImgUrl();
        ImageUtils.displayImage(ivActionDialog, mImgUrl,mImageLoadingListener);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mLinkUrl = mPopAdList.get(0).getLinkUrl();
        mLinkType = mPopAdList.get(0).getType();
        ivActionDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // -1 打开新界面 ， 0 不做跳转， 2 理财列表
                if (mLinkType.equals("0")){
                    dialog.dismiss();
                }
                if (mLinkType.equals("-1")) {
                    if (!TextUtils.isEmpty(mLinkUrl)) {
                        if (mLinkUrl.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                            //点击webview上的控件，执行指定跳转
                            DDJRCmdUtils.handleProtocol(mActivity, mLinkUrl);
                            dialog.dismiss();
                        } else {
                            Intent intent = new Intent(mActivity, BaseJsBridgeWebViewActivity.class);
                            intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH, mLinkUrl);
                            mActivity.startActivity(intent);
                            dialog.dismiss();
                        }

                    }
                }
                if (mLinkType.equals("2")) {
                    //跳转到理财列表
                    mSegmentBar.setCurrentTab(1);
                    dialog.dismiss();
                }
            }
        });
    }
    private  ImageLoadingListener mImageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {
            ivCancel.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            ivCancel.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    };
    public void showDialog() {
        dialog.show();
    }

}
