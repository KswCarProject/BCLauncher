package cn.kuwo.autosdk;

class c implements Runnable {
    private static /* synthetic */ int[] d;
    final /* synthetic */ b a;
    private final /* synthetic */ e b;
    private final /* synthetic */ int c;

    c(b bVar, e eVar, int i) {
        this.a = bVar;
        this.b = eVar;
        this.c = i;
    }

    static /* synthetic */ int[] a() {
        int[] iArr = d;
        if (iArr == null) {
            iArr = new int[e.a().length];
            try {
                iArr[e.NOTIFY_FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[e.NOTIFY_FINISH.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[e.NOTIFY_START.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            d = iArr;
        }
        return iArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r5 = this;
            cn.kuwo.autosdk.b r1 = r5.a
            monitor-enter(r1)
            cn.kuwo.autosdk.b r0 = r5.a     // Catch:{ all -> 0x001c }
            boolean r0 = r0.p     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x000b
            monitor-exit(r1)     // Catch:{ all -> 0x001c }
        L_0x000a:
            return
        L_0x000b:
            int[] r0 = a()     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.e r2 = r5.b     // Catch:{ all -> 0x001c }
            int r2 = r2.ordinal()     // Catch:{ all -> 0x001c }
            r0 = r0[r2]     // Catch:{ all -> 0x001c }
            switch(r0) {
                case 1: goto L_0x001f;
                case 2: goto L_0x002f;
                case 3: goto L_0x003d;
                default: goto L_0x001a;
            }     // Catch:{ all -> 0x001c }
        L_0x001a:
            monitor-exit(r1)     // Catch:{ all -> 0x001c }
            goto L_0x000a
        L_0x001c:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x001c }
            throw r0
        L_0x001f:
            cn.kuwo.autosdk.b r0 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.f r0 = r0.x     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.b r2 = r5.a     // Catch:{ all -> 0x001c }
            int r3 = r5.c     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.b r4 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.a r4 = r4.z     // Catch:{ all -> 0x001c }
            r0.a(r2, r3, r4)     // Catch:{ all -> 0x001c }
            goto L_0x001a
        L_0x002f:
            cn.kuwo.autosdk.b r0 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.f r0 = r0.x     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.b r2 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.b r3 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.a r3 = r3.z     // Catch:{ all -> 0x001c }
            r0.b(r2, r3)     // Catch:{ all -> 0x001c }
            goto L_0x001a
        L_0x003d:
            cn.kuwo.autosdk.b r0 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.f r0 = r0.x     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.b r2 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.b r3 = r5.a     // Catch:{ all -> 0x001c }
            cn.kuwo.autosdk.a r3 = r3.z     // Catch:{ all -> 0x001c }
            r0.a(r2, r3)     // Catch:{ all -> 0x001c }
            goto L_0x001a
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.c.run():void");
    }
}
