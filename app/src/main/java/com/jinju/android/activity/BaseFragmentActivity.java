package com.jinju.android.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jinju.android.base.DdApplication;

public class BaseFragmentActivity extends FragmentActivity {
	private DdApplication mApplication = DdApplication.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mApplication.insertActivity(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mApplication.removeActivity(this);
		super.onDestroy();
	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}
}