package com.jinju.android.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Libra on 2017/6/6.
 */

public class HomeListView extends ListView{

    public HomeListView(Context context) {
        super(context);
    }

    public HomeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
