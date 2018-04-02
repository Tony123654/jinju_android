package com.jinju.android.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.jinju.android.R;
import com.jinju.android.constant.BroadcastType;
import com.jinju.android.manager.ThreadManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.SDPathUtils;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Libra on 2017/5/16.
 */

public class DownloadService extends Service {
    private static final int MSG_INIT = 0;
    private static final int URL_ERROR = 1;
    private static final int NET_ERROR = 2;
    private static final int DOWNLOAD_SUCCESS = 3;
    private static final int BUTTON_PALY_ID = 4;

//    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
//            "/downloads/";
    public static final String DOWNLOAD_PATH = SDPathUtils.getDownloadPath();

    public static final String TAG = "download";
    public static final String ACTION_BUTTON = "com.notification.intent.action.ButtonClick";

    private static final String INTENT_BUTTONID_TAG = "ButtonId";

    public ButtonBroadcastReceiver receiver;
    private String mUrl;//下载链接
    private int length;//文件长度
    private static String fileName = null;//文件名
    private Notification notification;
    private RemoteViews contentView;
    private NotificationManager notificationManager;
    public static String appName = "jinju_";
    private String mVersion;
    private static boolean mInstall;//是否安装
    private static boolean isGoOn = true;//是否继续下载

    private Intent intent = new Intent(BroadcastType.DOWNLOAD_BROADCASTRECIVER);
    private ThreadManager.ThreadPoolProxy mThreadPoolProxy ;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    length = (int) msg.obj;
                    //线程池下载任务
                    ThreadManager.getSinglePool().execute(mDownloadRunnable);

                    //创建通知栏
//                    createNotification();
                    break;
                case DOWNLOAD_SUCCESS:
                    //下载完成，
//                    notifyNotification(100, 100);

                    if (mInstall) { //如果不是静默下载的就自动安装
                        sendBroadcast(100,100);
                        installApk(DownloadService.this, new File(DOWNLOAD_PATH, fileName));
                        AppUtils.showToast(DownloadService.this, R.string.download_finish);
                    }


                    break;
                case URL_ERROR:
                    AppUtils.showToast(DownloadService.this, R.string.download_url_error);
                    break;
                case NET_ERROR:
                    AppUtils.showToast(DownloadService.this, R.string.download_connect_error);
            }
        }

        ;
    };

    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mUrl = intent.getStringExtra("url");
            mVersion = intent.getStringExtra("version");
            mInstall = intent.getBooleanExtra("install",false);
            isGoOn = intent.getBooleanExtra("goOn",true);

            if (mUrl != null && !TextUtils.isEmpty(mUrl)) {

                ThreadManager.getSinglePool().stop();
                ThreadManager.getSinglePool().execute(mInitRunnable);
                //开始
//                new InitThread(mUrl).start();

            } else {
                mHandler.sendEmptyMessage(URL_ERROR);
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化子线程
     */
    Runnable mInitRunnable = new Runnable(){
        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                //连接网络文件
                URL url = new URL(mUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                int length = -1;
                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    //获得文件长度
                    length = conn.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                fileName = mUrl.substring(mUrl.lastIndexOf("/") + 1, mUrl.length());
                if (fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
                    fileName = getPackageName() + ".apk";
                }
                File file = new File(dir, fileName);
                raf = new RandomAccessFile(file, "rwd");
                //设置文件长度
                raf.setLength(length);
                mHandler.obtainMessage(MSG_INIT, length).sendToTarget();
            } catch (Exception e) {
                mHandler.sendEmptyMessage(URL_ERROR);
                e.printStackTrace();
            } finally {
                try {
                    conn.disconnect();
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /**
     * 下载线程
     *
     */
    Runnable mDownloadRunnable = new Runnable(){
        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;
            try {

                URL url = new URL(mUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = 0;
                conn.setRequestProperty("Range", "bytes=" + 0 + "-" + length);
                //设置文件写入位置
                fileName = appName + mVersion + ".apk";
                File file = new File(DownloadService.DOWNLOAD_PATH, fileName);
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                long mFinished = 0;
                //开始下载
                if (conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                    //LogUtil.i("下载开始了。。。");
                    //读取数据
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long speed = 0;
                    long time = System.currentTimeMillis();
                    while ((len = input.read(buffer)) != -1) {
                        //是否继续下载
                        if (!isGoOn) {
                            Log.i(TAG, "下载取消。。。");
                            break;
                        }
                        //写入文件
                        raf.write(buffer, 0, len);
                        //把下载进度发送广播给Activity
                        mFinished += len;
                        speed += len;
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            //发送广播
                            if (mInstall) {//非静默下载状态下发送广播更新弹窗进度条
                                sendBroadcast(mFinished,length);

                            }
                            // 通知栏进度条
//                            notifyNotification(mFinished, length);

                            Log.i(TAG, "mFinished==" + mFinished);
                            Log.i(TAG, "length==" + length);
                            Log.i(TAG, "speed==" + speed);
                            speed = 0;

                        }
                    }
                    if (mInstall) {
                        mHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
                    }
                    if(mFinished == length) {
                        Log.i(TAG, "下载完成了。。。");
                    }
                } else {
                    Log.i(TAG, "下载出错了。。。");
                    mHandler.sendEmptyMessage(NET_ERROR);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    };


    /**
     * 创建通知栏 进度提醒
     */
    @SuppressWarnings("deprecation")
    public void createNotification() {
        //应用的图标
        notification = new Notification(R.mipmap.ic_launcher, getResources().getString(R.string.download_downloading), System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;


        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setProgressBar(R.id.progress, 100, 0, false);
        contentView.setTextViewText(R.id.tv_progress, "0%");
        notification.contentView = contentView;

        //注册广播进行点击事件
        receiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(receiver, intentFilter);
        //设置点击安装
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_PALY_ID);
        PendingIntent intent_paly = PendingIntent.getBroadcast(this, BUTTON_PALY_ID, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.ll_notification, intent_paly);

//         点击notification自动消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //设置notification的PendingIntent
		/*Intent intt = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this,100, intt,Intent.FLAG_ACTIVITY_NEW_TASK	| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		notification.contentIntent = pi;*/

        notificationManager.notify(R.layout.notification_item, notification);
    }

    /**
     * 通知栏进度提醒
     * @param percent
     * @param length
     */
    private void notifyNotification(long percent, long length) {


        contentView.setTextViewText(R.id.tv_progress, (percent * 100 / length) + "%");
        contentView.setProgressBar(R.id.progress, (int) length, (int) percent, false);

        if (percent * 100 / length == 100) {
            contentView.setTextViewText(R.id.tv_download_info, getResources().getString(R.string.download_finish));
        }
        notification.contentView = contentView;
        notificationManager.notify(R.layout.notification_item, notification);
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Context context, File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        //大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.jinju.android.fileprovider",file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * （通知栏中的点击事件是通过广播来通知的，所以在需要处理点击事件的地方注册广播即可）
     * 广播监听按钮点击事件
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_BUTTON)) {
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_PALY_ID:
                        //关闭广播
                        closeNotification();
                        installApk(DownloadService.this, new File(DOWNLOAD_PATH, fileName));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 关闭通知
     */
    public void closeNotification() {

        if (receiver != null) {
            //注销广播
            unregisterReceiver(receiver);
        }
        if (notificationManager != null) {
            notificationManager.cancel(R.layout.notification_item);
        }
    }

    public void collapseStatusBar() {

        try {

            Method collapse;

            if (Build.VERSION.SDK_INT <= 16) {
                collapse = notificationManager.getClass().getMethod("collapse");
            } else {
                collapse = notificationManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(notificationManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 发送广播,更新弹窗进度条
     * @param percent 进度
     * @param length  总大小
     */
    private void sendBroadcast(long percent, long length) {
        if (intent!=null) {
            intent.putExtra("progress", percent * 100 / length);
            intent.putExtra("length", length);
            sendBroadcast(intent);
        }

    }

    /**
     *  停止下载
     * @param install 是否安装
     * @param goOn  是否继续下载
     */
    public static void stopDownload(boolean install,boolean goOn) {
        mInstall = install;
        isGoOn = goOn;
    }

    /**
     * 删除取消下载的文件
     */
    public static void delFile() {
        if (fileName!=null) {
            File file = new File(DownloadService.DOWNLOAD_PATH, fileName);
            file.delete();
        }

    }

}
