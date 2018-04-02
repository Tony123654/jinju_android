package com.jinju.android.activity.lock;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.activity.BaseActivity;
import com.jinju.android.base.DdApplication;
import com.jinju.android.util.AppUtils;
import com.jinju.android.widget.LockPatternView;

import java.util.List;

/**
 * Created by Libra on 2017/7/4.
 */

public class AlterGestureLockActivity extends BaseActivity {

    private TextView mErrorTips;
    private LockPatternView mLockPatternView;
    private TextView txtCancel;
    private String lockPassword;
    private Animation mShakeAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_gesture_lock);
        initView();
    }

    private void initView() {
        mErrorTips = (TextView) findViewById(R.id.txt_gesturepwd_unlock_failtip);
        mLockPatternView = (LockPatternView) findViewById(R.id.gesturepwd_unlock_lockview);
        txtCancel = (TextView) findViewById(R.id.txt_cancel);

        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);

        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
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
            lockPassword = DdApplication.getConfigManager().getLockPassword();
            if (DdApplication.getInstance().getLockPatternUtils().checkPattern(pattern, lockPassword)) {
                //解锁成功
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                AlterGestureLockActivity.this.setResult(RESULT_OK);
                finish();

            } else {
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mErrorTips.setText("密码错误，请重新输入");
                    mErrorTips.setTextColor(Color.RED);
                    mErrorTips.startAnimation(mShakeAnim);

                } else {
                    AppUtils.showToast(AlterGestureLockActivity.this, "输入长度不够(至少4个点)，请重试");
                }

                mLockPatternView.postDelayed(mClearPatternRunnable, 2000);

            }

        }
    };
}
