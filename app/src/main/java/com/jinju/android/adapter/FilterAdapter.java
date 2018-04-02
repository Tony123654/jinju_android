package com.jinju.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinju.android.R;
import com.jinju.android.api.Category;
import com.jinju.android.interfaces.TagWithListener;
import com.jinju.android.widget.TagView;

import java.util.List;

/**
 * Created by Libra on 2018/3/8.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder>{

    private LayoutInflater mLayoutInflater;
    private List<Category> mCodeList;
    private int mItemLayout;
    private int selectPosition = 0;
    private OnItemClickListener mItemClickListener;
    private Context mContext;
    public FilterAdapter(Context context, int itemLayout, List<Category> codeList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayout = itemLayout;
        mCodeList = codeList;
    }
    public void notifyDataChanged(List<Category> codeList){
        mCodeList = codeList;
        selectPosition = 0;
        notifyDataSetChanged();
    }
    @Override
    public FilterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder  =  new MyViewHolder(mLayoutInflater.inflate(mItemLayout, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mTagView.setText(mCodeList.get(position).getName());
        if (selectPosition==position) {
            holder.mTagView.setBackgroundResource(R.drawable.bg_filter_tag_selected);
            holder.mTagView.setTextColor(mContext.getResources().getColor(R.color.main_red));
        } else {
            holder.mTagView.setBackgroundResource(R.drawable.bg_filter_tag_normal);
            holder.mTagView.setTextColor(mContext.getResources().getColor(R.color.filter_tag_txt));
        }

        holder.mTagView.setListener(new TagWithListener() {
            @Override
            public void onItemSelect(Object item) {
                selectPosition = position;
                mItemClickListener.onItemClick(selectPosition);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCodeList==null?0:mCodeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TagView mTagView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTagView = (TagView) itemView.findViewById(R.id.txt_tag);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        if (mOnItemClickListener!=null) {
            mItemClickListener = mOnItemClickListener;
        }
    }
}
