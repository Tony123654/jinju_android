package com.jinju.android.builder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jinju.android.api.FinancialDetail;

public class FinancialDetailBuilder {

	public static FinancialDetail build(JSONObject jsonObject) throws JSONException {
		FinancialDetail financialDetail = new FinancialDetail();
		financialDetail.setId(jsonObject.optLong("id"));
		financialDetail.setName(jsonObject.optString("name"));
		financialDetail.setProductStatus(jsonObject.optInt("productStatus"));
		financialDetail.setProductAmount(jsonObject.optLong("productAmount"));
		financialDetail.setYearInterest(jsonObject.optString("yearInterest"));
		financialDetail.setActualInterestRate(jsonObject.optString("actualInterestRate"));
		financialDetail.setSubsidyInterestRate(jsonObject.optString("subsidyInterestRate"));
		financialDetail.setProductPerPayInterest(jsonObject.optLong("productPerPayInterest"));
		financialDetail.setLoanPeriodDesc(jsonObject.optString("loanPeriodDesc"));
		financialDetail.setHasPercent(jsonObject.optInt("hasPercent"));
		financialDetail.setHasFundsAmount(jsonObject.optLong("hasFundsAmount"));
		financialDetail.setAvaliableBalance(jsonObject.optLong("avaliableBalance"));
		financialDetail.setAsset(jsonObject.optLong("asset"));
		financialDetail.setActualAmount(jsonObject.optLong("actualAmount"));
		financialDetail.setDeposit(jsonObject.optLong("deposit"));
		financialDetail.setLeastBuy(jsonObject.optLong("leastBuy"));
		financialDetail.setMultiple(jsonObject.optLong("multiple"));
		financialDetail.setMostBuy(jsonObject.optLong("mostBuy"));
		financialDetail.setMemberGiftId(jsonObject.optLong("memberGiftId"));
		financialDetail.setMemberGiftList(MemberGiftBuilder.buildList(jsonObject.optJSONArray("memberGiftList")));
		financialDetail.setInterestList(MemberGiftBuilder.buildList((jsonObject.optJSONArray("interestList"))));
		financialDetail.setInterestPayTypeDesc(jsonObject.optString("interestPayTypeDesc"));
		financialDetail.setType(jsonObject.optString("type"));
		financialDetail.setStartDate(jsonObject.optString("startDate"));
		financialDetail.setIsOnlyNewer(jsonObject.optBoolean("isOnlyNewer"));
		financialDetail.setSubscribeBeginTime(jsonObject.optString("subscribeBeginTime"));
		financialDetail.setSubscribeEndTime(jsonObject.optString("subscribeEndTime"));
		financialDetail.setEndDate(jsonObject.optString("endDate"));
		financialDetail.setFundReturnDate(jsonObject.optString("fundReturnDate"));
		financialDetail.setLoanPeriodDays(jsonObject.optInt("loanPeriod"));
		financialDetail.setDayInterestRate(jsonObject.optDouble("interestRate"));
		financialDetail.setSecurityDescUrl(jsonObject.optString("securityDescUrl"));
		financialDetail.setIntroductionUrl(jsonObject.optString("introductionUrl"));
		financialDetail.setContractUrl(jsonObject.optString("contractUrl"));
		financialDetail.setAuditUrl(jsonObject.optString("auditUrl"));
		financialDetail.setSubscribeUrl(jsonObject.optString("subscribeUrl"));
		financialDetail.setFundReturnTypeDesc(jsonObject.optString("fundReturnTypeDesc"));
		financialDetail.setMaxSubscribe(jsonObject.optLong("maxSubscribe"));
		financialDetail.setContractTitle(jsonObject.optString("contractTitle"));
		financialDetail.setShowStatus(jsonObject.optString("showStatus"));
		financialDetail.setMemberStep(jsonObject.optInt("memberStep"));
		financialDetail.setTagList(TagBuilder.buildList(jsonObject.optJSONArray("tagList")));
		financialDetail.setActivityTag(jsonObject.optString("activityTag"));
		financialDetail.setInterestRateUnit(jsonObject.optString("interestRateUnit"));
		financialDetail.setBeginDuration(jsonObject.optLong("beginDuration"));
		financialDetail.setToday(jsonObject.optString("today"));
		financialDetail.setInvestUrl(jsonObject.optString("investUrl"));
		financialDetail.setActivityAwardInfo(ActivityAwardInfoBuilder.build(jsonObject.optJSONObject("activityInfo")));
		financialDetail.setOrderCount(jsonObject.optInt("orderCount"));
		financialDetail.setJoinActivity0011(jsonObject.optBoolean("isJoinActivity0011"));
		return financialDetail;
	}

}