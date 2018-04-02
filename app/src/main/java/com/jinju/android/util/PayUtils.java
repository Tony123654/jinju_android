package com.jinju.android.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.jinju.android.R;
import com.jinju.android.activity.WithdrawPasswordFindActivity;
import com.jinju.android.api.Response;
import com.jinju.android.dialog.CustomRoundDialog;

public final class PayUtils {
	public static void dealPayError(Context context, Response response, OnDealPayErrorFinishedListener listener) {
		if(TextUtils.equals(response.getCode(), "transPwdErr")) {
			createTransPwdErrDialog(context, response, listener);
		} else if(TextUtils.equals(response.getCode(), "transPwdLocked")) {
			createTransPwdLockedDialog(context, response);
		} else {
			createPayErrorDialog(context, response);
		}
	}
	
	public static void createTransPwdErrDialog(final Context context, Response response, final OnDealPayErrorFinishedListener listener) {
		final CustomRoundDialog customRoundDialog = new CustomRoundDialog(context,2);
		customRoundDialog.setContent(response.getMessage());
		customRoundDialog.setPositiveButton(R.string.try_again, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnDealPayErrorFinished("tryAgain");
                customRoundDialog.cancel();
            }
        });
		customRoundDialog.setNegativeButton(R.string.find_password, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, WithdrawPasswordFindActivity.class);
				context.startActivity(intent);
				customRoundDialog.cancel();
			}
		});
		customRoundDialog.show();
	}
	
	public static void createTransPwdLockedDialog(final Context context, Response response) {
		final CustomRoundDialog customRoundDialog = new CustomRoundDialog(context,1);
		customRoundDialog.setMessageTitle(response.getMessage());
		customRoundDialog.setPositiveButton(R.string.find_password, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(context, WithdrawPasswordFindActivity.class);
				context.startActivity(intent);
                customRoundDialog.cancel();
            }
        });
		customRoundDialog.show();
	}
	
	public static void createPayErrorDialog(final Context context, Response response) {
		final CustomRoundDialog customRoundDialog = new CustomRoundDialog(context,1);
		customRoundDialog.setMessageTitle(response.getMessage());
		customRoundDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    			customRoundDialog.cancel();
            }
        });
		customRoundDialog.show();
	}
	
	public interface OnDealPayErrorFinishedListener {
		
		public void OnDealPayErrorFinished(String dealCode);
	}
}