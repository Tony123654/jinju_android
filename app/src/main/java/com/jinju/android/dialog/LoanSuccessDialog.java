package com.jinju.android.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.PayResult;
import com.jinju.android.api.Response;
import com.jinju.android.api.ShareInfo;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.ShareType;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.share.CommonShareSDK;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.TextShowUtils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Libra on 2017/12/19.
 */

public class LoanSuccessDialog implements View.OnClickListener{
    private Dialog dialog;
    private Context mContext;
    private View view;
    private PayResult mPayResult;
    private ShareInfo mShareInfo;
    //权限列表
    private static String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> list;
    private Bitmap mBitmap;
    public LoanSuccessDialog(Context context,PayResult payResult) {
        mContext = context;
        mPayResult = payResult;

        init();
    }
    private void init() {

        view = LayoutInflater.from(mContext).inflate(
                R.layout.layout_loan_success_dialog, null);
        dialog =  new Dialog(mContext,R.style.custome_round_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window =dialog.getWindow();
//        Drawable drawable =new BitmapDrawable(mBitmap);
//        Drawable drawable = new BitmapDrawable(mContext.getResources(),mBitmap);
//        window.setBackgroundDrawable(drawable);
        WindowManager windowManager = ((Activity)mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        params.width = (int)(display.getWidth()*0.8);
        window.setAttributes(params);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);

        initView();
    }

    private void initView() {

        list = mPayResult.getActivityDesc();
        TextView  mTxtLoanSuccessDesc1 = (TextView) view.findViewById(R.id.txt_loan_success_desc_1);
        TextView  mTxtLoanSuccessDesc2 = (TextView) view.findViewById(R.id.txt_loan_success_desc_2);

        if (list!=null&&list.size()>0){
            mTxtLoanSuccessDesc1.setText(list.get(0));
        }
        if (list!=null&&list.size()>1){
            mTxtLoanSuccessDesc2.setText(Html.fromHtml(TextShowUtils.colorHtmlText(list.get(1))));
        }

        ImageView  mImgCancel = (ImageView) view.findViewById(R.id.img_cancel);
        TextView  mLayoutActivityDetail = (TextView) view.findViewById(R.id.layout_activity_detail);
        RelativeLayout  mLayoutWeixin = (RelativeLayout) view.findViewById(R.id.layout_weixin);
        RelativeLayout  mLayoutFriend = (RelativeLayout) view.findViewById(R.id.layout_friend);
        RelativeLayout  mLayoutQQ = (RelativeLayout) view.findViewById(R.id.layout_qq);


        mImgCancel.setOnClickListener(this);
        mLayoutActivityDetail.setOnClickListener(this);
        mLayoutWeixin.setOnClickListener(this);
        mLayoutFriend.setOnClickListener(this);
        mLayoutQQ.setOnClickListener(this);

        getShareInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                dismiss();
                break;
            case R.id.layout_activity_detail:
                Intent intent = new Intent(mContext,BaseJsBridgeWebViewActivity.class);
                intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mPayResult.getActivityUrl());
                mContext.startActivity(intent);
                break;
            case R.id.layout_weixin:
                //判断是否安装微信
                if (VersionUtils.isWeixinAvilible()) {
                    //判断是否开启权限
                    if (EasyPermissions.hasPermissions(mContext,mPermissionList)) {
                        //开始分享
                        CommonShareSDK.createWeixinShare(mContext,mShareInfo);
                        dismiss();
                    } else {
                        //去请求权限
                        ActivityCompat.requestPermissions((Activity)mContext,mPermissionList,0);
                    }
                } else {
                    AppUtils.showToast(mContext,"您尚未安装微信");
                    dismiss();
                }
                break;
            case R.id.layout_friend:
                //判断是否安装微信
                if (VersionUtils.isWeixinAvilible()) {
                    //判断是否开启权限
                    if (EasyPermissions.hasPermissions(mContext,mPermissionList)) {
                        //开始分享
                        CommonShareSDK.createWeixinCircle(mContext,mShareInfo);
                        dismiss();
                    } else {
                        //去请求权限
                        ActivityCompat.requestPermissions((Activity)mContext,mPermissionList,0);
                    }
                } else {
                    AppUtils.showToast(mContext,"您尚未安装微信");
                    dismiss();
                }
                break;
            case R.id.layout_qq:
                //判断是否安装微信
                if (VersionUtils.isQQClientAvailable()) {
                    //判断是否开启权限
                    if (EasyPermissions.hasPermissions(mContext,mPermissionList))  {
                        //开始分享
                        CommonShareSDK.createQQShare(mContext,mShareInfo);
                        dismiss();
                    } else {
                        //去请求权限
                        ActivityCompat.requestPermissions((Activity)mContext,mPermissionList,0);
                    }
                } else {
                    AppUtils.showToast(mContext,"您尚未安装QQ");
                    dismiss();
                }
                break;
            default:
                break;

        }
    }
    public void show() {
        dialog.show();
    }
    public void dismiss() {
        dialog.dismiss();
    }

    private void getShareInfo() {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("iv", DdApplication.getVersionName());
        datas.put("infoType", ShareType.REGISTER);
        DdApplication.getCommonManager().getShareInfo(datas,mOnShareInfoFinishedListener);
    }
    private CommonManager.OnShareInfoFinishedListener mOnShareInfoFinishedListener = new CommonManager.OnShareInfoFinishedListener() {
        @Override
        public void onShareInfoFinished(Response response, ShareInfo shareInfo) {
            if (response.isSuccess()) {
                mShareInfo =  shareInfo;
            }
        }
    };
}
