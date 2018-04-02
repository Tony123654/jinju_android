package com.jinju.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jinju.android.util.ImageUtils;
import com.jinju.android.widget.PhotoView;

import java.util.List;

/**
 * Created by Libra on 2017/11/30.
 */

public class MyImgPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mPicList;
    private Dialog dialog;
    private PhotoView photoView;

    public MyImgPagerAdapter(Context context,List<String> mPicList,Dialog dialog) {
        mContext = context;
        this.mPicList = mPicList;
        this.dialog = dialog;
    }
    public void notifyDataChanged(List<String> mPicList) {
        this.mPicList = mPicList;
        notifyDataSetChanged();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        photoView = new PhotoView(mContext);
        //开启缩放
        photoView.enable();
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ImageUtils.displayImage(photoView,mPicList.get(position));
        container.addView(photoView);
        return photoView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getCount() {

        return mPicList==null ? 0 :mPicList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }
}
