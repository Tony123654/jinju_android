package com.jinju.android.dialog;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jinju.android.R;
import com.jinju.android.activity.RedPacketActivity;
import com.jinju.android.util.ViewUtils;

/**
 * Created by awe on 2018/3/9.
 */

public class NewerGuideDialog {

    private CustomDialog customDialog;
    private ImageView imgBtnLook;
    private ImageView imgClose;

    float x;//dialog占屏幕宽度的比例
    float y;//dialog占屏幕高度的比例
    int marginTop;//金币距离顶部的边距
    int height, width;//屏幕宽高
    private Activity mActivity;

    public NewerGuideDialog(Activity activity) {
        mActivity = activity;
        init(mActivity);
    }

    private void init(final Activity mActivity) {
        //新手红包
        WindowManager wm = mActivity.getWindow().getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        x = 0.8f;//暂定宽是屏幕的0.8
        y = width * x * 700 / 490 / height;//724和546是那个大图的尺寸，dialog宽高比例搞根据这个图的尺寸来设置

        CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
        customDialog = builder.view(R.layout.layout_newer_guide)
                .style(R.style.mydialog_style)  //设置是全屏还是局部
                .size(y, x)
                .location(Gravity.CENTER)
                .canTouchout(true)         //点击空白不消失
                .cancelBackPress(true)      //点击Back键不消失
                .animation(true)
                .build();
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.show();

        imgBtnLook = (ImageView) customDialog.findViewById(R.id.img_btn_look);
        setImgLayout();
        imgBtnLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RedPacketActivity.class);
                mActivity.startActivity(intent);
                customDialog.dismiss();
            }
        });

        imgClose = (ImageView) customDialog.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
    }

    private void setImgLayout() {
        marginTop = (int) (height * y * 0.60);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewUtils.dip2px(mActivity,240),  ViewUtils.dip2px(mActivity,100));
        lp.setMargins(0, marginTop, 0, 0);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imgBtnLook.setLayoutParams(lp);
    }
}
