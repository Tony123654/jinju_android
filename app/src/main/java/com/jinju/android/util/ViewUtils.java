package com.jinju.android.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/5/11.
 */

public class ViewUtils {
    private static List<ImageView> lists = new ArrayList<ImageView>();
    private static List<ViewGroup.LayoutParams> params = new ArrayList<ViewGroup.LayoutParams>();
    /**
     *  drawable-ldpi  120DPI
        drawable-mdpi        160DPI
        drawable-hdpi         240DPI
        drawable-xhdpi       320DPI
        drawalbe-xxhdpi     480DPI
        drawable-xxxhdpi    640DPI
     *
     * 获取手机的分辨率
     */
    public static int getScreenSize(Activity activity){
        //mdpi  1 ，hdpi  2，xhdpi  3，xxhdpi  4，xxxhdpi 5
        DisplayMetrics dm = new DisplayMetrics();
        dm = activity.getApplicationContext().getResources().getDisplayMetrics();
        float dpi = dm.densityDpi;
        int type = 5;//默认是第最大的分辨率的dpi
        float[] floats = {120,160,240,320,480,640};
        for (int i = 1; i < floats.length; i++) {
            if (dpi < floats[i]) {
                float average = (floats[i-1]+floats[i] )/2;
                if (dpi < average) {
                    return i-1;
                } else {
                    return i;
                }
            }
        }
        return type;
    }

    /**
     * 首页图标 根据手机分辨率 设置自适应大小
     * @param activity
     * @param ImageView1
     * @param ImageView2
     */
    public static void setImageViewSize(Activity activity,ImageView ImageView1,ImageView ImageView2) {

        int type = getScreenSize(activity);
        lists.clear();
        params.clear();

        lists.add(ImageView1);
        lists.add(ImageView2);

        ViewGroup.LayoutParams para1 = ImageView1.getLayoutParams();
        ViewGroup.LayoutParams para2 = ImageView2.getLayoutParams();

        params.add(para1);
        params.add(para2);

        if (type <=2){
            for(int i= 0;i < 2;i++) {
                params.get(i).height = dip2px(activity,30);
                params.get(i).width = dip2px(activity,30);
            }
            for(int i= 0;i < 2;i++) {
                lists.get(i).setLayoutParams(params.get(i));
            }
        }
        if (type == 3) {
            for(int i= 0;i < 2;i++) {
                params.get(i).height = dip2px(activity,40);
                params.get(i).width = dip2px(activity,40);
            }
            for(int i= 0;i < 2;i++) {
                lists.get(i).setLayoutParams(params.get(i));
            }
        }
        if (type == 4) {
            for(int i= 0;i < 2;i++) {
                params.get(i).height = dip2px(activity,50);
                params.get(i).width = dip2px(activity,50);
            }
            for(int i= 0;i < 2;i++) {
                lists.get(i).setLayoutParams(params.get(i));
            }
        }
        if (type == 5) {
            for(int i= 0;i < 2;i++) {
                params.get(i).height = dip2px(activity,60);
                params.get(i).width = dip2px(activity,60);
            }
            for(int i= 0;i < 2;i++) {
                lists.get(i).setLayoutParams(params.get(i));
            }
        }

    }
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
