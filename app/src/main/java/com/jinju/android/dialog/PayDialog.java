package com.jinju.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.jinju.android.R;


public class PayDialog extends Dialog  {
	Activity activity;
	private View view;
	private boolean isOutSideTouch=true;

	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public boolean isOutSideTouch() {
		return isOutSideTouch;
	}
	public void setOutSideTouch(boolean isOutSideTouch) {
		this.isOutSideTouch = isOutSideTouch;
	}
	public PayDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	public PayDialog(Context context) {
		this(context,0);
		// TODO Auto-generated constructor stub
	}
	public PayDialog(Activity activity, View view) {
		super(activity, R.style.PayDialog);
		this.activity = activity;
		this.view=view;
	}
	public PayDialog(Activity activity, View view,int theme) {
		super(activity,theme);
		this.activity = activity;
		this.view=view;
	}
	public PayDialog(Activity activity, View view,int theme,boolean isOutSide) {
		super(activity,theme);
		this.activity = activity;
		this.view=view;
		this.isOutSideTouch=isOutSide;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(view);
		setCanceledOnTouchOutside(isOutSideTouch);
		
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;
		int screenHeight=dm.heightPixels;
		WindowManager.LayoutParams layoutParams = this.getWindow()
				.getAttributes();
		layoutParams.width = screenWidth;
		layoutParams.height = screenHeight-60;
		this.getWindow().setAttributes(layoutParams);
	}

}
