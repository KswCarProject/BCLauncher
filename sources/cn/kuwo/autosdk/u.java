package cn.kuwo.autosdk;

import android.os.Build;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class u {
    private static final String a;

    static {
        if (Build.VERSION.SDK_INT >= 17) {
            a = "u\\d+_a\\d+";
        } else {
            a = "app_\\d+";
        }
    }

    public static List a() {
        ArrayList arrayList = new ArrayList();
        for (String str : a("toolbox ps -p -P -x -c")) {
            try {
                arrayList.add(new v(str, (v) null));
            } catch (Exception e) {
                Log.d("ProcessManager", "Failed parsing line " + str);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:9:0x0028=Splitter:B:9:0x0028, B:34:0x0069=Splitter:B:34:0x0069} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List a(java.lang.String r5) {
        /*
            r1 = 0
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.lang.Runtime r0 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x0046 }
            java.lang.Process r1 = r0.exec(r5)     // Catch:{ Exception -> 0x0046 }
            java.io.InputStream r3 = r1.getInputStream()     // Catch:{ Exception -> 0x0046 }
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0046 }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0046 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x0046 }
            r0.<init>(r4)     // Catch:{ Exception -> 0x0046 }
        L_0x001c:
            java.lang.String r4 = r0.readLine()     // Catch:{ IOException -> 0x0039 }
            if (r4 != 0) goto L_0x0031
            r0.close()     // Catch:{ IOException -> 0x0039 }
            r3.close()     // Catch:{ IOException -> 0x0076 }
        L_0x0028:
            r1.waitFor()     // Catch:{ Exception -> 0x0046 }
            if (r1 == 0) goto L_0x0030
            r1.destroy()
        L_0x0030:
            return r2
        L_0x0031:
            java.lang.String r4 = r4.trim()     // Catch:{ IOException -> 0x0039 }
            r2.add(r4)     // Catch:{ IOException -> 0x0039 }
            goto L_0x001c
        L_0x0039:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0065 }
            r3.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0028
        L_0x0041:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x0046 }
            goto L_0x0028
        L_0x0046:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x006a }
            java.io.InputStream r3 = r1.getErrorStream()     // Catch:{ IOException -> 0x007b }
            r3.close()     // Catch:{ IOException -> 0x007b }
            java.io.InputStream r3 = r1.getInputStream()     // Catch:{ IOException -> 0x007b }
            r3.close()     // Catch:{ IOException -> 0x007b }
            java.io.OutputStream r3 = r1.getOutputStream()     // Catch:{ IOException -> 0x007b }
            r3.close()     // Catch:{ IOException -> 0x007b }
        L_0x005f:
            if (r1 == 0) goto L_0x0030
            r1.destroy()
            goto L_0x0030
        L_0x0065:
            r0 = move-exception
            r3.close()     // Catch:{ IOException -> 0x0071 }
        L_0x0069:
            throw r0     // Catch:{ Exception -> 0x0046 }
        L_0x006a:
            r0 = move-exception
            if (r1 == 0) goto L_0x0070
            r1.destroy()
        L_0x0070:
            throw r0
        L_0x0071:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ Exception -> 0x0046 }
            goto L_0x0069
        L_0x0076:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x0046 }
            goto L_0x0028
        L_0x007b:
            r3 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x006a }
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.u.a(java.lang.String):java.util.List");
    }
}
