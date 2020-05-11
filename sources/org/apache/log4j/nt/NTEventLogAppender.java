package org.apache.log4j.nt;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class NTEventLogAppender extends AppenderSkeleton {
    private int _handle;
    private String server;
    private String source;

    private native void deregisterEventSource(int i);

    private native int registerEventSource(String str, String str2);

    private native void reportEvent(int i, String str, int i2);

    public NTEventLogAppender() {
        this((String) null, (String) null, (Layout) null);
    }

    public NTEventLogAppender(String source2) {
        this((String) null, source2, (Layout) null);
    }

    public NTEventLogAppender(String server2, String source2) {
        this(server2, source2, (Layout) null);
    }

    public NTEventLogAppender(Layout layout) {
        this((String) null, (String) null, layout);
    }

    public NTEventLogAppender(String source2, Layout layout) {
        this((String) null, source2, layout);
    }

    public NTEventLogAppender(String server2, String source2, Layout layout) {
        this._handle = 0;
        this.source = null;
        this.server = null;
        source2 = source2 == null ? "Log4j" : source2;
        if (layout == null) {
            this.layout = new TTCCLayout();
        } else {
            this.layout = layout;
        }
        try {
            this._handle = registerEventSource(server2, source2);
        } catch (Exception e) {
            e.printStackTrace();
            this._handle = 0;
        }
    }

    public void close() {
    }

    public void activateOptions() {
        if (this.source != null) {
            try {
                this._handle = registerEventSource(this.server, this.source);
            } catch (Exception e) {
                LogLog.error("Could not register event source.", e);
                this._handle = 0;
            }
        }
    }

    public void append(LoggingEvent event) {
        String[] s;
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(this.layout.format(event));
        if (this.layout.ignoresThrowable() && (s = event.getThrowableStrRep()) != null) {
            for (String append : s) {
                sbuf.append(append);
            }
        }
        reportEvent(this._handle, sbuf.toString(), event.getLevel().toInt());
    }

    public void finalize() {
        deregisterEventSource(this._handle);
        this._handle = 0;
    }

    public void setSource(String source2) {
        this.source = source2.trim();
    }

    public String getSource() {
        return this.source;
    }

    public boolean requiresLayout() {
        return true;
    }

    static {
        String[] archs;
        try {
            archs = new String[]{System.getProperty("os.arch")};
        } catch (SecurityException e) {
            archs = new String[]{"amd64", "ia64", "x86"};
        }
        boolean loaded = false;
        int i = 0;
        while (true) {
            if (i >= archs.length) {
                break;
            }
            try {
                System.loadLibrary(new StringBuffer().append("NTEventLogAppender.").append(archs[i]).toString());
                loaded = true;
                break;
            } catch (UnsatisfiedLinkError e2) {
                loaded = false;
                i++;
            }
        }
        if (!loaded) {
            System.loadLibrary("NTEventLogAppender");
        }
    }
}
