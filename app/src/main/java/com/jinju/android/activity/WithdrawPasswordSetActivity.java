package com.jinju.android.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.KeyboardEnum;
import com.jinju.android.api.KeyboardEnum.ActionEnum;
import com.jinju.android.constant.SrcType;

/**
 * 设置手机支付密码界面
 */
public class WithdrawPasswordSetActivity extends BaseActivity implements OnClickListener{
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
	
	private ImageView mImgFinish;
	
	private long mSrcId;
	private int mSrcType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_password_set);
		
		Intent intent = getIntent();
		mSrcId = intent.getLongExtra("srcId", 0);
		mSrcType = intent.getIntExtra(SrcType.SRC_TYPE, SrcType.SRC_NORMAL);
		
		mImgFinish = (ImageView) findViewById(R.id.img_finish);
		mImgFinish.setOnClickListener(mImgFinishOnClickListener);
		TextView tv_phone_pay_reminder = (TextView) findViewById(R.id.tv_phone_pay_reminder);
		tv_phone_pay_reminder.setText(Html.fromHtml(getString(R.string.phone_pay_password_reminder)));
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
		Log.d("WithdrawPasswordSet", "id = "+ v.getId());
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
					confirmPwd();
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
	
	private void confirmPwd() {
		String password = "";
		for(int i = 0; i < mList.size(); i++) {
			password = password + mList.get(i);
		}
		Intent intent = new Intent(WithdrawPasswordSetActivity.this, WithdrawPasswordConfirmActivity.class);
		intent.putExtra(SrcType.SRC_TYPE, mSrcType);
		intent.putExtra("srcId", mSrcId);
		intent.putExtra("password", password);
		startActivity(intent);
		
		finish();
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