package com.jinju.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.ChargeRecord;
import com.jinju.android.util.DataUtils;

public class ChargeRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChargeRecord> mChargeRecordList;

    public ChargeRecordAdapter(Context context, List<ChargeRecord> chargeRecordList) {
        mContext = context;
        mChargeRecordList = chargeRecordList;
    }

    @Override
    public int getCount() {
        return mChargeRecordList == null? 0 : mChargeRecordList.size();
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
        ChargeRecord chargeRecord = mChargeRecordList.get(position);

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_charge_record_item, null);

            holder = new ViewHolder();
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount);
            holder.txtTopDate = (TextView) convertView.findViewById(R.id.txt_top_date);
            holder.rlChargeTopDate = (RelativeLayout) convertView.findViewById(R.id.rl_charge_top_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTime.setText(chargeRecord.getGmtCharge());
        holder.txtStatus.setText(chargeRecord.getStatusDesc());
        holder.txtTopDate.setText(chargeRecord.getGmtGroupTime());
        if (position == 0) {
            holder.rlChargeTopDate.setVisibility(View.VISIBLE);
        } else {
            if (chargeRecord.getGmtGroupTime().equals(mChargeRecordList.get(position - 1).getGmtGroupTime())) {
                holder.rlChargeTopDate.setVisibility(View.GONE);
            } else {
                holder.rlChargeTopDate.setVisibility(View.VISIBLE);
            }
        }
        if (chargeRecord.getStatusDesc().equals("充值成功")) {
            holder.txtAmount.setText("+"+DataUtils.convertCurrencyFormat(chargeRecord.getPayAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_black));
        } else if (chargeRecord.getStatusDesc().equals("充值失败")){
            holder.txtAmount.setText(DataUtils.convertCurrencyFormat(chargeRecord.getPayAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_gray));
        }else {
            holder.txtAmount.setText(DataUtils.convertCurrencyFormat(chargeRecord.getPayAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_black));
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView txtTime;
        public TextView txtType;
        public TextView txtStatus;
        public TextView txtAmount;
        public TextView txtTopDate;
        public RelativeLayout rlChargeTopDate;

    }
}