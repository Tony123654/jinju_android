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

public class SingleChoiceDialog extends Dialog {

	private String[] mItems;
	private String mCurrentItem;
	private WheelView mWheelView;
	private OnCompletedListener mOnCompletedListener;

	public SingleChoiceDialog(Context context) {
		super(context);
	}

	public SingleChoiceDialog(Context context, int theme) {
		super(context, theme);
	}

	public void setSingleChoiceItems(List<String> itemList) {
		mItems = itemList.toArray(new String[itemList.size()]);
	}
	
	public void setSingleChoiceItems(List<String> itemList, String currentItem) {
		mItems = itemList.toArray(new String[itemList.size()]);
		mCurrentItem = currentItem;
		
		if(mWheelView != null) {
			setCurrentItem();
		}
	}

	public void setOnCompletedListener(OnCompletedListener listener) {
		mOnCompletedListener = listener;
	}
	
	private void setCurrentItem() {
		for (int i = 0; i < mItems.length; i++) {   
			if(TextUtils.equals(mItems[i], mCurrentItem)) {
				mWheelView.setCurrentItem(i);
				break;
			}
		} 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_single_choice);

		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		TextView btnCancel = (TextView) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(mBtnCancelOnClickListener);

		TextView btnComplete = (TextView) findViewById(R.id.btn_complete);
		btnComplete.setOnClickListener(mBtnCompleteOnClickListener);

		mWheelView = (WheelView) findViewById(R.id.wheel_view);
		mWheelView.setShadowColor(0xFFFFFFFF, 0x00FFFFFF, 0x00FFFFFF);
		mWheelView.setWheelForeground(R.color.transparent);
		mWheelView.setWheelBackground(R.drawable.img_wheel_bg);
		mWheelView.addChangingListener(mOnWheelChangedListener);
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getContext(), mItems);
		adapter.setTextColor(getContext().getResources().getColor(R.color.dark_gray));
		mWheelView.setViewAdapter(adapter);
		setCurrentItem();
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
				int position = mWheelView.getCurrentItem();
				mOnCompletedListener.onCompletedFinished(position);
			}

			dismiss();
		}

	};

	private OnWheelChangedListener mOnWheelChangedListener = new OnWheelChangedListener() {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {

		}

	};

	public interface OnCompletedListener {

		public void onCompletedFinished(int position);

	}

}