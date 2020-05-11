package com.touchus.benchilauncher.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.SystemProperties;
import com.touchus.benchilauncher.R;
import java.util.ArrayList;
import java.util.List;

public class Utiltools {
    public static void onlineOtaUpdate() {
        SystemProperties.set("ctl.start", "ota_update");
    }

    public static void resetSDCard() {
        SystemProperties.set("ctl.start", "mkfs");
    }

    public static void stopKernelMCU() {
        SystemProperties.set("ctl.stop", "kernel_mcu");
    }

    public static void updateLauncherAPK() {
        SystemProperties.set("ctl.start", "update_apk");
    }

    public static int getResId(Context context, String idName) {
        try {
            return context.getResources().getIdentifier(idName, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return R.drawable.sou10;
        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                pName.add(pinfo.get(i).packageName);
            }
        }
        return pName.contains(packageName);
    }
}
