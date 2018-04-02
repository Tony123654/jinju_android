package com.jinju.android.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.ConfirmOrder;
import com.jinju.android.util.DataUtils;

public class MyFinancialPassawayAdapter extends BaseAdapter {

	private Context mContext;
	private List<ConfirmOrder> mMyFinancialPassawayList;
	private String endFinancialTime;
	private String lastTime = "";
	private String currentTime = "";
	public MyFinancialPassawayAdapter(Context context, List<ConfirmOrder> myFinancialPassawayList) {
		mContext = context;
		mMyFinancialPassawayList = myFinancialPassawayList;
	}

	@Override
	public int getCount() {
		return mMyFinancialPassawayList == null?0 :mMyFinancialPassawayList.size();
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
			convertView = View.inflate(mContext, R.layout.layout_my_financial_passaway_item, null);

			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txtTotalIncome = (TextView) convertView.findViewById(R.id.txt_total_income);
			holder.txtOrderCount = (TextView) convertView.findViewById(R.id.txt_order_count);

			holder.layoutTitleItem = (RelativeLayout) convertView.findViewById(R.id.layout_title_item);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
			holder.viewLine1 = convertView.findViewById(R.id.view_line_1);
			holder.viewLine2 = convertView.findViewById(R.id.view_line_2);
			holder.viewLine3 = convertView.findViewById(R.id.view_line_3);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}
		endFinancialTime = mMyFinancialPassawayList.get(mMyFinancialPassawayList.size()-1).getGmtTrade();

		ConfirmOrder myPosition = mMyFinancialPassawayList.get(position);

		holder.txtTime.setText(myPosition.getGmtTrade());

		holder.txtName.setText(myPosition.getProductName());
		holder.txtTotalIncome.setText(Html.fromHtml(mContext.getString(R.string.take_interest, DataUtils.convertCurrencyFormat(myPosition.getTotalIncome()))));
		//投资金额
		holder.txtOrderCount.setText(Html.fromHtml(mContext.getString(R.string.invest_money, DataUtils.convertCurrencyFormat(myPosition.getCurrPosition()))));

		if (position -1 >= 0 ) {
			//上个标的时间
			lastTime = mMyFinancialPassawayList.get(position-1).getGmtTrade();
		}

		//当前标的时间
		currentTime = myPosition.getGmtTrade();

		//订单 最后一组标的时间
		if (currentTime.equals(endFinancialTime)) {
			holder.viewLine2.setVisibility(View.GONE);
			holder.viewLine3.setVisibility(View.GONE);
		} else {
			holder.viewLine2.setVisibility(View.VISIBLE);
			holder.viewLine3.setVisibility(View.VISIBLE);
		}

		if (position == 0) {
			holder.layoutTitleItem.setVisibility(View.VISIBLE);
			holder.viewLine1.setVisibility(View.GONE);
		} else {
			holder.viewLine1.setVisibility(View.VISIBLE);
			if (currentTime.equals(lastTime)) {
				holder.layoutTitleItem.setVisibility(View.GONE);
			} else {
				holder.layoutTitleItem.setVisibility(View.VISIBLE);
			}
		}


		return convertView;
	}

	public final class ViewHolder {
		public TextView txtName;
		public TextView txtTotalIncome;
		public TextView txtOrderCount;

		public RelativeLayout layoutTitleItem;
		public TextView txtTime;
		public View viewLine1;
		public View viewLine2;
		public View viewLine3;
	}
}