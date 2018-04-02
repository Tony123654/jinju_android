package com.jinju.android.activity;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.SimpleFragmentPagerAdapter;
import com.jinju.android.api.ActivityAwardInfo;
import com.jinju.android.api.FinancialDetail;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.constant.ShowStatus;
import com.jinju.android.constant.SrcType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.fragment.InvestRecordFragment;
import com.jinju.android.fragment.ProductIntroduceFragment;
import com.jinju.android.fragment.SafetyGuaranteeFragment;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.util.NumberUtils;
import com.jinju.android.util.TextShowUtils;
import com.jinju.android.util.Utils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.widget.MagicProgressBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Libra on 2017/11/2.
 * 新的 理财详情页
 */

public class FinancialDetailActivity extends BaseFragmentActivity implements InvestRecordFragment.InvestRecordListener {

    private ProductIntroduceFragment mProductFragment;
    private SafetyGuaranteeFragment mSafetyFragment;
    private InvestRecordFragment mInvestRecordFragment;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ArrayList<Fragment> list_fragment=new ArrayList<>();     //定义要装fragment的列表
    private ArrayList<String> list_title=new ArrayList<>();

    private Dialog mProgressDialog;
    private TextView mTxtTitle;
    private TextView mTxtYearInterest;
    private TextView mTxtAddInterest;
    private TextView mTxtLoanPeriod;
    private TextView mTxtLeastAmount;
    private TextView mTxtProductAmount;
    private TextView mTxtHasFundsAmount;
    private MagicProgressBar mMagicProgressBar;
    private ValueAnimator progressAnimator;
    private TextView txtProgress;
    private TextView mTxtReturnDate;
    private TextView mTxtEndDate;
    private TextView mTxtPayType;
    private TextView mTxtTodayDate;
    private LinearLayout mBtnActivityIntroduce;
    private TextView mTxtBigCup;
    private TextView mTxtEndgameCup;
    private TextView txtCountRecord;
    //购买按钮
    private RelativeLayout mBtnFinancial;
    private TextView mTxtFinancial;
    private ImageView mIvCountDown;

    private long mFinancialId;
    private FinancialDetail mFinancialDetail;
    private String mShowStatus;
    private long mBeginDuration;
    private AnimationDrawable animationDrawable;
    private ImageView mImgAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_financial_detail);
        mFinancialId = getIntent().getLongExtra("id", 0);
        mProgressDialog = AppUtils.createLoadingDialog(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFinancialDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
        if (progressAnimator!=null) {
            progressAnimator.cancel();
        }
        mHandler.removeCallbacks(runnable);
        animationDrawable.stop();

    }

    //初始化界面
    private void initView() {

        initCommonView();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTxtTitle = (TextView) findViewById(R.id.txt_title);

        Bundle bundle = new Bundle();
        bundle.putLong("financialId", mFinancialId);

        mProductFragment = new ProductIntroduceFragment();
        mProductFragment.setArguments(bundle);
        mSafetyFragment = new SafetyGuaranteeFragment();
        mSafetyFragment.setArguments(bundle);
        mInvestRecordFragment = new InvestRecordFragment();
        mInvestRecordFragment.setArguments(bundle);

        list_fragment.add(mProductFragment);
        list_fragment.add(mSafetyFragment);
        list_fragment.add(mInvestRecordFragment);

        list_title.add("项目详情");
        list_title.add("安全保障");
        list_title.add("投资记录");
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this,list_fragment,list_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);//滑动条颜色
        tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);//标题字体颜色,左边是未选中，右边是选中
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void  initCommonView() {


        mTxtHasFundsAmount = (TextView) findViewById(R.id.txt_has_funds_amount);
        mTxtLoanPeriod = (TextView) findViewById(R.id.txt_loan_period);
        mTxtYearInterest = (TextView) findViewById(R.id.txt_year_interest);
        mTxtAddInterest = (TextView) findViewById(R.id.txt_add_interest);
        mTxtLeastAmount = (TextView) findViewById(R.id.txt_least_amount);
        mTxtProductAmount = (TextView) findViewById(R.id.txt_product_amount);
        mMagicProgressBar = (MagicProgressBar) findViewById(R.id.progress_bar);
        txtProgress = (TextView) findViewById(R.id.txt_progress);
        mTxtTodayDate = (TextView) findViewById(R.id.txt_today_date);
        mTxtEndDate = (TextView) findViewById(R.id.txt_end_date);
        mTxtReturnDate = (TextView) findViewById(R.id.txt_return_date);
        mTxtPayType = (TextView) findViewById(R.id.txt_pay_type);
        mBtnActivityIntroduce = (LinearLayout) findViewById(R.id.btn_activity_introduce);
        mBtnActivityIntroduce.setOnClickListener(mBtnActivityIntroduceOnClickListener);
        mTxtBigCup = (TextView) findViewById(R.id.txt_big_cup);
        mTxtEndgameCup = (TextView) findViewById(R.id.txt_endgame_cup);
        txtCountRecord = (TextView)findViewById(R.id.txt_count_record);

        mBtnFinancial = (RelativeLayout) findViewById(R.id.btn_financial);
        mTxtFinancial = (TextView) findViewById(R.id.txt_financial);
        mIvCountDown = (ImageView) findViewById(R.id.iv_count_down);
        mBtnFinancial.setOnClickListener(mBtnFinancialOnClickListener);
        //包标返现
        mImgAnimation = (ImageView) findViewById(R.id.img_animation);
        mImgAnimation.setImageResource(R.drawable.detail_animation);
        animationDrawable = (AnimationDrawable) mImgAnimation.getDrawable();
        mImgAnimation.setOnClickListener(mImgAnimationOnClickListener);
    }
    private void getFinancialDetail() {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("financialId", mFinancialId);
        datas.put("iv", DdApplication.getVersionName());

        mProgressDialog.show();
        DdApplication.getLoanManager().getFinancialDetail(datas, mOnGetFinancialDetailFinishedListener);
    }
    private LoanManager.OnGetFinancialDetailFinishedListener mOnGetFinancialDetailFinishedListener = new LoanManager.OnGetFinancialDetailFinishedListener() {

        @Override
        public void onGetFinancialDetailFinished(Response response, FinancialDetail financialDetail) {

            mProgressDialog.dismiss();
            if (response.isSuccess()) {
                mFinancialDetail = financialDetail;
                //是否包标
                if (financialDetail.isJoinActivity0011()) {
                    mImgAnimation.setVisibility(View.VISIBLE);
                    animationDrawable.start();
                }else {
                    mImgAnimation.setVisibility(View.GONE);
                    animationDrawable.stop();
                }
                mTxtTitle.setText(financialDetail.getName());

                mTxtYearInterest.setText(financialDetail.getActualInterestRate());
                if (!TextUtils.isEmpty(financialDetail.getSubsidyInterestRate())) {
                    double SubsidyInterestRate = Double.parseDouble(financialDetail.getSubsidyInterestRate());
                    if (SubsidyInterestRate > 0) {
                        mTxtAddInterest.setText("+"+financialDetail.getSubsidyInterestRate()+"%");
                    }
                }
                mTxtLoanPeriod.setText(Html.fromHtml(getString(R.string.financial_loan_period, financialDetail.getLoanPeriodDays())));
                mTxtLeastAmount.setText(Html.fromHtml(getString(R.string.financial_multiple, financialDetail.getLeastBuy()/100)));

                float percent = Float.parseFloat(NumberUtils.floatTwoStr((float)financialDetail.getHasPercent()/100));
                //滑动进度条
                mMagicProgressBar.setPercent(percent);
                setAnimation(percent);

                setCountRecord(mFinancialDetail.getOrderCount());
                mTxtProductAmount.setText(Html.fromHtml(getString(R.string.financial_product_amount, DataUtils.convertCurrencyFormat(financialDetail.getProductAmount()))));
                mTxtHasFundsAmount.setText(Html.fromHtml(getString(R.string.financial_has_funds_amount, DataUtils.convertCurrencyFormat(financialDetail.getHasFundsAmount()))));
                mTxtTodayDate.setText(financialDetail.getToday());
                mTxtEndDate.setText(financialDetail.getEndDate());
                mTxtReturnDate.setText(financialDetail.getFundReturnDate());
                mTxtPayType.setText(financialDetail.getFundReturnTypeDesc());

                ActivityAwardInfo mAwardInfoList = financialDetail.getActivityAwardInfo();
                mTxtBigCup.setText(Html.fromHtml(TextShowUtils.colorHtmlText(mAwardInfoList.getRichDesc())));
                mTxtEndgameCup.setText(Html.fromHtml(TextShowUtils.colorHtmlText(mAwardInfoList.getEndingDesc())));

                //购买按钮
                mShowStatus = financialDetail.getShowStatus();

                if(TextUtils.equals(mShowStatus, ShowStatus.FINANCIAL_NOT_START)){

                    mBeginDuration = mFinancialDetail.getBeginDuration();
                    //开始执行首标倒计时
                    mHandler.postDelayed(runnable,0);
                } else if(TextUtils.equals(mShowStatus, ShowStatus.FINANCIAL_NOW)){
                    setFinancialBtn(getString(R.string.financial_now), true, mShowStatus);
                } else if(TextUtils.equals(mShowStatus, ShowStatus.LAON_COMPLETE)){
                    setFinancialBtn(getString(R.string.loan_complete), false, mShowStatus);
                } else if(TextUtils.equals(mShowStatus, ShowStatus.FINANCIAL_FINISHED)){
                    setFinancialBtn(getString(R.string.financial_finished), false, mShowStatus);
                }
            }else {
                AppUtils.handleErrorResponse(FinancialDetailActivity.this, response);
            }
        }
    };
    /**
     * 活动介绍
     */
    private View.OnClickListener mBtnActivityIntroduceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mFinancialDetail!=null && !TextUtils.isEmpty(mFinancialDetail.getInvestUrl())) {
                Intent intent =  new Intent(FinancialDetailActivity.this, BaseJsBridgeWebViewActivity.class);
                intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mFinancialDetail.getInvestUrl());
                startActivity(intent);
            }

        }
    };
    /**
     * 包标返现
     */
    private View.OnClickListener mImgAnimationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mFinancialDetail!=null && !TextUtils.isEmpty(mFinancialDetail.getInvestUrl())) {
                Intent intent =  new Intent(FinancialDetailActivity.this, BaseJsBridgeWebViewActivity.class);
                intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,mFinancialDetail.getInvestUrl());
                startActivity(intent);
            }
        }
    };
    /**
     * 去购买
     */
    private View.OnClickListener mBtnFinancialOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mFinancialDetail!=null) {

                // TODO Auto-generated method stub
                if (!DdApplication.getConfigManager().isLogined()) {
                    Intent intent = new Intent(FinancialDetailActivity.this, LoginInAdvanceActivity.class);
                    intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                    intent.putExtra("srcId", mFinancialId);
                    startActivity(intent);
                    finish();
                    return;
                }
                final int mMemberStep = mFinancialDetail.getMemberStep();

                if (MemberStep.COMPLETE == mMemberStep) {

                    MobclickAgent.onEvent(FinancialDetailActivity.this, UmengTouchType.RP103_5);

                    Intent intent = new Intent(FinancialDetailActivity.this, FinancialConfirmActivity.class);
                    intent.putExtra("srcId", mFinancialId);
                    startActivity(intent);
                } else {
                    final CustomRoundDialog customRoundDialog = new CustomRoundDialog(FinancialDetailActivity.this,2);
                    if (MemberStep.SET_TRANS_PWD == mMemberStep) {//设置手机支付密码
                        customRoundDialog.setContent(getString(R.string.set_pay_password));
                    } else {
                        customRoundDialog.setContent(getString(R.string.fastpay_no_bind));
                    }
                    customRoundDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch(mMemberStep) {
                                case MemberStep.VERIFY_BANK_CARD: {
                                    Intent intent = new Intent(FinancialDetailActivity.this, VerifyBankCardActivity.class);
                                    intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                                    intent.putExtra("srcId", mFinancialId);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                case MemberStep.SEND_CODE: {
                                    Intent intent = new Intent(FinancialDetailActivity.this, VerifyCodeActivity.class);
                                    intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                                    intent.putExtra("srcId", mFinancialId);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                case MemberStep.SET_TRANS_PWD: {
                                    Intent intent = new Intent(FinancialDetailActivity.this, WithdrawPasswordSetActivity.class);
                                    intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                                    intent.putExtra("srcId", mFinancialId);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
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
            }
        }
    };
    private void setFinancialBtn(String btnStr, boolean btnStatus,String mShowStatus) {
        if(btnStatus){
            mTxtFinancial.setTextColor(getResources().getColor(R.color.white));
            mBtnFinancial.setBackgroundResource(R.drawable.btn_red_solid_noradius_bg);
        } else {
            mTxtFinancial.setTextColor(getResources().getColor(R.color.btn_text_disabled));
            mBtnFinancial.setBackgroundResource(R.drawable.button_disabled_noradius);
        }

        if (TextUtils.equals(mShowStatus, ShowStatus.FINANCIAL_NOT_START)) {
            if (mBeginDuration >= 1000) {
                mTxtFinancial.setText(getString(R.string.home_count_down, Utils.toHourDate(mBeginDuration)));
                mTxtFinancial.setTextColor(getResources().getColor(R.color.main_red));
                mBtnFinancial.setBackgroundResource(R.color.main_tag_background_red);
                mIvCountDown.setVisibility(View.VISIBLE);
                mBtnFinancial.setClickable(btnStatus);
            } else {
                //倒计时变成立即购买
                mBtnFinancial.setClickable(true);
                mIvCountDown.setVisibility(View.GONE);
                mTxtFinancial.setTextColor(getResources().getColor(R.color.white));
                mTxtFinancial.setText(getString(R.string.financial_now));
                mBtnFinancial.setBackgroundResource(R.drawable.btn_red_solid_noradius_bg);
            }

        }  else {
            mTxtFinancial.setText(btnStr);
            mIvCountDown.setVisibility(View.GONE);
            mBtnFinancial.setClickable(btnStatus);
        }

    }
    /**
     * 倒计时
     */
    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {

                mBeginDuration = mBeginDuration - 1000;
                setFinancialBtn(getString(R.string.home_count_down), false,mShowStatus);
                mHandler.removeCallbacks(runnable);
                if (mBeginDuration >= 1000) {
                    mHandler.postDelayed(this, 1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * 	数字动画
     * @param startProgress
     */
    private void setAnimation(float startProgress) {

        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        progressAnimator = ValueAnimator.ofFloat(0, startProgress);
        progressAnimator.setDuration(1000);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatorPercent = (float) animation.getAnimatedValue();
                //乘以1000/10 ,避免乘法运算数据结果误差
                txtProgress.setText(getString(R.string.financial_current_progress,(int)(animatorPercent*1000/10)+"%"));
            }
        });
        progressAnimator.start();

    }

    @Override
    public void RefreshSucceed(int countRecord) {

        setCountRecord(countRecord);

    }
    private void setCountRecord(int count) {
        if (count>0){
            txtCountRecord.setVisibility(View.VISIBLE);
        }else  {
            txtCountRecord.setVisibility(View.GONE);
        }
        if (count>=100) {
            txtCountRecord.setText("99+");
        } else {
            txtCountRecord.setText(count+"");
        }

    }

}
