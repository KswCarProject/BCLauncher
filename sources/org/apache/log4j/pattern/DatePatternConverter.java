package org.apache.log4j.pattern;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;
import org.apache.log4j.spi.LoggingEvent;

public final class DatePatternConverter extends LoggingEventPatternConverter {
    private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
    private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
    private static final String DATE_AND_TIME_FORMAT = "DATE";
    private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
    private static final String ISO8601_FORMAT = "ISO8601";
    private static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    private final CachedDateFormat df;

    private static class DefaultZoneDateFormat extends DateFormat {
        private static final long serialVersionUID = 1;
        private final DateFormat dateFormat;

        public DefaultZoneDateFormat(DateFormat format) {
            this.dateFormat = format;
        }

        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            this.dateFormat.setTimeZone(TimeZone.getDefault());
            return this.dateFormat.format(date, toAppendTo, fieldPosition);
        }

        public Date parse(String source, ParsePosition pos) {
            this.dateFormat.setTimeZone(TimeZone.getDefault());
            return this.dateFormat.parse(source, pos);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0079  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private DatePatternConverter(java.lang.String[] r11) {
        /*
            r10 = this;
            r9 = 1
            java.lang.String r7 = "Date"
            java.lang.String r8 = "date"
            r10.<init>(r7, r8)
            if (r11 == 0) goto L_0x000d
            int r7 = r11.length
            if (r7 != 0) goto L_0x003d
        L_0x000d:
            r3 = 0
        L_0x000e:
            if (r3 == 0) goto L_0x0018
            java.lang.String r7 = "ISO8601"
            boolean r7 = r3.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x0041
        L_0x0018:
            java.lang.String r2 = "yyyy-MM-dd HH:mm:ss,SSS"
        L_0x001a:
            r1 = 1000(0x3e8, float:1.401E-42)
            r4 = 0
            java.text.SimpleDateFormat r5 = new java.text.SimpleDateFormat     // Catch:{ IllegalArgumentException -> 0x0059 }
            r5.<init>(r2)     // Catch:{ IllegalArgumentException -> 0x0059 }
            int r1 = org.apache.log4j.pattern.CachedDateFormat.getMaximumCacheValidity(r2)     // Catch:{ IllegalArgumentException -> 0x007f }
        L_0x0026:
            if (r11 == 0) goto L_0x0079
            int r7 = r11.length
            if (r7 <= r9) goto L_0x0079
            r7 = r11[r9]
            java.util.TimeZone r6 = java.util.TimeZone.getTimeZone(r7)
            r5.setTimeZone(r6)
            r4 = r5
        L_0x0035:
            org.apache.log4j.pattern.CachedDateFormat r7 = new org.apache.log4j.pattern.CachedDateFormat
            r7.<init>(r4, r1)
            r10.df = r7
            return
        L_0x003d:
            r7 = 0
            r3 = r11[r7]
            goto L_0x000e
        L_0x0041:
            java.lang.String r7 = "ABSOLUTE"
            boolean r7 = r3.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x004c
            java.lang.String r2 = "HH:mm:ss,SSS"
            goto L_0x001a
        L_0x004c:
            java.lang.String r7 = "DATE"
            boolean r7 = r3.equalsIgnoreCase(r7)
            if (r7 == 0) goto L_0x0057
            java.lang.String r2 = "dd MMM yyyy HH:mm:ss,SSS"
            goto L_0x001a
        L_0x0057:
            r2 = r3
            goto L_0x001a
        L_0x0059:
            r0 = move-exception
        L_0x005a:
            java.lang.StringBuffer r7 = new java.lang.StringBuffer
            r7.<init>()
            java.lang.String r8 = "Could not instantiate SimpleDateFormat with pattern "
            java.lang.StringBuffer r7 = r7.append(r8)
            java.lang.StringBuffer r7 = r7.append(r3)
            java.lang.String r7 = r7.toString()
            org.apache.log4j.helpers.LogLog.warn(r7, r0)
            java.text.SimpleDateFormat r4 = new java.text.SimpleDateFormat
            java.lang.String r7 = "yyyy-MM-dd HH:mm:ss,SSS"
            r4.<init>(r7)
            r5 = r4
            goto L_0x0026
        L_0x0079:
            org.apache.log4j.pattern.DatePatternConverter$DefaultZoneDateFormat r4 = new org.apache.log4j.pattern.DatePatternConverter$DefaultZoneDateFormat
            r4.<init>(r5)
            goto L_0x0035
        L_0x007f:
            r0 = move-exception
            r4 = r5
            goto L_0x005a
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.pattern.DatePatternConverter.<init>(java.lang.String[]):void");
    }

    public static DatePatternConverter newInstance(String[] options) {
        return new DatePatternConverter(options);
    }

    public void format(LoggingEvent event, StringBuffer output) {
        synchronized (this) {
            this.df.format(event.timeStamp, output);
        }
    }

    public void format(Object obj, StringBuffer output) {
        if (obj instanceof Date) {
            format((Date) obj, output);
        }
        super.format(obj, output);
    }

    public void format(Date date, StringBuffer toAppendTo) {
        synchronized (this) {
            this.df.format(date.getTime(), toAppendTo);
        }
    }
}
