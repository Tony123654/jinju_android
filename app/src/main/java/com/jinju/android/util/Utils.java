package com.jinju.android.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Utils {

	private static final String DEFAULT_CHARSET = "utf-8";
	// 两次点击按钮之间的点击间隔不能少于1000毫秒
	private static final int MIN_CLICK_DELAY_TIME = 1000;
	private static long lastClickTime;
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static String getTime(long seconds, String format) {
		Date date = new Date(seconds * 1000);
		DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
		return dateFormat.format(date);
	}
	public static String toHourDate(long seconds) {

		long second = seconds/1000;
		long h = second/3600;//小時
		long m = (second - h*3600)/60;//分钟
		long s = (second - h*3600) % 60;//秒
		if (h >= 24) {
			return  h/24+"天"+ h%24+"小时"+m+"分"+s+"秒";
		} else {
			return  h+"小时"+m+"分"+s+"秒";
		}

	}
	public static String toShortDate(String dateText) {
		try {
			DateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date date = longDateFormat.parse(dateText);
			DateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			return shortDateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return dateText;
		}
	}
	public static String stampToDate(String s){

		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
	/*
     * 将时间转换为时间戳
     */
	public static long dateToStamp(String s) {

		try {
			Date date = simpleDateFormat.parse(s);
			long ts = date.getTime();

			return ts;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static String secondsToString(int seconds) {
		StringBuilder buffer = new StringBuilder();
		int minute = seconds / 60;
		if (minute < 10)
			buffer.append("0");
		buffer.append(minute).append(":");
		int second = seconds % 60;
		if (second < 10)
			buffer.append("0");
		buffer.append(second);
		return buffer.toString();
	}

	public static String bytesToString(byte[] data) {
		return bytesToString(data, DEFAULT_CHARSET);
	}

	public static String bytesToString(byte[] data, String charsetName) {
		try {
			return new String(data, charsetName);
		} catch (UnsupportedEncodingException e) {
			return new String(data);
		}
	}

	public static byte[] stringToBytes(String data) {
		return stringToBytes(data, DEFAULT_CHARSET);
	}

	public static byte[] stringToBytes(String data, String charsetName) {
		try {
			return data.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			return data.getBytes();
		}
	}

	public static String bytesToHex(byte[] data) {
		String result = new String();
		String hex = null;
		for (int i = 0; i < data.length; i++) {
			hex = (Integer.toHexString(data[i] & 0xFF));
			if (hex.length() == 1)
				result += "0";
			result += hex.toUpperCase(Locale.CHINA);
		}
		return result;
	}

	public static byte[] hexToBytes(String data) {
		if (data.length() % 2 == 1)
			data = "0" + data;
		int length = data.length() / 2;
		byte[] result = new byte[length];
		String hex = null;
		for (int i = 0; i < length; i++) {
			hex = data.substring(i * 2, i * 2 + 1);
			int high = Integer.parseInt(hex, 16);
			hex = data.substring(i * 2 + 1, i * 2 + 2);
			int low = Integer.parseInt(hex, 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static long getFileSize(File file) {
		if (!file.exists())
			return 0;
		if (file.isDirectory()) {
			long size = 0;
			for (File child : file.listFiles())
				size += getFileSize(child);
			return size;
		}
		return file.length();
	}

	public static void deleteFile(File file) {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			for (File child : file.listFiles())
				deleteFile(child);
		}
		file.delete();
	}

	public static String formatFileSize(long size) {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		if (size == 0)
			return "0B";
		if (size < 1024)
			return decimalFormat.format((double) size) + "B";
		if (size < 1048576)
			return decimalFormat.format((double) size / 1024) + "KB";
		if (size < 1073741824)
			return decimalFormat.format((double) size / 1048576) + "MB";
		return decimalFormat.format((double) size / 1073741824) + "GB";
	}

	public static double stringToDouble(String data) {
		double result = 0;
		try {
			result = Double.parseDouble(data);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static long stringToLong(String data) {
		long result = 0;
		try {
			result = Long.parseLong(data);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成base64编码
	 * 生成前端需要显示的形式
	 * @param filePath
	 * @return
     */
	public static String encodeFile(File filePath) {
		InputStream input = null;
		ByteArrayOutputStream out = null;

		try {
			input = new FileInputStream(filePath);
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024 * 4];
			int len = 0;
			while ((len = input.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			// 应前端要求 修改为标准base64编码
			return android.util.Base64.encodeToString(out.toByteArray(), android.util.Base64.CRLF);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static boolean isFastClick() {
		boolean flag = false;
		long curClickTime = System.currentTimeMillis();
		if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
			flag = true;
		}
		lastClickTime = curClickTime;
		return flag;
	}
}