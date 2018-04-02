package com.jinju.android.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.constant.NetworkType;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class AppUtils {
	public static Toast toast;

	/**
	 * @param context
	 * @return 加载dialog
     */
	public static Dialog createLoadingDialog(Context context) {
		String message = context.getResources().getString(R.string.loading);
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(false);
		return progressDialog;
	}

	public static void showAlertDialog(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);
		builder.setNeutralButton(android.R.string.ok, null);
		builder.create().show();
	}

	public static void showAlertDialog(Context context, int resource) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_name);
		builder.setMessage(resource);
		builder.setNeutralButton(android.R.string.ok, null);
		builder.create().show();
	}


	public static void showToast(Context context, String message){
		if (toast == null){
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		}else {
			toast.setText(message);
		}
		toast.setGravity(Gravity.CENTER,0,0);
		toast.show();
	}
	
	public static void showToast(Context context, int resource){
		if (toast == null){
			toast = Toast.makeText(context, resource, Toast.LENGTH_SHORT);
		}else {
			toast.setText(resource);
		}
		toast.setGravity(Gravity.CENTER,0,0);
		toast.show();
	}

	public static Response obtainErrorResponse(int resource) {
		Response response = new Response();
		response.setMessage(DdApplication.getInstance().getResources().getString(resource));
		response.setResult("error");
		return response;
	}

	public static void handleErrorResponse(Context context, Response response) {

		if(TextUtils.equals(response.getCode(), "forbidVisit")) {
			PayUtils.createPayErrorDialog(context, response);
		} else {
			showToast(context, response.getMessage());
		}

	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int getNetworkState() {
		DdApplication instance = DdApplication.getInstance();
		ConnectivityManager connectivityManager = (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null && networkInfo.isConnected())
			return NetworkType.MOBILE;
		networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected())
			return NetworkType.WIFI;
		return NetworkType.NONE;
	}

	public static File getExternalStoragePath() throws IOException {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			throw new IOException("sd card is not exist!");
		File storageDirectory = Environment.getExternalStorageDirectory();
		File storagePath = new File(storageDirectory, AppConstant.EXT_STORAGE_DIR);
		if (!storagePath.exists() && !storagePath.mkdirs())
			throw new IOException(String.format("%s cannot be created!", storagePath.toString()));
		if (!storagePath.isDirectory())
			throw new IOException(String.format("%s is not a directory!", storagePath.toString()));
		return storagePath;
	}

	public static String getApplicationVersion() {
		DdApplication instance = DdApplication.getInstance();
		PackageManager packageManager = instance.getPackageManager();
		String packageName = instance.getPackageName();
		String versionName = "";
		try {
			versionName = packageManager.getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public static void saveImageToGallery(Context context, Bitmap bitmap) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "juran");
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
			String fileName = dateFormat.format(new Date()) + ".jpg";
			File imageFile = new File(path, fileName);
			String filePath = imageFile.getAbsolutePath();
			ImageUtils.saveBitmap(bitmap, filePath, 100);
			updateGallery(context, filePath);
		}
	}

	public static void updateGallery(Context context, String filePath) {
		MediaScannerConnection.scanFile(context, new String[] { filePath }, null, new OnScanCompletedListener() {
			@Override
			public void onScanCompleted(String path, Uri uri) {

			}
		});
	}

	public static boolean isAppOnForeground() {
		DdApplication instance = DdApplication.getInstance();
		ActivityManager activityManager = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
		return !TextUtils.isEmpty(packageName) && packageName.equals(instance.getPackageName());
	}

	public static void checkNeedHideSoftInput(Activity activity, int x, int y, List<View> viewList) {
		boolean needHide = true;

		Rect rect = new Rect();
		for (View view : viewList) {
			if (!view.isShown())
				continue;

			view.getGlobalVisibleRect(rect);
			needHide = !rect.contains(x, y) && needHide;
		}

		if (needHide) {
			for (View view : viewList)
				view.clearFocus();

			hideSoftInput(activity);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		 
		return wm.getDefaultDisplay().getWidth();   
	}
	
	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		 
		return wm.getDefaultDisplay().getHeight();   
	}
	
	public static int getViewWidth(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
	}
	
	public static int getViewHeight(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
	}
	
	public static int getActivityWidth(Activity activity){
		Rect outRect = new Rect();
      
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect); 
		return outRect.width();
	}
	
	public static int getActivityHeight(Activity activity){
		Rect outRect = new Rect();
      
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect); 
		return outRect.height();
	}

	public static void showSoftInput(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
	}

	public static void hideSoftInput(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getWindow().peekDecorView().getWindowToken(), 0);
	}
	
	public static String getAppDc(Context context) {
		if (context == null) {
			return null;  
	    }  
		
	    String resultData = null;  
	    try {  
	        PackageManager packageManager = context.getPackageManager();  
	        if (packageManager != null) {  
	            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);  
	            if (applicationInfo != null) {  
	                if (applicationInfo.metaData != null) {  
	                    resultData = applicationInfo.metaData.getString("UMENG_CHANNEL");  
	                }  
	            }  
	  
	        }  
	    } catch (PackageManager.NameNotFoundException e) {  
	        e.printStackTrace();  
	    }  
	  
	    return resultData;  
	}
	
	public static String getAppAv() {
		return getApplicationVersion();
	}
	
	public static String getAppDt() {
		return AppConstant.OS_TYPE;
	}
	
	public static String getAppDi(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		return TelephonyMgr.getDeviceId(); 
	}
	
	public static String getAppFv() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String initString(String s) {
		if (s == null) {
			s = "";
		}
		return s;
	}
	public static String[] initSplit(String str){
		if (str == null) {
			return null;
		}
		String[] strings = str.split("\\|");
		return strings;
	}
}