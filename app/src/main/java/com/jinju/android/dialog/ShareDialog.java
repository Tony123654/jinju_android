package com.jinju.android.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jinju.android.R;
import com.jinju.android.api.JsShareInfo;
import com.jinju.android.share.ShareSDK;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.VersionUtils;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Libra on 2017/5/2.
 */

public class ShareDialog  {
    private  final int REQUEST_SHARE = 10;
    //权限列表
    private String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final Integer WECHAT = 1;
    private  final Integer FRIEND = 2;
    private final Integer QQ = 3;
    private  Dialog dialog;
    private  ShareSDK mShareSDK;


    public void showShareDialog(final Activity activity, final JsShareInfo mJsShareInfo) {

        init(activity,mJsShareInfo);
    }

    private void init(final Activity activity,final JsShareInfo mJsShareInfo) {

        mShareSDK = new ShareSDK(activity,mShareListener);

        List<Integer> buttons = mJsShareInfo.getButtons();
        View view = LayoutInflater.from(activity).inflate(
                R.layout.layout_share_dialog, null);

        dialog =  new Dialog(activity,R.style.custome_round_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        Window window =dialog.getWindow();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getHeight() * 0.4);

        dialog.getWindow().setAttributes(params);

        ImageView weChat = (ImageView) view.findViewById(R.id.iv_WeChat);
        ImageView friend = (ImageView) view.findViewById(R.id.iv_friend);
        ImageView qq = (ImageView) view.findViewById(R.id.iv_qq);
        if (buttons!=null) {
            if (!buttons.contains(WECHAT)) {
                weChat.setVisibility(View.GONE);
            } else {
                weChat.setVisibility(View.VISIBLE);
            }
            if (!buttons.contains(FRIEND)) {
                friend.setVisibility(View.GONE);
            } else {
                friend.setVisibility(View.VISIBLE);
            }
            if (!buttons.contains(QQ)) {
                qq.setVisibility(View.GONE);
            } else {
                qq.setVisibility(View.VISIBLE);
            }
        }

        weChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否安装微信
                if (VersionUtils.isWeixinAvilible()) {
                    //判断是否开启权限
                    if (EasyPermissions.hasPermissions(activity,mPermissionList)) {
                        //开始分享
                        mShareSDK.createWeixinShare(activity,mJsShareInfo);
                        dialog.dismiss();
                    } else {
                        //去请求权限
                        ActivityCompat.requestPermissions(activity,mPermissionList,REQUEST_SHARE);
                    }
                } else {
                    AppUtils.showToast(activity,"您尚未安装微信");
                    dialog.dismiss();
                }

            }
        });
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VersionUtils.isWeixinAvilible()) {

                    //android6.0适配，判断权限，打开微信QQ需要一些权限
                    if (EasyPermissions.hasPermissions(activity,mPermissionList)) {
                        //开始分享
                        mShareSDK.createWeixinCircle(activity,mJsShareInfo);
                        dialog.dismiss();
                    } else {
                        //去请求权限
                        ActivityCompat.requestPermissions(activity,mPermissionList,REQUEST_SHARE);
                    }

                } else {
                    AppUtils.showToast(activity,"您尚未安装微信");
                }

                dialog.dismiss();
            }
        });
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VersionUtils.isQQClientAvailable()) {
                    //android6.0适配，判断权限，打开微信QQ需要一些权限
                    if (EasyPermissions.hasPermissions(activity,mPermissionList)) {
                        //开始分享
                        mShareSDK.createQQShare(activity,mJsShareInfo);
                        dialog.dismiss();
                    } else {
                        //去请求权限
                        ActivityCompat.requestPermissions(activity,mPermissionList,REQUEST_SHARE);
                    }

                } else {
                    AppUtils.showToast(activity,"您尚未安装QQ");
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface ShareResultListener {
        void onShareStart();
        void onShareSuccess();
        void onShareError();
        void onShareCancel();
    }
    private ShareResultListener mShareResultListener;
    public void setShareListener(ShareResultListener shareResultListener) {
        if (shareResultListener!=null) {
            mShareResultListener = shareResultListener;
        }
    }
    private ShareSDK.ShareListener mShareListener = new ShareSDK.ShareListener() {
        @Override
        public void onStart() {
            mShareResultListener.onShareStart();
        }
        @Override
        public void onSuccess() {
            mShareResultListener.onShareSuccess();
        }
        @Override
        public void onError() {
            mShareResultListener.onShareError();
        }
        @Override
        public void onCancel() {
            mShareResultListener.onShareCancel();
        }
    };
}
