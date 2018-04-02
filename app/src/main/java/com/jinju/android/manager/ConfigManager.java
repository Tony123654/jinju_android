package com.jinju.android.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AuthStatus;
import com.jinju.android.interfaces.IManager;
import com.jinju.android.util.StringUtils;

import java.util.List;

public class ConfigManager implements IManager {

	private DdSharedPreferences mAppSharedPreferences;

	public ConfigManager() {
		mAppSharedPreferences = new DdSharedPreferences(DdApplication.getInstance(), "jinju");
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onExit() {

	}

	public void setFirstRun(boolean isFirstRun) {
		mAppSharedPreferences.putBoolean("is_first_run", isFirstRun);
	}

	public boolean isFirstRun() {
		return mAppSharedPreferences.getBoolean("is_first_run", true);
	}

	public void setLogined(boolean isLogined) {
		mAppSharedPreferences.putBoolean("is_logined", isLogined);
	}

	public boolean isLogined() {
		return mAppSharedPreferences.getBoolean("is_logined", false);
	}
	
	public void setAuthStatus(String authStatus){
		mAppSharedPreferences.putString("auth_status", authStatus);
	}
	
	public boolean isAuthed(){
		return TextUtils.equals(mAppSharedPreferences.getString("auth_status", AuthStatus.NONE), AuthStatus.AUTH);
	}
	//是否第一次安装
	public void setFirstInstall(String version) {
		mAppSharedPreferences.putString("version",version);
	}
	public String getFirstInstall() {
		return mAppSharedPreferences.getString("version", "");
	}

	public void setToken(String token) {
		mAppSharedPreferences.putString("token", token);
	}
	public String getToken() {
		return mAppSharedPreferences.getString("token", "");
	}

	public void setPhone(String phone) {
		mAppSharedPreferences.putString("phone",phone);
	}
	public String getPhone() {
		return mAppSharedPreferences.getString("phone", "");
	}

	public void setMemberId(String memberId) {
		mAppSharedPreferences.putString("memberId", memberId);
	}
	public String getMemberId() {
		return mAppSharedPreferences.getString("memberId", "");
	}

	public void setNickName(String nickName) {
		mAppSharedPreferences.putString("nickName",nickName);
	}
	public String getNickName() {
		return mAppSharedPreferences.getString("nickName","");
	}
	//是否替换底部icon
	public void setBottomTab(int type) {
		mAppSharedPreferences.putInt("tabType",type);
	}
	public int getBottomTab() {
		return mAppSharedPreferences.getInt("tabType",0);//默认不替换
	}
	//是否是第一次注册
	public void setFirstRegister(boolean firstRegister){
		mAppSharedPreferences.putBoolean("firstRegister",firstRegister);
	}
	public boolean getFirstRegister(){
		return mAppSharedPreferences.getBoolean("firstRegister",false);
	}
	//存取check的版本号
	public void setCheckVersion(String checkVersion) {
		mAppSharedPreferences.putString("checkVersion",checkVersion);
	}
	public String getCheckVersion() {
		return mAppSharedPreferences.getString("checkVersion","");
	}

	//是否有新版本更新
	public void setVersionUpdate(boolean isVersionUpdate){
		mAppSharedPreferences.putBoolean("isVersionUpdate",isVersionUpdate);
	}
	public boolean getVersionUpdate(){
		return mAppSharedPreferences.getBoolean("isVersionUpdate",false);
	}

	//存储对象	versionUpdateList_profile
	public  void setVersionUpdateList(List versionUpdateList) {
		mAppSharedPreferences.putObjectString("versionUpdateList",versionUpdateList);

	}
	public List getVersionUpdateList() {
		return mAppSharedPreferences.getObjectString("versionUpdateList",null);
	}

	//手机IMEI识别码
	public  void setPhoneIMEICode(String imeiCode) {
		mAppSharedPreferences.putString("imeiCode",imeiCode);
	}
	public  String getPhoneIMEICode() {
		return mAppSharedPreferences.getString("imeiCode","");
	}
	public void setSecurityMessage(String message) {
		mAppSharedPreferences.putString("securityMessage",message);
	}
	public String getSecurityMessage() {
		return mAppSharedPreferences.getString("securityMessage","");
	}
	public void setLoadInfoList(List loadInfoList){
		mAppSharedPreferences.putObjectString("loadInfo",loadInfoList);
	}
	public List getLoadInfoList() {
		return mAppSharedPreferences.getObjectString("loadInfo",null);
	}

	//存储对象
	public  void setRemindList(List remindList) {
		mAppSharedPreferences.putObjectString("remind_key",remindList);

	}
	public List getRemindList() {
		return mAppSharedPreferences.getObjectString("remind_key",null);
	}

	//与手势锁匹配的账号
	public void setLockMatchingCode(String code) {
		mAppSharedPreferences.putString("matching_code",code);
	}
	public String getLockMatchingCode(){
		return mAppSharedPreferences.getString("matching_code","");
	}
	//我的界面金额隐藏
	public void setMoneyVisible(boolean isVisible) {
		mAppSharedPreferences.putBoolean("money_visible",isVisible);
	}
	//默认显示金额
	public boolean getMoneyVisible(){
		return mAppSharedPreferences.getBoolean("money_visible",true);
	}
	//手势锁密码
	public void setLockPassword(String password) {
		mAppSharedPreferences.putString("lock_password",password);
	}
	public String getLockPassword() {
		return mAppSharedPreferences.getString("lock_password","");
	}

	//手势锁的开关状态
	public void setLockStatus(Boolean status) {
		mAppSharedPreferences.putBoolean("open_lock",status);

	}
	public  boolean getLockStatus() {
		return mAppSharedPreferences.getBoolean("open_lock",false);
	}
	//计时开始退出后台运行的当前时间
	public void setStartTime(long time) {
		mAppSharedPreferences.putLong("start_time",time);
	}
	public long getStartTime() {
		return mAppSharedPreferences.getLong("start_time",0);
	}
	public class DdSharedPreferences {

		private SharedPreferences mSharedPreferences;

		public DdSharedPreferences(Context context, String name) {
			mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		}

		public boolean getBoolean(String key, boolean defValue) {
			return mSharedPreferences.getBoolean(key, defValue);
		}

		public boolean putBoolean(String key, boolean value) {
			return mSharedPreferences.edit().putBoolean(key, value).commit();
		}

		public int getInt(String key, int defValue) {
			return mSharedPreferences.getInt(key, defValue);
		}

		public boolean putInt(String key, int value) {
			return mSharedPreferences.edit().putInt(key, value).commit();
		}

		public String getString(String key, String defValue) {
			return mSharedPreferences.getString(key, defValue);
		}

		public boolean putString(String key, String value) {
			return mSharedPreferences.edit().putString(key, value).commit();
		}
		
		public long getLong(String key, long defValue) {
			return mSharedPreferences.getLong(key, defValue);
		}
		
		public boolean putLong(String key, long value) {
			return mSharedPreferences.edit().putLong(key, value).commit();
		}
		//存储Object对象，必须implement Serializable
		public List getObjectString(String key,String defValue) {
			String string = mSharedPreferences.getString(key,defValue);
			if (!TextUtils.isEmpty(string)) {
				List list = StringUtils.String2List(string);
				return list;
			}
			return null;
		}
		public boolean putObjectString(String key,List value) {
			String objectString = StringUtils.Object2List(value);
			return mSharedPreferences.edit().putString(key,objectString).commit();
		}


	}


}