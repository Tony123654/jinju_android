package com.jinju.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.api.ProductIntroduce;
import com.jinju.android.api.Response;
import com.jinju.android.base.DdApplication;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.dialog.ImagePagerDialog;
import com.jinju.android.util.DDJRCmdUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.PublicStaticClassUtils;
import com.jinju.android.webview.BaseJsBridgeWebViewActivity;
import com.jinju.android.widget.MyDetailScrollView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Libra on 2017/11/1.
 *
 * 项目详情 fragment
 */

public class ProductIntroduceFragment extends BaseFragment {
    private View mView;
    private Context mContext;
    private TextView mTxtProductDesc;
    private TextView mTxtProjectDesc;
    private ImageView mImgProjectTheory;
    private TextView mTxtLoanPurpose;
    private TextView mTxtCommonProblem;
    private LinearLayout mLayoutLoanSource;
    private LinearLayout layoutHorizontalScroll;

    private long financialId;
    private ProductIntroduce mProductIntroduce;
    private List<String> fundSourceList;
    private List<String> imgUrlList;
    private ImagePagerDialog mImagePagerDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_product_introduce, container, false);
        mContext = getContext();
        financialId = getArguments().getLong("financialId");
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {//判断Fragment已经依附Activity
            getProductIntroduce();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImagePagerDialog!=null) {
            mImagePagerDialog.dismiss();
        }
    }

    private void initView() {
        mTxtProductDesc = (TextView) mView.findViewById(R.id.txt_product_desc);
        mImgProjectTheory = (ImageView) mView.findViewById(R.id.img_project_theory);
        mTxtProjectDesc = (TextView) mView.findViewById(R.id.txt_project_desc);
        mTxtLoanPurpose = (TextView) mView.findViewById(R.id.txt_loan_purpose);
        mLayoutLoanSource = (LinearLayout) mView.findViewById(R.id.layout_loan_source);
        mTxtCommonProblem = (TextView) mView.findViewById(R.id.txt_common_problem);
        mTxtCommonProblem.setOnClickListener(mTxtCommonProblemOnClickListener);
        layoutHorizontalScroll = (LinearLayout) mView.findViewById(R.id.layout_horizontal_scroll);
        //图片展示窗口
        if (mImagePagerDialog==null) {
            mImagePagerDialog = new ImagePagerDialog(getActivity(),imgUrlList);
        }

        MyDetailScrollView oneScrollView= (MyDetailScrollView) mView.findViewById(R.id.oneScrollview);
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
    private View.OnClickListener mTxtCommonProblemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = mProductIntroduce.getQuestionUrl();
            if (url.startsWith(DDJRCmdUtils.DDJR_OVERRIDE_SCHEMA)) {
                //点击webview上的控件，执行指定跳转
                DDJRCmdUtils.handleProtocol(getActivity(), url);
            } else {
                Intent intent = new Intent(getContext(), BaseJsBridgeWebViewActivity.class);
                intent.putExtra(BaseJsBridgeWebViewActivity.EXTRA_URL_PATH,url);
                startActivity(intent);
            }

        }
    };

    private void getProductIntroduce() {
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("financialId", financialId);
        datas.put("iv", DdApplication.getVersionName());

        DdApplication.getLoanManager().getProductIntroduce(datas, mOnGetProductIntroduceFinishedListener);
    }
    private LoanManager.OnGetProductIntroduceFinishedListener mOnGetProductIntroduceFinishedListener = new  LoanManager.OnGetProductIntroduceFinishedListener() {
        @Override
        public void onGetProductIntroduceFinished(Response response, ProductIntroduce productIntroduce) {

            if (response.isSuccess()) {
                mProductIntroduce = productIntroduce;
                imgUrlList = productIntroduce.getPledgeFiles();
                mTxtProductDesc.setText( productIntroduce.getProDesc());
                ImageUtils.displayImage(mImgProjectTheory, productIntroduce.getProjectTheory());
                mTxtProjectDesc.setText(productIntroduce.getLoanerDesc());
                mTxtLoanPurpose.setText(productIntroduce.getLoanUsage());

                fundSourceList = productIntroduce.getFundReturnSource();

                //借款来源
                if (mLayoutLoanSource.getChildCount()>0) {
                    mLayoutLoanSource.removeAllViews();
                }
                for (String sourceDesc:fundSourceList) {
                    TextView mTextView = new TextView(mContext);
                    mTextView.setText(sourceDesc);
                    mTextView.setTextColor(getResources().getColor(R.color.word_black));
                    mTextView.setTextSize(14);
                    mLayoutLoanSource.addView(mTextView);
                }
                //材料图片
                if (layoutHorizontalScroll.getChildCount()>0) {
                    layoutHorizontalScroll.removeAllViews();
                }
                for (int i = 0; i < productIntroduce.getPledgeFiles().size() ;i++){

                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setLayoutParams( new ViewGroup.LayoutParams(250, 250));
                    layout.setGravity(Gravity. CENTER);

                    ImageView imageView = new ImageView(mContext);
                    imageView.setLayoutParams( new ViewGroup.LayoutParams(200,200));
                    imageView.setScaleType(ImageView.ScaleType. CENTER_CROP);
                    ImageUtils.displayImage(imageView, productIntroduce.getPledgeFiles().get(i));
                    layout.addView(imageView);
                    final int current = i;

                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mImagePagerDialog!=null) {
                                mImagePagerDialog.showImgPager(imgUrlList,current);
                            }

                        }
                    });

                    layoutHorizontalScroll.addView(layout);

                }
            }
        }
    };
}
