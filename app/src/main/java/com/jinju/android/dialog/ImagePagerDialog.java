package com.jinju.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.MyImgPagerAdapter;

import java.util.List;


/**
 * Created by Libra on 2017/11/7.
 */

public class ImagePagerDialog {
    private List<String> mPicList;
    private ViewPager mViewPager;
    private Dialog dialog;
    private Activity activity;
    private Window window;
    private TextView mTxtPicCount;
    private MyImgPagerAdapter adapter;


    public ImagePagerDialog(Activity activity, List<String> mPicList) {
        this.mPicList = mPicList;
        this.activity = activity;
        init();
    }

    private void init() {

        dialog = new Dialog(activity, R.style.scroll_picture_dialog);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_dialogpager_img, null);
        dialog.setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        window =dialog.getWindow();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = display.getWidth();
        params.y = display.getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(params);

        adapter = new MyImgPagerAdapter(activity,mPicList,dialog);

        mTxtPicCount = (TextView) contentView.findViewById(R.id.txt_pic_count);
        mViewPager = (ViewPager)contentView.findViewById(R.id.view_pager);
        mViewPager.addOnPageChangeListener(addOnPageChangeListener);
        mViewPager.setAdapter(adapter);

    }

    private ViewPager.OnPageChangeListener addOnPageChangeListener  = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if (mPicList!=null&&mPicList.size() > 0) {
                mTxtPicCount.setText(position+1+"/"+mPicList.size());
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void showImgPager(List<String> picList ,int mCurrent) {
        mPicList = picList;
        if (picList!=null&&picList.size()>0) {
            mTxtPicCount.setText(mCurrent+1+"/"+picList.size());
        }
        adapter.notifyDataChanged(picList);
        mViewPager.setCurrentItem(mCurrent);
        dialog.show();
    }
    public void dismiss() {
        dialog.dismiss();
    }
}
