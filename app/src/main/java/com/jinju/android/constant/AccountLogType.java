package com.jinju.android.constant;

import java.util.ArrayList;
import java.util.List;

public final class AccountLogType {

	public static final String ALL = "allTrans";//全部
	public static final String RECHARGE = "recharge";//收入
	public static final String WITHDRAW = "withdraw";//支出
	public static final String FREEZE = "freeze";//冻结和其他

	public static final List<String> VALUES = new ArrayList<String>();

	static {
		VALUES.add(ALL);
		VALUES.add(RECHARGE);
		VALUES.add(WITHDRAW);
		VALUES.add(FREEZE);
	}

}