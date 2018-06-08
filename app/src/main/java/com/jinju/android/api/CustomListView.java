package com.jinju.android.api;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jinju.android.R;


/**
 * Created by wywl on 2016/4/16.
 */
public class CustomListView  extends ListView implements AbsListView.OnScrollListener {

    private Context context;
    /** 实际的padding的距离与界面上偏移距离的比例 */
    private final static int RATIO = 3;
    private final static int RELEASE_TO_REFRESH = 0;
    private final static int PULL_TO_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;

    /** 加载中 */
    private final static int ENDINT_LOADING = 1;
    /** 手动完成刷新 */
    private final static int ENDINT_MANUAL_LOAD_DONE = 2;
    /** 自动完成刷新 */
    private final static int ENDINT_AUTO_LOAD_DONE = 3;

    /**
     * 0:RELEASE_TO_REFRESH;
     */
    private int mHeadState;
    /**
     * 0:完成/等待刷新 ;
     * <p>
     * 1:加载中
     */
    private int mEndState;

    /** 可以加载更多？ */
    private boolean mCanLoadMore = false;
    /** 可以下拉刷新？ */
    private boolean mCanRefresh = false;
    /** 可以自动加载更多吗？（注意，先判断是否有加载更多，如果没有，这个flag也没有意义） */
    private boolean mIsAutoLoadMore = true;
    /** 下拉刷新后是否显示第一条Item */
    private boolean mIsMoveToFirstItemAfterRefresh = false;
    // ============================================================================

    private LayoutInflater mInflater;

    private LinearLayout mHeadView;

    private LinearLayout lytHeadView;
    private TextView mLastUpdatedTextView;
    private ImageView mProgressBar;

    private View mEndRootView;
    private ProgressBar mEndLoadProgressBar;
    public TextView mEndLoadTipsTextView;

    /** headView动画 */
    private RotateAnimation mArrowAnim;
    /** headView反转动画 */
    private RotateAnimation mArrowReverseAnim;

    /** 用于保证startY的值在一个完整的touch事件中只被记录一次 */
    private boolean mIsRecored;

    private int mHeadViewWidth;
    private int mHeadViewHeight;
    private int mHeadShowViewHeight;

    private int mStartY;
    private boolean mIsBack;

    private int mFirstItemIndex;
    private int mLastItemIndex;
    private int mCount;
    private boolean mEnoughCount;// 足够数量充满屏幕？

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;


    public CustomListView(Context pContext, AttributeSet pAttrs) {
        super(pContext, pAttrs);
        this.context=pContext;
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        init(pContext);
    }

    public CustomListView(Context pContext) {
        super(pContext);
        this.context=pContext;
        init(pContext);
    }

    public CustomListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
        super(pContext, pAttrs, pDefStyle);
        this.context=pContext;
        init(pContext);
    }

    /**
     * 初始化操作
     *
     * @param pContext
     * @date 2013-11-20 下午4:10:46
     * @version 1.0load_more
     */
    private void init(Context pContext) {
        setCacheColorHint(pContext.getResources().getColor(R.color.transparent));
        mInflater = LayoutInflater.from(pContext);

        addHeadView();
        setOnScrollListener(this);

        initPullImageAnimation(0);
    }

    /**
     * 添加下拉刷新的HeadView
     *
     * @date 2013-11-11 下午9:48:26
     * @version 1.0
     */
    AnimationDrawable animationDrawable;

    private void addHeadView() {
        mHeadView = (LinearLayout) mInflater.inflate(R.layout.head, null);


        mProgressBar = (ImageView) mHeadView
                .findViewById(R.id.head_progressBar);

        lytHeadView=(LinearLayout)mHeadView.findViewById(R.id.lytHeadView);

        animationDrawable = (AnimationDrawable) mProgressBar.getDrawable();
        animationDrawable.start();
        mLastUpdatedTextView = (TextView) mHeadView
                .findViewById(R.id.head_lastUpdatedTextView);

        measureView(mHeadView);
        mHeadViewHeight = mHeadView.getMeasuredHeight();
        mHeadViewWidth = mHeadView.getMeasuredWidth();

        measureView(lytHeadView);
        mHeadShowViewHeight=lytHeadView.getMeasuredHeight();


        mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
        mHeadView.invalidate();

        //        Log.v("size", "width:" + mHeadViewWidth + " height:" + mHeadViewHeight);

        addHeaderView(mHeadView, null, false);

        mHeadState = DONE;
    }

    /**
     * 添加加载更多FootView
     *
     * @date 2013-11-11 下午9:52:37
     * @version 1.0
     */
    //    private void addFooterView() {
    //        mEndRootView = mInflater.inflate(R.layout.list_footer_more, null);
    //        mEndRootView.setVisibility(View.VISIBLE);
    //        mEndLoadProgressBar = (ProgressBar) mEndRootView
    //                .findViewById(R.id.pull_to_refresh_progress);
    //        mEndLoadTipsTextView = (TextView) mEndRootView
    //                .findViewById(R.id.load_more);
    //        mEndRootView.setOnClickListener(new OnClickListener() {
    //
    //            @Override
    //            public void onClick(View v) {
    //                if (mCanLoadMore) {
    //                    if (mCanRefresh) {
    //                        // 当可以下拉刷新时，如果FootView没有正在加载，并且HeadView没有正在刷新，才可以点击加载更多。
    //                        if (mEndState != ENDINT_LOADING
    //                                && mHeadState != REFRESHING) {
    //                            mEndState = ENDINT_LOADING;
    //                            onLoadMore();
    //                        }
    //                    } else if (mEndState != ENDINT_LOADING) {
    //                        // 当不能下拉刷新时，FootView不正在加载时，才可以点击加载更多。
    //                        mEndState = ENDINT_LOADING;
    //                        onLoadMore();
    //                    }
    //                }
    //            }
    //        });
    //
    //        addFooterView(mEndRootView);
    //
    //        if (mIsAutoLoadMore) {
    //            mEndState = ENDINT_AUTO_LOAD_DONE;
    //        } else {
    //            mEndState = ENDINT_MANUAL_LOAD_DONE;
    //        }
    //    }


    /**
     * 实例化下拉刷新的箭头的动画效果
     *
     * @param pAnimDuration
     *            动画运行时长
     * @date 2013-11-20 上午11:53:22
     * @version 1.0
     */
    private void initPullImageAnimation(final int pAnimDuration) {

        int _Duration;

        if (pAnimDuration > 0) {
            _Duration = pAnimDuration;
        } else {
            _Duration = 250;
        }
        // Interpolator _Interpolator;
        // switch (pAnimType) {
        // case 0:
        // _Interpolator = new AccelerateDecelerateInterpolator();
        // break;
        // case 1:
        // _Interpolator = new AccelerateInterpolator();
        // break;
        // case 2:
        // _Interpolator = new AnticipateInterpolator();
        // break;
        // case 3:
        // _Interpolator = new AnticipateOvershootInterpolator();
        // break;
        // case 4:
        // _Interpolator = new BounceInterpolator();
        // break;
        // case 5:
        // _Interpolator = new CycleInterpolator(1f);
        // break;
        // case 6:
        // _Interpolator = new DecelerateInterpolator();
        // break;
        // case 7:
        // _Interpolator = new OvershootInterpolator();
        // break;
        // default:
        // _Interpolator = new LinearInterpolator();
        // break;
        // }

        Interpolator _Interpolator = new LinearInterpolator();

        mArrowAnim = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mArrowAnim.setInterpolator(_Interpolator);
        mArrowAnim.setDuration(_Duration);
        mArrowAnim.setFillAfter(true);

        mArrowReverseAnim = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mArrowReverseAnim.setInterpolator(_Interpolator);
        mArrowReverseAnim.setDuration(_Duration);
        mArrowReverseAnim.setFillAfter(true);
    }

    /**
     * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
     *
     * @param pChild
     * @date 2013-11-20 下午4:12:07
     * @version 1.0
     */
    private void measureView(View pChild) {
        ViewGroup.LayoutParams p = pChild.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;

        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        pChild.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 为了判断滑动到ListView底部没
     */
    @Override
    public void onScroll(AbsListView pView, int pFirstVisibleItem,
                         int pVisibleItemCount, int pTotalItemCount) {
        mFirstItemIndex = pFirstVisibleItem;
        mLastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
        mCount = pTotalItemCount - 2;
        if (pTotalItemCount > pVisibleItemCount) {
            mEnoughCount = true;
            // endingView.setVisibility(View.VISIBLE);
        } else {
            mEnoughCount = false;
        }
    }

    /**
     * 这个方法，可能有点乱，大家多读几遍就明白了。
     */
    @Override
    public void onScrollStateChanged(AbsListView pView, int pScrollState) {
        if (mCanLoadMore) {// 存在加载更多功能
            if (mLastItemIndex == mCount && pScrollState == SCROLL_STATE_IDLE) {
                // SCROLL_STATE_IDLE=0，滑动停止
                if (mEndState != ENDINT_LOADING) {
                    if (mIsAutoLoadMore) {// 自动加载更多，我们让FootView显示 “更 多”
                        if (mCanRefresh) {
                            // 存在下拉刷新并且HeadView没有正在刷新时，FootView可以自动加载更多。
                            if (mHeadState != REFRESHING) {
                                // FootView显示 : 更 多 ---> 加载中...
                                mEndState = ENDINT_LOADING;
                                //                                onLoadMore();
//                                changeEndViewByState();
                                System.out.println("=================44444444444444444444======================");

                            }
                        } else {// 没有下拉刷新，我们直接进行加载更多。
                            // FootView显示 : 更 多 ---> 加载中...
                            mEndState = ENDINT_LOADING;
                            //                            onLoadMore();
//                            changeEndViewByState();
                            System.out.println("=================5555555555555555555555555=======================");
                        }
                    } else {// 不是自动加载更多，我们让FootView显示 “点击加载”
                        // FootView显示 : 点击加载 ---> 加载中...
                        mEndState = ENDINT_MANUAL_LOAD_DONE;
//                        changeEndViewByState();
                        System.out.println("=================66666666666666666=======================");
                    }
                }
            }
        } else if (mEndRootView != null
                && mEndRootView.getVisibility() == VISIBLE) {
            // 突然关闭加载更多功能之后，我们要移除FootView。
            System.out.println("==================ddddddddddddd========================");
            mEndRootView.setVisibility(View.VISIBLE);
            this.removeFooterView(mEndRootView);
        }

//        switch(pScrollState){
//            case OnScrollListener.SCROLL_STATE_IDLE://空闲状态
//                //TODO 购物车显现
//                System.out.println("1111111111111111111111111");
//                Intent intent = new Intent(
//                        Constants.SHOP_CAR_SHOW);
//                context.sendBroadcast(intent);
//
//                break;
//            case OnScrollListener.SCROLL_STATE_FLING://滚动状态
//                //TODO 购物车隐藏
//                System.out.println("2222222222222222222222222");
//                Intent intent1 = new Intent(
//                        Constants.SHOP_CAR_HIDE);
//                context.sendBroadcast(intent1);
//                break;
//            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
//                System.out.println("3333333333333333333333333");
//                //TODO 购物车隐藏
//                Intent intent2 = new Intent(
//                        Constants.SHOP_CAR_HIDE);
//                context.sendBroadcast(intent2);
//                break;
//        }
    }

    /**
     * 改变加载更多状态
     *
     * @date 2013-11-11 下午10:05:27
     * @version 1.0
     */
//    private void changeEndViewByState() {
//        if (mCanLoadMore) {
//            // 允许加载更多
//            switch (mEndState) {
//                case ENDINT_LOADING:// 刷新中
//
//                    // 加载中...
//                    if (mEndLoadTipsTextView.getText().equals(
//                            R.string.p2refresh_doing_end_refresh)) {
//                        break;
//                    }
//                    mEndLoadTipsTextView
//                            .setText(R.string.p2refresh_doing_end_refresh);
//                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
//                    mEndLoadProgressBar.setVisibility(View.VISIBLE);
//                    System.out.println("=================1111111111111111=======================");
//                    break;
//                case ENDINT_MANUAL_LOAD_DONE:// 手动刷新完成
//
//                    // 点击加载
//                    mEndLoadTipsTextView
//                            .setText(R.string.p2refresh_end_click_load_more);
//                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
//                    mEndLoadProgressBar.setVisibility(View.GONE);
//                    mEndRootView.setVisibility(View.VISIBLE);
//                    System.out.println("=================222222222222222222222=======================");
//                    break;
//                case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成
//                    // 更 多
//                    mEndLoadTipsTextView.setText(R.string.p2refresh_end_load_more);
//                    mEndLoadTipsTextView.setVisibility(View.GONE);
//                    mEndLoadProgressBar.setVisibility(View.GONE);
//                    mEndRootView.setVisibility(View.VISIBLE);
//                    //                     setTextchange();
//                    System.out.println("=================333333333333333333333=======================");
//                    //                    mEndRootView.setVisibility(View.VISIBLE);
//                    this.removeFooterView(mEndRootView);
//                    break;
//                default:
//                    // 原来的代码是为了： 当所有item的高度小于ListView本身的高度时，
//                    // 要隐藏掉FootView，大家自己去原作者的代码参考。
//
//                    //                     if (enoughCount) {
//                    //                     endRootView.setVisibility(View.VISIBLE);
//                    //                     } else {
//                    //                     endRootView.setVisibility(View.GONE);
//                    //                     }
//                    break;
//            }
//        }
//    }

    /**
     *
     */
    public boolean onTouchEvent(MotionEvent event) {

        if (mCanRefresh) {
            if (mCanLoadMore && mEndState == ENDINT_LOADING) {
                // 如果存在加载更多功能，并且当前正在加载更多，默认不允许下拉刷新，必须加载完毕后才能使用。
                return super.onTouchEvent(event);
            }

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (mFirstItemIndex == 0 && !mIsRecored) {
                        mIsRecored = true;
                        mStartY = (int) event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    System.out.println("---action up-----");
                    System.out.println("最后位置为：" + "(" + event.getX() + " , " + event.getY() + ")");

                    if (mHeadState != REFRESHING && mHeadState != LOADING) {
                        if (mHeadState == DONE) {

                        }
                        if (mHeadState == PULL_TO_REFRESH) {
                            mHeadState = DONE;
                            changeHeaderViewByState();
                        }
                        if (mHeadState == RELEASE_TO_REFRESH) {
                            mHeadState = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }

                    mIsRecored = false;
                    mIsBack = false;

                    break;

                case MotionEvent.ACTION_MOVE:

                    int tempY = (int) event.getY();

                    if (!mIsRecored && mFirstItemIndex == 0) {
                        mIsRecored = true;
                        mStartY = tempY;
                    }

                    if (mHeadState != REFRESHING && mIsRecored
                            && mHeadState != LOADING) {

                        // 保证在设置padding的过程中，当前的位置一直是在head，
                        // 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                        // 可以松手去刷新了
                        if (mHeadState == RELEASE_TO_REFRESH) {

                            setSelection(0);

                            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                            if (((tempY - mStartY) / RATIO < mHeadViewHeight)
                                    && (tempY - mStartY) > 0) {
                                mHeadState = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            }
                            // 一下子推到顶了
                            else if (tempY - mStartY <= 0) {
                                mHeadState = DONE;
                                changeHeaderViewByState();
                            }

                            if (((tempY - mStartY) / RATIO < mHeadViewHeight)
                                    && (tempY - mStartY) > 0) {
                                mHeadState = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            }



                            // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (mHeadState == PULL_TO_REFRESH) {

                            setSelection(0);

                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - mStartY) / RATIO >= mHeadShowViewHeight) {
                                mHeadState = RELEASE_TO_REFRESH;
                                mIsBack = true;
                                changeHeaderViewByState();
                            } else if (tempY - mStartY <= 0) {
                                mHeadState = DONE;
                                changeHeaderViewByState();
                            }
                        }

                        if (mHeadState == DONE) {
                            if (tempY - mStartY > 0) {
                                mHeadState = PULL_TO_REFRESH;
                                changeHeaderViewByState();
                            }
                        }

                        if (mHeadState == PULL_TO_REFRESH) {
                            mHeadView.setPadding(0, -1 * mHeadViewHeight
                                    + (tempY - mStartY) / RATIO, 0, 0);

                        }

                        if (mHeadState == RELEASE_TO_REFRESH) {
                            mHeadView.setPadding(0, (tempY - mStartY) / RATIO
                                    - mHeadViewHeight, 0, 0);
                        }
                    }
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当HeadView状态改变时候，调用该方法，以更新界面
     *
     * @date 2013-11-20 下午4:29:44
     * @version 1.0
     */
    private void changeHeaderViewByState() {
        switch (mHeadState) {
            case RELEASE_TO_REFRESH:

                // mProgressBar.setVisibility(View.GONE);
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }

                // reportImage.setImageResource(R.drawable.listviewheadjing);
                // mTipsTextView.setVisibility(View.VISIBLE);
                mLastUpdatedTextView.setVisibility(View.GONE);

                // 松开刷新
                // mTipsTextView.setText(R.string.p2refresh_release_refresh);

                break;
            case PULL_TO_REFRESH:
                // mProgressBar.setVisibility(View.GONE);
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }

                // reportImage.setImageResource(R.drawable.listviewheadjing);
                // mTipsTextView.setVisibility(View.VISIBLE);
                mLastUpdatedTextView.setVisibility(View.GONE);

                // 是由RELEASE_To_REFRESH状态转变来的
                if (mIsBack) {
                    mIsBack = false;

                    // 下拉刷新
                    // mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
                } else {
                    // 下拉刷新
                    // mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
                }
                break;

            case REFRESHING:
                mHeadView.setPadding(0, -1*(mHeadViewHeight-mHeadShowViewHeight), 0, 0);
                mProgressBar.setVisibility(View.VISIBLE);
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.start();
                }
                // reportImage.setImageResource(R.drawable.listviewhead);
                // 正在刷新...
                // mTipsTextView.setText(R.string.p2refresh_doing_head_refresh);
                mLastUpdatedTextView.setVisibility(View.GONE);

                break;
            case DONE:

                mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);

                // 此处可以改进，同上所述。
                if (animationDrawable != null && !animationDrawable.isRunning()) {
                    animationDrawable.stop();
                }
                // mProgressBar.setVisibility(View.GONE);
                // reportImage.setImageResource(R.drawable.listviewheadjing);
                // reportImage.setVisibility(View.GONE);

                // 下拉刷新
                // mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
                // mLastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
        }
    }


    //    @Override
    //    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    //        super.onMeasure(widthMeasureSpec, mExpandSpec);
    //    }

    /**
     * 下拉刷新监听接口
     *
     * @date 2013-11-20 下午4:50:51
     * @version 1.0
     */
    public interface OnRefreshListener {

        public void onRefresh();
    }

    /**
     * 加载更多监听接口
     *
     * @date 2013-11-20 下午4:50:51
     * @version 1.0
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public void setOnRefreshListener(OnRefreshListener pRefreshListener) {

        if (pRefreshListener != null) {
            mRefreshListener = pRefreshListener;
            mCanRefresh = true;
        }
    }

    public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
        if (pLoadMoreListener != null) {
            mLoadMoreListener = pLoadMoreListener;
            mCanLoadMore = true;
//            if (mCanLoadMore && getFooterViewsCount() == 0) {
//                //                addFooterView();
//            }
        }
    }

    /**
     * 正在下拉刷新
     *
     * @date 2013-11-20 下午4:45:47
     * @version 1.0
     */
    public void onRefresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    public void onRefreshing() {
        mHeadState = REFRESHING;
                changeHeaderViewByState();
        onRefresh();
    }

    /**
     * 下拉刷新完成
     *
     * @date 2013-11-20 下午4:44:12
     * @version 1.0
     */
        public void onRefreshComplete() {
            // 下拉刷新后是否显示第一条Itemadd
            if (mIsMoveToFirstItemAfterRefresh)
                setSelection(0);

            mHeadState = DONE;
            // 最近更新: Time
//            Date date = new Date();
//
//            String time = new TimeReversal().getDateTimeString(date.getTime(), 1);
            // mLastUpdatedTextView.setText(getResources().getString(
            // R.string.p2refresh_refresh_nowtime)
            // + ":" + time);
            changeHeaderViewByState();
        }

    /**
     * 正在加载更多，FootView显示 ： 加载中...
     *
     * @date 2013-11-20 下午4:35:51
     * @version 1.0
     */
    //    private void onLoadMore() {
    //        if (mLoadMoreListener != null) {
    //            // 加载中...
    //            System.out.println("草泥马比="+mEndLoadTipsTextView);
    //            mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
    //            mEndLoadTipsTextView.setVisibility(View.VISIBLE);
    //            mEndLoadProgressBar.setVisibility(View.VISIBLE);
    //            mLoadMoreListener.onLoadMore();
    //        }
    //    }

    /**
     * 加载更多完成
     *
     * @date 2013-11-11 下午10:21:38
     * @version 1.0
     */
    //    public void onLoadMoreComplete() {
    //        if (mIsAutoLoadMore) {
    //            mEndState = ENDINT_AUTO_LOAD_DONE;
    //        } else {
    //            mEndState = ENDINT_MANUAL_LOAD_DONE;
    //        }
    //        changeEndViewByState();
    //    }

    /**
     * 主要更新一下刷新时间啦！
     *
     * @param adapter
     * @date 2013-11-20 下午5:35:51
     * @version 1.0
     */
    // Subject subject;
    // SubjectDao subjectdao;

    public void setAdapter(BaseAdapter adapter) {
        // 最近更新: Time
        // SubjectId = (Long) getTag();
        // if (SubjectId != null) {
        //
        // subjectdao = new SubjcetDaoImpl(getContext());
        // Long time = subjectdao.querysubject(SubjectId).get(0)
        // .getRefreshtime();
        // if (time != null) {
        // try {
        // mLastUpdatedTextView.setText(getResources().getString(
        // R.string.p2refresh_refresh_lasttime)
        // +":"+ (DateUtil.longToString(time, "MM-dd HH:mm")));
        // } catch (NotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (ParseException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // }
        //
        // } else {

        // mLastUpdatedTextView.setText(getResources().getString(
        // R.string.p2refresh_refresh_lasttime)
        // + "今天"+new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA)
        // .format(new Date()));
        // }
        super.setAdapter(adapter);
    }

    //    public void setTextchange() {
    //        mEndRootView = mInflater.inflate(R.layout.list_footer_more, null);
    //        mEndRootView.setVisibility(View.VISIBLE);
    //        mEndLoadTipsTextView = (TextView) mEndRootView
    //                .findViewById(R.id.load_more);
    //        mEndLoadTipsTextView.setText(R.string.p2refresh_end);
    //        mEndLoadTipsTextView.setVisibility(View.VISIBLE);
    //
    //    }
}
