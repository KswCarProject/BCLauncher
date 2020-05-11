package cn.kuwo.autosdk;

public class o {
    public static int a(byte[] bArr, boolean z) {
        return (int) a(bArr, 4, z);
    }

    public static long a(byte[] bArr, int i, boolean z) {
        long j;
        if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("byte array is null or empty!");
        }
        int min = Math.min(i, bArr.length);
        if (z) {
            j = 0;
            int i2 = 0;
            while (i2 < min) {
                i2++;
                j = ((long) (bArr[i2] & 255)) | (j << 8);
            }
        } else {
            long j2 = 0;
            int i3 = min - 1;
            while (i3 >= 0) {
                i3--;
                j2 = ((long) (bArr[i3] & 255)) | (j << 8);
            }
        }
        return j;
    }
}
