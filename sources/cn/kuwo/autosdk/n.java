package cn.kuwo.autosdk;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.Random;

public final class n {
    public static String a;
    public static String b;
    public static String c = "kwplayerhd";
    public static String d = "ar";
    public static String e = "1.2.0.0";
    public static String f = (String.valueOf(c) + "_" + d + "_" + e);
    public static String g = "";

    private static String a() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        int nextInt = random.nextInt(5);
        if (nextInt == 0) {
            nextInt = 1;
        }
        int i = nextInt * 10000;
        sb.append(i + random.nextInt(i));
        int nextInt2 = (random.nextInt(5) + 5) * 100000;
        sb.append(nextInt2 + random.nextInt(nextInt2));
        return sb.toString();
    }

    public static void a(Context context, String str) {
        String str2;
        g = String.valueOf(f) + "_" + str + ".apk";
        try {
            a = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            if (TextUtils.isEmpty(a)) {
                a = "";
            }
        } catch (Throwable th) {
            th.printStackTrace();
            a = "";
        }
        try {
            str2 = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Throwable th2) {
            th2.printStackTrace();
            str2 = "";
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = !TextUtils.isEmpty(a) ? String.valueOf(a.replace(":", "")) + "000" : a();
        }
        b = str2;
    }
}
