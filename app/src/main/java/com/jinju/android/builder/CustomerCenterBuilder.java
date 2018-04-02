package com.jinju.android.builder;

import com.jinju.android.api.CustomerCenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Libra on 2017/12/20.
 */

public class CustomerCenterBuilder {

    public static CustomerCenter build(JSONObject jsonObject) throws JSONException {
        CustomerCenter customerCenter = new CustomerCenter();
        customerCenter.setCouWithInst(jsonObject.optString("couWithInst"));
        customerCenter.setLogWithReg(jsonObject.optString("logWithReg"));
        customerCenter.setPayWithDraw(jsonObject.optString("payWithDraw"));
        customerCenter.setProduct(jsonObject.optString("product"));
        customerCenter.setSendWithCollect(jsonObject.optString("sendWithCollect"));
        return customerCenter;
    }
}
