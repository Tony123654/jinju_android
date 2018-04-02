package com.jinju.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.HomeNotice;

import java.util.List;

/**
 * Created by Libra on 2017/10/11.
 */

public class HomeNoticeAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomeNotice> mList;
    public HomeNoticeAdapter(Context context, List<HomeNotice> list) {
        mContext = context;
        mList = list;
    }
    @Override
    public int getCount() {
        return mList ==null? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_official_notice_item, null);
            holder = new ViewHolder();
            holder.mTxtTitle = (TextView)convertView.findViewById(R.id.txt_title);
            holder.mTxtTime =(TextView) convertView.findViewById(R.id.txt_time);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }
        HomeNotice mHomeNotice = mList.get(position);
        holder.mTxtTitle.setText(mHomeNotice.getTitle());
        holder.mTxtTime.setText(mHomeNotice.getNoticeDate());
        return convertView;
    }
    private  class ViewHolder {
        public TextView mTxtTitle;
        public TextView mTxtTime;


    }
}
