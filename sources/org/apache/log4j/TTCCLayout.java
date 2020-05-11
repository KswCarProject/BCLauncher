package org.apache.log4j;

import java.util.TimeZone;
import org.apache.log4j.helpers.DateLayout;
import org.apache.log4j.spi.LoggingEvent;

public class TTCCLayout extends DateLayout {
    protected final StringBuffer buf = new StringBuffer(256);
    private boolean categoryPrefixing = true;
    private boolean contextPrinting = true;
    private boolean threadPrinting = true;

    public TTCCLayout() {
        setDateFormat(DateLayout.RELATIVE_TIME_DATE_FORMAT, (TimeZone) null);
    }

    public TTCCLayout(String dateFormatType) {
        setDateFormat(dateFormatType);
    }

    public void setThreadPrinting(boolean threadPrinting2) {
        this.threadPrinting = threadPrinting2;
    }

    public boolean getThreadPrinting() {
        return this.threadPrinting;
    }

    public void setCategoryPrefixing(boolean categoryPrefixing2) {
        this.categoryPrefixing = categoryPrefixing2;
    }

    public boolean getCategoryPrefixing() {
        return this.categoryPrefixing;
    }

    public void setContextPrinting(boolean contextPrinting2) {
        this.contextPrinting = contextPrinting2;
    }

    public boolean getContextPrinting() {
        return this.contextPrinting;
    }

    public String format(LoggingEvent event) {
        String ndc;
        this.buf.setLength(0);
        dateFormat(this.buf, event);
        if (this.threadPrinting) {
            this.buf.append('[');
            this.buf.append(event.getThreadName());
            this.buf.append("] ");
        }
        this.buf.append(event.getLevel().toString());
        this.buf.append(' ');
        if (this.categoryPrefixing) {
            this.buf.append(event.getLoggerName());
            this.buf.append(' ');
        }
        if (this.contextPrinting && (ndc = event.getNDC()) != null) {
            this.buf.append(ndc);
            this.buf.append(' ');
        }
        this.buf.append("- ");
        this.buf.append(event.getRenderedMessage());
        this.buf.append(LINE_SEP);
        return this.buf.toString();
    }

    public boolean ignoresThrowable() {
        return true;
    }
}
