package cn.kuwo.autosdk;

public final class r {
    /* access modifiers changed from: private */
    public static volatile int a = 0;
    /* access modifiers changed from: private */
    public static t[] b = new t[5];

    public static void a(s sVar, Runnable runnable) {
        if (sVar == s.NET) {
        }
        c().a(runnable, 0);
    }

    private static t c() {
        if (a == 0) {
            return new t((t) null);
        }
        synchronized (b) {
            if (a == 0) {
                t tVar = new t((t) null);
                return tVar;
            }
            a--;
            t tVar2 = b[a];
            b[a] = null;
            return tVar2;
        }
    }
}
