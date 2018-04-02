package com.jinju.android.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.PayOrder;
import com.jinju.android.api.Response;
import com.jinju.android.api.WithdrawDetail;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.PayType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.PayDialog;
import com.jinju.android.dialog.WarmPromptDialog;
import com.jinju.android.manager.TradeManager.OnInitWithdrawFinishedListener;
import com.jinju.android.manager.TradeManager.OnWithdrawFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.PayUtils;
import com.jinju.android.util.TextShowUtils;
import com.jinju.android.util.Utils;
import com.jinju.android.util.PayUtils.OnDealPayErrorFinishedListener;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.widget.PayView;
import com.jinju.android.widget.PayView.OnPayListener;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现
 */
public class WithdrawActivity extends BaseActivity {

    private static final int WITHDRAW_SUCCESS = 0;
    private TextView mTxtCanUseBalance;
    private TextView mTxtServiceCharge;
    private TextView mTxtActualAmount;
    private TextView mWarmPrompt;
    private TextView mTxtBankName;
    private TextView mTxtBankCardNum;
    private TextView mTxtWithdrawAlert;
    private TextView mTxtFreeCount;
    private TextView mTxtWithdrawRecord;

    private EditText mEditAmount;
    private int mPoundageType;
    private String mPoundageValue;
    private String mAmount;
    private String realCanUseBalance;
    private Button mBtnConfirm;
    private long mCanUseBalance = 0;
    private long mMemberBankId;

    private ImageView mImgBankLogo;
    private PayDialog mPayDialog;
    private PayOrder mPayOrder;
    private Dialog mProgressDialog;
    private List<String> mWarmPromptList;
    private Boolean isTouch = true;
    private WarmPromptDialog warmPromptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);

        mTxtCanUseBalance = (TextView) findViewById(R.id.txt_can_use_balance);    //可提现金额
        mTxtServiceCharge = (TextView) findViewById(R.id.txt_service_charge);    //手续费
        mTxtActualAmount = (TextView) findViewById(R.id.txt_actual_to_the_account); //实际到账金额
        mWarmPrompt = (TextView) findViewById(R.id.btn_warm_prompt);
        mWarmPrompt.setOnClickListener(mWarmPromptOnClickListener);
        mWarmPromptList = new ArrayList<String>();

        mEditAmount = (EditText) findViewById(R.id.edit_amount);

        //设置EditText hint字体大小
        SpannableString ss = new SpannableString("请输入金额（建议单笔提现100元以上）");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEditAmount.setHint(new SpannedString(ss));
        mEditAmount.addTextChangedListener(mTextChangedListener);

        //免费提现次数
        mTxtFreeCount = (TextView) findViewById(R.id.txt_free_count);

        mImgBankLogo = (ImageView) findViewById(R.id.img_bank_logo);
        mTxtBankName = (TextView) findViewById(R.id.txt_bank_name);
        mTxtBankCardNum = (TextView) findViewById(R.id.txt_bank_card_num);
        //到账提醒
        mTxtWithdrawAlert = (TextView) findViewById(R.id.withdraw_alert);

        mTxtWithdrawRecord = (TextView) findViewById(R.id.txt_withdraw_record);
        mTxtWithdrawRecord.setOnClickListener(mTxtWithdrawRecordOnClickListener);

        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);
        setConfirmButton(false);

        mPayOrder = new PayOrder();


        mProgressDialog = AppUtils.createLoadingDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initWithdraw();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (warmPromptDialog != null) {
            warmPromptDialog.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == WITHDRAW_SUCCESS) {
            mEditAmount.setText("");
            initServiceCharge();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        List<View> viewList = new ArrayList<View>();
        viewList.add(mEditAmount);

        AppUtils.checkNeedHideSoftInput(WithdrawActivity.this, (int) event.getX(), (int) event.getY(), viewList);
        return super.dispatchTouchEvent(event);
    }

    private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }

    };

    private TextWatcher mTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //设置最多输入两位小数
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    mEditAmount.setText(s);
                    mEditAmount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEditAmount.setText(s);
                mEditAmount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEditAmount.setText(s.subSequence(0, 1));
                    mEditAmount.setSelection(1);
                    return;
                }
            }

            //设置输入框最大限度

            if (s.toString().isEmpty() == false) {
                //输入的提现
                double editBalance = Double.parseDouble(s.toString().trim());
                //最大提现
                double balance = (double) mCanUseBalance / 100;

                if (balance < editBalance) {
                    mEditAmount.setText(realCanUseBalance);
                    mEditAmount.setSelection((realCanUseBalance).length());
                }

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (TextUtils.isEmpty(s.toString()) || !DataUtils.isDouble(s.toString())) {
                setConfirmButton(false);
            } else {

                setConfirmButton(true);
            }
            //手续费计算
            initServiceCharge();

        }
    };

    private void setConfirmButton(boolean enable) {
        if (enable) {
            mBtnConfirm.setBackgroundResource(R.drawable.btn_red_solid_noradius_bg);
            mBtnConfirm.setTextColor(getResources().getColor(R.color.white));
            mBtnConfirm.setClickable(true);
        } else {
            mBtnConfirm.setBackgroundResource(R.drawable.button_disabled_noradius);
            mBtnConfirm.setTextColor(getResources().getColor(R.color.btn_text_disabled));
            mBtnConfirm.setClickable(false);
        }
    }

    private void initWithdraw() {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("iv", VersionUtils.getVersionName());    //	参数传当前版本号

        mProgressDialog.show();
        DdApplication.getTradeManager().initWithdraw(datas, mOnInitWithdrawFinishedListener);
    }

    private OnInitWithdrawFinishedListener mOnInitWithdrawFinishedListener = new OnInitWithdrawFinishedListener() {

        @Override
        public void onInitWithdrawFinished(Response response, WithdrawDetail withdrawDetail) {
            if (response.isSuccess()) {

                mWarmPromptList = withdrawDetail.getWarmPrompt();
                mCanUseBalance = withdrawDetail.getCanUseBalance();
                realCanUseBalance = DataUtils.convertDecimalNumberFormat(mCanUseBalance);
                mMemberBankId = withdrawDetail.getMemberBankId();
                mPoundageType = withdrawDetail.getPoundageType();        //手续费类型
                mPoundageValue = withdrawDetail.getPoundageValue();        //手续费获取数据

                ImageUtils.displayImage(mImgBankLogo, withdrawDetail.getLogoPath());
                mTxtBankName.setText(withdrawDetail.getBankName());
                mTxtBankCardNum.setText(getString(R.string.bank_card_tail, withdrawDetail.getCardNoTail()));
                mTxtCanUseBalance.setText(getString(R.string.withdraw_balance, DataUtils.convertCurrencyFormat(withdrawDetail.getCanUseBalance())));
                mTxtWithdrawAlert.setText(withdrawDetail.getNote());
                mTxtFreeCount.setText(Html.fromHtml(TextShowUtils.oneColorHtmlText(withdrawDetail.getPoundageDesc())));        //剩余免费提现次数

            } else {
                AppUtils.handleErrorResponse(WithdrawActivity.this, response);
            }
            mProgressDialog.dismiss();
        }
    };

    /**
     * 初始化手续费
     */
    private void initServiceCharge() {
        if (TextUtils.isEmpty(mEditAmount.getText().toString())) {     //非空判断
            mTxtActualAmount.setText("0.00");
            mTxtServiceCharge.setText("0.00");
        } else {
            Double inputNum = Double.parseDouble(mEditAmount.getText().toString());
            if (inputNum < 5) {     //金额<5
                mTxtActualAmount.setText("0.00");
                mTxtServiceCharge.setText("0.00");
            } else {    //手续费类别判断
                if (mPoundageType == 0) {    //固定手续费
                    BigDecimal enterTheAmount = new BigDecimal(inputNum);           //输入金额
                    BigDecimal poundage = new BigDecimal(DataUtils.convertCurrencyFormat(Double.parseDouble(mPoundageValue)));  //处理过的手续费
                    String actualAmount = DataUtils.convertTwoDecimal(enterTheAmount.subtract(poundage).doubleValue());       //实际到账金额
                    mTxtActualAmount.setText(actualAmount);
                    mTxtServiceCharge.setText(DataUtils.convertCurrencyFormat(Double.parseDouble(mPoundageValue)));   //手续费
                } else if (mPoundageType == 1) {  //比例手续费
                    BigDecimal enterTheAmount = new BigDecimal(Double.parseDouble(mEditAmount.getText().toString()));
                    BigDecimal precision = new BigDecimal(mPoundageValue);
                    BigDecimal percent = new BigDecimal(100);
                    String serviceCharge = DataUtils.convertTwoDecimal(enterTheAmount.multiply(precision).divide(percent, 2).doubleValue());
                    mTxtServiceCharge.setText(serviceCharge);
                    BigDecimal poundage = new BigDecimal(DataUtils.convertTwoDecimal(enterTheAmount.multiply(precision).divide(percent, 2).doubleValue()));
                    String actualAmount = DataUtils.convertTwoDecimal(enterTheAmount.subtract(poundage).doubleValue());       //实际到账金额
                    mTxtActualAmount.setText(actualAmount);
                }
            }
        }
    }

    /**
     * 提现记录
     */
    private OnClickListener mTxtWithdrawRecordOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            MobclickAgent.onEvent(WithdrawActivity.this, UmengTouchType.RP112_1);

            Intent intent = new Intent(WithdrawActivity.this, WithdrawRecordActivity.class);
            startActivity(intent);
        }

    };

    private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mAmount = mEditAmount.getText().toString().trim();
            if (TextUtils.isEmpty(mAmount)) {
                AppUtils.showToast(WithdrawActivity.this, R.string.withdraw_amount_hint);
                return;
            }

            if (Utils.stringToDouble(mAmount) < 5) {
                AppUtils.showToast(WithdrawActivity.this, R.string.input_amount_tip);
                return;
            }

            mPayOrder.setPayType(PayType.WITHDRAW);
            mPayOrder.setLeftPay((long) (Double.parseDouble(mAmount) * 100));
            mPayOrder.setBalancePay(0);
            mPayOrder.setBankPay(0);
            //防止按钮被连续点击两次
            if (isTouch) {
                isTouch = false;
                showPayDialog();
            }

        }

    };

    private void showPayDialog() {
        mPayDialog = new PayDialog(WithdrawActivity.this, getDecorViewDialog());
        mPayDialog.setOnKeyListener(keyListener);
        mPayDialog.show();
    }

    protected View getDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayView.getInstance(mPayOrder, this, new OnPayListener() {

            @Override
            public void onSurePay(String password) {
                // TODO Auto-generated method stub
                mPayDialog.dismiss();
                isTouch = true;
                mPayDialog = null;

                withdraw(password);
            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub
                mPayDialog.dismiss();
                isTouch = true;
                mPayDialog = null;
            }
        }).getView();
    }

    private void withdraw(String password) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("transAmountStr", mAmount);
        datas.put("memberBankId", mMemberBankId);
        datas.put("transPwd", password);
        datas.put("iv", 1);


        mProgressDialog.show();
        DdApplication.getTradeManager().withdraw(datas, mOnWithdrawFinishedListener);
    }

    private OnWithdrawFinishedListener mOnWithdrawFinishedListener = new OnWithdrawFinishedListener() {

        @Override
        public void onWithdrawFinished(Response response, String getMoneyDate) {
            mProgressDialog.dismiss();

            if (response.isSuccess()) {
                MobclickAgent.onEvent(WithdrawActivity.this, UmengTouchType.RP112_2);
                Intent intent = new Intent(WithdrawActivity.this, WithdrawSuccessActivity.class);
                intent.putExtra("amount", mAmount);
                intent.putExtra("getMoneyDate", getMoneyDate);

                startActivityForResult(intent, WITHDRAW_SUCCESS);
            } else {
                PayUtils.dealPayError(WithdrawActivity.this, response, mOnDealPayErrorFinishedListener);
            }

        }

    };

    private OnDealPayErrorFinishedListener mOnDealPayErrorFinishedListener = new OnDealPayErrorFinishedListener() {

        @Override
        public void OnDealPayErrorFinished(String dealCode) {
            // TODO Auto-generated method stub
            if (TextUtils.equals(dealCode, "tryAgain")) {
                showPayDialog();
            }
        }

    };

    //温馨提示
    private OnClickListener mWarmPromptOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //温馨提示
            warmPromptDialog = new WarmPromptDialog(mWarmPromptList);
            warmPromptDialog.showDialog(WithdrawActivity.this);
        }
    };


    /**
     * dialog窗口返回键监听
     */
    private DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                isTouch = true;
            }
            return false;
        }
    };
}
