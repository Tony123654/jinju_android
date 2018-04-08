package com.jinju.android.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Bank;
import com.jinju.android.util.ImageUtils;

import java.util.ArrayList;

public class BankAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Bank> mBankList;
	private Bank mSelectBank;

	public BankAdapter(Context context, ArrayList<Bank> bankList, Bank selectBank) {
		mContext = context;
		mBankList = bankList;
		mSelectBank = selectBank;
	}

	@Override
	public int getCount() {
		return mBankList == null ? 0 : mBankList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.layout_bank_item, null);

			holder = new ViewHolder();
			holder.imgBankLogo = (ImageView) convertView.findViewById(R.id.img_bank_logo);
			holder.txtBankName = (TextView) convertView.findViewById(R.id.txt_bank_name);
			holder.txtBankInfo = (TextView) convertView.findViewById(R.id.txt_bank_info);
			holder.imgBankSelect = (ImageView) convertView.findViewById(R.id.img_bank_select);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}

		Bank bank = mBankList.get(position);

		if(null != mSelectBank && TextUtils.equals(mSelectBank.getBankCode(), bank.getBankCode())) {
			holder.imgBankSelect.setVisibility(View.VISIBLE);
		} else {
			holder.imgBankSelect.setVisibility(View.GONE);
		}

		String payLimitOnceStr;
		String payLimitDayStr;
		//加载银行卡Logo
		ImageUtils.displayImage(holder.imgBankLogo, bank.getLogoPath());
		holder.txtBankName.setText(bank.getBankName());

		long payLimitOnce = bank.getPayLimitOnce();

		if(payLimitOnce < 100000) {
			payLimitOnceStr = payLimitOnce/10000 + mContext.getString(R.string.hundred_unit);
		} else if(payLimitOnce >= 100000 && payLimitOnce < 1000000) {
			payLimitOnceStr = payLimitOnce/100000 + mContext.getString(R.string.thousand_unit);
		} else {
			payLimitOnceStr = payLimitOnce/1000000 + mContext.getString(R.string.ten_thousand_unit);
		}

		long payLimitDay = bank.getPayLimitDay();

		if(payLimitDay < 100000) {
			payLimitDayStr = payLimitDay/10000 + mContext.getString(R.string.hundred_unit);
		} else if(payLimitDay >= 100000 && payLimitDay < 1000000) {
			payLimitDayStr = payLimitDay/100000 + mContext.getString(R.string.thousand_unit);
		} else {
			payLimitDayStr = payLimitDay/1000000 + mContext.getString(R.string.ten_thousand_unit);
		}

		holder.txtBankInfo.setText(mContext.getString(R.string.bank_limit_info, payLimitOnceStr, payLimitDayStr));

		return convertView;
	}

	public final class ViewHolder {
		public ImageView imgBankLogo;
		public ImageView imgBankSelect;
		public TextView txtBankName;
		public TextView txtBankInfo;
	}

}