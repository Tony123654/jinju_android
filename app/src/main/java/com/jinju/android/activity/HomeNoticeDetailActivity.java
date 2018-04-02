package com.jinju.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.HomeNotice;

/**
 * Created by Libra on 2017/10/17.
 */

public class HomeNoticeDetailActivity extends BaseActivity {

    private HomeNotice notice;
    private TextView mTxtNoticeTitle;
    private TextView mTxtContent;
    private TextView mTxtMark;
    private TextView mTxtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_notice_detail);
        notice = (HomeNotice)getIntent().getSerializableExtra("noticeDetail");
        initView();
    }
    private void initView() {
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText(R.string.home_notice_detail_title);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTxtNoticeTitle = (TextView) findViewById(R.id.txt_notice_title);
        mTxtContent = (TextView) findViewById(R.id.txt_content);
        mTxtMark = (TextView) findViewById(R.id.txt_mark);
        mTxtTime = (TextView) findViewById(R.id.txt_time);

        mTxtNoticeTitle.setText(notice.getTitle());
        mTxtContent.setText(notice.getContent());
        mTxtMark.setText(notice.getMark());
        mTxtTime.setText(notice.getNoticeDate());

    }
}
