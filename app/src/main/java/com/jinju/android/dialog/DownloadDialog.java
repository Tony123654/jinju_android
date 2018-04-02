package com.jinju.android.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.constant.BroadcastType;
import com.jinju.android.interfaces.DownloadFinishListener;
import com.jinju.android.service.DownloadService;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Libra on 2017/8/16.
 * 跟新下载dialog
 */

public class DownloadDialog {
    private static final int PERMISSION_PHONE_STORAGE = 0;
    private Activity mActivity;
    private Dialog dialog;
    private View view;
    private MyBroadcastReciver mMyBroadcast;
    private ProgressBar mProgressBar;
    private TextView txtProgressPercent;
    private DownloadFinishListener mDownloadFinishListener;
    private String mFileSize;
    private static Intent mIntent;

    public DownloadDialog(Activity activity,DownloadFinishListener listener) {
        mActivity = activity;
        mDownloadFinishListener = listener;
    }
    public void showDownloadDialog(int networkType, final String version, final String apkUrl,String fileSize) {

        this.mFileSize = fileSize;
        dialog =  new Dialog(mActivity, R.style.custome_round_dialog);
        //正在下载窗口
        if (networkType == 2) {

            //动态注册广播
        mMyBroadcast = new MyBroadcastReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastType.DOWNLOAD_BROADCASTRECIVER);
        mActivity.registerReceiver(mMyBroadcast, intentFilter);

            view = LayoutInflater.from(mActivity).inflate(R.layout.layout_download_dialog_twobtn, null);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            txtProgressPercent = (TextView) view.findViewById(R.id.txt_Progress_percent);

            Button btnCancel = (Button)view.findViewById(R.id.btn_cancel);
            if (EasyPermissions.hasPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                //下载apk，并自动安装
                downloadApk(mActivity,version,apkUrl,true,true);
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_PHONE_STORAGE);
            }


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消正在下载，并删除未完成下载的文件
                    DownloadService.stopDownload(false,false);
                    DownloadService.delFile();
                    unregisterReceiver();
                    dialog.dismiss();
                }
            });
        }
        //网络提示窗口
        if (networkType == 1) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.layout_download_dialog_onebtn, null);
            Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
            Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
            TextView txtNetworkWarning = (TextView) view.findViewById(R.id.txt_network_warning);
            txtNetworkWarning.setText(mActivity.getString(R.string.download_warning,mFileSize));
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到正在下载更新
                    DownloadDialog downloadDialog = new DownloadDialog(mActivity,mDownloadFinishListener);
                    downloadDialog.showDownloadDialog(2,version,apkUrl,mFileSize);
                    dialog.dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }



        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setOnKeyListener(keylistener);
        dialog.show();

        Window window =dialog.getWindow();
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getHeight() * 0.4);
        dialog.getWindow().setAttributes(params);

        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);


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
     * 开启服务下载apk
     * @param mActivity
     * @param mVersion 下载的版本号
     * @param apkUrl
     * @param install  下载后是否安装
     */

    public static void downloadApk(Activity mActivity,String mVersion,String apkUrl,boolean install,boolean isGoOn) {


        if (mIntent==null) {
            //启动服务
            mIntent = new Intent(mActivity,DownloadService.class);
        }
        mIntent.putExtra("url",apkUrl);
        mIntent.putExtra("version",mVersion);
        mIntent.putExtra("install",install);
        mIntent.putExtra("goOn",isGoOn);
        mActivity.startService(mIntent);
    }

    /**
     * 广播 更新进度
     */
    private class MyBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long progress = intent.getLongExtra("progress",0);

            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress((int)progress);
            txtProgressPercent.setText(progress+"%");

            if (progress == 100) {
                dialog.dismiss();
                mDownloadFinishListener.DownloadFinished();
                unregisterReceiver();
            }
        }
    }

    /**
     * 关闭广播
     */
    public void unregisterReceiver() {

        if (mMyBroadcast!=null) {
            mActivity.unregisterReceiver(mMyBroadcast);
            mMyBroadcast = null;
        }
    }

}
