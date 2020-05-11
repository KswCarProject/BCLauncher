package com.touchus.publicutils.utils;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class UtilTools {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 >= timeD || timeD >= 500) {
            lastClickTime = time;
            return false;
        }
        lastClickTime = time;
        return true;
    }

    public static void sendKeyeventToSystem(int keyeventCode) {
        if (keyeventCode >= 0) {
            final int keycode = keyeventCode;
            new Thread(new Runnable() {
                public void run() {
                    new Instrumentation().sendKeyDownUpSync(keycode);
                }
            }).start();
        }
    }

    public static boolean isLetterDigitOrChinese(String str) {
        return str.matches("^[a-z0-9A-Z.]+$");
    }

    public static UUID getMyUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        UUID deviceUuid = new UUID((long) (Settings.Secure.getString(context.getContentResolver(), "android_id")).hashCode(), (((long) (tm.getDeviceId()).hashCode()) << 32) | ((long) (tm.getSimSerialNumber()).hashCode()));
        Log.d("debug", "uuid=" + deviceUuid.toString());
        return deviceUuid;
    }

    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        List<ActivityManager.RunningServiceInfo> myList = ((ActivityManager) mContext.getSystemService("activity")).getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        int i = 0;
        while (true) {
            if (i >= myList.size()) {
                break;
            } else if (myList.get(i).service.getClassName().toString().equals(serviceName)) {
                isWork = true;
                break;
            } else {
                i++;
            }
        }
        return isWork;
    }

    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        return TextUtils.isEmpty(imei) ? "" : imei;
    }

    public static boolean checkUSBExist() {
        if (calculateTotalSizeInMB(getStatFs("/mnt/usbotg")) > 0) {
            return true;
        }
        return false;
    }

    public static boolean checkTFCarkExist() {
        if (calculateTotalSizeInMB(getStatFs("storage/sdcard1")) > 0) {
            return true;
        }
        return false;
    }

    private static StatFs getStatFs(String path) {
        try {
            return new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static long calculateTotalSizeInMB(StatFs stat) {
        if (stat != null) {
            return ((long) stat.getBlockSize()) * ((long) stat.getBlockCount());
        }
        return 0;
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo mNetworkInfo;
        if (context == null || (mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return false;
        }
        return mNetworkInfo.isAvailable();
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> list = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (list == null || list.size() <= 0 || !className.equals(list.get(0).topActivity.getClassName())) {
            return false;
        }
        return true;
    }

    public static boolean stringFilter(String str) {
        try {
            Pattern p = Pattern.compile("^[一-龥A-Za-z0-9_]+$");
            for (int i = str.length() - 1; i >= 0; i--) {
                if (!p.matcher(String.valueOf(str.charAt(i))).matches()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getPackage(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean checkURL(Context context, String url) {
        boolean value = false;
        if (!isNetworkConnected(context)) {
            return false;
        }
        try {
            int code = ((HttpURLConnection) new URL(url).openConnection()).getResponseCode();
            System.out.println(">>>>>>>>>>>>>>>> " + code + " <<<<<<<<<<<<<<<<<<");
            value = code == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return value;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x005d A[SYNTHETIC, Splitter:B:20:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006f A[SYNTHETIC, Splitter:B:26:0x006f] */
    /* JADX WARNING: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean echoFile(java.lang.String r8, java.lang.String r9) {
        /*
            r4 = 0
            java.lang.String r5 = "driverlog"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "echoFile: "
            r6.<init>(r7)
            java.lang.StringBuilder r6 = r6.append(r8)
            java.lang.String r7 = " path: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r9)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
            r2 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0053 }
            r1.<init>(r9)     // Catch:{ Exception -> 0x0053 }
            boolean r5 = r1.exists()     // Catch:{ Exception -> 0x0053 }
            if (r5 == 0) goto L_0x007e
            boolean r5 = r1.canWrite()     // Catch:{ Exception -> 0x0053 }
            if (r5 == 0) goto L_0x007e
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0053 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0053 }
            byte[] r5 = r8.getBytes()     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            r3.write(r5)     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            r3.flush()     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            if (r3 == 0) goto L_0x0045
            r3.close()     // Catch:{ Exception -> 0x0048 }
        L_0x0045:
            r4 = 1
            r2 = r3
        L_0x0047:
            return r4
        L_0x0048:
            r0 = move-exception
            java.lang.String r4 = "driverlog"
            java.lang.String r5 = r0.getMessage()
            android.util.Log.d(r4, r5)
            goto L_0x0045
        L_0x0053:
            r0 = move-exception
        L_0x0054:
            java.lang.String r5 = "driverlog"
            java.lang.String r6 = "echofile err----------"
            android.util.Log.d(r5, r6)     // Catch:{ all -> 0x006c }
            if (r2 == 0) goto L_0x0047
            r2.close()     // Catch:{ Exception -> 0x0061 }
            goto L_0x0047
        L_0x0061:
            r0 = move-exception
            java.lang.String r5 = "driverlog"
            java.lang.String r6 = r0.getMessage()
            android.util.Log.d(r5, r6)
            goto L_0x0047
        L_0x006c:
            r4 = move-exception
        L_0x006d:
            if (r2 == 0) goto L_0x0072
            r2.close()     // Catch:{ Exception -> 0x0073 }
        L_0x0072:
            throw r4
        L_0x0073:
            r0 = move-exception
            java.lang.String r5 = "driverlog"
            java.lang.String r6 = r0.getMessage()
            android.util.Log.d(r5, r6)
            goto L_0x0072
        L_0x007e:
            if (r2 == 0) goto L_0x0047
            r2.close()     // Catch:{ Exception -> 0x0084 }
            goto L_0x0047
        L_0x0084:
            r0 = move-exception
            java.lang.String r5 = "driverlog"
            java.lang.String r6 = r0.getMessage()
            android.util.Log.d(r5, r6)
            goto L_0x0047
        L_0x008f:
            r4 = move-exception
            r2 = r3
            goto L_0x006d
        L_0x0092:
            r0 = move-exception
            r2 = r3
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: com.touchus.publicutils.utils.UtilTools.echoFile(java.lang.String, java.lang.String):boolean");
    }
}
