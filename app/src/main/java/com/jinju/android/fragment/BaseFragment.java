package com.jinju.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {

	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null)
			mRootView = onInitView(inflater, container, savedInstanceState);

		ViewGroup parent = (ViewGroup) mRootView.getParent();

		if (parent != null) {
			parent.removeView(mRootView);
		}

		return mRootView;
	}

	public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	public void dispatchTouchEvent(MotionEvent event) {

	}

	public void dispatchNotification(int notifyName) {

	}

}