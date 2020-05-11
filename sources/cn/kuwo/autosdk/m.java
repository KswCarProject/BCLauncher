package cn.kuwo.autosdk;

public final class m {
    private static char[] a = new char[64];
    private static byte[] b = new byte[128];

    static {
        char c = 'A';
        int i = 0;
        while (c <= 'Z') {
            a[i] = c;
            c = (char) (c + 1);
            i++;
        }
        char c2 = 'a';
        while (c2 <= 'z') {
            a[i] = c2;
            c2 = (char) (c2 + 1);
            i++;
        }
        char c3 = '0';
        while (c3 <= '9') {
            a[i] = c3;
            c3 = (char) (c3 + 1);
            i++;
        }
        int i2 = i + 1;
        a[i] = '+';
        int i3 = i2 + 1;
        a[i2] = '/';
        for (int i4 = 0; i4 < b.length; i4++) {
            b[i4] = -1;
        }
        for (int i5 = 0; i5 < 64; i5++) {
            b[a[i5]] = (byte) i5;
        }
    }

    public static char[] a(byte[] bArr, int i) {
        return a(bArr, i, (String) null);
    }

    public static char[] a(byte[] bArr, int i, String str) {
        byte b2;
        byte b3;
        if (str != null && !str.equals("")) {
            byte[] bytes = str.getBytes();
            int i2 = 0;
            while (i2 < bArr.length) {
                int i3 = 0;
                while (i3 < bytes.length && i2 < bArr.length) {
                    bArr[i2] = (byte) (bArr[i2] ^ bytes[i3]);
                    i3++;
                    i2++;
                }
            }
        }
        int i4 = ((i * 4) + 2) / 3;
        char[] cArr = new char[(((i + 2) / 3) * 4)];
        int i5 = 0;
        int i6 = 0;
        while (i6 < i) {
            int i7 = i6 + 1;
            byte b4 = bArr[i6] & 255;
            if (i7 < i) {
                b2 = bArr[i7] & 255;
                i7++;
            } else {
                b2 = 0;
            }
            if (i7 < i) {
                i6 = i7 + 1;
                b3 = bArr[i7] & 255;
            } else {
                i6 = i7;
                b3 = 0;
            }
            int i8 = b4 >>> 2;
            int i9 = ((b4 & 3) << 4) | (b2 >>> 4);
            int i10 = ((b2 & 15) << 2) | (b3 >>> 6);
            byte b5 = b3 & 63;
            int i11 = i5 + 1;
            cArr[i5] = a[i8];
            int i12 = i11 + 1;
            cArr[i11] = a[i9];
            cArr[i12] = i12 < i4 ? a[i10] : '=';
            int i13 = i12 + 1;
            cArr[i13] = i13 < i4 ? a[b5] : '=';
            i5 = i13 + 1;
        }
        return cArr;
    }
}
