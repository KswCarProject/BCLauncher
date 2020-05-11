package cn.kuwo.autosdk;

final class t extends Thread {
    private volatile Runnable a;
    private volatile int b;
    private volatile boolean c;

    private t() {
    }

    /* synthetic */ t(t tVar) {
        this();
    }

    public void a(Runnable runnable, int i) {
        this.a = runnable;
        this.b = i;
        if (!this.c) {
            start();
            this.c = true;
            return;
        }
        synchronized (this) {
            notify();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        wait();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r4 = this;
            r3 = 5
        L_0x0001:
            int r0 = r4.b
            android.os.Process.setThreadPriority(r0)
            java.lang.Runnable r0 = r4.a
            r0.run()
            int r0 = cn.kuwo.autosdk.r.a
            if (r0 < r3) goto L_0x0015
        L_0x0011:
            r0 = 0
            r4.c = r0
            return
        L_0x0015:
            monitor-enter(r4)
            cn.kuwo.autosdk.t[] r1 = cn.kuwo.autosdk.r.b     // Catch:{ all -> 0x0024 }
            monitor-enter(r1)     // Catch:{ all -> 0x0024 }
            int r0 = cn.kuwo.autosdk.r.a     // Catch:{ all -> 0x0040 }
            if (r0 < r3) goto L_0x0027
            monitor-exit(r1)     // Catch:{ all -> 0x0040 }
            monitor-exit(r4)     // Catch:{ all -> 0x0024 }
            goto L_0x0011
        L_0x0024:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0024 }
            throw r0
        L_0x0027:
            cn.kuwo.autosdk.t[] r0 = cn.kuwo.autosdk.r.b     // Catch:{ all -> 0x0040 }
            int r2 = cn.kuwo.autosdk.r.a     // Catch:{ all -> 0x0040 }
            r0[r2] = r4     // Catch:{ all -> 0x0040 }
            int r0 = cn.kuwo.autosdk.r.a     // Catch:{ all -> 0x0040 }
            int r0 = r0 + 1
            cn.kuwo.autosdk.r.a = r0     // Catch:{ all -> 0x0040 }
            monitor-exit(r1)     // Catch:{ all -> 0x0040 }
            r4.wait()     // Catch:{ InterruptedException -> 0x0043 }
            monitor-exit(r4)     // Catch:{ all -> 0x0024 }
            goto L_0x0001
        L_0x0040:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0040 }
            throw r0     // Catch:{ all -> 0x0024 }
        L_0x0043:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0024 }
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.t.run():void");
    }
}
