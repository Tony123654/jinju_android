package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.CustomerCenter;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Libra on 2017/9/20.
 */

public class CallCenterActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout mBtnContactUs;
    private TextView mTxtLogin;
    private TextView mTxtCharge;
    private TextView mTxtInvest;
    private TextView mTxtProduct;
    private TextView mTxtRedPacket;
    private Dialog mProgressDialog;
    private CustomerCenter mCustomerCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center);
        initView();
        getCustomerCenter();
    }


    private void initView() {

        mProgressDialog = AppUtils.createLoadingDialog(this);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtTitle.setText(R.string.call_center_title);
        TextView mTxtTitleRight = (TextView) findViewById(R.id.tv_title_right);
        mTxtTitleRight.setVisibility(View.VISIBLE);
        mTxtTitleRight.setText(R.string.call_center_feedback);

        mBtnContactUs = (LinearLayout) findViewById(R.id.btn_contact_us);
        mBtnContactUs.setOnClickListener(mBtnContactUsOnClickListener);
        mTxtLogin = (TextView) findViewById(R.id.txt_login);
        mTxtCharge = (TextView) findViewById(R.id.txt_charge);
        mTxtInvest = (TextView) findViewById(R.id.txt_invest);
        mTxtProduct = (TextView) findViewById(R.id.txt_product);
        mTxtRedPacket = (TextView) findViewById(R.id.txt_red_packet);
        mTxtLogin.setOnClickListener(this);
        mTxtCharge.setOnClickListener(this);
        mTxtInvest.setOnClickListener(this);
        mTxtProduct.setOnClickListener(this);
        mTxtRedPacket.setOnClickListener(this);

        mTxtTitleRight.setOnClickListener(mBtnFeedbackOnClickListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }

    /**
     * 联系我们
     */
    private View.OnClickListener mBtnContactUsOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(CallCenterActivity.this, UmengTouchType.RP105_11);

            // TODO Auto-generated method stub
            Intent intent = new Intent(CallCenterActivity.this, ContactUsActivity.class);
            startActivity(intent);
        }

    };
    /**
     * 帮助中心
     */
    private View.OnClickListener mBtnHelpOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(CallCenterActivity.this, UmengTouchType.RP105_10);

            // TODO Auto-generated method stub
            Intent intent = new Intent(CallCenterActivity.this, BaseJsBridgeWebViewActivity.class);
            intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH, AppConstant.HELP_CENTER);
            startActivity(intent);
        }

    };
    /**
     * 意见反馈
     */
    private View.OnClickListener mBtnFeedbackOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent mIntent = new Intent(CallCenterActivity.this,OpinionFeedbackActivity.class);
            startActivity(mIntent);
        }

    };

    private void getCustomerCenter() {
        mProgressDialog.show();
        DdApplication.getCommonManager().getCustomerCenter(mOnCustomerCenterFinishedListener);
    }
    private CommonManager.OnCustomerCenterFinishedListener mOnCustomerCenterFinishedListener =new CommonManager.OnCustomerCenterFinishedListener() {
        @Override
        public void onCustomerCenterFinished(Response response, CustomerCenter customerCenter) {
            mProgressDialog.dismiss();
            if (response.isSuccess()) {
                mCustomerCenter = customerCenter;
            }
        }
    };
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.txt_login:
                if (mCustomerCenter!=null) {
                    intent = new Intent(this,BaseJsBridgeWebViewActivity.class);
                    intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mCustomerCenter.getLogWithReg());
                    startActivity(intent);
                }
                break;
            case R.id.txt_charge:
                if (mCustomerCenter!=null) {
                    intent = new Intent(this,BaseJsBridgeWebViewActivity.class);
                    intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mCustomerCenter.getPayWithDraw());
                    startActivity(intent);
                }
                break;
            case R.id.txt_invest:
                if (mCustomerCenter!=null) {
                    intent = new Intent(this,BaseJsBridgeWebViewActivity.class);
                    intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mCustomerCenter.getSendWithCollect());
                    startActivity(intent);
                }
                break;
            case R.id.txt_product:
                if (mCustomerCenter!=null) {
                    intent = new Intent(this,BaseJsBridgeWebViewActivity.class);
                    intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mCustomerCenter.getProduct());
                    startActivity(intent);
                }
                break;
            case R.id.txt_red_packet:
                if (mCustomerCenter!=null) {
                    intent = new Intent(this,BaseJsBridgeWebViewActivity.class);
                    intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mCustomerCenter.getCouWithInst());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
