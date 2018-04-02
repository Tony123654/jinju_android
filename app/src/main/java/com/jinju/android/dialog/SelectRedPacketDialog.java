package com.jinju.android.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.activity.FinancialConfirmActivity;
import com.jinju.android.api.MemberGift;
import com.jinju.android.fragment.SelectRateTicketFragment;
import com.jinju.android.fragment.SelectRedPacketFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/12/18.
 */

public class SelectRedPacketDialog extends DialogFragment{
    private static final int MY_RED_PACKET_FRAGMENT = 0;
    private static final int RATE_RED_PACKET_FRAGMENT = 1;
    private TextView mTxtMyRedPacket;
    private TextView mTxtRateTicket;
    private List<Fragment>  mFragmentList = new ArrayList<Fragment>();
    private ViewPager mViewPager;

    private List<MemberGift> mMemberGiftList;//普通红包
    private List<MemberGift> mInterestList;//加息红包
    private View view;
    private SelectRateTicketFragment mRateTicketFragment;
    private SelectRedPacketFragment mRedPacketFragment;
    private OnSelectItemListener mOnSelectItemListener;
    private int mSelectPosition;
    private int mSelectType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view =   inflater.inflate(R.layout.layout_select_red_packet_dialog, container);
        mMemberGiftList = (List<MemberGift>)getArguments().getSerializable(FinancialConfirmActivity.MEMBER_GIFT_LIST);
        mInterestList = (List<MemberGift>)getArguments().getSerializable(FinancialConfirmActivity.INTEREST_GIFT_LIST);
        mSelectPosition = getArguments().getInt(FinancialConfirmActivity.SELECT_POSITION);
        mSelectType = getArguments().getInt(FinancialConfirmActivity.SELECT_Type);

        setStyle(DialogFragment.STYLE_NORMAL,R.style.select_red_packet_dialog);
        Dialog dialog = getDialog();

        Window window  = getDialog().getWindow();
        window.setWindowAnimations(R.style.anim_action_dialog);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.getDecorView().setPadding(0, 0, 0, 0);//设置水平铺满屏幕
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        Display display =  getActivity().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = (int) (display.getHeight() * 0.6);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);

        Bundle bundle = new Bundle();
        bundle.putSerializable(FinancialConfirmActivity.MEMBER_GIFT_LIST,(Serializable)mMemberGiftList);
        bundle.putSerializable(FinancialConfirmActivity.INTEREST_GIFT_LIST,(Serializable)mInterestList);
        bundle.putInt(FinancialConfirmActivity.SELECT_POSITION,mSelectPosition);
        bundle.putInt(FinancialConfirmActivity.SELECT_Type,mSelectType);
        mRateTicketFragment = new SelectRateTicketFragment();
        mRateTicketFragment.setArguments(bundle);
        mRateTicketFragment.setOnSelectItemListener(mRateTicketOnSelectItemListener);
        mRedPacketFragment = new SelectRedPacketFragment();
        mRedPacketFragment.setArguments(bundle);
        mRedPacketFragment.setOnSelectItemListener(mRedPacketOnSelectItemListener);

        mViewPager = (ViewPager) view.findViewById(R.id.vp);

        mFragmentList.add(mRateTicketFragment);
        mFragmentList.add(mRedPacketFragment);
        MyViewPagerAdapter mViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        if (mSelectPosition>=0) {
            if (mSelectType==0) {
                mViewPager.setCurrentItem(0);//选中加息红包
            }
            if (mSelectType==1) {
                mViewPager.setCurrentItem(1);//选中抵扣红包
            }
        }else {

            //优先弹出抵扣红包
            if (mMemberGiftList!=null&&mMemberGiftList.size()>0) {
                mViewPager.setCurrentItem(1);
            } else {
                mViewPager.setCurrentItem(0);
            }
        }
        iniView();
        return view;
    }

    private void iniView() {

        TextView mTxtCancel = (TextView) view.findViewById(R.id.txt_cancel);
        mTxtCancel.setOnClickListener(mTxtCancelOnClickListener);
        mTxtMyRedPacket = (TextView) view.findViewById(R.id.txt_my_red_packet);
        mTxtRateTicket = (TextView) view.findViewById(R.id.txt_rate_ticket);
        mTxtMyRedPacket.setOnClickListener(mTxtMyRedPacketOnClickListener);
        mTxtRateTicket.setOnClickListener(mTxtRateTicketOnClickListener);

    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            int currentItem = mViewPager.getCurrentItem();
            switch (currentItem) {
                case MY_RED_PACKET_FRAGMENT:
                    mTxtMyRedPacket.setTextColor(getResources().getColor(R.color.light_red));
                    mTxtMyRedPacket.setBackgroundResource(R.mipmap.bg_loan_tab);
                    mTxtRateTicket.setTextColor(getResources().getColor(R.color.word_black));
                    mTxtRateTicket.setBackgroundResource(R.color.white);
                    break;
                case RATE_RED_PACKET_FRAGMENT:
                    mTxtMyRedPacket.setTextColor(getResources().getColor(R.color.word_black));
                    mTxtMyRedPacket.setBackgroundResource(R.color.white);
                    mTxtRateTicket.setTextColor(getResources().getColor(R.color.light_red));
                    mTxtRateTicket.setBackgroundResource(R.mipmap.bg_loan_tab);
                    break;
                default:
                    break;
            }
        }

    }
    private View.OnClickListener mTxtMyRedPacketOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeView(MY_RED_PACKET_FRAGMENT);
        }
    };
    private View.OnClickListener mTxtRateTicketOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeView(RATE_RED_PACKET_FRAGMENT);
        }
    };
    private void changeView(int desTab) {
        mViewPager.setCurrentItem(desTab, true);
    }

    /**
     * 确定 不使用红包
     */
    private View.OnClickListener mTxtCancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSelectPosition = -1;
            mOnSelectItemListener.onItemSelect(mSelectType,-1);
            dismiss();
        }
    };
    /**
     * 加息券选中
     */
    private SelectRateTicketFragment.OnRateTicketSelectItemListener mRateTicketOnSelectItemListener  = new SelectRateTicketFragment.OnRateTicketSelectItemListener() {
        @Override
        public void onItemSelect(int position) {
            mSelectPosition = position;
            mSelectType = 0;
            mOnSelectItemListener.onItemSelect(mSelectType,mSelectPosition);
            dismiss();
        }
    };
    /**
     * 红包选中
     */
    private SelectRedPacketFragment.OnRedPacketSelectItemListener mRedPacketOnSelectItemListener  = new SelectRedPacketFragment.OnRedPacketSelectItemListener() {

        @Override
        public void onItemSelect(int position) {
            mSelectPosition = position;
            mSelectType = 1;
            mOnSelectItemListener.onItemSelect(mSelectType,mSelectPosition);
            dismiss();
        }
    };
    public interface OnSelectItemListener {
        void onItemSelect(int type,int position);
    }
    public void setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        if (onSelectItemListener!=null) {
            mOnSelectItemListener = onSelectItemListener;
        }
    }

}
