package cn.kuwo.autosdk;

import cn.kuwo.autosdk.api.SearchStatus;
import java.util.List;

class l implements Runnable {
    final /* synthetic */ k a;
    private final /* synthetic */ SearchStatus b;
    private final /* synthetic */ boolean c;
    private final /* synthetic */ List d;
    private final /* synthetic */ boolean e;

    l(k kVar, SearchStatus searchStatus, boolean z, List list, boolean z2) {
        this.a = kVar;
        this.b = searchStatus;
        this.c = z;
        this.d = list;
        this.e = z2;
    }

    public void run() {
        this.a.d.searchFinshed(this.b, this.c, this.d, this.e);
    }
}
