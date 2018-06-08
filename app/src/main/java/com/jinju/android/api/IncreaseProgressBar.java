package com.jinju.android.api;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * @author Luki
 */
public class IncreaseProgressBar extends ProgressBar {

    private int targetProgress;
    private static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            IncreaseProgressBar bar = (IncreaseProgressBar) msg.obj;
            if (msg.what == 0) {
                bar.increase(msg.arg1);
            } else {
                bar.decrease(msg.arg1);
            }
        };
    };

    /**
     * @param context
     */
    public IncreaseProgressBar(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public IncreaseProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public IncreaseProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public synchronized void setProgress(double progress) {
        setProgress((int) progress);
    }

    public synchronized void setProgressWithOutAnim(double progress) {
        super.setProgress((int) progress);//设置多少的
    }

    /* (non-Javadoc)
     * @see android.widget.ProgressBar#setProgress(int)
     */
    @Override
    public synchronized void setProgress(int progress) {
        handler.removeCallbacksAndMessages(this);
        if (progress > 0) {
            this.targetProgress = progress;//这个是初始化的进度
            increase(0);
        } else {
            super.setProgress(0);
        }

    }

    /**
     * 从0开始然后每次5毫秒增加刷新一次
     */
    private void increase(int progress) {
        progress++;// 每次++
        super.setProgress(progress);
        if (this.targetProgress > progress) {
            handler.sendMessageDelayed(handler.obtainMessage(0, progress, 0, this), 5);
        }
    }

    private void decrease(int progress) {
        progress--;
        super.setProgress(progress);
        if (this.targetProgress < progress) {
            handler.sendMessageDelayed(handler.obtainMessage(-1, progress, 0, this), 5);
        }
    }

}
