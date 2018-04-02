package com.jinju.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.WithdrawRecord;
import com.jinju.android.util.DataUtils;

public class WithdrawRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<WithdrawRecord> mWithdrawRecordList;

    public WithdrawRecordAdapter(Context context, List<WithdrawRecord> withdrawRecordList) {
        mContext = context;
        mWithdrawRecordList = withdrawRecordList;

    }

    @Override
    public int getCount() {
        return mWithdrawRecordList ==null? 0 : mWithdrawRecordList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_charge_record_item, null);

            holder = new ViewHolder();

            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount);
            holder.txtTopDate = (TextView) convertView.findViewById(R.id.txt_top_date);
            holder.rlChargeTopDate = (RelativeLayout) convertView.findViewById(R.id.rl_charge_top_date);
            holder.rlContent = (RelativeLayout) convertView.findViewById(R.id.rl_content);
            holder.mLayoutStateDetail = (LinearLayout) convertView.findViewById(R.id.layout_state_detail);
            holder.mLayoutState_1 = (LinearLayout) convertView.findViewById(R.id.layout_state_1);
            holder.mLayoutState_2 = (LinearLayout) convertView.findViewById(R.id.layout_state_2);
            holder.mLayoutState_3 = (LinearLayout) convertView.findViewById(R.id.layout_state_3);
            holder.mViewStateLine_1 = convertView.findViewById(R.id.view_state_line_1);
            holder.mTxtState_1 = (TextView) convertView.findViewById(R.id.txt_state_1);
            holder.mTxtState_2 = (TextView) convertView.findViewById(R.id.txt_state_2);
            holder.mTxtState_3 = (TextView) convertView.findViewById(R.id.txt_state_3);
            holder.mTxtStateTime_1 = (TextView) convertView.findViewById(R.id.txt_state_time_1);
            holder.mTxtStateTime_2 = (TextView) convertView.findViewById(R.id.txt_state_time_2);
            holder.mTxtStateTime_3 = (TextView) convertView.findViewById(R.id.txt_state_time_3);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final WithdrawRecord withdrawRecord = mWithdrawRecordList.get(position);
        holder.txtTime.setText(withdrawRecord.getGmtApply());
        holder.txtStatus.setText(withdrawRecord.getStatusDesc());
        holder.txtAmount.setText(DataUtils.convertCurrencyFormat(withdrawRecord.getTransAmount()));
        holder.txtTopDate.setText(withdrawRecord.getGmtApplyFormat());

        if (withdrawRecord.getStatusDesc().equals("提现成功")) {
            holder.txtAmount.setText("-"+DataUtils.convertCurrencyFormat(withdrawRecord.getTransAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_black));
        }else if (withdrawRecord.getStatusDesc().equals("提现失败")){
            holder.txtAmount.setText(DataUtils.convertCurrencyFormat(withdrawRecord.getTransAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_gray));
        }else if (withdrawRecord.getStatusDesc().equals("提现取消")) {
            holder.txtAmount.setText(DataUtils.convertCurrencyFormat(withdrawRecord.getTransAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_gray));
        }else if (withdrawRecord.getStatusDesc().equals("提交银行")) {
            holder.txtAmount.setText(DataUtils.convertCurrencyFormat(withdrawRecord.getTransAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_black));
        }else if (withdrawRecord.getStatusDesc().equals("待处理")) {
            holder.txtAmount.setText(DataUtils.convertCurrencyFormat(withdrawRecord.getTransAmount()));
            holder.txtAmount.setTextColor(mContext.getResources().getColor(R.color.word_black));
        }

        //Item头部日期设置
        if (position == 0) {
            holder.rlChargeTopDate.setVisibility(View.VISIBLE);
        } else {
            if (withdrawRecord.getGmtApplyFormat().equals(mWithdrawRecordList.get(position - 1).getGmtApplyFormat())) {
                holder.rlChargeTopDate.setVisibility(View.GONE);
            } else {
                holder.rlChargeTopDate.setVisibility(View.VISIBLE);
            }
        }

        //设置隐藏的布局
        if (withdrawRecord.getStateType()) {
            holder.mLayoutStateDetail.setVisibility(View.VISIBLE);
        } else {
            holder.mLayoutStateDetail.setVisibility(View.GONE);
        }
        setShowState(holder,position);

        holder.rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (withdrawRecord.getStateType()) {
                    withdrawRecord.setStateType(false);
                } else {
                    withdrawRecord.setStateType(true);
                }
                //替换以显示的数据
                mWithdrawRecordList.set(position,withdrawRecord);
                //通知adapter数据改变需要重新加载
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView txtTime;

        public TextView txtStatus;
        public TextView txtAmount;
        public TextView txtTopDate;
        public RelativeLayout rlChargeTopDate;
        public RelativeLayout rlContent;

        public LinearLayout mLayoutStateDetail;
        public LinearLayout mLayoutState_1;
        public LinearLayout mLayoutState_2;
        public LinearLayout mLayoutState_3;
        public View mViewStateLine_1;
        public TextView mTxtState_1;
        public TextView mTxtState_2;
        public TextView mTxtState_3;
        public TextView mTxtStateTime_1;
        public TextView mTxtStateTime_2;
        public TextView mTxtStateTime_3;
    }

    private void setShowState(ViewHolder holder,int position) {

        WithdrawRecord withdrawRecord = mWithdrawRecordList.get(position);
        //申请提现
        if (withdrawRecord.getStatus().equals("A")) {
            holder.mLayoutState_1.setVisibility(View.VISIBLE);
            holder.mTxtState_1.setText(R.string.withdraw_record_apply);
            holder.mTxtStateTime_1.setText(withdrawRecord.getGmtApply());
            holder.mViewStateLine_1.setVisibility(View.GONE);

            holder.mLayoutState_2.setVisibility(View.GONE);
            holder.mLayoutState_3.setVisibility(View.GONE);
        }
        //提交到银行
        else if (withdrawRecord.getStatus().equals("G") || withdrawRecord.getStatus().equals("C")) {
            holder.mLayoutState_1.setVisibility(View.VISIBLE);
            holder.mTxtState_1.setText(R.string.withdraw_record_apply);
            holder.mTxtStateTime_1.setText(withdrawRecord.getGmtApply());
            holder.mViewStateLine_1.setVisibility(View.VISIBLE);

            holder.mLayoutState_2.setVisibility(View.GONE);

            holder.mLayoutState_3.setVisibility(View.VISIBLE);
            if (withdrawRecord.getStatus().equals("G")) {
                holder.mTxtState_3.setText(R.string.withdraw_record_submit_bank);
                holder.mTxtStateTime_3.setText(withdrawRecord.getGmtToBank());
            } else {
                //取消状态
                holder.mTxtState_3.setText(R.string.withdraw_record_cancel);
                holder.mTxtStateTime_3.setText(withdrawRecord.getGmtDate());
            }
        }
        //提现成功 ,提现失败
        else if (withdrawRecord.getStatus().equals("S") || withdrawRecord.getStatus().equals("F")) {
            holder.mLayoutState_1.setVisibility(View.VISIBLE);
            holder.mTxtState_1.setText(R.string.withdraw_record_apply);
            holder.mTxtStateTime_1.setText(withdrawRecord.getGmtApply());
            holder.mViewStateLine_1.setVisibility(View.VISIBLE);

            holder.mLayoutState_2.setVisibility(View.VISIBLE);
            holder.mTxtState_2.setText(R.string.withdraw_record_submit_bank);
            holder.mTxtStateTime_2.setText(withdrawRecord.getGmtToBank());

            holder.mLayoutState_3.setVisibility(View.VISIBLE);
            if (withdrawRecord.getStatus().equals("S")) {
                holder.mTxtState_3.setText(R.string.withdraw_record_success);
                holder.mTxtStateTime_3.setText(withdrawRecord.getGmtDate());
            } else {
                holder.mTxtState_3.setText(R.string.withdraw_record_failure);
                holder.mTxtStateTime_3.setText(withdrawRecord.getGmtDate());
            }
        }

    }
}