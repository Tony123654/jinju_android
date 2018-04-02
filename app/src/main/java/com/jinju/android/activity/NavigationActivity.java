package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jinju.android.R;
import com.jinju.android.api.Index;

import java.util.ArrayList;

/**
 * Created by Libra on 2017/9/30.
 * 宣传页
 */

public class NavigationActivity extends BaseActivity {

    private ViewPager vp;
    private ArrayList<View> list;
    private ImageView btn;
    private Index mIndex;
    private ImageView[] pointImgViews; //装载导航小圆点
    private ViewGroup pointGroup; //与布局文件的小黑点位置ID相关联


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        Intent intent = getIntent();
        mIndex = (Index) intent.getSerializableExtra("index");
        initView();
//        addPoints();
    }

    private void initView() {

        vp = (ViewPager) findViewById(R.id.vp_navigation);
        pointGroup = (ViewGroup)findViewById(R.id.viewPoints);
        LayoutInflater inflater = getLayoutInflater();

        list = new ArrayList<View>();
        View view1 = inflater.inflate(R.layout.layout_navigation_page1,null);
        View view2 = inflater.inflate(R.layout.layout_navigation_page2,null);
        View view3 = inflater.inflate(R.layout.layout_navigation_page3,null);
        View view4 = inflater.inflate(R.layout.layout_navigation_page4,null);
        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);
        btn = (ImageView) view4.findViewById(R.id.btn_into);
        btn.setOnClickListener(mOnClickListener);
        vp.addOnPageChangeListener(mAddOnPageChangeListener);
        vp.setAdapter(myAdapter);

    }
    private void addPoints(){
        ImageView pointImgView;
        pointImgViews = new ImageView[list.size()]; //确定小圆点的个数

        //动态添加小圆点
        for(int i=0; i<list.size(); i++){
            pointImgView = new ImageView(NavigationActivity.this);
            pointImgView.setLayoutParams(new ViewGroup.LayoutParams(25,25)); //设置圆点大小
            pointImgView.setPadding(5, 0, 5, 0);
            pointImgViews[i] = pointImgView;

            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0)
                pointImgViews[i].setImageDrawable(getResources().getDrawable(
                        R.mipmap.icon_red));
            else
                pointImgViews[i].setImageDrawable(getResources().getDrawable(R.mipmap.icon_gray));
            // 将imageviews添加到小圆点视图组
            pointGroup.addView(pointImgViews[i]);
        }
    }
    private ViewPager.OnPageChangeListener mAddOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            for (int i = 0; i < pointImgViews.length; i++) {
//                // 当前view下设置小圆点为未选中状态
//                pointImgViews[i].setImageDrawable(getResources().getDrawable(
//                        R.mipmap.icon_gray));
//                //设置小圆点为选中状态
//                if(position == i)
//                    pointImgViews[i].setImageDrawable(getResources().getDrawable(
//                            R.mipmap.icon_red));
//            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private PagerAdapter myAdapter = new PagerAdapter(){
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
            if(null != mIndex) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("index", mIndex);
                intent.putExtras(bundle);
            }
            startActivity(intent);
            finish();
        }
    };

}
