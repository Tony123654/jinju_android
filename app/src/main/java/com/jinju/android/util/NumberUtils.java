package com.jinju.android.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public final class NumberUtils {
	
	public static String formatPhoneNum(String phoneNum){
		String result;
		if(TextUtils.isEmpty(phoneNum)){
			return null;
		}else{
			if(phoneNum.length() <= 3){
				result = phoneNum;
			}else if(phoneNum.length() > 3 && phoneNum.length() <= 7){
				result = phoneNum.substring(0,3)+"  "+phoneNum.substring(3);
			}else if(phoneNum.length() > 7 && phoneNum.length() <= 11){
				result = phoneNum.substring(0,3)+"  "+phoneNum.substring(3,7) + "  " + phoneNum.substring(7);
			}else{
				result = phoneNum;
			}
			return result;
		}
	}
	
	public static String formatCard(String card){
		String result;
		if(TextUtils.isEmpty(card)){
			return null;
		}else{
			if(card.length() <= 6){
				result = card;
			}else if(card.length() >  6 && card.length() <= 14){
				result = card.substring(0,6)+"  "+card.substring(6);
			}else if(card.length() > 14 && card.length() <= 18){
				result = card.substring(0,6)+"  "+ card.substring(6,14) + "  " + card.substring(14);
			}else {
				result = card;
			}
			return result;
		}
	}
	
	public static String formatBankCardNo(String bankCardNo){
		String result;
		if(TextUtils.isEmpty(bankCardNo)) {
			return null;
		} else {
			if(bankCardNo.length() >= 15 && bankCardNo.length() <= 16) {
				result = bankCardNo.substring(0, 4) + "  " + 
						bankCardNo.substring(4, 8) + "  " + 
						bankCardNo.substring(8, 12) + "  " +
						bankCardNo.substring(12);
			} else if(bankCardNo.length() > 16 && bankCardNo.length() <= 20) {
				result = bankCardNo.substring(0, 4) + "  " + 
						bankCardNo.substring(4, 8) + "  " + 
						bankCardNo.substring(8, 12) + "  " +
						bankCardNo.substring(12, 16) + "  " +
						bankCardNo.substring(16);
			} else if(bankCardNo.length() > 20 && bankCardNo.length() <= 25) {
				result = bankCardNo.substring(0, 4) + "  " + 
						bankCardNo.substring(4, 8) + "  " + 
						bankCardNo.substring(8, 12) + "  " +
						bankCardNo.substring(12, 16) + "  " +
						bankCardNo.substring(16, 20) + "  " +
						bankCardNo.substring(20);
			} else {
				result = bankCardNo;
			}
			return result;
		}
	}
	
	public static boolean PhoneNumValid(String PhoneNum){
		Pattern mobilePattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");

		Matcher matcher = mobilePattern.matcher(PhoneNum);
		return matcher.matches();
	}
	/**
	 * 有小数保留两位小数点，无小数返回整数
	 *
	 * @param number
	 * @return string
	 */
	public static String floatTwoStr(double number) {
		return new DecimalFormat("#.00").format(number);
	}
}