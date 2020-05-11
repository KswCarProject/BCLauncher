package com.touchus.publicutils.utils;

import java.io.UnsupportedEncodingException;

public class QuotedPrintable {
    private static final byte CR = 13;
    private static final byte EQUALS = 61;
    private static final byte LF = 10;
    private static final byte LIT_END = 126;
    private static final byte LIT_START = 33;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte TAB = 9;
    private static int mCurrentLineLength = 0;

    public static int decode(byte[] qp) {
        int retlen;
        int qplen = qp.length;
        int i = 0;
        int retlen2 = 0;
        while (i < qplen) {
            if (qp[i] == 61 && qplen - i > 2) {
                if (qp[i + 1] == 13 && qp[i + 2] == 10) {
                    i += 2;
                    retlen = retlen2;
                    i++;
                    retlen2 = retlen;
                } else if (!isHexDigit(qp[i + 1]) || !isHexDigit(qp[i + 2])) {
                    System.err.println("decode: Invalid sequence = " + qp[i + 1] + qp[i + 2]);
                } else {
                    retlen = retlen2 + 1;
                    qp[retlen2] = (byte) ((getHexValue(qp[i + 1]) * 16) + getHexValue(qp[i + 2]));
                    i += 2;
                    i++;
                    retlen2 = retlen;
                }
            }
            if ((qp[i] >= 32 && qp[i] <= Byte.MAX_VALUE) || qp[i] == 9 || qp[i] == 13 || qp[i] == 10) {
                retlen = retlen2 + 1;
                qp[retlen2] = qp[i];
                i++;
                retlen2 = retlen;
            } else {
                retlen = retlen2;
                i++;
                retlen2 = retlen;
            }
        }
        return retlen2;
    }

    private static boolean isHexDigit(byte b) {
        return (b >= 48 && b <= 57) || (b >= 65 && b <= 70);
    }

    private static byte getHexValue(byte b) {
        return (byte) Character.digit((char) b, 16);
    }

    public static String decode(byte[] qp, String enc) {
        int len = decode(qp);
        try {
            return new String(qp, 0, len, enc);
        } catch (UnsupportedEncodingException e) {
            return new String(qp, 0, len);
        }
    }

    public static String encode(String content, String enc) {
        byte[] str;
        if (content == null) {
            return null;
        }
        try {
            str = content.getBytes(enc);
        } catch (UnsupportedEncodingException e) {
            str = content.getBytes();
        }
        return encode(str);
    }

    public static String encode(byte[] content) {
        if (content == null) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        mCurrentLineLength = 0;
        for (byte c : content) {
            if (c < 33 || c > 126 || c == 61) {
                checkLineLength(3, out);
                out.append('=');
                out.append(String.format("%02X", new Object[]{Byte.valueOf(c)}));
            } else {
                checkLineLength(1, out);
                out.append((char) c);
            }
        }
        return out.toString();
    }

    private static void checkLineLength(int required, StringBuilder out) {
        if (mCurrentLineLength + required > 75) {
            out.append("=/r/n");
            mCurrentLineLength = required;
            return;
        }
        mCurrentLineLength += required;
    }
}
