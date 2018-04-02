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
 * Created by wangjw on 2017/1/29.
 */

public class MyRedPacketFragment extends BaseFragment {

    private String redPacketType = CouponStatus.CAN_USE;
    private int mCurrentPage = 0;
    private int mTotalPage;
    private ListView mListView;
    private RedPacketAdapter mRedPacketAdapter;
    private List<Gift> mGiftList;

    private Context mContext;
    private View mFooterView;
    private ImageView mImgNoRedPacket;
    private TextView mTxtNoRedPacketDesc;
    private TextView mTxtNoMore;
    private TextView mTxtCheckRedPacket;
    private LinearLayout mLayoutCheckRedPacket;
    private String mCouponRule;//红包规则地址
    private List<String> mLoadInfoList;
    private SmartRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_red_packet, container, false);

        mContext = getActivity();

        //查看历史红包
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.layout_red_packet_footer_view,null);
        mImgNoRedPacket = (ImageView) mFooterView.findViewById(R.id.img_no_red_packet);
        mTxtNoRedPacketDesc = (TextView) mFooterView.findViewById(R.id.txt_no_red_packet_desc);
        mTxtNoMore = (TextView) mFooterView.findViewById(R.id.txt_no_more);
        mLayoutCheckRedPacket = (LinearLayout) mFooterView.findViewById(R.id.layout_check_red_packet);
        mTxtCheckRedPacket = (TextView) mFooterView.findViewById(R.id.txt_check_red_packet);
        mFooterView.setVisibility(View.GONE);
        mTxtCheckRedPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPacketType = CouponStatus.HISTORY;
                mOnMyRedPacketListener.onCurrentView(CouponStatus.HISTORY,mCouponRule);
            }
        });

        mGiftList = new ArrayList<Gift>();
        mRedPacketAdapter = new RedPacketAdapter(mContext, mGiftList,redPacketType);
        refreshLayout = (SmartRefreshLayout)view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new FrameAnimationHeader(mContext));
        refreshLayout.setOnRefreshLoadmoreListener(mOnRefreshLoadmoreListener);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.addFooterView(mFooterView);
        //设置刷新文本
        mLoadInfoList = DdApplication.getConfigManager().getLoadInfoList();

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

    private void getMyCouponList(int currentPage) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", AppConstant.DEFAULT_PAGE_SIZE);
        datas.put("currentPage", currentPage);
        datas.put("condition", redPacketType);
        datas.put("category","envelope");//红包
        datas.put("iv",DdApplication.getVersionName());

        DdApplication.getUserManager().getMyCouponList(datas, mOnGetMyCouponListFinishedListener);
    }

    private UserManager.OnGetMyCouponListFinishedListener mOnGetMyCouponListFinishedListener = new UserManager.OnGetMyCouponListFinishedListener() {

        @Override
        public void onGetMyCouponListFinished(Response response, Page page, List<Gift> giftList,String couponRule) {

            if (response.isSuccess()) {
                mCurrentPage = page.getCurrentPage();
                mTotalPage = page.getTotalPage();

                mCouponRule = couponRule;
                mOnGetCouponRuleListener.getCouponRule(mCouponRule);

                if (mCurrentPage == 1) {
                    mGiftList.clear();
                    mGiftList.addAll(giftList);
                    mRedPacketAdapter.notifySetInvalidated(redPacketType);//重绘控件（还原到初始状态）
                } else {
                    mGiftList.addAll(giftList);
                    mRedPacketAdapter.notifySetChanged(redPacketType);//重绘当前可见区域
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

            if (redPacketType.equals(CouponStatus.CAN_USE)) {

                if (position<mGiftList.size()) {//添加footView会多加个item，所以要限制一下
                    if (mGiftList.get(position).getmGiftType()==2) {//现金红包充值
                        useRedPacket(mGiftList.get(position).getMemberGiftId());
                    } else {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("activityType", MainActivity.TAB_LOAN);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }

            }

        }

    };

    private void useRedPacket(long memberGiftId) {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("memberGiftId", memberGiftId);

        DdApplication.getUserManager().couponCharge(datas, mOnCouponChargeFinishedListener);
    }

    private UserManager.OnCouponChargeFinishedListener mOnCouponChargeFinishedListener=new UserManager.OnCouponChargeFinishedListener() {
        @Override
        public void onCouponChargeFinished(Response response) {
            if (response.isSuccess()) {
                AppUtils.showToast(mContext, response.getMessage());
                getMyCouponList(1);
            } else {
                AppUtils.handleErrorResponse(mContext, response);
            }

        }
    };


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

    public interface OnMyRedPacketListener {
        void onCurrentView(String type,String mCouponRule);
    }
    private OnMyRedPacketListener mOnMyRedPacketListener;

    public void setOnMyRedPacketListener(OnMyRedPacketListener onMyRedPacketListener)  {
        if (onMyRedPacketListener!=null) {
            mOnMyRedPacketListener = onMyRedPacketListener;
        }
    }
    //获取红包规则
    public interface OnGetCouponRuleListener{
        void getCouponRule(String couponRule);
    }
    private OnGetCouponRuleListener mOnGetCouponRuleListener;
    public void setOnGetCouponRuleListener(OnGetCouponRuleListener onGetCouponRuleListener) {
        if(onGetCouponRuleListener!=null) {
            mOnGetCouponRuleListener = onGetCouponRuleListener;
        }
    }

    /**
     * 刷新界面
     * @param type
     */
    public void refreshMyRedPacket(String type) {
        redPacketType = type;
        getMyCouponList(1);
    }
    private void setFooterView (){
        mFooterView.setVisibility(View.VISIBLE);
        if (redPacketType.equals(CouponStatus.CAN_USE)) {
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
        } else{//历史
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
