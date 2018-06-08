package com.jinju.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.ReportListBean;
import com.jinju.android.api.ReportListBean.MediaListBean;
import com.jinju.android.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: MyAdapter <p>
 * 创建人: mwb <p>
 * 创建时间: 2018/6/5 15:38 <p>
 * 描述:
 * <p>
 * 更新人: <p>
 * 更新时间: <p>
 * 更新描述: <p>
 */
public class MyAdapter extends BaseAdapter {

//    private data;
    private List<ReportListBean.MediaListBean> data;
    private LayoutInflater       inflater;
    public MyAdapter(ArrayList<ReportListBean.MediaListBean> list, Context context) {
         this.data = list;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int Position) {
        return data.get(Position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, parent, false); //加载布局
            holder = new ViewHolder();

            holder.iv_media = (ImageView) convertView.findViewById(R.id.iv_media);
            holder.tvNoticeDate = (TextView) convertView.findViewById(R.id.tv_notice_date);
            holder.tvMark = (TextView) convertView.findViewById(R.id.tv_mark);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        HomeNoticeBean bean = (HomeNoticeBean) mList.get(position);
//        holder.tvContent.setText(bean.getNoticeList().get(0).getContent());
//        holder.tvNoticeDate.setText(bean.getNoticeList().get(0).getNoticeDate());
//        holder.tvMark.setText(bean.getNoticeList().get(0).getMark());
        MediaListBean bean = data.get(position);
//        Bitmap bitmap = BitmapFactory.decodeFile(bean.getShowPic());
//                holder.iv_media.setImageBitmap(bitmap);
//        holder.iv_media.setImageResource(Integer.parseInt(bean.getShowPic()));
//        int headerWidth = ViewUtils.getScreenWidth(this);
//                int imageWidth = (headerWidth - ViewUtils.dip2px(DiscoverActivity.this, 36)) / 2;
//                ViewGroup.LayoutParams params = discoverImg1.getLayoutParams();
//                params.width = imageWidth;
//                params.height = imageWidth * 7 / 17;
//                discoverImg1.setLayoutParams(params);
        ImageUtils.displayImage(holder.iv_media, bean.getShowPic());
//        Resources resources = ImageUtils.displayImage(holder.iv_media, bean.getShowPic());
//        holder.iv_media.setImageBitmap(BitmapFactory.decodeResource(resources));
        holder.tvMark.setText(bean.getMark());
        holder.tvNoticeDate.setText(bean.getShowPic());

        return convertView;
    }


    private class ViewHolder {
        ImageView iv_media;
        TextView  tvNoticeDate;
        TextView  tvMark;
    }
}
