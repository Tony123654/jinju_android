package com.jinju.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Notice;

import java.util.List;

/**
 * 公告adapter
 *
 * Created by Libra on 2017/6/27.
 */

public class NoticeAdapter extends BaseAdapter {
    private Context mContext;
    private List<Notice> noticeList;
    public NoticeAdapter (Context context,List<Notice> list) {
        mContext = context;
        noticeList = list;
    }
    @Override
    public int getCount() {
        return noticeList == null? 0 : noticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.layout_notice_item, null);
            holder = new ViewHolder();
            holder.mTxtNotice = (TextView) convertView.findViewById(R.id.txt_notice);
            holder.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.mTxtNoticeTime = (TextView) convertView.findViewById(R.id.txt_notice_time);
            holder.mTxtContent = (TextView) convertView.findViewById(R.id.txt_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Notice notice = noticeList.get(position);

        int type = notice.getMsgType();
        if (type == 1) {
            holder.mTxtNotice.setText("通知");
            holder.mTxtNotice.setTextColor(mContext.getResources().getColor(R.color.main_tag_text_red));
            holder.mTxtNotice.setBackgroundResource(R.drawable.solid_tag_red_noradius_bg);
        }
        if (type == 2) {
            holder.mTxtNotice.setText("交易");
            holder.mTxtNotice.setTextColor(mContext.getResources().getColor(R.color.main_tag_text_yellow));
            holder.mTxtNotice.setBackgroundResource(R.drawable.solid_tag_yellow_noradius_bg);
        }
        if (type == 3) {
            holder.mTxtNotice.setText("公告");
            holder.mTxtNotice.setTextColor(mContext.getResources().getColor(R.color.main_tag_text_red));
            holder.mTxtNotice.setBackgroundResource(R.drawable.solid_tag_red_noradius_bg);
        }
        holder.mTxtTitle.setText(notice.getMsgTypeDesc());
        holder.mTxtNoticeTime.setText(notice.getMsgDate());
        holder.mTxtContent.setText(notice.getMsgContent());
        return convertView;
    }
    public final class ViewHolder {
        public TextView mTxtNotice;
        public TextView mTxtTitle;
        public TextView mTxtNoticeTime;
        public TextView mTxtContent;
    }
}
