package com.jinju.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jinju.android.R;
import com.jinju.android.activity.FinancialConfirmActivity;
import com.jinju.android.adapter.SelectRedPacketAdapter;
import com.jinju.android.api.MemberGift;

import java.util.List;

/**
 * Created by Libra on 2017/12/18.
 */

public class SelectRateTicketFragment extends BaseFragment {

    private Context mContext;
    private ListView mListView;
    private List<MemberGift> mInterestGiftList;
    private SelectRedPacketAdapter mSelectAdapter;
    private OnRateTicketSelectItemListener mOnSelectItemListener;
    private int mSelectPosition;

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_red_packet, container, false);

        mContext = getActivity();
        mInterestGiftList = (List<MemberGift>) getArguments().getSerializable(FinancialConfirmActivity.INTEREST_GIFT_LIST);
        mSelectPosition = getArguments().getInt(FinancialConfirmActivity.SELECT_POSITION);

        int mSelectType = getArguments().getInt(FinancialConfirmActivity.SELECT_Type);
        if (mSelectType!=0) {//选中的不是加息券
            mSelectPosition = -1;
        }
        mSelectAdapter = new SelectRedPacketAdapter(mContext,mInterestGiftList,mSelectPosition);
        mListView = (ListView) view.findViewById(R.id.list_view);

        mListView.setOnItemClickListener(mOnItemClickListener);
        mListView.setAdapter(mSelectAdapter);
        mListView.setEmptyView(view.findViewById(R.id.layout_empty));

        return view;
    }
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSelectAdapter.setSelectedPosition(position);
            mSelectAdapter.notifyDataSetInvalidated();
            mOnSelectItemListener.onItemSelect(position);
        }
    };
    public interface OnRateTicketSelectItemListener {
        void onItemSelect(int position);
    }
    public void setOnSelectItemListener(OnRateTicketSelectItemListener onSelectItemListener) {
        if (onSelectItemListener!=null) {
            mOnSelectItemListener = onSelectItemListener;
        }
    }
}
