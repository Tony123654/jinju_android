package com.jinju.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.InvestRecord;

import java.util.List;

/**
 * Created by Libra on 2017/11/6.
 * 投资记录adapter
 */

public class InvestRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<InvestRecord>  mInvestRecordList;
    public InvestRecordAdapter(Context context, List<InvestRecord> investRecordList) {
        this.mContext = context;
        this.mInvestRecordList = investRecordList;
    }
    @Override
    public int getCount() {
        return mInvestRecordList == null? 0 :mInvestRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvestRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_invest_record_item, null);
            holder = new ViewHolder();
            holder.mTxtUser = (TextView)convertView.findViewById(R.id.txt_user);
            holder.mTxtInvestAmount =(TextView) convertView.findViewById(R.id.txt_invest_amount);
            holder.mTxtInvestTime =(TextView) convertView.findViewById(R.id.txt_invest_time);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }
        InvestRecord mInvestRecord = mInvestRecordList.get(position);
        holder.mTxtUser.setText(mInvestRecord.getMobile());
        holder.mTxtInvestAmount.setText(mInvestRecord.getPayAmount());
        holder.mTxtInvestTime.setText(mInvestRecord.getGmtCreate());
        return convertView;
    }
    private  class ViewHolder {
        public TextView mTxtUser;
        public TextView mTxtInvestAmount;
        public TextView mTxtInvestTime;

    }
}
