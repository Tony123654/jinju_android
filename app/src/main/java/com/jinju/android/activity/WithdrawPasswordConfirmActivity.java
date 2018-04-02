package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.KeyboardEnum;
import com.jinju.android.api.KeyboardEnum.ActionEnum;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.SrcType;
import com.jinju.android.manager.UserManager.OnEditWithdrawPasswordFinishedListener;
import com.jinju.android.util.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WithdrawPasswordConfirmActivity extends BaseActivity implements OnClickListener{
	private TextView mTxtPayBox1;
	private TextView mTxtPayBox2;
	private TextView mTxtPayBox3;
	private TextView mTxtPayBox4;
	private TextView mTxtPayBox5;
	private TextView mTxtPayBox6;
	
	private ImageView mImgKeyboardZero;
	private ImageView mImgKeyboardOne;
	private ImageView mImgKeyboardTwo;
	private ImageView mImgKeyboardThree;
	private ImageView mImgKeyboardFour;
	private ImageView mImgKeyboardFive;
	private ImageView mImgKeyboardSix;
	private ImageView mImgKeyboardSeven;
	private ImageView mImgKeyboardEight;
	private ImageView mImgKeyboardNine;
	private ImageView mImgKeyboardDel;
	
	private String mPassword;
	private ArrayList<String> mConfirmList = new ArrayList<String>();
	
	private ImageView mImgFinish;
	
	private long mSrcId;
	private int mSrcType;
	
	private Dialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_password_confirm);
		
		Intent intent = getIntent();
		mPassword = intent.getStringExtra("password");
		mSrcId = intent.getLongExtra("srcId", 0);
		mSrcType = intent.getIntExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);
		
		mImgFinish = (ImageView) findViewById(R.id.img_finish);
		mImgFinish.setOnClickListener(mImgFinishOnClickListener);

		mTxtPayBox1 = (TextView) findViewById(R.id.txt_pay_box1);
		mTxtPayBox2 = (TextView) findViewById(R.id.txt_pay_box2);
		mTxtPayBox3 = (TextView) findViewById(R.id.txt_pay_box3);
		mTxtPayBox4 = (TextView) findViewById(R.id.txt_pay_box4);
		mTxtPayBox5 = (TextView) findViewById(R.id.txt_pay_box5);
		mTxtPayBox6 = (TextView) findViewById(R.id.txt_pay_box6);
		
		mImgKeyboardZero = (ImageView) findViewById(R.id.img_keyboard_zero);
		mImgKeyboardZero.setOnClickListener(this);
		mImgKeyboardOne = (ImageView) findViewById(R.id.img_keyboard_one);
		mImgKeyboardOne.setOnClickListener(this);
		mImgKeyboardTwo = (ImageView) findViewById(R.id.img_keyboard_two);
		mImgKeyboardTwo.setOnClickListener(this);
		mImgKeyboardThree = (ImageView) findViewById(R.id.img_keyboard_three);
		mImgKeyboardThree.setOnClickListener(this);
		mImgKeyboardFour = (ImageView) findViewById(R.id.img_keyboard_four);
		mImgKeyboardFour.setOnClickListener(this);
		mImgKeyboardFive = (ImageView) findViewById(R.id.img_keyboard_five);
		mImgKeyboardFive.setOnClickListener(this);
		mImgKeyboardSix = (ImageView) findViewById(R.id.img_keyboard_six);
		mImgKeyboardSix.setOnClickListener(this);
		mImgKeyboardSeven = (ImageView) findViewById(R.id.img_keyboard_seven);
		mImgKeyboardSeven.setOnClickListener(this);
		mImgKeyboardEight = (ImageView) findViewById(R.id.img_keyboard_eight);
		mImgKeyboardEight.setOnClickListener(this);
		mImgKeyboardNine = (ImageView) findViewById(R.id.img_keyboard_nine);
		mImgKeyboardNine.setOnClickListener(this);
		mImgKeyboardDel = (ImageView) findViewById(R.id.img_keyboard_del);
		mImgKeyboardDel.setOnClickListener(this);
		
		mProgressDialog = AppUtils.createLoadingDialog(this);
	}

	@Override
	protected void onDestroy() {
		mProgressDialog.dismiss();
		super.onDestroy();
	}
	
	private OnClickListener mImgFinishOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
		
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.img_keyboard_zero:
			parseActionType(KeyboardEnum.zero);
			break;
		case R.id.img_keyboard_one:
			parseActionType(KeyboardEnum.one);
			break;
		case R.id.img_keyboard_two:
			parseActionType(KeyboardEnum.two);
			break;
		case R.id.img_keyboard_three:
			parseActionType(KeyboardEnum.three);
			break;
		case R.id.img_keyboard_four:
			parseActionType(KeyboardEnum.four);
			break;
		case R.id.img_keyboard_five:
			parseActionType(KeyboardEnum.five);
			break;
		case R.id.img_keyboard_six:
			parseActionType(KeyboardEnum.six);
			break;
		case R.id.img_keyboard_seven:
			parseActionType(KeyboardEnum.seven);
			break;
		case R.id.img_keyboard_eight:
			parseActionType(KeyboardEnum.eight);
			break;
		case R.id.img_keyboard_nine:
			parseActionType(KeyboardEnum.nine);
			break;
		case R.id.img_keyboard_del:
			parseActionType(KeyboardEnum.del);
			break;
		default:
			break;
		}
	}

	private void parseActionType(KeyboardEnum type) {
		// TODO Auto-generated method stub
		if(type.getType()==ActionEnum.add){
			if(mConfirmList.size()<6){
				mConfirmList.add(type.getValue());
				updateUi();
				
				if(6 == mConfirmList.size()) {
					setPwd();
				}
			}
		}else if(type.getType()==ActionEnum.delete){
			if(mConfirmList.size()>0){
				mConfirmList.remove(mConfirmList.get(mConfirmList.size()-1));
				updateUi();
			}
		}else if(type.getType()==ActionEnum.longClick){
			mConfirmList.clear();
			updateUi();
		}

	}
	
	private void setPwd() {
		String confirmPassword = "";
		
		for(int i = 0; i < mConfirmList.size(); i++) {
			confirmPassword = confirmPassword + mConfirmList.get(i);
		}
		
		if(mPassword.equals(confirmPassword)) {
			Map<String, Object> datas = new HashMap<String, Object>();
			datas.put("transPwd", mPassword);
			datas.put("type", "set");
			datas.put("iv", 1);

			mProgressDialog.show();
			DdApplication.getUserManager().editWithdrawPassword(datas, mOnEditWithdrawPasswordFinishedListener);
		} else {
			AppUtils.showToast(this, getString(R.string.withdraw_password_error));
			Intent intent = new Intent(WithdrawPasswordConfirmActivity.this, WithdrawPasswordSetActivity.class);
			intent.putExtra(SrcType.SRC_TYPE, mSrcType);
			intent.putExtra("srcId", mSrcId);
			startActivity(intent);
			
			finish();
		}
	}
	
	private OnEditWithdrawPasswordFinishedListener mOnEditWithdrawPasswordFinishedListener = new OnEditWithdrawPasswordFinishedListener() {

		@Override
		public void onEditWithdrawPasswordFinished(Response response) {
			if (response.isSuccess()) {
				AppUtils.showToast(WithdrawPasswordConfirmActivity.this, response.getMessage());
				switch(mSrcType) {
				case SrcType.SRC_NORMAL:
					finish();
					break;
				case SrcType.SRC_FINANCIAL:{
					Intent intent = new Intent(WithdrawPasswordConfirmActivity.this, FinancialConfirmActivity.class);
					intent.putExtra("srcId", mSrcId);
					startActivity(intent);
					finish();
					break;
					}
				case SrcType.SRC_WITHDRAW:{
					Intent intent = new Intent(WithdrawPasswordConfirmActivity.this, WithdrawPasswordActivity.class);
					startActivity(intent);
					finish();
					break;
					}
				default:{
					finish();
					break;
					}
				}
			} else {
				mProgressDialog.dismiss();
				AppUtils.handleErrorResponse(WithdrawPasswordConfirmActivity.this, response);
			}
		}

	};
	
	private void updateUi() {
		// TODO Auto-generated method stub
		if(mConfirmList.size()==0){
			mTxtPayBox1.setText("");
			mTxtPayBox2.setText("");
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mConfirmList.size()==1){
			mTxtPayBox1.setText(mConfirmList.get(0));
			mTxtPayBox2.setText("");
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mConfirmList.size()==2){
			mTxtPayBox1.setText(mConfirmList.get(0));
			mTxtPayBox2.setText(mConfirmList.get(1));
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mConfirmList.size()==3){
			mTxtPayBox1.setText(mConfirmList.get(0));
			mTxtPayBox2.setText(mConfirmList.get(1));
			mTxtPayBox3.setText(mConfirmList.get(2));
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mConfirmList.size()==4){
			mTxtPayBox1.setText(mConfirmList.get(0));
			mTxtPayBox2.setText(mConfirmList.get(1));
			mTxtPayBox3.setText(mConfirmList.get(2));
			mTxtPayBox4.setText(mConfirmList.get(3));
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mConfirmList.size()==5){
			mTxtPayBox1.setText(mConfirmList.get(0));
			mTxtPayBox2.setText(mConfirmList.get(1));
			mTxtPayBox3.setText(mConfirmList.get(2));
			mTxtPayBox4.setText(mConfirmList.get(3));
			mTxtPayBox5.setText(mConfirmList.get(4));
			mTxtPayBox6.setText("");
		}else if(mConfirmList.size()==6){
			mTxtPayBox1.setText(mConfirmList.get(0));
			mTxtPayBox2.setText(mConfirmList.get(1));
			mTxtPayBox3.setText(mConfirmList.get(2));
			mTxtPayBox4.setText(mConfirmList.get(3));
			mTxtPayBox5.setText(mConfirmList.get(4));
			mTxtPayBox6.setText(mConfirmList.get(5));
		}
	}

}