package com.jinju.android.api;

import java.io.Serializable;

/**
 * Created by Libra on 2017/8/17.
 */

public class Remind implements Serializable {
    public boolean remindFlag;
    private static final long serialVersionUID = 1L;
    public boolean getRemindFlag() {
        return remindFlag;
    }

    public void setRemindFlag(boolean remindFlag) {
        this.remindFlag = remindFlag;
    }
}
