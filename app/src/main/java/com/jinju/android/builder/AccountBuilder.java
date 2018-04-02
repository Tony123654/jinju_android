package com.jinju.android.builder;

import com.jinju.android.api.Account;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountBuilder {

	public static Account build(JSONObject jsonObject) throws JSONException {
		Account account = new Account();
		account.setAccount(jsonObject.optString("account"));
		account.setMemberId(jsonObject.optString("memberId"));
		account.setAuthStatus(jsonObject.optString("authStatus"));
		account.setWithCount(jsonObject.optInt("withCount"));
		account.setGiftCount(jsonObject.optInt("giftCount"));
		account.setPositionCount(jsonObject.optInt("positionCount"));
		account.setCurrBalance(jsonObject.optLong("currBalance"));
		account.setNetAssets(jsonObject.optLong("netAssets"));
		account.setPositionAssets(jsonObject.optLong("positionAssets"));
		account.setMemberStep(jsonObject.optInt("memberStep"));
		account.setAboutsuUrl(jsonObject.optString("aboutusUrl"));
		account.setTodayIncome(jsonObject.optLong("todayIncome"));
		account.setTotalIncome(jsonObject.optLong("totalIncome"));
		account.setCanUseBalance(jsonObject.optLong("canUseBalance"));
		account.setNewMessageFlag(jsonObject.optInt("newMessageFlag"));
		account.setInviteLocation(jsonObject.optString("inviteAddr"));
		account.setRedPacketDesc(jsonObject.optJSONObject("couponMessage").optString("desc"));
		account.setIsValid(jsonObject.optJSONObject("couponMessage").optInt("isValid"));
		account.setYesterdayIncome(jsonObject.optLong("yesterdayIncome"));
		account.setInviteRecord(jsonObject.optString("inviteRecord"));

		return account;
	}

}