package com.jinju.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinju.android.R;
import com.jinju.android.widget.LoadingView;

/**
 * Created by Libra on 2017/9/18.
 */

public class LoadingDialog {

    private Context mContext;
    private View view;
    private Dialog dialog;
    private LoadingView loadingView;

    public LoadingDialog(Context context) {
        this.mContext = context;
        init();
    }
    private void init() {
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog, null);
        dialog =  new Dialog(mContext,R.style.custome_round_dialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setOnKeyListener(keylistener);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(false);

        loadingView = (LoadingView) view.findViewById(R.id.loadingView);

    }

    /**
     * 返回键监听
     */
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                return true;
            } else {
                return false;
            }
        }
    } ;
    public void show() {
        loadingView.start();
        dialog.show();
    }
    public void dismiss() {
        loadingView.stop();
        dialog.dismiss();
    }

}
