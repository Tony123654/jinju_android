package com.jinju.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.adapter.InvestRecordAdapter;
import com.jinju.android.api.InvestRecord;
import com.jinju.android.api.Page;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.PublicStaticClassUtils;
import com.jinju.android.widget.MyDetailScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Libra on 2017/11/2.
 */

public class InvestRecordFragment extends BaseFragment {


    private long financialId;
    private List<InvestRecord> mInvestRecordList;
    private View mView;
    private TextView txtUserPhoneFirst;
    private TextView txtInvestAmountFirst;
    private TextView txtUserPhoneTwo;
    private TextView txtInvestAmountTwo;
    private TextView txtUserPhoneThird;
    private TextView txtInvestAmountThird;
    private ListView mListView;
    private InvestRecordAdapter mInvestRecordAdapter;
    private Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //创建接口回调
        mInvestRecordListener = (InvestRecordListener)context;
    }

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        financialId = getArguments().getLong("financialId");
        mView = inflater.inflate(R.layout.fragment_invest_record, container, false);
        initView();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {//判断Fragment已经依附Activity
            getInvestRecord(1);
        }
    }

    private void initView() {
//        txtUserPhoneFirst = (TextView) mView.findViewById(R.id.txt_user_phone_first);
//        txtUserPhoneTwo = (TextView) mView.findViewById(R.id.txt_user_phone_two);
//        txtUserPhoneThird = (TextView)  mView.findViewById(R.id.txt_user_phone_third);
//        txtInvestAmountFirst = (TextView) mView.findViewById(R.id.txt_invest_amount_first);
//        txtInvestAmountTwo = (TextView) mView.findViewById(R.id.txt_invest_amount_two);
//        txtInvestAmountThird = (TextView) mView.findViewById(R.id.txt_invest_amount_third);
        mInvestRecordList = new ArrayList<InvestRecord>();
        mInvestRecordAdapter = new InvestRecordAdapter(mContext,mInvestRecordList);

        mListView = (ListView) mView.findViewById(R.id.list_view);

        mListView.setEmptyView(mView.findViewById(R.id.layout_empty));
        mListView.setAdapter(mInvestRecordAdapter);

        MyDetailScrollView oneScrollView= (MyDetailScrollView) mView.findViewById(R.id.threeScrollview);
        oneScrollView.setScrollListener(new MyDetailScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if (scrollY == 0) {
                    PublicStaticClassUtils.IsTop = true;
                } else {
                    PublicStaticClassUtils.IsTop = false;
                }
            }

            @Override
            public void notBottom() {

            }

        });
    }

    private void getInvestRecord(int currentPage) {

        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("pageSize", 100);
        datas.put("currentPage", currentPage);
        datas.put("fpId", financialId);
        datas.put("iv", DdApplication.getVersionName());

        DdApplication.getLoanManager().getInvestRecord(datas,mOnGetInvestRecordFinishedListener);
    }
    private LoanManager.OnGetInvestRecordFinishedListener mOnGetInvestRecordFinishedListener = new LoanManager.OnGetInvestRecordFinishedListener() {
        @Override
        public void onGetInvestRecordFinished(Response response,int countRecord,Page page, List<InvestRecord> orderList, List<InvestRecord> topThreeList) {

            if (response.isSuccess()) {

                mInvestRecordListener.RefreshSucceed(countRecord);

                mInvestRecordList.clear();
                mInvestRecordList.addAll(orderList);
                mInvestRecordAdapter.notifyDataSetInvalidated();

                if (topThreeList.size()==1) {
                    txtUserPhoneFirst.setText(topThreeList.get(0).getTopMobile());
                    txtInvestAmountFirst.setText(topThreeList.get(0).getTopPayAmount());

                    txtUserPhoneTwo.setText("暂无");
                    txtUserPhoneThird.setText("暂无");
                }
                if(topThreeList.size()==2) {
                    txtUserPhoneFirst.setText(topThreeList.get(0).getTopMobile());
                    txtInvestAmountFirst.setText(topThreeList.get(0).getTopPayAmount());
                    txtUserPhoneTwo.setText(topThreeList.get(1).getTopMobile());
                    txtInvestAmountTwo.setText(topThreeList.get(1).getTopPayAmount());

                    txtUserPhoneThird.setText("暂无");
                }
                if(topThreeList.size()==3) {
                    txtUserPhoneFirst.setText(topThreeList.get(0).getTopMobile());
                    txtInvestAmountFirst.setText(topThreeList.get(0).getTopPayAmount());
                    txtUserPhoneTwo.setText(topThreeList.get(1).getTopMobile());
                    txtInvestAmountTwo.setText(topThreeList.get(1).getTopPayAmount());
                    txtUserPhoneThird.setText(topThreeList.get(2).getTopMobile());
                    txtInvestAmountThird.setText(topThreeList.get(2).getTopPayAmount());
                }

            } else {
                AppUtils.handleErrorResponse(mContext, response);
            }

        }
    };
    public interface InvestRecordListener {
        public void RefreshSucceed(int countRecord);
    }
    private InvestRecordListener mInvestRecordListener;
}
