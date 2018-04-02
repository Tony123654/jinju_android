package com.jinju.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Libra on 2017/10/31.
 */

public class MyDetailScrollView extends ScrollView {
    private static String TAG=MyDetailScrollView.class.getName();

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    private ScrollListener mScrollListener;

    public MyDetailScrollView(Context context) {
        super(context);
    }

    public MyDetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:

                if(mScrollListener!=null){
                    int contentHeight=getChildAt(0).getHeight();
                    int scrollHeight=getHeight();
                    Log.d(TAG,"scrollY:"+getScrollY()+"contentHeight:"+contentHeight+" scrollHeight"+scrollHeight+"object:"+this);

                    int scrollY=getScrollY();
                    mScrollListener.onScroll(scrollY);

                    if(scrollY+scrollHeight>=contentHeight||contentHeight<=scrollHeight){
                        mScrollListener.onScrollToBottom();
                    }else {
                        mScrollListener.notBottom();
                    }

                    if(scrollY==0){
                        mScrollListener.onScrollToTop();
                    }

                }

                break;
        }
        boolean result=super.onTouchEvent(ev);
        requestDisallowInterceptTouchEvent(false);

        return result;
    }

    public interface ScrollListener{
        void onScrollToBottom();
        void onScrollToTop();
        void onScroll(int scrollY);
        void notBottom();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
