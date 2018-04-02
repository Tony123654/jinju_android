package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.AutomateRule;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AutomateStatus;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.manager.UserManager.OnGetAutomateRuleFinishedListener;
import com.jinju.android.manager.UserManager.OnSetAutomateRuleFinishedListener;
import com.jinju.android.util.AppUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动投标
 */
public class AutomateActivity extends BaseActivity {
    private AutomateRule mAutomateRule;
    private ImageView mImgAutomateStatus;
    private LinearLayout mLayoutAutomate;
    private Dialog mProgressDialog;

    private TextView mTxtCycle;
    private TextView mTxtYearInterest;
    private TextView mTxtMinAmount;
    private TextView mTxtRetainAmount;
    private TextView mTxtSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automate);

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.setting_automate);

        mAutomateRule = new AutomateRule();

        mImgAutomateStatus = (ImageView) findViewById(R.id.img_automate_status);
        mImgAutomateStatus.setOnClickListener(mImgAutomateStatusOnClickListener);

        mLayoutAutomate = (LinearLayout) findViewById(R.id.layout_automate);
        mTxtCycle = (TextView) findViewById(R.id.txt_cycle);
        mTxtYearInterest = (TextView) findViewById(R.id.txt_year_interest);
        mTxtMinAmount = (TextView) findViewById(R.id.txt_min_amount);
        mTxtRetainAmount = (TextView) findViewById(R.id.txt_retain_amount);

        mTxtSetting = (TextView) findViewById(R.id.txt_setting);
        mTxtSetting.setOnClickListener(mTxtSettingOnClickListener);

        mProgressDialog = AppUtils.createLoadingDialog(this);
    }

    @Override
    protected void onResume() {
        getAutomateRule();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mProgressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    private void getAutomateRule() {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("iv", 1);

        mProgressDialog.show();
        DdApplication.getUserManager().getAutomateRule(datas, mOnGetAutomateRuleFinishedListener);
    }

    private OnGetAutomateRuleFinishedListener mOnGetAutomateRuleFinishedListener = new OnGetAutomateRuleFinishedListener() {

        @Override
        public void onGetAutomateRuleFinished(Response response, AutomateRule automateRule) {
            if (response.isSuccess()) {
                mAutomateRule = automateRule;

                boolean isEnabled = TextUtils.equals(AutomateStatus.ENABLE, mAutomateRule.getStatus());
                mLayoutAutomate.setVisibility(isEnabled ? View.VISIBLE : View.GONE);
                mImgAutomateStatus.setImageResource(isEnabled ? R.mipmap.img_switch_on : R.mipmap.img_switch_off);

                String minTimeLimitStr;
                if (0 == automateRule.getMinTimeLimit()) {
                    minTimeLimitStr = getString(R.string.no_limit);
                } else {
                    minTimeLimitStr = getString(R.string.day_number, automateRule.getMinTimeLimit());
                }

                String maxTimeLimitStr;
                if (0 == automateRule.getMaxTimeLimit()) {
                    maxTimeLimitStr = getString(R.string.no_limit);
                } else {
                    maxTimeLimitStr = getString(R.string.day_number, automateRule.getMaxTimeLimit());
                }

                mTxtCycle.setText(Html.fromHtml(getString(R.string.automate_cycle_rang, minTimeLimitStr, maxTimeLimitStr)));

                if (TextUtils.equals(automateRule.getYearRateMin(), "0")) {
                    mTxtYearInterest.setText(getString(R.string.no_limit));
                } else {
                    mTxtYearInterest.setText(getString(R.string.percent_number, automateRule.getYearRateMin()));
                }

                if (TextUtils.equals(automateRule.getMinMoneyYuan(), "0")) {
                    mTxtMinAmount.setText(getString(R.string.no_limit));
                } else {
                    mTxtMinAmount.setText(getString(R.string.money_number, automateRule.getMinMoneyYuan()));
                }

                if (TextUtils.equals(automateRule.getRetainFundYuan(), "0")) {
                    mTxtRetainAmount.setText(getString(R.string.no_limit));
                } else {
                    mTxtRetainAmount.setText(getString(R.string.money_number, automateRule.getRetainFundYuan()));
                }

            } else {
                AppUtils.handleErrorResponse(AutomateActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };

    private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }

    };

    private OnClickListener mImgAutomateStatusOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            boolean isEnabled = TextUtils.equals(AutomateStatus.ENABLE, mAutomateRule.getStatus());
            mAutomateRule.setStatus(isEnabled ? AutomateStatus.DISABLE : AutomateStatus.ENABLE);
            mLayoutAutomate.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
            mImgAutomateStatus.setImageResource(isEnabled ? R.mipmap.img_switch_off : R.mipmap.img_switch_on);

            if (isEnabled) {
                MobclickAgent.onEvent(AutomateActivity.this, UmengTouchType.RP115_2);
            } else {
                MobclickAgent.onEvent(AutomateActivity.this, UmengTouchType.RP115_1);
            }

            Map<String, Object> datas = new HashMap<String, Object>();
            datas.put("iv", 1);
            datas.put("status", mAutomateRule.getStatus());
            datas.put("minMoneyYuan", mAutomateRule.getMinMoneyYuan());
            datas.put("minRate", mAutomateRule.getYearRateMin());
            datas.put("minTimeLimit", mAutomateRule.getMinTimeLimit());
            datas.put("maxTimeLimit", mAutomateRule.getMaxTimeLimit());
            datas.put("retainFundYuan", mAutomateRule.getRetainFundYuan());

            mProgressDialog.show();
            DdApplication.getUserManager().setAutomateRule(datas, mOnSetAutomateRuleFinishedListener);
        }

    };

    private OnSetAutomateRuleFinishedListener mOnSetAutomateRuleFinishedListener = new OnSetAutomateRuleFinishedListener() {

        @Override
        public void onSetAutomateRuleFinished(Response response) {
            mProgressDialog.dismiss();
            if (!response.isSuccess()) {
                AppUtils.handleErrorResponse(AutomateActivity.this, response);
                getAutomateRule();
            }
        }
    };

    private OnClickListener mTxtSettingOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(AutomateActivity.this, AutomateSettingActivity.class);
            startActivity(intent);
        }

    };

}