package com.jinju.android.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.AccountLog;
import com.jinju.android.constant.TransType;
import com.jinju.android.util.DataUtils;

import java.util.List;

/**
 * Created by Libra on 2018/3/9.
 */

public class FundAccountAdapter extends BaseAdapter {
    private Context mContext;
    private List<AccountLog> mAccountLogList;
    public FundAccountAdapter(Context context, List<AccountLog> accountLogList) {
        mContext = context;
        mAccountLogList = accountLogList;
    }
    @Override
    public int getCount() {
        return mAccountLogList ==null ? 0 : mAccountLogList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAccountLogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_fund_account_item, null);

            holder = new ViewHolder();
            holder.txtType = (TextView) convertView.findViewById(R.id.txt_type);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.txtMoney = (TextView) convertView.findViewById(R.id.txt_money);
            holder.txtBalance = (TextView) convertView.findViewById(R.id.txt_balance);

            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }
        AccountLog accountLog = mAccountLogList.get(position);
        holder.txtType.setText(accountLog.getTransDesc());
        holder.txtTime.setText(accountLog.getGmtCreate());
        String transAmount = DataUtils.convertCurrencyFormat(accountLog.getTransAmount());
        boolean hasSign = accountLog.getTransAmount() > 0 && TextUtils.equals(accountLog.getTransType(), TransType.IN);
        holder.txtMoney.setText(hasSign ? String.format("+%s", transAmount) : transAmount);
        holder.txtMoney.setTextColor(mContext.getResources().getColor(TransType.getResColorByValue(accountLog.getTransType())));
        String canUseBalance = DataUtils.convertCurrencyFormat(accountLog.getCanUseBalance());
        holder.txtBalance.setText(canUseBalance);
        return convertView;
    }
    public final class ViewHolder {
        public TextView txtTime;
        public TextView txtType;
        public TextView txtMoney;
        public TextView txtBalance;
    }
}
