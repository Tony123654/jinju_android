package com.jinju.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.activity.MainActivity;
import com.jinju.android.adapter.RedPacketAdapter;
import com.jinju.android.api.Gift;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.AppConstant;
import com.jinju.android.constant.CouponStatus;
import com.jinju.android.manager.UserManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.LoadTitleUtils;
import com.jinju.android.widget.FrameAnimationHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Libra on 2017/12/15.
 */

public class RateRedPacketFragment extends BaseFragment {
    private String rateType = CouponStatus.CAN_USE;
    private int mCurrentPage = 0;
    private int mTotalPage;

    private ListView mListView;
    private List<Gift> mGiftList;
    private RedPacketAdapter mRedPacketAdapter;
    private View mFooterView;
    private Context mContext;
    private String mCouponRule;
    private ImageView mImgNoRedPacket;
    private TextView mTxtNoRedPacketDesc;
    private TextView mTxtNoMore;
    private LinearLayout mLayoutCheckRedPacket;
    private TextView mTxtCheckRedPacket;
    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_red_packet, container, false);

        mContext = getActivity();

        //查看历史红包
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.layout_red_packet_footer_view,null);
        mImgNoRedPacket = (ImageView) mFooterView.findViewById(R.id.img_no_red_packet);
        mTxtNoRedPacketDesc = (TextView) mFooterView.findViewById(R.id.txt_no_red_packet_desc);
        mLayoutCheckRedPacket = (LinearLayout) mFooterView.findViewById(R.id.layout_check_red_packet);
        mTxtCheckRedPacket = (TextView) mFooterView.findViewById(R.id.txt_check_red_packet);
        mTxtNoMore = (TextView) mFooterView.findViewById(R.id.txt_no_more);
        mFooterView.setVisibility(View.GONE);
        mTxtCheckRedPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateType = CouponStatus.HISTORY;
                mOnRateRedPacketListener.onCurrentView(CouponStatus.HISTORY,mCouponRule);
            }
        });

        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(mContext));
        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
        mListView = (ListView) view.findViewById(R.id.list_view);
        //设置刷新文本
        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();
        mGiftList = new ArrayList<Gift>();
        mRedPacketAdapter = new RedPacketAdapter(mContext, mGiftList, rateType);
        mListView = (ListView) view.findViewById(R.id.list_view);

        mListView.addFooterView(mFooterView);
        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setAdapter(mRedPacketAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyCouponList(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private OnRefreshLoadmoreListener mOnRefreshLoadmoreListener= new OnRefreshLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            if(mCurrentPage < mTotalPage) {
                getMyCouponList(mCurrentPage + 1);
            } else {
               refreshLayout.finishLoadmore();
            }
        }
        @Override

        public void onRefresh(RefreshLayout refreshlayout) {
            getMyCouponList(1);
        }
    };
    private void getMyCouponList(int currentPage) {

        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
        datas.put("currentPage", currentPage);
        datas.put("condition", rateType);
        datas.put("category","coupon");
        datas.put("iv", DdApplication.getVersionName());
        DdApplication.getUserManager().getMyCouponList(datas, mOnGetMyCouponListFinishedListener);
    }

    private UserManager.OnGetMyCouponListFinishedListener mOnGetMyCouponListFinishedListener = new UserManager.OnGetMyCouponListFinishedListener() {

        @Override
        public void onGetMyCouponListFinished(Response response, Page page, List<Gift> giftList,String couponRules) {

            if (response.isSuccess()) {
                mCurrentPage = page.getCurrentPage();
                mTotalPage = page.getTotalPage();
                mCouponRule = couponRules;

                if (mCurrentPage == 1) {
                    mGiftList.clear();
                    mGiftList.addAll(giftList);
                    mRedPacketAdapter.notifySetInvalidated(rateType);
                } else {
                    mGiftList.addAll(giftList);
                    mRedPacketAdapter.notifySetChanged(rateType);
                }
                setFooterView();
            } else {
                AppUtils.handleErrorResponse(mContext, response);
            }

            LoadTitleUtils.setRefreshLayout(refreshLayout,mCurrentPage,mLoadInfoList);
        }
    };

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position<mGiftList.size()) {//添加footView会多加个item，所以要限制一下
                if (rateType.equals(CouponStatus.CAN_USE)) {

                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("activityType", MainActivity.TAB_LOAN);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        }

    };
    public interface OnRateRedPacketListener {
        void onCurrentView(String type,String mCouponRule);
    }
    private OnRateRedPacketListener mOnRateRedPacketListener;

    public void setOnRateRedPacketListener(OnRateRedPacketListener onRateRedPacketListener)  {
        if (onRateRedPacketListener!=null) {
            mOnRateRedPacketListener = onRateRedPacketListener;
        }
    }
    public void refreshRate(String type) {
        rateType = type;
        getMyCouponList(1);
    }
    private void setFooterView (){
        if (rateType.equals(CouponStatus.CAN_USE)) {
            if (mCurrentPage< mTotalPage) {
                mFooterView.setVisibility(View.GONE);
            }else {
                mFooterView.setVisibility(View.VISIBLE);
                mTxtNoRedPacketDesc.setVisibility(View.GONE);
                mLayoutCheckRedPacket.setVisibility(View.VISIBLE);
                if (mGiftList!=null&&mGiftList.size()>0){
                    mImgNoRedPacket.setVisibility(View.GONE);
                    mTxtNoMore.setText("无更多可用红包");
                }else {
                    mImgNoRedPacket.setVisibility(View.VISIBLE);
                    mTxtNoMore.setText("暂无可用红包");
                }
            }
        } else{
            //历史界面

            mFooterView.setVisibility(View.VISIBLE);
            if (mGiftList!=null&&mGiftList.size()>0){
                mImgNoRedPacket.setVisibility(View.GONE);
                mTxtNoRedPacketDesc.setVisibility(View.GONE);
            }else {
                mImgNoRedPacket.setVisibility(View.VISIBLE);
                mTxtNoRedPacketDesc.setVisibility(View.VISIBLE);
                mTxtNoRedPacketDesc.setText("暂无失效红包～");
            }
            mLayoutCheckRedPacket.setVisibility(View.GONE);

        }
    }

}
