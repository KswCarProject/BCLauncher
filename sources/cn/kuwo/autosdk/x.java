package cn.kuwo.autosdk;

import android.text.TextUtils;
import java.util.ArrayList;

public class x {
    private static y a = null;
    private static final String[] b = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        int length = str.length();
        while (true) {
            length--;
            if (length < 0) {
                return true;
            }
            if (!Character.isDigit(str.charAt(length)) && (length != 0 || str.charAt(length) != '-')) {
                return false;
            }
        }
    }

    public static boolean a(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return str.equals(str2);
    }

    public static String[] a(String str, char c) {
        return a(str, c, false);
    }

    private static String[] a(String str, char c, boolean z) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return new String[0];
        }
        ArrayList arrayList = new ArrayList();
        boolean z2 = false;
        boolean z3 = false;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            if (str.charAt(i2) == c) {
                if (z3 || z) {
                    arrayList.add(str.substring(i, i2));
                    z2 = true;
                }
                i = i2 + 1;
                i2 = i;
            } else {
                z3 = true;
                i2++;
                z2 = false;
            }
        }
        if (z3 || (z && z2)) {
            arrayList.add(str.substring(i, i2));
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public static String b(String str) {
        String[] split = str.split(":");
        return split.length == 2 ? split[1] : "";
    }

    public static String c(String str) {
        return str == null ? "" : str;
    }
}
