package com.jinju.android.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.jinju.android.R;
import com.jinju.android.activity.lock.UnlockGestureActivity;
import com.jinju.android.api.Advert;
import com.jinju.android.api.Index;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.help.UIHelper;
import com.jinju.android.manager.CommonManager;
import com.jinju.android.manager.CommonManager.OnGetIndexFinishedListener;
import com.jinju.android.manager.CommonManager.OnLoadImageFinishedListener;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.util.DDJRCmdUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.VersionUtils;
import com.jinju.android.util.ViewUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;

public class SplashActivity extends BaseActivity {
	private static final String TAG = DdApplication.class.getSimpleName();
	private static final int REQ_SPLASH_UNLOCK = 1;

	private InitAppTask mInitAppTask;
	private ImageView mImgLoad;

	private Index mIndex;
	private Handler mHandler;
	private ConfigManager mConfigManager;
	private String oldVersion;//是否第一次安装


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mConfigManager = DdApplication.getConfigManager();
		oldVersion = mConfigManager.getFirstInstall();
		boolean mLockStatus = mConfigManager.getLockStatus();
		String token = mConfigManager.getToken();
		if (TextUtils.isEmpty(token)) {
			//非登录状态下
			initView();
		} else {
			//登录状态下
			if (mLockStatus) {
				//手势解锁
				String mLockPassword = mConfigManager.getLockPassword();
				if (!TextUtils.isEmpty(mLockPassword)) {
					Intent intent = new Intent(this, UnlockGestureActivity.class);
					startActivityForResult(intent,REQ_SPLASH_UNLOCK);
				}
			} else {
				initView();
			}
		}
	}

	private void initView() {
		mHandler = new Handler();
		mInitAppTask = new InitAppTask();
		mHandler.postDelayed(mInitAppTask, AppConstant.SPLASH_DELAY_TIME);
		mImgLoad = (ImageView) findViewById(R.id.img_load);

		loadImage();
		getRefreshInfo();
		getIndex();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQ_SPLASH_UNLOCK:
				if (resultCode == RESULT_OK) {
					initView();
				}
				break;
		}
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
		if (mInitAppTask!=null&&mHandler!=null) {
			mHandler.removeCallbacks(mInitAppTask);
		}
		super.onDestroy();
	}
	
	private void loadImage() {
		DdApplication.getCommonManager().loadImage(mOnLoadImageFinishedListener);
	}
	
	private OnLoadImageFinishedListener mOnLoadImageFinishedListener = new OnLoadImageFinishedListener() {

		@Override
		public void onLoadImageFinished(Response response, Advert advert) {
			// TODO Auto-generated method stub
			if (response.isSuccess()) { 
				String imageUrl = advert.getImageUrl();
				final String linkUrl = advert.getLinkUrl();
				ImageUtils.displayImage(mImgLoad, imageUrl);

				final String type = advert.getType();

					//广告图片加载成功
					mImgLoad.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							//跳转链接
							if (type.equals("-1")) {
								mHandler.removeCallbacks(mInitAppTask);
								if (!linkUrl.isEmpty()) {
									if (linkUrl.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
										//点击webview上的控件，执行指定跳转
										DDJRCmdUtils.handleProtocol(SplashActivity.this, linkUrl);
									}else {
										Intent intent = new Intent(SplashActivity.this,BaseJsBridgeWebViewActivity.class);
										intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,linkUrl);
										Bundle bundle = new Bundle();
										if (mIndex!=null) {
											bundle.putSerializable("index", mIndex);
										}
										bundle.putBoolean("advert",true);
										intent.putExtras(bundle);
										startActivity(intent);
										finish();
									}

								}
							}
							//跳转到理财列表
							if (type.equals("2")) {
								mHandler.removeCallbacks(mInitAppTask);
								UIHelper.showLoanActivity(SplashActivity.this);
							}
						}
					});

			}
		}
		
	};
	
	private void getIndex() {
		//对应传的参数 mdpi  1 ，hdpi  2，xhdpi  3，xxhdpi  4，xxxhdpi 5
		//当手机屏幕dpi<=2时，传的参数都是2,即hdpi图片
		int type = ViewUtils.getScreenSize(this);
		if (type < 2 | type == 2) {
			type = 2;
		}
		if (type == 4 || type >4)  {
			type = 4;
		}
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("iv",  VersionUtils.getVersionName());
		datas.put("imgType", type);

		DdApplication.getCommonManager().getIndex(datas, mOnGetIndexFinishedListener);
	}
	
	private OnGetIndexFinishedListener mOnGetIndexFinishedListener = new OnGetIndexFinishedListener() {

		@Override
		public void onGetIndexFinished(Response response, Index index) {
			// TODO Auto-generated method stub
			if (response.isSuccess()) { 
				mIndex = index;
				String mSecurityMessage = index.getSecurityMessage();
				mConfigManager.setSecurityMessage(mSecurityMessage);
			}
		}
		
	};

	private void getRefreshInfo() {
		DdApplication.getCommonManager().getRefreshInfo(mOnRefreshInfoFinishedListener);
	}
	private CommonManager.OnRefreshInfoFinishedListener mOnRefreshInfoFinishedListener = new CommonManager.OnRefreshInfoFinishedListener() {
		@Override
		public void onRefreshInfoFinished(Response response, List<String> refreshInfoList,int bottomType) {
			if (response.isSuccess()) {
				mConfigManager.setLoadInfoList(refreshInfoList);
				mConfigManager.setBottomTab(bottomType);
			}
		}
	};
	private void startNextActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		if(null != mIndex) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("index", mIndex);
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	private class InitAppTask implements Runnable {

		@Override
		public void run() {
			String newVersion = DdApplication.getVersionName();
			if (!oldVersion.equals(newVersion)) {
				Intent mIntent = new Intent(SplashActivity.this,NavigationActivity.class);
				if(null != mIndex) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("index", mIndex);
					mIntent.putExtras(bundle);
				}
				startActivity(mIntent);
				finish();
			} else {
				startNextActivity();
				finish();
			}

		}
	}
	

}