package cn.kuwo.autosdk;

import android.text.TextUtils;
import cn.kuwo.autosdk.api.SearchMode;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class z {
    protected static String a = null;
    public static String b = "http://mobi.kuwo.cn/mobi.s?f=kuwo&q=";

    private static String a(String str) {
        StringBuilder a2 = a();
        a2.append(str);
        byte[] bytes = a2.toString().getBytes();
        byte[] a3 = p.a(bytes, bytes.length, p.a, p.b);
        return String.valueOf(b) + new String(m.a(a3, a3.length));
    }

    public static String a(String str, SearchMode searchMode, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("type=new_search");
        if (searchMode != null) {
            sb.append("&mode=all");
        }
        if (TextUtils.isEmpty(str)) {
            sb.append("&word=");
        } else {
            try {
                sb.append("&word=" + URLEncoder.encode(str, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (i >= 0) {
            sb.append("&pn=" + i);
        }
        if (i2 >= 0) {
            sb.append("&rn=" + i2);
        }
        return a(sb.toString());
    }

    public static synchronized StringBuilder a() {
        StringBuilder sb;
        synchronized (z.class) {
            sb = new StringBuilder();
            if (a == null) {
                sb.append("user=").append(n.b);
                sb.append("&prod=").append(n.f);
                sb.append("&corp=kuwo");
                sb.append("&source=").append(n.g);
                sb.append("&");
                a = sb.toString();
            } else {
                sb.append(a);
            }
        }
        return sb;
    }
}
