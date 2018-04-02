package com.jinju.android.util;

import android.content.Context;
import android.text.TextUtils;

import com.jinju.android.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

public final class DataUtils {

	private static NumberFormat mCurrencyFormat = new DecimalFormat("###,##0.00");
	private static NumberFormat mNumberFormat = new DecimalFormat("###.###");
	private static NumberFormat mNumberDecimalFormat = new DecimalFormat("##0.00");
	private static NumberFormat mThreeDecimalFormat = new DecimalFormat("###,##0.000");
	
	private static double THRESHOLD = 0.00001;

	private static DecimalFormat dfs = null;
	public static DecimalFormat format(String pattern) {
		if (dfs == null) {
			dfs = new DecimalFormat();
		}
		dfs.setRoundingMode(RoundingMode.FLOOR);
		dfs.applyPattern(pattern);
		return dfs;
	}

	public static String convertTwoDecimalNoFormat(double value) {
		if(value != 0.00){
            DecimalFormat df = new DecimalFormat("#######0.00");
			return df.format(value);
        }else{
            return "0.00";
        }
	}
	
	public static String convertTwoDecimal(double value) {
		BigDecimal decimal = new BigDecimal(value);
		return mCurrencyFormat.format(decimal.doubleValue());
	}
	
	public static String convertThreeDecimal(double value) {
		BigDecimal decimal = new BigDecimal(value);
		return mThreeDecimalFormat.format(decimal.doubleValue());
	}
	
	public static String convertMillion(long value) {
		BigDecimal decimal = new BigDecimal(value);
		BigDecimal divisor = new BigDecimal(1000000);
		return mCurrencyFormat.format(decimal.divide(divisor).doubleValue());
	}

	public static String convertCurrencyFormat(Context context, double value) {
		return context.getString(R.string.money_number, convertCurrencyFormat(value));
	}

	public static String convertCurrencyFormat(double value) {
		BigDecimal decimal = new BigDecimal(value);
		BigDecimal divisor = new BigDecimal(100);
		return mCurrencyFormat.format(decimal.divide(divisor).doubleValue());
	}


	//乘法精确运算
	public static double mul(double value) {
		BigDecimal b1 = new BigDecimal(value);
		BigDecimal b2 = new BigDecimal(100);
		return b1.multiply(b2).doubleValue();
	}
	//除法精确运算
	public static double divide(double value) {
		BigDecimal b1 = new BigDecimal(value);
		BigDecimal b2 = new BigDecimal(100);
		return b1.divide(b2).doubleValue();
	}
	public static String convertToYuan(long value){
		String amount = convertCurrencyFormat(value);
		int pointIndex = amount.indexOf(".");
		
		if(pointIndex == -1){
			return amount;
		} else {
			return amount.substring(0, pointIndex);
		}
	}
	public static String convertToYuanOrWanYuan(Context context,long value) {
		int num = (int) (value / 100);
		if (num >= 10000) {
			if (num % 10000 == 0) {
				return context.getString(R.string.million_money_number, num /10000);
			} else {
				float f = num * 1.0f / 10000;
				return context.getString(R.string.million_money_number, new DecimalFormat("#.0").format(f));
			}
		} else {
			return context.getString(R.string.money_number, num);
		}
	}

	public static String convertDecimalNumberFormat(double value) {
		BigDecimal decimal = new BigDecimal(value);
		BigDecimal divisor = new BigDecimal(100);

		return mNumberDecimalFormat.format(decimal.divide(divisor).doubleValue());

	}
	public static String convertNumberFormat(double value) {
		return mNumberFormat.format(value);
	}

	public static String convertNumberScale(String value, int scale) {
		double valueD = Double.parseDouble(value);
		if(valueD < THRESHOLD && valueD > -THRESHOLD){
			return "0";
		}
		
		BigDecimal bd = new BigDecimal(value);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public static int compareDouble(double a, double b) {
		BigDecimal aDec = new BigDecimal(Double.toString(a));
		BigDecimal bDec = new BigDecimal(Double.toString(b));
		
		return aDec.compareTo(bDec);
	}

	public static int getFundingAssetsColor(long profit) {
		return profit == 0 ? R.color.gray : (profit > 0 ? R.color.red : R.color.green);
	}

	public static boolean checkIdCard(String card) {
		if (TextUtils.isEmpty(card.trim()))
			return false;

		Pattern pattern = Pattern.compile("^[0-9]{17}[0-9X]$");
		if (!pattern.matcher(card).matches())
			return false;

		final int[] weights = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		final String[] parityBits = new String[] { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

		int power = 0;
		for (int i = 0; i < 17; i++)
			power += Integer.parseInt(String.valueOf(card.charAt(i))) * weights[i];

		return parityBits[power % 11] == card.substring(17);
	}
	
	
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]*");
	    return pattern.matcher(str).matches();  
	}

}