package com.touchus.publicutils.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class APPSettings {
    public static boolean isNavgation(String str) {
        if (str.equals("com.google.android.apps.maps") || str.equals("com.nng.igo.primong.igoworld") || str.contains("com.tomtom.gplay.navapp") || str.contains("com.navigon.navigator_one") || str.contains("com.sygic.incar") || str.contains("com.waze") || str.contains("com.sygic") || str.contains("com.kingwaytek") || str.equals("com.papago.S1pioneer") || str.equals("com.papago.s1OBU") || str.equals("com.papago.M11_Int") || str.equals("com.nng.igo.primong.hun10th") || str.equals("cld.navi.mainframe") || str.contains("cld.navi.kgomap") || str.contains("com.tencent.map") || str.contains("ru.yandex.yandexnavi") || str.equals("com.sogou.map.android.sogounav") || str.equals("com.sogou.map.android.maps")) {
            return true;
        }
        return false;
    }

    public static boolean isAvalbe(Context context, String name) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        }
        return true;
    }
}
