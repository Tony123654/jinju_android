package com.jinju.android.builder;

import com.jinju.android.api.InvestRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Libra on 2017/11/6.
 */

public class InvestRecordListBuilder {
    public static InvestRecord build(JSONObject jsonObject) throws JSONException {
        InvestRecord mInvestRecord = new InvestRecord();
        mInvestRecord.setGmtCreate(jsonObject.optString("gmtCreate"));
        mInvestRecord.setMobile(jsonObject.optString("mobile"));
        mInvestRecord.setPayAmount(jsonObject.optString("payAmount"));
        mInvestRecord.setTopGmtCreate(jsonObject.optString("topGmtCreate"));
        mInvestRecord.setTopMobile(jsonObject.optString("topMobile"));
        mInvestRecord.setTopPayAmount(jsonObject.optString("topPayAmount"));
        return mInvestRecord;
    }
    public static List<InvestRecord> buildList(JSONArray jsonArray) throws JSONException {

        List<InvestRecord> mInvestRecordList = new ArrayList<InvestRecord>();
        if (jsonArray == null)
            return mInvestRecordList;

        for (int i = 0; i < jsonArray.length(); i++)
            mInvestRecordList.add(build(jsonArray.optJSONObject(i)));

        return mInvestRecordList;
    }
}
