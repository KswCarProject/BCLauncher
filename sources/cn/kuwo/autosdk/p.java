package cn.kuwo.autosdk;

public final class p {
    public static final byte[] a = "ylzsxkwm".getBytes();
    public static final int b = a.length;
    private static final long[] c = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, 2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE};
    private static final int[] d;
    private static final int[] e;
    private static final char[][] f;
    private static final int[] g;
    private static final int[] h;
    private static final int[] i;
    private static final int[] j;
    private static final int[] k = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private static final long[] l;
    private static long m = 0;
    private static long n;
    private static long o;
    private static int[] p = new int[2];
    private static byte[] q = new byte[8];
    private static int r;
    private static int s;
    private static int t;

    static {
        int[] iArr = new int[64];
        iArr[0] = 57;
        iArr[1] = 49;
        iArr[2] = 41;
        iArr[3] = 33;
        iArr[4] = 25;
        iArr[5] = 17;
        iArr[6] = 9;
        iArr[7] = 1;
        iArr[8] = 59;
        iArr[9] = 51;
        iArr[10] = 43;
        iArr[11] = 35;
        iArr[12] = 27;
        iArr[13] = 19;
        iArr[14] = 11;
        iArr[15] = 3;
        iArr[16] = 61;
        iArr[17] = 53;
        iArr[18] = 45;
        iArr[19] = 37;
        iArr[20] = 29;
        iArr[21] = 21;
        iArr[22] = 13;
        iArr[23] = 5;
        iArr[24] = 63;
        iArr[25] = 55;
        iArr[26] = 47;
        iArr[27] = 39;
        iArr[28] = 31;
        iArr[29] = 23;
        iArr[30] = 15;
        iArr[31] = 7;
        iArr[32] = 56;
        iArr[33] = 48;
        iArr[34] = 40;
        iArr[35] = 32;
        iArr[36] = 24;
        iArr[37] = 16;
        iArr[38] = 8;
        iArr[40] = 58;
        iArr[41] = 50;
        iArr[42] = 42;
        iArr[43] = 34;
        iArr[44] = 26;
        iArr[45] = 18;
        iArr[46] = 10;
        iArr[47] = 2;
        iArr[48] = 60;
        iArr[49] = 52;
        iArr[50] = 44;
        iArr[51] = 36;
        iArr[52] = 28;
        iArr[53] = 20;
        iArr[54] = 12;
        iArr[55] = 4;
        iArr[56] = 62;
        iArr[57] = 54;
        iArr[58] = 46;
        iArr[59] = 38;
        iArr[60] = 30;
        iArr[61] = 22;
        iArr[62] = 14;
        iArr[63] = 6;
        d = iArr;
        int[] iArr2 = new int[64];
        iArr2[0] = 31;
        iArr2[2] = 1;
        iArr2[3] = 2;
        iArr2[4] = 3;
        iArr2[5] = 4;
        iArr2[6] = -1;
        iArr2[7] = -1;
        iArr2[8] = 3;
        iArr2[9] = 4;
        iArr2[10] = 5;
        iArr2[11] = 6;
        iArr2[12] = 7;
        iArr2[13] = 8;
        iArr2[14] = -1;
        iArr2[15] = -1;
        iArr2[16] = 7;
        iArr2[17] = 8;
        iArr2[18] = 9;
        iArr2[19] = 10;
        iArr2[20] = 11;
        iArr2[21] = 12;
        iArr2[22] = -1;
        iArr2[23] = -1;
        iArr2[24] = 11;
        iArr2[25] = 12;
        iArr2[26] = 13;
        iArr2[27] = 14;
        iArr2[28] = 15;
        iArr2[29] = 16;
        iArr2[30] = -1;
        iArr2[31] = -1;
        iArr2[32] = 15;
        iArr2[33] = 16;
        iArr2[34] = 17;
        iArr2[35] = 18;
        iArr2[36] = 19;
        iArr2[37] = 20;
        iArr2[38] = -1;
        iArr2[39] = -1;
        iArr2[40] = 19;
        iArr2[41] = 20;
        iArr2[42] = 21;
        iArr2[43] = 22;
        iArr2[44] = 23;
        iArr2[45] = 24;
        iArr2[46] = -1;
        iArr2[47] = -1;
        iArr2[48] = 23;
        iArr2[49] = 24;
        iArr2[50] = 25;
        iArr2[51] = 26;
        iArr2[52] = 27;
        iArr2[53] = 28;
        iArr2[54] = -1;
        iArr2[55] = -1;
        iArr2[56] = 27;
        iArr2[57] = 28;
        iArr2[58] = 29;
        iArr2[59] = 30;
        iArr2[60] = 31;
        iArr2[61] = 30;
        iArr2[62] = -1;
        iArr2[63] = -1;
        e = iArr2;
        char[] cArr = new char[64];
        cArr[0] = 14;
        cArr[1] = 4;
        cArr[2] = 3;
        cArr[3] = 15;
        cArr[4] = 2;
        cArr[5] = 13;
        cArr[6] = 5;
        cArr[7] = 3;
        cArr[8] = 13;
        cArr[9] = 14;
        cArr[10] = 6;
        cArr[11] = 9;
        cArr[12] = 11;
        cArr[13] = 2;
        cArr[15] = 5;
        cArr[16] = 4;
        cArr[17] = 1;
        cArr[18] = 10;
        cArr[19] = 12;
        cArr[20] = 15;
        cArr[21] = 6;
        cArr[22] = 9;
        cArr[23] = 10;
        cArr[24] = 1;
        cArr[25] = 8;
        cArr[26] = 12;
        cArr[27] = 7;
        cArr[28] = 8;
        cArr[29] = 11;
        cArr[30] = 7;
        cArr[33] = 15;
        cArr[34] = 10;
        cArr[35] = 5;
        cArr[36] = 14;
        cArr[37] = 4;
        cArr[38] = 9;
        cArr[39] = 10;
        cArr[40] = 7;
        cArr[41] = 8;
        cArr[42] = 12;
        cArr[43] = 3;
        cArr[44] = 13;
        cArr[45] = 1;
        cArr[46] = 3;
        cArr[47] = 6;
        cArr[48] = 15;
        cArr[49] = 12;
        cArr[50] = 6;
        cArr[51] = 11;
        cArr[52] = 2;
        cArr[53] = 9;
        cArr[54] = 5;
        cArr[56] = 4;
        cArr[57] = 2;
        cArr[58] = 11;
        cArr[59] = 14;
        cArr[60] = 1;
        cArr[61] = 7;
        cArr[62] = 8;
        cArr[63] = 13;
        char[] cArr2 = new char[64];
        cArr2[0] = 15;
        cArr2[2] = 9;
        cArr2[3] = 5;
        cArr2[4] = 6;
        cArr2[5] = 10;
        cArr2[6] = 12;
        cArr2[7] = 9;
        cArr2[8] = 8;
        cArr2[9] = 7;
        cArr2[10] = 2;
        cArr2[11] = 12;
        cArr2[12] = 3;
        cArr2[13] = 13;
        cArr2[14] = 5;
        cArr2[15] = 2;
        cArr2[16] = 1;
        cArr2[17] = 14;
        cArr2[18] = 7;
        cArr2[19] = 8;
        cArr2[20] = 11;
        cArr2[21] = 4;
        cArr2[23] = 3;
        cArr2[24] = 14;
        cArr2[25] = 11;
        cArr2[26] = 13;
        cArr2[27] = 6;
        cArr2[28] = 4;
        cArr2[29] = 1;
        cArr2[30] = 10;
        cArr2[31] = 15;
        cArr2[32] = 3;
        cArr2[33] = 13;
        cArr2[34] = 12;
        cArr2[35] = 11;
        cArr2[36] = 15;
        cArr2[37] = 3;
        cArr2[38] = 6;
        cArr2[40] = 4;
        cArr2[41] = 10;
        cArr2[42] = 1;
        cArr2[43] = 7;
        cArr2[44] = 8;
        cArr2[45] = 4;
        cArr2[46] = 11;
        cArr2[47] = 14;
        cArr2[48] = 13;
        cArr2[49] = 8;
        cArr2[51] = 6;
        cArr2[52] = 2;
        cArr2[53] = 15;
        cArr2[54] = 9;
        cArr2[55] = 5;
        cArr2[56] = 7;
        cArr2[57] = 1;
        cArr2[58] = 10;
        cArr2[59] = 12;
        cArr2[60] = 14;
        cArr2[61] = 2;
        cArr2[62] = 5;
        cArr2[63] = 9;
        char[] cArr3 = new char[64];
        cArr3[0] = 10;
        cArr3[1] = 13;
        cArr3[2] = 1;
        cArr3[3] = 11;
        cArr3[4] = 6;
        cArr3[5] = 8;
        cArr3[6] = 11;
        cArr3[7] = 5;
        cArr3[8] = 9;
        cArr3[9] = 4;
        cArr3[10] = 12;
        cArr3[11] = 2;
        cArr3[12] = 15;
        cArr3[13] = 3;
        cArr3[14] = 2;
        cArr3[15] = 14;
        cArr3[17] = 6;
        cArr3[18] = 13;
        cArr3[19] = 1;
        cArr3[20] = 3;
        cArr3[21] = 15;
        cArr3[22] = 4;
        cArr3[23] = 10;
        cArr3[24] = 14;
        cArr3[25] = 9;
        cArr3[26] = 7;
        cArr3[27] = 12;
        cArr3[28] = 5;
        cArr3[30] = 8;
        cArr3[31] = 7;
        cArr3[32] = 13;
        cArr3[33] = 1;
        cArr3[34] = 2;
        cArr3[35] = 4;
        cArr3[36] = 3;
        cArr3[37] = 6;
        cArr3[38] = 12;
        cArr3[39] = 11;
        cArr3[41] = 13;
        cArr3[42] = 5;
        cArr3[43] = 14;
        cArr3[44] = 6;
        cArr3[45] = 8;
        cArr3[46] = 15;
        cArr3[47] = 2;
        cArr3[48] = 7;
        cArr3[49] = 10;
        cArr3[50] = 8;
        cArr3[51] = 15;
        cArr3[52] = 4;
        cArr3[53] = 9;
        cArr3[54] = 11;
        cArr3[55] = 5;
        cArr3[56] = 9;
        cArr3[58] = 14;
        cArr3[59] = 3;
        cArr3[60] = 10;
        cArr3[61] = 7;
        cArr3[62] = 1;
        cArr3[63] = 12;
        char[] cArr4 = new char[64];
        cArr4[0] = 7;
        cArr4[1] = 10;
        cArr4[2] = 1;
        cArr4[3] = 15;
        cArr4[5] = 12;
        cArr4[6] = 11;
        cArr4[7] = 5;
        cArr4[8] = 14;
        cArr4[9] = 9;
        cArr4[10] = 8;
        cArr4[11] = 3;
        cArr4[12] = 9;
        cArr4[13] = 7;
        cArr4[14] = 4;
        cArr4[15] = 8;
        cArr4[16] = 13;
        cArr4[17] = 6;
        cArr4[18] = 2;
        cArr4[19] = 1;
        cArr4[20] = 6;
        cArr4[21] = 11;
        cArr4[22] = 12;
        cArr4[23] = 2;
        cArr4[24] = 3;
        cArr4[26] = 5;
        cArr4[27] = 14;
        cArr4[28] = 10;
        cArr4[29] = 13;
        cArr4[30] = 15;
        cArr4[31] = 4;
        cArr4[32] = 13;
        cArr4[33] = 3;
        cArr4[34] = 4;
        cArr4[35] = 9;
        cArr4[36] = 6;
        cArr4[37] = 10;
        cArr4[38] = 1;
        cArr4[39] = 12;
        cArr4[40] = 11;
        cArr4[42] = 2;
        cArr4[43] = 5;
        cArr4[45] = 13;
        cArr4[46] = 14;
        cArr4[47] = 2;
        cArr4[48] = 8;
        cArr4[49] = 15;
        cArr4[50] = 7;
        cArr4[51] = 4;
        cArr4[52] = 15;
        cArr4[53] = 1;
        cArr4[54] = 10;
        cArr4[55] = 7;
        cArr4[56] = 5;
        cArr4[57] = 6;
        cArr4[58] = 12;
        cArr4[59] = 11;
        cArr4[60] = 3;
        cArr4[61] = 8;
        cArr4[62] = 9;
        cArr4[63] = 14;
        char[] cArr5 = new char[64];
        cArr5[0] = 2;
        cArr5[1] = 4;
        cArr5[2] = 8;
        cArr5[3] = 15;
        cArr5[4] = 7;
        cArr5[5] = 10;
        cArr5[6] = 13;
        cArr5[7] = 6;
        cArr5[8] = 4;
        cArr5[9] = 1;
        cArr5[10] = 3;
        cArr5[11] = 12;
        cArr5[12] = 11;
        cArr5[13] = 7;
        cArr5[14] = 14;
        cArr5[16] = 12;
        cArr5[17] = 2;
        cArr5[18] = 5;
        cArr5[19] = 9;
        cArr5[20] = 10;
        cArr5[21] = 13;
        cArr5[23] = 3;
        cArr5[24] = 1;
        cArr5[25] = 11;
        cArr5[26] = 15;
        cArr5[27] = 5;
        cArr5[28] = 6;
        cArr5[29] = 8;
        cArr5[30] = 9;
        cArr5[31] = 14;
        cArr5[32] = 14;
        cArr5[33] = 11;
        cArr5[34] = 5;
        cArr5[35] = 6;
        cArr5[36] = 4;
        cArr5[37] = 1;
        cArr5[38] = 3;
        cArr5[39] = 10;
        cArr5[40] = 2;
        cArr5[41] = 12;
        cArr5[42] = 15;
        cArr5[44] = 13;
        cArr5[45] = 2;
        cArr5[46] = 8;
        cArr5[47] = 5;
        cArr5[48] = 11;
        cArr5[49] = 8;
        cArr5[51] = 15;
        cArr5[52] = 7;
        cArr5[53] = 14;
        cArr5[54] = 9;
        cArr5[55] = 4;
        cArr5[56] = 12;
        cArr5[57] = 7;
        cArr5[58] = 10;
        cArr5[59] = 9;
        cArr5[60] = 1;
        cArr5[61] = 13;
        cArr5[62] = 6;
        cArr5[63] = 3;
        char[] cArr6 = new char[64];
        cArr6[0] = 12;
        cArr6[1] = 9;
        cArr6[3] = 7;
        cArr6[4] = 9;
        cArr6[5] = 2;
        cArr6[6] = 14;
        cArr6[7] = 1;
        cArr6[8] = 10;
        cArr6[9] = 15;
        cArr6[10] = 3;
        cArr6[11] = 4;
        cArr6[12] = 6;
        cArr6[13] = 12;
        cArr6[14] = 5;
        cArr6[15] = 11;
        cArr6[16] = 1;
        cArr6[17] = 14;
        cArr6[18] = 13;
        cArr6[20] = 2;
        cArr6[21] = 8;
        cArr6[22] = 7;
        cArr6[23] = 13;
        cArr6[24] = 15;
        cArr6[25] = 5;
        cArr6[26] = 4;
        cArr6[27] = 10;
        cArr6[28] = 8;
        cArr6[29] = 3;
        cArr6[30] = 11;
        cArr6[31] = 6;
        cArr6[32] = 10;
        cArr6[33] = 4;
        cArr6[34] = 6;
        cArr6[35] = 11;
        cArr6[36] = 7;
        cArr6[37] = 9;
        cArr6[39] = 6;
        cArr6[40] = 4;
        cArr6[41] = 2;
        cArr6[42] = 13;
        cArr6[43] = 1;
        cArr6[44] = 9;
        cArr6[45] = 15;
        cArr6[46] = 3;
        cArr6[47] = 8;
        cArr6[48] = 15;
        cArr6[49] = 3;
        cArr6[50] = 1;
        cArr6[51] = 14;
        cArr6[52] = 12;
        cArr6[53] = 5;
        cArr6[54] = 11;
        cArr6[56] = 2;
        cArr6[57] = 12;
        cArr6[58] = 14;
        cArr6[59] = 7;
        cArr6[60] = 5;
        cArr6[61] = 10;
        cArr6[62] = 8;
        cArr6[63] = 13;
        char[] cArr7 = new char[64];
        cArr7[0] = 4;
        cArr7[1] = 1;
        cArr7[2] = 3;
        cArr7[3] = 10;
        cArr7[4] = 15;
        cArr7[5] = 12;
        cArr7[6] = 5;
        cArr7[8] = 2;
        cArr7[9] = 11;
        cArr7[10] = 9;
        cArr7[11] = 6;
        cArr7[12] = 8;
        cArr7[13] = 7;
        cArr7[14] = 6;
        cArr7[15] = 9;
        cArr7[16] = 11;
        cArr7[17] = 4;
        cArr7[18] = 12;
        cArr7[19] = 15;
        cArr7[21] = 3;
        cArr7[22] = 10;
        cArr7[23] = 5;
        cArr7[24] = 14;
        cArr7[25] = 13;
        cArr7[26] = 7;
        cArr7[27] = 8;
        cArr7[28] = 13;
        cArr7[29] = 14;
        cArr7[30] = 1;
        cArr7[31] = 2;
        cArr7[32] = 13;
        cArr7[33] = 6;
        cArr7[34] = 14;
        cArr7[35] = 9;
        cArr7[36] = 4;
        cArr7[37] = 1;
        cArr7[38] = 2;
        cArr7[39] = 14;
        cArr7[40] = 11;
        cArr7[41] = 13;
        cArr7[42] = 5;
        cArr7[44] = 1;
        cArr7[45] = 10;
        cArr7[46] = 8;
        cArr7[47] = 3;
        cArr7[49] = 11;
        cArr7[50] = 3;
        cArr7[51] = 5;
        cArr7[52] = 9;
        cArr7[53] = 4;
        cArr7[54] = 15;
        cArr7[55] = 2;
        cArr7[56] = 7;
        cArr7[57] = 8;
        cArr7[58] = 12;
        cArr7[59] = 15;
        cArr7[60] = 10;
        cArr7[61] = 7;
        cArr7[62] = 6;
        cArr7[63] = 12;
        char[] cArr8 = new char[64];
        cArr8[0] = 13;
        cArr8[1] = 7;
        cArr8[2] = 10;
        cArr8[4] = 6;
        cArr8[5] = 9;
        cArr8[6] = 5;
        cArr8[7] = 15;
        cArr8[8] = 8;
        cArr8[9] = 4;
        cArr8[10] = 3;
        cArr8[11] = 10;
        cArr8[12] = 11;
        cArr8[13] = 14;
        cArr8[14] = 12;
        cArr8[15] = 5;
        cArr8[16] = 2;
        cArr8[17] = 11;
        cArr8[18] = 9;
        cArr8[19] = 6;
        cArr8[20] = 15;
        cArr8[21] = 12;
        cArr8[23] = 3;
        cArr8[24] = 4;
        cArr8[25] = 1;
        cArr8[26] = 14;
        cArr8[27] = 13;
        cArr8[28] = 1;
        cArr8[29] = 2;
        cArr8[30] = 7;
        cArr8[31] = 8;
        cArr8[32] = 1;
        cArr8[33] = 2;
        cArr8[34] = 12;
        cArr8[35] = 15;
        cArr8[36] = 10;
        cArr8[37] = 4;
        cArr8[39] = 3;
        cArr8[40] = 13;
        cArr8[41] = 14;
        cArr8[42] = 6;
        cArr8[43] = 9;
        cArr8[44] = 7;
        cArr8[45] = 8;
        cArr8[46] = 9;
        cArr8[47] = 6;
        cArr8[48] = 15;
        cArr8[49] = 1;
        cArr8[50] = 5;
        cArr8[51] = 12;
        cArr8[52] = 3;
        cArr8[53] = 10;
        cArr8[54] = 14;
        cArr8[55] = 5;
        cArr8[56] = 8;
        cArr8[57] = 7;
        cArr8[58] = 11;
        cArr8[60] = 4;
        cArr8[61] = 13;
        cArr8[62] = 2;
        cArr8[63] = 11;
        f = new char[][]{cArr, cArr2, cArr3, cArr4, cArr5, cArr6, cArr7, cArr8};
        int[] iArr3 = new int[32];
        iArr3[0] = 15;
        iArr3[1] = 6;
        iArr3[2] = 19;
        iArr3[3] = 20;
        iArr3[4] = 28;
        iArr3[5] = 11;
        iArr3[6] = 27;
        iArr3[7] = 16;
        iArr3[9] = 14;
        iArr3[10] = 22;
        iArr3[11] = 25;
        iArr3[12] = 4;
        iArr3[13] = 17;
        iArr3[14] = 30;
        iArr3[15] = 9;
        iArr3[16] = 1;
        iArr3[17] = 7;
        iArr3[18] = 23;
        iArr3[19] = 13;
        iArr3[20] = 31;
        iArr3[21] = 26;
        iArr3[22] = 2;
        iArr3[23] = 8;
        iArr3[24] = 18;
        iArr3[25] = 12;
        iArr3[26] = 29;
        iArr3[27] = 5;
        iArr3[28] = 21;
        iArr3[29] = 10;
        iArr3[30] = 3;
        iArr3[31] = 24;
        g = iArr3;
        int[] iArr4 = new int[64];
        iArr4[0] = 39;
        iArr4[1] = 7;
        iArr4[2] = 47;
        iArr4[3] = 15;
        iArr4[4] = 55;
        iArr4[5] = 23;
        iArr4[6] = 63;
        iArr4[7] = 31;
        iArr4[8] = 38;
        iArr4[9] = 6;
        iArr4[10] = 46;
        iArr4[11] = 14;
        iArr4[12] = 54;
        iArr4[13] = 22;
        iArr4[14] = 62;
        iArr4[15] = 30;
        iArr4[16] = 37;
        iArr4[17] = 5;
        iArr4[18] = 45;
        iArr4[19] = 13;
        iArr4[20] = 53;
        iArr4[21] = 21;
        iArr4[22] = 61;
        iArr4[23] = 29;
        iArr4[24] = 36;
        iArr4[25] = 4;
        iArr4[26] = 44;
        iArr4[27] = 12;
        iArr4[28] = 52;
        iArr4[29] = 20;
        iArr4[30] = 60;
        iArr4[31] = 28;
        iArr4[32] = 35;
        iArr4[33] = 3;
        iArr4[34] = 43;
        iArr4[35] = 11;
        iArr4[36] = 51;
        iArr4[37] = 19;
        iArr4[38] = 59;
        iArr4[39] = 27;
        iArr4[40] = 34;
        iArr4[41] = 2;
        iArr4[42] = 42;
        iArr4[43] = 10;
        iArr4[44] = 50;
        iArr4[45] = 18;
        iArr4[46] = 58;
        iArr4[47] = 26;
        iArr4[48] = 33;
        iArr4[49] = 1;
        iArr4[50] = 41;
        iArr4[51] = 9;
        iArr4[52] = 49;
        iArr4[53] = 17;
        iArr4[54] = 57;
        iArr4[55] = 25;
        iArr4[56] = 32;
        iArr4[58] = 40;
        iArr4[59] = 8;
        iArr4[60] = 48;
        iArr4[61] = 16;
        iArr4[62] = 56;
        iArr4[63] = 24;
        h = iArr4;
        int[] iArr5 = new int[56];
        iArr5[0] = 56;
        iArr5[1] = 48;
        iArr5[2] = 40;
        iArr5[3] = 32;
        iArr5[4] = 24;
        iArr5[5] = 16;
        iArr5[6] = 8;
        iArr5[8] = 57;
        iArr5[9] = 49;
        iArr5[10] = 41;
        iArr5[11] = 33;
        iArr5[12] = 25;
        iArr5[13] = 17;
        iArr5[14] = 9;
        iArr5[15] = 1;
        iArr5[16] = 58;
        iArr5[17] = 50;
        iArr5[18] = 42;
        iArr5[19] = 34;
        iArr5[20] = 26;
        iArr5[21] = 18;
        iArr5[22] = 10;
        iArr5[23] = 2;
        iArr5[24] = 59;
        iArr5[25] = 51;
        iArr5[26] = 43;
        iArr5[27] = 35;
        iArr5[28] = 62;
        iArr5[29] = 54;
        iArr5[30] = 46;
        iArr5[31] = 38;
        iArr5[32] = 30;
        iArr5[33] = 22;
        iArr5[34] = 14;
        iArr5[35] = 6;
        iArr5[36] = 61;
        iArr5[37] = 53;
        iArr5[38] = 45;
        iArr5[39] = 37;
        iArr5[40] = 29;
        iArr5[41] = 21;
        iArr5[42] = 13;
        iArr5[43] = 5;
        iArr5[44] = 60;
        iArr5[45] = 52;
        iArr5[46] = 44;
        iArr5[47] = 36;
        iArr5[48] = 28;
        iArr5[49] = 20;
        iArr5[50] = 12;
        iArr5[51] = 4;
        iArr5[52] = 27;
        iArr5[53] = 19;
        iArr5[54] = 11;
        iArr5[55] = 3;
        i = iArr5;
        int[] iArr6 = new int[64];
        iArr6[0] = 13;
        iArr6[1] = 16;
        iArr6[2] = 10;
        iArr6[3] = 23;
        iArr6[5] = 4;
        iArr6[6] = -1;
        iArr6[7] = -1;
        iArr6[8] = 2;
        iArr6[9] = 27;
        iArr6[10] = 14;
        iArr6[11] = 5;
        iArr6[12] = 20;
        iArr6[13] = 9;
        iArr6[14] = -1;
        iArr6[15] = -1;
        iArr6[16] = 22;
        iArr6[17] = 18;
        iArr6[18] = 11;
        iArr6[19] = 3;
        iArr6[20] = 25;
        iArr6[21] = 7;
        iArr6[22] = -1;
        iArr6[23] = -1;
        iArr6[24] = 15;
        iArr6[25] = 6;
        iArr6[26] = 26;
        iArr6[27] = 19;
        iArr6[28] = 12;
        iArr6[29] = 1;
        iArr6[30] = -1;
        iArr6[31] = -1;
        iArr6[32] = 40;
        iArr6[33] = 51;
        iArr6[34] = 30;
        iArr6[35] = 36;
        iArr6[36] = 46;
        iArr6[37] = 54;
        iArr6[38] = -1;
        iArr6[39] = -1;
        iArr6[40] = 29;
        iArr6[41] = 39;
        iArr6[42] = 50;
        iArr6[43] = 44;
        iArr6[44] = 32;
        iArr6[45] = 47;
        iArr6[46] = -1;
        iArr6[47] = -1;
        iArr6[48] = 43;
        iArr6[49] = 48;
        iArr6[50] = 38;
        iArr6[51] = 55;
        iArr6[52] = 33;
        iArr6[53] = 52;
        iArr6[54] = -1;
        iArr6[55] = -1;
        iArr6[56] = 45;
        iArr6[57] = 41;
        iArr6[58] = 49;
        iArr6[59] = 35;
        iArr6[60] = 28;
        iArr6[61] = 31;
        iArr6[62] = -1;
        iArr6[63] = -1;
        j = iArr6;
        long[] jArr = new long[3];
        jArr[1] = 1048577;
        jArr[2] = 3145731;
        l = jArr;
    }

    private static long a(int[] iArr, int i2, long j2) {
        long j3 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            if (iArr[i3] >= 0 && (c[iArr[i3]] & j2) != 0) {
                j3 |= c[i3];
            }
        }
        return j3;
    }

    private static long a(long[] jArr, long j2) {
        m = a(d, 64, j2);
        p[0] = (int) (m & 4294967295L);
        p[1] = (int) ((m & -4294967296L) >> 32);
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= 16) {
                s = p[0];
                p[0] = p[1];
                p[1] = s;
                m = ((((long) p[1]) << 32) & -4294967296L) | (4294967295L & ((long) p[0]));
                m = a(h, 64, m);
                return m;
            }
            o = (long) p[1];
            o = a(e, 64, o);
            o ^= jArr[i3];
            for (int i4 = 0; i4 < 8; i4++) {
                q[i4] = (byte) ((int) (255 & (o >> (i4 * 8))));
            }
            r = 0;
            t = 7;
            while (t >= 0) {
                r <<= 4;
                r |= f[t][q[t]];
                t--;
            }
            o = (long) r;
            o = a(g, 32, o);
            n = (long) p[0];
            p[0] = p[1];
            p[1] = (int) (n ^ o);
            i2 = i3 + 1;
        }
    }

    private static void a(long j2, long[] jArr, int i2) {
        long a2 = a(i, 56, j2);
        for (int i3 = 0; i3 < 16; i3++) {
            a2 = ((a2 & (l[k[i3]] ^ -1)) >> k[i3]) | ((l[k[i3]] & a2) << (28 - k[i3]));
            jArr[i3] = a(j, 64, a2);
        }
        if (i2 == 1) {
            for (int i4 = 0; i4 < 8; i4++) {
                long j3 = jArr[i4];
                jArr[i4] = jArr[15 - i4];
                jArr[15 - i4] = j3;
            }
        }
    }

    public static synchronized byte[] a(byte[] bArr, int i2, byte[] bArr2, int i3) {
        byte[] bArr3;
        synchronized (p.class) {
            long j2 = 0;
            for (int i4 = 0; i4 < 8; i4++) {
                j2 |= ((long) bArr2[i4]) << (i4 * 8);
            }
            int i5 = i2 / 8;
            long[] jArr = new long[16];
            for (int i6 = 0; i6 < 16; i6++) {
                jArr[i6] = 0;
            }
            long[] jArr2 = new long[i5];
            for (int i7 = 0; i7 < i5; i7++) {
                for (int i8 = 0; i8 < 8; i8++) {
                    jArr2[i7] = jArr2[i7] | (((long) bArr[(i7 * 8) + i8]) << (i8 * 8));
                }
            }
            long[] jArr3 = new long[((((i5 + 1) * 8) + 1) / 8)];
            a(j2, jArr, 0);
            for (int i9 = 0; i9 < i5; i9++) {
                jArr3[i9] = a(jArr, jArr2[i9]);
            }
            int i10 = i2 % 8;
            byte[] bArr4 = new byte[(i2 - (i5 * 8))];
            System.arraycopy(bArr, i5 * 8, bArr4, 0, i2 - (i5 * 8));
            long j3 = 0;
            for (int i11 = 0; i11 < i10; i11++) {
                j3 |= ((long) bArr4[i11]) << (i11 * 8);
            }
            jArr3[i5] = a(jArr, j3);
            bArr3 = new byte[(jArr3.length * 8)];
            int i12 = 0;
            for (int i13 = 0; i13 < jArr3.length; i13++) {
                for (int i14 = 0; i14 < 8; i14++) {
                    bArr3[i12] = (byte) ((int) (255 & (jArr3[i13] >> (i14 * 8))));
                    i12++;
                }
            }
        }
        return bArr3;
    }
}
