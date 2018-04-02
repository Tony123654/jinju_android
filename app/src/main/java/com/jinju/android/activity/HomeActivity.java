package com.jinju.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allure.lbanners.LMBanners;
import com.allure.lbanners.transformer.TransitionEffect;
import com.allure.lbanners.utils.ScreenUtils;
import com.jinju.android.adapter.HomeFinancialAdapter;
import com.jinju.android.adapter.UrlImgAdapter;
import com.jinju.android.api.ButtonList;
import com.jinju.android.api.HomeNotice;
import com.jinju.android.api.Tag;
import com.jinju.android.api.WidgetLocation;
import com.jinju.android.constant.BroadcastType;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.constant.ShowStatus;
import com.jinju.android.constant.SrcType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.util.DDJRCmdUtils;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.util.Utils;
import com.jinju.android.util.ViewUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.widget.FrameAnimationHeader;
import com.jinju.android.widget.HomeListView;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.jinju.android.R;
import com.jinju.android.api.Advert;
import com.jinju.android.api.Index;
import com.jinju.android.api.Product;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.ImageUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 推荐页面
 */
public class HomeActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView mTxtName;
    private TextView txtActivityTag;
    private TextView txtActivityTag_2;
    private TextView mTxtInterestRate;
    private TextView mTxtAddInterest;
    private TextView mTxtProductHome;
    private LinearLayout mLayoutProduct;
    private LinearLayout mLayoutSelectProduct;
    private TextView mBtnFinancial;
    private TextView mTxtLeastInvest;
    private TextView mTxtDeadline;

    private Index mIndex;
    private List<Advert> mAdvertList;
    private List<Product> mProductList;
    private List<Product> mNoHeaderProductList;
    private List<ButtonList> mTopButtonList;
    private List<ButtonList> mBottomSlideList;

    private ImageView mIconNew;
    private View mLayoutCompanyIntroduce;
    private View mLayoutInviteFriends;
    private ImageView mImgCompanyIntroduce, mImgInviteFriends;
    //开售时间
    private List<Long> mSellTimeList = new ArrayList<>();
    //列表倒计时
    private Timer timer = null;
    private TimerTask task = null;
    private boolean countDown = true;

    private HomeFinancialAdapter adapter;
    private HomeListView lv;
    private long beginDuration;//首标倒计时时间
    private String showStatus;
    private ConfigManager mConfigManager;
    //轮播图
    private LMBanners mLBanners;
    private UrlImgAdapter mUrlImgAdapter;
    //公告
    private MarqueeView mMarqueeView;
    private List<HomeNotice> currentTrotList;
    private List<HomeNotice> oldTrotList = new ArrayList<>();
    private List<TextView> mTextViewList;
    private MarqueeFactory<TextView, HomeNotice> marqueeFactory;
    private ImageView mImgFloatWindow;
    private ProgressBar progressBar;
    private TextView mTxtProgress;
    private RelativeLayout layoutMoreNotice;
    private RelativeLayout mLayoutHomeNotice;

    private Handler mHandler = new Handler();
    private TextView mTxtCompanyIntroduceTitle;
    private TextView mTxtCompanyIntroduceDesc;
    private TextView mTxtInviteFriendsTitle;
    private TextView mTxtInviteFriendsDesc;
    private LinearLayout mLayoutStepView;
    private TextView txt_step_1;
    private TextView txt_step_1_desc;
    private TextView txt_step_2_desc;
    private TextView txt_step_3_desc;
    private ImageView img_step_1;
    private ImageView img_step_2;
    private TextView btn_step_state;

    private TextView mTxtDealAmount;
    private TextView mTxtEarnAmount;
    private LinearLayout mLayoutHorizontalScroll;

    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;
    private LinearLayout mLayoutPlatformData;
    private Intent bottomIntent;
    private LinearLayout ll_introduce_invite;

    private WidgetLocation widgetLocation;
    private int[] location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        bottomIntent = new Intent(BroadcastType.MAIN_BROADCASTRECEIVER);

        Intent intent = getIntent();
        mIndex = (Index) intent.getSerializableExtra("index");

        mConfigManager = DdApplication.getConfigManager();

        mLayoutCompanyIntroduce = findViewById(R.id.layout_company_introduce);    //了解朵朵
        ll_introduce_invite = (LinearLayout) findViewById(R.id.ll_introduce_invite);

        mLayoutInviteFriends = findViewById(R.id.layout_invite_friends);
        mImgCompanyIntroduce = (ImageView) findViewById(R.id.img_company_introduce);
        mImgInviteFriends = (ImageView) findViewById(R.id.img_invite_friends);
        mTxtCompanyIntroduceTitle = (TextView) findViewById(R.id.txt_company_introduce_title);
        mTxtCompanyIntroduceDesc = (TextView) findViewById(R.id.txt_company_introduce_desc);
        mTxtInviteFriendsTitle = (TextView) findViewById(R.id.txt_invite_friends_title);
        mTxtInviteFriendsDesc = (TextView) findViewById(R.id.txt_invite_friends_desc);
        mLayoutCompanyIntroduce.setOnClickListener(this);
        mLayoutInviteFriends.setOnClickListener(this);

        mLayoutStepView = (LinearLayout) findViewById(R.id.layout_step_view);
        txt_step_1 = (TextView) findViewById(R.id.txt_step_1);
        txt_step_1_desc = (TextView) findViewById(R.id.txt_step_1_desc);
        txt_step_2_desc = (TextView) findViewById(R.id.txt_step_2_desc);
        txt_step_3_desc = (TextView) findViewById(R.id.txt_step_3_desc);
        img_step_1 = (ImageView) findViewById(R.id.img_step_1);
        img_step_2 = (ImageView) findViewById(R.id.img_step_2);
        btn_step_state = (TextView) findViewById(R.id.btn_step_state);
        txt_step_1_desc.setText(Html.fromHtml(getResources().getString(R.string.home_cash_gift_desc)));
        txt_step_2_desc.setText(Html.fromHtml(getResources().getString(R.string.home_exclusive_interest_decs)));
        txt_step_3_desc.setText(Html.fromHtml(getResources().getString(R.string.home_get_gift_desc)));
        mLayoutPlatformData = (LinearLayout) findViewById(R.id.layout_platform_data);//平台数据
        mTxtDealAmount = (TextView) findViewById(R.id.txt_deal_amount);
        mTxtEarnAmount = (TextView) findViewById(R.id.txt_earn_amount);
        mLayoutHorizontalScroll = (LinearLayout) findViewById(R.id.layout_horizontal_scroll);
        mLayoutPlatformData.setOnClickListener(this);

        //设置刷新文本
        mLoadInfoList = mConfigManager.getLoadInfoList();

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(this));
        refreshLayout.setOnRefreshListener(mOnRefreshListener);

        mProductList = new ArrayList<Product>();//所有的标
        mNoHeaderProductList = new ArrayList<Product>();//无首标
        mAdvertList = new ArrayList<Advert>();

        initBannersView();

        mTxtName = (TextView) findViewById(R.id.txt_name);
        txtActivityTag = (TextView) findViewById(R.id.txt_activity_tag);
        txtActivityTag_2 = (TextView) findViewById(R.id.txt_activity_tag_2);
        mTextViewList = new ArrayList<TextView>();
        mTextViewList.add(txtActivityTag);
        mTextViewList.add(txtActivityTag_2);
        mTxtInterestRate = (TextView) findViewById(R.id.txt_interest_rate);
        mTxtAddInterest = (TextView) findViewById(R.id.txt_add_interest);
        mTxtProductHome = (TextView) findViewById(R.id.txt_product_home);
        mLayoutProduct = (LinearLayout) findViewById(R.id.layout_product);
        mTxtLeastInvest = (TextView) findViewById(R.id.txt_least_invest);
        mTxtDeadline = (TextView) findViewById(R.id.txt_deadline);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTxtProgress = (TextView) findViewById(R.id.txt_progress);
        mImgFloatWindow = (ImageView) findViewById(R.id.img_float_window);
        mImgFloatWindow.setOnClickListener(this);
        lv = (HomeListView) findViewById(R.id.product_list);
        lv.setOnItemClickListener(mOnItemClickListener);
        //设置去焦点，避免焦点总是在第一条
        lv.setFocusable(false);

        mLayoutSelectProduct = (LinearLayout) findViewById(R.id.layout_select_product);
        mLayoutSelectProduct.setOnClickListener(mLayoutSelectProductOnClickListener);

        mBtnFinancial = (TextView) findViewById(R.id.txt_financial);
        mBtnFinancial.setOnClickListener(mBtnFinancialClickListener);

        mIconNew = (ImageView) findViewById(R.id.icon_new);

        adapter = new HomeFinancialAdapter(this, mNoHeaderProductList);
        lv.setAdapter(adapter);

        //初始化公告栏
        initNoticeView();

        if (mIndex != null) {
            setupView();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        getIndex();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (task != null) {
            task.cancel();
        }
        mHandler.removeCallbacks(runnable);
    }

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            getIndex();
        }
    };

    private void getIndex() {

        Map<String, Object> datas = new HashMap<String, Object>();

        datas.put("iv", DdApplication.getVersionName());

        DdApplication.getCommonManager().getIndex(datas, mOnGetIndexFinishedListener);
    }

    private CommonManager.OnGetIndexFinishedListener mOnGetIndexFinishedListener = new CommonManager.OnGetIndexFinishedListener() {

        @Override
        public void onGetIndexFinished(Response response, Index index) {
            // TODO Auto-generated method stub
            if (response.isSuccess()) {
                mIndex = index;

                String mSecurityMessage = index.getSecurityMessage();
                mConfigManager.setSecurityMessage(mSecurityMessage);

                //是否有红包提醒
                sendRedPacketRemindBroadcast();
                //新手引导
                beginnerGuidance();
                //底部滑动页
                addBottomSlideView();
                //首页页面
                setupView();

            } else {
                AppUtils.handleErrorResponse(HomeActivity.this, response);
            }

            LoadTitleUtils.setRefreshLayout(refreshLayout, 1, mLoadInfoList);

        }
    };

    private void setupView() {
        currentTrotList = mIndex.getNoticeList();
        if (currentTrotList != null) {
            if (currentTrotList.size() > 0) {
                mLayoutHomeNotice.setVisibility(View.VISIBLE);
                setNoticeDatas();
            } else {
                mLayoutHomeNotice.setVisibility(View.GONE);
            }
        }

        mTopButtonList = mIndex.getTopButtonList();//icon顶部信息
        if (mTopButtonList != null && mTopButtonList.size() > 0) {
            //设置首页图标大小
            ViewUtils.setImageViewSize(this, mImgCompanyIntroduce, mImgInviteFriends);
            mBottomSlideList = mIndex.getBottomSlideList();//icon底部信息
            mTxtCompanyIntroduceTitle.setText(mTopButtonList.get(0).getTitle());
            mTxtCompanyIntroduceDesc.setText(mTopButtonList.get(0).getDesc());
            ImageUtils.displayImage(mImgCompanyIntroduce, mTopButtonList.get(0).getPic());
            if (mTopButtonList.size() > 1) {
                mTxtInviteFriendsTitle.setText(mTopButtonList.get(1).getTitle());
                mTxtInviteFriendsDesc.setText(mTopButtonList.get(1).getDesc());
                ImageUtils.displayImage(mImgInviteFriends, mTopButtonList.get(1).getPic());
            }

        }

        if (!TextUtils.isEmpty(mIndex.getFloatImg())) {

            mImgFloatWindow.setMaxWidth(ViewUtils.dip2px(this, 60));
            ImageUtils.displayImage(mImgFloatWindow, mIndex.getFloatImg());
            mImgFloatWindow.setVisibility(View.VISIBLE);
        } else {
            mImgFloatWindow.setVisibility(View.GONE);
        }

        mAdvertList = mIndex.getAdvertList();
        mLBanners.setAdapter(mUrlImgAdapter, mAdvertList);

        //获取关于朵朵控件
        getIntroduceSize();
        
        mProductList = mIndex.getProductList();

        if (mProductList.isEmpty()) {
            mTxtProductHome.setVisibility(View.GONE);
            mLayoutSelectProduct.setVisibility(View.GONE);
            mIconNew.setVisibility(View.GONE);
            progressBar.setProgress(0);
            mTxtName.setText("");
            mTxtInterestRate.setText("");
            mTxtAddInterest.setText("");
            mTxtLeastInvest.setText("");
            mTxtDeadline.setText("");

            adapter.notifyDataChanged(null, mSellTimeList);
            return;
        }
        mLayoutSelectProduct.setVisibility(View.VISIBLE);
        //首标
        Product product = mProductList.get(0);

        mTxtName.setText(product.getName());
//		mTxtLoanPeriod.setText(product.getLoanPeriodDesc());
//		mTxtFundsAmount.setText(getString(R.string.money_number, DataUtils.convertCurrencyFormat(product.getHasFundsAmount())));
        mTxtLeastInvest.setText(getString(R.string.home_least_invest, product.getLeastBuy() / 100));
        mTxtDeadline.setText(getString(R.string.home_deadline, product.getLoanPeriodDesc()));
        progressBar.setProgress(product.getHasPercent());
        mTxtProgress.setText(getString(R.string.home_sold, product.getHasPercent() + "%"));
        //利息
        mTxtInterestRate.setText(product.getActualInterestRate());

        //加息
        if (!TextUtils.isEmpty(product.getSubsidyInterestRate())) {
            double SubsidyInterestRate = Double.parseDouble(product.getSubsidyInterestRate());

            if (SubsidyInterestRate > 0) {
                mTxtAddInterest.setText("+" + product.getSubsidyInterestRate() + "%");
                mTxtAddInterest.setVisibility(View.VISIBLE);
            } else {
                mTxtAddInterest.setVisibility(View.GONE);
            }
        } else {
            mTxtAddInterest.setVisibility(View.GONE);
        }

        //角标
        showStatus = product.getShowStatus();
        //设置标签和角标
        setProductLabel();
        beginDuration = product.getBeginDuration();

        if (TextUtils.equals(showStatus, ShowStatus.FINANCIAL_NOT_START)) {
            //开始执行首标倒计时
            mHandler.postDelayed(runnable, 0);
        } else if (TextUtils.equals(showStatus, ShowStatus.FINANCIAL_NOW)) {
            mHandler.removeCallbacks(runnable);
            setFinancialBtn(getString(R.string.financial_now), true, showStatus);
        } else if (TextUtils.equals(showStatus, ShowStatus.LAON_COMPLETE)) {
            setFinancialBtn(getString(R.string.loan_complete), false, showStatus);
        } else if (TextUtils.equals(showStatus, ShowStatus.FINANCIAL_FINISHED)) {
            setFinancialBtn(getString(R.string.financial_finished), false, showStatus);
        }
        //首页列表
        setProductView();

    }

    /**
     * 关于朵朵布局尺寸获取
     */
    private void getIntroduceSize() {

        if (widgetLocation==null) {
            widgetLocation = new WidgetLocation();
            location = new int[2];
        }
        //获取坐标位置location[0]为x坐标，location[1]为y坐标
        mLayoutCompanyIntroduce.getLocationOnScreen(location);

        widgetLocation.setViewX(location[0]);
        widgetLocation.setViewY(location[1] + 15);
        widgetLocation.setViewWidth(ll_introduce_invite.getWidth() / 2);
        widgetLocation.setViewHeight(ll_introduce_invite.getHeight());

        if (widgetLocation.getViewWidth()>0&&widgetLocation.getViewHeight()>0) {
            if (!DdApplication.getVersionName().equals(mConfigManager.getFirstInstall())) {
                sendWidgetLocationBroadcast();
            }
        }
    }

    /**
     * 公告滚动
     */
    public void setNoticeDatas() {

        //首页公告数据
        mMarqueeView.stopFlipping();

        if (oldTrotList == null) {
            oldTrotList = currentTrotList;
        }

        if (currentTrotList != null && currentTrotList.size() > 0) {

            if (currentTrotList.size() > 1) {

                //当后台数据减少时，重新加载数据并更新界面
                if (oldTrotList.size() > currentTrotList.size()) {
                    marqueeFactory.resetData(currentTrotList, false);
                } else {
                    marqueeFactory.resetData(currentTrotList, true);
                }
                mMarqueeView.startFlipping();
            } else {
                mMarqueeView.stopFlipping();
                marqueeFactory.resetData(currentTrotList, false);
            }
            oldTrotList = currentTrotList;
        }

    }

    private void setFinancialBtn(String btnStr, boolean btnStatus, String mShowStatus) {
        if (btnStatus) {
            mBtnFinancial.setTextColor(getResources().getColor(R.color.white));
            mBtnFinancial.setBackgroundResource(R.drawable.btn_red_solid_radius_bg);
        } else {
            mBtnFinancial.setTextColor(getResources().getColor(R.color.btn_text_disabled));
            mBtnFinancial.setBackgroundResource(R.drawable.button_radius_disabled);
        }

        if (TextUtils.equals(mShowStatus, ShowStatus.FINANCIAL_NOT_START)) {
            if (beginDuration >= 1000) {
                mBtnFinancial.setText(getString(R.string.home_count_down, Utils.toHourDate(beginDuration)));
                mBtnFinancial.setClickable(btnStatus);
                mBtnFinancial.setTextColor(getResources().getColor(R.color.main_red));
                mBtnFinancial.setBackgroundResource(R.drawable.btn_light_red_radius_bg);

            } else {
                //倒计时变成立即购买
                mBtnFinancial.setText(getString(R.string.financial_now));
                mBtnFinancial.setClickable(true);
                mBtnFinancial.setTextColor(getResources().getColor(R.color.white));
                mBtnFinancial.setBackgroundResource(R.drawable.btn_red_solid_radius_bg);
            }

        } else {
            mBtnFinancial.setText(btnStr);
            mBtnFinancial.setClickable(btnStatus);
        }

    }

    //首页列表页
    private void setProductView() {

        if (1 == mProductList.size()) {
            mTxtProductHome.setVisibility(View.GONE);
            mLayoutProduct.setVisibility(View.GONE);
        } else {
            mTxtProductHome.setVisibility(View.VISIBLE);
            mLayoutProduct.setVisibility(View.VISIBLE);
            mSellTimeList.clear();
            for (int i = 1; i < mProductList.size(); i++) {
                long time = mProductList.get(i).getBeginDuration();
                if (time < 1000) {
                    time = 0;
                }
                mSellTimeList.add(time);
            }
            //将首标移除，传给adapter
            if (mNoHeaderProductList != null) {
                mNoHeaderProductList.clear();
                for (int i = 1; i < mProductList.size(); i++) {
                    mNoHeaderProductList.add(mProductList.get(i));
                }
            }

            //开始倒计时
            restartItem();
        }

    }

    /**
     * 重新开始倒计时
     */
    public void restartItem() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        startItem();
    }

    /**
     * 开始倒计时
     */
    public void startItem() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            // UI thread
            task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countDownTime();
                        }
                    });

                }
            };
        }
        //每隔1000毫秒执行一次
        timer.schedule(task, 0, 1000);
    }

    /**
     * 倒计时数据刷新
     */

    public void countDownTime() {

        //是否开启倒计时
        for (int i = 0; i < mSellTimeList.size(); i++) {
            if (mSellTimeList.get(i) > 1000) {
                countDown = true;
            }
        }
        if (countDown) {
            //开始倒计时
            for (int i = 0; i < mSellTimeList.size(); i++) {
                mSellTimeList.set(i, mSellTimeList.get(i) - 1000);
            }
            adapter.notifyDataChanged(mNoHeaderProductList, mSellTimeList);
            countDown = false;
        } else {
            //停止倒计时
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (task != null) {
                task.cancel();
                task = null;
            }

            adapter.notifyDataChanged(mNoHeaderProductList, mSellTimeList);
        }
    }

    //列表详情
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mNoHeaderProductList != null) {
                Intent intent = new Intent(HomeActivity.this, FinancialDetailActivity.class);
                intent.putExtra("id", mNoHeaderProductList.get(position).getProductId());
                startActivity(intent);
            }

        }
    };
    //首标详情
    private View.OnClickListener mLayoutSelectProductOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mProductList.isEmpty()) {
                AppUtils.showToast(HomeActivity.this, "理财产品不存在！");
                return;
            }

            MobclickAgent.onEvent(HomeActivity.this, UmengTouchType.RP101_5);
            Product product = mProductList.get(0);
            Intent intent = new Intent(HomeActivity.this, FinancialDetailActivity.class);
            intent.putExtra("id", product.getProductId());
            startActivity(intent);
        }
    };
    //首标购买
    private View.OnClickListener mBtnFinancialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mProductList.isEmpty()) {
                AppUtils.showToast(HomeActivity.this, "理财产品不存在！");
                return;
            }
            Product product = mProductList.get(0);
            final long financialId = product.getProductId();
            //没有登录
            if (!DdApplication.getConfigManager().isLogined()) {
                MobclickAgent.onEvent(HomeActivity.this, UmengTouchType.RP101_6);
                Intent intent = new Intent(HomeActivity.this, LoginInAdvanceActivity.class);
                intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                intent.putExtra("srcId", financialId);
                startActivity(intent);

                return;
            }
            final int mMemberStep = mIndex.getMemberStep();

            if (MemberStep.COMPLETE == mMemberStep) {
                //购买
                Intent intent = new Intent(HomeActivity.this, FinancialConfirmActivity.class);
                intent.putExtra("srcId", financialId);
                startActivity(intent);
            } else {
                final CustomRoundDialog customRoundDialog = new CustomRoundDialog(HomeActivity.this, 2);
                if (MemberStep.SET_TRANS_PWD == mMemberStep) {//设置手机支付密码
                    customRoundDialog.setContent(getString(R.string.set_pay_password));
                } else {
                    customRoundDialog.setContent(getString(R.string.fastpay_no_bind));
                }
                customRoundDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bindingBankStep(mMemberStep, financialId);

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
    };

    /**
     * 公司介绍 新手指引 邀请有礼 安全保障
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.layout_company_introduce://公司介绍

                if (mTopButtonList != null && mTopButtonList.size() > 0) {
                    MobclickAgent.onEvent(HomeActivity.this, UmengTouchType.RP101_1);
                    String linkUrl =  mTopButtonList.get(0).getLinkUrl();
                    if (linkUrl.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                        //点击webview上的控件，执行指定跳转
                        DDJRCmdUtils.handleProtocol(HomeActivity.this, linkUrl);
                    } else {
                        intent = new Intent(this, BaseJsBridgeWebViewActivity.class);
                        intent.putExtra("url", linkUrl);
                        startActivity(intent);
                    }

                }

                break;
            case R.id.layout_invite_friends://邀请有礼

                if (mTopButtonList != null && mTopButtonList.size() > 1) {
                    MobclickAgent.onEvent(HomeActivity.this, UmengTouchType.RP101_3);
                    String linkUrl = mTopButtonList.get(1).getLinkUrl();
                    if (linkUrl.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                        //点击webview上的控件，执行指定跳转
                        DDJRCmdUtils.handleProtocol(HomeActivity.this,linkUrl);
                    } else {
                        intent = new Intent(this, BaseJsBridgeWebViewActivity.class);
                        intent.putExtra("url", linkUrl);
                        startActivity(intent);
                    }


                }
                break;

            case R.id.img_float_window:
                String floatType = mIndex.getFloatType();
                if (floatType.equals("-1")) {
                    String url = mIndex.getFloatUrl();
                    if (!TextUtils.isEmpty(url)) {
                        if (url.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                            //点击webview上的控件，执行指定跳转
                            DDJRCmdUtils.handleProtocol(HomeActivity.this,url);
                        } else {
                            intent = new Intent(this, BaseJsBridgeWebViewActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }

                    }
                }
                if (floatType.equals("2")) {
                    intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("index", MainActivity.TAB_LOAN);
                    startActivity(intent);

                }

                break;
            case R.id.layout_platform_data:
                if (mIndex != null) {
                    String dataLinkUrl = mIndex.getDataLinkUrl();
                    if (dataLinkUrl.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                        //点击webview上的控件，执行指定跳转
                        DDJRCmdUtils.handleProtocol(HomeActivity.this,dataLinkUrl);
                    } else {
                        intent = new Intent(HomeActivity.this, BaseJsBridgeWebViewActivity.class);
                        intent.putExtra("url", dataLinkUrl);
                        startActivity(intent);
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 首页首标倒计时
     */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                beginDuration = beginDuration - 1000;

                setFinancialBtn(getString(R.string.home_count_down), false, showStatus);
                mHandler.removeCallbacks(runnable);
                if (beginDuration >= 1000) {
                    mHandler.postDelayed(this, 1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 初始化轮播图
     */
    public void initBannersView() {
        mLBanners = (LMBanners) findViewById(R.id.banners);
        //设置高度
        mLBanners.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dip2px(this, 200)));
        //参数设置
        mLBanners.isGuide(false);//是否为引导页
        mLBanners.setAutoPlay(true);//自动播放
        mLBanners.setVertical(false);//是否锤子播放
        mLBanners.setScrollDurtion(2000);//两页切换时间
        mLBanners.setCanLoop(true);//循环播放
//		mLBanners.setSelectIndicatorRes(R.drawable.guide_indicator_select);//选中的原点
//		mLBanners.setUnSelectUnIndicatorRes(R.drawable.guide_indicator_unselect);//未选中的原点
        //若自定义原点到底部的距离,默认20,必须在setIndicatorWidth之前调用
        mLBanners.setIndicatorBottomPadding(10);
        mLBanners.setIndicatorWidth(5);//原点默认为5dp
        mLBanners.setHoriZontalTransitionEffect(TransitionEffect.Default);//选中喜欢的样式
//        mLBanners.setHoriZontalCustomTransformer(new ParallaxTransformer(R.id.id_image));//自定义样式
        mLBanners.setDurtion(5000);//轮播切换时间
//        mLBanners.hideIndicatorLayout();//隐藏原点
//        mLBanners.showIndicatorLayout();//显示原点
        mLBanners.setIndicatorPosition(LMBanners.IndicaTorPosition.BOTTOM_MID);//设置原点显示位置

        //网络图片
        mUrlImgAdapter = new UrlImgAdapter(HomeActivity.this);

    }

    /**
     * 初始化公告栏
     */
    private void initNoticeView() {
        mMarqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        mLayoutHomeNotice = (RelativeLayout) findViewById(R.id.layout_home_notice);
        layoutMoreNotice = (RelativeLayout) findViewById(R.id.layout_more_notice);

        layoutMoreNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeNoticeActivity.class);
                startActivity(intent);
            }
        });
        marqueeFactory = new NoticeMF(this);
        mMarqueeView.setMarqueeFactory(marqueeFactory);
        marqueeFactory.setOnItemClickListener(new MarqueeFactory.OnItemClickListener<TextView, HomeNotice>() {
            @Override
            public void onItemClickListener(MarqueeFactory.ViewHolder<TextView, HomeNotice> holder) {
                if (currentTrotList != null && currentTrotList.size() > 0) {
                    Intent mIntent = new Intent(HomeActivity.this, HomeNoticeDetailActivity.class);
                    mIntent.putExtra("noticeDetail", currentTrotList.get(holder.position));
                    startActivity(mIntent);
                }
            }
        });

    }

    /**
     * 公告界面滚动类型
     */
    public class NoticeMF extends MarqueeFactory<TextView, HomeNotice> {
        private LayoutInflater inflater;

        public NoticeMF(Context mContext) {
            super(mContext);
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public TextView generateMarqueeItemView(HomeNotice data) {
            TextView mView = (TextView) inflater.inflate(R.layout.layout_home_notice_item, null);
            mView.setText(data.getTitle());
            return mView;
        }
    }

    /**
     * 设置角标和标签
     */
    private void setProductLabel() {
        Product product = mProductList.get(0);
        List<Tag> tags = product.getTagList();
        txtActivityTag.setVisibility(View.GONE);
        txtActivityTag_2.setVisibility(View.GONE);
        mIconNew.setVisibility(View.GONE);
        for (int i = 0; i < tags.size(); i++) {

            if (tags.get(i).getTagType() == 1) {//type =1 数据为角标,否则就是标签
                //非已售罄 显示角标
                if (showStatus.equals("3") | showStatus.equals("4")) {//已售罄
                    mIconNew.setVisibility(View.GONE);
                } else {
                    mIconNew.setVisibility(View.VISIBLE);
                    ImageUtils.noCacheDisplayImage(mIconNew, tags.get(0).getTagImg());
                }
            } else {
                String colorString = tags.get(i).getTagColor();
                if (tags.get(0).getTagType() == 1) {//有角标
                    mTextViewList.get(i - 1).setVisibility(View.VISIBLE);
                    if (colorString.equals("red")) {
                        mTextViewList.get(i - 1).setBackgroundResource(R.drawable.icon_label_red);
                        mTextViewList.get(i - 1).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (colorString.equals("orange")) {
                        mTextViewList.get(i - 1).setBackgroundResource(R.drawable.icon_label_blue);
                        mTextViewList.get(i - 1).setTextColor(getResources().getColor(R.color.white));
                    }
                    mTextViewList.get(i - 1).setText(tags.get(i).getTagName());
                } else {
                    mTextViewList.get(i).setVisibility(View.VISIBLE);
                    if (colorString.equals("red")) {
                        mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_red);
                        mTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                    }
                    if (colorString.equals("orange")) {
                        mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_blue);
                        mTextViewList.get(i).setTextColor(getResources().getColor(R.color.white));
                    }
                    mTextViewList.get(i).setText(tags.get(i).getTagName());
                }

            }
        }
    }

    /**
     * 底部滑动图片
     */
    private void addBottomSlideView() {

        mTxtDealAmount.setText(mIndex.getTotalFinancial());
        mTxtEarnAmount.setText(mIndex.getTotalInterest());

        if (mLayoutHorizontalScroll.getChildCount() > 0) {
            mLayoutHorizontalScroll.removeAllViews();
        }
        mBottomSlideList = mIndex.getBottomSlideList();
        if (mBottomSlideList != null) {
            for (int i = 0; i < mBottomSlideList.size(); i++) {
                LinearLayout layout = new LinearLayout(this);
                if (i == mBottomSlideList.size() - 1) {//最后一张图片不需要间隔
                    layout.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.dip2px(this, 145), ViewUtils.dip2px(this, 80)));
                } else {
                    layout.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.dip2px(this, 150), ViewUtils.dip2px(this, 80)));
                }

                layout.setGravity(Gravity.CENTER_VERTICAL);

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewUtils.dip2px(this, 145), ViewUtils.dip2px(this, 80)));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageUtils.discoverDisplayImage(imageView, mBottomSlideList.get(i).getPic());
                layout.addView(imageView);
                final String linkUrl = mBottomSlideList.get(i).getLinkUrl();
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (linkUrl.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                            //点击webview上的控件，执行指定跳转
                            DDJRCmdUtils.handleProtocol(HomeActivity.this,linkUrl);
                        }else {
                            Intent intent = new Intent(HomeActivity.this, BaseJsBridgeWebViewActivity.class);
                            intent.putExtra("url", linkUrl);
                            startActivity(intent);
                        }

                    }
                });

                mLayoutHorizontalScroll.addView(layout);
            }
        }

    }

    /**
     * 新手指引
     */
    private void beginnerGuidance() {
        final int stepType = mIndex.getStepType();
        if (stepType == 1) {
            mLayoutStepView.setVisibility(View.VISIBLE);
            txt_step_1.setText(R.string.home_register_give);
            btn_step_state.setText(R.string.home_go_register);
            txt_step_1_desc.setTextColor(Color.parseColor("#999999"));
            txt_step_1_desc.setText(Html.fromHtml(getResources().getString(R.string.home_cash_gift_desc)));
            txt_step_2_desc.setText(Html.fromHtml(getResources().getString(R.string.home_exclusive_interest_decs)));
            txt_step_3_desc.setText(Html.fromHtml(getResources().getString(R.string.home_get_gift_desc)));
            img_step_1.setImageResource(R.mipmap.icon_home_step_1);
            img_step_2.setImageResource(R.mipmap.icon_home_step_2);
        } else if (stepType == 2) {
            mLayoutStepView.setVisibility(View.VISIBLE);
            txt_step_1.setText(R.string.home_real_name);
            txt_step_1_desc.setText(R.string.home_real_name_desc);
            txt_step_1_desc.setTextColor(getResources().getColor(R.color.main_red));
            btn_step_state.setText(R.string.home_go_real_name);
            img_step_1.setImageResource(R.mipmap.icon_home_step_1);
            img_step_2.setImageResource(R.mipmap.icon_home_step_2);
        } else if (stepType == 3) {
            mLayoutStepView.setVisibility(View.VISIBLE);
            txt_step_1.setText(R.string.home_register_success);
            txt_step_1_desc.setText(R.string.home_welcome);
            txt_step_1_desc.setTextColor(getResources().getColor(R.color.main_red));
            btn_step_state.setText(R.string.home_go_buy);
            img_step_1.setImageResource(R.mipmap.icon_home_step_1_finish);
            img_step_2.setImageResource(R.mipmap.icon_home_step_2_finish);
        } else if (stepType == 4) {
            mLayoutStepView.setVisibility(View.GONE);
        }
        btn_step_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepType == 1) {
                    //没有登录
                    if (!DdApplication.getConfigManager().isLogined()) {
                        MobclickAgent.onEvent(HomeActivity.this, UmengTouchType.RP101_6);
                        Intent intent = new Intent(HomeActivity.this, LoginInAdvanceActivity.class);
                        intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);
                        startActivity(intent);
                    }
                } else if (stepType == 2) {
                    if (mProductList != null && mProductList.size() > 0) {
                        Product product = mProductList.get(0);
                        final long financialId = product.getProductId();
                        final int mMemberStep = mIndex.getMemberStep();
                        bindingBankStep(mMemberStep, financialId);
                    }

                } else if (stepType == 3) {
                    if (mProductList != null && mProductList.size() > 0) {
                        Product product = mProductList.get(0);
                        final long financialId = product.getProductId();
                        Intent intent = new Intent(HomeActivity.this, FinancialDetailActivity.class);
                        intent.putExtra("id", financialId);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("activityType", MainActivity.TAB_LOAN);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void bindingBankStep(int mMemberStep, long financialId) {
        switch (mMemberStep) {
            case MemberStep.VERIFY_BANK_CARD: {
                Intent intent = new Intent(HomeActivity.this, VerifyBankCardActivity.class);
                intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                intent.putExtra("srcId", financialId);
                startActivity(intent);
                break;
            }
            case MemberStep.SEND_CODE: {
                Intent intent = new Intent(HomeActivity.this, VerifyCodeActivity.class);
                intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                intent.putExtra("srcId", financialId);
                startActivity(intent);
                break;
            }
            case MemberStep.SET_TRANS_PWD: {
                Intent intent = new Intent(HomeActivity.this, WithdrawPasswordSetActivity.class);
                intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
                intent.putExtra("srcId", financialId);
                startActivity(intent);
                break;
            }
        }
    }

    /**
     * 发送广播 是否有红包提醒
     */
    private void sendRedPacketRemindBroadcast() {
        if (mIndex.isDot()) {
            bottomIntent.putExtra("redPacket", true);
        } else {
            bottomIntent.putExtra("redPacket", false);
        }

        //发送广播
        sendBroadcast(bottomIntent);
    }

    /**
     * 发送 控件位置坐标信息
     */
    private void sendWidgetLocationBroadcast() {

        bottomIntent.putExtra("homeWidgetFrame",widgetLocation);

        //发送广播
        sendBroadcast(bottomIntent);
    }
}