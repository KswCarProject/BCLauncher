package a_vcard.android.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    private Log() {
    }

    public static int v(String tag, String msg) {
        return println(2, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return println(2, tag, msg + 10 + getStackTraceString(tr));
    }

    public static int d(String tag, String msg) {
        return println(3, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return println(3, tag, msg + 10 + getStackTraceString(tr));
    }

    public static int i(String tag, String msg) {
        return println(4, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return println(4, tag, msg + 10 + getStackTraceString(tr));
    }

    public static int w(String tag, String msg) {
        return println(5, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return println(5, tag, msg + 10 + getStackTraceString(tr));
    }

    public static boolean isLoggable(String tag, int level) {
        return true;
    }

    public static int w(String tag, Throwable tr) {
        return println(5, tag, getStackTraceString(tr));
    }

    public static int e(String tag, String msg) {
        return println(6, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return println(6, tag, msg + 10 + getStackTraceString(tr));
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        tr.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static int println(int priority, String tag, String msg) {
        logger.logp(prioToLevel(priority), tag, (String) null, msg);
        return 1;
    }

    private static Level prioToLevel(int priority) {
        switch (priority) {
            case 2:
                return Level.ALL;
            case 3:
                return Level.FINEST;
            case 4:
                return Level.INFO;
            case 5:
                return Level.WARNING;
            case 6:
                return Level.SEVERE;
            case 7:
                return Level.ALL;
            default:
                return Level.WARNING;
        }
    }
}
