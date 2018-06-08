package com.jinju.android.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.FinancialDetail;
import com.jinju.android.util.Toaster;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 类名: CalculatorDialog <p>
 * 创建人: mwb <p>
 * 创建时间: 2018/5/31 17:39 <p>
 * 描述:
 * <p>
 * 更新人: <p>
 * 更新时间: <p>
 * 更新描述: <p>
 */
public class CalculatorDialog extends Dialog {

    private Double  minRate;
    private int     reduceDay;
    private boolean isWallet;
    private double  money;
    private Context context;
    public CalculatorDialog(Context context, FinancialDetail mProjectBean) {
        this(context, mProjectBean.getDayInterestRate(),mProjectBean.getLoanPeriodDays());
    }
    /**
     * @param context
     */
    public CalculatorDialog(Context context, Double rate, int reduceDay) {
        super(context, R.style.Dialog_Tip);
        this.context=context;
        this.minRate = rate;
        this.reduceDay = reduceDay;
        init();
    }

    /**
     *
     */
    @SuppressLint("InflateParams")
    private void init() {
        final LinearLayout payDialogLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.dialog_project_cal, null);
        setContentView(payDialogLayout);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        final ImageView close = (ImageView) payDialogLayout.findViewById(R.id.close);// 退出

        TextView tvInterest = (TextView) payDialogLayout.findViewById(R.id.tv_interest);

        TextView tvDurtion = (TextView) payDialogLayout.findViewById(R.id.tv_durtion);
        final EditText etmoney = (EditText) payDialogLayout.findViewById(R.id.money);
        final TextView tvTility = (TextView) payDialogLayout.findViewById(R.id.tv_tility);// 收益
        String userConent="  年化利率:"+String.format("%.2f",minRate)+"%";
        String durtion="  收益期限:"+reduceDay+"天";
        tvInterest.setText(userConent);
        tvDurtion.setText(durtion);
        etmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.toString().length();
                if (len == 1 && text.equals("0")) {
                    s.clear();
                }else if(len>1&&text.startsWith("0")){
                    s.delete(0,1);
                }
                if (TextUtils.isEmpty(s.toString())) {
                    Toaster.showLong((Activity) context,"请输入理财金额");
                    calculateInterest("0", tvTility);
                } else {
                    calculateInterest(s.toString(), tvTility);
                }
            }
        });

        View.OnClickListener cc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.close:
                        dismiss();
                        break;
                }
            }
        };

        close.setOnClickListener(cc);// 给返回设置点击事件
    }

    private void calculateInterest(String  s, TextView tvTility) {
        DecimalFormat df;
        money=Double.parseDouble(s);
        BigDecimal b = new BigDecimal(money * (minRate / 100.0) / 365.0);
        b = b.multiply(new BigDecimal(reduceDay));
        double f1;
        df = new DecimalFormat("#0.00");
        f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvTility.setText(df.format(f1));//换算后的显示
    }

}
