package com.jinju.android.util;

import android.app.Activity;
import android.content.Intent;

import com.jinju.android.activity.FinancialConfirmActivity;
import com.jinju.android.activity.MainActivity;
import com.jinju.android.activity.VerifyBankCardActivity;
import com.jinju.android.activity.VerifyCodeActivity;
import com.jinju.android.activity.WithdrawPasswordSetActivity;
import com.jinju.android.api.Response;
import com.jinju.android.constant.MemberStep;
import com.jinju.android.constant.SrcType;

public final class LoginUtils {
	
	public static void loginSuccess(Activity activity, Response response, int srcType, long srcId, int memberStep) {
		Intent intent;
		switch (srcType) {
			case SrcType.SRC_REGISTER:
				intent = new Intent(activity, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("activityType", MainActivity.TAB_PROFILE);
				activity.startActivity(intent);
				activity.finish();
				break;
			case SrcType.SRC_NORMAL:
				AppUtils.showToast(activity, response.getMessage());
				activity.setResult(Activity.RESULT_OK);
				activity.finish();
				break;
			case SrcType.SRC_MAIN:
				intent = new Intent(activity, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("index", MainActivity.TAB_PROFILE);
				activity.startActivity(intent);
				activity.setResult(Activity.RESULT_OK);
				activity.finish();
				break;
			case SrcType.SRC_FINANCIAL: {
				switch (memberStep) {
					case MemberStep.COMPLETE:
						intent = new Intent(activity, FinancialConfirmActivity.class);
						intent.putExtra("srcId", srcId);
						activity.startActivity(intent);
						activity.setResult(Activity.RESULT_OK);
						activity.finish();
						break;
					case MemberStep.VERIFY_BANK_CARD:
						intent = new Intent(activity, VerifyBankCardActivity.class);
						intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
						intent.putExtra("srcId", srcId);
						activity.startActivity(intent);
						activity.setResult(Activity.RESULT_OK);
						activity.finish();
						break;
					case MemberStep.SEND_CODE:
						intent = new Intent(activity, VerifyCodeActivity.class);
						intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
						intent.putExtra("srcId", srcId);
						activity.startActivity(intent);
						activity.setResult(Activity.RESULT_OK);
						activity.finish();
						break;
					case MemberStep.SET_TRANS_PWD:
						intent = new Intent(activity, WithdrawPasswordSetActivity.class);
						intent.putExtra(SrcType.SRC_TYPE, SrcType.SRC_FINANCIAL);
						intent.putExtra("srcId", srcId);
						activity.startActivity(intent);
						activity.setResult(Activity.RESULT_OK);
						activity.finish();
						break;
					default:
						break;
				}
			}
			break;
			default:
				AppUtils.showToast(activity, response.getMessage());
				activity.setResult(Activity.RESULT_OK);
				activity.finish();
				break;
		}
	}
}