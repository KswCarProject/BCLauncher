package a_vcard.android.telephony;

import a_vcard.android.text.Editable;
import a_vcard.android.text.SpannableStringBuilder;
import java.util.Locale;

public class PhoneNumberUtils {
    public static final int FORMAT_JAPAN = 2;
    public static final int FORMAT_NANP = 1;
    public static final int FORMAT_UNKNOWN = 0;
    private static final String[] NANP_COUNTRIES = {"US", "CA", "AS", "AI", "AG", "BS", "BB", "BM", "VG", "KY", "DM", "DO", "GD", "GU", "JM", "PR", "MS", "NP", "KN", "LC", "VC", "TT", "TC", "VI"};
    private static final int NANP_STATE_DASH = 4;
    private static final int NANP_STATE_DIGIT = 1;
    private static final int NANP_STATE_ONE = 3;
    private static final int NANP_STATE_PLUS = 2;

    public static String formatNumber(String source) {
        SpannableStringBuilder text = new SpannableStringBuilder(source);
        formatNumber(text, getFormatTypeForLocale(Locale.getDefault()));
        return text.toString();
    }

    public static int getFormatTypeForLocale(Locale locale) {
        String country = locale.getCountry();
        for (String equals : NANP_COUNTRIES) {
            if (equals.equals(country)) {
                return 1;
            }
        }
        if (locale.equals(Locale.JAPAN)) {
            return 2;
        }
        return 0;
    }

    public static void formatNumber(Editable text, int defaultFormattingType) {
        int formatType = defaultFormattingType;
        if (text.length() > 2 && text.charAt(0) == '+') {
            if (text.charAt(1) == '1') {
                formatType = 1;
            } else if (text.length() >= 3 && text.charAt(1) == '8' && text.charAt(2) == '1') {
                formatType = 2;
            } else {
                return;
            }
        }
        switch (formatType) {
            case 1:
                formatNanpNumber(text);
                return;
            case 2:
                formatJapaneseNumber(text);
                return;
            default:
                return;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004b, code lost:
        r2 = r2 + 1;
        r6 = r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void formatNanpNumber(a_vcard.android.text.Editable r15) {
        /*
            int r4 = r15.length()
            java.lang.String r12 = "+1-nnn-nnn-nnnn"
            int r12 = r12.length()
            if (r4 <= r12) goto L_0x000d
        L_0x000c:
            return
        L_0x000d:
            r12 = 0
            java.lang.CharSequence r10 = r15.subSequence(r12, r4)
            r8 = 0
        L_0x0013:
            int r12 = r15.length()
            if (r8 >= r12) goto L_0x002a
            char r12 = r15.charAt(r8)
            r13 = 45
            if (r12 != r13) goto L_0x0027
            int r12 = r8 + 1
            r15.delete(r8, r12)
            goto L_0x0013
        L_0x0027:
            int r8 = r8 + 1
            goto L_0x0013
        L_0x002a:
            int r4 = r15.length()
            r12 = 3
            int[] r1 = new int[r12]
            r5 = 0
            r11 = 1
            r7 = 0
            r2 = 0
            r6 = r5
        L_0x0036:
            if (r2 >= r4) goto L_0x0078
            char r0 = r15.charAt(r2)
            switch(r0) {
                case 43: goto L_0x0073;
                case 44: goto L_0x003f;
                case 45: goto L_0x0070;
                case 46: goto L_0x003f;
                case 47: goto L_0x003f;
                case 48: goto L_0x004f;
                case 49: goto L_0x0044;
                case 50: goto L_0x004f;
                case 51: goto L_0x004f;
                case 52: goto L_0x004f;
                case 53: goto L_0x004f;
                case 54: goto L_0x004f;
                case 55: goto L_0x004f;
                case 56: goto L_0x004f;
                case 57: goto L_0x004f;
                default: goto L_0x003f;
            }
        L_0x003f:
            r12 = 0
            r15.replace(r12, r4, r10)
            goto L_0x000c
        L_0x0044:
            if (r7 == 0) goto L_0x0049
            r12 = 2
            if (r11 != r12) goto L_0x004f
        L_0x0049:
            r11 = 3
            r5 = r6
        L_0x004b:
            int r2 = r2 + 1
            r6 = r5
            goto L_0x0036
        L_0x004f:
            r12 = 2
            if (r11 != r12) goto L_0x0057
            r12 = 0
            r15.replace(r12, r4, r10)
            goto L_0x000c
        L_0x0057:
            r12 = 3
            if (r11 != r12) goto L_0x0062
            int r5 = r6 + 1
            r1[r6] = r2
        L_0x005e:
            r11 = 1
            int r7 = r7 + 1
            goto L_0x004b
        L_0x0062:
            r12 = 4
            if (r11 == r12) goto L_0x00a8
            r12 = 3
            if (r7 == r12) goto L_0x006b
            r12 = 6
            if (r7 != r12) goto L_0x00a8
        L_0x006b:
            int r5 = r6 + 1
            r1[r6] = r2
            goto L_0x005e
        L_0x0070:
            r11 = 4
            r5 = r6
            goto L_0x004b
        L_0x0073:
            if (r2 != 0) goto L_0x003f
            r11 = 2
            r5 = r6
            goto L_0x004b
        L_0x0078:
            r12 = 7
            if (r7 != r12) goto L_0x00a6
            int r5 = r6 + -1
        L_0x007d:
            r2 = 0
        L_0x007e:
            if (r2 >= r5) goto L_0x008e
            r9 = r1[r2]
            int r12 = r9 + r2
            int r13 = r9 + r2
            java.lang.String r14 = "-"
            r15.replace(r12, r13, r14)
            int r2 = r2 + 1
            goto L_0x007e
        L_0x008e:
            int r3 = r15.length()
        L_0x0092:
            if (r3 <= 0) goto L_0x000c
            int r12 = r3 + -1
            char r12 = r15.charAt(r12)
            r13 = 45
            if (r12 != r13) goto L_0x000c
            int r12 = r3 + -1
            r15.delete(r12, r3)
            int r3 = r3 + -1
            goto L_0x0092
        L_0x00a6:
            r5 = r6
            goto L_0x007d
        L_0x00a8:
            r5 = r6
            goto L_0x005e
        */
        throw new UnsupportedOperationException("Method not decompiled: a_vcard.android.telephony.PhoneNumberUtils.formatNanpNumber(a_vcard.android.text.Editable):void");
    }

    public static void formatJapaneseNumber(Editable text) {
        JapanesePhoneNumberFormatter.format(text);
    }
}
