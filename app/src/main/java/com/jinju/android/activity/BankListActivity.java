package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.BankAdapter;
import com.jinju.android.api.Bank;

import java.io.Serializable;
import java.util.ArrayList;

public class BankListActivity extends BaseActivity {
	private RelativeLayout mBtnBack;
	private ListView mListView;

	private BankAdapter mBankAdapter;

	private ArrayList<Bank> mBankList;
	private Bank mSelectBank;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank_list);

		Intent intent = getIntent();

		mBankList = (ArrayList<Bank>) intent.getSerializableExtra("banklist");
		mSelectBank = (Bank) intent.getSerializableExtra("selectBank");

		mBtnBack = (RelativeLayout) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(mmBtnBackOnClickListener);
		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTitle.setText("选择银行卡类型");

		mListView  = (ListView) findViewById(R.id.list_view);
		mBankAdapter = new BankAdapter(this, mBankList, mSelectBank);
		mListView.setOnItemClickListener(mOnItemClickListener);

		mListView.setAdapter(mBankAdapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return super.dispatchTouchEvent(event);
	}

	private OnClickListener mmBtnBackOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			finish();
		}

	};

	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (mBankList.size()>0) {
				Bank bank = mBankList.get(position);
				if(null != mSelectBank && TextUtils.equals(mSelectBank.getBankCode(), bank.getBankCode())) {
					finish();
				}

				Intent intent = new Intent();
				intent.putExtra("selectBank", (Serializable) bank);
				setResult(RESULT_OK, intent);
				finish();
			}

		}
	};

}