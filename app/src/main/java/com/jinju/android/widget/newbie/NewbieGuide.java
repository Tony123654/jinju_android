package com.jinju.android.widget.newbie;

import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.api.WidgetLocation;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * NewbieGuide
 */
public class NewbieGuide implements GuideView.OnHighLightViewListener{

    public static final int CENTER = 0;

    private boolean mEveryWhereTouchable = true;
    private OnGuideChangedListener mOnGuideChangedListener;

    private List<HoleBean> mHoleList;

    private Activity mActivity;
    private GuideView mGuideView;
    private FrameLayout mParentView;

    private ImageView mBtnPacketImg;
    private ImageView redPacketImg;
    private ImageView redTipsImg;
    private ImageView homeTipsImg;
    private ImageView homeKnowImg;

    public NewbieGuide(Activity activity) {
        init(activity);
    }


    private NewbieGuide init(Activity activity) {
        mActivity = activity;
        mParentView = (FrameLayout) mActivity.getWindow().getDecorView();
        mGuideView = new GuideView(mActivity);
        mGuideView.setOnHighLightViewListener(this);
        mHoleList = new ArrayList<>();

        return this;
    }

    /**
     * 添加高亮
     */
    public NewbieGuide addHighLightView(View view, int type) {
        HoleBean hole = new HoleBean(mActivity, view, type);
        mHoleList.add(hole);
        return this;
    }

    /**
     * 传入宽高添加高亮
     */
    public NewbieGuide addHighLightView2(WidgetLocation widgetLocation, int type) {
        HoleBean hole = new HoleBean(mActivity, widgetLocation.getViewX(), widgetLocation.getViewY(), widgetLocation.getViewWidth(), widgetLocation.getViewHeight(), type);
        mHoleList.add(hole);
        return this;
    }


    /**
     * 红包图片
     */
    public NewbieGuide addRedPacketImg(int id, int offSetY) {
        int statusBarHeight = ScreenUtils.getStatusBarHeight(mActivity);
        redPacketImg = new ImageView(mActivity);
        redPacketImg.setImageResource(id);
        mGuideView.addView(redPacketImg, getLp(CENTER, offSetY + statusBarHeight));
        redPacketImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        return this;
    }

    /**
     * 红包按钮
     *
     * @param id
     * @param offSetY
     * @return
     */
    public NewbieGuide addBtnRedPacketImg(int id, int offSetY) {
        mBtnPacketImg = new ImageView(mActivity);
        mBtnPacketImg.setImageResource(id);

        mGuideView.addView(mBtnPacketImg, getLp(CENTER, offSetY));
        mBtnPacketImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        return this;
    }

    /**
     * 提示文字
     */
    public NewbieGuide addRedTipsImg(int id, int offsetX, int offsetY) {
        //获取状态栏的高度
        int statusBarHeight = ScreenUtils.getStatusBarHeight(mActivity);
        redTipsImg = new ImageView(mActivity);
        redTipsImg.setImageResource(id);
        mGuideView.addView(redTipsImg, getLp(offsetX, offsetY + statusBarHeight));
        return this;
    }

    /**
     *  首页 了解我们从这里开始
     */
    public NewbieGuide addIntroduceTipsImg(int id , int offsetX , int offsetY){
        homeTipsImg = new ImageView(mActivity);
        homeTipsImg.setImageResource(id);
        mGuideView.addView(homeTipsImg,getLp(offsetX,offsetY));
        return this;
    }

    /**
     * 首页 我知道了
     */
    public NewbieGuide addKnowTipsImg(int id , int offsetX , int offsetY){
        homeKnowImg = new ImageView(mActivity);
        homeKnowImg.setImageResource(id);
        mGuideView.addView(homeKnowImg,getLp(offsetX,offsetY));
        homeKnowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeNotCallBack();
            }
        });
        return this;
    }


    //生成提示文本
    private TextView generateMsgTv(String msg) {
        TextView msgTv = new TextView(mActivity);
        msgTv.setText(msg);
        msgTv.setTextColor(0xffffffff);
        msgTv.setTextSize(15);
        msgTv.setLineSpacing(ScreenUtils.dpToPx(mActivity, 5), 1f);
        msgTv.setGravity(Gravity.CENTER);
        return msgTv;
    }

    //生成我知道了文本
    private TextView generateKnowTv(String text) {
        TextView knowTv = new TextView(mActivity);
        knowTv.setTextColor(0xffffffff);
        knowTv.setTextSize(15);
        knowTv.setPadding(ScreenUtils.dpToPx(mActivity, 15), ScreenUtils.dpToPx(mActivity, 5), ScreenUtils.dpToPx(mActivity, 15),
                ScreenUtils.dpToPx(mActivity, 5));
//        knowTv.setBackgroundResource(R.drawable.solid_white_bg);
        knowTv.setText(text);
        knowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        return knowTv;
    }

    //生成布局参数
    private RelativeLayout.LayoutParams getLp(int offsetX, int offsetY) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(WRAP_CONTENT,
                WRAP_CONTENT);
        //水平方向
        if (offsetX == CENTER) {
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        } else if (offsetX < 0) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            lp.rightMargin = -offsetX;
        } else {
            lp.leftMargin = offsetX;
        }
        //垂直方向
        if (offsetY == CENTER) {
            lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        } else if (offsetY < 0) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            lp.bottomMargin = -offsetY;
        } else {
            lp.topMargin = offsetY;
        }
        return lp;
    }

    public void show() {

        int paddingTop = ScreenUtils.getStatusBarHeight(mActivity);
        mGuideView.setPadding(0, paddingTop, 0, 0);
        mGuideView.setDate(mHoleList);  //传入控件，设置蒙层透明
        mParentView.addView(mGuideView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT));

        if (mOnGuideChangedListener != null) mOnGuideChangedListener.onShowed();

        if (mEveryWhereTouchable) {//点击任意部位消失
            mGuideView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    remove();
                    return false;
                }
            });
        }

    }

    public void remove() {
        if (mGuideView != null && mGuideView.getParent() != null) {
            mGuideView.recycler();
            ((ViewGroup) mGuideView.getParent()).removeView(mGuideView);
            if (mOnGuideChangedListener != null) {
                mOnGuideChangedListener.onRemoved();
            }
        }
    }
    public void removeNotCallBack() {
        if (mGuideView != null && mGuideView.getParent() != null) {
            mGuideView.recycler();
            ((ViewGroup) mGuideView.getParent()).removeView(mGuideView);
        }
    }

    public NewbieGuide setEveryWhereTouchable(boolean everyWhereTouchable) {
        mEveryWhereTouchable = everyWhereTouchable;
        return this;
    }

    public void setOnGuideChangedListener(OnGuideChangedListener onGuideChangedListener) {
        this.mOnGuideChangedListener = onGuideChangedListener;
    }

    //浮层显示后的回调
    public interface OnGuideChangedListener {
        void onShowed();
        void onRemoved();
    }

    /**
     * 高亮区域点击事件
     */
    @Override
    public void onClickHighLight() {
        remove();
    }
}
