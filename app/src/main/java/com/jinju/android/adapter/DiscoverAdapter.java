package com.jinju.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Discover;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.ViewUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

import java.util.List;

/**
 * Created by Libra on 2017/11/7.
 */

public class DiscoverAdapter extends BaseAdapter {
    private Context mContext;
    private List<Discover> mDiscoverList;    //发现页列表总数
    private int mType;   //0:精彩活动   1:往期活动
    private int imgWidth ;
    private int betweenWidth ;


    public DiscoverAdapter(Context context,List<Discover> discoverList ,int type) {
        mContext = context;
        mDiscoverList = discoverList;
        mType =type;
        imgWidth = ViewUtils.getScreenWidth(mContext);
        betweenWidth = ViewUtils.dip2px(mContext,40);//图片的宽
    }
    public void notifyDataChanged(List<Discover> discoverList) {
        mDiscoverList = discoverList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDiscoverList == null? 0: mDiscoverList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDiscoverList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.layout_discover_item, null);
                holder = new ViewHolder();
                holder.layoutActivityBg =  convertView.findViewById(R.id.layout_activity_bg);
                holder.layoutContentEnd = convertView.findViewById(R.id.layout_content_end);
                holder.imgActivity = (ImageView) convertView.findViewById(R.id.img_activity);
                holder.txtEndDesc = (TextView) convertView.findViewById(R.id.txt_end_desc);
                holder.txtActivityTitle = (TextView) convertView.findViewById(R.id.txt_activity_title);
                holder.txtActivityTime = (TextView) convertView.findViewById(R.id.txt_activity_time);
                holder.layoutTitle = convertView.findViewById(R.id.layout_title);
                holder.discoverItemLl = convertView.findViewById(R.id.discover_item_ll);
                convertView.setTag(holder);
            } else{
                holder = (ViewHolder)convertView.getTag();
            }

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
            holder.discoverItemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String linkUrl = mDiscoverList.get(position).getJumpUrl();
                    if (!TextUtils.isEmpty(linkUrl)){
                        Intent intent = new Intent(mContext, BaseJsBridgeWebViewActivity.class);
                        intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH, linkUrl);
                        mContext.startActivity(intent);
                    }
                }
            });

        //设置精彩活动亮、过往活动暗
        if (mType == 0){
            holder.txtEndDesc.setVisibility(View.GONE);
            holder.layoutActivityBg.setVisibility(View.GONE);
            holder.layoutTitle.setVisibility(View.GONE);
            holder.layoutContentEnd.setVisibility(View.GONE);
        }else if (mType == 1){
            holder.layoutTitle.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {
        private View layoutActivityBg;
        private View layoutContentEnd;
        private View layoutTitle;
        private ImageView imgActivity;
        private TextView txtEndDesc;
        private TextView txtActivityTitle;
        private TextView txtActivityTime;
        private View discoverItemLl;
    }
}
