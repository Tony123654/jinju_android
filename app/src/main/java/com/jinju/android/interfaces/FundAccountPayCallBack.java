package com.jinju.android.interfaces;

import com.jinju.android.api.Category;

import java.util.List;

/**
 * Created by Libra on 2018/3/12.
 * 资金流水 支出
 */

public interface FundAccountPayCallBack {
    public void fundAccountPayValue(List<Category> mCodeList);
}
