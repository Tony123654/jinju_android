package com.jinju.android.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Financial;
import com.jinju.android.api.Tag;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/6/7.
 */

public class FinancialMoreAdapter extends BaseAdapter {
    private Context mContext;
    private List<Financial> mFinancialList;

    public FinancialMoreAdapter(Context context, List<Financial> financialList) {
        mContext = context;
        mFinancialList = financialList;
    }
    public void notifyChanged(List<Financial> financialList) {
        mFinancialList = financialList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mFinancialList == null ? 0 :mFinancialList.size();
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
        FinancialMoreAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_product_item, null);

            holder = new FinancialMoreAdapter.ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iconNew = (ImageView) convertView.findViewById(R.id.iv_new);
            holder.horizontalProgress = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.txtYearInterest = (TextView) convertView.findViewById(R.id.tv_year_interest);
            holder.txtAddInterest = (TextView) convertView.findViewById(R.id.tv_add_interest);
            holder.txtLoanPeriod = (TextView) convertView.findViewById(R.id.tv_loan_period);
            holder.txtActivityTag = (TextView) convertView.findViewById(R.id.tv_activity_tag);
            holder.txtActivityTag_2 = (TextView) convertView.findViewById(R.id.tv_activity_tag_2);
            holder.txtSurplusInvest = (TextView) convertView.findViewById(R.id.tv_surplus_invest);
            holder.txtUnit = (TextView)convertView.findViewById(R.id.tv_unit);
            holder.layoutSurplusInvest = convertView.findViewById(R.id.layout_surplus_invest);
            holder.layoutSellOut = convertView.findViewById(R.id.layout_sell_out);
            holder.mTextViewList = new ArrayList<TextView>();
            holder.mTextViewList.add(holder.txtActivityTag);
            holder.mTextViewList.add(holder.txtActivityTag_2);
            convertView.setTag(holder);
        } else{
            holder = (FinancialMoreAdapter.ViewHolder)convertView.getTag();
        }

        final Financial financial = mFinancialList.get(position);

        holder.txtName.setText(financial.getName());

        holder.horizontalProgress.setVisibility(View.VISIBLE);
        int hasPercent = financial.getHasPercent();
        holder.horizontalProgress.setProgress(hasPercent);

        long hasFundsAmount = financial.getHasFundsAmount()/100;

        if (hasFundsAmount < 10000 && hasFundsAmount > 0 ) {

            holder.txtSurplusInvest.setText(hasFundsAmount+"元");
        }
        if (hasFundsAmount > 10000 | hasFundsAmount == 10000) {
            double amount = (double)hasFundsAmount/10000;
            String money = NumberUtils.floatTwoStr(amount);

            holder.txtSurplusInvest.setText(money+"万元");
        }
        String status = financial.getShowStatus();
        //已售罄的条目色
        if(status.equals("3")| status.equals("4")) {
            //已售罄进度为0
            holder.horizontalProgress.setProgress(0);
            holder.txtSurplusInvest.setText("已售罄");
            holder.txtSurplusInvest.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));

            holder.txtName.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
            holder.txtYearInterest.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
            holder.txtAddInterest.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
            holder.txtUnit.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
            holder.txtLoanPeriod.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));

            holder.layoutSurplusInvest.setVisibility(View.GONE);
            holder.layoutSellOut.setVisibility(View.VISIBLE);
        }else {
            holder.txtName.setTextColor(mContext.getResources().getColor(R.color.word_black));
            holder.txtYearInterest.setTextColor(mContext.getResources().getColor(R.color.main_red));
            holder.txtAddInterest.setTextColor(mContext.getResources().getColor(R.color.main_red));
            holder.txtUnit.setTextColor(mContext.getResources().getColor(R.color.main_red));
            holder.txtLoanPeriod.setTextColor(mContext.getResources().getColor(R.color.word_black));
            holder.txtSurplusInvest.setTextColor(mContext.getResources().getColor(R.color.word_black));

            holder.layoutSurplusInvest.setVisibility(View.VISIBLE);
            holder.layoutSellOut.setVisibility(View.GONE);
        }

        holder.txtYearInterest.setText(financial.getActualInterestRate());
        if (!TextUtils.isEmpty(financial.getSubsidyInterestRate())) {
            double SubsidyInterestRate = Double.parseDouble(financial.getSubsidyInterestRate());
            if (SubsidyInterestRate > 0) {
                holder.txtAddInterest.setText("+"+financial.getSubsidyInterestRate()+"%");
            } else {
                holder.txtAddInterest.setText("");
            }
        } else {
            holder.txtAddInterest.setText("");
        }
        String loanPeriodDesc = financial.getLoanPeriodDesc();
        holder.txtLoanPeriod.setText(loanPeriodDesc);
        //标签设置
        List<Tag> tags = financial.getTagList();

        holder.txtActivityTag.setVisibility(View.GONE);
        holder.txtActivityTag_2.setVisibility(View.GONE);
        holder.iconNew.setVisibility(View.GONE);
        for (int i = 0;i < tags.size();i++) {
            if (tags.get(i).getTagType() == 1) {//type =1 数据为角标,否则就是标签
                holder.iconNew.setVisibility(View.VISIBLE);
                ImageUtils.noCacheDisplayImage(holder.iconNew ,tags.get(0).getTagImg());
            } else {
                String colorString = tags.get(i).getTagColor();
                if (tags.get(0).getTagType() == 1) { //有角标
                    holder.mTextViewList.get(i-1).setVisibility(View.VISIBLE);
                    if (status.equals("3")| status.equals("4")) {
                        holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_gray);
                    } else {
                        if (colorString.equals("red")) {
                            holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_red);
                        }
                        if (colorString.equals("orange")) {
                            holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_blue);
                        }
                    }
                    holder.mTextViewList.get(i-1).setText(tags.get(i).getTagName());

                } else {//无角标
                    holder.mTextViewList.get(i).setVisibility(View.VISIBLE);
                    if (status.equals("3")| status.equals("4")) {
                        holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_gray);
                    } else {
                        if (colorString.equals("red")) {
                            holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_red);
                        }
                        if (colorString.equals("orange")) {
                            holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_blue);
                        }
                    }
                    holder.mTextViewList.get(i).setText(tags.get(i).getTagName());
                }
            }
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView txtName;
        public ImageView iconNew;
        public ProgressBar horizontalProgress;//进度条
        public TextView txtYearInterest;
        public TextView txtAddInterest;
        public TextView txtLoanPeriod;
        public TextView txtActivityTag;
        public TextView txtActivityTag_2;
        public TextView txtSurplusInvest;
        public TextView txtUnit;
        public View layoutSurplusInvest ;
        public View layoutSellOut ;
        public List<TextView> mTextViewList ;
    }
}
