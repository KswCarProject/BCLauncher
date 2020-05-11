package org.apache.log4j;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.NullEnumeration;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.HierarchyEventListener;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

public class Category implements AppenderAttachable {
    private static final String FQCN;
    static Class class$org$apache$log4j$Category;
    AppenderAttachableImpl aai;
    protected boolean additive = true;
    protected volatile Level level;
    protected String name;
    protected volatile Category parent;
    protected LoggerRepository repository;
    protected ResourceBundle resourceBundle;

    static {
        Class cls;
        if (class$org$apache$log4j$Category == null) {
            cls = class$("org.apache.log4j.Category");
            class$org$apache$log4j$Category = cls;
        } else {
            cls = class$org$apache$log4j$Category;
        }
        FQCN = cls.getName();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    protected Category(String name2) {
        this.name = name2;
    }

    public synchronized void addAppender(Appender newAppender) {
        if (this.aai == null) {
            this.aai = new AppenderAttachableImpl();
        }
        this.aai.addAppender(newAppender);
        this.repository.fireAddAppenderEvent(this, newAppender);
    }

    public void assertLog(boolean assertion, String msg) {
        if (!assertion) {
            error(msg);
        }
    }

    public void callAppenders(LoggingEvent event) {
        int writes = 0;
        Category c = this;
        while (true) {
            if (c == null) {
                break;
            }
            synchronized (c) {
                if (c.aai != null) {
                    writes += c.aai.appendLoopOnAppenders(event);
                }
                if (!c.additive) {
                }
            }
            c = c.parent;
        }
        if (writes == 0) {
            this.repository.emitNoAppenderWarning(this);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void closeNestedAppenders() {
        Enumeration enumeration = getAllAppenders();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Appender a = (Appender) enumeration.nextElement();
                if (a instanceof AppenderAttachable) {
                    a.close();
                }
            }
        }
    }

    public void debug(Object message) {
        if (!this.repository.isDisabled(10000) && Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, (Throwable) null);
        }
    }

    public void debug(Object message, Throwable t) {
        if (!this.repository.isDisabled(10000) && Level.DEBUG.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.DEBUG, message, t);
        }
    }

    public void error(Object message) {
        if (!this.repository.isDisabled(Priority.ERROR_INT) && Level.ERROR.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.ERROR, message, (Throwable) null);
        }
    }

    public void error(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.ERROR_INT) && Level.ERROR.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.ERROR, message, t);
        }
    }

    public static Logger exists(String name2) {
        return LogManager.exists(name2);
    }

    public void fatal(Object message) {
        if (!this.repository.isDisabled(Priority.FATAL_INT) && Level.FATAL.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.FATAL, message, (Throwable) null);
        }
    }

    public void fatal(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.FATAL_INT) && Level.FATAL.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.FATAL, message, t);
        }
    }

    /* access modifiers changed from: protected */
    public void forcedLog(String fqcn, Priority level2, Object message, Throwable t) {
        callAppenders(new LoggingEvent(fqcn, this, level2, message, t));
    }

    public boolean getAdditivity() {
        return this.additive;
    }

    public synchronized Enumeration getAllAppenders() {
        Enumeration allAppenders;
        if (this.aai == null) {
            allAppenders = NullEnumeration.getInstance();
        } else {
            allAppenders = this.aai.getAllAppenders();
        }
        return allAppenders;
    }

    public synchronized Appender getAppender(String name2) {
        Appender appender;
        if (this.aai == null || name2 == null) {
            appender = null;
        } else {
            appender = this.aai.getAppender(name2);
        }
        return appender;
    }

    public Level getEffectiveLevel() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.level != null) {
                return c.level;
            }
        }
        return null;
    }

    public Priority getChainedPriority() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.level != null) {
                return c.level;
            }
        }
        return null;
    }

    public static Enumeration getCurrentCategories() {
        return LogManager.getCurrentLoggers();
    }

    public static LoggerRepository getDefaultHierarchy() {
        return LogManager.getLoggerRepository();
    }

    public LoggerRepository getHierarchy() {
        return this.repository;
    }

    public LoggerRepository getLoggerRepository() {
        return this.repository;
    }

    public static Category getInstance(String name2) {
        return LogManager.getLogger(name2);
    }

    public static Category getInstance(Class clazz) {
        return LogManager.getLogger(clazz);
    }

    public final String getName() {
        return this.name;
    }

    public final Category getParent() {
        return this.parent;
    }

    public final Level getLevel() {
        return this.level;
    }

    public final Level getPriority() {
        return this.level;
    }

    public static final Category getRoot() {
        return LogManager.getRootLogger();
    }

    public ResourceBundle getResourceBundle() {
        for (Category c = this; c != null; c = c.parent) {
            if (c.resourceBundle != null) {
                return c.resourceBundle;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getResourceBundleString(String key) {
        ResourceBundle rb = getResourceBundle();
        if (rb == null) {
            return null;
        }
        try {
            return rb.getString(key);
        } catch (MissingResourceException e) {
            error(new StringBuffer().append("No resource is associated with key \"").append(key).append("\".").toString());
            return null;
        }
    }

    public void info(Object message) {
        if (!this.repository.isDisabled(Priority.INFO_INT) && Level.INFO.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.INFO, message, (Throwable) null);
        }
    }

    public void info(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.INFO_INT) && Level.INFO.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.INFO, message, t);
        }
    }

    public boolean isAttached(Appender appender) {
        if (appender == null || this.aai == null) {
            return false;
        }
        return this.aai.isAttached(appender);
    }

    public boolean isDebugEnabled() {
        if (this.repository.isDisabled(10000)) {
            return false;
        }
        return Level.DEBUG.isGreaterOrEqual(getEffectiveLevel());
    }

    public boolean isEnabledFor(Priority level2) {
        if (this.repository.isDisabled(level2.level)) {
            return false;
        }
        return level2.isGreaterOrEqual(getEffectiveLevel());
    }

    public boolean isInfoEnabled() {
        if (this.repository.isDisabled(Priority.INFO_INT)) {
            return false;
        }
        return Level.INFO.isGreaterOrEqual(getEffectiveLevel());
    }

    public void l7dlog(Priority priority, String key, Throwable t) {
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            String msg = getResourceBundleString(key);
            if (msg == null) {
                msg = key;
            }
            forcedLog(FQCN, priority, msg, t);
        }
    }

    public void l7dlog(Priority priority, String key, Object[] params, Throwable t) {
        String msg;
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            String pattern = getResourceBundleString(key);
            if (pattern == null) {
                msg = key;
            } else {
                msg = MessageFormat.format(pattern, params);
            }
            forcedLog(FQCN, priority, msg, t);
        }
    }

    public void log(Priority priority, Object message, Throwable t) {
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, priority, message, t);
        }
    }

    public void log(Priority priority, Object message) {
        if (!this.repository.isDisabled(priority.level) && priority.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, priority, message, (Throwable) null);
        }
    }

    public void log(String callerFQCN, Priority level2, Object message, Throwable t) {
        if (!this.repository.isDisabled(level2.level) && level2.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(callerFQCN, level2, message, t);
        }
    }

    private void fireRemoveAppenderEvent(Appender appender) {
        if (appender == null) {
            return;
        }
        if (this.repository instanceof Hierarchy) {
            ((Hierarchy) this.repository).fireRemoveAppenderEvent(this, appender);
        } else if (this.repository instanceof HierarchyEventListener) {
            ((HierarchyEventListener) this.repository).removeAppenderEvent(this, appender);
        }
    }

    public synchronized void removeAllAppenders() {
        if (this.aai != null) {
            Vector appenders = new Vector();
            Enumeration iter = this.aai.getAllAppenders();
            while (iter != null && iter.hasMoreElements()) {
                appenders.add(iter.nextElement());
            }
            this.aai.removeAllAppenders();
            Enumeration iter2 = appenders.elements();
            while (iter2.hasMoreElements()) {
                fireRemoveAppenderEvent((Appender) iter2.nextElement());
            }
            this.aai = null;
        }
    }

    public synchronized void removeAppender(Appender appender) {
        if (appender != null) {
            if (this.aai != null) {
                boolean wasAttached = this.aai.isAttached(appender);
                this.aai.removeAppender(appender);
                if (wasAttached) {
                    fireRemoveAppenderEvent(appender);
                }
            }
        }
    }

    public synchronized void removeAppender(String name2) {
        if (name2 != null) {
            if (this.aai != null) {
                Appender appender = this.aai.getAppender(name2);
                this.aai.removeAppender(name2);
                if (appender != null) {
                    fireRemoveAppenderEvent(appender);
                }
            }
        }
    }

    public void setAdditivity(boolean additive2) {
        this.additive = additive2;
    }

    /* access modifiers changed from: package-private */
    public final void setHierarchy(LoggerRepository repository2) {
        this.repository = repository2;
    }

    public void setLevel(Level level2) {
        this.level = level2;
    }

    public void setPriority(Priority priority) {
        this.level = (Level) priority;
    }

    public void setResourceBundle(ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }

    public static void shutdown() {
        LogManager.shutdown();
    }

    public void warn(Object message) {
        if (!this.repository.isDisabled(Priority.WARN_INT) && Level.WARN.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.WARN, message, (Throwable) null);
        }
    }

    public void warn(Object message, Throwable t) {
        if (!this.repository.isDisabled(Priority.WARN_INT) && Level.WARN.isGreaterOrEqual(getEffectiveLevel())) {
            forcedLog(FQCN, Level.WARN, message, t);
        }
    }
}
