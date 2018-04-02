package com.jinju.android.widget;

import java.util.ArrayList;

import com.jinju.android.R;
import com.jinju.android.api.KeyboardEnum;
import com.jinju.android.api.KeyboardEnum.ActionEnum;
import com.jinju.android.api.PayOrder;
import com.jinju.android.constant.PayType;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.util.DataUtils;
import com.jinju.android.util.ImageUtils;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayView{
	private TextView mTxtPayAmount;
	private TextView mTxtBalancePay;
	private LinearLayout mLayoutBalance;
	private TextView mTxtBankPay;
	private LinearLayout mLayoutBank;
	private TextView mTxtBankInfo;
	private TextView mTxtDayLimit;
	private View mViewSeparator;
	private TextView mTxtPayType;
	
	private ImageView mImgBankLogo;
	private ImageView mImgFinish;
	
	private TextView mTxtPayBox1;
	private TextView mTxtPayBox2;
	private TextView mTxtPayBox3;
	private TextView mTxtPayBox4;
	private TextView mTxtPayBox5;
	private TextView mTxtPayBox6;
	
	private ImageView mImgKeyboardOne;
	private ImageView mImgKeyboardTwo;
	private ImageView mImgKeyboardThree;
	private ImageView mImgKeyboardFour;
	private ImageView mImgKeyboardFive;
	private ImageView mImgKeyboardSix;
	private ImageView mImgKeyboardSeven;
	private ImageView mImgKeyboardEight;
	private ImageView mImgKeyboardNine;
	private ImageView mImgKeyboardZero;
	private ImageView mImgKeyboardDel;

	private ArrayList<String> mPwdList = new ArrayList<String>();
	private View mView;
	private OnPayListener mListener;
	private Context mContext;
		
	public PayView(PayOrder payOrder, Context context, OnPayListener listener){
		getDecorView(payOrder, context, listener);
	}
	
	public static PayView getInstance(PayOrder payOrder,Context context,OnPayListener listener){
		return new PayView(payOrder, context, listener);
	}

	public void getDecorView(PayOrder payOrder,Context context, OnPayListener listener){
		mListener = listener;
		mContext = context;
		mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pay, null);
		
		mTxtPayType = (TextView) mView.findViewById(R.id.txt_pay_type);
		if(PayType.PAY == payOrder.getPayType()) {
			mTxtPayType.setText(payOrder.getProductName());
		} else if(PayType.CHARGE == payOrder.getPayType()) {
			mTxtPayType.setText(mContext.getString(R.string.fast_pay_amount));
		} else if(PayType.WITHDRAW == payOrder.getPayType()) {
			mTxtPayType.setText(mContext.getString(R.string.withdraw_amount_notify));
		}
		
		mTxtPayAmount = (TextView) mView.findViewById(R.id.txt_pay_amount);
		mTxtPayAmount.setText(context.getString(R.string.rmb_pay, DataUtils.convertCurrencyFormat(payOrder.getLeftPay())));
		
		mLayoutBalance = (LinearLayout) mView.findViewById(R.id.layout_balance);
		mLayoutBank = (LinearLayout) mView.findViewById(R.id.layout_bank);
		mTxtDayLimit = (TextView) mView.findViewById(R.id.txt_day_limit);
		mViewSeparator =  mView.findViewById(R.id.view_separator);
		
		if(0 == payOrder.getBalancePay() && 0 == payOrder.getBankPay()) {
			mLayoutBalance.setVisibility(View.GONE);
			mLayoutBank.setVisibility(View.GONE);
			mTxtDayLimit.setVisibility(View.GONE);
			mViewSeparator.setVisibility(View.GONE);
		} else {
			if(0 == payOrder.getBalancePay()) {
				mLayoutBalance.setVisibility(View.GONE);
			} else {
				mLayoutBalance.setVisibility(View.VISIBLE);
				mTxtBalancePay = (TextView) mView.findViewById(R.id.txt_balance_pay);
				mTxtBalancePay.setText(DataUtils.convertCurrencyFormat(payOrder.getBalancePay()));
			}
			
			if(0 == payOrder.getBankPay()) {
				mLayoutBank.setVisibility(View.GONE);
				mTxtDayLimit.setVisibility(View.GONE);
			} else {
				mLayoutBank.setVisibility(View.VISIBLE);
				mTxtDayLimit.setVisibility(View.VISIBLE);
				mTxtBankInfo = (TextView) mView.findViewById(R.id.txt_bank_info);
				mTxtBankPay = (TextView) mView.findViewById(R.id.txt_bank_pay);
				mTxtBankInfo.setText(context.getString(R.string.bank_pay, payOrder.getBankName(), payOrder.getBankTailNum()));
				mTxtBankPay.setText( DataUtils.convertCurrencyFormat(payOrder.getBankPay()));
				mImgBankLogo = (ImageView) mView.findViewById(R.id.img_bank_logo);
				ImageUtils.displayImage(mImgBankLogo, payOrder.getBankLogo());

			}
		}
		
		mImgFinish = (ImageView) mView.findViewById(R.id.img_finish);
		mImgFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(mContext, UmengTouchType.RP104_3);
				parseActionType(KeyboardEnum.cancel);
			}
			
		});
		
		mTxtPayBox1 = (TextView) mView.findViewById(R.id.txt_pay_box1);
		mTxtPayBox2 = (TextView) mView.findViewById(R.id.txt_pay_box2);
		mTxtPayBox3 = (TextView) mView.findViewById(R.id.txt_pay_box3);
		mTxtPayBox4 = (TextView) mView.findViewById(R.id.txt_pay_box4);
		mTxtPayBox5 = (TextView) mView.findViewById(R.id.txt_pay_box5);
		mTxtPayBox6 = (TextView) mView.findViewById(R.id.txt_pay_box6);
		
		mImgKeyboardOne = (ImageView) mView.findViewById(R.id.img_keyboard_one);
		mImgKeyboardOne.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.one);
			}
			
		});
		
		mImgKeyboardTwo = (ImageView) mView.findViewById(R.id.img_keyboard_two);
		mImgKeyboardTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.two);
			}
			
		});
		
		mImgKeyboardThree = (ImageView) mView.findViewById(R.id.img_keyboard_three);
		mImgKeyboardThree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.three);
			}
			
		});
		
		mImgKeyboardFour = (ImageView) mView.findViewById(R.id.img_keyboard_four);
		mImgKeyboardFour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.four);
			}
			
		});
		
		mImgKeyboardFive = (ImageView) mView.findViewById(R.id.img_keyboard_five);
		mImgKeyboardFive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.five);
			}
			
		});
		
		mImgKeyboardSix = (ImageView) mView.findViewById(R.id.img_keyboard_six);
		mImgKeyboardSix.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.six);
			}
			
		});
		
		mImgKeyboardSeven = (ImageView) mView.findViewById(R.id.img_keyboard_seven);
		mImgKeyboardSeven.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.seven);
			}
			
		});
		
		mImgKeyboardEight = (ImageView) mView.findViewById(R.id.img_keyboard_eight);
		mImgKeyboardEight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.eight);
			}
			
		});
		
		mImgKeyboardNine = (ImageView) mView.findViewById(R.id.img_keyboard_nine);
		mImgKeyboardNine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.nine);
			}
			
		});
		
		mImgKeyboardZero = (ImageView) mView.findViewById(R.id.img_keyboard_zero);
		mImgKeyboardZero.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.zero);
			}
			
		});
		
		mImgKeyboardDel = (ImageView) mView.findViewById(R.id.img_keyboard_del);
		mImgKeyboardDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.del);
			}
			
		});
		
		mImgKeyboardDel.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				parseActionType(KeyboardEnum.longdel);
				return false;
			}
			
		});
	}

	private void parseActionType(KeyboardEnum type) {
		// TODO Auto-generated method stub
		if(type.getType()==ActionEnum.add){
			if(mPwdList.size()<6){
				mPwdList.add(type.getValue());
				updateUi();
				
				if(mPwdList.size() == 6) {
					String password = "";
					for(int i = 0; i < 6; i++) {
						password = password + mPwdList.get(i);
					}
					mListener.onSurePay(password);
				}
			}
		}else if(type.getType()==ActionEnum.delete){
			if(mPwdList.size()>0){
				mPwdList.remove(mPwdList.get(mPwdList.size()-1));
				updateUi();
			}
		}else if(type.getType()==ActionEnum.cancel){
			mListener.onCancelPay();
		}else if(type.getType()==ActionEnum.longClick){
			mPwdList.clear();
			updateUi();
		}

	}
	private void updateUi() {
		// TODO Auto-generated method stub
		if(mPwdList.size()==0){
			mTxtPayBox1.setText("");
			mTxtPayBox2.setText("");
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mPwdList.size()==1){
			mTxtPayBox1.setText(mPwdList.get(0));
			mTxtPayBox2.setText("");
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mPwdList.size()==2){
			mTxtPayBox1.setText(mPwdList.get(0));
			mTxtPayBox2.setText(mPwdList.get(1));
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mPwdList.size()==3){
			mTxtPayBox1.setText(mPwdList.get(0));
			mTxtPayBox2.setText(mPwdList.get(1));
			mTxtPayBox3.setText(mPwdList.get(2));
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mPwdList.size()==4){
			mTxtPayBox1.setText(mPwdList.get(0));
			mTxtPayBox2.setText(mPwdList.get(1));
			mTxtPayBox3.setText(mPwdList.get(2));
			mTxtPayBox4.setText(mPwdList.get(3));
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mPwdList.size()==5){
			mTxtPayBox1.setText(mPwdList.get(0));
			mTxtPayBox2.setText(mPwdList.get(1));
			mTxtPayBox3.setText(mPwdList.get(2));
			mTxtPayBox4.setText(mPwdList.get(3));
			mTxtPayBox5.setText(mPwdList.get(4));
			mTxtPayBox6.setText("");
		}else if(mPwdList.size()==6){
			mTxtPayBox1.setText(mPwdList.get(0));
			mTxtPayBox2.setText(mPwdList.get(1));
			mTxtPayBox3.setText(mPwdList.get(2));
			mTxtPayBox4.setText(mPwdList.get(3));
			mTxtPayBox5.setText(mPwdList.get(4));
			mTxtPayBox6.setText(mPwdList.get(5));
		}
	}

	public interface OnPayListener{
		void onCancelPay();
		void onSurePay(String password);
	}

	public View getView(){
		return mView;
	}
}
