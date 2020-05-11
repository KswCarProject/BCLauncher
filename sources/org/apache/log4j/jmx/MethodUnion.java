package org.apache.log4j.jmx;

import java.lang.reflect.Method;

class MethodUnion {
    Method readMethod;
    Method writeMethod;

    MethodUnion(Method readMethod2, Method writeMethod2) {
        this.readMethod = readMethod2;
        this.writeMethod = writeMethod2;
    }
}
