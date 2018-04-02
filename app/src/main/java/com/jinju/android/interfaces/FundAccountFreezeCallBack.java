package com.jinju.android.interfaces;

import com.jinju.android.api.Category;

import java.util.List;

/**
 * Created by Libra on 2018/3/12.
 * 资金流水 其他
 */

public interface FundAccountFreezeCallBack {
    public void fundAccountFreezeValue(List<Category> mCodeList);
}
