package com.jinju.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Discover;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.ViewUtils;

import java.util.List;

/**
 * Created by awe on 2018/1/22.
 */

public class DiscoverRecyclerAdapter extends RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private List<Discover> mDiscoverList;    //发现页列表总数
    private int mType;   //0:精彩活动   1:往期活动
    private int imgWidth ;
    private int betweenWidth ;


    public DiscoverRecyclerAdapter(Context context ,List<Discover> discoverlist ,int type) {
        super();
        mDiscoverList = discoverlist;
        mType = type;
        mContext = context;
        imgWidth = ViewUtils.getScreenWidth(mContext);
        betweenWidth = ViewUtils.dip2px(mContext, 40);//图片的宽
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        View view = View.inflate(parent.getContext(), R.layout.layout_discover_item, null);
        // 创建一个ViewHolder
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //绑定数据导ViewHolder上
        Discover mDiscover = mDiscoverList.get(position);
        ImageUtils.displayImage(holder.imgActivity,mDiscover.getImgPath());
        //图片
        ViewGroup.LayoutParams para = holder.imgActivity.getLayoutParams();
        para.height = (int)((float)(imgWidth-betweenWidth)/5*2.8);
        para.width = imgWidth-betweenWidth;
        holder.imgActivity.setLayoutParams(para);
        holder.layoutActivityBg.setLayoutParams(para);
        holder.txtActivityTitle.setText(mDiscover.getTitle());
        holder.txtActivityTime.setText(mDiscover.getFindDateBegin());


        //设置精彩活动亮、过往活动暗
        if (mType == 0) {
            holder.layoutActivityBg.setVisibility(View.GONE);
            holder.layoutContentEnd.setVisibility(View.GONE);
        } else if (mType == 1) {
            holder.layoutActivityBg.setVisibility(View.VISIBLE);
            holder.layoutContentEnd.setVisibility(View.VISIBLE);
        }

        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return mDiscoverList.size();
    }

    //点击--1
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    //长按点击
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View layoutActivityBg;
        private View layoutContentEnd;
        private View layoutTitle;
        private ImageView imgActivity;
        private TextView txtEndDesc;
        private TextView txtActivityTitle;
        private TextView txtActivityTime;
        private View discoverItemLl;

        public ViewHolder(View itemView) {

            super(itemView);

            layoutActivityBg = itemView.findViewById(R.id.layout_activity_bg);
            layoutContentEnd = itemView.findViewById(R.id.layout_content_end);
            imgActivity = (ImageView) itemView.findViewById(R.id.img_activity);
            txtEndDesc = (TextView) itemView.findViewById(R.id.txt_end_desc);
            txtActivityTitle = (TextView) itemView.findViewById(R.id.txt_activity_title);
            txtActivityTime = (TextView) itemView.findViewById(R.id.txt_activity_time);
            layoutTitle = itemView.findViewById(R.id.layout_title);
            discoverItemLl = itemView.findViewById(R.id.discover_item_ll);
        }

    }

}
