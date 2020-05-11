package com.touchus.publicutils.utils;

import android.os.SystemClock;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    static SimpleDateFormat simpleCallDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static String getSimpleDate(String formatString) {
        return new SimpleDateFormat(formatString).format(new Date());
    }

    public static String getSimpleDate() {
        return getSimpleDateFormat().format(new Date());
    }

    public static String getSimpleCallDate() {
        return simpleCallDateFormat.format(new Date());
    }

    public static String getSimpleDate(Date date) {
        return getSimpleDateFormat().format(date);
    }

    public static long getIntervalTime(Date old_date, Date new_date) {
        try {
            return new_date.getTime() - old_date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int dateTimeCompara(String time1, String time2) throws ParseException {
        Date dt1 = simpleDateFormat.parse(time1);
        Date dt2 = simpleDateFormat.parse(time2);
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        }
        if (dt1.getTime() < dt2.getTime()) {
            return -1;
        }
        return 0;
    }

    public static String intToString(int time) {
        String str;
        String str2 = "";
        if (time <= 0) {
            return str2;
        }
        int hours = time / 3600;
        int minutes = (time - (hours * 3600)) / 60;
        int second = time % 60;
        if (hours > 0) {
            str2 = String.valueOf(hours) + ":";
        }
        if (minutes <= 0 && hours <= 0) {
            str = "00:";
        } else if (minutes >= 10) {
            str = String.valueOf(str2) + hours + ":";
        } else if (minutes == 0) {
            str = String.valueOf(str2) + "00:";
        } else {
            str = String.valueOf(str2) + "0" + minutes + ":";
        }
        if (second <= 0 && minutes <= 0) {
            return str;
        }
        if (second >= 10) {
            return String.valueOf(str) + second;
        }
        if (second == 0) {
            return String.valueOf(str) + "00";
        }
        return String.valueOf(str) + "0" + second;
    }

    public static String secToTimeString(long second) {
        String mHour;
        String mMinute;
        long hour = second / 3600;
        if (hour == 0) {
            long minute = second / 60;
            if (minute < 10) {
                mMinute = "0" + minute;
            } else {
                mMinute = new StringBuilder().append(minute).toString();
            }
            if (second % 60 < 10) {
                return String.valueOf(mMinute) + " : 0" + (second % 60);
            }
            return String.valueOf(mMinute) + " : " + (second % 60);
        }
        if (hour < 10) {
            mHour = "0" + hour;
        } else {
            mHour = new StringBuilder().append(hour).toString();
        }
        return String.valueOf(mHour) + " : " + secToTimeString(second % 3600);
    }

    public static void setTime(int hour, int minute) throws IOException, InterruptedException {
        requestPermission();
        Calendar c = Calendar.getInstance();
        c.set(11, hour);
        c.set(12, minute);
        long when = c.getTimeInMillis();
        if (when / 1000 < 2147483647L) {
            SystemClock.setCurrentTimeMillis(when);
        }
        if (Calendar.getInstance().getTimeInMillis() - when > 1000) {
            throw new IOException("failed to set Time.");
        }
    }

    public static void requestPermission() throws InterruptedException, IOException {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    public static Process createSuProcess() throws IOException {
        File rootUser = new File("/system/xbin/ru");
        if (rootUser.exists()) {
            return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
        }
        return Runtime.getRuntime().exec("su");
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0032 A[SYNTHETIC, Splitter:B:11:0x0032] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Process createSuProcess(java.lang.String r5) throws java.io.IOException {
        /*
            r0 = 0
            java.lang.Process r2 = createSuProcess()
            java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ all -> 0x002f }
            java.io.OutputStream r3 = r2.getOutputStream()     // Catch:{ all -> 0x002f }
            r1.<init>(r3)     // Catch:{ all -> 0x002f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x003a }
            java.lang.String r4 = java.lang.String.valueOf(r5)     // Catch:{ all -> 0x003a }
            r3.<init>(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r4 = "\n"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x003a }
            r1.writeBytes(r3)     // Catch:{ all -> 0x003a }
            java.lang.String r3 = "exit $?\n"
            r1.writeBytes(r3)     // Catch:{ all -> 0x003a }
            if (r1 == 0) goto L_0x002e
            r1.close()     // Catch:{ IOException -> 0x0038 }
        L_0x002e:
            return r2
        L_0x002f:
            r3 = move-exception
        L_0x0030:
            if (r0 == 0) goto L_0x0035
            r0.close()     // Catch:{ IOException -> 0x0036 }
        L_0x0035:
            throw r3
        L_0x0036:
            r4 = move-exception
            goto L_0x0035
        L_0x0038:
            r3 = move-exception
            goto L_0x002e
        L_0x003a:
            r3 = move-exception
            r0 = r1
            goto L_0x0030
        */
        throw new UnsupportedOperationException("Method not decompiled: com.touchus.publicutils.utils.TimeUtils.createSuProcess(java.lang.String):java.lang.Process");
    }
}
