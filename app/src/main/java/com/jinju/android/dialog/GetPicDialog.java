package com.jinju.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.jinju.android.R;
import com.jinju.android.util.ImageSelectUtils;

/**
 * Created by Libra on 2017/5/3.
 * 获取图片dialog
 */

public class GetPicDialog {

    private static Dialog dialog;
    private static Window window;
   public static void initDialog(final Activity activity) {

       View view = LayoutInflater.from(activity).inflate(
               R.layout.layout_picture_dialog, null);
       dialog =  new Dialog(activity,R.style.custome_round_dialog);
       dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.WRAP_CONTENT));

        dialog.show();
        window =dialog.getWindow();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = display.getHeight();
       // 以下这两句是为了保证按钮可以水平满屏
       params.width = ViewGroup.LayoutParams.MATCH_PARENT;
       params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
       dialog.onWindowAttributesChanged(params);

       Button photograph = (Button) view.findViewById(R.id.bt_photograph);
       Button select = (Button) view.findViewById(R.id.bt_select);
       Button cancel = (Button) view.findViewById(R.id.bt_cancel);
       photograph.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               ImageSelectUtils.doTakePhoto(activity);
               dialog.dismiss();
           }
       });
       select.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ImageSelectUtils.getPickPhoto(activity);
               dialog.dismiss();
           }
       });
       cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
    }

}
