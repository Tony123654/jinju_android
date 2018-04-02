package com.jinju.android.dialog;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.interfaces.DownloadFinishListener;
import com.jinju.android.service.DownloadService;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.NetworkUtil;
import com.jinju.android.util.VersionUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Libra on 2017/5/11.
 *
 *  发现新版本dialog
 */

public class UpdateDialog implements DownloadFinishListener {

    private static final int PERMISSION_PHONE_STORAGE = 0;

    private Activity mActivity;
    private Dialog dialog;
    private View view;
    private String forceUpdate;//是否强制更新
    private Button btnTwoConfirm;
    private Button btnOneConfirm;
    private String fileSize;

    public UpdateDialog(Activity activity) {
        mActivity = activity;
    }
    public void initUpdateDialog(final String apkUrl, final String version, final String text, String forceUpdate,String fileSize) {

        this.forceUpdate = forceUpdate;
        this.fileSize = fileSize;
        dialog =  new Dialog(mActivity,R.style.custome_round_dialog);
        //强制更新
        if (forceUpdate.equals("Y")) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.layout_update_dialog_onebtn, null);

            btnOneConfirm = (Button) view.findViewById(R.id.btn_confirm);
            TextView txtContent = (TextView) view.findViewById(R.id.txt_content);
            boolean localApk = localApk(version);
            if (localApk) {
                btnOneConfirm.setText(R.string.download_now_install);
            } else {
                btnOneConfirm.setText(R.string.download_now_update);
            }
            txtContent.setText(text);

            btnOneConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean localApk = localApk(version);
                    if (localApk) {
                        installLocalApk(localApk,version);
                    } else {
                        downloadDialog(version,apkUrl);
                    }

                }
            });
        } else {

            //普通更新
            view = LayoutInflater.from(mActivity).inflate(R.layout.layout_update_dialog_twobtn, null);
            btnTwoConfirm = (Button)view.findViewById(R.id.btn_confirm);
            Button btnTwocancel = (Button)view.findViewById(R.id.btn_cancel);
            TextView txtContent = (TextView) view.findViewById(R.id.txt_content);
            boolean localApk = localApk(version);
            if (localApk) {
                btnTwoConfirm.setText(R.string.download_now_install);
            } else {
                btnTwoConfirm.setText(R.string.download_now_update);
            }
            txtContent.setText(text);

            btnTwoConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean localApk = localApk(version);
                    if (localApk) {
                        installLocalApk(localApk,version);
                    } else {
                        downloadDialog(version,apkUrl);
                    }
                }
            });
            btnTwocancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //取消按钮后，进行静默下载
                    boolean localApk = localApk(version);
                    if (!localApk) {//本地没有安装包，静默下载,但不自动安装
                        //WIFI状态下进行静默下载
                        int netType =  UpdateDialog.getNetType(mActivity);
                        if (netType == 2) { //WIFI状态下,静默下载
                            DownloadDialog.downloadApk(mActivity,version,apkUrl,false,true);
                        }

                    }

                    //取消正在下载
                    dialog.dismiss();

                }
            });

        }

        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setOnKeyListener(keylistener);
        Window window =dialog.getWindow();
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getHeight() * 0.4);
        dialog.getWindow().setAttributes(params);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);

    }

    public  void show() {
        dialog.show();
    }
    public  void dismiss() {
        dialog.dismiss();
    }
    /**
     * 返回键监听
     */
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                return true;
            } else {
                return false;
            }
        }
    } ;


    /**
     * 去下载窗口
     * @param mVersion
     * @param apkUrl
     */

    private void downloadDialog(String mVersion,String apkUrl) {
        int network = getNetType(mActivity);
        //去下载窗口
        if (network == 0) {
            AppUtils.showToast(mActivity,R.string.download_network);
        } else {
            DownloadDialog downloadDialog =  new DownloadDialog(mActivity,this);
            downloadDialog.showDownloadDialog(network,mVersion,apkUrl,fileSize);
            if (!forceUpdate.equals("Y")) {//非强制更新，隐藏dialog
                dialog.dismiss();
            }
        }
    }

    /**
     * * 判断本地是否拥有要需要更新的安装包
     * @param updateVersion
     * @return
     */
    private boolean localApk(String updateVersion) {

        if (EasyPermissions.hasPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {

            List<String> versions = VersionUtils.getApkVersion(mActivity,DownloadService.DOWNLOAD_PATH);
            if (versions!=null) {
                for (int i = 0;i < versions.size();i++) {
                    if (TextUtils.equals(updateVersion,versions.get(i))){
                        return true;
                    }
                }
            }

        } else {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_PHONE_STORAGE);
        }



        return false;
    }

    /**
     * 本地有安装包 去安装
     * @param localApk
     * @param mVersion
     */
    public void installLocalApk(boolean localApk,String mVersion) {
        //本地有需要更新的安装包
        if (localApk) {
            if (EasyPermissions.hasPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                //安装包名称
                String fileName = DownloadService.appName + mVersion + ".apk";
                DownloadService.installApk(mActivity, new File(DownloadService.DOWNLOAD_PATH, fileName));
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_PHONE_STORAGE);
            }
        }
    }

    /**
     *  判断当前网络类型
     * @return
     */
    public static int getNetType(Activity mActivity) {
        int state = NetworkUtil.getNetType(mActivity);
        //state = 0代表无网络 state = 1代表运营商网络 state = 2代表无线网络
        if (state == ConnectivityManager.TYPE_WIFI){
            state = 2;
        } else if (state == ConnectivityManager.TYPE_MOBILE) {
            state = 1;
        } else if (state < 0) {
            state = 0;
        }
        return state;
    }

    @Override
    public void DownloadFinished() {

        if (forceUpdate.equals("Y")) {
            btnOneConfirm.setText(R.string.download_now_install);
        } else {
            btnTwoConfirm.setText(R.string.download_now_install);
        }
    }

    @Override
    public void DownloadFailed() {

    }

    @Override
    public void DownloadCancel() {

    }
}
