package com.jinju.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.JsModalInfo;

import java.util.List;

/**
 * Created by Libra on 2017/4/28.
 */

public class ModelDialog {

    private static JsModalInfo mJsModalInfo;
    private static Dialog dialog;

    public static void showModelDialog(final Context context, JsModalInfo jsModalInfo, final DialogListener listener) {


        mJsModalInfo = jsModalInfo;

        int[] icons = {R.drawable.alert_tips_icon, R.drawable.alert_success_icon, R.drawable.alert_fail_icon, 0};
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_tips_dialog_onebtn, null);
        dialog =  new Dialog(context,R.style.custome_round_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        dialog.show();
        Window window =dialog.getWindow();
        WindowManager windowManager = ((Activity)context).getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getHeight() * 0.4);

        dialog.getWindow().setAttributes(params);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_title);
        RelativeLayout iv_cancel = (RelativeLayout) view.findViewById(R.id.iv_cancel);

        TextView content = (TextView) view.findViewById(R.id.tv_content);
        Button bt1 = (Button) view.findViewById(R.id.bt1_confirm);
        Button bt2 = (Button) view.findViewById(R.id.bt2_confirm);
        Button bt3 = (Button) view.findViewById(R.id.bt3_confirm);
        final List<JsModalInfo.Buttons> buttons =  mJsModalInfo.getButtons();

        title.setText(mJsModalInfo.getTitle());
        content.setText(mJsModalInfo.getText());

        if (mJsModalInfo.getForce() == 1) {
            iv_cancel.setVisibility(View.GONE);
        } else {
            iv_cancel.setVisibility(View.VISIBLE);
        }
        if (buttons.size() == 3) {
            bt1.setText(buttons.get(0).getText());
            bt2.setText(buttons.get(1).getText());
            bt3.setText(buttons.get(2).getText());
            bt2.setVisibility(View.VISIBLE);
            bt3.setVisibility(View.VISIBLE);
        }
        if (buttons.size() == 2) {
            bt1.setText(buttons.get(0).getText());
            bt2.setText(buttons.get(1).getText());
            bt2.setVisibility(View.VISIBLE);
            bt3.setVisibility(View.GONE);
        }
        if (buttons.size() == 1) {
            bt1.setText(buttons.get(0).getText());
            bt2.setVisibility(View.GONE);
            bt3.setVisibility(View.GONE);
        }
        if (mJsModalInfo.getType().equals("0")) {
            iv.setBackgroundResource(icons[0]);
        } else if (mJsModalInfo.getType().equals("1")) {
            iv.setBackgroundResource(icons[1]);
        } else if (mJsModalInfo.getType().equals("2")) {
            iv.setBackgroundResource(icons[2]);
        }


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                listener.dialogFinish(buttons.get(0).getValue());
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                listener.dialogFinish(buttons.get(1).getValue());
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.dialogFinish(buttons.get(2).getValue());
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.dialogFinish("");

            }
        });

    }
    public interface DialogListener {
        void dialogFinish(String value);
    }


}
