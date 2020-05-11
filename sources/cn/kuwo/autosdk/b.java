package cn.kuwo.autosdk;

import android.os.Handler;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class b implements Runnable {
    static String a = "HttpSession";
    static long b = Long.MAX_VALUE;
    static final AtomicLong c = new AtomicLong();
    static Proxy d = Proxy.NO_PROXY;
    public static int e = 8192;
    d f = new d(this);
    String g = "";
    Map h = new HashMap();
    boolean i;
    byte[] j;
    String k;
    int l;
    long m;
    boolean n = true;
    Proxy o;
    volatile boolean p;
    volatile boolean q;
    int r;
    long s = Thread.currentThread().getId();
    HttpURLConnection t;
    InputStream u;
    OutputStream v;
    ByteArrayOutputStream w;
    f x;
    Handler y = null;
    a z = new a();

    public b() {
        a("Accept", "*/*");
        a("Connection", "Close");
    }

    private void a(Handler handler, Runnable runnable) {
        if (handler != null) {
            if (handler.getLooper().getThread().getId() == Thread.currentThread().getId()) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        }
    }

    public a a(String str) {
        this.g = str;
        return d();
    }

    /* access modifiers changed from: package-private */
    public void a(int i2, int i3, byte[] bArr, int i4) {
        if (this.x != null && this.y != null) {
            a(this.y, (Runnable) this.f.a(i2, i3, bArr, i4));
        }
    }

    public void a(long j2) {
        this.m = j2;
    }

    /* access modifiers changed from: package-private */
    public void a(e eVar, int i2) {
        if (this.x != null && this.y != null) {
            a(this.y, (Runnable) new c(this, eVar, i2));
        }
    }

    public void a(String str, String str2) {
        this.h.put(str, str2);
    }

    /* access modifiers changed from: package-private */
    public boolean a() {
        if (TextUtils.isEmpty(this.g) || this.g.length() > e) {
            return false;
        }
        if ((this.i && this.j == null) || Thread.currentThread().getId() != this.s) {
            return false;
        }
        this.r++;
        return 1 == this.r;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0022 A[SYNTHETIC, Splitter:B:15:0x0022] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x002b A[SYNTHETIC, Splitter:B:20:0x002b] */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(byte[] r6, int r7, int r8) {
        /*
            r5 = this;
            r0 = 0
            java.lang.String r1 = r5.k
            if (r1 == 0) goto L_0x001c
            r2 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x001e, all -> 0x0028 }
            java.lang.String r3 = r5.k     // Catch:{ IOException -> 0x001e, all -> 0x0028 }
            java.lang.String r4 = "rw"
            r1.<init>(r3, r4)     // Catch:{ IOException -> 0x001e, all -> 0x0028 }
            long r2 = (long) r8
            r1.seek(r2)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            r2 = 0
            r1.write(r6, r2, r7)     // Catch:{ IOException -> 0x0036, all -> 0x0033 }
            if (r1 == 0) goto L_0x001c
            r1.close()     // Catch:{ IOException -> 0x0031 }
        L_0x001c:
            r0 = 1
        L_0x001d:
            return r0
        L_0x001e:
            r1 = move-exception
            r1 = r2
        L_0x0020:
            if (r1 == 0) goto L_0x001d
            r1.close()     // Catch:{ IOException -> 0x0026 }
            goto L_0x001d
        L_0x0026:
            r1 = move-exception
            goto L_0x001d
        L_0x0028:
            r0 = move-exception
        L_0x0029:
            if (r2 == 0) goto L_0x002e
            r2.close()     // Catch:{ IOException -> 0x002f }
        L_0x002e:
            throw r0
        L_0x002f:
            r1 = move-exception
            goto L_0x002e
        L_0x0031:
            r0 = move-exception
            goto L_0x001c
        L_0x0033:
            r0 = move-exception
            r2 = r1
            goto L_0x0029
        L_0x0036:
            r2 = move-exception
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.b.a(byte[], int, int):boolean");
    }

    /* access modifiers changed from: package-private */
    public boolean b() {
        String str = this.g;
        if (!this.g.toUpperCase().startsWith("HTTP")) {
            str = "http://" + this.g;
        }
        try {
            try {
                this.t = (HttpURLConnection) new URL(str).openConnection(this.o == null ? d : this.o);
                this.t.setInstanceFollowRedirects(true);
                try {
                    if (this.h != null) {
                        for (String str2 : this.h.keySet()) {
                            this.t.setRequestProperty(str2, (String) this.h.get(str2));
                        }
                    }
                    if (((int) this.m) != 0) {
                        if (((int) this.m) > 0) {
                            this.t.setConnectTimeout((int) this.m);
                        } else {
                            this.z.g = "connect timeout";
                            return false;
                        }
                    }
                    if (!this.i) {
                        try {
                            this.t.connect();
                            this.z.m = System.currentTimeMillis() - this.z.e;
                        } catch (IOException e2) {
                            this.z.g = "connect failed";
                            return false;
                        }
                    } else if (this.j != null) {
                        this.t.setDoOutput(true);
                        this.t.setDoInput(true);
                        try {
                            this.v = new BufferedOutputStream(this.t.getOutputStream());
                            this.v.write(this.j);
                            this.v.flush();
                        } catch (IOException e3) {
                            this.z.g = "post write failed";
                            return false;
                        }
                    }
                    return true;
                } catch (Exception e4) {
                    if (!this.p) {
                        this.z.g = EnvironmentCompat.MEDIA_UNKNOWN;
                    }
                    return false;
                }
            } catch (IOException e5) {
                e5.printStackTrace();
                this.z.g = "connect error";
                return false;
            }
        } catch (MalformedURLException e6) {
            e6.printStackTrace();
            this.z.g = "url error";
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void c() {
        try {
            if (this.w != null) {
                this.w.close();
            }
        } catch (IOException e2) {
        }
        try {
            if (this.v != null) {
                this.v.close();
            }
        } catch (IOException e3) {
        }
        try {
            if (this.u != null) {
                this.u.close();
            }
        } catch (IOException e4) {
        }
        try {
            if (this.t != null) {
                this.t.disconnect();
            }
        } catch (Throwable th) {
            this.t = null;
        }
        return;
    }

    /* access modifiers changed from: package-private */
    public a d() {
        if (!a()) {
            this.z.b = -1;
            return this.z;
        }
        this.z.k = this.g;
        if (this.p) {
            this.z.g = "user cancel";
            this.z.b = -3;
            return this.z;
        } else if (this.n && c.get() > b) {
            this.z.g = "flow limit";
            this.z.b = -4;
            return this.z;
        } else if (!b()) {
            return this.z;
        } else {
            if (this.p) {
                this.z.g = "user cancel";
                this.z.b = -3;
                return this.z;
            }
            try {
                this.z.b = this.t.getResponseCode();
                if (this.z.b == 200 || this.z.b == 201 || this.z.b == 206) {
                    this.u = new BufferedInputStream(this.t.getInputStream());
                    this.w = new ByteArrayOutputStream();
                    try {
                        byte[] bArr = new byte[4096];
                        if (((int) this.m) > 0) {
                            this.t.setReadTimeout((int) this.m);
                        }
                        while (true) {
                            int read = this.u.read(bArr, 0, 4096);
                            if (read > 0 && !this.p) {
                                if (this.n) {
                                    c.set(c.get() + ((long) read));
                                }
                                try {
                                    this.w.write(bArr, 0, read);
                                } catch (OutOfMemoryError e2) {
                                    this.z.b = -5;
                                    this.z.g = "write data failed";
                                    return this.z;
                                }
                            }
                        }
                        if (this.p) {
                            this.z.g = "user cancel";
                            this.z.b = -3;
                            a aVar = this.z;
                            this.z.f = System.currentTimeMillis() - this.z.d;
                            this.q = true;
                            c();
                            return aVar;
                        }
                        this.z.n = (System.currentTimeMillis() - this.z.e) - this.z.m;
                        try {
                            this.z.c = this.w.toByteArray();
                            this.z.a = true;
                            this.z.f = System.currentTimeMillis() - this.z.d;
                            this.q = true;
                            c();
                            return this.z;
                        } catch (OutOfMemoryError e3) {
                            this.z.b = -5;
                            this.z.g = "OutOfMemoryError";
                            a aVar2 = this.z;
                            this.z.f = System.currentTimeMillis() - this.z.d;
                            this.q = true;
                            c();
                            return aVar2;
                        }
                    } catch (OutOfMemoryError e4) {
                        this.z.b = -5;
                        this.z.g = "OutOfMemoryError";
                        a aVar3 = this.z;
                        this.z.f = System.currentTimeMillis() - this.z.d;
                        this.q = true;
                        c();
                        return aVar3;
                    }
                } else {
                    this.z.g = "resqonse code error ";
                    a aVar4 = this.z;
                    this.z.f = System.currentTimeMillis() - this.z.d;
                    this.q = true;
                    c();
                    return aVar4;
                }
            } catch (IOException e5) {
                this.z.b = -1;
                this.z.g = "read data failed";
            } catch (Exception e6) {
                this.z.b = -1;
                this.z.g = EnvironmentCompat.MEDIA_UNKNOWN;
            } finally {
                this.z.f = System.currentTimeMillis() - this.z.d;
                this.q = true;
                c();
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006d A[SYNTHETIC, Splitter:B:26:0x006d] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0075 A[SYNTHETIC, Splitter:B:31:0x0075] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a0 A[SYNTHETIC, Splitter:B:49:0x00a0] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00a9 A[SYNTHETIC, Splitter:B:54:0x00a9] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean e() {
        /*
            r8 = this;
            r3 = 0
            r0 = 1
            r1 = 0
            java.lang.String r2 = r8.k
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x000c
        L_0x000b:
            return r0
        L_0x000c:
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x0063 }
            java.lang.String r4 = r8.k     // Catch:{ IOException -> 0x0063 }
            r2.<init>(r4)     // Catch:{ IOException -> 0x0063 }
            boolean r4 = r2.exists()     // Catch:{ IOException -> 0x0063 }
            if (r4 != 0) goto L_0x001c
            r2.createNewFile()     // Catch:{ IOException -> 0x0063 }
        L_0x001c:
            long r4 = r2.length()
            int r6 = r8.l
            long r6 = (long) r6
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 <= 0) goto L_0x0079
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x0066, all -> 0x0072 }
            java.lang.String r4 = r8.k     // Catch:{ IOException -> 0x0066, all -> 0x0072 }
            java.lang.String r5 = "rw"
            r2.<init>(r4, r5)     // Catch:{ IOException -> 0x0066, all -> 0x0072 }
            int r3 = r8.l     // Catch:{ IOException -> 0x00e4 }
            long r4 = (long) r3     // Catch:{ IOException -> 0x00e4 }
            r2.setLength(r4)     // Catch:{ IOException -> 0x00e4 }
            if (r2 == 0) goto L_0x003b
            r2.close()     // Catch:{ IOException -> 0x00d2 }
        L_0x003b:
            int r1 = r8.l
            if (r1 <= 0) goto L_0x005b
            java.lang.String r1 = "Range"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "bytes="
            r2.<init>(r3)
            int r3 = r8.l
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "-"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r8.a((java.lang.String) r1, (java.lang.String) r2)
        L_0x005b:
            java.lang.String r1 = "Accept-Encoding"
            java.lang.String r2 = "identity"
            r8.a((java.lang.String) r1, (java.lang.String) r2)
            goto L_0x000b
        L_0x0063:
            r0 = move-exception
            r0 = r1
            goto L_0x000b
        L_0x0066:
            r0 = move-exception
            r2 = r3
        L_0x0068:
            r0.printStackTrace()     // Catch:{ all -> 0x00e1 }
            if (r2 == 0) goto L_0x0070
            r2.close()     // Catch:{ IOException -> 0x00ce }
        L_0x0070:
            r0 = r1
            goto L_0x000b
        L_0x0072:
            r0 = move-exception
        L_0x0073:
            if (r3 == 0) goto L_0x0078
            r3.close()     // Catch:{ IOException -> 0x00d0 }
        L_0x0078:
            throw r0
        L_0x0079:
            long r4 = r2.length()
            int r2 = r8.l
            long r6 = (long) r2
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 >= 0) goto L_0x00ad
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x009a }
            java.lang.String r4 = r8.k     // Catch:{ IOException -> 0x009a }
            java.lang.String r5 = "rw"
            r2.<init>(r4, r5)     // Catch:{ IOException -> 0x009a }
            r4 = 0
            r2.setLength(r4)     // Catch:{ IOException -> 0x00de, all -> 0x00db }
            if (r2 == 0) goto L_0x0097
            r2.close()     // Catch:{ IOException -> 0x00d9 }
        L_0x0097:
            r8.l = r1
            goto L_0x005b
        L_0x009a:
            r0 = move-exception
        L_0x009b:
            r0.printStackTrace()     // Catch:{ all -> 0x00a6 }
            if (r3 == 0) goto L_0x00a3
            r3.close()     // Catch:{ IOException -> 0x00d5 }
        L_0x00a3:
            r0 = r1
            goto L_0x000b
        L_0x00a6:
            r0 = move-exception
        L_0x00a7:
            if (r3 == 0) goto L_0x00ac
            r3.close()     // Catch:{ IOException -> 0x00d7 }
        L_0x00ac:
            throw r0
        L_0x00ad:
            int r1 = r8.l
            if (r1 <= 0) goto L_0x005b
            java.lang.String r1 = "Range"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "bytes="
            r2.<init>(r3)
            int r3 = r8.l
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r3 = "-"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r8.a((java.lang.String) r1, (java.lang.String) r2)
            goto L_0x005b
        L_0x00ce:
            r0 = move-exception
            goto L_0x0070
        L_0x00d0:
            r1 = move-exception
            goto L_0x0078
        L_0x00d2:
            r1 = move-exception
            goto L_0x003b
        L_0x00d5:
            r0 = move-exception
            goto L_0x00a3
        L_0x00d7:
            r1 = move-exception
            goto L_0x00ac
        L_0x00d9:
            r2 = move-exception
            goto L_0x0097
        L_0x00db:
            r0 = move-exception
            r3 = r2
            goto L_0x00a7
        L_0x00de:
            r0 = move-exception
            r3 = r2
            goto L_0x009b
        L_0x00e1:
            r0 = move-exception
            r3 = r2
            goto L_0x0073
        L_0x00e4:
            r0 = move-exception
            goto L_0x0068
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.b.e():boolean");
    }

    /* access modifiers changed from: package-private */
    public int f() {
        try {
            String host = this.t.getURL().getHost();
            this.z.b = this.t.getResponseCode();
            if (!this.t.getURL().getHost().equalsIgnoreCase(host)) {
                this.z.l = this.t.getURL().toString();
            }
            if (this.z.b == 200 || this.z.b == 201 || this.z.b == 206) {
                int contentLength = this.t.getContentLength();
                if (!(this.h == null ? false : "identity".equals(this.h.get("Accept-Encoding")))) {
                    return -1;
                }
                return contentLength;
            }
            this.z.g = "response code error" + this.z.b;
            return -2;
        } catch (IOException e2) {
            this.z.g = "get response code exception";
            return -2;
        } catch (Exception e3) {
            if (!this.p) {
                this.z.g = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            return -2;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean g() {
        try {
            if (((int) this.m) > 0) {
                this.t.setReadTimeout((int) this.m);
            }
            return true;
        } catch (Exception e2) {
            if (!this.p) {
                this.z.g = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public void h() {
        c();
        this.q = true;
        this.z.f = System.currentTimeMillis() - this.z.d;
        if (this.z.a()) {
            a(e.NOTIFY_FINISH, 0);
        } else if (!this.p) {
            a(e.NOTIFY_FAILED, 0);
        }
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 202 */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0116, code lost:
        if (r11.p == false) goto L_0x0191;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0118, code lost:
        r11.z.g = "user cancel";
        h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0125, code lost:
        if (r11.n == false) goto L_0x0134;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0127, code lost:
        c.set(c.get() + ((long) r3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0136, code lost:
        if (r11.k != null) goto L_0x013e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
        r11.w.write(r2, 0, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0140, code lost:
        if (r11.k == null) goto L_0x016c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0153, code lost:
        if (((long) (r11.l + r0)) == new java.io.File(r11.k).length()) goto L_0x016c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0155, code lost:
        r11.z.g = "io error (check file length error)";
        h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0161, code lost:
        r11.z.g = "out of memory error";
        h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0173, code lost:
        if (a(r2, r3, r11.l + r0) != false) goto L_0x0180;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0175, code lost:
        r11.z.g = "write file error";
        h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0180, code lost:
        r0 = r0 + r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0181, code lost:
        if (r1 != -1) goto L_0x0187;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0185, code lost:
        if (r11.k != null) goto L_0x0110;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0187, code lost:
        a(r11.l + r1, r11.l + r0, r2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0191, code lost:
        r11.z.n = (java.lang.System.currentTimeMillis() - r11.z.e) - r11.z.m;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:?, code lost:
        r11.z.c = r11.w.toByteArray();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x01ad, code lost:
        r11.z.a = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x01b2, code lost:
        if (r1 != -1) goto L_0x01c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01b6, code lost:
        if (r11.k == null) goto L_0x01c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x01b8, code lost:
        a(cn.kuwo.autosdk.e.NOTIFY_START, r1 + r11.l);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x01c0, code lost:
        h();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x01c6, code lost:
        r11.z.g = "OutOfMemoryError";
        h();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r11 = this;
            r0 = 0
            r10 = -1
            cn.kuwo.autosdk.a r1 = r11.z
            long r2 = java.lang.System.currentTimeMillis()
            r1.e = r2
            cn.kuwo.autosdk.a r1 = r11.z
            java.lang.String r2 = r11.g
            r1.k = r2
            boolean r1 = r11.n
            if (r1 == 0) goto L_0x002a
            java.util.concurrent.atomic.AtomicLong r1 = c
            long r2 = r1.get()
            long r4 = b
            int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r1 <= 0) goto L_0x002a
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "flow limit"
            r0.g = r1
            r11.h()
        L_0x0029:
            return
        L_0x002a:
            boolean r1 = r11.e()
            if (r1 != 0) goto L_0x003a
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "file error"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x003a:
            boolean r1 = r11.p
            if (r1 == 0) goto L_0x0048
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "user cancel"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x0048:
            boolean r1 = r11.b()
            if (r1 != 0) goto L_0x0052
            r11.h()
            goto L_0x0029
        L_0x0052:
            int r1 = r11.f()
            r2 = -2
            if (r2 != r1) goto L_0x005d
            r11.h()
            goto L_0x0029
        L_0x005d:
            if (r1 != r10) goto L_0x0063
            java.lang.String r2 = r11.k
            if (r2 != 0) goto L_0x006b
        L_0x0063:
            cn.kuwo.autosdk.e r2 = cn.kuwo.autosdk.e.NOTIFY_START
            int r3 = r11.l
            int r3 = r3 + r1
            r11.a((cn.kuwo.autosdk.e) r2, (int) r3)
        L_0x006b:
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x008b }
            java.net.HttpURLConnection r3 = r11.t     // Catch:{ IOException -> 0x008b }
            java.io.InputStream r3 = r3.getInputStream()     // Catch:{ IOException -> 0x008b }
            r2.<init>(r3)     // Catch:{ IOException -> 0x008b }
            r11.u = r2     // Catch:{ IOException -> 0x008b }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream
            r2.<init>()
            r11.w = r2
            r2 = 16384(0x4000, float:2.2959E-41)
            byte[] r2 = new byte[r2]     // Catch:{ OutOfMemoryError -> 0x0099 }
            java.io.ByteArrayOutputStream r3 = r11.w
            if (r3 != 0) goto L_0x00a1
            r11.h()
            goto L_0x0029
        L_0x008b:
            r0 = move-exception
            r0.printStackTrace()
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "read data failed"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x0099:
            r0 = move-exception
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "OutOfMemoryError"
            r0.g = r1
            goto L_0x0029
        L_0x00a1:
            boolean r3 = r11.g()
            if (r3 != 0) goto L_0x0110
            r11.h()
            goto L_0x0029
        L_0x00ac:
            java.io.InputStream r3 = r11.u     // Catch:{ IOException -> 0x00c9, Exception -> 0x00d5 }
            r4 = 0
            int r5 = r2.length     // Catch:{ IOException -> 0x00c9, Exception -> 0x00d5 }
            int r3 = r3.read(r2, r4, r5)     // Catch:{ IOException -> 0x00c9, Exception -> 0x00d5 }
            if (r3 >= 0) goto L_0x0103
            java.lang.String r2 = r11.k
            if (r2 == 0) goto L_0x0114
            if (r0 == r1) goto L_0x00e5
            if (r1 == r10) goto L_0x00e5
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "ContentLength error"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x00c9:
            r0 = move-exception
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "read error"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x00d5:
            r0 = move-exception
            boolean r0 = r11.p
            if (r0 != 0) goto L_0x00e0
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "unknown"
            r0.g = r1
        L_0x00e0:
            r11.h()
            goto L_0x0029
        L_0x00e5:
            java.io.File r2 = new java.io.File
            java.lang.String r3 = r11.k
            r2.<init>(r3)
            int r3 = r11.l
            int r0 = r0 + r3
            long r4 = (long) r0
            long r2 = r2.length()
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 == 0) goto L_0x0114
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "io error (file lenght error) "
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x0103:
            boolean r4 = r11.g()
            if (r4 != 0) goto L_0x010e
            r11.h()
            goto L_0x0029
        L_0x010e:
            if (r3 != 0) goto L_0x0123
        L_0x0110:
            boolean r3 = r11.p
            if (r3 == 0) goto L_0x00ac
        L_0x0114:
            boolean r0 = r11.p
            if (r0 == 0) goto L_0x0191
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "user cancel"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x0123:
            boolean r4 = r11.n
            if (r4 == 0) goto L_0x0134
            java.util.concurrent.atomic.AtomicLong r4 = c
            java.util.concurrent.atomic.AtomicLong r5 = c
            long r6 = r5.get()
            long r8 = (long) r3
            long r6 = r6 + r8
            r4.set(r6)
        L_0x0134:
            java.lang.String r4 = r11.k
            if (r4 != 0) goto L_0x013e
            java.io.ByteArrayOutputStream r4 = r11.w     // Catch:{ OutOfMemoryError -> 0x0160 }
            r5 = 0
            r4.write(r2, r5, r3)     // Catch:{ OutOfMemoryError -> 0x0160 }
        L_0x013e:
            java.lang.String r4 = r11.k
            if (r4 == 0) goto L_0x016c
            java.io.File r4 = new java.io.File
            java.lang.String r5 = r11.k
            r4.<init>(r5)
            int r5 = r11.l
            int r5 = r5 + r0
            long r6 = (long) r5
            long r4 = r4.length()
            int r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r4 == 0) goto L_0x016c
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "io error (check file length error)"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x0160:
            r0 = move-exception
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "out of memory error"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x016c:
            int r4 = r11.l
            int r4 = r4 + r0
            boolean r4 = r11.a(r2, r3, r4)
            if (r4 != 0) goto L_0x0180
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "write file error"
            r0.g = r1
            r11.h()
            goto L_0x0029
        L_0x0180:
            int r0 = r0 + r3
            if (r1 != r10) goto L_0x0187
            java.lang.String r4 = r11.k
            if (r4 != 0) goto L_0x0110
        L_0x0187:
            int r4 = r11.l
            int r4 = r4 + r1
            int r5 = r11.l
            int r5 = r5 + r0
            r11.a(r4, r5, r2, r3)
            goto L_0x0110
        L_0x0191:
            cn.kuwo.autosdk.a r0 = r11.z
            long r2 = java.lang.System.currentTimeMillis()
            cn.kuwo.autosdk.a r4 = r11.z
            long r4 = r4.e
            long r2 = r2 - r4
            cn.kuwo.autosdk.a r4 = r11.z
            long r4 = r4.m
            long r2 = r2 - r4
            r0.n = r2
            cn.kuwo.autosdk.a r0 = r11.z     // Catch:{ OutOfMemoryError -> 0x01c5 }
            java.io.ByteArrayOutputStream r2 = r11.w     // Catch:{ OutOfMemoryError -> 0x01c5 }
            byte[] r2 = r2.toByteArray()     // Catch:{ OutOfMemoryError -> 0x01c5 }
            r0.c = r2     // Catch:{ OutOfMemoryError -> 0x01c5 }
            cn.kuwo.autosdk.a r0 = r11.z
            r2 = 1
            r0.a = r2
            if (r1 != r10) goto L_0x01c0
            java.lang.String r0 = r11.k
            if (r0 == 0) goto L_0x01c0
            cn.kuwo.autosdk.e r0 = cn.kuwo.autosdk.e.NOTIFY_START
            int r2 = r11.l
            int r1 = r1 + r2
            r11.a((cn.kuwo.autosdk.e) r0, (int) r1)
        L_0x01c0:
            r11.h()
            goto L_0x0029
        L_0x01c5:
            r0 = move-exception
            cn.kuwo.autosdk.a r0 = r11.z
            java.lang.String r1 = "OutOfMemoryError"
            r0.g = r1
            r11.h()
            goto L_0x0029
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.b.run():void");
    }
}
