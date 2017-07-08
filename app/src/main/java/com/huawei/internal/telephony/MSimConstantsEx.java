package com.huawei.internal.telephony;

import com.huawei.android.telephony.IIccPhoneBookManagerEx;

public class MSimConstantsEx {
    public static final int INVALID_SUBSCRIPTION = -1;
    public static final int MAX_PHONE_COUNT_DUAL_SIM = 2;
    public static final int MAX_PHONE_COUNT_SINGLE_SIM = 1;
    public static final int MAX_PHONE_COUNT_TRI_SIM = 3;
    public static final int SUB3 = 2;

    public static final String get_SUBSCRIPTION_KEY() {
        return IIccPhoneBookManagerEx.SLOT_ID;
    }

    public static final int get_SUB1() {
        return 0;
    }

    public static final int get_SUB2() {
        return MAX_PHONE_COUNT_SINGLE_SIM;
    }
}
