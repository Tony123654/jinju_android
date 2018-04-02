package com.jinju.android.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinju.android.R;

import java.util.List;

/**
 * Created by awe on 2018/2/6.
 */

public class WarmPromptDialog {

    private CustomDialog customDialog;
    private ImageView imgFinish;
    private List<String> mWarmPromptList;
    private LinearLayout mLayoutScroll;

    float x;
    float y;
    int width;
    int height;

    public WarmPromptDialog(List<String> warmPromptList) {
        mWarmPromptList = warmPromptList;
    }

    public void showDialog(Activity mActivity) {

        WindowManager wm = mActivity.getWindow().getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        x = 0.8f;//暂定宽是屏幕的0.8
        y = width * x * 400 / 300 / height;

        CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
        customDialog = builder.view(R.layout.layout_warm_prompt)
                .style(R.style.mydialog_style)  //设置是全屏还是局部
                .location(Gravity.CENTER)
                .size(y, x)
                .canTouchout(true)         //点击空白不消失
                .build();
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.show();

        mLayoutScroll = (LinearLayout) customDialog.findViewById(R.id.ll_warm_prompt_scroll);
        if (mLayoutScroll.getChildCount() > 0){
            mLayoutScroll.removeAllViews();
        }

        //遍历集合填充content
        if (mWarmPromptList!=null & mWarmPromptList.size()!=0){

            for (int i = 0; i < mWarmPromptList.size(); i++) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(10,10,10,0);

                TextView textView = new TextView(mActivity);
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(14);
                textView.setTextColor(mActivity.getResources().getColor(R.color.charge_content_time_txt));
                textView.setText(mWarmPromptList.get(i).toString());

                mLayoutScroll.addView(textView);
            }
        }

        //关闭Dialog
        imgFinish = (ImageView) customDialog.findViewById(R.id.img_warm_prompt_finish);
        imgFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });

    }
    public void dismiss() {
        customDialog.dismiss();
    }
}
