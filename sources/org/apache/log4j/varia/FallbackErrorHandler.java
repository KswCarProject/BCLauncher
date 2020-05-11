package org.apache.log4j.varia;

import java.io.InterruptedIOException;
import java.util.Vector;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;

public class FallbackErrorHandler implements ErrorHandler {
    Appender backup;
    Vector loggers;
    Appender primary;

    public void setLogger(Logger logger) {
        LogLog.debug(new StringBuffer().append("FB: Adding logger [").append(logger.getName()).append("].").toString());
        if (this.loggers == null) {
            this.loggers = new Vector();
        }
        this.loggers.addElement(logger);
    }

    public void activateOptions() {
    }

    public void error(String message, Exception e, int errorCode) {
        error(message, e, errorCode, (LoggingEvent) null);
    }

    public void error(String message, Exception e, int errorCode, LoggingEvent event) {
        if (e instanceof InterruptedIOException) {
            Thread.currentThread().interrupt();
        }
        LogLog.debug(new StringBuffer().append("FB: The following error reported: ").append(message).toString(), e);
        LogLog.debug("FB: INITIATING FALLBACK PROCEDURE.");
        if (this.loggers != null) {
            for (int i = 0; i < this.loggers.size(); i++) {
                Logger l = (Logger) this.loggers.elementAt(i);
                LogLog.debug(new StringBuffer().append("FB: Searching for [").append(this.primary.getName()).append("] in logger [").append(l.getName()).append("].").toString());
                LogLog.debug(new StringBuffer().append("FB: Replacing [").append(this.primary.getName()).append("] by [").append(this.backup.getName()).append("] in logger [").append(l.getName()).append("].").toString());
                l.removeAppender(this.primary);
                LogLog.debug(new StringBuffer().append("FB: Adding appender [").append(this.backup.getName()).append("] to logger ").append(l.getName()).toString());
                l.addAppender(this.backup);
            }
        }
    }

    public void error(String message) {
    }

    public void setAppender(Appender primary2) {
        LogLog.debug(new StringBuffer().append("FB: Setting primary appender to [").append(primary2.getName()).append("].").toString());
        this.primary = primary2;
    }

    public void setBackupAppender(Appender backup2) {
        LogLog.debug(new StringBuffer().append("FB: Setting backup appender to [").append(backup2.getName()).append("].").toString());
        this.backup = backup2;
    }
}
