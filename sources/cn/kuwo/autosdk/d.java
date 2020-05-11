package cn.kuwo.autosdk;

class d implements Runnable {
    int a;
    int b;
    byte[] c;
    int d;
    final /* synthetic */ b e;

    d(b bVar) {
        this.e = bVar;
    }

    public d a(int i, int i2, byte[] bArr, int i3) {
        this.a = i;
        this.b = i2;
        this.c = bArr;
        this.d = i3;
        return this;
    }

    public void run() {
        if (!this.e.p) {
            synchronized (this.e) {
                this.e.x.a(this.e, this.a, this.b, this.c, this.d);
            }
            this.c = null;
        }
    }
}
