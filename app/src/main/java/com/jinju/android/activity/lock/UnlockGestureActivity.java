package com.jinju.android.activity.lock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.activity.BaseActivity;
import com.jinju.android.activity.ForgetGesturePasswordActivity;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.widget.LockPatternView;

import java.util.List;

/**
 * 解锁界面
 *
 * Created by Libra on 2017/6/22.
 */

public class UnlockGestureActivity extends BaseActivity {


    private static final int REQ_FORGET_GETTURE_PHONE = 1;
    private static final int REQ_CREATE_PATTERN = 2;
    private LockPatternView mLockPatternView;
    private String lockPassword;
    private TextView mErrorTips;
    private TextView mTxtForgetGesture;
    private Animation mShakeAnim;
    private ConfigManager mConfigManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_unlock);
        mConfigManager = DdApplication.getConfigManager();
        initView();
    }

    private void initView() {
        TextView txtNickname = (TextView) findViewById(R.id.txt_nickname);
        txtNickname.setText(mConfigManager.getNickName());
        mLockPatternView = (LockPatternView)findViewById(R.id.gesturepwd_unlock_lockview);
        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);

        mErrorTips = (TextView) findViewById(R.id.txt_gesturepwd_unlock_failtip);

        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);

        mTxtForgetGesture = (TextView) findViewById(R.id.txt_forget_gesture);
        mTxtForgetGesture.setOnClickListener(mTxtForgetGestureOnClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_FORGET_GETTURE_PHONE://忘记密码
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(UnlockGestureActivity.this, CreateGestureActivity.class);
                    startActivityForResult(intent,REQ_CREATE_PATTERN);
                }
                break;
            case REQ_CREATE_PATTERN:
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
                        finish();
                    }

                }
                break;
        }
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };
    //忘记密码按钮
    private View.OnClickListener mTxtForgetGestureOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String phone = mConfigManager.getPhone();
            if (!TextUtils.isEmpty(phone)) {
                Intent intent = new Intent(UnlockGestureActivity.this,ForgetGesturePasswordActivity.class);
                intent.putExtra("mobile",phone);
                startActivityForResult(intent,REQ_FORGET_GETTURE_PHONE);
            }
        }
    };
    protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {
        @Override
        public void onPatternStart() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
        }

        @Override
        public void onPatternCleared() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
        }

        @Override
        public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

        }

        @Override
        public void onPatternDetected(List<LockPatternView.Cell> pattern) {
            if (pattern == null)
                return;
            lockPassword = mConfigManager.getLockPassword();
            if (DdApplication.getInstance().getLockPatternUtils().checkPattern(pattern, lockPassword)) {
                //解锁成功
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                UnlockGestureActivity.this.setResult(RESULT_OK);
                finish();
            } else {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mErrorTips.setText("密码错误，请重新输入");
                    mErrorTips.setTextColor(Color.RED);
                    mErrorTips.startAnimation(mShakeAnim);

                } else {
                    AppUtils.showToast(UnlockGestureActivity.this, "输入长度不够(至少4个点)，请重试");
                }

                mLockPatternView.postDelayed(mClearPatternRunnable, 2000);

            }

        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
