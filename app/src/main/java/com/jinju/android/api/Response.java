package com.jinju.android.api;

public class Response {

	private String mCode;
	private String mMessage;
	private String mResult;
	private String mBody;
	private String mData;

	public boolean isSuccess() {
		return mResult.equals("success");
	}

	public String getCode() {
		return mCode;
	}

	public void setCode(String code) {
		mCode = code;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		mMessage = message;
	}

	public String getResult() {
		return mResult;
	}

	public void setResult(String result) {
		mResult = result;
	}

	public String getBody() {
		return mBody;
	}

	public void setBody(String body) {
		mBody = body;
	}
	
	public void setData(String data) {
		mData = data;
	}
	
	public String getData(){
		return mData;
	}

}