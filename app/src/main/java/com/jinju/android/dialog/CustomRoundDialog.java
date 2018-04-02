package com.jinju.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jinju.android.R;


/**
 * Created by miyun-8767 on 2016/1/19.
 */
public class CustomRoundDialog extends Dialog {
    private TextView mDialogTitle;
    private TextView mDialogContent;
    private Button mBtnPositive;
    private Button mBtnNegative;

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    public CustomRoundDialog(Context context,int btnNum) {
        super(context,R.style.custome_round_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        setCustomView(btnNum);
    }
    private void setCustomView(int btnNum){
        if (btnNum == 1) {
            super.setContentView(R.layout.dialog_custom_round_onebtn);
        } else {
           super.setContentView(R.layout.dialog_custom_round_twobtn);
            mBtnNegative = (Button) findViewById(R.id.btn_negative);
        }
        mDialogTitle = (TextView) findViewById(R.id.dialog_title);
        mDialogContent = (TextView) findViewById(R.id.dialog_content);
        mBtnPositive = (Button) findViewById(R.id.btn_positive);
    }




    public void setMessageTitle(String message) {
        mDialogTitle.setText(message);
    }
    public void setMessageTitle(int textId) {
        mDialogTitle.setText(textId);
    }
    public void setContent(String content) {
        mDialogContent.setText(content);
    }
    public void setContentGravity(int gravityId) {
        mDialogContent.setGravity(gravityId);
    }
    public void setContent(int textId) {
        mDialogContent.setText(textId);
    }
    public void setPositiveButton(int textId, View.OnClickListener listener) {
        mBtnPositive.setText(textId);
        mBtnPositive.setOnClickListener(listener);
    }
    public void setPositiveButton(CharSequence text, View.OnClickListener listener) {
        mBtnPositive.setText(text);
        mBtnPositive.setOnClickListener(listener);
    }
    public void setNegativeButton(int textId, View.OnClickListener listener) {
        if (mBtnNegative != null) {
            mBtnNegative.setText(textId);
            mBtnNegative.setOnClickListener(listener);
        }

    }

    public void setNegativeButton(CharSequence text, View.OnClickListener listener) {
        if (mBtnNegative != null) {
            mBtnNegative.setText(text);
            mBtnNegative.setOnClickListener(listener);
        }
    }
}
