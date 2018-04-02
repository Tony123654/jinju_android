package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.MyBank;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.manager.TradeManager.OnGetBankListFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.ViewUtils;
import com.jinju.android.widget.CircleImageView;

import java.util.List;

/**
 * 我的银行卡
 */
public class ManageBankCardActivity extends BaseActivity {
	private LinearLayout mLayoutNoBankCard;
	private LinearLayout mLayoutBankList;
	private Dialog mProgressDialog;
	private LinearLayout mBtnAdd;
	
	private int mMemberStep;
	private String[] strings = new String[]{"01020000","01030000","01040000","01050000","03080000","03010000","03030000","03090000","03020000","03050000","03060000","03040000","04105840","01000000","03100000","04012900"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_bank_card);

		RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(mBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText(R.string.withdraw_bank);

		//无银行卡显界面
		mLayoutNoBankCard = (LinearLayout) findViewById(R.id.layout_no_bank_card);
		//有银行卡显示界面
		mLayoutBankList = (LinearLayout)findViewById(R.id.layout_bank_list);
		
		mBtnAdd = (LinearLayout)findViewById(R.id.btn_add);
		mBtnAdd.setOnClickListener(mBtnAddOnClickListener);
		
		mProgressDialog = AppUtils.createLoadingDialog(this);
	}
	
	@Override
	protected void onResume() {
		getBankList();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}
	
	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			finish();
		}

	};

	private void getBankList() {
		mProgressDialog.show();
		DdApplication.getTradeManager().getBankList(mOnGetBankListFinishedListener);
	}

	/**
	 * 获取银行卡列表数据
	 */
	private OnGetBankListFinishedListener mOnGetBankListFinishedListener = new OnGetBankListFinishedListener() {

		@Override
		public void onGetBankListFinished(Response response, boolean hasFastPayBank, List<MyBank> mybankList, int memberStep) {
			if (response.isSuccess()) {
				mMemberStep = memberStep;
				//当银行卡已认证时才显示列表，注意这里只绑定了一张银行卡
				if (null == mybankList || mybankList.size() == 0) {
					mLayoutNoBankCard.setVisibility(View.VISIBLE);
					mBtnAdd.setVisibility(View.VISIBLE);
				} else {

					MyBank myBank = mybankList.get(0);
					//如果有认证才显示
					if (myBank.getStatusDesc().equals(getResources().getString(R.string.to_be_certified))) {
						mLayoutNoBankCard.setVisibility(View.VISIBLE);
						mBtnAdd.setVisibility(View.VISIBLE);
					} else {
						mLayoutNoBankCard.setVisibility(View.GONE);
						mBtnAdd.setVisibility(View.GONE);
						mLayoutBankList.removeAllViews();
						if (mybankList.size() > 0) {
							mLayoutBankList.setVisibility(View.VISIBLE);
						}

						for (int i = 0; i < mybankList.size(); i++) {
							initMyBankView(mLayoutBankList, i, mybankList.get(i));
						}
					}

				}

			} else {
				AppUtils.handleErrorResponse(ManageBankCardActivity.this, response);
			}

			mProgressDialog.dismiss();
		}

	};
	/**
	 * 点击添加银行卡
	 */
	private OnClickListener mBtnAddOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent;
			switch (mMemberStep) {
				case MemberStep.VERIFY_BANK_CARD:
					intent = new Intent(ManageBankCardActivity.this, VerifyBankCardActivity.class);
					intent.putExtra("id", "-1");
					startActivity(intent);
					break;
				case MemberStep.SEND_CODE:
					intent = new Intent(ManageBankCardActivity.this, VerifyCodeActivity.class);
					startActivity(intent);
					break;
				case MemberStep.SET_TRANS_PWD:
					intent = new Intent(ManageBankCardActivity.this, WithdrawPasswordSetActivity.class);
					startActivity(intent);
					break;

			}
		}

	};

	/**
	 * 	已绑定的银行卡
	 * @param layout
	 * @param position
	 * @param myBank
     */
	private void initMyBankView(LinearLayout layout, int position, final MyBank myBank) {
		View view = LayoutInflater.from(this).inflate(R.layout.layout_my_bank_item, layout, false);

		View layoutBankBg = view.findViewById(R.id.layout_bank_bg);
		int mScreenWidth = ViewUtils.getScreenWidth(this);
		int betweenWidth = ViewUtils.dip2px(this,32);
		//图片
		ViewGroup.LayoutParams para = layoutBankBg.getLayoutParams();
		para.height = (mScreenWidth - betweenWidth)/2;
		layoutBankBg.setLayoutParams(para);

		CircleImageView imgBank = (CircleImageView) view.findViewById(R.id.img_bank);
		TextView txtBankName = (TextView) view.findViewById(R.id.txt_bank_name);
		TextView txtCard = (TextView) view.findViewById(R.id.txt_card);
		TextView txtOnceLimit = (TextView) view.findViewById(R.id.txt_once_limit);
		TextView txtDayLimit = (TextView) view.findViewById(R.id.txt_day_limit);
		TextView txtName = (TextView) view.findViewById(R.id.txt_name);

		ImageUtils.displayImage(imgBank, myBank.getLogoPath());
		txtBankName.setText(myBank.getBankName());
		txtName.setText(myBank.getName());
		txtOnceLimit.setText(getString(R.string.bank_once_limit,myBank.getOnceLimit()));
		txtDayLimit.setText(getString(R.string.bank_day_limit,myBank.getDayLimit()));
		txtCard.setText(getString(R.string.bank_card_num, myBank.getCardNoTail()));
		initBankBackground(myBank.getBankCode(),layoutBankBg);//银行卡背景图
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (myBank.getStatusDesc().equals(getResources().getString(R.string.to_be_certified))) {
					Intent intent = new Intent(ManageBankCardActivity.this, VerifyCodeActivity.class);
					startActivity(intent);
				}

//				if(1 == myBank.getFastPayStatus()) {
//					Intent intent = new Intent(ManageBankCardActivity.this, EditBankActivity.class);
//					intent.putExtra("id", myBank.getId());
//					startActivity(intent);
//				} else {
//					Intent intent = new Intent(ManageBankCardActivity.this, VerifyBankCardActivity.class);
//					intent.putExtra("id", myBank.getId());
//					startActivity(intent);
//				}
			}
			
		});
		
		layout.addView(view);

	}

	public void initBankBackground(String bankCode,View layoutBankBg) {

		for (int i = 0;i < strings.length; i++) {

			if (strings[i].equals(bankCode)) {
				if (i ==0 ) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_gongshang_bank);
				}else if (i == 1) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_nongye_bank);
				}else if(i == 2) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_china_bank);
				}else if (i == 3) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_jianshe_bank);
				}else if (i == 4) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_zhaoshang_bank);
				}else if (i == 5) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_jiaotong_bank);
				}else if (i == 6) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_guangda_bank);
				}else if (i == 7) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_xingye_bank);
				}else if (i == 8) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_zhongxin_bank);
				}else if (i == 9) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_minsheng_bank);
				}else if (i == 10) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_guangfa_bank);
				}else if (i == 11) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_huaxia_bank);
				}else if (i == 12) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_pingan_bank);
				} else if (i == 13) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_youchu_bank);
				}else if (i == 14) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_pufa_bank);
				}else if (i == 15) {
					layoutBankBg.setBackgroundResource(R.mipmap.icon_shanghai_bank);
				}
			}
		}
	}

}