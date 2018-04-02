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
import com.jinju.android.api.FastPay;
import com.jinju.android.api.PayOrder;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.PayType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.PayDialog;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.PayUtils;
import com.jinju.android.util.Utils;
import com.jinju.android.widget.PayView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 充值
 */
public class ChargeActivity extends BaseActivity {

    private String mFastPayAmount;
    private TextView mTxtBankName;
    private TextView mTxtBankCardNum;
    private TextView mTxtBankCardTips;
    private TextView mTxtChargeRecord;
    private TextView mTxtChargeCanUseBalance;

    private EditText mEditFastPayAmount;
    private ImageView mImgBankLogo;
    private Button mBtnConfirm;

    private FastPay mFastPay;
    private PayOrder mPayOrder;
    private PayDialog mPayDialog;
    private Dialog mProgressDialog;
    private Boolean isTouch = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mImgBackOnClickListener);

        mImgBankLogo = (ImageView) findViewById(R.id.img_bank_logo);
        mTxtBankName = (TextView) findViewById(R.id.txt_bank_name);
        mTxtBankCardNum = (TextView) findViewById(R.id.txt_bank_card_num);
        mTxtBankCardTips = (TextView) findViewById(R.id.txt_bank_card_tips);

        mEditFastPayAmount = (EditText) findViewById(R.id.edit_fast_pay_amount);
        mEditFastPayAmount.addTextChangedListener(mTextChangedListener);
        //设置EditText hint字体大小
        SpannableString ss = new SpannableString("请输入金额（建议单笔充值100元以上）");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEditFastPayAmount.setHint(new SpannedString(ss));
        //可用余额
        mTxtChargeCanUseBalance = (TextView) findViewById(R.id.txt_can_use_balance);

        mTxtChargeRecord = (TextView) findViewById(R.id.txt_charge_record);
        mTxtChargeRecord.setOnClickListener(mTxtChargeRecordOnClickListener);

        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(mBtnConfirmOnClickListener);

        setConfirmButton(false);

        TextView txtSafeMessage = (TextView) findViewById(R.id.txt_safe_message);
        String message = DdApplication.getConfigManager().getSecurityMessage();
        txtSafeMessage.setText(message);

        mPayOrder = new PayOrder();

        mProgressDialog = AppUtils.createLoadingDialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initFastPay();
    }

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
                    mEditFastPayAmount.setText(s);
                    mEditFastPayAmount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEditFastPayAmount.setText(s);
                mEditFastPayAmount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEditFastPayAmount.setText(s.subSequence(0, 1));
                    mEditFastPayAmount.setSelection(1);
                    return;
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
        }
    };

    private void setConfirmButton(boolean enable) {
        if (enable) {
            mBtnConfirm.setBackgroundResource(R.drawable.btn_red_solid_radius_bg);
            mBtnConfirm.setClickable(true);
        } else {
            mBtnConfirm.setBackgroundResource(R.drawable.button_radius_disabled);
            mBtnConfirm.setClickable(false);
        }
    }

    @Override
    public void onDestroy() {
        mProgressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        List<View> viewList = new ArrayList<View>();
        viewList.add(mEditFastPayAmount);

        AppUtils.checkNeedHideSoftInput(this, (int) event.getX(), (int) event.getY(), viewList);

        return super.dispatchTouchEvent(event);
    }

    public OnClickListener mImgBackOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            finish();
        }

    };

    private void initFastPay() {
        Map<String, Object> datas = new HashMap<String, Object>();

        mProgressDialog.show();
        DdApplication.getUserManager().initFastPay(datas, mOnInitFastPayFinishedListener);
    }

    private UserManager.OnInitFastPayFinishedListener mOnInitFastPayFinishedListener = new UserManager.OnInitFastPayFinishedListener() {

        @Override
        public void OnInitFastPayFinished(Response response, FastPay fastPay) {
            // TODO Auto-generated method stub
            if (response.isSuccess()) {

                mFastPay = fastPay;

                mTxtChargeCanUseBalance.setText(Html.fromHtml(getString(R.string.charge_can_use_balance, fastPay.getCanUseBalance())));
                mTxtBankCardTips.setText(getString(R.string.bank_card_tips,fastPay.getOnceLimit()/1000000,fastPay.getDayLimit()/1000000));

                String imageUrl = fastPay.getLogoPath();
                ImageUtils.displayImage(mImgBankLogo, imageUrl);
                mTxtBankName.setText(fastPay.getBankName());
                mTxtBankCardNum.setText(getString(R.string.bank_card_tail, fastPay.getBankCardNum()));

            } else {
                AppUtils.handleErrorResponse(ChargeActivity.this, response);
            }
            mProgressDialog.dismiss();
        }

    };

    private OnClickListener mTxtChargeRecordOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            MobclickAgent.onEvent(ChargeActivity.this, UmengTouchType.RP113_1);

            Intent intent = new Intent(ChargeActivity.this, ChargeRecordActivity.class);
            startActivity(intent);
        }

    };

    private OnClickListener mBtnConfirmOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mFastPayAmount = mEditFastPayAmount.getText().toString().trim();

            if (TextUtils.isEmpty(mFastPayAmount)) {
                AppUtils.showToast(ChargeActivity.this, R.string.input_pay_amount);
                return;
            }

            if (!DataUtils.isDouble(mFastPayAmount)) {
                AppUtils.showToast(ChargeActivity.this, R.string.pay_amount_error);
                return;
            }

            double amount = Utils.stringToDouble(mFastPayAmount);
            if ((long) (amount * 100) < 500) {
                AppUtils.showToast(ChargeActivity.this, getString(R.string.pay_amount_min_error));
                return;
            }
            if (mFastPay != null) {
                if ((long) (amount * 100) > mFastPay.getOnceLimit()) {
                    AppUtils.showToast(ChargeActivity.this, getString(R.string.pay_amount_max_error, DataUtils.convertToYuan(mFastPay.getOnceLimit())));
                    return;
                }
            }

            mPayOrder.setPayType(PayType.CHARGE);
            mPayOrder.setLeftPay((long) (Double.parseDouble(mFastPayAmount) * 100));
            mPayOrder.setBalancePay(0);
            mPayOrder.setBankPay(0);
            //防止按钮被连续点击两次
            if (isTouch) {
                isTouch = false;
                showPayDialog();
            }

        }

    };

    protected void showPayDialog() {
        mPayDialog = new PayDialog(ChargeActivity.this, getDecorViewDialog());
        mPayDialog.setOnKeyListener(keyListener);
        mPayDialog.show();
    }

    protected View getDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayView.getInstance(mPayOrder, this, new PayView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                // TODO Auto-generated method stub
                mPayDialog.dismiss();
                isTouch = true;
                mPayDialog = null;

                fastPay(password);
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

    private void fastPay(String password) {
        if (mFastPay != null) {
            Map<String, Object> datas = new HashMap<String, Object>();
            datas.put("memberBankId", mFastPay.getMemberBankId());
            datas.put("validateCode", password);
            datas.put("amountStr", mFastPayAmount);
            datas.put("iv", 1);

            mProgressDialog.show();
            DdApplication.getUserManager().fastPay(datas, mOnFastPayFinishedListener);
        }

    }

    private UserManager.OnFastPayFinishedListener mOnFastPayFinishedListener = new UserManager.OnFastPayFinishedListener() {

        @Override
        public void OnFastPayFinished(Response response) {
            // TODO Auto-generated method stub
            mProgressDialog.dismiss();
            if (response.isSuccess()) {
                MobclickAgent.onEvent(ChargeActivity.this, UmengTouchType.RP113_2);

                Intent intent = new Intent(ChargeActivity.this, ChargeSuccessActivity.class);
                intent.putExtra("amount", mFastPayAmount);
                startActivity(intent);
            } else {
                PayUtils.dealPayError(ChargeActivity.this, response, mOnDealPayErrorFinishedListener);
            }
        }

    };

    private PayUtils.OnDealPayErrorFinishedListener mOnDealPayErrorFinishedListener = new PayUtils.OnDealPayErrorFinishedListener() {

        @Override
        public void OnDealPayErrorFinished(String dealCode) {
            // TODO Auto-generated method stub
            if (TextUtils.equals(dealCode, "tryAgain")) {
                showPayDialog();
            }
        }

    };
    /**
     * dialog窗口返回键监听
     */
    private DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                isTouch = true;
            }
            return false;
        }
    };
}
