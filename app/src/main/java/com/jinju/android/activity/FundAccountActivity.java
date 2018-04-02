package com.jinju.android.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.FilterAdapter;
import com.jinju.android.adapter.SimpleFragmentPagerAdapter;
import com.jinju.android.api.Category;
import com.jinju.android.fragment.FundAccountAllFragment;
import com.jinju.android.fragment.FundAccountFreezeFragment;
import com.jinju.android.fragment.FundAccountIncomeFragment;
import com.jinju.android.fragment.FundAccountPayFragment;
import com.jinju.android.interfaces.FundAccountAllCallBack;
import com.jinju.android.interfaces.FundAccountFreezeCallBack;
import com.jinju.android.interfaces.FundAccountIncomeCallBack;
import com.jinju.android.interfaces.FundAccountPayCallBack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2018/3/8.
 *
 * 资金流水
 */

public class FundAccountActivity extends BaseFragmentActivity implements FundAccountAllCallBack,FundAccountIncomeCallBack,FundAccountPayCallBack,FundAccountFreezeCallBack {
    private ArrayList<Fragment> list_fragment=new ArrayList<>();     //定义要装fragment的列表
    private ArrayList<String> list_title=new ArrayList<>();
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RecyclerView mRecyclerView;
    private RelativeLayout mLayoutFilter;
    private TextView mTxtTitleRight;
    private List<Category> mAllCodeList = new ArrayList<Category>();
    private List<Category> mIncomeCodeList = new ArrayList<Category>();
    private List<Category> mPayCodeList = new ArrayList<Category>();
    private List<Category> mFreezeCodeList = new ArrayList<Category>();
    private FilterAdapter mFilterAdapter;
    private OnAllSelectTagListener mOnAllSelectTagListener;
    private OnIncomeSelectTagListener mOnIncomeSelectTagListener;
    private OnPaySelectTagListener mOnPaySelectTagListener;
    private OnFreezeSelectTagListener mOnFreezeSelectTagListener;
    private int selectTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_account);
        initView();
    }

    private void initView() {
        RelativeLayout mBtnBack = (RelativeLayout) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(mBtnBackOnClickListener);
        TextView mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtTitle.setText(R.string.fund_account_log);
        mTxtTitleRight = (TextView) findViewById(R.id.tv_title_right);
        mTxtTitleRight.setVisibility(View.VISIBLE);
        mTxtTitleRight.setText(R.string.fund_account_filter);
        mTxtTitleRight.setOnClickListener(TxtTitleRightOnClickListener);


        FundAccountAllFragment allFragment = new FundAccountAllFragment();
        FundAccountIncomeFragment incomeFragment = new FundAccountIncomeFragment();
        FundAccountPayFragment payFragment = new FundAccountPayFragment();
        FundAccountFreezeFragment freezeFragment = new FundAccountFreezeFragment();

        list_fragment.add(allFragment);
        list_fragment.add(incomeFragment);
        list_fragment.add(payFragment);
        list_fragment.add(freezeFragment);
        list_title.add("全部");
        list_title.add("收入");
        list_title.add("支出");
        list_title.add("冻结");
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this,list_fragment,list_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //初始化标签
                selectTag = 0;
                if (position == 0) {
                    mOnAllSelectTagListener.onAllSelectTag(selectTag);
                    mFilterAdapter.notifyDataChanged(mAllCodeList);
                }
                if (position == 1) {
                    mOnIncomeSelectTagListener.onIncomeSelectTag(selectTag);
                    mFilterAdapter.notifyDataChanged(mIncomeCodeList);
                }
                if (position == 2) {
                    mOnPaySelectTagListener.onPaySelectTag(selectTag);
                    mFilterAdapter.notifyDataChanged(mPayCodeList);
                }
                if (position == 3) {
                    mOnFreezeSelectTagListener.onFreezeSelectTag(selectTag);
                    mFilterAdapter.notifyDataChanged(mFreezeCodeList);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_red));//滑动条颜色
        tabLayout.setTabTextColors(getResources().getColor(R.color.word_black),getResources().getColor(R.color.word_black));//标题字体颜色,左边是未选中，右边是选中
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        setUpIndicatorWidth();
        //设置筛选项
        showFilterTags();
        viewPager.setCurrentItem(0);
    }
    private View.OnClickListener mBtnBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private View.OnClickListener TxtTitleRightOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mTxtTitleRight.getText().toString().equals("确定")) {
                mLayoutFilter.setVisibility(View.GONE);
                mTxtTitleRight.setText("筛选");
                //选中筛选
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == 0) {
                    mOnAllSelectTagListener.onAllSelectTag(selectTag);
                }
                if (currentItem == 1) {
                    mOnIncomeSelectTagListener.onIncomeSelectTag(selectTag);
                }
                if (currentItem == 2) {
                    mOnPaySelectTagListener.onPaySelectTag(selectTag);
                }
                if (currentItem == 3) {
                    mOnFreezeSelectTagListener.onFreezeSelectTag(selectTag);
                }
            } else {
                mLayoutFilter.setVisibility(View.VISIBLE);
                mTxtTitleRight.setText("确定");

            }
        }
    };

    /**
     * 筛选标签
     */
    private void showFilterTags() {
        mLayoutFilter = (RelativeLayout) findViewById(R.id.layout_filter);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtTitleRight.setText("筛选");
                mLayoutFilter.setVisibility(View.GONE);
            }
        });
        //设置分割
        GridLayoutManager mGridLayoutManager= new GridLayoutManager(this, 3);

        mFilterAdapter = new FilterAdapter(this, R.layout.filter_grid_recycleview_item, mAllCodeList);
        mFilterAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mFilterAdapter);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

    }

    /**
     * 设置tab的宽度
     */
    private void setUpIndicatorWidth() {
        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(tabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(40);
                    params.setMarginEnd(40);
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void fundAccountAllValue(List<Category> codeList) {
        Category allCategory = new Category();
        Category otherCategory = new Category();
        allCategory.setName("全部");
        otherCategory.setName("其他");
        mAllCodeList.clear();
        mAllCodeList.add(allCategory);
        mAllCodeList.addAll(codeList);
        mAllCodeList.add(otherCategory);

    }
    @Override
    public void fundAccountIncomeValue(List<Category> codeList) {
        Category category = new Category();
        category.setName("全部");
        mIncomeCodeList.clear();
        mIncomeCodeList.add(category);
        mIncomeCodeList.addAll(codeList);

    }
    @Override
    public void fundAccountPayValue(List<Category> codeList) {
        Category category = new Category();
        category.setName("全部");
        mPayCodeList.clear();
        mPayCodeList.add(category);
        mPayCodeList.addAll(codeList);

    }
    @Override
    public void fundAccountFreezeValue(List<Category> codeList) {
        Category category = new Category();
        category.setName("全部");
        mFreezeCodeList.clear();
        mFreezeCodeList.add(category);
        mFreezeCodeList.addAll(codeList);

    }


    private FilterAdapter.OnItemClickListener mOnItemClickListener = new FilterAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int selectPosition) {
            selectTag =  selectPosition;
        }

    };
    public interface OnAllSelectTagListener{
        void onAllSelectTag(int selectTag);
    }
    public interface OnIncomeSelectTagListener{
        void onIncomeSelectTag(int selectTag);
    }
    public interface OnPaySelectTagListener{
        void onPaySelectTag(int selectTag);
    }
    public interface OnFreezeSelectTagListener{
        void onFreezeSelectTag(int selectTag);
    }
    public void setOnAllSelectTagListener(OnAllSelectTagListener onAllSelectTagListener) {
        if (onAllSelectTagListener!=null) {
            mOnAllSelectTagListener = onAllSelectTagListener ;
        }
    }
    public void setOnIncomeSelectTagListener(OnIncomeSelectTagListener onIncomeSelectTagListener) {
        if (onIncomeSelectTagListener!=null) {
            mOnIncomeSelectTagListener = onIncomeSelectTagListener ;
        }
    }
    public void setOnPaySelectTagListener(OnPaySelectTagListener onPaySelectTagListener) {
        if (onPaySelectTagListener!=null) {
            mOnPaySelectTagListener = onPaySelectTagListener ;
        }
    }
    public void setOnFreezeSelectTagListener(OnFreezeSelectTagListener onFreezeSelectTagListener) {
        if (onFreezeSelectTagListener!=null) {
            mOnFreezeSelectTagListener = onFreezeSelectTagListener ;
        }
    }

}
