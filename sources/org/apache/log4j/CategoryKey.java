package org.apache.log4j;

class CategoryKey {
    static Class class$org$apache$log4j$CategoryKey;
    int hashCache;
    String name;

    CategoryKey(String name2) {
        this.name = name2;
        this.hashCache = name2.hashCode();
    }

    public final int hashCode() {
        return this.hashCache;
    }

    public final boolean equals(Object rArg) {
        Class<?> cls;
        if (this == rArg) {
            return true;
        }
        if (rArg != null) {
            if (class$org$apache$log4j$CategoryKey == null) {
                cls = class$("org.apache.log4j.CategoryKey");
                class$org$apache$log4j$CategoryKey = cls;
            } else {
                cls = class$org$apache$log4j$CategoryKey;
            }
            if (cls == rArg.getClass()) {
                return this.name.equals(((CategoryKey) rArg).name);
            }
        }
        return false;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }
}
