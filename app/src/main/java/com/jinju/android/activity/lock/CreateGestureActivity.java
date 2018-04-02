package com.jinju.android.activity.lock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.activity.BaseActivity;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置手势锁界面
 * Created by Libra on 2017/6/21.
 *
 */

public class CreateGestureActivity extends BaseActivity implements View.OnClickListener{

    private static final int ID_EMPTY_MESSAGE = -1;
    private static final String KEY_PATTERN_CHOICE = "chosenPattern";
    private static final String KEY_UI_STAGE = "uiStage";
    public  static final int BACK_PRESSED = 10000;
    private LockPatternView mLockPatternView;
    private TextView mHeaderText;
    private TextView tv_reset;
    private TextView tv_title;
    private View mPreviewViews[][] = new View[3][3];
    protected List<LockPatternView.Cell> mChosenPattern = null;
    private Stage mUiStage = Stage.Introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_create);
        //设置解锁
        mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_create_lockview);
        mHeaderText = (TextView) findViewById(R.id.gesturepwd_create_text);

        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);
        tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_reset.setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        String strTitle = "";

        try {
            Intent intent = getIntent();
            strTitle = intent.getStringExtra("TitleText");
        } catch (Exception e) {
            strTitle = "";
        }

        if (strTitle != null && !strTitle.isEmpty()) {
            tv_title.setText(strTitle);
        }

        initPreviewViews();
        if (savedInstanceState == null) {
            //初始化显示
            updateStage(Stage.Introduction);
        } else {
            //显示退出前保存的状态
            final String patternString = savedInstanceState.getString(KEY_PATTERN_CHOICE);
            if (patternString != null) {
                //解密保存的状态数据
                mChosenPattern = LockPatternUtils.stringToPattern(patternString);
            }
            updateStage(Stage.values()[savedInstanceState.getInt(KEY_UI_STAGE)]);
        }
    }
    // 预览图
    private void initPreviewViews() {

        mPreviewViews[0][0] = findViewById(R.id.gesturepwd_setting_preview_0);
        mPreviewViews[0][1] = findViewById(R.id.gesturepwd_setting_preview_1);
        mPreviewViews[0][2] = findViewById(R.id.gesturepwd_setting_preview_2);
        mPreviewViews[1][0] = findViewById(R.id.gesturepwd_setting_preview_3);
        mPreviewViews[1][1] = findViewById(R.id.gesturepwd_setting_preview_4);
        mPreviewViews[1][2] = findViewById(R.id.gesturepwd_setting_preview_5);
        mPreviewViews[2][0] = findViewById(R.id.gesturepwd_setting_preview_6);
        mPreviewViews[2][1] = findViewById(R.id.gesturepwd_setting_preview_7);
        mPreviewViews[2][2] = findViewById(R.id.gesturepwd_setting_preview_8);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //ordinal（）初始枚举分配的常量为0
        outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
        if (mChosenPattern != null) {
            //加密保存activity数据
            outState.putString(KEY_PATTERN_CHOICE, LockPatternUtils.patternToString(mChosenPattern));
        }

    }

    /**
     * 刷新步骤
     * @param stage
     */
    private void updateStage(Stage stage) {
        mUiStage = stage;
        if (stage == Stage.ChoiceTooShort) {
            mHeaderText.setText(getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE));
        } else {
            mHeaderText.setText(stage.headerMessage);
        }

        if (stage.patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
        switch (mUiStage) {
            case Introduction:
                mLockPatternView.clearPattern();
                break;
            case ChoiceTooShort:
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case FirstChoiceValid:
                break;
            case NeedToConfirm:
                mLockPatternView.clearPattern();
                updatePreviewViews();
                break;
            case ConfirmWrong:
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case ChoiceConfirmed:
                break;
        }

    }
    /**
     *  刷新预览图
     */
    private void updatePreviewViews() {
        if (mChosenPattern == null)
            return;
        Log.i("way", "result = " + mChosenPattern.toString());
        for (LockPatternView.Cell cell : mChosenPattern) {
            Log.i("way", "cell.getRow() = " + cell.getRow() + ", cell.getColumn() = " + cell.getColumn());
            mPreviewViews[cell.getRow()][cell.getColumn()].setBackgroundResource(R.mipmap.gesture_create_grid_selected);
        }
    }
    /**
     *  重设预览图
     */
    private void resetPreviewViews() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPreviewViews[i][j].setBackground(null);
            }
        }
    }
    private void postClearPatternRunnable() {
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
    }

    /**
     * 清除图案
     */
    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };
    /**
     * 解锁图案监听
     */
    LockPatternView.OnPatternListener mChooseNewLockPatternListener  = new LockPatternView.OnPatternListener() {
        @Override
        public void onPatternStart() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
            patternInProgress();
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
            if (mUiStage == Stage.NeedToConfirm || mUiStage == Stage.ConfirmWrong) {
                if (mChosenPattern == null)
                    throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
                if (mChosenPattern.equals(pattern)) {
                    updateStage(Stage.ChoiceConfirmed);

                    //自动确认保存
                    if (mUiStage != Stage.ChoiceConfirmed) {
                        throw new IllegalStateException("expected ui stage "
                                + Stage.ChoiceConfirmed + " when button is "
                                + RightButtonMode.Confirm);
                    }
                    saveChosenPatternAndFinish();
                } else {
                    updateStage(Stage.ConfirmWrong);
                }
            } else if (mUiStage == Stage.Introduction || mUiStage == Stage.ChoiceTooShort) {
                if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                    updateStage(Stage.ChoiceTooShort);
                } else {
                    mChosenPattern = new ArrayList<LockPatternView.Cell>(
                            pattern);
                    updateStage(Stage.FirstChoiceValid);

                    //自动下一步：再让用户画一遍
                    if (mUiStage.rightMode == RightButtonMode.Continue) {
                        if (mUiStage != Stage.FirstChoiceValid) {
                            throw new IllegalStateException("expected ui stage " + Stage.FirstChoiceValid + " when button is " + RightButtonMode.Continue);
                        }
                        updateStage(Stage.NeedToConfirm);
                    } else if (mUiStage.rightMode == RightButtonMode.Ok) {
                        mLockPatternView.clearPattern();
                        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                        updateStage(Stage.Introduction);
                    }
                }
            } else {
                throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
            }

        }

        private void patternInProgress() {
            mHeaderText.setText(R.string.lockpattern_recording_inprogress);
        }
    };
    //手势设置成功
    private void saveChosenPatternAndFinish() {

        //手势成功后清空原来的手势锁锁
        DdApplication.getInstance().getLockPatternUtils().clearLock();
        //保存手势密码
        DdApplication.getInstance().getLockPatternUtils().saveLockPattern(mChosenPattern);
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("pattern", LockPatternUtils.patternToHash(mChosenPattern));
        ConfigManager  mConfigManager = DdApplication.getConfigManager();
        //手势锁要与账号对应保存在本地，这里用账号作为对应码
        String mLockMatchingCode = mConfigManager.getPhone();
        mConfigManager.setLockMatchingCode(mLockMatchingCode);
        //设置返回数据
        this.setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reset:
                mChosenPattern = null;
                mLockPatternView.clearPattern();
                resetPreviewViews();
                updateStage(Stage.Introduction);
                break;
            default:
                break;
        }
    }

    /**
     * The states of the left footer button.
     */
    enum LeftButtonMode {
        Cancel(android.R.string.cancel, true), CancelDisabled(
                android.R.string.cancel, false), Retry(
                R.string.lockpattern_retry_button_text, true), RetryDisabled(
                R.string.lockpattern_retry_button_text, false), Gone(
                ID_EMPTY_MESSAGE, false);


        final int text;
        final boolean enabled;
        /**
         * @param text    The displayed text for this mode.
         * @param enabled Whether the button should be enabled.
         */
        LeftButtonMode(int text, boolean enabled) {
            this.text = text;
            this.enabled = enabled;
        }
    }

    /**
     * The states of the right button.
     */
    enum RightButtonMode {
        Continue(R.string.lockpattern_continue_button_text, true), ContinueDisabled(
                R.string.lockpattern_continue_button_text, false), Confirm(
                R.string.lockpattern_confirm_button_text, true), ConfirmDisabled(
                R.string.lockpattern_confirm_button_text, false), Ok(
                android.R.string.ok, true);

        final int text;
        final boolean enabled;
        /**
         * @param text    The displayed text for this mode.
         * @param enabled Whether the button should be enabled.
         */
        RightButtonMode(int text, boolean enabled) {
            this.text = text;
            this.enabled = enabled;
        }
    }
    protected enum Stage {

        Introduction(R.string.lockpattern_recording_intro_header,
                LeftButtonMode.Cancel, RightButtonMode.ContinueDisabled,
                ID_EMPTY_MESSAGE, true), ChoiceTooShort(
                R.string.lockpattern_recording_incorrect_too_short,
                LeftButtonMode.Retry, RightButtonMode.ContinueDisabled,
                ID_EMPTY_MESSAGE, true), FirstChoiceValid(
                R.string.lockpattern_pattern_entered_header,
                LeftButtonMode.Retry, RightButtonMode.Continue,
                ID_EMPTY_MESSAGE, false), NeedToConfirm(
                R.string.lockpattern_need_to_confirm, LeftButtonMode.Cancel,
                RightButtonMode.ConfirmDisabled, ID_EMPTY_MESSAGE, true), ConfirmWrong(
                R.string.lockpattern_need_to_unlock_wrong,
                LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled,
                ID_EMPTY_MESSAGE, true), ChoiceConfirmed(
                R.string.lockpattern_pattern_confirmed_header,
                LeftButtonMode.Cancel, RightButtonMode.Confirm,
                ID_EMPTY_MESSAGE, false);

        final int headerMessage;
        final LeftButtonMode leftMode;
        final RightButtonMode rightMode;
        final int footerMessage;
        final boolean patternEnabled;
        /**
         * @param headerMessage  The message displayed at the top.
         * @param leftMode       The mode of the left button.
         * @param rightMode      The mode of the right button.
         * @param footerMessage  The footer message.
         * @param patternEnabled Whether the pattern widget is enabled.
         */
        Stage(int headerMessage, LeftButtonMode leftMode,
              RightButtonMode rightMode, int footerMessage,
              boolean patternEnabled) {
            this.headerMessage = headerMessage;
            this.leftMode = leftMode;
            this.rightMode = rightMode;
            this.footerMessage = footerMessage;
            this.patternEnabled = patternEnabled;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(BACK_PRESSED);
    }
}
