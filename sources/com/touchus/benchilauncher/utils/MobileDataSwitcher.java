package com.touchus.benchilauncher.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class MobileDataSwitcher {
    public static void setMobileData(Context pContext, boolean pBoolean) {
        ((ConnectivityManager) pContext.getSystemService("connectivity")).setMobileDataEnabled(pBoolean);
    }

    public static boolean getMobileDataState(Context pContext) {
        try {
            return Boolean.valueOf(((ConnectivityManager) pContext.getSystemService("connectivity")).getMobileDataEnabled()).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }
}
