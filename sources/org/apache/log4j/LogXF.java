package org.apache.log4j;

import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public abstract class LogXF {
    private static final String FQCN;
    protected static final Level TRACE = new Level(Level.TRACE_INT, "TRACE", 7);
    static Class class$org$apache$log4j$LogXF;

    static {
        Class cls;
        if (class$org$apache$log4j$LogXF == null) {
            cls = class$("org.apache.log4j.LogXF");
            class$org$apache$log4j$LogXF = cls;
        } else {
            cls = class$org$apache$log4j$LogXF;
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

    protected LogXF() {
    }

    protected static Boolean valueOf(boolean b) {
        if (b) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    protected static Character valueOf(char c) {
        return new Character(c);
    }

    protected static Byte valueOf(byte b) {
        return new Byte(b);
    }

    protected static Short valueOf(short b) {
        return new Short(b);
    }

    protected static Integer valueOf(int b) {
        return new Integer(b);
    }

    protected static Long valueOf(long b) {
        return new Long(b);
    }

    protected static Float valueOf(float b) {
        return new Float(b);
    }

    protected static Double valueOf(double b) {
        return new Double(b);
    }

    protected static Object[] toArray(Object param1) {
        return new Object[]{param1};
    }

    protected static Object[] toArray(Object param1, Object param2) {
        return new Object[]{param1, param2};
    }

    protected static Object[] toArray(Object param1, Object param2, Object param3) {
        return new Object[]{param1, param2, param3};
    }

    protected static Object[] toArray(Object param1, Object param2, Object param3, Object param4) {
        return new Object[]{param1, param2, param3, param4};
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" ENTRY").toString(), (Throwable) null));
        }
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod, String param) {
        if (logger.isDebugEnabled()) {
            Logger logger2 = logger;
            logger.callAppenders(new LoggingEvent(FQCN, logger2, Level.DEBUG, new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" ENTRY ").append(param).toString(), (Throwable) null));
        }
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod, Object param) {
        String msg;
        if (logger.isDebugEnabled()) {
            String msg2 = new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" ENTRY ").toString();
            if (param == null) {
                msg = new StringBuffer().append(msg2).append(Configurator.NULL).toString();
            } else {
                try {
                    msg = new StringBuffer().append(msg2).append(param).toString();
                } catch (Throwable th) {
                    msg = new StringBuffer().append(msg2).append(LocationInfo.NA).toString();
                }
            }
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, (Throwable) null));
        }
    }

    public static void entering(Logger logger, String sourceClass, String sourceMethod, Object[] params) {
        String msg;
        if (logger.isDebugEnabled()) {
            String msg2 = new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" ENTRY ").toString();
            if (params == null || params.length <= 0) {
                msg = new StringBuffer().append(msg2).append("{}").toString();
            } else {
                String delim = "{";
                for (int i = 0; i < params.length; i++) {
                    try {
                        msg2 = new StringBuffer().append(msg2).append(delim).append(params[i]).toString();
                    } catch (Throwable th) {
                        msg2 = new StringBuffer().append(msg2).append(delim).append(LocationInfo.NA).toString();
                    }
                    delim = ",";
                }
                msg = new StringBuffer().append(msg2).append("}").toString();
            }
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, (Throwable) null));
        }
    }

    public static void exiting(Logger logger, String sourceClass, String sourceMethod) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" RETURN").toString(), (Throwable) null));
        }
    }

    public static void exiting(Logger logger, String sourceClass, String sourceMethod, String result) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" RETURN ").append(result).toString(), (Throwable) null));
        }
    }

    public static void exiting(Logger logger, String sourceClass, String sourceMethod, Object result) {
        String msg;
        if (logger.isDebugEnabled()) {
            String msg2 = new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" RETURN ").toString();
            if (result == null) {
                msg = new StringBuffer().append(msg2).append(Configurator.NULL).toString();
            } else {
                try {
                    msg = new StringBuffer().append(msg2).append(result).toString();
                } catch (Throwable th) {
                    msg = new StringBuffer().append(msg2).append(LocationInfo.NA).toString();
                }
            }
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, msg, (Throwable) null));
        }
    }

    public static void throwing(Logger logger, String sourceClass, String sourceMethod, Throwable thrown) {
        if (logger.isDebugEnabled()) {
            logger.callAppenders(new LoggingEvent(FQCN, logger, Level.DEBUG, new StringBuffer().append(sourceClass).append(".").append(sourceMethod).append(" THROW").toString(), thrown));
        }
    }
}
