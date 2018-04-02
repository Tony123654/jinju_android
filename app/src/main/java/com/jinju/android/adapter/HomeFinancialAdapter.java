package com.jinju.android.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Product;
import com.jinju.android.api.Tag;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.NumberUtils;
import com.jinju.android.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/6/6.
 */

public class HomeFinancialAdapter extends BaseAdapter {
    private Context mContext;
    private List<Product> mProductList;
    private List<Long> mSellTimeList;
    public HomeFinancialAdapter (Context context, List<Product> productList) {
        this.mContext = context;
        this.mProductList = productList;

    }
    public void notifyDataChanged (List<Product> productList,List<Long> sellTimeList){

        mProductList = productList;
        mSellTimeList = sellTimeList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mProductList == null? 0 : mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_product_item, null);

            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iconNew = (ImageView) convertView.findViewById(R.id.iv_new);
            holder.horizontalProgress = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            holder.txtYearInterest = (TextView) convertView.findViewById(R.id.tv_year_interest);
            holder.txtAddInterest = (TextView) convertView.findViewById(R.id.tv_add_interest);
            holder.txtLoanPeriod = (TextView) convertView.findViewById(R.id.tv_loan_period);
            holder.txtActivityTag = (TextView) convertView.findViewById(R.id.tv_activity_tag);
            holder.txtActivityTag_2 = (TextView) convertView.findViewById(R.id.tv_activity_tag_2);
            holder.txtSurplusInvest = (TextView) convertView.findViewById(R.id.tv_surplus_invest);
            holder.countDown = (TextView)convertView.findViewById(R.id.tv_count_down);
            holder.txtUnit = (TextView)convertView.findViewById(R.id.tv_unit);//單位
            holder.layoutSurplusInvest = convertView.findViewById(R.id.layout_surplus_invest);
            holder.layoutSellOut = convertView.findViewById(R.id.layout_sell_out);
            holder.layoutCountDown = (RelativeLayout)convertView.findViewById(R.id.layout_count_down);
            holder.mTextViewList = new ArrayList<TextView>();
            holder.mTextViewList.add(holder.txtActivityTag);
            holder.mTextViewList.add(holder.txtActivityTag_2);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }

        final Product product = mProductList.get(position);

        holder.txtName.setText(product.getName());
        //进度条和时间
        if (mSellTimeList!=null) {
            long beginDuration = mSellTimeList.get(position);

            if (beginDuration >= 1000){//倒计时
                holder.layoutCountDown.setVisibility(View.VISIBLE);
                holder.horizontalProgress.setVisibility(View.GONE);
                holder.countDown.setText(mContext.getString(R.string.home_count_down, Utils.toHourDate(beginDuration)));

            } else {
                holder.layoutCountDown.setVisibility(View.GONE);
                holder.horizontalProgress.setVisibility(View.VISIBLE);
                int hasPercent = product.getHasPercent();
                holder.horizontalProgress.setProgress(hasPercent);
            }
        }


        long hasFundsAmount = product.getHasFundsAmount()/100;

        if (hasFundsAmount < 10000 && hasFundsAmount > 0 ) {

            holder.txtSurplusInvest.setText(hasFundsAmount+"元");
        }
        if (hasFundsAmount > 10000 | hasFundsAmount == 10000) {
            double amount = (double)hasFundsAmount/10000;
            String money = NumberUtils.floatTwoStr(amount);

            holder.txtSurplusInvest.setText(money+"万元");
        }

        String status = product.getShowStatus();
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
        holder.txtYearInterest.setText(product.getActualInterestRate());
        if (!TextUtils.isEmpty(product.getSubsidyInterestRate())) {
            double SubsidyInterestRate = Double.parseDouble(product.getSubsidyInterestRate());
            if (SubsidyInterestRate > 0) {
                holder.txtAddInterest.setText("+"+product.getSubsidyInterestRate()+"%");
            }else {
                holder.txtAddInterest.setText("");
            }
        } else {
            holder.txtAddInterest.setText("");
        }

        String loanPeriodDesc = product.getLoanPeriodDesc();
        holder.txtLoanPeriod.setText(loanPeriodDesc);

        if(status.equals("3")|status.equals("4")) {
            //已售罄进度为0
            holder.horizontalProgress.setProgress(0);
            holder.txtSurplusInvest.setText("已售罄");

        }


        List<Tag> tags = product.getTagList();
        holder.txtActivityTag.setVisibility(View.GONE);
        holder.txtActivityTag_2.setVisibility(View.GONE);
        holder.iconNew.setVisibility(View.GONE);
        for (int i = 0;i < tags.size();i++) {

            if (tags.get(i).getTagType() == 1) {//type =1 数据为角标,否则就是标签

                holder.iconNew.setVisibility(View.VISIBLE);
                ImageUtils.noCacheDisplayImage(holder.iconNew ,tags.get(0).getTagImg());
            } else {
                String colorString = tags.get(i).getTagColor();
                if (tags.get(0).getTagType() == 1) {
                    holder.mTextViewList.get(i-1).setVisibility(View.VISIBLE);
                    if (colorString.equals("red")) {
                        holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_red);
                    }
                    if (colorString.equals("orange")) {
                        holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_blue);
                    }
                    holder.mTextViewList.get(i-1).setText(tags.get(i).getTagName());
                } else {
                    holder.mTextViewList.get(i).setVisibility(View.VISIBLE);
                    if (colorString.equals("red")) {
                        holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_red);
                    }
                    if (colorString.equals("orange")) {
                        holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_blue);
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
        public TextView countDown;//倒计时
        public TextView txtUnit ;
        public View layoutSurplusInvest ;
        public View layoutSellOut ;
        public List<TextView> mTextViewList ;
        public RelativeLayout layoutCountDown ;
    }

}
