package com.jinju.android.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.help.UIHelper;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.VersionUtils;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Libra on 2017/8/3.
 */

public class ContactUsActivity extends BaseActivity {
    private ClipboardManager cm;
    private TextView mTxtServicePhone;
    private TextView mTxtWeChatCode;
    private TextView mTxtQqCode;
    private TextView mTxtOfficialAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txt_title = (TextView)findViewById(R.id.txt_title);
        txt_title.setText(R.string.call_center_contact_us);

        LinearLayout mBtnServicePhone = (LinearLayout) findViewById(R.id.btn_service_phone);
        mBtnServicePhone.setOnClickListener(mBtnServicePhoneOnClickListener);
        LinearLayout mBtnWeChat = (LinearLayout) findViewById(R.id.btn_weChat);
        mBtnWeChat.setOnClickListener(mBtnWeChatOnClickListener);
        LinearLayout mBtnQqCode = (LinearLayout) findViewById(R.id.btn_qq_code);
        mBtnQqCode.setOnClickListener(mBtnQqCodeOnClickListener);
        LinearLayout mBtnOfficialAccounts = (LinearLayout) findViewById(R.id.btn_official_accounts);
        mBtnOfficialAccounts.setOnClickListener(mBtnOfficialAccountsOnClickListener);

        mTxtServicePhone = (TextView) findViewById(R.id.txt_service_phone);
        mTxtWeChatCode = (TextView) findViewById(R.id.txt_weChat_code);
        mTxtQqCode = (TextView) findViewById(R.id.txt_qq_code);
        mTxtOfficialAccounts = (TextView) findViewById(R.id.txt_official_accounts);

        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //处理内存泄漏
        UMShareAPI.get(this).release();
    }

    private View.OnClickListener mBtnServicePhoneOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cm.setText(mTxtServicePhone.getText());
            UIHelper.callPhoneWindow(ContactUsActivity.this,"拨打电话",mTxtServicePhone.getText().toString());
        }
    };
    /**
     * 打开微信应用
     */
    private View.OnClickListener mBtnWeChatOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cm.setText(mTxtWeChatCode.getText().toString());
            final CustomRoundDialog customRoundDialog = new CustomRoundDialog(ContactUsActivity.this, 2);
            customRoundDialog.setContent(getString(R.string.contact_us_dialog_remind,"客服微信号","客服微信号"));
            customRoundDialog.setContentGravity(Gravity.CENTER);
            customRoundDialog.setPositiveButton("打开微信", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否安装微信
                    if (VersionUtils.isWeixinAvilible()) {
                        VersionUtils.openApp(ContactUsActivity.this,"com.tencent.mm");
                    } else {
                        AppUtils.showToast(ContactUsActivity.this,"您尚未安装微信");
                    }
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.show();

        }
    };
    /**
     * 打开QQ应用
     */
    private View.OnClickListener mBtnQqCodeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            cm.setText(mTxtQqCode.getText().toString());

            final CustomRoundDialog customRoundDialog = new CustomRoundDialog(ContactUsActivity.this, 2);
            customRoundDialog.setContent(getString(R.string.contact_us_dialog_remind,"客服QQ号","客服QQ号"));
            customRoundDialog.setContentGravity(Gravity.CENTER);
            customRoundDialog.setPositiveButton("打开QQ", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否安装微信
                    if (VersionUtils.isQQClientAvailable()) {
                        VersionUtils.openApp(ContactUsActivity.this,"com.tencent.mobileqq");
                    } else {
                        AppUtils.showToast(ContactUsActivity.this,"您尚未安装QQ");
                    }
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.show();

        }
    };
    /**
     * 微信公众号
     */
    private View.OnClickListener mBtnOfficialAccountsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cm.setText(getResources().getString(R.string.contact_us_official_accounts_id));

            final CustomRoundDialog customRoundDialog = new CustomRoundDialog(ContactUsActivity.this, 2);
            customRoundDialog.setContent(getString(R.string.contact_us_dialog_remind,"微信公众号","微信公众号"));
            customRoundDialog.setContentGravity(Gravity.CENTER);
            customRoundDialog.setPositiveButton("打开微信", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断是否安装微信
                    if (VersionUtils.isWeixinAvilible()) {
                        VersionUtils.openApp(ContactUsActivity.this,"com.tencent.mm");
                    } else {
                        AppUtils.showToast(ContactUsActivity.this,"您尚未安装微信");
                    }
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.show();

        }
    };

}
