package com.jinju.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinju.android.R;
import com.jinju.android.activity.FinancialMoreActivity;
import com.jinju.android.api.Financial;
import com.jinju.android.api.Remind;
import com.jinju.android.api.Response;
import com.jinju.android.api.Tag;
import com.jinju.android.base.DdApplication;
import com.jinju.android.constant.UmengTouchType;
import com.jinju.android.dialog.CustomRoundDialog;
import com.jinju.android.manager.ConfigManager;
import com.jinju.android.manager.LoanManager;
import com.jinju.android.util.AppUtils;
import com.jinju.android.util.CalendarEventUtils;
import com.jinju.android.util.ImageUtils;
import com.jinju.android.util.NumberUtils;
import com.jinju.android.util.Utils;
import com.umeng.analytics.MobclickAgent;

import pub.devrel.easypermissions.EasyPermissions;

public class LoanFinancialAdapter extends BaseAdapter {

	private static final int REQUEST_PERMISSION_CALENDAR = 1;
	private Activity mContext;
	private List<Financial> mAllFinancialList;
	private List<Long> mSellTimeList;
	private int mFinancialOutCount = 0;
	private int mPrivilegeCount = 0;//特权项目
	private int mFinancialActivityCount = 0;//活动项目
	private int mChoicenessCount = 0;//精选
	private ViewHolder holder;
	private Financial financial;
	private String mRegId = "";
	private boolean isRemindFlag;
	private final ConfigManager mConfigManager;
	private List<Remind> list;
	private Intent mIntent;

	public LoanFinancialAdapter(Activity context, List<Financial> financialList,Intent intent) {
		mContext = context;
		mAllFinancialList = financialList;
		mIntent = intent;
		mConfigManager = DdApplication.getConfigManager();
	}
	public void notifyChanged(List<Financial> financialList,int privilegeCount, int financialActivityCount,int choicenessCount, int financialOutCount, List<Long> sellTimeList, String regId) {

		mAllFinancialList = financialList;
		mPrivilegeCount = privilegeCount;
		mFinancialOutCount = financialOutCount;
		mFinancialActivityCount = financialActivityCount;
		mChoicenessCount = choicenessCount;
		mSellTimeList = sellTimeList;
		mRegId = regId;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return mAllFinancialList == null ? 0 : mAllFinancialList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.layout_product_item, null);

			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.iconNew = (ImageView) convertView.findViewById(R.id.iv_new);
			holder.horizontalProgress = (ProgressBar) convertView.findViewById(R.id.progress_bar);
			holder.txtYearInterest = (TextView) convertView.findViewById(R.id.tv_year_interest);
			holder.txtAddInterest = (TextView) convertView.findViewById(R.id.tv_add_interest);
			holder.txtLoanPeriod = (TextView) convertView.findViewById(R.id.tv_loan_period);
			holder.txtActivityTag = (TextView) convertView.findViewById(R.id.tv_activity_tag);
			holder.txtActivityTag_2 = (TextView) convertView.findViewById(R.id.tv_activity_tag_2);
			holder.txtSurplusInvest = (TextView) convertView.findViewById(R.id.tv_surplus_invest);
			holder.sellOutMore = (RelativeLayout)convertView.findViewById(R.id.rl_sell_out);
			holder.countDown = (TextView)convertView.findViewById(R.id.tv_count_down);
			holder.txtRemind = (TextView)convertView.findViewById(R.id.txt_remind);
			holder.txtChoicenessActivity = (TextView)convertView.findViewById(R.id.txt_choiceness_activity);
			holder.txtUnit = (TextView)convertView.findViewById(R.id.tv_unit);//單位
			holder.layoutSurplusInvest = convertView.findViewById(R.id.layout_surplus_invest);
			holder.layoutSellOut = convertView.findViewById(R.id.layout_sell_out);
			holder.layoutCountDown = (RelativeLayout)convertView.findViewById(R.id.layout_count_down);
			holder.mTextViewList = new ArrayList<TextView>();
			holder.mTextViewList.add(holder.txtActivityTag);
			holder.mTextViewList.add(holder.txtActivityTag_2);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}

		financial = mAllFinancialList.get(position);

		holder.txtName.setText(financial.getName());
		//进度条和时间
		long beginDuration = mSellTimeList.get(position);
		if (beginDuration >= 1000){//倒计时
			holder.layoutCountDown.setVisibility(View.VISIBLE);
			holder.horizontalProgress.setVisibility(View.GONE);
			holder.countDown.setText(mContext.getString(R.string.home_count_down,Utils.toHourDate(beginDuration)));
		} else {
			holder.layoutCountDown.setVisibility(View.GONE);
			holder.horizontalProgress.setVisibility(View.VISIBLE);
			int hasPercent = financial.getHasPercent();
			holder.horizontalProgress.setProgress(hasPercent);
		}
		//判断是否已开售提醒

		list = mConfigManager.getRemindList();

		isRemindFlag = list.get(position).getRemindFlag();
		holder.txtRemind.setVisibility(View.VISIBLE);
		if (isRemindFlag) {
			holder.txtRemind.setBackgroundResource(R.drawable.solid_noradius_blue_bg);
			holder.txtRemind.setText(R.string.loan_cancel_remind);
			holder.txtRemind.setTextColor(mContext.getResources().getColor(R.color.blue_selected));
		} else {
			holder.txtRemind.setBackgroundResource(R.drawable.solid_noradius_red_bg);
			holder.txtRemind.setText(R.string.loan_open_remind);
			holder.txtRemind.setTextColor(mContext.getResources().getColor(R.color.main_red));
		}
		holder.txtRemind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//重新获取
				isRemindFlag = list.get(position).getRemindFlag();
				//开启权限
				if (!EasyPermissions.hasPermissions(mContext,new String[]{Manifest.permission.READ_CALENDAR})) {
					mIntent.putExtra("permissions",true);
					//发送广播
					mContext.sendBroadcast(mIntent);
				} else {

					if (!isRemindFlag) {
						//开启提醒
						final CustomRoundDialog customRoundDialog = new CustomRoundDialog(mContext, 2);
						customRoundDialog.setMessageTitle(mContext.getString(R.string.loan_remind_dialog_title));
						customRoundDialog.setContent(mContext.getString(R.string.loan_remind_dialog));
						customRoundDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (EasyPermissions.hasPermissions(mContext,new String[]{Manifest.permission.READ_CALENDAR})) {
									//重新获取数据，否则数据混乱
									financial = mAllFinancialList.get(position);
									String tsStartSubscribe = financial.getTsStartSubscribe();
									long mTsStartSubscribe = Long.parseLong(tsStartSubscribe);
									CalendarEventUtils.insertCalendar(mContext.getString(R.string.app_name),mContext.getString(R.string.loan_remind_content,financial.getName(),Utils.stampToDate(tsStartSubscribe)),mTsStartSubscribe,mContext,new CalendarEventUtils.OnAddCalendarListener() {
										@Override
										public void addCalendarFinish() {

											setSaleRemind(position,financial.getId());
										}
									});
								}  else {

									//去请求权限
									ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.READ_CALENDAR},REQUEST_PERMISSION_CALENDAR);
								}
								customRoundDialog.cancel();

							}
						});
						customRoundDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								customRoundDialog.cancel();
							}
						});
						customRoundDialog.show();

					} else {
						//重新获取数据，否则数据混乱
						financial = mAllFinancialList.get(position);
						//取消提醒
						cancelSaleRemind(position,financial.getId());
					}
				}

			}
		});


		long hasFundsAmount = financial.getHasFundsAmount()/100;

		if (hasFundsAmount < 10000 && hasFundsAmount > 0 ) {

			holder.txtSurplusInvest.setText(hasFundsAmount+"元");
		}
		if (hasFundsAmount > 10000 | hasFundsAmount == 10000) {
			double amount = (double)hasFundsAmount/10000;
			String money = NumberUtils.floatTwoStr(amount);

			holder.txtSurplusInvest.setText(money+"万元");
		}
		String status = financial.getShowStatus();
		if(status.equals("3")| status.equals("4")) {
			//已售罄进度为0
			holder.horizontalProgress.setProgress(0);
			holder.txtSurplusInvest.setText("已售罄");
			holder.txtSurplusInvest.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));

			holder.txtName.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
			holder.txtYearInterest.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
			holder.txtAddInterest.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
			holder.txtUnit.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));
			holder.txtLoanPeriod.setTextColor(mContext.getResources().getColor(R.color.word_black_gray));

			holder.layoutSurplusInvest.setVisibility(View.GONE);
			holder.layoutSellOut.setVisibility(View.VISIBLE);
		}else {
			holder.txtName.setTextColor(mContext.getResources().getColor(R.color.word_black));
			holder.txtYearInterest.setTextColor(mContext.getResources().getColor(R.color.main_red));
			holder.txtAddInterest.setTextColor(mContext.getResources().getColor(R.color.main_red));
			holder.txtUnit.setTextColor(mContext.getResources().getColor(R.color.main_red));
			holder.txtLoanPeriod.setTextColor(mContext.getResources().getColor(R.color.word_black));
			holder.txtSurplusInvest.setTextColor(mContext.getResources().getColor(R.color.word_black));

			holder.layoutSurplusInvest.setVisibility(View.VISIBLE);
			holder.layoutSellOut.setVisibility(View.GONE);
		}

		holder.txtYearInterest.setText(financial.getActualInterestRate());

		if (!TextUtils.isEmpty(financial.getSubsidyInterestRate())) {

			double SubsidyInterestRate = Double.parseDouble(financial.getSubsidyInterestRate());
			if (SubsidyInterestRate > 0) {
				holder.txtAddInterest.setText("+"+ financial.getSubsidyInterestRate()+"%");
			} else {
				holder.txtAddInterest.setText("");
			}
		} else {
			holder.txtAddInterest.setText("");
		}

		String loanPeriodDesc = financial.getLoanPeriodDesc();
		holder.txtLoanPeriod.setText(loanPeriodDesc);

		List<Tag> tags = financial.getTagList();


		holder.txtActivityTag.setVisibility(View.GONE);
		holder.txtActivityTag_2.setVisibility(View.GONE);
		holder.iconNew.setVisibility(View.GONE);
		for (int i = 0;i < tags.size();i++) {

			if (tags.get(i).getTagType() == 1) {//type =1 数据为角标,否则就是标签

				holder.iconNew.setVisibility(View.VISIBLE);
				ImageUtils.noCacheDisplayImage(holder.iconNew ,tags.get(0).getTagImg());
			} else {
				String colorString = tags.get(i).getTagColor();
				if (tags.get(0).getTagType() == 1) { //有角标
					holder.mTextViewList.get(i-1).setVisibility(View.VISIBLE);
					if (status.equals("3")| status.equals("4")) {
						holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_gray);
					} else {
						if (colorString.equals("red")) {
							holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_red);
						}
						if (colorString.equals("orange")) {
							holder.mTextViewList.get(i-1).setBackgroundResource(R.drawable.icon_label_blue);
						}
					}
					holder.mTextViewList.get(i-1).setText(tags.get(i).getTagName());

				} else {//无角标
					holder.mTextViewList.get(i).setVisibility(View.VISIBLE);
					if (status.equals("3")| status.equals("4")) {
						holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_gray);
					} else {
						if (colorString.equals("red")) {
							holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_red);
						}
						if (colorString.equals("orange")) {
							holder.mTextViewList.get(i).setBackgroundResource(R.drawable.icon_label_blue);
						}
					}
					holder.mTextViewList.get(i).setText(tags.get(i).getTagName());
				}

			}
		}

		if (mPrivilegeCount>0&&position<mPrivilegeCount) {
			holder.txtChoicenessActivity.setVisibility(View.GONE);
			holder.sellOutMore.setVisibility(View.GONE);
		}

		if (mPrivilegeCount>0) {//有特权标
			//有活动标，活动标题显示
			if (mFinancialActivityCount>0) {

				if (position == mPrivilegeCount-1) {
					holder.txtChoicenessActivity.setVisibility(View.VISIBLE);
					holder.sellOutMore.setVisibility(View.GONE);
					holder.txtChoicenessActivity.setText(R.string.loan_activity_project);
				} else {
					if (mPrivilegeCount-1<position&&position<mPrivilegeCount+mFinancialActivityCount-1) {
						holder.txtChoicenessActivity.setVisibility(View.GONE);
						holder.sellOutMore.setVisibility(View.GONE);
					}
				}

			}
			//精选标题显示
			if (mChoicenessCount>0) {
				if (position == mPrivilegeCount+mFinancialActivityCount-1) {
					holder.txtChoicenessActivity.setVisibility(View.VISIBLE);
					holder.sellOutMore.setVisibility(View.GONE);
					holder.txtChoicenessActivity.setText(R.string.loan_choiceness_project);
				} else {
					if (mPrivilegeCount+mFinancialActivityCount-1 < position) {
						holder.txtChoicenessActivity.setVisibility(View.GONE);
						holder.sellOutMore.setVisibility(View.GONE);
					}
				}
			}else {
				if (mPrivilegeCount+mFinancialActivityCount-1 <= position) {
					holder.txtChoicenessActivity.setVisibility(View.GONE);
					holder.sellOutMore.setVisibility(View.GONE);
				}
			}

		} else {//无特权标

			//无特权标，有活动标，不显示活动标题
			if (mFinancialActivityCount>0) {

				if (position<mPrivilegeCount+mFinancialActivityCount) {
					holder.txtChoicenessActivity.setVisibility(View.GONE);
					holder.sellOutMore.setVisibility(View.GONE);
				}
				//精选标题显示
				if (mChoicenessCount>0) {
					if (position == mPrivilegeCount+mFinancialActivityCount-1) {
						holder.txtChoicenessActivity.setVisibility(View.VISIBLE);
						holder.sellOutMore.setVisibility(View.GONE);
						holder.txtChoicenessActivity.setText(R.string.loan_choiceness_project);
					} else {
						if (mPrivilegeCount+mFinancialActivityCount-1 < position) {
							holder.txtChoicenessActivity.setVisibility(View.GONE);
							holder.sellOutMore.setVisibility(View.GONE);
						}
					}
				}else {
					if (mPrivilegeCount+mFinancialActivityCount-1 <= position) {
						holder.txtChoicenessActivity.setVisibility(View.GONE);
						holder.sellOutMore.setVisibility(View.GONE);
					}
				}
			} else {
				//无特权标，无活动标，不显示精选标题
				holder.txtChoicenessActivity.setVisibility(View.GONE);
				holder.sellOutMore.setVisibility(View.GONE);

			}
		}

		//显示更多按钮
		if (position == mAllFinancialList.size()-mFinancialOutCount) {
			holder.sellOutMore.setVisibility(View.VISIBLE);
			holder.txtChoicenessActivity.setVisibility(View.GONE);
		}

		//更多
		holder.sellOutMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, UmengTouchType.RP102_4);
				Intent intent = new Intent(mContext,FinancialMoreActivity.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	private void setSaleRemind(int position,long financialId) {
		Map<String, Object> datas = new HashMap<String, Object>();

		if (mRegId == null) {
			mRegId = "";
		}
		datas.put("financialId", financialId);
		datas.put("regId",mRegId);

		DdApplication.getLoanManager().setSaleRemind(datas,position,mOnSetSaleRemindFinishedListener);
	}
	private LoanManager.OnSetSaleRemindFinishedListener mOnSetSaleRemindFinishedListener = new LoanManager.OnSetSaleRemindFinishedListener() {
		@Override
		public void onSetSaleRemindFinished(Response response,int position) {

			if (response.isSuccess()) {

				Remind remind = new Remind();
				remind.setRemindFlag(true);
				list.set(position,remind);
				mConfigManager.setRemindList(list);

				holder.txtRemind.setBackgroundResource(R.drawable.solid_noradius_blue_bg);
				holder.txtRemind.setText(R.string.loan_cancel_remind);
				holder.txtRemind.setTextColor(mContext.getResources().getColor(R.color.blue_selected));

			} else {
				AppUtils.handleErrorResponse(mContext, response);
			}
		}
	};

	private void cancelSaleRemind(int position,long financialId) {
		Map<String, Object> datas = new HashMap<String, Object>();

		datas.put("financialId", financialId);
		datas.put("regId",mRegId);
		DdApplication.getLoanManager().cancelSaleRemind(datas,position,mOnCancelSaleRemindFinishedListener);
	}
	private LoanManager.OnCancelSaleRemindFinishedListener mOnCancelSaleRemindFinishedListener = new LoanManager.OnCancelSaleRemindFinishedListener() {
		@Override
		public void OnCancelSaleRemindFinished(Response response,int position) {
			if (response.isSuccess()) {
				Remind remind = new Remind();
				remind.setRemindFlag(false);
				list.set(position,remind);
				mConfigManager.setRemindList(list);

				holder.txtRemind.setBackgroundResource(R.drawable.solid_noradius_red_bg);
				holder.txtRemind.setText(R.string.loan_open_remind);
				holder.txtRemind.setTextColor(mContext.getResources().getColor(R.color.main_red));
			} else {
				AppUtils.handleErrorResponse(mContext, response);
			}
		}
	};
	public class ViewHolder {
		public TextView txtName;
		public ImageView iconNew;
		public ProgressBar horizontalProgress;//进度条
		public TextView txtYearInterest;
		public TextView txtAddInterest;
		public TextView txtLoanPeriod;
		public TextView txtActivityTag;
		public TextView txtActivityTag_2;
		public TextView txtSurplusInvest;
		public RelativeLayout sellOutMore;//更多
		public TextView countDown;//倒计时
		public TextView txtUnit ;
		public TextView txtRemind ;//提醒
		public TextView txtChoicenessActivity ;
		public View layoutSurplusInvest ;
		public View layoutSellOut ;
		public List<TextView> mTextViewList ;
		public RelativeLayout layoutCountDown ;
	}


}