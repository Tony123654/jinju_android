package com.jinju.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.GiftUsePoint;
import com.jinju.android.api.MemberGift;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;

import java.util.List;

import static com.jinju.android.util.AppUtils.initString;

/**
 * Created by wangjw on 2017/1/31.
 */

public class SelectRedPacketAdapter extends BaseAdapter {
    private Context mContext;
    private List<MemberGift> mMemberGiftList;
    private int selectedPosition;

    public SelectRedPacketAdapter(Context context, List<MemberGift> memberGiftList,int selectRed) {
        mContext = context;
        mMemberGiftList = memberGiftList;//所有红包
        selectedPosition = selectRed;

    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    @Override
    public int getCount() {
        return mMemberGiftList==null ? 0: mMemberGiftList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMemberGiftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_select_coupon_item, null);

            holder = new ViewHolder();
            holder.txtRedPacketValue = (TextView) convertView.findViewById(R.id.txt_red_packet_value);
            holder.txtRedPacketName = (TextView) convertView.findViewById(R.id.txt_red_packet_name);
            holder.txtUseCondition1 = (TextView) convertView.findViewById(R.id.txt_use_condition1);
            holder.txtUseCondition2 = (TextView) convertView.findViewById(R.id.txt_use_condition2);
            holder.txtUseCondition3 = (TextView) convertView.findViewById(R.id.txt_expire);
            holder.txt_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.layoutRedPacket = (RelativeLayout) convertView.findViewById(R.id.layout_red_packet);
            holder.layoutState = (RelativeLayout) convertView.findViewById(R.id.layout_state);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            holder.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }

        MemberGift gift = mMemberGiftList.get(position);

            String giftValue = DataUtils.convertNumberFormat(gift.getGiftValue()/ 100);

            int giftType = gift.getGiftType();
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
            } else {
                holder.txtUseCondition1.setText(initString(strings[0]));
                holder.txtUseCondition2.setVisibility(View.GONE);
                holder.txtUseCondition3.setVisibility(View.GONE);
            }

            //可用
            holder.layoutRedPacket.setBackgroundResource(R.mipmap.img_red_packet_left);
            holder.layoutState.setBackgroundResource(R.mipmap.img_red_packet_right);
            holder.txtStatus.setBackgroundResource(R.mipmap.img_red_packet_right_red);
            holder.txtStatus.setText("可用");

            if (selectedPosition == position) {
                holder.imgCheck.setVisibility(View.VISIBLE);
                holder.txtStatus.setVisibility(View.GONE);
            } else {
                holder.imgCheck.setVisibility(View.GONE);
                holder.txtStatus.setVisibility(View.VISIBLE);
            }

        return convertView;
    }

    public final class ViewHolder {
        public TextView txtRedPacketValue;
        public TextView txtRedPacketName;
        public TextView txtUseCondition1;
        public TextView txtUseCondition2;
        public TextView txtUseCondition3;
        public TextView txt_name;
        public RelativeLayout layoutRedPacket;
        public RelativeLayout layoutState;
        public TextView txtStatus;
        public ImageView imgCheck;
    }
}
