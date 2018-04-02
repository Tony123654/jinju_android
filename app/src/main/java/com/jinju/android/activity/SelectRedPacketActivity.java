package com.jinju.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.SelectRedPacketAdapter;
import com.jinju.android.api.MemberGift;
import com.jinju.android.constant.UmengTouchType;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class SelectRedPacketActivity extends BaseActivity {
    private RelativeLayout mLayoutNoUse;
    private ImageView mImgNoUseConfirm;
    private ListView mListView;
    private SelectRedPacketAdapter mSelectRedPacketAdapter;

    private List<MemberGift> mMemberGiftList;
    private List<MemberGift> canUseGiftList;
    private List<MemberGift> noCanUseGiftList;
    private int selectRed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_red_packet);

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(mBtnBackOnClickListener);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("选择使用红包");

        Intent intent = getIntent();

        canUseGiftList = (ArrayList<MemberGift>) intent.getSerializableExtra("canUseGiftList");
        noCanUseGiftList = (ArrayList<MemberGift>) intent.getSerializableExtra("noCanUseGiftList");
        selectRed = intent.getIntExtra("selectRed",-1);
        mLayoutNoUse = (RelativeLayout) findViewById(R.id.layout_no_use);
        mLayoutNoUse.setOnClickListener(mLayoutNoUseOnClickListener);
        mImgNoUseConfirm = (ImageView) findViewById(R.id.img_no_use_confirm);

        if (selectRed== -2) {
            mImgNoUseConfirm.setVisibility(View.VISIBLE);
        }
        //紅包列表数据
        getGift();
        mListView = (ListView) findViewById(R.id.list_view);

        mSelectRedPacketAdapter = new SelectRedPacketAdapter(this, mMemberGiftList,selectRed);
        mListView.setAdapter(mSelectRedPacketAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

    }

    private View.OnClickListener mBtnBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(SelectRedPacketActivity.this, UmengTouchType.RP104_6);

            finish();
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            //如果点击的是可用红包，即可以跳转
            if (position <= canUseGiftList.size()-1) {
                MobclickAgent.onEvent(SelectRedPacketActivity.this, UmengTouchType.RP104_5);

                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };
    //不使用红包
    private View.OnClickListener mLayoutNoUseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MobclickAgent.onEvent(SelectRedPacketActivity.this, UmengTouchType.RP104_4);

            Intent intent = new Intent();
            intent.putExtra("position", -2);
            setResult(RESULT_OK, intent);
            finish();
        }
    };
    private void getGift() {
        mMemberGiftList = new ArrayList<MemberGift>();
        //添加可用红包到集合
        for (int i = 0; i < canUseGiftList.size(); i++) {
            mMemberGiftList.add(canUseGiftList.get(i));
        }
        //添加不可用红包到集合
        if (noCanUseGiftList.size()>0) {
            for (int i = 0; i < noCanUseGiftList.size(); i++) {
                mMemberGiftList.add(noCanUseGiftList.get(i));
            }
        }
    }
}
