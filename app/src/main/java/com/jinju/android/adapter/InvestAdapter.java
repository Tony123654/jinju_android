package com.jinju.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.InvestList;

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
public class InvestAdapter extends BaseAdapter {

    //    private data;
    private List<InvestList.OrderListBean> data;
    private LayoutInflater       inflater;
    public InvestAdapter(ArrayList<InvestList.OrderListBean> listInvest, Context context) {
        this.data = listInvest;
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
            convertView = inflater.inflate(R.layout.invest_item, parent, false); //加载布局
            holder = new ViewHolder();

//            holder.iv_media = (ImageView) convertView.findViewById(R.id.iv_media);
//            holder.tvNoticeDate = (TextView) convertView.findViewById(R.id.tv_notice_date);
//            holder.tvMark = (TextView) convertView.findViewById(R.id.tv_mark);

            holder.tv_mobile= (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.tv_DateTime= (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_pay= (TextView) convertView.findViewById(R.id.tv_pay);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //        HomeNoticeBean bean = (HomeNoticeBean) mList.get(position);
        //        holder.tvContent.setText(bean.getNoticeList().get(0).getContent());
        //        holder.tvNoticeDate.setText(bean.getNoticeList().get(0).getNoticeDate());
        //        holder.tvMark.setText(bean.getNoticeList().get(0).getMark());
        InvestList.OrderListBean bean = data.get(position);
        //        Bitmap bitmap = BitmapFactory.decodeFile(bean.getShowPic());
        //                holder.iv_media.setImageBitmap(bitmap);
        //        holder.iv_media.setImageResource(Integer.parseInt(bean.getShowPic()));
//        ImageUtils.displayImage(holder.iv_media, bean.getShowPic());
//        holder.tvMark.setText(bean.getMark());
//        holder.tvNoticeDate.setText(bean.getShowPic());
        holder.tv_mobile.setText(bean.getMobile());
        holder.tv_DateTime.setText(bean.getGmtCreate());
        holder.tv_pay.setText(bean.getPayAmount());


        return convertView;
    }


    private class ViewHolder {
        TextView  tv_mobile;
        TextView  tv_DateTime;
        TextView  tv_pay;
    }
}
