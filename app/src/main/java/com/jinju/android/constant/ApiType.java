package com.jinju.android.constant;

public final class ApiType {
	//测试环境
	public static final String LOAD_IMAGE = "/loading.json";
	public static final String GET_INDEX = "/index.json";
	public static final String LOGIN = "/member/login.json";
	public static final String CHECK_MOBILE_AVAILABLE = "/reg/check_mobile_available.json";
	public static final String CHECK_MOBILE_LOGIN_OR_REGISTER = "/member/pre_login.json";
	public static final String REGISTER = "/member/reg.json";
	public static final String LOGOUT = "/member/logout.json";
	public static final String CHECK_UPDATE = "/check_update.json";
	public static final String UPLOAD_AVATAR = "/myspace/member/set_head_img.json";
	public static final String SEND_VERIFY_CODE = "/common/send_verify_code.json";
	public static final String SEND_VERIFY_CODE_LOGINED = "/common/send_verify_code_logined.json";
	public static final String GET_FINANCIAL_LIST = "/financial.json";
	public static final String GET_MORE_FINANCIAL_LIST = "/more/financial.json ";
	public static final String GET_FINANCIAL_DETAIL = "/financial/detail.json";
	public static final String GET_ACCOUNT = "/myspace/account.json";
	public static final String GET_SETTING = "/myspace/set.json";
	public static final String GET_MY_ASSET = "/myspace/common/my_asset.json";
	public static final String AUTOMATE_RULE = "/myspace/financial/automate_rule.json";
	public static final String GET_FUND_ACCOUNT_LOG = "/myspace/fund/account_log_v2.7.json";
	public static final String GET_MY_FINANCIAL_POSITION = "/myspace/financial/my_position.json";
	public static final String GET_FINANCIAL_POSITION_DETAIL = "/myspace/financial/position_detail.json";
	public static final String GET_MY_COUPON_LIST = "/myspace/fund/coupon.json";
	public static final String COUPON_CHARGE = "/myspace/fund/coupon_charge.json";
	public static final String SET_NICKNAME = "/myspace/member/set_nick.json";
	public static final String CHANGE_VALIDATE = "/myspace/member/mobile/change_validate.json";
	public static final String CHANGE_MOBILE = "/myspace/member/mobile/change_mobile.json";
	public static final String CHANGE_PASSWORD = "/myspace/member/change_pwd.json";
	public static final String RESET_PASSWORD = "/member/reset_pwd.json";
	public static final String VALIDATE_OLD_WITHDRAW_PASSWORD = "/myspace/fund/old_trans_pwd_validate.json";
	public static final String EDIT_WITHDRAW_PASSWORD = "/myspace/fund/trans_pwd_edit.json";
	public static final String FORGET_WITHDRAW_PASSWORD = "/myspace/fund/find_trans_pwd.json";
	public static final String RESET_WITHDRAW_PASSWORD = "/myspace/fund/find_trans_pwd_next.json";
	public static final String VALIDATE_FIND_TRANS_PWD = "/myspace/fund/find_trans_pwd_validate.json";
	public static final String GET_CHARGE_RECORD = "/myspace/fund/pay_online_log.json";
	public static final String WITHDRAW = "/myspace/fund/withdraw.json";
	public static final String GET_WITHDRAW_RECORD = "/myspace/fund/withdraw_record.json";
	public static final String INIT_EDIT = "/myspace/member/init_edit.json";
	public static final String SAVE_AUTH = "/myspace/member/save_auth.json";
	public static final String GET_BANK_LIST = "/myspace/member/bank.json";
	public static final String BANK_EDIT = "/myspace/member/bank/edit.json";
	public static final String AUTH_CARD= "/myspace/member/bank/auth.json";
	public static final String SEND_CODE = "/myspace/member/bank/send_code.json";
	public static final String BANK_SAVE = "/myspace/member/bank/save.json";
	public static final String CONFIRM_BANK_CARD = "/myspace/member/bank/confirm.json";
	public static final String GET_PROVINCE = "/ajax/queryProvince.json";
	public static final String GET_CITY = "/ajax/queryCity.json";
	public static final String INIT_FAST_PAY = "/myspace/fund/authpay/init_fast_pay.json";
	public static final String FAST_PAY = "/myspace/fund/fast_pay.json";
	public static final String FINANCIAL_CONFIRM_ORDER = "/financial/confirm_order.json";
	public static final String FINANCIAL_PAY = "/financial/pay.json";
	public static final String GET_INFO = "/myspace/member/info.json";
	public static final String GET_ABOUT = "/myspace/member/about_us_button.json";
	public static final String RECORD_REGID = "/member/regid/checkin.json";//发送regid
	public static final String MY_MESSAGE = "/member/message.json";//我的消息
	public static final String VERIFY_GESTURE_CODE = "/member/gesturecode/verify.json";//发送手势忘记密码短信验证
	public static final String SET_SALE_REMIND= "/financial/startsubscribe/set.json";//设置开售提醒
	public static final String CANCEL_SALE_REMIND= "/financial/startsubscribe/cancel.json";//设置开售提醒
	public static final String SUBMIT_FEEDBACK= "/myspace/msgfeedback/save_msg_Desc.json";//意见反馈
	public static final String HOME_NOTICE_LIST= "/notice/notice_list.json";//首页公告
	public static final String PRODUCT_INTRODUCE = "/financial/proInfo.json";//理财详情页的項目介绍
	public static final String INVEST_RECORD = "/financial/financial_order_list_v2.2.json";//理财详情页的投资记录
	public static final String GET_DISCOVER_LIST = "/findActView/find_list.json";//发现页
	public static final String GET_SHARE_INFO = "/wechat/share.json";//原生获取分享信息

	public static final String GET_CUSTOMER_CENTER = "/myspace/fund/customer_center.json";//获取客服中心信息
	public static final String GET_REFRESH_INFO = "/config.json";//刷新头部信息

}