package org.apache.log4j.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import org.apache.log4j.pattern.LogEvent;
import org.apache.log4j.spi.LoggingEvent;

public final class MDCKeySetExtractor {
    public static final MDCKeySetExtractor INSTANCE = new MDCKeySetExtractor();
    static Class class$org$apache$log4j$pattern$LogEvent;
    static Class class$org$apache$log4j$spi$LoggingEvent;
    private final Method getKeySetMethod;

    private MDCKeySetExtractor() {
        Method getMethod;
        Class cls;
        try {
            if (class$org$apache$log4j$spi$LoggingEvent == null) {
                cls = class$("org.apache.log4j.spi.LoggingEvent");
                class$org$apache$log4j$spi$LoggingEvent = cls;
            } else {
                cls = class$org$apache$log4j$spi$LoggingEvent;
            }
            getMethod = cls.getMethod("getPropertyKeySet", (Class[]) null);
        } catch (Exception e) {
            getMethod = null;
        }
        this.getKeySetMethod = getMethod;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public Set getPropertyKeySet(LoggingEvent event) throws Exception {
        Class cls;
        Set keySet = null;
        if (this.getKeySetMethod != null) {
            return (Set) this.getKeySetMethod.invoke(event, (Object[]) null);
        }
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outBytes);
        os.writeObject(event);
        os.close();
        byte[] raw = outBytes.toByteArray();
        if (class$org$apache$log4j$pattern$LogEvent == null) {
            cls = class$("org.apache.log4j.pattern.LogEvent");
            class$org$apache$log4j$pattern$LogEvent = cls;
        } else {
            cls = class$org$apache$log4j$pattern$LogEvent;
        }
        String subClassName = cls.getName();
        if (raw[6] != 0 && raw[7] != subClassName.length()) {
            return null;
        }
        for (int i = 0; i < subClassName.length(); i++) {
            raw[i + 8] = (byte) subClassName.charAt(i);
        }
        ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(raw));
        Object cracked = is.readObject();
        if (cracked instanceof LogEvent) {
            keySet = ((LogEvent) cracked).getPropertyKeySet();
        }
        is.close();
        return keySet;
    }
}
