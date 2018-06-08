package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jinju.android.R;

public class IntroduceActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.invest_first:
                startActivity(new Intent(this,FinancialDetailActivity.class));
        }

    }
}
