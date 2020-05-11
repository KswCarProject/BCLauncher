package org.apache.log4j.helpers;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.LoggerRepository;

public class OptionConverter {
    static String DELIM_START = "${";
    static int DELIM_START_LEN = 2;
    static char DELIM_STOP = '}';
    static int DELIM_STOP_LEN = 1;
    static Class class$java$lang$String;
    static Class class$org$apache$log4j$Level;
    static Class class$org$apache$log4j$spi$Configurator;

    private OptionConverter() {
    }

    public static String[] concatanateArrays(String[] l, String[] r) {
        String[] a = new String[(l.length + r.length)];
        System.arraycopy(l, 0, a, 0, l.length);
        System.arraycopy(r, 0, a, l.length, r.length);
        return a;
    }

    public static String convertSpecialChars(String s) {
        int len = s.length();
        StringBuffer sbuf = new StringBuffer(len);
        int i = 0;
        while (i < len) {
            int i2 = i + 1;
            char c = s.charAt(i);
            if (c == '\\') {
                int i3 = i2 + 1;
                c = s.charAt(i2);
                if (c == 'n') {
                    c = 10;
                    i2 = i3;
                } else if (c == 'r') {
                    c = 13;
                    i2 = i3;
                } else if (c == 't') {
                    c = 9;
                    i2 = i3;
                } else if (c == 'f') {
                    c = 12;
                    i2 = i3;
                } else if (c == 8) {
                    c = 8;
                    i2 = i3;
                } else if (c == '\"') {
                    c = '\"';
                    i2 = i3;
                } else if (c == '\'') {
                    c = '\'';
                    i2 = i3;
                } else if (c == '\\') {
                    c = '\\';
                    i2 = i3;
                } else {
                    i2 = i3;
                }
            }
            sbuf.append(c);
            i = i2;
        }
        return sbuf.toString();
    }

    public static String getSystemProperty(String key, String def) {
        try {
            return System.getProperty(key, def);
        } catch (Throwable th) {
            LogLog.debug(new StringBuffer().append("Was not allowed to read system property \"").append(key).append("\".").toString());
            return def;
        }
    }

    public static Object instantiateByKey(Properties props, String key, Class superClass, Object defaultValue) {
        String className = findAndSubst(key, props);
        if (className != null) {
            return instantiateByClassName(className.trim(), superClass, defaultValue);
        }
        LogLog.error(new StringBuffer().append("Could not find value for key ").append(key).toString());
        return defaultValue;
    }

    public static boolean toBoolean(String value, boolean dEfault) {
        if (value == null) {
            return dEfault;
        }
        String trimmedVal = value.trim();
        if ("true".equalsIgnoreCase(trimmedVal)) {
            return true;
        }
        if ("false".equalsIgnoreCase(trimmedVal)) {
            return false;
        }
        return dEfault;
    }

    public static int toInt(String value, int dEfault) {
        if (value == null) {
            return dEfault;
        }
        String s = value.trim();
        try {
            return Integer.valueOf(s).intValue();
        } catch (NumberFormatException e) {
            LogLog.error(new StringBuffer().append("[").append(s).append("] is not in proper int form.").toString());
            e.printStackTrace();
            return dEfault;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: org.apache.log4j.Level} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.apache.log4j.Level toLevel(java.lang.String r14, org.apache.log4j.Level r15) {
        /*
            r11 = 0
            r13 = 0
            if (r14 != 0) goto L_0x0005
        L_0x0004:
            return r15
        L_0x0005:
            java.lang.String r14 = r14.trim()
            r12 = 35
            int r4 = r14.indexOf(r12)
            r12 = -1
            if (r4 != r12) goto L_0x0021
            java.lang.String r12 = "NULL"
            boolean r12 = r12.equalsIgnoreCase(r14)
            if (r12 == 0) goto L_0x001c
            r15 = r11
            goto L_0x0004
        L_0x001c:
            org.apache.log4j.Level r15 = org.apache.log4j.Level.toLevel((java.lang.String) r14, (org.apache.log4j.Level) r15)
            goto L_0x0004
        L_0x0021:
            r9 = r15
            int r12 = r4 + 1
            java.lang.String r1 = r14.substring(r12)
            java.lang.String r5 = r14.substring(r13, r4)
            java.lang.String r12 = "NULL"
            boolean r12 = r12.equalsIgnoreCase(r5)
            if (r12 == 0) goto L_0x0036
            r15 = r11
            goto L_0x0004
        L_0x0036:
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            java.lang.String r12 = "toLevel:class=["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r1)
            java.lang.String r12 = "]"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r12 = ":pri=["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r5)
            java.lang.String r12 = "]"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.apache.log4j.helpers.LogLog.debug(r11)
            java.lang.Class r2 = org.apache.log4j.helpers.Loader.loadClass(r1)     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r11 = 2
            java.lang.Class[] r7 = new java.lang.Class[r11]     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r12 = 0
            java.lang.Class r11 = class$java$lang$String     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            if (r11 != 0) goto L_0x00a2
            java.lang.String r11 = "java.lang.String"
            java.lang.Class r11 = class$(r11)     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            class$java$lang$String = r11     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
        L_0x0076:
            r7[r12] = r11     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r12 = 1
            java.lang.Class r11 = class$org$apache$log4j$Level     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            if (r11 != 0) goto L_0x00a5
            java.lang.String r11 = "org.apache.log4j.Level"
            java.lang.Class r11 = class$(r11)     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            class$org$apache$log4j$Level = r11     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
        L_0x0085:
            r7[r12] = r11     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            java.lang.String r11 = "toLevel"
            java.lang.reflect.Method r10 = r2.getMethod(r11, r7)     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r11 = 2
            java.lang.Object[] r8 = new java.lang.Object[r11]     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r11 = 0
            r8[r11] = r5     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r11 = 1
            r8[r11] = r15     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r11 = 0
            java.lang.Object r6 = r10.invoke(r11, r8)     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r0 = r6
            org.apache.log4j.Level r0 = (org.apache.log4j.Level) r0     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            r9 = r0
        L_0x009f:
            r15 = r9
            goto L_0x0004
        L_0x00a2:
            java.lang.Class r11 = class$java$lang$String     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            goto L_0x0076
        L_0x00a5:
            java.lang.Class r11 = class$org$apache$log4j$Level     // Catch:{ ClassNotFoundException -> 0x00a8, NoSuchMethodException -> 0x00c6, InvocationTargetException -> 0x00ea, ClassCastException -> 0x0126, IllegalAccessException -> 0x0145, RuntimeException -> 0x0164 }
            goto L_0x0085
        L_0x00a8:
            r3 = move-exception
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            java.lang.String r12 = "custom level class ["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r1)
            java.lang.String r12 = "] not found."
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.apache.log4j.helpers.LogLog.warn(r11)
            goto L_0x009f
        L_0x00c6:
            r3 = move-exception
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            java.lang.String r12 = "custom level class ["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r1)
            java.lang.String r12 = "]"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r12 = " does not have a class function toLevel(String, Level)"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.apache.log4j.helpers.LogLog.warn(r11, r3)
            goto L_0x009f
        L_0x00ea:
            r3 = move-exception
            java.lang.Throwable r11 = r3.getTargetException()
            boolean r11 = r11 instanceof java.lang.InterruptedException
            if (r11 != 0) goto L_0x00fb
            java.lang.Throwable r11 = r3.getTargetException()
            boolean r11 = r11 instanceof java.io.InterruptedIOException
            if (r11 == 0) goto L_0x0102
        L_0x00fb:
            java.lang.Thread r11 = java.lang.Thread.currentThread()
            r11.interrupt()
        L_0x0102:
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            java.lang.String r12 = "custom level class ["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r1)
            java.lang.String r12 = "]"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r12 = " could not be instantiated"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.apache.log4j.helpers.LogLog.warn(r11, r3)
            goto L_0x009f
        L_0x0126:
            r3 = move-exception
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            java.lang.String r12 = "class ["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r1)
            java.lang.String r12 = "] is not a subclass of org.apache.log4j.Level"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.apache.log4j.helpers.LogLog.warn(r11, r3)
            goto L_0x009f
        L_0x0145:
            r3 = move-exception
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            java.lang.String r12 = "class ["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r1)
            java.lang.String r12 = "] cannot be instantiated due to access restrictions"
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.apache.log4j.helpers.LogLog.warn(r11, r3)
            goto L_0x009f
        L_0x0164:
            r3 = move-exception
            java.lang.StringBuffer r11 = new java.lang.StringBuffer
            r11.<init>()
            java.lang.String r12 = "class ["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r1)
            java.lang.String r12 = "], level ["
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.StringBuffer r11 = r11.append(r5)
            java.lang.String r12 = "] conversion failed."
            java.lang.StringBuffer r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            org.apache.log4j.helpers.LogLog.warn(r11, r3)
            goto L_0x009f
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.helpers.OptionConverter.toLevel(java.lang.String, org.apache.log4j.Level):org.apache.log4j.Level");
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static long toFileSize(String value, long dEfault) {
        if (value == null) {
            return dEfault;
        }
        String s = value.trim().toUpperCase();
        long multiplier = 1;
        int index = s.indexOf("KB");
        if (index != -1) {
            multiplier = 1024;
            s = s.substring(0, index);
        } else {
            int index2 = s.indexOf("MB");
            if (index2 != -1) {
                multiplier = 1048576;
                s = s.substring(0, index2);
            } else {
                int index3 = s.indexOf("GB");
                if (index3 != -1) {
                    multiplier = 1073741824;
                    s = s.substring(0, index3);
                }
            }
        }
        if (s == null) {
            return dEfault;
        }
        try {
            return Long.valueOf(s).longValue() * multiplier;
        } catch (NumberFormatException e) {
            LogLog.error(new StringBuffer().append("[").append(s).append("] is not in proper int form.").toString());
            LogLog.error(new StringBuffer().append("[").append(value).append("] not in expected format.").toString(), e);
            return dEfault;
        }
    }

    public static String findAndSubst(String key, Properties props) {
        String value = props.getProperty(key);
        if (value == null) {
            return null;
        }
        try {
            return substVars(value, props);
        } catch (IllegalArgumentException e) {
            LogLog.error(new StringBuffer().append("Bad option value [").append(value).append("].").toString(), e);
            return value;
        }
    }

    public static Object instantiateByClassName(String className, Class superClass, Object defaultValue) {
        if (className == null) {
            return defaultValue;
        }
        try {
            Class classObj = Loader.loadClass(className);
            if (superClass.isAssignableFrom(classObj)) {
                return classObj.newInstance();
            }
            LogLog.error(new StringBuffer().append("A \"").append(className).append("\" object is not assignable to a \"").append(superClass.getName()).append("\" variable.").toString());
            LogLog.error(new StringBuffer().append("The class \"").append(superClass.getName()).append("\" was loaded by ").toString());
            LogLog.error(new StringBuffer().append("[").append(superClass.getClassLoader()).append("] whereas object of type ").toString());
            LogLog.error(new StringBuffer().append("\"").append(classObj.getName()).append("\" was loaded by [").append(classObj.getClassLoader()).append("].").toString());
            return defaultValue;
        } catch (ClassNotFoundException e) {
            LogLog.error(new StringBuffer().append("Could not instantiate class [").append(className).append("].").toString(), e);
            return defaultValue;
        } catch (IllegalAccessException e2) {
            LogLog.error(new StringBuffer().append("Could not instantiate class [").append(className).append("].").toString(), e2);
            return defaultValue;
        } catch (InstantiationException e3) {
            LogLog.error(new StringBuffer().append("Could not instantiate class [").append(className).append("].").toString(), e3);
            return defaultValue;
        } catch (RuntimeException e4) {
            LogLog.error(new StringBuffer().append("Could not instantiate class [").append(className).append("].").toString(), e4);
            return defaultValue;
        }
    }

    public static String substVars(String val, Properties props) throws IllegalArgumentException {
        StringBuffer sbuf = new StringBuffer();
        int i = 0;
        while (true) {
            int j = val.indexOf(DELIM_START, i);
            if (j != -1) {
                sbuf.append(val.substring(i, j));
                int k = val.indexOf(DELIM_STOP, j);
                if (k == -1) {
                    throw new IllegalArgumentException(new StringBuffer().append('\"').append(val).append("\" has no closing brace. Opening brace at position ").append(j).append('.').toString());
                }
                String key = val.substring(j + DELIM_START_LEN, k);
                String replacement = getSystemProperty(key, (String) null);
                if (replacement == null && props != null) {
                    replacement = props.getProperty(key);
                }
                if (replacement != null) {
                    sbuf.append(substVars(replacement, props));
                }
                i = k + DELIM_STOP_LEN;
            } else if (i == 0) {
                return val;
            } else {
                sbuf.append(val.substring(i, val.length()));
                return sbuf.toString();
            }
        }
    }

    public static void selectAndConfigure(InputStream inputStream, String clazz, LoggerRepository hierarchy) {
        Configurator configurator;
        Class cls;
        if (clazz != null) {
            LogLog.debug(new StringBuffer().append("Preferred configurator class: ").append(clazz).toString());
            if (class$org$apache$log4j$spi$Configurator == null) {
                cls = class$("org.apache.log4j.spi.Configurator");
                class$org$apache$log4j$spi$Configurator = cls;
            } else {
                cls = class$org$apache$log4j$spi$Configurator;
            }
            configurator = (Configurator) instantiateByClassName(clazz, cls, (Object) null);
            if (configurator == null) {
                LogLog.error(new StringBuffer().append("Could not instantiate configurator [").append(clazz).append("].").toString());
                return;
            }
        } else {
            configurator = new PropertyConfigurator();
        }
        configurator.doConfigure(inputStream, hierarchy);
    }

    public static void selectAndConfigure(URL url, String clazz, LoggerRepository hierarchy) {
        Configurator configurator;
        Class cls;
        String filename = url.getFile();
        if (clazz == null && filename != null && filename.endsWith(".xml")) {
            clazz = "org.apache.log4j.xml.DOMConfigurator";
        }
        if (clazz != null) {
            LogLog.debug(new StringBuffer().append("Preferred configurator class: ").append(clazz).toString());
            if (class$org$apache$log4j$spi$Configurator == null) {
                cls = class$("org.apache.log4j.spi.Configurator");
                class$org$apache$log4j$spi$Configurator = cls;
            } else {
                cls = class$org$apache$log4j$spi$Configurator;
            }
            configurator = (Configurator) instantiateByClassName(clazz, cls, (Object) null);
            if (configurator == null) {
                LogLog.error(new StringBuffer().append("Could not instantiate configurator [").append(clazz).append("].").toString());
                return;
            }
        } else {
            configurator = new PropertyConfigurator();
        }
        configurator.doConfigure(url, hierarchy);
    }
}
