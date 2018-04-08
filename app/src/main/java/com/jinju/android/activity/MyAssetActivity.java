package com.jinju.android.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.MyAsset;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.UserManager.OnGetMyAssetFinishedListener;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.DataUtils;
import com.jinju.android.widget.PercentPieView;

import java.util.HashMap;
import java.util.Map;

/**
 * 资产视图
 */
public class MyAssetActivity extends BaseActivity {

    private TextView tvCanUseBalance;
    private TextView tvLoanAmount;
    private TextView tvAmountFrozen;
    private TextView tvTotalMoney;
    private Dialog mProgressDialog;
    private PercentPieView percentPieView;
    private double sum;
    private double percentOne;
    private double percentTwo;
    private double percentThree;
    private double[] angle = new double[]{0, 0, 0};
    private double[] data = new double[]{0, 0, 0};
    private int[] color = new int[]{0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_asset);
        mProgressDialog = AppUtils.createLoadingDialog(this);
        initViews();
        getMyAsset();

    }

    private void initViews() {
        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);

        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.my_asset_title);

        tvCanUseBalance = (TextView) findViewById(R.id.tv_can_use_balance);     //可用金额
        tvLoanAmount = (TextView) findViewById(R.id.tv_loan_amount);            //理财金额
        tvAmountFrozen = (TextView) findViewById(R.id.tv_amount_frozen);        //冻结金额
        tvTotalMoney = (TextView) findViewById(R.id.tv_total_money);            //总金额
        percentPieView = (PercentPieView) findViewById(R.id.percentPieView);    //饼状图

        tvTotalMoney.setText(DataUtils.convertCurrencyFormat(0.00));
    }

    @Override
    protected void onDestroy() {
        mProgressDialog.dismiss();
        super.onDestroy();
    }

    private OnClickListener mBtnBackOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            finish();
        }

    };

    private void getMyAsset() {
        mProgressDialog.show();
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("iv", DdApplication.getVersionName());
        DdApplication.getUserManager().getMyAsset(datas, mOnGetMyAssetFinishedListener);
    }

    private OnGetMyAssetFinishedListener mOnGetMyAssetFinishedListener = new OnGetMyAssetFinishedListener() {

        @Override
        public void onGetMyAssetFinished(Response response, MyAsset myAsset) {
            if (response.isSuccess()) {
                tvCanUseBalance.setText(DataUtils.convertCurrencyFormat(myAsset.getCanUseBalance()));   //可用金额
                tvLoanAmount.setText(DataUtils.convertCurrencyFormat(myAsset.getCurrentPosition()));      //理财金额
                tvAmountFrozen.setText(DataUtils.convertCurrencyFormat(myAsset.getFrozenBalance()));    //冻结金额

                if (!(myAsset.getCanUseBalance() == 0 & myAsset.getCurrentPosition() == 0 & myAsset.getFrozenBalance() == 0)) {

                    data[0] = myAsset.getCanUseBalance();       //可用金额
                    data[1] = myAsset.getCurrentPosition();     //理财金额
                    data[2] = myAsset.getFrozenBalance();       //冻结金额
                    color[0] = getResources().getColor(R.color.main_red);
                    color[1] = getResources().getColor(R.color.loanAmount);
                    color[2] = getResources().getColor(R.color.amountFrozen);
                    sum = data[0] + data[1] + data[2];
                    percentOne = data[0] / sum;
                    percentTwo = data[1] / sum;
                    percentThree = data[2] / sum;
                    angle[0] = Math.ceil(percentOne * 360);     //可用
                    angle[1] = Math.ceil(percentTwo * 360);     //理财
                    angle[2] = Math.ceil(percentThree * 360);   //冻结
                    percentPieView.setData(color, sum, angle);
                    //接口取总和值
                    tvTotalMoney.setText(DataUtils.convertCurrencyFormat(myAsset.getNetAssets()));
                }
            } else {
                AppUtils.handleErrorResponse(MyAssetActivity.this, response);
            }

            mProgressDialog.dismiss();

        }
    };

}