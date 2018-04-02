package com.jinju.android.receiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.help.UIHelper;
import com.jinju.android.manager.CommonManager;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Libra on 2017/5/8.
 */

public class PushMessageReceiver extends com.xiaomi.mipush.sdk.PushMessageReceiver {
    private static final String TAG = PushMessageReceiver.class.getSimpleName();
    private String mRegId = "";
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mStartTime;
    private String mUserAccount;
    private String mEndTime;

    // 用来接收服务器向客户端发送的消息
    @Override
    public void onReceiveMessage(Context context, MiPushMessage message) {
        Log.v(TAG,
                "发送推送是否成功 " + message.toString());
        //获取服务器发送给客户端的消息
//        Map<String, String> extra = message.getExtra();

        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
    }
    //接收服务器推送的透传消息
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }
    }
    //接收服务器推送的通知消息，用户点击后触发，消息封装在 MiPushMessage类中
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

//        // 针对 按home键的情况
        Activity activity = DdApplication.getInstance().currentActivity();
        if (activity!=null) {
            Log.v(TAG, "将压入后台的app唤起到前台");
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(activity.getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
            UIHelper.showMainActivity(activity);
        } else {
            // 启动 App
            Log.v(TAG, "启动 App");
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Bundle bundle = new Bundle();
//            bundle.putString(XMDDLaunch.EXTRA_DATA, message.getContent());
//            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }
    //接收服务器推送的通知消息，消息到达客户端时触发
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if(!TextUtils.isEmpty(message.getTopic())) {
            mTopic=message.getTopic();
        } else if(!TextUtils.isEmpty(message.getAlias())) {
            mAlias=message.getAlias();
        } else if(!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount=message.getUserAccount();
        }
    }
    //获取给服务器发送命令的结果
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        Log.v(TAG, "获取给服务器发送注册命令的结果2" + message.toString());

        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);

        Log.v(TAG,
                "发送推送是否成功1" + cmdArg1);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }
    //获取给服务器发送注册命令的结果
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        Log.v(TAG,
                "获取给服务器发送注册命令的结果2" + message.toString());
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);

        Log.v(TAG,
                "发送推送是否成功2" + cmdArg1);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
            /**
             *  // 已登陆成功 注册regId上传个后台
             // 解决 老版本用户升级覆盖安装
             // 解决 regId 有可能会变的情况
             // 解决 用户登陆时候 regId可能会为空的情况
             */

            if (DdApplication.getConfigManager().isLogined()) {
                String memberId = DdApplication.getConfigManager().getMemberId();
                Map<String, Object> datas = new HashMap<String, Object>();
                datas.put("regId",mRegId);
                datas.put("memberId",memberId);
                DdApplication.getCommonManager().recordRegId(datas, mOnRecordRegIdListener);
            }
        }

    }
    private CommonManager.OnRecordRegIdListener mOnRecordRegIdListener = new CommonManager.OnRecordRegIdListener() {
        @Override
        public void onRecordRegIdFinished(Response response) {
            if (response.isSuccess()) {
                Log.i(TAG,"注册成功");
            }
        }
    };

}
