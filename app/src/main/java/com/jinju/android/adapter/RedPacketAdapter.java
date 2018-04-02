package com.jinju.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Gift;
import com.jinju.android.api.GiftUsePoint;
import com.jinju.android.constant.CouponStatus;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;

import java.util.List;

import static com.jinju.android.util.AppUtils.initString;

public class RedPacketAdapter extends BaseAdapter {
	private String mCondition;
	private Context mContext;
	private List<Gift> mGiftList;

	public RedPacketAdapter(Context context, List<Gift> giftList, String condition) {
		mContext = context;
		mGiftList = giftList;
		mCondition = condition;


	}
	public void notifySetInvalidated(String condition) {

		mCondition = condition;
		notifyDataSetInvalidated();
	}
	public  void  notifySetChanged(String condition) {
		mCondition = condition;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mGiftList==null? 0:mGiftList.size();
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
			convertView = View.inflate(mContext, R.layout.layout_coupon_item, null);

			holder = new ViewHolder();
			holder.layoutRedPacket = (RelativeLayout) convertView.findViewById(R.id.layout_red_packet);
			holder.layoutState = (RelativeLayout) convertView.findViewById(R.id.layout_state);
			holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
			holder.txtRedPacketValue = (TextView) convertView.findViewById(R.id.txt_red_packet_value);
			holder.txtRedPacketName = (TextView) convertView.findViewById(R.id.txt_red_packet_name);
			holder.txtUseCondition1 = (TextView) convertView.findViewById(R.id.txt_use_condition1);
			holder.txtUseCondition2 = (TextView) convertView.findViewById(R.id.txt_use_condition2);
			holder.txtUseCondition3 = (TextView) convertView.findViewById(R.id.txt_expire);
			holder.txt_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}

		Gift gift = mGiftList.get(position);
		String giftValue = DataUtils.convertNumberFormat(gift.getGiftValue() / 100);
		int giftType = gift.getmGiftType();
		if (giftType == 3) {//加息红包
			String ratesValue = DataUtils.convertNumberFormat(gift.getGiftValue());
			holder.txtRedPacketValue.setText(ratesValue+"%");
		} else {
			holder.txtRedPacketValue.setText("¥"+giftValue);

		}
		holder.txtRedPacketName.setText(gift.getGiftName());
		holder.txt_name.setText(gift.getGiftTitle());

		List<GiftUsePoint> usePointList = gift.getGiftUsePointList();

		String[] strings = AppUtils.initSplit(usePointList.get(0).getmUseCondition());
		if (strings.length == 3) {
			holder.txtUseCondition1.setText(initString(strings[0]));
			holder.txtUseCondition2.setText(initString(strings[1]));
			holder.txtUseCondition3.setText(initString(strings[2]));

			holder.txtUseCondition2.setVisibility(View.VISIBLE);
			holder.txtUseCondition3.setVisibility(View.VISIBLE);
		} else if (strings.length == 2){
			holder.txtUseCondition1.setText(initString(strings[0]));
			holder.txtUseCondition2.setText(initString(strings[1]));
			holder.txtUseCondition3.setVisibility(View.GONE);
		} else if (strings.length == 1){
			holder.txtUseCondition1.setText(initString(strings[0]));
			holder.txtUseCondition2.setVisibility(View.GONE);
			holder.txtUseCondition3.setVisibility(View.GONE);
		}

		if (mCondition.equals(CouponStatus.HISTORY)) {
			holder.layoutRedPacket.setBackgroundResource(R.mipmap.img_gray_packet_left);
			holder.layoutState.setBackgroundResource(R.mipmap.img_gray_packet_right);
			holder.txtStatus.setBackgroundResource(R.mipmap.img_red_packet_right_gray);
			if (gift.getmStatus() == 3) {//已过期
				holder.txtStatus.setText("已过期");

			} else if (gift.getmStatus() == 1) {//已使用红包
				holder.txtStatus.setText("已使用");
			}
		} else {
			holder.layoutRedPacket.setBackgroundResource(R.mipmap.img_red_packet_left);
			holder.layoutState.setBackgroundResource(R.mipmap.img_red_packet_right);
			holder.txtStatus.setBackgroundResource(R.mipmap.img_red_packet_right_red);
			holder.txtStatus.setText("可用");
		}

		return convertView;
	}

	public final class ViewHolder {
		public RelativeLayout layoutRedPacket;
		public RelativeLayout layoutState;
		public TextView txtRedPacketValue;
		public TextView txtRedPacketName;
		public TextView txtUseCondition1;
		public TextView txtUseCondition2;
		public TextView txtUseCondition3;
		public TextView txtStatus;
		public TextView txt_name;
	}
}