package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.PayResult;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.LoanSuccessDialog;
import com.jinju.android.fragment.MyFinancialHoldFragment;
import com.jinju.android.fragment.MyFinancialPassawayFragment;
import com.jinju.android.util.DataUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;

/**
 * 我的理财
 */
public class MyFinancialActivity extends BaseFragmentActivity implements OnClickListener{
	private TextView mTxtTotalInvest;
	private TextView mTxtTotalIncome;
	
	private TextView mTxtFinancialHold;
	private TextView mTxtFinancialPassway;
	private RelativeLayout mRlFinancialHold;
	private RelativeLayout mRlFinancialPassway;

	private ViewPager mViewPager;
	
	private MyFinancialHoldFragment mMyFinancialHoldFragment;
	private MyFinancialPassawayFragment mMyFinancialPasswayFragment;
	
	private ArrayList<Fragment> mFragmentList;
    
    private static final int HOLD_FRAGMENT = 0;
    private static final int PASSWAY_FRAGMENT = 1;
	private PayResult payResult;
	private LoanSuccessDialog mLoanSuccessDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_financial);

		Intent mIntent = getIntent();
		payResult = (PayResult) mIntent.getSerializableExtra("payResult");

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.my_loan);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		mTxtTotalInvest = (TextView) findViewById(R.id.txt_total_invest);//累计投资
		mTxtTotalIncome = (TextView) findViewById(R.id.txt_total_income);//累计收益
		
		mTxtFinancialHold = (TextView) findViewById(R.id.txt_financial_hold);
		mTxtFinancialPassway = (TextView) findViewById(R.id.txt_financial_passway);
		mRlFinancialHold = (RelativeLayout) findViewById(R.id.rl_financial_hold);
		mRlFinancialPassway = (RelativeLayout) findViewById(R.id.rl_financial_passway);

		mRlFinancialHold.setOnClickListener(this);
		mRlFinancialPassway.setOnClickListener(this);
		
		mMyFinancialHoldFragment = new MyFinancialHoldFragment();
		mMyFinancialPasswayFragment = new MyFinancialPassawayFragment();
	    mFragmentList = new ArrayList<Fragment>();
	    mFragmentList.add(mMyFinancialHoldFragment);
		mFragmentList.add(mMyFinancialPasswayFragment);
	    mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));

		if (payResult!=null) {//购买完成后跳转到我的理财弹起窗口
			if (mLoanSuccessDialog==null) {

				mLoanSuccessDialog = new LoanSuccessDialog(MyFinancialActivity.this,payResult);

			}
			mLoanSuccessDialog.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//如果不实现onActivityResult方法，会导致分享或回调无法正常进行
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLoanSuccessDialog!=null) {
			mLoanSuccessDialog.dismiss();
		}
		//分享防止内存泄漏
		UMShareAPI.get(this).release();
	}

	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};

	class MyViewPagerAdapter extends FragmentStatePagerAdapter  
    {  
  
        public MyViewPagerAdapter(FragmentManager fm)   
        {  
            super(fm);  
        }  
  
        @Override  
        public Fragment getItem(int position) {  
            return mFragmentList.get(position);  
        }  
  
        @Override  
        public int getCount() {  
            return mFragmentList.size();  
        }  
          

        @Override  
        public void finishUpdate(ViewGroup container)   
        {  
            super.finishUpdate(container);
            int currentItem=mViewPager.getCurrentItem();
            switch(currentItem){
            case HOLD_FRAGMENT:
				MobclickAgent.onEvent(MyFinancialActivity.this, UmengTouchType.RP114_1);

            	mTxtFinancialHold.setTextColor(getResources().getColor(R.color.light_red));
            	mTxtFinancialHold.setBackgroundResource(R.mipmap.bg_loan_tab);
            	mTxtFinancialPassway.setTextColor(getResources().getColor(R.color.gray));
            	mTxtFinancialPassway.setBackgroundResource(R.color.white);
            	break;
            case PASSWAY_FRAGMENT:
				MobclickAgent.onEvent(MyFinancialActivity.this, UmengTouchType.RP114_2);

            	mTxtFinancialHold.setTextColor(getResources().getColor(R.color.gray));
            	mTxtFinancialHold.setBackgroundResource(R.color.white);
            	mTxtFinancialPassway.setTextColor(getResources().getColor(R.color.light_red));
            	mTxtFinancialPassway.setBackgroundResource(R.mipmap.bg_loan_tab);
            	break;
            default:
            	break;
            }
        }  
          
    }  

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())  
        {  
        case R.id.rl_financial_hold:
            changeView(HOLD_FRAGMENT);  
            break;  
        case R.id.rl_financial_passway:
            changeView(PASSWAY_FRAGMENT);  
            break; 
        default:  
            break;  
        }  
	}
	
	private void changeView(int desTab)  
    {  
        mViewPager.setCurrentItem(desTab, true);  
    } 
	
	public void setAssets(long totalAsset, long totalInvest, long totalIncome) {

		mTxtTotalInvest.setText(DataUtils.convertCurrencyFormat(totalInvest));
		mTxtTotalIncome.setText(DataUtils.convertCurrencyFormat(totalIncome));
	}

}