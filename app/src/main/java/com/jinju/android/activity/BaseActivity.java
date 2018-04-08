package com.jinju.android.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.jinju.android.base.DdApplication;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class BaseActivity extends Activity implements EasyPermissions.PermissionCallbacks{
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
	//设置android app 的字体大小不受系统字体大小改变的影响
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());
		return res;
	}

	public BaseActivity() {
		super();
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {

	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
}