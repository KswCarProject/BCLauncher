package cn.kuwo.autosdk;

import android.os.Handler;
import android.os.Looper;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.SearchMode;

public class h implements g {
    private volatile k a = null;
    private String b = "";
    private int c = 0;
    private Handler d = new i(Looper.getMainLooper());

    public void a(String str, SearchMode searchMode, OnSearchListener onSearchListener) {
        this.b = str;
        this.c = 0;
        if (this.a != null) {
            this.a.a = true;
            this.a = null;
        }
        this.a = new k(this.b, this.c, searchMode);
        this.a.a(this.d, onSearchListener);
        r.a(s.NET, this.a);
    }
}
