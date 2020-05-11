package de.mindpipe.android.logging.log4j;

import android.util.Log;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

public class LogCatAppender extends AppenderSkeleton {
    protected Layout tagLayout;

    public LogCatAppender(Layout messageLayout, Layout tagLayout2) {
        this.tagLayout = tagLayout2;
        setLayout(messageLayout);
    }

    public LogCatAppender(Layout messageLayout) {
        this(messageLayout, new PatternLayout("%c"));
    }

    public LogCatAppender() {
        this(new PatternLayout("%m%n"));
    }

    /* access modifiers changed from: protected */
    public void append(LoggingEvent le) {
        switch (le.getLevel().toInt()) {
            case Level.TRACE_INT /*5000*/:
                if (le.getThrowableInformation() != null) {
                    Log.v(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                    return;
                } else {
                    Log.v(getTagLayout().format(le), getLayout().format(le));
                    return;
                }
            case 10000:
                if (le.getThrowableInformation() != null) {
                    Log.d(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                    return;
                } else {
                    Log.d(getTagLayout().format(le), getLayout().format(le));
                    return;
                }
            case Priority.INFO_INT /*20000*/:
                if (le.getThrowableInformation() != null) {
                    Log.i(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                    return;
                } else {
                    Log.i(getTagLayout().format(le), getLayout().format(le));
                    return;
                }
            case Priority.WARN_INT /*30000*/:
                if (le.getThrowableInformation() != null) {
                    Log.w(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                    return;
                } else {
                    Log.w(getTagLayout().format(le), getLayout().format(le));
                    return;
                }
            case Priority.ERROR_INT /*40000*/:
                if (le.getThrowableInformation() != null) {
                    Log.e(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                    return;
                } else {
                    Log.e(getTagLayout().format(le), getLayout().format(le));
                    return;
                }
            case Priority.FATAL_INT /*50000*/:
                if (le.getThrowableInformation() != null) {
                    Log.wtf(getTagLayout().format(le), getLayout().format(le), le.getThrowableInformation().getThrowable());
                    return;
                } else {
                    Log.wtf(getTagLayout().format(le), getLayout().format(le));
                    return;
                }
            default:
                return;
        }
    }

    public void close() {
    }

    public boolean requiresLayout() {
        return true;
    }

    public Layout getTagLayout() {
        return this.tagLayout;
    }

    public void setTagLayout(Layout tagLayout2) {
        this.tagLayout = tagLayout2;
    }
}
