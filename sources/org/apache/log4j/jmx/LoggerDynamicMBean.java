package org.apache.log4j.jmx;

import a_vcard.android.provider.Contacts;
import java.beans.IntrospectionException;
import java.util.Enumeration;
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
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.DateLayout;
import org.apache.log4j.helpers.OptionConverter;

public class LoggerDynamicMBean extends AbstractDynamicMBean implements NotificationListener {
    private static Logger cat;
    static Class class$org$apache$log4j$Appender;
    static Class class$org$apache$log4j$jmx$LoggerDynamicMBean;
    private Vector dAttributes = new Vector();
    private String dClassName = getClass().getName();
    private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
    private String dDescription = "This MBean acts as a management facade for a org.apache.log4j.Logger instance.";
    private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[1];
    private Logger logger;

    static {
        Class cls;
        if (class$org$apache$log4j$jmx$LoggerDynamicMBean == null) {
            cls = class$("org.apache.log4j.jmx.LoggerDynamicMBean");
            class$org$apache$log4j$jmx$LoggerDynamicMBean = cls;
        } else {
            cls = class$org$apache$log4j$jmx$LoggerDynamicMBean;
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

    public LoggerDynamicMBean(Logger logger2) {
        this.logger = logger2;
        buildDynamicMBeanInfo();
    }

    public void handleNotification(Notification notification, Object handback) {
        cat.debug(new StringBuffer().append("Received notification: ").append(notification.getType()).toString());
        registerAppenderMBean((Appender) notification.getUserData());
    }

    private void buildDynamicMBeanInfo() {
        this.dConstructors[0] = new MBeanConstructorInfo("HierarchyDynamicMBean(): Constructs a HierarchyDynamicMBean instance", getClass().getConstructors()[0]);
        this.dAttributes.add(new MBeanAttributeInfo(Contacts.PeopleColumns.NAME, "java.lang.String", "The name of this Logger.", true, false, false));
        this.dAttributes.add(new MBeanAttributeInfo("priority", "java.lang.String", "The priority of this logger.", true, true, false));
        this.dOperations[0] = new MBeanOperationInfo("addAppender", "addAppender(): add an appender", new MBeanParameterInfo[]{new MBeanParameterInfo("class name", "java.lang.String", "add an appender to this logger"), new MBeanParameterInfo("appender name", "java.lang.String", "name of the appender")}, "void", 1);
    }

    /* access modifiers changed from: protected */
    public Logger getLogger() {
        return this.logger;
    }

    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] attribs = new MBeanAttributeInfo[this.dAttributes.size()];
        this.dAttributes.toArray(attribs);
        return new MBeanInfo(this.dClassName, this.dDescription, attribs, this.dConstructors, this.dOperations, new MBeanNotificationInfo[0]);
    }

    public Object invoke(String operationName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        if (!operationName.equals("addAppender")) {
            return null;
        }
        addAppender(params[0], params[1]);
        return "Hello world.";
    }

    public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attributeName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke a getter of ").append(this.dClassName).append(" with null attribute name").toString());
        } else if (attributeName.equals(Contacts.PeopleColumns.NAME)) {
            return this.logger.getName();
        } else {
            if (attributeName.equals("priority")) {
                Level l = this.logger.getLevel();
                if (l == null) {
                    return null;
                }
                return l.toString();
            }
            if (attributeName.startsWith("appender=")) {
                try {
                    return new ObjectName(new StringBuffer().append("log4j:").append(attributeName).toString());
                } catch (MalformedObjectNameException e) {
                    cat.error(new StringBuffer().append("Could not create ObjectName").append(attributeName).toString());
                } catch (RuntimeException e2) {
                    cat.error(new StringBuffer().append("Could not create ObjectName").append(attributeName).toString());
                }
            }
            throw new AttributeNotFoundException(new StringBuffer().append("Cannot find ").append(attributeName).append(" attribute in ").append(this.dClassName).toString());
        }
    }

    /* access modifiers changed from: package-private */
    public void addAppender(String appenderClass, String appenderName) {
        Class cls;
        cat.debug(new StringBuffer().append("addAppender called with ").append(appenderClass).append(", ").append(appenderName).toString());
        if (class$org$apache$log4j$Appender == null) {
            cls = class$("org.apache.log4j.Appender");
            class$org$apache$log4j$Appender = cls;
        } else {
            cls = class$org$apache$log4j$Appender;
        }
        Appender appender = (Appender) OptionConverter.instantiateByClassName(appenderClass, cls, (Object) null);
        appender.setName(appenderName);
        this.logger.addAppender(appender);
    }

    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        Level p;
        if (attribute == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute cannot be null"), new StringBuffer().append("Cannot invoke a setter of ").append(this.dClassName).append(" with null attribute").toString());
        }
        String name = attribute.getName();
        Object value = attribute.getValue();
        if (name == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null"), new StringBuffer().append("Cannot invoke the setter of ").append(this.dClassName).append(" with null attribute name").toString());
        } else if (!name.equals("priority")) {
            throw new AttributeNotFoundException(new StringBuffer().append("Attribute ").append(name).append(" not found in ").append(getClass().getName()).toString());
        } else if (value instanceof String) {
            String s = (String) value;
            Level p2 = this.logger.getLevel();
            if (s.equalsIgnoreCase(DateLayout.NULL_DATE_FORMAT)) {
                p = null;
            } else {
                p = OptionConverter.toLevel(s, p2);
            }
            this.logger.setLevel(p);
        }
    }

    /* access modifiers changed from: package-private */
    public void appenderMBeanRegistration() {
        Enumeration enumeration = this.logger.getAllAppenders();
        while (enumeration.hasMoreElements()) {
            registerAppenderMBean((Appender) enumeration.nextElement());
        }
    }

    /* access modifiers changed from: package-private */
    public void registerAppenderMBean(Appender appender) {
        String name = getAppenderName(appender);
        cat.debug(new StringBuffer().append("Adding AppenderMBean for appender named ").append(name).toString());
        try {
            AppenderDynamicMBean appenderMBean = new AppenderDynamicMBean(appender);
            ObjectName objectName = new ObjectName("log4j", "appender", name);
            try {
                if (!this.server.isRegistered(objectName)) {
                    registerMBean(appenderMBean, objectName);
                    this.dAttributes.add(new MBeanAttributeInfo(new StringBuffer().append("appender=").append(name).toString(), "javax.management.ObjectName", new StringBuffer().append("The ").append(name).append(" appender.").toString(), true, true, false));
                }
                ObjectName objectName2 = objectName;
            } catch (JMException e) {
                e = e;
                ObjectName objectName3 = objectName;
                cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
            } catch (IntrospectionException e2) {
                e = e2;
                ObjectName objectName4 = objectName;
                cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
            } catch (RuntimeException e3) {
                e = e3;
                ObjectName objectName5 = objectName;
                cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
            }
        } catch (JMException e4) {
            e = e4;
            cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
        } catch (IntrospectionException e5) {
            e = e5;
            cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
        } catch (RuntimeException e6) {
            e = e6;
            cat.error(new StringBuffer().append("Could not add appenderMBean for [").append(name).append("].").toString(), e);
        }
    }

    public void postRegister(Boolean registrationDone) {
        appenderMBeanRegistration();
    }
}
