package com.jinju.android.dialog;

import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.jinju.android.R;


public class DoubleChoiceDialog extends Dialog {

	private String[] mItemsFirst;
	private String[] mItemsSecond;
	private String mCurrentItemFirst;
	private String mCurrentItemSecond;
	private WheelView mWheelViewFirst;
	private WheelView mWheelViewSecond;
	private OnCompletedListener mOnCompletedListener;

	public DoubleChoiceDialog(Context context) {
		super(context);
	}

	public DoubleChoiceDialog(Context context, int theme) {
		super(context, theme);
	}

	public void setFirstChoiceItems(List<String> itemListFirst) {
		mItemsFirst = itemListFirst.toArray(new String[itemListFirst.size()]);
	}
	
	public void setSecondChoiceItems(List<String> itemListSecond) {
		mItemsSecond = itemListSecond.toArray(new String[itemListSecond.size()]);
	}
	
	public void setFirstChoiceItems(List<String> itemListFirst, String currentItemFirst) {
		mItemsFirst = itemListFirst.toArray(new String[itemListFirst.size()]);
		mCurrentItemFirst = currentItemFirst;
		
		if(mWheelViewFirst != null) {
			setFirstCurrentItem();
		}
	}
	
	public void setSecondChoiceItems(List<String> itemListSecond, String currentItemSecond) {
		mItemsSecond = itemListSecond.toArray(new String[itemListSecond.size()]);
		mCurrentItemSecond = currentItemSecond;
		
		if(mWheelViewSecond != null) {
			setSecondCurrentItem();
		}
	}

	public void setOnCompletedListener(OnCompletedListener listener) {
		mOnCompletedListener = listener;
	}
	
	private void setFirstCurrentItem() {
		for (int i = 0; i < mItemsFirst.length; i++) {   
			if(TextUtils.equals(mItemsFirst[i], mCurrentItemFirst)) {
				mWheelViewFirst.setCurrentItem(i);
				break;
			}
		} 
	}
	
	private void setSecondCurrentItem() {
		for (int i = 0; i < mItemsSecond.length; i++) {
			if(TextUtils.equals(mItemsSecond[i], mCurrentItemSecond)) {
				mWheelViewSecond.setCurrentItem(i);
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_double_choice);

		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		TextView btnCancel = (TextView) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(mBtnCancelOnClickListener);

		TextView btnComplete = (TextView) findViewById(R.id.btn_complete);
		btnComplete.setOnClickListener(mBtnCompleteOnClickListener);
		
		mWheelViewFirst = (WheelView) findViewById(R.id.wheel_view_first);
		mWheelViewFirst.setShadowColor(0xFFFFFFFF, 0x00FFFFFF, 0x00FFFFFF);
		mWheelViewFirst.setWheelForeground(R.color.transparent);
		mWheelViewFirst.setWheelBackground(R.drawable.img_wheel_bg);
		mWheelViewFirst.addChangingListener(mOnFirstWheelChangedListener);
		ArrayWheelAdapter<String> adapterFirst = new ArrayWheelAdapter<String>(getContext(), mItemsFirst);
		adapterFirst.setTextColor(getContext().getResources().getColor(R.color.dark_gray));
		mWheelViewFirst.setViewAdapter(adapterFirst);
		setFirstCurrentItem();

		mWheelViewSecond = (WheelView) findViewById(R.id.wheel_view_second);
		mWheelViewSecond.setShadowColor(0xFFFFFFFF, 0x00FFFFFF, 0x00FFFFFF);
		mWheelViewSecond.setWheelForeground(R.color.transparent);
		mWheelViewSecond.setWheelBackground(R.drawable.img_wheel_bg);
		mWheelViewSecond.addChangingListener(mOnSecondWheelChangedListener);
		ArrayWheelAdapter<String> adapterSecond = new ArrayWheelAdapter<String>(getContext(), mItemsSecond);
		adapterSecond.setTextColor(getContext().getResources().getColor(R.color.dark_gray));
		mWheelViewSecond.setViewAdapter(adapterSecond);
		setSecondCurrentItem();
	}

	private View.OnClickListener mBtnCancelOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			dismiss();
		}

	};

	private View.OnClickListener mBtnCompleteOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			if (mOnCompletedListener != null) {
				int firstPosition = mWheelViewFirst.getCurrentItem();
				int secondPosition = mWheelViewSecond.getCurrentItem();
				mOnCompletedListener.onCompletedFinished(firstPosition, secondPosition);
			}

			dismiss();
		}

	};

	private OnWheelChangedListener mOnFirstWheelChangedListener = new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {

		}

	};
	
	private OnWheelChangedListener mOnSecondWheelChangedListener = new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {

		}

	};

	public interface OnCompletedListener {

		public void onCompletedFinished(int firstPosition, int secondPosition);

	}

}