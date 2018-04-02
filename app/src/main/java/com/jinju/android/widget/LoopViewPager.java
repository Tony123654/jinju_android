package com.jinju.android.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A ViewPager subclass enabling infinte scrolling of the viewPager elements
 * 
 * When used for paginating views (in opposite to fragments), no code changes
 * should be needed only change xml's from <android.support.v4.view.ViewPager>
 * to <com.imbryk.viewPager.LoopViewPager>
 * 
 * If "blinking" can be seen when paginating to first or last view, simply call
 * seBoundaryCaching( true ), or change DEFAULT_BOUNDARY_CASHING to true
 * 
 * When using a FragmentPagerAdapter or FragmentStatePagerAdapter, additional
 * changes in the adapter must be done. The adapter must be prepared to create 2
 * extra items e.g.:
 * 
 * The original adapter creates 4 items: [0,1,2,3] The modified adapter will
 * have to create 6 items [0,1,2,3,4,5] with mapping
 * realPosition=(position-1)%count [0->3, 1->0, 2->1, 3->2, 4->3, 5->0]
 */
public class LoopViewPager extends ViewPager {

	private static final boolean DEFAULT_BOUNDARY_CASHING = false;
	public static final int DEFAULT_INTERVAL = 5000;

	OnPageChangeListener mOuterPageChangeListener;
	private LoopPagerAdapterWrapper mAdapter;
	private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;

	private Handler mCountdownHandler;
	private CountdownTask mCountdownTask;

	private boolean mIsAutoScroll = false;
	private boolean mIsStopByTouch = false;

	public LoopViewPager(Context context) {
		super(context);
		initLoopViewPager();
	}

	public LoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLoopViewPager();
	}

	private void initLoopViewPager() {
		super.setOnPageChangeListener(onPageChangeListener);
		mCountdownHandler = new Handler();
		mCountdownTask = new CountdownTask();
	}

	public void startAutoScroll() {

		mIsAutoScroll = true;
		mCountdownHandler.removeCallbacks(mCountdownTask);
		mCountdownHandler.postDelayed(mCountdownTask, DEFAULT_INTERVAL);

	}

	public void stopAutoScroll() {
		mIsAutoScroll = false;
		mCountdownHandler.removeCallbacks(mCountdownTask);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN && mIsAutoScroll) {
			mIsStopByTouch = true;
			stopAutoScroll();
		} else if (action == MotionEvent.ACTION_UP && mIsStopByTouch) {
			mIsStopByTouch = false;
			startAutoScroll();
		}

		return super.dispatchTouchEvent(event);
	}

	/**
	 * helper function which may be used when implementing FragmentPagerAdapter
	 * 
	 * @param position
	 * @param count
	 * @return (position-1)%count
	 */
	public static int toRealPosition(int position, int count) {
		position = position - 1;
		if (position < 0) {
			position += count;
		} else {
			position = position % count;
		}
		return position;
	}

	/**
	 * If set to true, the boundary views (i.e. first and last) will never be
	 * destroyed This may help to prevent "blinking" of some views
	 * 
	 * @param flag
	 */
	public void setBoundaryCaching(boolean flag) {
		mBoundaryCaching = flag;
		if (mAdapter != null) {
			mAdapter.setBoundaryCaching(flag);
		}
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		mAdapter = new LoopPagerAdapterWrapper(adapter);

		mAdapter.setBoundaryCaching(mBoundaryCaching);
		super.setAdapter(mAdapter);
		setCurrentItem(0, false);
	}

	@Override
	public PagerAdapter getAdapter() {
		return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
	}

	@Override
	public int getCurrentItem() {
		return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
	}

	public void setCurrentItem(int item, boolean smoothScroll) {
		int realItem = mAdapter.toInnerPosition(item);
		super.setCurrentItem(realItem, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {
		if (getCurrentItem() != item) {
			setCurrentItem(item, true);
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mOuterPageChangeListener = listener;
	};

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		private float mPreviousOffset = -1;
		private float mPreviousPosition = -1;

		@Override
		public void onPageSelected(int position) {

			int realPosition = mAdapter.toRealPosition(position);
			if (mPreviousPosition != realPosition) {
				mPreviousPosition = realPosition;
				if (mOuterPageChangeListener != null) {
					mOuterPageChangeListener.onPageSelected(realPosition);
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			int realPosition = position;
			if (mAdapter != null) {
				realPosition = mAdapter.toRealPosition(position);

				if (positionOffset == 0 && mPreviousOffset == 0 && (position == 0 || position == mAdapter.getCount() - 1)) {
					setCurrentItem(realPosition, false);
				}
			}

			mPreviousOffset = positionOffset;
			if (mOuterPageChangeListener != null) {
				if (realPosition != mAdapter.getRealCount() - 1) {
					mOuterPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
				} else {
					if (positionOffset > 0.5) {
						mOuterPageChangeListener.onPageScrolled(0, 0, 0);
					} else {
						mOuterPageChangeListener.onPageScrolled(realPosition, 0, 0);
					}
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (mAdapter != null) {
				int position = LoopViewPager.super.getCurrentItem();
				int realPosition = mAdapter.toRealPosition(position);
				if (state == ViewPager.SCROLL_STATE_IDLE && (position == 0 || position == mAdapter.getCount() - 1)) {
					setCurrentItem(realPosition, false);
				}
			}
			if (mOuterPageChangeListener != null) {
				mOuterPageChangeListener.onPageScrollStateChanged(state);
			}
		}
	};

	private class CountdownTask implements Runnable {

		@Override
		public void run() {
			if (mAdapter != null && mAdapter.getRealCount() > 0) {
				setCurrentItem(getCurrentItem() + 1);
				mCountdownHandler.postDelayed(this, DEFAULT_INTERVAL);
			}
		}
	};

}
