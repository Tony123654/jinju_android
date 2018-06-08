package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Account;
import com.jinju.android.api.Response;
import com.jinju.android.api.VersionUpdate;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.BroadcastType;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.dialog.GuideDialog;
import com.jinju.android.dialog.UpdateDialog;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.manager.UserManager.OnGetAccountFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 总资产界面
 */
public class ProfileActivity extends BaseActivity implements OnClickListener {
    private TextView mTvVersionUpdate;
    private TextView mTxtMyRedpacketDesc;
    private TextView mTxtCanUseBalance;
    private TextView mTxtTotalAsset;
    private TextView mTvTodayIncome;
    private TextView mTxtTotalIncome;
    private TextView mBtnWithdraw;
    private TextView mBtnCharge;
    private TextView mTickName;
    private TextView mYuan;

    private RelativeLayout mBtnAbout;
    private RelativeLayout mLayoutTotalAsset;
    private RelativeLayout mBtnMessage;
    private LinearLayout rlMyLoan;
    private RelativeLayout rlAccountList;
    private RelativeLayout rlAutomaticBid;
    private RelativeLayout rlMyInvitation;
    private LinearLayout rlSettings;
    private LinearLayout mBtnCoupon;
    private LinearLayout mBtnBank;
    private RelativeLayout mBtnCallCenter;
    private RelativeLayout rlVersionUpdate;

    private ImageView mImgMessage;
    private ImageView mImgMoneyVisible;
    private ImageView mImgMyCoupon;
    private ImageView mImgVersionUpdate;

    private ConfigManager mConfigManager;
    private Account mAccount;
    private int mMemberStep;
    private Intent mIntent; //判断是否是第一次注册
    private boolean isVisible;
    private Dialog mProgressDialog;
    private UpdateDialog mUpdateDialog;
    private VersionUpdate versionUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //接收广播与MainActivity关联
        mIntent = new Intent(BroadcastType.MAIN_BROADCASTRECEIVER);

        //公告
        mBtnMessage = (RelativeLayout) findViewById(R.id.btn_message);
        mBtnMessage.setOnClickListener(mBtnMessageOnClickListener);

        mImgMessage = (ImageView) findViewById(R.id.img_message);

        mTxtTotalAsset = (TextView) findViewById(R.id.txt_total_asset);

        mTxtCanUseBalance = (TextView) findViewById(R.id.txt_can_use_balance);

        mTvTodayIncome = (TextView) findViewById(R.id.txt_today_income);
        mTxtTotalIncome = (TextView) findViewById(R.id.txt_total_income);
        //提现
        mBtnWithdraw = (TextView) findViewById(R.id.btn_withdraw);
        mBtnWithdraw.setOnClickListener(mBtnWithdrawOnClickListener);

        //充值
        mBtnCharge = (TextView) findViewById(R.id.btn_charge);
        mBtnCharge.setOnClickListener(mBtnChargeOnClickListener);

        mBtnCoupon = (LinearLayout) findViewById(R.id.btn_coupon);
        mBtnCoupon.setOnClickListener(mBtnCouponOnClickListener);
//        mTxtMyRedpacketDesc = (TextView) findViewById(R.id.txt_my_redpacket_desc);
//        mImgMyCoupon = (ImageView) findViewById(R.id.img_my_coupon);

        mBtnBank = (LinearLayout) findViewById(R.id.btn_bank);
        mBtnBank.setOnClickListener(mBtnBankOnClickListener);

//        mBtnAbout = (RelativeLayout) findViewById(R.id.btn_about);
//        mBtnAbout.setOnClickListener(mBtnAboutOnClickListener);

//        mBtnCallCenter = (RelativeLayout) findViewById(R.id.btn_call_center);
//        mBtnCallCenter.setOnClickListener(mBtnCallCenterOnClickListener);

        //头像设置
        CircleImageView mImgHead = (CircleImageView) findViewById(R.id.img_head);
        mImgHead.setOnClickListener(mImgHeadOnClickListener);
        mImgMoneyVisible = (ImageView) findViewById(R.id.img_money_visible);
        mImgMoneyVisible.setOnClickListener(mImgMoneyVisibleOnClickListener);
        mConfigManager = DdApplication.getConfigManager();
        mProgressDialog = AppUtils.createLoadingDialog(this);
        initViews();

//        setVersionHint();
    }

    private void initViews() {
        rlSettings = (LinearLayout) findViewById(R.id.rl_settings);
        rlMyLoan = (LinearLayout) findViewById(R.id.rl_my_loan);
//        rlAccountList = (RelativeLayout) findViewById(R.id.rl_account_list);
//        rlAutomaticBid = (RelativeLayout) findViewById(R.id.rl_automatic_bid);
//        rlMyInvitation = (RelativeLayout) findViewById(R.id.rl_my_invitation);
//        rlVersionUpdate = (RelativeLayout) findViewById(R.id.rl_version_updating);
        mLayoutTotalAsset = (RelativeLayout) findViewById(R.id.layout_total_asset);

        mYuan = (TextView) findViewById(R.id.yuan);
        mTickName = (TextView) findViewById(R.id.tv_nickname);
//        mImgVersionUpdate = (ImageView) findViewById(R.id.img_version_update);
//        mTvVersionUpdate = (TextView) findViewById(R.id.tv_version_update_desc);

        rlSettings.setOnClickListener(this);
        rlMyLoan.setOnClickListener(this);
//        rlAccountList.setOnClickListener(this);
//        rlAutomaticBid.setOnClickListener(this);
//        rlMyInvitation.setOnClickListener(this);
//        rlVersionUpdate.setOnClickListener(this);
        mLayoutTotalAsset.setOnClickListener(this);

    }

    //onNewIntent 是在singleTask模式下，且存在实例才会去调用
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//		String operate = getIntent().getStringExtra("operate");
//		//切换tab时调用
//		if (TextUtils.equals(operate, "refresh")) {
//			updateAccount();
//		}
//		updateAccount();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateAccount();
    }

    @Override
    protected void onDestroy() {
        if (mUpdateDialog != null) {
            mUpdateDialog.dismiss();
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }


    private OnClickListener mImgMoneyVisibleOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            isVisible = mConfigManager.getMoneyVisible();
            if (isVisible) {
                mImgMoneyVisible.setImageResource(R.mipmap.icon_money_gone);
                mConfigManager.setMoneyVisible(false);

                mTxtTotalAsset.setText("*****");
                mTvTodayIncome.setText("*****");
                mTxtTotalIncome.setText("*****");
                mTxtCanUseBalance.setText("");
                mYuan.setText("*****");

            } else {
                mImgMoneyVisible.setImageResource(R.mipmap.icon_money_visible);
                mConfigManager.setMoneyVisible(true);
                if (mAccount != null) {
                    mTxtTotalAsset.setText(DataUtils.convertCurrencyFormat(mAccount.getNetAssets()));
                    mTvTodayIncome.setText(DataUtils.convertCurrencyFormat(mAccount.getYesterdayIncome()));
                    mTxtTotalIncome.setText(DataUtils.convertCurrencyFormat(mAccount.getTotalIncome()));
                    mTxtCanUseBalance.setText(DataUtils.convertCurrencyFormat(mAccount.getCanUseBalance()));    //可用余额
                    mYuan.setText("元");
                }

            }
        }

    };

    /**
     * 提现
     */
    private OnClickListener mBtnWithdrawOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            if (MemberStep.COMPLETE == mMemberStep) {
                MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_4);

                Intent intent = new Intent(ProfileActivity.this, WithdrawActivity.class);
                startActivity(intent);
            } else {
                final CustomRoundDialog customRoundDialog = new CustomRoundDialog(ProfileActivity.this, 2);
                if (MemberStep.SET_TRANS_PWD == mMemberStep) {//设置手机支付密码
                    customRoundDialog.setContent(getString(R.string.set_pay_password));
                } else {
                    customRoundDialog.setContent(getString(R.string.fastpay_no_bind));
                }
                customRoundDialog.setPositiveButton(R.string.confirm, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (mMemberStep) {
                            case MemberStep.VERIFY_BANK_CARD: {
                                Intent intent = new Intent(ProfileActivity.this, VerifyBankCardActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case MemberStep.SEND_CODE: {
                                Intent intent = new Intent(ProfileActivity.this, VerifyCodeActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case MemberStep.SET_TRANS_PWD: {
                                Intent intent = new Intent(ProfileActivity.this, WithdrawPasswordSetActivity.class);
                                startActivity(intent);
                                break;
                            }
                        }
                        customRoundDialog.cancel();
                    }
                });
                customRoundDialog.setNegativeButton(R.string.cancel, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customRoundDialog.cancel();
                    }
                });
                customRoundDialog.show();
            }
        }
    };

    private OnClickListener mBtnChargeOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            clickCharge();
        }

    };

    /**
     * 充值
     */
    private void clickCharge() {
        if (MemberStep.COMPLETE == mMemberStep) {
            MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_5);

            Intent intent = new Intent(ProfileActivity.this, ChargeActivity.class);
            startActivity(intent);
        } else {
            final CustomRoundDialog customRoundDialog = new CustomRoundDialog(ProfileActivity.this, 2);
            if (MemberStep.SET_TRANS_PWD == mMemberStep) {//设置手机支付密码
                customRoundDialog.setContent(getString(R.string.set_pay_password));
            } else {
                customRoundDialog.setContent(getString(R.string.fastpay_no_bind));
            }
            customRoundDialog.setPositiveButton(R.string.confirm, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mMemberStep) {
                        case MemberStep.VERIFY_BANK_CARD: {
                            Intent intent = new Intent(ProfileActivity.this, VerifyBankCardActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case MemberStep.SEND_CODE: {
                            Intent intent = new Intent(ProfileActivity.this, VerifyCodeActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case MemberStep.SET_TRANS_PWD: {
                            Intent intent = new Intent(ProfileActivity.this, WithdrawPasswordSetActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.setNegativeButton(R.string.cancel, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    customRoundDialog.cancel();
                }
            });
            customRoundDialog.show();
        }
    }

    //点击头像
    private OnClickListener mImgHeadOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP106_1);

            Intent intent = new Intent(ProfileActivity.this, InfoActivity.class);
            startActivity(intent);
        }

    };

    /**
     * 红包
     */
    private OnClickListener mBtnCouponOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_8);

            Intent intent = new Intent(ProfileActivity.this, RedPacketActivity.class);
            intent.putExtra("isValid", mAccount.getIsValid());
            startActivity(intent);
//            mImgMyCoupon.setImageResource(R.mipmap.icon_my_coupon);
        }

    };
    /**
     * 我的银行卡
     */
    private OnClickListener mBtnBankOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_9);

            Intent intent = new Intent(ProfileActivity.this, ManageBankCardActivity.class);
            startActivity(intent);
        }

    };

    /**
     * 关于我们
     */
//    private OnClickListener mBtnAboutOnClickListener = new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//
//            Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
//            startActivity(intent);
//
//        }
//
//    };
    /**
     * 客服中心
     */
//    private OnClickListener mBtnCallCenterOnClickListener = new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(ProfileActivity.this, CallCenterActivity.class);
//            startActivity(intent);
//        }
//
//    };

    /**
     * 公告
     */
    private OnClickListener mBtnMessageOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_2);

            Intent intent = new Intent(ProfileActivity.this, NoticeActivity.class);
            startActivity(intent);
        }

    };


    private void updateAccount() {
        if (DdApplication.getConfigManager().isLogined()) {
            getAccount();
        }

    }

    private void getAccount() {
        mProgressDialog.show();
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("iv", VersionUtils.getVersionName());
        DdApplication.getUserManager().getAccount(datas, mOnGetAccountFinishedListener);
    }

    private OnGetAccountFinishedListener mOnGetAccountFinishedListener = new OnGetAccountFinishedListener() {

        @Override
        public void onGetAccountFinished(Response response, Account account) {
            if (response.isSuccess()) {
                mAccount = account;

                mTickName.setText(mAccount.getAccount());
                if (mAccount.getNewMessageFlag() > 0) {
                    mImgMessage.setImageResource(R.mipmap.icon_message);
                } else {
                    mImgMessage.setImageResource(R.mipmap.icon_message_normal);
                }

                mMemberStep = account.getMemberStep();
                isVisible = mConfigManager.getMoneyVisible();

                if (isVisible) {
                    mImgMoneyVisible.setImageResource(R.mipmap.icon_money_visible);

                    mTvTodayIncome.setText(DataUtils.convertCurrencyFormat(account.getYesterdayIncome()));      //昨日收益
                    mTxtTotalIncome.setText(DataUtils.convertCurrencyFormat(account.getTotalIncome()));         //累计收益
                    mTxtTotalAsset.setText(DataUtils.convertCurrencyFormat(account.getNetAssets()));          //我的资产
                    mTxtCanUseBalance.setText(DataUtils.convertCurrencyFormat(account.getCanUseBalance()));     //可用余额
                    mYuan.setText("元");
                } else {
                    mImgMoneyVisible.setImageResource(R.mipmap.icon_money_gone);
                    mTxtTotalAsset.setText("*****");
                    mTvTodayIncome.setText("*****");
                    mTxtTotalIncome.setText("*****");
                    mTxtCanUseBalance.setText("");
                    mYuan.setText("*****");
                }

                //红包提示
                String redPacketDesc = account.getRedPacketDesc();
                if (!TextUtils.isEmpty(redPacketDesc)) {
//                    mTxtMyRedpacketDesc.setText(Html.fromHtml(TextShowUtils.oneColorHtmlText(redPacketDesc)));
//                    mImgMyCoupon.setImageResource(R.mipmap.icon_my_coupon_desc);    //红包点
                    mIntent.putExtra("redpacket", true);
                } else {
//                    mTxtMyRedpacketDesc.setText("");
                    mIntent.putExtra("redpacket", false);
//                    mImgMyCoupon.setImageResource(R.mipmap.icon_my_coupon);
                }


                //发送广播
                sendBroadcast(mIntent);
                //判断是否是第一次注册，唤起引导窗口
                boolean isFirstRegister = mConfigManager.getFirstRegister();
                if (isFirstRegister) {
                    new GuideDialog(ProfileActivity.this);   //新手红包
                    mConfigManager.setFirstRegister(false);          //将第一次注册状态设置成false
                }

            } else {
                AppUtils.handleErrorResponse(ProfileActivity.this, response);
            }

            mProgressDialog.dismiss();
        }

    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_total_asset:   //资产视图
                MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_3);
                Intent intent = new Intent(ProfileActivity.this, MyAssetActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_my_loan:  //我的理财
                MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_6);
                Intent mfIntent = new Intent(ProfileActivity.this, MyFinancialActivity.class);
                startActivity(mfIntent);
                break;

//            case R.id.rl_account_list:  //资金流水
//                MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP111_1);
//                Intent alIntent = new Intent(ProfileActivity.this, FundAccountActivity.class);
//                startActivity(alIntent);
//                break;
//
//            case R.id.rl_automatic_bid: //自动投标
//                MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_7);
//                Intent abIntent = new Intent(ProfileActivity.this, AutomateActivity.class);
//                startActivity(abIntent);
//                break;
//
//            case R.id.rl_my_invitation: //我的邀请
//                if (mAccount.getInviteRecord() != null & mAccount.getInviteRecord().length() > 0) {
//                    Intent miIntent = new Intent(ProfileActivity.this, BaseJsBridgeWebViewActivity.class);
//                    miIntent.putExtra("url", mAccount.getInviteRecord());
//                    startActivity(miIntent);
//                }
//                break;

            case R.id.rl_settings:      //设置
                MobclickAgent.onEvent(ProfileActivity.this, UmengTouchType.RP105_1);
                Intent setIntent = new Intent(ProfileActivity.this, SettingActivity.class);
                startActivity(setIntent);
                break;

//            case R.id.rl_version_updating:      //版本升级
//                if (mConfigManager.getVersionUpdate()) {
//                    if (versionUpdate != null) {
//                        //版本更新弹窗
//                        String downLoadUrl = versionUpdate.getDownloadUrl();
//                        String downLoadContent = versionUpdate.getContent();
//                        String downLoadVersion = versionUpdate.getVersion();
//                        String downLoadFileSize = versionUpdate.getFilesize();
//                        String downLoadForceUpdate = versionUpdate.getForceUpdate();
//                        if (mUpdateDialog == null) {
//                            mUpdateDialog = new UpdateDialog(ProfileActivity.this);
//                            mUpdateDialog.initUpdateDialog(downLoadUrl, downLoadVersion, downLoadContent, downLoadForceUpdate, downLoadFileSize);
//                        }
//                        mUpdateDialog.show();
//                    }
//                } else {
//                    AppUtils.showToast(this, "当前已是最新版本");
//                }
//
//                break;

            default:

                break;
        }
    }

//    private void setVersionHint() {
//        List<VersionUpdate> versionUpdateList = mConfigManager.getVersionUpdateList();
//
//        if (versionUpdateList != null) {
//            versionUpdate = versionUpdateList.get(0);
//            //需要更新的版本号
//            int version = Integer.valueOf(versionUpdate.getVersion().replaceAll("\\.", ""));
//            //本地的版本号
//            int localVersion = Integer.valueOf(DdApplication.getVersionName().replaceAll("\\.", ""));
//            if (version <= localVersion) {
//                mConfigManager.setVersionUpdate(false);
//            }
//        }
//        if (mConfigManager.getVersionUpdate()) {
//            if (versionUpdateList != null) {
//                mImgVersionUpdate.setImageResource(R.mipmap.icon_version_update_dot);
//                mTvVersionUpdate.setText("发现新版本V" + versionUpdate.getVersion());
//            }
//        } else {
//            mTvVersionUpdate.setText("当前已是最新版本");
//            mImgVersionUpdate.setImageResource(R.mipmap.icon_version_update);
//        }
//    }
}