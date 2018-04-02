package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.activity.lock.AlterGestureLockActivity;
import com.jinju.android.activity.lock.CreateGestureActivity;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.util.AppUtils;
import com.umeng.analytics.MobclickAgent;

/**
 *  手势密码 界面
 *
 * Created by Libra on 2017/6/22.
 *
 */

public class GestureLockActivity extends BaseActivity {

    private static final int REQ_ALTER_PASSWORD = 1;
    private static final int REQ_CREATE_PATTERN = 2;
    private static final int REQ_FORGET_GETTURE_PHONE = 3;
    private static final int REQ_OFF_LOCK = 4;
    private ImageView mImgGestureStatus;
    private boolean mLockStatus;
    private RelativeLayout mLayoutGestureAlter;
    private RelativeLayout mLayoutGestureForget;
    private ConfigManager mConfigManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        mConfigManager = DdApplication.getConfigManager();
        initView();
    }

    private void initView() {
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.gesture_password_guide_switch_btn);

        mLockStatus = mConfigManager.getLockStatus();
        mImgGestureStatus = (ImageView) findViewById(R.id.img_gesture_status);
        mImgGestureStatus.setImageResource( mLockStatus ? R.mipmap.img_switch_on : R.mipmap.img_switch_off);
        mImgGestureStatus.setOnClickListener(mImgGestureStatusOnClickListener);
        //修改手势密码
        mLayoutGestureAlter = (RelativeLayout) findViewById(R.id.layout_gesture_alter);
        mLayoutGestureAlter.setOnClickListener(mLayoutGestureAlterOnClickListener);
        //忘记手势密码
        mLayoutGestureForget = (RelativeLayout) findViewById(R.id.layout_gesture_forget);
        mLayoutGestureForget.setOnClickListener(mLayoutGestureForgetOnClickListener);


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ALTER_PASSWORD://密码修改

                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(GestureLockActivity.this, CreateGestureActivity.class);
                    startActivityForResult(intent,REQ_CREATE_PATTERN);
                }

                break;
            case REQ_FORGET_GETTURE_PHONE ://忘记密码
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(GestureLockActivity.this, CreateGestureActivity.class);
                    startActivityForResult(intent,REQ_CREATE_PATTERN);
                }
                break;
            case REQ_CREATE_PATTERN://重新创建手势返回结果
                if (resultCode == RESULT_OK) {
                    //创建手势成功保存手势
                    byte[] pattern = data.getByteArrayExtra("pattern");
                    if (pattern != null) {
                        StringBuffer buffer = new StringBuffer();
                        for (byte c : pattern) {
                            buffer.append(c);
                        }
                        //保存锁开启状态
                        mConfigManager.setLockStatus(true);
                        //保存创建的手势图
                        mConfigManager.setLockPassword(buffer.toString());
                        AppUtils.showToast(this, "手势密码设置成功");
                    }

                }
                break;
            case REQ_OFF_LOCK:
                if (resultCode == RESULT_OK) {
                    //清空原来的手势锁
                    DdApplication.getInstance().getLockPatternUtils().clearLock();
                    mImgGestureStatus.setImageResource(R.mipmap.img_switch_off);
                    finish();
                }

                break;
        }
    }
    //开关键
    private View.OnClickListener mImgGestureStatusOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            MobclickAgent.onEvent(GestureLockActivity.this, UmengTouchType.RP110_3);
            if (mLockStatus) {
                Intent intent = new Intent(GestureLockActivity.this, AlterGestureLockActivity.class);
                intent.putExtra("pattern",mConfigManager.getLockPassword());
                startActivityForResult(intent,REQ_OFF_LOCK);

            } else {
                mConfigManager.setLockStatus(true);
                mImgGestureStatus.setImageResource(R.mipmap.img_switch_on);
            }
        }
    };

    /**
     * 修改手势密码
     */
    private View.OnClickListener mLayoutGestureAlterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mLockPassword = mConfigManager.getLockPassword();
            if (!TextUtils.isEmpty(mLockPassword)) {
                MobclickAgent.onEvent(GestureLockActivity.this, UmengTouchType.RP110_1);

                Intent intent = new Intent(GestureLockActivity.this, AlterGestureLockActivity.class);
                intent.putExtra("pattern",mLockPassword);
                startActivityForResult(intent,REQ_ALTER_PASSWORD);
            }
        }

    };
    /**
     *  忘记密码
     */
    private View.OnClickListener mLayoutGestureForgetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone = mConfigManager.getPhone();

            if (!TextUtils.isEmpty(phone)) {
                MobclickAgent.onEvent(GestureLockActivity.this, UmengTouchType.RP110_2);

                Intent intent = new Intent(GestureLockActivity.this,ForgetGesturePasswordActivity.class);
                intent.putExtra("mobile",phone);
                startActivityForResult(intent,REQ_FORGET_GETTURE_PHONE);
            }
        }
    };
}
