package com.jinju.android.constant;

import java.util.ArrayList;
import java.util.List;

public final class CouponStatus {

	public static final String CAN_USE = "canUse";
	public static final String HISTORY = "history";

	public static final List<String> VALUES = new ArrayList<String>();

	static {
		VALUES.add(CAN_USE);
		VALUES.add(HISTORY);
	}

}