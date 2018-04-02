package com.jinju.android.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.ConfirmOrder;
import com.jinju.android.util.DataUtils;

public class MyFinancialHoldAdapter extends BaseAdapter {

	private Context mContext;
	private List<ConfirmOrder> mMyFinancialAllList;

	private String lastTime = "";
	private String currentTime = "";
	private String endFinancialTime;

	public MyFinancialHoldAdapter(Context context, List<ConfirmOrder> myFinancialAllList) {
		mContext = context;
		mMyFinancialAllList = myFinancialAllList;
	}
	public void notifyDataSetInvalidated(List<ConfirmOrder> myFinancialAllList) {
		mMyFinancialAllList = myFinancialAllList;
		endFinancialTime();
		notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return (mMyFinancialAllList == null?0:mMyFinancialAllList.size());
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
			convertView = View.inflate(mContext, R.layout.layout_my_financial_hold_item, null);

			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
			holder.txtOrderCount = (TextView) convertView.findViewById(R.id.txt_order_count);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
			holder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount);
			holder.layoutStartDate = (LinearLayout) convertView.findViewById(R.id.layout_start_date);
			holder.txtStartDate = (TextView) convertView.findViewById(R.id.txt_start_date);

			holder.layoutTitleItem = (RelativeLayout) convertView.findViewById(R.id.layout_title_item);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
			holder.viewLine1 = convertView.findViewById(R.id.view_line_1);
			holder.viewLine2 = convertView.findViewById(R.id.view_line_2);
			holder.viewLine3 = convertView.findViewById(R.id.view_line_3);

			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}

		ConfirmOrder confirmOrder = mMyFinancialAllList.get(position);
		int type = confirmOrder.getType();

		if (type==0) {//募集中
			//当前标的时间
			currentTime = confirmOrder.getGmtCreate();
		} else {
			//当前标的时间
			currentTime = confirmOrder.getGmtTrade();
		}
		if (position -1 >= 0 ) {
			//上个标的时间
			if (mMyFinancialAllList.get(position-1).getType()==0) {
				lastTime = mMyFinancialAllList.get(position-1).getGmtCreate();
			}else {
				lastTime = mMyFinancialAllList.get(position-1).getGmtTrade();
			}
		}

		if (type == 0 ) {
			//募集中
			holder.txtTime.setText(confirmOrder.getGmtCreate());
			holder.txtName.setText(confirmOrder.getProductName());
			holder.txtOrderCount.setText(Html.fromHtml(mContext.getString(R.string.order_count, confirmOrder.getOrderCount())));
			holder.txtStatus.setText(mContext.getString(R.string.subscribing));
			holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.word_black));
			holder.txtAmount.setText(Html.fromHtml(mContext.getString(R.string.order_amount, DataUtils.convertCurrencyFormat(confirmOrder.getOrderAmount()))));
			holder.layoutStartDate.setVisibility(View.VISIBLE);
			holder.txtStartDate.setText(mContext.getString(R.string.order_start_date));
		} else {
			//持有中
			holder.txtTime.setText(confirmOrder.getGmtTrade());
			holder.txtName.setText(confirmOrder.getProductName());
			holder.txtOrderCount.setText(Html.fromHtml(mContext.getString(R.string.curr_position, DataUtils.convertCurrencyFormat(confirmOrder.getCurrPosition()))));

			holder.txtStatus.setVisibility(View.VISIBLE);
			holder.txtStatus.setText(confirmOrder.getStatusDesc());
			holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.word_black));
			//预计收益
			holder.txtAmount.setText(Html.fromHtml(mContext.getString(R.string.predict_income, DataUtils.convertCurrencyFormat(confirmOrder.getExpectTotalIncome()))));
			holder.layoutStartDate.setVisibility(View.GONE);
		}
		//订单 最后一组标的时间
		if (endFinancialTime.equals(currentTime)) {
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
			if (lastTime.equals(currentTime)) {
				holder.layoutTitleItem.setVisibility(View.GONE);
			} else {
				holder.layoutTitleItem.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	public final class ViewHolder {
		public TextView txtName;
		public TextView txtOrderCount;
		public TextView txtStatus;
		public TextView txtAmount;
		public LinearLayout layoutStartDate;
		public TextView txtStartDate;
		public TextView txtTime;
		public RelativeLayout layoutTitleItem;
		public View viewLine1;
		public View viewLine2;
		public View viewLine3;
	}
	//提取最后一组标的时间
	private void endFinancialTime() {
		if (mMyFinancialAllList.size()>0) {
			int type = mMyFinancialAllList.get(mMyFinancialAllList.size() - 1).getType();
			if (type == 0) {
				endFinancialTime = mMyFinancialAllList.get(mMyFinancialAllList.size() - 1).getGmtCreate();
			} else {
				endFinancialTime = mMyFinancialAllList.get(mMyFinancialAllList.size() - 1).getGmtTrade();
			}

		}
	}
}