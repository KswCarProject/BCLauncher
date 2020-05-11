package org.apache.log4j.spi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Method;
import org.apache.log4j.helpers.LogLog;

public class LocationInfo implements Serializable {
    public static final String NA = "?";
    public static final LocationInfo NA_LOCATION_INFO = new LocationInfo(NA, NA, NA, NA);
    static Class class$java$lang$Throwable = null;
    private static Method getClassNameMethod = null;
    private static Method getFileNameMethod = null;
    private static Method getLineNumberMethod = null;
    private static Method getMethodNameMethod = null;
    private static Method getStackTraceMethod = null;
    static boolean inVisualAge = false;
    private static PrintWriter pw = new PrintWriter(sw);
    static final long serialVersionUID = -1325822038990805636L;
    private static StringWriter sw = new StringWriter();
    transient String className;
    transient String fileName;
    public String fullInfo;
    transient String lineNumber;
    transient String methodName;

    static {
        Class cls;
        boolean z = false;
        inVisualAge = false;
        try {
            if (Class.forName("com.ibm.uvm.tools.DebugSupport") != null) {
                z = true;
            }
            inVisualAge = z;
            LogLog.debug("Detected IBM VisualAge environment.");
        } catch (Throwable th) {
        }
        try {
            if (class$java$lang$Throwable == null) {
                cls = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls;
            } else {
                cls = class$java$lang$Throwable;
            }
            getStackTraceMethod = cls.getMethod("getStackTrace", (Class[]) null);
            Class stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
            getClassNameMethod = stackTraceElementClass.getMethod("getClassName", (Class[]) null);
            getMethodNameMethod = stackTraceElementClass.getMethod("getMethodName", (Class[]) null);
            getFileNameMethod = stackTraceElementClass.getMethod("getFileName", (Class[]) null);
            getLineNumberMethod = stackTraceElementClass.getMethod("getLineNumber", (Class[]) null);
        } catch (ClassNotFoundException e) {
            LogLog.debug("LocationInfo will use pre-JDK 1.4 methods to determine location.");
        } catch (NoSuchMethodException e2) {
            LogLog.debug("LocationInfo will use pre-JDK 1.4 methods to determine location.");
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0122, code lost:
        r6 = r6 + org.apache.log4j.Layout.LINE_SEP_LEN;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public LocationInfo(java.lang.Throwable r17, java.lang.String r18) {
        /*
            r16 = this;
            r16.<init>()
            if (r17 == 0) goto L_0x0007
            if (r18 != 0) goto L_0x0008
        L_0x0007:
            return
        L_0x0008:
            java.lang.reflect.Method r13 = getLineNumberMethod
            if (r13 == 0) goto L_0x00c0
            r9 = 0
            java.lang.reflect.Method r13 = getStackTraceMethod     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r17
            java.lang.Object r13 = r13.invoke(r0, r9)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.Object[] r13 = (java.lang.Object[]) r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r13
            java.lang.Object[] r0 = (java.lang.Object[]) r0     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r3 = r0
            java.lang.String r10 = "?"
            int r13 = r3.length     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            int r5 = r13 + -1
        L_0x0020:
            if (r5 < 0) goto L_0x0007
            java.lang.reflect.Method r13 = getClassNameMethod     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r14 = r3[r5]     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.Object r12 = r13.invoke(r14, r9)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r18
            boolean r13 = r0.equals(r12)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            if (r13 == 0) goto L_0x0170
            int r2 = r5 + 1
            int r13 = r3.length     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            if (r2 >= r13) goto L_0x0007
            r0 = r16
            r0.className = r10     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.reflect.Method r13 = getMethodNameMethod     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r14 = r3[r2]     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.Object r13 = r13.invoke(r14, r9)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            r0.methodName = r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.reflect.Method r13 = getFileNameMethod     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r14 = r3[r2]     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.Object r13 = r13.invoke(r14, r9)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            r0.fileName = r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            java.lang.String r13 = r0.fileName     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            if (r13 != 0) goto L_0x0065
            java.lang.String r13 = "?"
            r0 = r16
            r0.fileName = r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
        L_0x0065:
            java.lang.reflect.Method r13 = getLineNumberMethod     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r14 = r3[r2]     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.Object r13 = r13.invoke(r14, r9)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.Integer r13 = (java.lang.Integer) r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            int r8 = r13.intValue()     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            if (r8 >= 0) goto L_0x0147
            java.lang.String r13 = "?"
            r0 = r16
            r0.lineNumber = r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
        L_0x007b:
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r1.<init>()     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            java.lang.String r13 = r0.className     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r13 = "."
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            java.lang.String r13 = r0.methodName     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r13 = "("
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            java.lang.String r13 = r0.fileName     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r13 = ":"
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            java.lang.String r13 = r0.lineNumber     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r13 = ")"
            r1.append(r13)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            java.lang.String r13 = r1.toString()     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            r0.fullInfo = r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            goto L_0x0007
        L_0x00ba:
            r4 = move-exception
            java.lang.String r13 = "LocationInfo failed using JDK 1.4 methods"
            org.apache.log4j.helpers.LogLog.debug(r13, r4)
        L_0x00c0:
            java.io.StringWriter r14 = sw
            monitor-enter(r14)
            java.io.PrintWriter r13 = pw     // Catch:{ all -> 0x017d }
            r0 = r17
            r0.printStackTrace(r13)     // Catch:{ all -> 0x017d }
            java.io.StringWriter r13 = sw     // Catch:{ all -> 0x017d }
            java.lang.String r11 = r13.toString()     // Catch:{ all -> 0x017d }
            java.io.StringWriter r13 = sw     // Catch:{ all -> 0x017d }
            java.lang.StringBuffer r13 = r13.getBuffer()     // Catch:{ all -> 0x017d }
            r15 = 0
            r13.setLength(r15)     // Catch:{ all -> 0x017d }
            monitor-exit(r14)     // Catch:{ all -> 0x017d }
            r0 = r18
            int r6 = r11.lastIndexOf(r0)
            r13 = -1
            if (r6 == r13) goto L_0x0007
            int r13 = r18.length()
            int r13 = r13 + r6
            int r14 = r11.length()
            if (r13 >= r14) goto L_0x0119
            int r13 = r18.length()
            int r13 = r13 + r6
            char r13 = r11.charAt(r13)
            r14 = 46
            if (r13 == r14) goto L_0x0119
            java.lang.StringBuffer r13 = new java.lang.StringBuffer
            r13.<init>()
            r0 = r18
            java.lang.StringBuffer r13 = r13.append(r0)
            java.lang.String r14 = "."
            java.lang.StringBuffer r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            int r5 = r11.lastIndexOf(r13)
            r13 = -1
            if (r5 == r13) goto L_0x0119
            r6 = r5
        L_0x0119:
            java.lang.String r13 = org.apache.log4j.Layout.LINE_SEP
            int r6 = r11.indexOf(r13, r6)
            r13 = -1
            if (r6 == r13) goto L_0x0007
            int r13 = org.apache.log4j.Layout.LINE_SEP_LEN
            int r6 = r6 + r13
            java.lang.String r13 = org.apache.log4j.Layout.LINE_SEP
            int r7 = r11.indexOf(r13, r6)
            r13 = -1
            if (r7 == r13) goto L_0x0007
            boolean r13 = inVisualAge
            if (r13 != 0) goto L_0x013d
            java.lang.String r13 = "at "
            int r6 = r11.lastIndexOf(r13, r7)
            r13 = -1
            if (r6 == r13) goto L_0x0007
            int r6 = r6 + 3
        L_0x013d:
            java.lang.String r13 = r11.substring(r6, r7)
            r0 = r16
            r0.fullInfo = r13
            goto L_0x0007
        L_0x0147:
            java.lang.String r13 = java.lang.String.valueOf(r8)     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            r0 = r16
            r0.lineNumber = r13     // Catch:{ IllegalAccessException -> 0x00ba, InvocationTargetException -> 0x0151, RuntimeException -> 0x0175 }
            goto L_0x007b
        L_0x0151:
            r4 = move-exception
            java.lang.Throwable r13 = r4.getTargetException()
            boolean r13 = r13 instanceof java.lang.InterruptedException
            if (r13 != 0) goto L_0x0162
            java.lang.Throwable r13 = r4.getTargetException()
            boolean r13 = r13 instanceof java.io.InterruptedIOException
            if (r13 == 0) goto L_0x0169
        L_0x0162:
            java.lang.Thread r13 = java.lang.Thread.currentThread()
            r13.interrupt()
        L_0x0169:
            java.lang.String r13 = "LocationInfo failed using JDK 1.4 methods"
            org.apache.log4j.helpers.LogLog.debug(r13, r4)
            goto L_0x00c0
        L_0x0170:
            r10 = r12
            int r5 = r5 + -1
            goto L_0x0020
        L_0x0175:
            r4 = move-exception
            java.lang.String r13 = "LocationInfo failed using JDK 1.4 methods"
            org.apache.log4j.helpers.LogLog.debug(r13, r4)
            goto L_0x00c0
        L_0x017d:
            r13 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x017d }
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.spi.LocationInfo.<init>(java.lang.Throwable, java.lang.String):void");
    }

    private static final void appendFragment(StringBuffer buf, String fragment) {
        if (fragment == null) {
            buf.append(NA);
        } else {
            buf.append(fragment);
        }
    }

    public LocationInfo(String file, String classname, String method, String line) {
        this.fileName = file;
        this.className = classname;
        this.methodName = method;
        this.lineNumber = line;
        StringBuffer buf = new StringBuffer();
        appendFragment(buf, classname);
        buf.append(".");
        appendFragment(buf, method);
        buf.append("(");
        appendFragment(buf, file);
        buf.append(":");
        appendFragment(buf, line);
        buf.append(")");
        this.fullInfo = buf.toString();
    }

    public String getClassName() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.className == null) {
            int iend = this.fullInfo.lastIndexOf(40);
            if (iend == -1) {
                this.className = NA;
            } else {
                int iend2 = this.fullInfo.lastIndexOf(46, iend);
                int ibegin = 0;
                if (inVisualAge) {
                    ibegin = this.fullInfo.lastIndexOf(32, iend2) + 1;
                }
                if (iend2 == -1) {
                    this.className = NA;
                } else {
                    this.className = this.fullInfo.substring(ibegin, iend2);
                }
            }
        }
        return this.className;
    }

    public String getFileName() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.fileName == null) {
            int iend = this.fullInfo.lastIndexOf(58);
            if (iend == -1) {
                this.fileName = NA;
            } else {
                this.fileName = this.fullInfo.substring(this.fullInfo.lastIndexOf(40, iend - 1) + 1, iend);
            }
        }
        return this.fileName;
    }

    public String getLineNumber() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.lineNumber == null) {
            int iend = this.fullInfo.lastIndexOf(41);
            int ibegin = this.fullInfo.lastIndexOf(58, iend - 1);
            if (ibegin == -1) {
                this.lineNumber = NA;
            } else {
                this.lineNumber = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return this.lineNumber;
    }

    public String getMethodName() {
        if (this.fullInfo == null) {
            return NA;
        }
        if (this.methodName == null) {
            int iend = this.fullInfo.lastIndexOf(40);
            int ibegin = this.fullInfo.lastIndexOf(46, iend);
            if (ibegin == -1) {
                this.methodName = NA;
            } else {
                this.methodName = this.fullInfo.substring(ibegin + 1, iend);
            }
        }
        return this.methodName;
    }
}
