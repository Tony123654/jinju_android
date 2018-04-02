package com.jinju.android.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Libra on 2017/9/21.
 *
 * 反馈
 */

public class OpinionFeedbackActivity extends BaseActivity {

    private EditText mEditFeedback;
    private TextView mTxtInputLimit;
    private Dialog mProgressDialog;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_feedback);
        mContext = this;
        initView();
    }

    private void initView() {
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.call_center_feedback);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditFeedback = (EditText) findViewById(R.id.edit_feedback);
        mEditFeedback.addTextChangedListener(mEditCardWatcher);

        mTxtInputLimit = (TextView) findViewById(R.id.txt_input_limit);

        Button mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(mBtnSubmitOnClickListener);


        mProgressDialog = AppUtils.createLoadingDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }

    private View.OnClickListener mBtnSubmitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mEditFeedback.getText().length()>0) {
                submit();
            } else {
                AppUtils.showToast(OpinionFeedbackActivity.this,"请输入反馈内容后再提交");
            }
        }
    };
    private TextWatcher mEditCardWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int editLength = mEditFeedback.getText().length();
            mTxtInputLimit.setText(getString(R.string.opinion_feedback_input_limit,300-editLength));
        }
    };
    private void submit() {
        Map<String, Object> datas = new HashMap<String, Object>();
        String mFeedback =  mEditFeedback.getText().toString().trim();

        datas.put("msgDesc", mFeedback);

        mProgressDialog.show();
        DdApplication.getUserManager().submitFeedback(datas,mOnSubmitFeedbackFinishedListener);
    }
    private UserManager.OnSubmitFeedbackFinishedListener mOnSubmitFeedbackFinishedListener = new UserManager.OnSubmitFeedbackFinishedListener(){
        @Override
        public void OnSubmitFeedbackFinished(Response response) {

            mProgressDialog.dismiss();
            if (response.isSuccess()) {

                AppUtils.showToast(mContext,"您的意见反馈已成功提交");
                finish();
            }else {
                AppUtils.handleErrorResponse(mContext, response);
            }

        }
    };
}
