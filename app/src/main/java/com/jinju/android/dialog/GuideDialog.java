package com.jinju.android.dialog;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jinju.android.R;
import com.jinju.android.util.ViewUtils;

/**
 * Created by Libra on 2018/1/12.
 */

public class GuideDialog {

    //红包窗口相关
    private CustomDialog customDialog;
    private AnimationDrawable guideRedPacketCoin;
    private ImageView icon;

    float x;//dialog占屏幕宽度的比例
    float y;//dialog占屏幕高度的比例
    int marginTop;//金币距离顶部的边距
    int height, width;//屏幕宽高
    private Activity mActivity;

    private Handler handler = new Handler();
    public GuideDialog(Activity activity) {

        mActivity = activity;
        init(mActivity);
    }

    private void init(final Activity mActivity) {
        //新手红包
        WindowManager wm = mActivity.getWindow().getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        x = 0.8f;//暂定宽是屏幕的0.8
        y = width * x * 724 / 546 / height;//724和546是那个大图的尺寸，dialog宽高比例搞根据这个图的尺寸来设置

        CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
        customDialog = builder.view(R.layout.layout_redpacket)
                .style(R.style.mydialog_style)  //设置是全屏还是局部
                .size(y, x)
                .location(Gravity.CENTER)
                .canTouchout(false)         //点击空白不消失
                .cancelBackPress(true)      //点击Back键不消失
                .animation(true)
                .build();
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.show();
        icon = (ImageView) customDialog.findViewById(R.id.img_red_packet_open);
        setIconLayout();
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = 0;   //duration是记录动画播放的总时间
                icon.setImageResource(R.drawable.anim_guide_redpacket);
                guideRedPacketCoin = (AnimationDrawable) icon.getDrawable();

                icon.post(new Runnable() {      //在异步线程中执行启动方法
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams para = icon.getLayoutParams();
                        para.height = ViewUtils.dip2px(mActivity,85);
                        para.width = ViewUtils.dip2px(mActivity,85);
                        icon.setLayoutParams(para);
                        guideRedPacketCoin.start();     //启动动画
                    }
                });
                duration = 1300;

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        customDialog.dismiss();
                        new NewerGuideDialog(mActivity);   //新手红包
                    }
                }, duration);
            }
        });

    }

    private void setIconLayout() {
        marginTop = (int) (height * y * 0.62);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewUtils.dip2px(mActivity,100),  ViewUtils.dip2px(mActivity,100));
        lp.setMargins(0, marginTop, 0, 0);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        icon.setLayoutParams(lp);
    }

}
