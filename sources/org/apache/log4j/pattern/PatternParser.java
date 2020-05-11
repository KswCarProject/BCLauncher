package org.apache.log4j.pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;

public final class PatternParser {
    private static final int CONVERTER_STATE = 1;
    private static final int DOT_STATE = 3;
    private static final char ESCAPE_CHAR = '%';
    private static final Map FILENAME_PATTERN_RULES;
    private static final int LITERAL_STATE = 0;
    private static final int MAX_STATE = 5;
    private static final int MIN_STATE = 4;
    private static final Map PATTERN_LAYOUT_RULES;
    static Class class$org$apache$log4j$pattern$ClassNamePatternConverter;
    static Class class$org$apache$log4j$pattern$DatePatternConverter;
    static Class class$org$apache$log4j$pattern$FileDatePatternConverter;
    static Class class$org$apache$log4j$pattern$FileLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$FullLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$IntegerPatternConverter;
    static Class class$org$apache$log4j$pattern$LevelPatternConverter;
    static Class class$org$apache$log4j$pattern$LineLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$LineSeparatorPatternConverter;
    static Class class$org$apache$log4j$pattern$LoggerPatternConverter;
    static Class class$org$apache$log4j$pattern$MessagePatternConverter;
    static Class class$org$apache$log4j$pattern$MethodLocationPatternConverter;
    static Class class$org$apache$log4j$pattern$NDCPatternConverter;
    static Class class$org$apache$log4j$pattern$PropertiesPatternConverter;
    static Class class$org$apache$log4j$pattern$RelativeTimePatternConverter;
    static Class class$org$apache$log4j$pattern$SequenceNumberPatternConverter;
    static Class class$org$apache$log4j$pattern$ThreadPatternConverter;
    static Class class$org$apache$log4j$pattern$ThrowableInformationPatternConverter;

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        Class cls10;
        Class cls11;
        Class cls12;
        Class cls13;
        Class cls14;
        Class cls15;
        Class cls16;
        Class cls17;
        Class cls18;
        Class cls19;
        Class cls20;
        Class cls21;
        Class cls22;
        Class cls23;
        Class cls24;
        Class cls25;
        Class cls26;
        Class cls27;
        Class cls28;
        Class cls29;
        Class cls30;
        Class cls31;
        Class cls32;
        Class cls33;
        Map rules = new HashMap(17);
        if (class$org$apache$log4j$pattern$LoggerPatternConverter == null) {
            cls = class$("org.apache.log4j.pattern.LoggerPatternConverter");
            class$org$apache$log4j$pattern$LoggerPatternConverter = cls;
        } else {
            cls = class$org$apache$log4j$pattern$LoggerPatternConverter;
        }
        rules.put("c", cls);
        if (class$org$apache$log4j$pattern$LoggerPatternConverter == null) {
            cls2 = class$("org.apache.log4j.pattern.LoggerPatternConverter");
            class$org$apache$log4j$pattern$LoggerPatternConverter = cls2;
        } else {
            cls2 = class$org$apache$log4j$pattern$LoggerPatternConverter;
        }
        rules.put("logger", cls2);
        if (class$org$apache$log4j$pattern$ClassNamePatternConverter == null) {
            cls3 = class$("org.apache.log4j.pattern.ClassNamePatternConverter");
            class$org$apache$log4j$pattern$ClassNamePatternConverter = cls3;
        } else {
            cls3 = class$org$apache$log4j$pattern$ClassNamePatternConverter;
        }
        rules.put("C", cls3);
        if (class$org$apache$log4j$pattern$ClassNamePatternConverter == null) {
            cls4 = class$("org.apache.log4j.pattern.ClassNamePatternConverter");
            class$org$apache$log4j$pattern$ClassNamePatternConverter = cls4;
        } else {
            cls4 = class$org$apache$log4j$pattern$ClassNamePatternConverter;
        }
        rules.put("class", cls4);
        if (class$org$apache$log4j$pattern$DatePatternConverter == null) {
            cls5 = class$("org.apache.log4j.pattern.DatePatternConverter");
            class$org$apache$log4j$pattern$DatePatternConverter = cls5;
        } else {
            cls5 = class$org$apache$log4j$pattern$DatePatternConverter;
        }
        rules.put("d", cls5);
        if (class$org$apache$log4j$pattern$DatePatternConverter == null) {
            cls6 = class$("org.apache.log4j.pattern.DatePatternConverter");
            class$org$apache$log4j$pattern$DatePatternConverter = cls6;
        } else {
            cls6 = class$org$apache$log4j$pattern$DatePatternConverter;
        }
        rules.put("date", cls6);
        if (class$org$apache$log4j$pattern$FileLocationPatternConverter == null) {
            cls7 = class$("org.apache.log4j.pattern.FileLocationPatternConverter");
            class$org$apache$log4j$pattern$FileLocationPatternConverter = cls7;
        } else {
            cls7 = class$org$apache$log4j$pattern$FileLocationPatternConverter;
        }
        rules.put("F", cls7);
        if (class$org$apache$log4j$pattern$FileLocationPatternConverter == null) {
            cls8 = class$("org.apache.log4j.pattern.FileLocationPatternConverter");
            class$org$apache$log4j$pattern$FileLocationPatternConverter = cls8;
        } else {
            cls8 = class$org$apache$log4j$pattern$FileLocationPatternConverter;
        }
        rules.put("file", cls8);
        if (class$org$apache$log4j$pattern$FullLocationPatternConverter == null) {
            cls9 = class$("org.apache.log4j.pattern.FullLocationPatternConverter");
            class$org$apache$log4j$pattern$FullLocationPatternConverter = cls9;
        } else {
            cls9 = class$org$apache$log4j$pattern$FullLocationPatternConverter;
        }
        rules.put("l", cls9);
        if (class$org$apache$log4j$pattern$LineLocationPatternConverter == null) {
            cls10 = class$("org.apache.log4j.pattern.LineLocationPatternConverter");
            class$org$apache$log4j$pattern$LineLocationPatternConverter = cls10;
        } else {
            cls10 = class$org$apache$log4j$pattern$LineLocationPatternConverter;
        }
        rules.put("L", cls10);
        if (class$org$apache$log4j$pattern$LineLocationPatternConverter == null) {
            cls11 = class$("org.apache.log4j.pattern.LineLocationPatternConverter");
            class$org$apache$log4j$pattern$LineLocationPatternConverter = cls11;
        } else {
            cls11 = class$org$apache$log4j$pattern$LineLocationPatternConverter;
        }
        rules.put("line", cls11);
        if (class$org$apache$log4j$pattern$MessagePatternConverter == null) {
            cls12 = class$("org.apache.log4j.pattern.MessagePatternConverter");
            class$org$apache$log4j$pattern$MessagePatternConverter = cls12;
        } else {
            cls12 = class$org$apache$log4j$pattern$MessagePatternConverter;
        }
        rules.put("m", cls12);
        if (class$org$apache$log4j$pattern$MessagePatternConverter == null) {
            cls13 = class$("org.apache.log4j.pattern.MessagePatternConverter");
            class$org$apache$log4j$pattern$MessagePatternConverter = cls13;
        } else {
            cls13 = class$org$apache$log4j$pattern$MessagePatternConverter;
        }
        rules.put("message", cls13);
        if (class$org$apache$log4j$pattern$LineSeparatorPatternConverter == null) {
            cls14 = class$("org.apache.log4j.pattern.LineSeparatorPatternConverter");
            class$org$apache$log4j$pattern$LineSeparatorPatternConverter = cls14;
        } else {
            cls14 = class$org$apache$log4j$pattern$LineSeparatorPatternConverter;
        }
        rules.put("n", cls14);
        if (class$org$apache$log4j$pattern$MethodLocationPatternConverter == null) {
            cls15 = class$("org.apache.log4j.pattern.MethodLocationPatternConverter");
            class$org$apache$log4j$pattern$MethodLocationPatternConverter = cls15;
        } else {
            cls15 = class$org$apache$log4j$pattern$MethodLocationPatternConverter;
        }
        rules.put("M", cls15);
        if (class$org$apache$log4j$pattern$MethodLocationPatternConverter == null) {
            cls16 = class$("org.apache.log4j.pattern.MethodLocationPatternConverter");
            class$org$apache$log4j$pattern$MethodLocationPatternConverter = cls16;
        } else {
            cls16 = class$org$apache$log4j$pattern$MethodLocationPatternConverter;
        }
        rules.put("method", cls16);
        if (class$org$apache$log4j$pattern$LevelPatternConverter == null) {
            cls17 = class$("org.apache.log4j.pattern.LevelPatternConverter");
            class$org$apache$log4j$pattern$LevelPatternConverter = cls17;
        } else {
            cls17 = class$org$apache$log4j$pattern$LevelPatternConverter;
        }
        rules.put("p", cls17);
        if (class$org$apache$log4j$pattern$LevelPatternConverter == null) {
            cls18 = class$("org.apache.log4j.pattern.LevelPatternConverter");
            class$org$apache$log4j$pattern$LevelPatternConverter = cls18;
        } else {
            cls18 = class$org$apache$log4j$pattern$LevelPatternConverter;
        }
        rules.put("level", cls18);
        if (class$org$apache$log4j$pattern$RelativeTimePatternConverter == null) {
            cls19 = class$("org.apache.log4j.pattern.RelativeTimePatternConverter");
            class$org$apache$log4j$pattern$RelativeTimePatternConverter = cls19;
        } else {
            cls19 = class$org$apache$log4j$pattern$RelativeTimePatternConverter;
        }
        rules.put("r", cls19);
        if (class$org$apache$log4j$pattern$RelativeTimePatternConverter == null) {
            cls20 = class$("org.apache.log4j.pattern.RelativeTimePatternConverter");
            class$org$apache$log4j$pattern$RelativeTimePatternConverter = cls20;
        } else {
            cls20 = class$org$apache$log4j$pattern$RelativeTimePatternConverter;
        }
        rules.put("relative", cls20);
        if (class$org$apache$log4j$pattern$ThreadPatternConverter == null) {
            cls21 = class$("org.apache.log4j.pattern.ThreadPatternConverter");
            class$org$apache$log4j$pattern$ThreadPatternConverter = cls21;
        } else {
            cls21 = class$org$apache$log4j$pattern$ThreadPatternConverter;
        }
        rules.put("t", cls21);
        if (class$org$apache$log4j$pattern$ThreadPatternConverter == null) {
            cls22 = class$("org.apache.log4j.pattern.ThreadPatternConverter");
            class$org$apache$log4j$pattern$ThreadPatternConverter = cls22;
        } else {
            cls22 = class$org$apache$log4j$pattern$ThreadPatternConverter;
        }
        rules.put("thread", cls22);
        if (class$org$apache$log4j$pattern$NDCPatternConverter == null) {
            cls23 = class$("org.apache.log4j.pattern.NDCPatternConverter");
            class$org$apache$log4j$pattern$NDCPatternConverter = cls23;
        } else {
            cls23 = class$org$apache$log4j$pattern$NDCPatternConverter;
        }
        rules.put("x", cls23);
        if (class$org$apache$log4j$pattern$NDCPatternConverter == null) {
            cls24 = class$("org.apache.log4j.pattern.NDCPatternConverter");
            class$org$apache$log4j$pattern$NDCPatternConverter = cls24;
        } else {
            cls24 = class$org$apache$log4j$pattern$NDCPatternConverter;
        }
        rules.put("ndc", cls24);
        if (class$org$apache$log4j$pattern$PropertiesPatternConverter == null) {
            cls25 = class$("org.apache.log4j.pattern.PropertiesPatternConverter");
            class$org$apache$log4j$pattern$PropertiesPatternConverter = cls25;
        } else {
            cls25 = class$org$apache$log4j$pattern$PropertiesPatternConverter;
        }
        rules.put("X", cls25);
        if (class$org$apache$log4j$pattern$PropertiesPatternConverter == null) {
            cls26 = class$("org.apache.log4j.pattern.PropertiesPatternConverter");
            class$org$apache$log4j$pattern$PropertiesPatternConverter = cls26;
        } else {
            cls26 = class$org$apache$log4j$pattern$PropertiesPatternConverter;
        }
        rules.put("properties", cls26);
        if (class$org$apache$log4j$pattern$SequenceNumberPatternConverter == null) {
            cls27 = class$("org.apache.log4j.pattern.SequenceNumberPatternConverter");
            class$org$apache$log4j$pattern$SequenceNumberPatternConverter = cls27;
        } else {
            cls27 = class$org$apache$log4j$pattern$SequenceNumberPatternConverter;
        }
        rules.put("sn", cls27);
        if (class$org$apache$log4j$pattern$SequenceNumberPatternConverter == null) {
            cls28 = class$("org.apache.log4j.pattern.SequenceNumberPatternConverter");
            class$org$apache$log4j$pattern$SequenceNumberPatternConverter = cls28;
        } else {
            cls28 = class$org$apache$log4j$pattern$SequenceNumberPatternConverter;
        }
        rules.put("sequenceNumber", cls28);
        if (class$org$apache$log4j$pattern$ThrowableInformationPatternConverter == null) {
            cls29 = class$("org.apache.log4j.pattern.ThrowableInformationPatternConverter");
            class$org$apache$log4j$pattern$ThrowableInformationPatternConverter = cls29;
        } else {
            cls29 = class$org$apache$log4j$pattern$ThrowableInformationPatternConverter;
        }
        rules.put("throwable", cls29);
        PATTERN_LAYOUT_RULES = new ReadOnlyMap(rules);
        Map fnameRules = new HashMap(4);
        if (class$org$apache$log4j$pattern$FileDatePatternConverter == null) {
            cls30 = class$("org.apache.log4j.pattern.FileDatePatternConverter");
            class$org$apache$log4j$pattern$FileDatePatternConverter = cls30;
        } else {
            cls30 = class$org$apache$log4j$pattern$FileDatePatternConverter;
        }
        fnameRules.put("d", cls30);
        if (class$org$apache$log4j$pattern$FileDatePatternConverter == null) {
            cls31 = class$("org.apache.log4j.pattern.FileDatePatternConverter");
            class$org$apache$log4j$pattern$FileDatePatternConverter = cls31;
        } else {
            cls31 = class$org$apache$log4j$pattern$FileDatePatternConverter;
        }
        fnameRules.put("date", cls31);
        if (class$org$apache$log4j$pattern$IntegerPatternConverter == null) {
            cls32 = class$("org.apache.log4j.pattern.IntegerPatternConverter");
            class$org$apache$log4j$pattern$IntegerPatternConverter = cls32;
        } else {
            cls32 = class$org$apache$log4j$pattern$IntegerPatternConverter;
        }
        fnameRules.put("i", cls32);
        if (class$org$apache$log4j$pattern$IntegerPatternConverter == null) {
            cls33 = class$("org.apache.log4j.pattern.IntegerPatternConverter");
            class$org$apache$log4j$pattern$IntegerPatternConverter = cls33;
        } else {
            cls33 = class$org$apache$log4j$pattern$IntegerPatternConverter;
        }
        fnameRules.put("index", cls33);
        FILENAME_PATTERN_RULES = new ReadOnlyMap(fnameRules);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private PatternParser() {
    }

    public static Map getPatternLayoutRules() {
        return PATTERN_LAYOUT_RULES;
    }

    public static Map getFileNamePatternRules() {
        return FILENAME_PATTERN_RULES;
    }

    private static int extractConverter(char lastChar, String pattern, int i, StringBuffer convBuf, StringBuffer currentLiteral) {
        convBuf.setLength(0);
        if (!Character.isUnicodeIdentifierStart(lastChar)) {
            return i;
        }
        convBuf.append(lastChar);
        while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
            convBuf.append(pattern.charAt(i));
            currentLiteral.append(pattern.charAt(i));
            i++;
        }
        return i;
    }

    private static int extractOptions(String pattern, int i, List options) {
        int end;
        while (i < pattern.length() && pattern.charAt(i) == '{' && (end = pattern.indexOf(125, i)) != -1) {
            options.add(pattern.substring(i + 1, end));
            i = end + 1;
        }
        return i;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void parse(java.lang.String r13, java.util.List r14, java.util.List r15, java.util.Map r16, java.util.Map r17) {
        /*
            if (r13 != 0) goto L_0x000a
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r5 = "pattern"
            r1.<init>(r5)
            throw r1
        L_0x000a:
            java.lang.StringBuffer r3 = new java.lang.StringBuffer
            r1 = 32
            r3.<init>(r1)
            int r11 = r13.length()
            r12 = 0
            r2 = 0
            org.apache.log4j.pattern.FormattingInfo r4 = org.apache.log4j.pattern.FormattingInfo.getDefault()
            r10 = r2
        L_0x001c:
            if (r10 >= r11) goto L_0x0175
            int r2 = r10 + 1
            char r0 = r13.charAt(r10)
            switch(r12) {
                case 0: goto L_0x0029;
                case 1: goto L_0x006b;
                case 2: goto L_0x0027;
                case 3: goto L_0x00f4;
                case 4: goto L_0x00b3;
                case 5: goto L_0x013b;
                default: goto L_0x0027;
            }
        L_0x0027:
            r10 = r2
            goto L_0x001c
        L_0x0029:
            if (r2 != r11) goto L_0x0030
            r3.append(r0)
            r10 = r2
            goto L_0x001c
        L_0x0030:
            r1 = 37
            if (r0 != r1) goto L_0x0067
            char r1 = r13.charAt(r2)
            switch(r1) {
                case 37: goto L_0x0061;
                default: goto L_0x003b;
            }
        L_0x003b:
            int r1 = r3.length()
            if (r1 == 0) goto L_0x0054
            org.apache.log4j.pattern.LiteralPatternConverter r1 = new org.apache.log4j.pattern.LiteralPatternConverter
            java.lang.String r5 = r3.toString()
            r1.<init>(r5)
            r14.add(r1)
            org.apache.log4j.pattern.FormattingInfo r1 = org.apache.log4j.pattern.FormattingInfo.getDefault()
            r15.add(r1)
        L_0x0054:
            r1 = 0
            r3.setLength(r1)
            r3.append(r0)
            r12 = 1
            org.apache.log4j.pattern.FormattingInfo r4 = org.apache.log4j.pattern.FormattingInfo.getDefault()
            goto L_0x0027
        L_0x0061:
            r3.append(r0)
            int r2 = r2 + 1
            goto L_0x0027
        L_0x0067:
            r3.append(r0)
            goto L_0x0027
        L_0x006b:
            r3.append(r0)
            switch(r0) {
                case 45: goto L_0x008b;
                case 46: goto L_0x009b;
                default: goto L_0x0071;
            }
        L_0x0071:
            r1 = 48
            if (r0 < r1) goto L_0x009d
            r1 = 57
            if (r0 > r1) goto L_0x009d
            org.apache.log4j.pattern.FormattingInfo r9 = new org.apache.log4j.pattern.FormattingInfo
            boolean r1 = r4.isLeftAligned()
            int r5 = r0 + -48
            int r6 = r4.getMaxLength()
            r9.<init>(r1, r5, r6)
            r12 = 4
            r4 = r9
            goto L_0x0027
        L_0x008b:
            org.apache.log4j.pattern.FormattingInfo r9 = new org.apache.log4j.pattern.FormattingInfo
            r1 = 1
            int r5 = r4.getMinLength()
            int r6 = r4.getMaxLength()
            r9.<init>(r1, r5, r6)
            r4 = r9
            goto L_0x0027
        L_0x009b:
            r12 = 3
            goto L_0x0027
        L_0x009d:
            r1 = r13
            r5 = r16
            r6 = r17
            r7 = r14
            r8 = r15
            int r2 = finalizeConverter(r0, r1, r2, r3, r4, r5, r6, r7, r8)
            r12 = 0
            org.apache.log4j.pattern.FormattingInfo r4 = org.apache.log4j.pattern.FormattingInfo.getDefault()
            r1 = 0
            r3.setLength(r1)
            goto L_0x0027
        L_0x00b3:
            r3.append(r0)
            r1 = 48
            if (r0 < r1) goto L_0x00d7
            r1 = 57
            if (r0 > r1) goto L_0x00d7
            org.apache.log4j.pattern.FormattingInfo r9 = new org.apache.log4j.pattern.FormattingInfo
            boolean r1 = r4.isLeftAligned()
            int r5 = r4.getMinLength()
            int r5 = r5 * 10
            int r6 = r0 + -48
            int r5 = r5 + r6
            int r6 = r4.getMaxLength()
            r9.<init>(r1, r5, r6)
            r4 = r9
            goto L_0x0027
        L_0x00d7:
            r1 = 46
            if (r0 != r1) goto L_0x00de
            r12 = 3
            goto L_0x0027
        L_0x00de:
            r1 = r13
            r5 = r16
            r6 = r17
            r7 = r14
            r8 = r15
            int r2 = finalizeConverter(r0, r1, r2, r3, r4, r5, r6, r7, r8)
            r12 = 0
            org.apache.log4j.pattern.FormattingInfo r4 = org.apache.log4j.pattern.FormattingInfo.getDefault()
            r1 = 0
            r3.setLength(r1)
            goto L_0x0027
        L_0x00f4:
            r3.append(r0)
            r1 = 48
            if (r0 < r1) goto L_0x0112
            r1 = 57
            if (r0 > r1) goto L_0x0112
            org.apache.log4j.pattern.FormattingInfo r9 = new org.apache.log4j.pattern.FormattingInfo
            boolean r1 = r4.isLeftAligned()
            int r5 = r4.getMinLength()
            int r6 = r0 + -48
            r9.<init>(r1, r5, r6)
            r12 = 5
            r4 = r9
            goto L_0x0027
        L_0x0112:
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r5 = "Error occured in position "
            java.lang.StringBuffer r1 = r1.append(r5)
            java.lang.StringBuffer r1 = r1.append(r2)
            java.lang.String r5 = ".\n Was expecting digit, instead got char \""
            java.lang.StringBuffer r1 = r1.append(r5)
            java.lang.StringBuffer r1 = r1.append(r0)
            java.lang.String r5 = "\"."
            java.lang.StringBuffer r1 = r1.append(r5)
            java.lang.String r1 = r1.toString()
            org.apache.log4j.helpers.LogLog.error(r1)
            r12 = 0
            goto L_0x0027
        L_0x013b:
            r3.append(r0)
            r1 = 48
            if (r0 < r1) goto L_0x015f
            r1 = 57
            if (r0 > r1) goto L_0x015f
            org.apache.log4j.pattern.FormattingInfo r9 = new org.apache.log4j.pattern.FormattingInfo
            boolean r1 = r4.isLeftAligned()
            int r5 = r4.getMinLength()
            int r6 = r4.getMaxLength()
            int r6 = r6 * 10
            int r7 = r0 + -48
            int r6 = r6 + r7
            r9.<init>(r1, r5, r6)
            r4 = r9
            goto L_0x0027
        L_0x015f:
            r1 = r13
            r5 = r16
            r6 = r17
            r7 = r14
            r8 = r15
            int r2 = finalizeConverter(r0, r1, r2, r3, r4, r5, r6, r7, r8)
            r12 = 0
            org.apache.log4j.pattern.FormattingInfo r4 = org.apache.log4j.pattern.FormattingInfo.getDefault()
            r1 = 0
            r3.setLength(r1)
            goto L_0x0027
        L_0x0175:
            int r1 = r3.length()
            if (r1 == 0) goto L_0x018e
            org.apache.log4j.pattern.LiteralPatternConverter r1 = new org.apache.log4j.pattern.LiteralPatternConverter
            java.lang.String r5 = r3.toString()
            r1.<init>(r5)
            r14.add(r1)
            org.apache.log4j.pattern.FormattingInfo r1 = org.apache.log4j.pattern.FormattingInfo.getDefault()
            r15.add(r1)
        L_0x018e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.pattern.PatternParser.parse(java.lang.String, java.util.List, java.util.List, java.util.Map, java.util.Map):void");
    }

    private static PatternConverter createConverter(String converterId, StringBuffer currentLiteral, Map converterRegistry, Map rules, List options) {
        Class converterClass;
        Object obj;
        String converterName = converterId;
        int i = converterId.length();
        Object obj2 = null;
        while (i > 0 && obj2 == null) {
            converterName = converterName.substring(0, i);
            if (converterRegistry != null) {
                obj = converterRegistry.get(converterName);
            } else {
                obj = obj2;
            }
            if (obj == null && rules != null) {
                obj = rules.get(converterName);
            }
            i--;
            obj2 = obj;
        }
        if (obj2 == null) {
            LogLog.error(new StringBuffer().append("Unrecognized format specifier [").append(converterId).append("]").toString());
            return null;
        }
        if (obj2 instanceof Class) {
            converterClass = (Class) obj2;
        } else if (obj2 instanceof String) {
            try {
                converterClass = Loader.loadClass((String) obj2);
            } catch (ClassNotFoundException ex) {
                LogLog.warn(new StringBuffer().append("Class for conversion pattern %").append(converterName).append(" not found").toString(), ex);
                return null;
            }
        } else {
            LogLog.warn(new StringBuffer().append("Bad map entry for conversion pattern %").append(converterName).append(".").toString());
            return null;
        }
        try {
            Object newObj = converterClass.getMethod("newInstance", new Class[]{Class.forName("[Ljava.lang.String;")}).invoke((Object) null, new Object[]{(String[]) ((String[]) options.toArray(new String[options.size()]))});
            if (newObj instanceof PatternConverter) {
                currentLiteral.delete(0, currentLiteral.length() - (converterId.length() - converterName.length()));
                return (PatternConverter) newObj;
            }
            LogLog.warn(new StringBuffer().append("Class ").append(converterClass.getName()).append(" does not extend PatternConverter.").toString());
            return null;
        } catch (Exception ex2) {
            LogLog.error(new StringBuffer().append("Error creating converter for ").append(converterId).toString(), ex2);
            try {
                PatternConverter pc = (PatternConverter) converterClass.newInstance();
                currentLiteral.delete(0, currentLiteral.length() - (converterId.length() - converterName.length()));
                return pc;
            } catch (Exception ex22) {
                LogLog.error(new StringBuffer().append("Error creating converter for ").append(converterId).toString(), ex22);
            }
        }
    }

    private static int finalizeConverter(char c, String pattern, int i, StringBuffer currentLiteral, FormattingInfo formattingInfo, Map converterRegistry, Map rules, List patternConverters, List formattingInfos) {
        StringBuffer msg;
        StringBuffer convBuf = new StringBuffer();
        int i2 = extractConverter(c, pattern, i, convBuf, currentLiteral);
        String converterId = convBuf.toString();
        List options = new ArrayList();
        int i3 = extractOptions(pattern, i2, options);
        PatternConverter pc = createConverter(converterId, currentLiteral, converterRegistry, rules, options);
        if (pc == null) {
            if (converterId == null || converterId.length() == 0) {
                msg = new StringBuffer("Empty conversion specifier starting at position ");
            } else {
                msg = new StringBuffer("Unrecognized conversion specifier [");
                msg.append(converterId);
                msg.append("] starting at position ");
            }
            msg.append(Integer.toString(i3));
            msg.append(" in conversion pattern.");
            LogLog.error(msg.toString());
            patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
            formattingInfos.add(FormattingInfo.getDefault());
        } else {
            patternConverters.add(pc);
            formattingInfos.add(formattingInfo);
            if (currentLiteral.length() > 0) {
                patternConverters.add(new LiteralPatternConverter(currentLiteral.toString()));
                formattingInfos.add(FormattingInfo.getDefault());
            }
        }
        currentLiteral.setLength(0);
        return i3;
    }

    private static class ReadOnlyMap implements Map {
        private final Map map;

        public ReadOnlyMap(Map src) {
            this.map = src;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        public Set entrySet() {
            return this.map.entrySet();
        }

        public Object get(Object key) {
            return this.map.get(key);
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public Set keySet() {
            return this.map.keySet();
        }

        public Object put(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }

        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.map.size();
        }

        public Collection values() {
            return this.map.values();
        }
    }
}
