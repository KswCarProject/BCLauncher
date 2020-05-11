package cn.kuwo.autosdk;

import android.os.Handler;
import android.text.TextUtils;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.SearchMode;
import cn.kuwo.autosdk.api.SearchStatus;
import cn.kuwo.autosdk.bean.Constants;
import java.util.List;

public final class k implements Runnable {
    public volatile boolean a = false;
    private String b = null;
    private int c = 0;
    /* access modifiers changed from: private */
    public OnSearchListener d = null;
    private SearchMode e = SearchMode.ALL;
    private Handler f;

    public k(String str, int i, SearchMode searchMode) {
        this.e = searchMode;
        this.b = str;
        this.c = i;
    }

    private void a() {
        boolean z = true;
        boolean z2 = false;
        j jVar = new j(this.e);
        if (!this.a) {
            int i = 3;
            int[] iArr = new int[1];
            boolean z3 = false;
            List list = null;
            while (list == null) {
                int i2 = i - 1;
                if (i == 0) {
                    break;
                }
                long currentTimeMillis = System.currentTimeMillis();
                List a2 = jVar.a(this.b, this.c, iArr);
                boolean z4 = System.currentTimeMillis() - currentTimeMillis > Constants.SEARCH_TIMEOUT;
                if (!this.a) {
                    list = a2;
                    z3 = z4;
                    i = i2;
                } else {
                    return;
                }
            }
            if (this.a) {
                return;
            }
            if (list == null) {
                if (iArr[0] == 1) {
                    if (this.d != null) {
                        SearchStatus searchStatus = SearchStatus.FAILED;
                        if (this.c == 0) {
                            z2 = true;
                        }
                        a(searchStatus, z2, (List) null, true);
                    }
                } else if (this.d != null) {
                    SearchStatus searchStatus2 = SearchStatus.FAILED;
                    if (this.c != 0) {
                        z = false;
                    }
                    a(searchStatus2, z, (List) null, z3);
                }
            } else if (this.d != null) {
                SearchStatus searchStatus3 = SearchStatus.SUCCESS;
                if (this.c != 0) {
                    z = false;
                }
                a(searchStatus3, z, list, false);
            }
        }
    }

    private void a(SearchStatus searchStatus, boolean z, List list, boolean z2) {
        if (this.f != null) {
            this.f.post(new l(this, searchStatus, z, list, z2));
        }
    }

    public void a(Handler handler, OnSearchListener onSearchListener) {
        this.f = handler;
        this.d = onSearchListener;
    }

    public void run() {
        if (this.a || !TextUtils.isEmpty(this.b)) {
            if (!this.a) {
                a();
            }
        } else if (this.d != null) {
            a(SearchStatus.FAILED, this.c == 0, (List) null, false);
        }
    }
}
