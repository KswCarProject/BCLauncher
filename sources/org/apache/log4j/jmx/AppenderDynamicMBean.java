package org.apache.log4j.jmx;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.OptionHandler;

public class AppenderDynamicMBean extends AbstractDynamicMBean {
    private static Logger cat;
    static Class class$java$lang$String;
    static Class class$org$apache$log4j$Layout;
    static Class class$org$apache$log4j$Priority;
    static Class class$org$apache$log4j$jmx$AppenderDynamicMBean;
    private Appender appender;
    private Vector dAttributes = new Vector();
    private String dClassName = getClass().getName();
    private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
    private String dDescription = "This MBean acts as a management facade for log4j appenders.";
    private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[2];
    private Hashtable dynamicProps = new Hashtable(5);

    static {
        Class cls;
        if (class$org$apache$log4j$jmx$AppenderDynamicMBean == null) {
            cls = class$("org.apache.log4j.jmx.AppenderDynamicMBean");
            class$org$apache$log4j$jmx$AppenderDynamicMBean = cls;
        } else {
            cls = class$org$apache$log4j$jmx$AppenderDynamicMBean;
        }
        cat = Logger.getLogger(cls);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public AppenderDynamicMBean(Appender appender2) throws IntrospectionException {
        this.appender = appender2;
        buildDynamicMBeanInfo();
    }

    private void buildDynamicMBeanInfo() throws IntrospectionException {
        Class cls;
        String returnClassName;
        this.dConstructors[0] = new MBeanConstructorInfo("AppenderDynamicMBean(): Constructs a AppenderDynamicMBean instance", getClass().getConstructors()[0]);
        PropertyDescriptor[] pd = Introspector.getBeanInfo(this.appender.getClass()).getPropertyDescriptors();
        int size = pd.length;
        for (int i = 0; i < size; i++) {
            String name = pd[i].getName();
            Method readMethod = pd[i].getReadMethod();
            Method writeMethod = pd[i].getWriteMethod();
            if (readMethod != null) {
                Class returnClass = readMethod.getReturnType();
                if (isSupportedType(returnClass)) {
                    if (class$org$apache$log4j$Priority == null) {
                        cls = class$("org.apache.log4j.Priority");
                        class$org$apache$log4j$Priority = cls;
                    } else {
                        cls = class$org$apache$log4j$Priority;
                    }
                    if (returnClass.isAssignableFrom(cls)) {
                        returnClassName = "java.lang.String";
                    } else {
                        returnClassName = returnClass.getName();
                    }
                    this.dAttributes.add(new MBeanAttributeInfo(name, returnClassName, "Dynamic", true, writeMethod != null, false));
                    this.dynamicProps.put(name, new MethodUnion(readMethod, writeMethod));
                }
            }
        }
        this.dOperations[0] = new MBeanOperationInfo("activateOptions", "activateOptions(): add an appender", new MBeanParameterInfo[0], "void", 1);
        this.dOperations[1] = new MBeanOperationInfo("setLayout", "setLayout(): add a layout", new MBeanParameterInfo[]{new MBeanParameterInfo("layout class", "java.lang.String", "layout class")}, "void", 1);
    }

    private boolean isSupportedType(Class clazz) {
        Class cls;
        Class cls2;
        if (clazz.isPrimitive()) {
            return true;
        }
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        if (clazz == cls) {
            return true;
        }
        if (class$org$apache$log4j$Priority == null) {
            cls2 = class$("org.apache.log4j.Priority");
            class$org$apache$log4j$Priority = cls2;
        } else {
            cls2 = class$org$apache$log4j$Priority;
        }
        if (clazz.isAssignableFrom(cls2)) {
            return true;
        }
        return false;
    }

    public MBeanInfo getMBeanInfo() {
        cat.debug("getMBeanInfo called.");
        MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[this.dAttributes.size()];
        this.dAttributes.toArray(attribs);
        return new MBeanInfo(this.dClassName, this.dDescription, attribs, this.dConstructors, this.dOperations, new MBeanNotificationInfo[0]);
    }

    public Object invoke(String operationName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        Class cls;
        if (!operationName.equals("activateOptions") || !(this.appender instanceof OptionHandler)) {
            if (operationName.equals("setLayout")) {
                String str = params[0];
                if (class$org$apache$log4j$Layout == null) {
                    cls = class$("org.apache.log4j.Layout");
                    class$org$apache$log4j$Layout = cls;
                } else {
                    cls = class$org$apache$log4j$Layout;
                }
                Layout layout = (Layout) OptionConverter.instantiateByClassName(str, cls, (Object) null);
                this.appender.setLayout(layout);
                registerLayoutMBean(layout);
            }
            return null;
        }
        ((OptionHandler) this.appender).activateOptions();
        return "Options activated.";
    }

    /* access modifiers changed from: package-private */
    public void registerLayoutMBean(Layout layout) {
        if (layout != null) {
            String name = new StringBuffer().append(getAppenderName(this.appender)).append(",layout=").append(layout.getClass().getName()).toString();
            cat.debug(new StringBuffer().append("Adding LayoutMBean:").append(name).toString());
            try {
                LayoutDynamicMBean appenderMBean = new LayoutDynamicMBean(layout);
                ObjectName objectName = new ObjectName(new StringBuffer().append("log4j:appender=").append(name).toString());
                try {
                    if (!this.server.isRegistered(objectName)) {
                        registerMBean(appenderMBean, objectName);
                        this.dAttributes.add(new MBeanAttributeInfo(new StringBuffer().append("appender=").append(name).toString(), "javax.management.ObjectName", new StringBuffer().append("The ").append(name).append(" layout.").toString(), true, true, false));
                    }
                    ObjectName objectName2 = objectName;
                } catch (JMException e) {
                    e = e;
                    ObjectName objectName3 = objectName;
                    cat.error(new StringBuffer().append("Could not add DynamicLayoutMBean for [").append(name).append("].").toString(), e);
                } catch (IntrospectionException e2) {
                    e = e2;
                    ObjectName objectName4 = objectName;
                    cat.error(new StringBuffer().append("Could not add DynamicLayoutMBean for [").append(name).append("].").toString(), e);
                } catch (RuntimeException e3) {
                    e = e3;
                    ObjectName objectName5 = objectName;
                    cat.error(new StringBuffer().append("Could not add DynamicLayoutMBean for [").append(name).append("].").toString(), e);
                }
            } catch (JMException e4) {
                e = e4;
                cat.error(new StringBuffer().append("Could not add DynamicLayoutMBean for [").append(name).append("].").toString(), e);
            } catch (IntrospectionException e5) {
                e = e5;
                cat.error(new StringBuffer().append("Could not add DynamicLayoutMBean for [").append(name).append("].").toString(), e);
            } catch (RuntimeException e6) {
                e = e6;
                cat.error(new StringBuffer().append("Could not add DynamicLayoutMBean for [").append(name).append("].").toString(), e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Logger getLogger() {
        return cat;
    }

    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke a getter of ").append(this.dClassName).append(" with null attribute name").toString());
        }
        cat.debug(new StringBuffer().append("getAttribute called with [").append(attributeName).append("].").toString());
        if (attributeName.startsWith(new StringBuffer().append("appender=").append(this.appender.getName()).append(",layout").toString())) {
            try {
                return new ObjectName(new StringBuffer().append("log4j:").append(attributeName).toString());
            } catch (MalformedObjectNameException e) {
                cat.error("attributeName", e);
            } catch (RuntimeException e2) {
                cat.error("attributeName", e2);
            }
        }
        MethodUnion mu = (MethodUnion) this.dynamicProps.get(attributeName);
        if (mu == null || mu.readMethod == null) {
            throw new AttributeNotFoundException(new StringBuffer().append("Cannot find ").append(attributeName).append(" attribute in ").append(this.dClassName).toString());
        }
        try {
            return mu.readMethod.invoke(this.appender, (Object[]) null);
        } catch (IllegalAccessException e3) {
            return null;
        } catch (InvocationTargetException e4) {
            if ((e4.getTargetException() instanceof InterruptedException) || (e4.getTargetException() instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            return null;
        } catch (RuntimeException e5) {
            return null;
        }
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        Class cls;
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), new StringBuffer().append("Cannot invoke a setter of ").append(this.dClassName).append(" with null attribute").toString());
        }
        String name = attribute.getName();
        Object value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke the setter of ").append(this.dClassName).append(" with null attribute name").toString());
        }
        MethodUnion mu = (MethodUnion) this.dynamicProps.get(name);
        if (mu != null && mu.writeMethod != null) {
            Object[] o = new Object[1];
            Class cls2 = mu.writeMethod.getParameterTypes()[0];
            if (class$org$apache$log4j$Priority == null) {
                cls = class$("org.apache.log4j.Priority");
                class$org$apache$log4j$Priority = cls;
            } else {
                cls = class$org$apache$log4j$Priority;
            }
            if (cls2 == cls) {
                value = OptionConverter.toLevel((String) value, (Level) getAttribute(name));
            }
            o[0] = value;
            try {
                mu.writeMethod.invoke(this.appender, o);
            } catch (InvocationTargetException e) {
                if ((e.getTargetException() instanceof InterruptedException) || (e.getTargetException() instanceof InterruptedIOException)) {
                    Thread.currentThread().interrupt();
                }
                cat.error("FIXME", e);
            } catch (IllegalAccessException e2) {
                cat.error("FIXME", e2);
            } catch (RuntimeException e3) {
                cat.error("FIXME", e3);
            }
        } else if (!name.endsWith(".layout")) {
            throw new AttributeNotFoundException(new StringBuffer().append("Attribute ").append(name).append(" not found in ").append(getClass().getName()).toString());
        }
    }

    public ObjectName preRegister(MBeanServer server, ObjectName name) {
        cat.debug(new StringBuffer().append("preRegister called. Server=").append(server).append(", name=").append(name).toString());
        this.server = server;
        registerLayoutMBean(this.appender.getLayout());
        return name;
    }
}
