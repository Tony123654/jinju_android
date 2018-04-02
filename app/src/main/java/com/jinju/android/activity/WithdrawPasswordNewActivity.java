package com.jinju.android.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.KeyboardEnum;
import com.jinju.android.api.KeyboardEnum.ActionEnum;

public class WithdrawPasswordNewActivity extends BaseActivity implements OnClickListener{
	public static final int REQUEST_WITHDRAW = 1;
	
	private TextView mTxtWithdrawNewTitle;
	
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
	
	private ArrayList<String> mList = new ArrayList<String>();
	
	private String mType;
	private String mOldTransPwd;
	private String mIdCard;
	
	private RelativeLayout mBtnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_password_new);
		
		Intent intent = getIntent();
		mType = intent.getStringExtra("type");
		
		mTxtWithdrawNewTitle = (TextView) findViewById(R.id.txt_withdraw_new_title);
		
		if(mType.equals("modify")) {
			mOldTransPwd = intent.getStringExtra("oldTransPwd");
			mTxtWithdrawNewTitle.setText(getString(R.string.modify_withdraw_password));
		} else {
			mIdCard = intent.getStringExtra("idCard");
			mTxtWithdrawNewTitle.setText(getString(R.string.find_withdraw_password));
		}

		mBtnBack = (RelativeLayout) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(mBtnBackOnClickListener);

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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		mList.clear();
		updateUi();
		super.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == REQUEST_WITHDRAW) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

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
			if(mList.size()<6){
				mList.add(type.getValue());
				updateUi();
				
				if(6 == mList.size()) {
					confirmNewPassword();
				}
			}
		}else if(type.getType()==ActionEnum.delete){
			if(mList.size()>0){
				mList.remove(mList.get(mList.size()-1));
				updateUi();
			}
		}else if(type.getType()==ActionEnum.longClick){
			mList.clear();
			updateUi();
		}

	}
	
	private void confirmNewPassword() {
		String password = "";
		for(int i = 0; i < mList.size(); i++) {
			password = password + mList.get(i);
		}
		
		Intent intent = new Intent(WithdrawPasswordNewActivity.this, WithdrawPasswordNewConfirmActivity.class);
		intent.putExtra("type", mType);
		if(mType.equals("modify")) {
			intent.putExtra("oldTransPwd", mOldTransPwd);
		} else {
			intent.putExtra("idCard", mIdCard);
		}
		intent.putExtra("transPwd", password);
		startActivityForResult(intent, REQUEST_WITHDRAW);
	}
	
	private void updateUi() {
		// TODO Auto-generated method stub
		if(mList.size()==0){
			mTxtPayBox1.setText("");
			mTxtPayBox2.setText("");
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mList.size()==1){
			mTxtPayBox1.setText(mList.get(0));
			mTxtPayBox2.setText("");
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mList.size()==2){
			mTxtPayBox1.setText(mList.get(0));
			mTxtPayBox2.setText(mList.get(1));
			mTxtPayBox3.setText("");
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mList.size()==3){
			mTxtPayBox1.setText(mList.get(0));
			mTxtPayBox2.setText(mList.get(1));
			mTxtPayBox3.setText(mList.get(2));
			mTxtPayBox4.setText("");
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mList.size()==4){
			mTxtPayBox1.setText(mList.get(0));
			mTxtPayBox2.setText(mList.get(1));
			mTxtPayBox3.setText(mList.get(2));
			mTxtPayBox4.setText(mList.get(3));
			mTxtPayBox5.setText("");
			mTxtPayBox6.setText("");
		}else if(mList.size()==5){
			mTxtPayBox1.setText(mList.get(0));
			mTxtPayBox2.setText(mList.get(1));
			mTxtPayBox3.setText(mList.get(2));
			mTxtPayBox4.setText(mList.get(3));
			mTxtPayBox5.setText(mList.get(4));
			mTxtPayBox6.setText("");
		}else if(mList.size()==6){
			mTxtPayBox1.setText(mList.get(0));
			mTxtPayBox2.setText(mList.get(1));
			mTxtPayBox3.setText(mList.get(2));
			mTxtPayBox4.setText(mList.get(3));
			mTxtPayBox5.setText(mList.get(4));
			mTxtPayBox6.setText(mList.get(5));
		}
	}

}