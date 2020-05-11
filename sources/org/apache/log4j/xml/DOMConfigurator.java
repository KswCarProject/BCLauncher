package org.apache.log4j.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.config.PropertySetter;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.or.RendererMap;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.Configurator;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RendererSupport;
import org.apache.log4j.spi.ThrowableRenderer;
import org.apache.log4j.spi.ThrowableRendererSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMConfigurator implements Configurator {
    static final String ADDITIVITY_ATTR = "additivity";
    static final String APPENDER_REF_TAG = "appender-ref";
    static final String APPENDER_TAG = "appender";
    static final String CATEGORY = "category";
    static final String CATEGORY_FACTORY_TAG = "categoryFactory";
    static final String CLASS_ATTR = "class";
    static final String CONFIGURATION_TAG = "log4j:configuration";
    static final String CONFIG_DEBUG_ATTR = "configDebug";
    static final String EMPTY_STR = "";
    static final String ERROR_HANDLER_TAG = "errorHandler";
    static final String FILTER_TAG = "filter";
    static final String INTERNAL_DEBUG_ATTR = "debug";
    static final String LAYOUT_TAG = "layout";
    static final String LEVEL_TAG = "level";
    static final String LOGGER = "logger";
    static final String LOGGER_FACTORY_TAG = "loggerFactory";
    static final String LOGGER_REF = "logger-ref";
    static final String NAME_ATTR = "name";
    static final String OLD_CONFIGURATION_TAG = "configuration";
    static final Class[] ONE_STRING_PARAM;
    static final String PARAM_TAG = "param";
    static final String PRIORITY_TAG = "priority";
    static final String REF_ATTR = "ref";
    static final String RENDERED_CLASS_ATTR = "renderedClass";
    static final String RENDERER_TAG = "renderer";
    static final String RENDERING_CLASS_ATTR = "renderingClass";
    private static final String RESET_ATTR = "reset";
    static final String ROOT_REF = "root-ref";
    static final String ROOT_TAG = "root";
    static final String THRESHOLD_ATTR = "threshold";
    private static final String THROWABLE_RENDERER_TAG = "throwableRenderer";
    static final String VALUE_ATTR = "value";
    static Class class$java$lang$String = null;
    static Class class$org$apache$log4j$spi$ErrorHandler = null;
    static Class class$org$apache$log4j$spi$Filter = null;
    static Class class$org$apache$log4j$spi$LoggerFactory = null;
    static final String dbfKey = "javax.xml.parsers.DocumentBuilderFactory";
    Hashtable appenderBag = new Hashtable();
    protected LoggerFactory catFactory = null;
    Properties props;
    LoggerRepository repository;

    private interface ParseAction {
        Document parse(DocumentBuilder documentBuilder) throws SAXException, IOException;
    }

    static {
        Class cls;
        Class[] clsArr = new Class[1];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        clsArr[0] = cls;
        ONE_STRING_PARAM = clsArr;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: org.w3c.dom.Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: org.w3c.dom.Element} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.log4j.Appender findAppenderByName(org.w3c.dom.Document r10, java.lang.String r11) {
        /*
            r9 = this;
            java.util.Hashtable r7 = r9.appenderBag
            java.lang.Object r0 = r7.get(r11)
            org.apache.log4j.Appender r0 = (org.apache.log4j.Appender) r0
            if (r0 == 0) goto L_0x000c
            r7 = r0
        L_0x000b:
            return r7
        L_0x000c:
            r2 = 0
            java.lang.String r7 = "appender"
            org.w3c.dom.NodeList r3 = r10.getElementsByTagName(r7)
            r6 = 0
        L_0x0014:
            int r7 = r3.getLength()
            if (r6 >= r7) goto L_0x0035
            org.w3c.dom.Node r5 = r3.item(r6)
            org.w3c.dom.NamedNodeMap r4 = r5.getAttributes()
            java.lang.String r7 = "name"
            org.w3c.dom.Node r1 = r4.getNamedItem(r7)
            java.lang.String r7 = r1.getNodeValue()
            boolean r7 = r11.equals(r7)
            if (r7 == 0) goto L_0x0055
            r2 = r5
            org.w3c.dom.Element r2 = (org.w3c.dom.Element) r2
        L_0x0035:
            if (r2 != 0) goto L_0x0058
            java.lang.StringBuffer r7 = new java.lang.StringBuffer
            r7.<init>()
            java.lang.String r8 = "No appender named ["
            java.lang.StringBuffer r7 = r7.append(r8)
            java.lang.StringBuffer r7 = r7.append(r11)
            java.lang.String r8 = "] could be found."
            java.lang.StringBuffer r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            org.apache.log4j.helpers.LogLog.error(r7)
            r7 = 0
            goto L_0x000b
        L_0x0055:
            int r6 = r6 + 1
            goto L_0x0014
        L_0x0058:
            org.apache.log4j.Appender r0 = r9.parseAppender(r2)
            if (r0 == 0) goto L_0x0063
            java.util.Hashtable r7 = r9.appenderBag
            r7.put(r11, r0)
        L_0x0063:
            r7 = r0
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.xml.DOMConfigurator.findAppenderByName(org.w3c.dom.Document, java.lang.String):org.apache.log4j.Appender");
    }

    /* access modifiers changed from: protected */
    public Appender findAppenderByReference(Element appenderRef) {
        return findAppenderByName(appenderRef.getOwnerDocument(), subst(appenderRef.getAttribute(REF_ATTR)));
    }

    private static void parseUnrecognizedElement(Object instance, Element element, Properties props2) throws Exception {
        boolean recognized = false;
        if (instance instanceof UnrecognizedElementHandler) {
            recognized = ((UnrecognizedElementHandler) instance).parseUnrecognizedElement(element, props2);
        }
        if (!recognized) {
            LogLog.warn(new StringBuffer().append("Unrecognized element ").append(element.getNodeName()).toString());
        }
    }

    private static void quietParseUnrecognizedElement(Object instance, Element element, Properties props2) {
        try {
            parseUnrecognizedElement(instance, element, props2);
        } catch (Exception ex) {
            if ((ex instanceof InterruptedException) || (ex instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Error in extension content: ", ex);
        }
    }

    /* access modifiers changed from: protected */
    public Appender parseAppender(Element appenderElement) {
        String className = subst(appenderElement.getAttribute(CLASS_ATTR));
        LogLog.debug(new StringBuffer().append("Class name: [").append(className).append(']').toString());
        try {
            Object instance = Loader.loadClass(className).newInstance();
            Appender appender = (Appender) instance;
            PropertySetter propSetter = new PropertySetter(appender);
            appender.setName(subst(appenderElement.getAttribute("name")));
            NodeList children = appenderElement.getChildNodes();
            int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else if (currentElement.getTagName().equals(LAYOUT_TAG)) {
                        appender.setLayout(parseLayout(currentElement));
                    } else if (currentElement.getTagName().equals(FILTER_TAG)) {
                        parseFilters(currentElement, appender);
                    } else if (currentElement.getTagName().equals(ERROR_HANDLER_TAG)) {
                        parseErrorHandler(currentElement, appender);
                    } else if (currentElement.getTagName().equals(APPENDER_REF_TAG)) {
                        String refName = subst(currentElement.getAttribute(REF_ATTR));
                        if (appender instanceof AppenderAttachable) {
                            LogLog.debug(new StringBuffer().append("Attaching appender named [").append(refName).append("] to appender named [").append(appender.getName()).append("].").toString());
                            ((AppenderAttachable) appender).addAppender(findAppenderByReference(currentElement));
                        } else {
                            LogLog.error(new StringBuffer().append("Requesting attachment of appender named [").append(refName).append("] to appender named [").append(appender.getName()).append("] which does not implement org.apache.log4j.spi.AppenderAttachable.").toString());
                        }
                    } else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return appender;
        } catch (Exception oops) {
            if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create an Appender. Reported error follows.", oops);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void parseErrorHandler(Element element, Appender appender) {
        Class cls;
        String subst = subst(element.getAttribute(CLASS_ATTR));
        if (class$org$apache$log4j$spi$ErrorHandler == null) {
            cls = class$("org.apache.log4j.spi.ErrorHandler");
            class$org$apache$log4j$spi$ErrorHandler = cls;
        } else {
            cls = class$org$apache$log4j$spi$ErrorHandler;
        }
        ErrorHandler eh = (ErrorHandler) OptionConverter.instantiateByClassName(subst, cls, (Object) null);
        if (eh != null) {
            eh.setAppender(appender);
            PropertySetter propSetter = new PropertySetter(eh);
            NodeList children = element.getChildNodes();
            int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    Element currentElement = (Element) currentNode;
                    String tagName = currentElement.getTagName();
                    if (tagName.equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else if (tagName.equals(APPENDER_REF_TAG)) {
                        eh.setBackupAppender(findAppenderByReference(currentElement));
                    } else if (tagName.equals(LOGGER_REF)) {
                        String loggerName = currentElement.getAttribute(REF_ATTR);
                        eh.setLogger(this.catFactory == null ? this.repository.getLogger(loggerName) : this.repository.getLogger(loggerName, this.catFactory));
                    } else if (tagName.equals(ROOT_REF)) {
                        eh.setLogger(this.repository.getRootLogger());
                    } else {
                        quietParseUnrecognizedElement(eh, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            appender.setErrorHandler(eh);
        }
    }

    /* access modifiers changed from: protected */
    public void parseFilters(Element element, Appender appender) {
        Class cls;
        String clazz = subst(element.getAttribute(CLASS_ATTR));
        if (class$org$apache$log4j$spi$Filter == null) {
            cls = class$("org.apache.log4j.spi.Filter");
            class$org$apache$log4j$spi$Filter = cls;
        } else {
            cls = class$org$apache$log4j$spi$Filter;
        }
        Filter filter = (Filter) OptionConverter.instantiateByClassName(clazz, cls, (Object) null);
        if (filter != null) {
            PropertySetter propSetter = new PropertySetter(filter);
            NodeList children = element.getChildNodes();
            int length = children.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = children.item(loop);
                if (currentNode.getNodeType() == 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else {
                        quietParseUnrecognizedElement(filter, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            LogLog.debug(new StringBuffer().append("Adding filter of type [").append(filter.getClass()).append("] to appender named [").append(appender.getName()).append("].").toString());
            appender.addFilter(filter);
        }
    }

    /* access modifiers changed from: protected */
    public void parseCategory(Element loggerElement) {
        Logger cat;
        String catName = subst(loggerElement.getAttribute("name"));
        String className = subst(loggerElement.getAttribute(CLASS_ATTR));
        if (EMPTY_STR.equals(className)) {
            LogLog.debug("Retreiving an instance of org.apache.log4j.Logger.");
            if (this.catFactory == null) {
                cat = this.repository.getLogger(catName);
            } else {
                cat = this.repository.getLogger(catName, this.catFactory);
            }
        } else {
            LogLog.debug(new StringBuffer().append("Desired logger sub-class: [").append(className).append(']').toString());
            try {
                cat = (Logger) Loader.loadClass(className).getMethod("getLogger", ONE_STRING_PARAM).invoke((Object) null, new Object[]{catName});
            } catch (InvocationTargetException oops) {
                if ((oops.getTargetException() instanceof InterruptedException) || (oops.getTargetException() instanceof InterruptedIOException)) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error(new StringBuffer().append("Could not retrieve category [").append(catName).append("]. Reported error follows.").toString(), oops);
                return;
            } catch (Exception oops2) {
                LogLog.error(new StringBuffer().append("Could not retrieve category [").append(catName).append("]. Reported error follows.").toString(), oops2);
                return;
            }
        }
        synchronized (cat) {
            boolean additivity = OptionConverter.toBoolean(subst(loggerElement.getAttribute(ADDITIVITY_ATTR)), true);
            LogLog.debug(new StringBuffer().append("Setting [").append(cat.getName()).append("] additivity to [").append(additivity).append("].").toString());
            cat.setAdditivity(additivity);
            parseChildrenOfLoggerElement(loggerElement, cat, false);
        }
    }

    /* access modifiers changed from: protected */
    public void parseCategoryFactory(Element factoryElement) {
        Class cls;
        String className = subst(factoryElement.getAttribute(CLASS_ATTR));
        if (EMPTY_STR.equals(className)) {
            LogLog.error("Category Factory tag class attribute not found.");
            LogLog.debug("No Category Factory configured.");
            return;
        }
        LogLog.debug(new StringBuffer().append("Desired category factory: [").append(className).append(']').toString());
        if (class$org$apache$log4j$spi$LoggerFactory == null) {
            cls = class$("org.apache.log4j.spi.LoggerFactory");
            class$org$apache$log4j$spi$LoggerFactory = cls;
        } else {
            cls = class$org$apache$log4j$spi$LoggerFactory;
        }
        Object factory = OptionConverter.instantiateByClassName(className, cls, (Object) null);
        if (factory instanceof LoggerFactory) {
            this.catFactory = (LoggerFactory) factory;
        } else {
            LogLog.error(new StringBuffer().append("Category Factory class ").append(className).append(" does not implement org.apache.log4j.LoggerFactory").toString());
        }
        PropertySetter propSetter = new PropertySetter(factory);
        NodeList children = factoryElement.getChildNodes();
        int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == 1) {
                Element currentElement = (Element) currentNode;
                if (currentElement.getTagName().equals(PARAM_TAG)) {
                    setParameter(currentElement, propSetter);
                } else {
                    quietParseUnrecognizedElement(factory, currentElement, this.props);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void parseRoot(Element rootElement) {
        Logger root = this.repository.getRootLogger();
        synchronized (root) {
            parseChildrenOfLoggerElement(rootElement, root, true);
        }
    }

    /* access modifiers changed from: protected */
    public void parseChildrenOfLoggerElement(Element catElement, Logger cat, boolean isRoot) {
        PropertySetter propSetter = new PropertySetter(cat);
        cat.removeAllAppenders();
        NodeList children = catElement.getChildNodes();
        int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == 1) {
                Element currentElement = (Element) currentNode;
                String tagName = currentElement.getTagName();
                if (tagName.equals(APPENDER_REF_TAG)) {
                    Element appenderRef = (Element) currentNode;
                    Appender appender = findAppenderByReference(appenderRef);
                    String refName = subst(appenderRef.getAttribute(REF_ATTR));
                    if (appender != null) {
                        LogLog.debug(new StringBuffer().append("Adding appender named [").append(refName).append("] to category [").append(cat.getName()).append("].").toString());
                    } else {
                        LogLog.debug(new StringBuffer().append("Appender named [").append(refName).append("] not found.").toString());
                    }
                    cat.addAppender(appender);
                } else if (tagName.equals(LEVEL_TAG)) {
                    parseLevel(currentElement, cat, isRoot);
                } else if (tagName.equals(PRIORITY_TAG)) {
                    parseLevel(currentElement, cat, isRoot);
                } else if (tagName.equals(PARAM_TAG)) {
                    setParameter(currentElement, propSetter);
                } else {
                    quietParseUnrecognizedElement(cat, currentElement, this.props);
                }
            }
        }
        propSetter.activate();
    }

    /* access modifiers changed from: protected */
    public Layout parseLayout(Element layout_element) {
        String className = subst(layout_element.getAttribute(CLASS_ATTR));
        LogLog.debug(new StringBuffer().append("Parsing layout of class: \"").append(className).append("\"").toString());
        try {
            Object instance = Loader.loadClass(className).newInstance();
            Layout layout = (Layout) instance;
            PropertySetter propSetter = new PropertySetter(layout);
            NodeList params = layout_element.getChildNodes();
            int length = params.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = params.item(loop);
                if (currentNode.getNodeType() == 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return layout;
        } catch (Exception oops) {
            if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create the Layout. Reported error follows.", oops);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void parseRenderer(Element element) {
        String renderingClass = subst(element.getAttribute(RENDERING_CLASS_ATTR));
        String renderedClass = subst(element.getAttribute(RENDERED_CLASS_ATTR));
        if (this.repository instanceof RendererSupport) {
            RendererMap.addRenderer((RendererSupport) this.repository, renderedClass, renderingClass);
        }
    }

    /* access modifiers changed from: protected */
    public ThrowableRenderer parseThrowableRenderer(Element element) {
        String className = subst(element.getAttribute(CLASS_ATTR));
        LogLog.debug(new StringBuffer().append("Parsing throwableRenderer of class: \"").append(className).append("\"").toString());
        try {
            Object instance = Loader.loadClass(className).newInstance();
            ThrowableRenderer tr = (ThrowableRenderer) instance;
            PropertySetter propSetter = new PropertySetter(tr);
            NodeList params = element.getChildNodes();
            int length = params.getLength();
            for (int loop = 0; loop < length; loop++) {
                Node currentNode = params.item(loop);
                if (currentNode.getNodeType() == 1) {
                    Element currentElement = (Element) currentNode;
                    if (currentElement.getTagName().equals(PARAM_TAG)) {
                        setParameter(currentElement, propSetter);
                    } else {
                        parseUnrecognizedElement(instance, currentElement, this.props);
                    }
                }
            }
            propSetter.activate();
            return tr;
        } catch (Exception oops) {
            if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                Thread.currentThread().interrupt();
            }
            LogLog.error("Could not create the ThrowableRenderer. Reported error follows.", oops);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void parseLevel(Element element, Logger logger, boolean isRoot) {
        String catName = logger.getName();
        if (isRoot) {
            catName = ROOT_TAG;
        }
        String priStr = subst(element.getAttribute(VALUE_ATTR));
        LogLog.debug(new StringBuffer().append("Level value for ").append(catName).append(" is  [").append(priStr).append("].").toString());
        if (!Configurator.INHERITED.equalsIgnoreCase(priStr) && !Configurator.NULL.equalsIgnoreCase(priStr)) {
            String className = subst(element.getAttribute(CLASS_ATTR));
            if (EMPTY_STR.equals(className)) {
                logger.setLevel(OptionConverter.toLevel(priStr, Level.DEBUG));
            } else {
                LogLog.debug(new StringBuffer().append("Desired Level sub-class: [").append(className).append(']').toString());
                try {
                    logger.setLevel((Level) Loader.loadClass(className).getMethod("toLevel", ONE_STRING_PARAM).invoke((Object) null, new Object[]{priStr}));
                } catch (Exception oops) {
                    if ((oops instanceof InterruptedException) || (oops instanceof InterruptedIOException)) {
                        Thread.currentThread().interrupt();
                    }
                    LogLog.error(new StringBuffer().append("Could not create level [").append(priStr).append("]. Reported error follows.").toString(), oops);
                    return;
                }
            }
        } else if (isRoot) {
            LogLog.error("Root level cannot be inherited. Ignoring directive.");
        } else {
            logger.setLevel((Level) null);
        }
        LogLog.debug(new StringBuffer().append(catName).append(" level set to ").append(logger.getLevel()).toString());
    }

    /* access modifiers changed from: protected */
    public void setParameter(Element elem, PropertySetter propSetter) {
        propSetter.setProperty(subst(elem.getAttribute("name")), subst(OptionConverter.convertSpecialChars(elem.getAttribute(VALUE_ATTR))));
    }

    public static void configure(Element element) {
        new DOMConfigurator().doConfigure(element, LogManager.getLoggerRepository());
    }

    public static void configureAndWatch(String configFilename) {
        configureAndWatch(configFilename, FileWatchdog.DEFAULT_DELAY);
    }

    public static void configureAndWatch(String configFilename, long delay) {
        XMLWatchdog xdog = new XMLWatchdog(configFilename);
        xdog.setDelay(delay);
        xdog.start();
    }

    public void doConfigure(String filename, LoggerRepository repository2) {
        doConfigure(new ParseAction(this, filename) {
            private final DOMConfigurator this$0;
            private final String val$filename;

            {
                this.this$0 = r1;
                this.val$filename = r2;
            }

            public Document parse(DocumentBuilder parser) throws SAXException, IOException {
                return parser.parse(new File(this.val$filename));
            }

            public String toString() {
                return new StringBuffer().append("file [").append(this.val$filename).append("]").toString();
            }
        }, repository2);
    }

    public void doConfigure(URL url, LoggerRepository repository2) {
        doConfigure(new ParseAction(this, url) {
            private final DOMConfigurator this$0;
            private final URL val$url;

            {
                this.this$0 = r1;
                this.val$url = r2;
            }

            public Document parse(DocumentBuilder parser) throws SAXException, IOException {
                URLConnection uConn = this.val$url.openConnection();
                uConn.setUseCaches(false);
                InputStream stream = uConn.getInputStream();
                try {
                    InputSource src = new InputSource(stream);
                    src.setSystemId(this.val$url.toString());
                    return parser.parse(src);
                } finally {
                    stream.close();
                }
            }

            public String toString() {
                return new StringBuffer().append("url [").append(this.val$url.toString()).append("]").toString();
            }
        }, repository2);
    }

    public void doConfigure(InputStream inputStream, LoggerRepository repository2) throws FactoryConfigurationError {
        doConfigure(new ParseAction(this, inputStream) {
            private final DOMConfigurator this$0;
            private final InputStream val$inputStream;

            {
                this.this$0 = r1;
                this.val$inputStream = r2;
            }

            public Document parse(DocumentBuilder parser) throws SAXException, IOException {
                InputSource inputSource = new InputSource(this.val$inputStream);
                inputSource.setSystemId("dummy://log4j.dtd");
                return parser.parse(inputSource);
            }

            public String toString() {
                return new StringBuffer().append("input stream [").append(this.val$inputStream.toString()).append("]").toString();
            }
        }, repository2);
    }

    public void doConfigure(Reader reader, LoggerRepository repository2) throws FactoryConfigurationError {
        doConfigure(new ParseAction(this, reader) {
            private final DOMConfigurator this$0;
            private final Reader val$reader;

            {
                this.this$0 = r1;
                this.val$reader = r2;
            }

            public Document parse(DocumentBuilder parser) throws SAXException, IOException {
                InputSource inputSource = new InputSource(this.val$reader);
                inputSource.setSystemId("dummy://log4j.dtd");
                return parser.parse(inputSource);
            }

            public String toString() {
                return new StringBuffer().append("reader [").append(this.val$reader.toString()).append("]").toString();
            }
        }, repository2);
    }

    /* access modifiers changed from: protected */
    public void doConfigure(InputSource inputSource, LoggerRepository repository2) throws FactoryConfigurationError {
        if (inputSource.getSystemId() == null) {
            inputSource.setSystemId("dummy://log4j.dtd");
        }
        doConfigure(new ParseAction(this, inputSource) {
            private final DOMConfigurator this$0;
            private final InputSource val$inputSource;

            {
                this.this$0 = r1;
                this.val$inputSource = r2;
            }

            public Document parse(DocumentBuilder parser) throws SAXException, IOException {
                return parser.parse(this.val$inputSource);
            }

            public String toString() {
                return new StringBuffer().append("input source [").append(this.val$inputSource.toString()).append("]").toString();
            }
        }, repository2);
    }

    private final void doConfigure(ParseAction action, LoggerRepository repository2) throws FactoryConfigurationError {
        this.repository = repository2;
        try {
            LogLog.debug(new StringBuffer().append("System property is :").append(OptionConverter.getSystemProperty(dbfKey, (String) null)).toString());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            LogLog.debug("Standard DocumentBuilderFactory search succeded.");
            LogLog.debug(new StringBuffer().append("DocumentBuilderFactory is: ").append(dbf.getClass().getName()).toString());
            try {
                dbf.setValidating(true);
                DocumentBuilder docBuilder = dbf.newDocumentBuilder();
                docBuilder.setErrorHandler(new SAXErrorHandler());
                docBuilder.setEntityResolver(new Log4jEntityResolver());
                parse(action.parse(docBuilder).getDocumentElement());
            } catch (Exception e) {
                if ((e instanceof InterruptedException) || (e instanceof InterruptedIOException)) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error(new StringBuffer().append("Could not parse ").append(action.toString()).append(".").toString(), e);
            }
        } catch (FactoryConfigurationError fce) {
            LogLog.debug("Could not instantiate a DocumentBuilderFactory.", fce.getException());
            throw fce;
        }
    }

    public void doConfigure(Element element, LoggerRepository repository2) {
        this.repository = repository2;
        parse(element);
    }

    public static void configure(String filename) throws FactoryConfigurationError {
        new DOMConfigurator().doConfigure(filename, LogManager.getLoggerRepository());
    }

    public static void configure(URL url) throws FactoryConfigurationError {
        new DOMConfigurator().doConfigure(url, LogManager.getLoggerRepository());
    }

    /* access modifiers changed from: protected */
    public void parse(Element element) {
        ThrowableRenderer tr;
        String rootElementName = element.getTagName();
        if (!rootElementName.equals(CONFIGURATION_TAG)) {
            if (rootElementName.equals(OLD_CONFIGURATION_TAG)) {
                LogLog.warn("The <configuration> element has been deprecated.");
                LogLog.warn("Use the <log4j:configuration> element instead.");
            } else {
                LogLog.error("DOM element is - not a <log4j:configuration> element.");
                return;
            }
        }
        String debugAttrib = subst(element.getAttribute(INTERNAL_DEBUG_ATTR));
        LogLog.debug(new StringBuffer().append("debug attribute= \"").append(debugAttrib).append("\".").toString());
        if (debugAttrib.equals(EMPTY_STR) || debugAttrib.equals(Configurator.NULL)) {
            LogLog.debug("Ignoring debug attribute.");
        } else {
            LogLog.setInternalDebugging(OptionConverter.toBoolean(debugAttrib, true));
        }
        String resetAttrib = subst(element.getAttribute(RESET_ATTR));
        LogLog.debug(new StringBuffer().append("reset attribute= \"").append(resetAttrib).append("\".").toString());
        if (!EMPTY_STR.equals(resetAttrib) && OptionConverter.toBoolean(resetAttrib, false)) {
            this.repository.resetConfiguration();
        }
        String confDebug = subst(element.getAttribute(CONFIG_DEBUG_ATTR));
        if (!confDebug.equals(EMPTY_STR) && !confDebug.equals(Configurator.NULL)) {
            LogLog.warn("The \"configDebug\" attribute is deprecated.");
            LogLog.warn("Use the \"debug\" attribute instead.");
            LogLog.setInternalDebugging(OptionConverter.toBoolean(confDebug, true));
        }
        String thresholdStr = subst(element.getAttribute(THRESHOLD_ATTR));
        LogLog.debug(new StringBuffer().append("Threshold =\"").append(thresholdStr).append("\".").toString());
        if (!EMPTY_STR.equals(thresholdStr) && !Configurator.NULL.equals(thresholdStr)) {
            this.repository.setThreshold(thresholdStr);
        }
        NodeList children = element.getChildNodes();
        int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == 1) {
                Element currentElement = (Element) currentNode;
                String tagName = currentElement.getTagName();
                if (tagName.equals(CATEGORY_FACTORY_TAG) || tagName.equals(LOGGER_FACTORY_TAG)) {
                    parseCategoryFactory(currentElement);
                }
            }
        }
        for (int loop2 = 0; loop2 < length; loop2++) {
            Node currentNode2 = children.item(loop2);
            if (currentNode2.getNodeType() == 1) {
                Element currentElement2 = (Element) currentNode2;
                String tagName2 = currentElement2.getTagName();
                if (tagName2.equals(CATEGORY) || tagName2.equals(LOGGER)) {
                    parseCategory(currentElement2);
                } else if (tagName2.equals(ROOT_TAG)) {
                    parseRoot(currentElement2);
                } else if (tagName2.equals(RENDERER_TAG)) {
                    parseRenderer(currentElement2);
                } else if (tagName2.equals(THROWABLE_RENDERER_TAG)) {
                    if ((this.repository instanceof ThrowableRendererSupport) && (tr = parseThrowableRenderer(currentElement2)) != null) {
                        ((ThrowableRendererSupport) this.repository).setThrowableRenderer(tr);
                    }
                } else if (!tagName2.equals(APPENDER_TAG) && !tagName2.equals(CATEGORY_FACTORY_TAG) && !tagName2.equals(LOGGER_FACTORY_TAG)) {
                    quietParseUnrecognizedElement(this.repository, currentElement2, this.props);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public String subst(String value) {
        return subst(value, this.props);
    }

    public static String subst(String value, Properties props2) {
        try {
            return OptionConverter.substVars(value, props2);
        } catch (IllegalArgumentException e) {
            LogLog.warn("Could not perform variable substitution.", e);
            return value;
        }
    }

    public static void setParameter(Element elem, PropertySetter propSetter, Properties props2) {
        propSetter.setProperty(subst(elem.getAttribute("name"), props2), subst(OptionConverter.convertSpecialChars(elem.getAttribute(VALUE_ATTR)), props2));
    }

    public static Object parseElement(Element element, Properties props2, Class expectedClass) throws Exception {
        Object instance = OptionConverter.instantiateByClassName(subst(element.getAttribute(CLASS_ATTR), props2), expectedClass, (Object) null);
        if (instance == null) {
            return null;
        }
        PropertySetter propSetter = new PropertySetter(instance);
        NodeList children = element.getChildNodes();
        int length = children.getLength();
        for (int loop = 0; loop < length; loop++) {
            Node currentNode = children.item(loop);
            if (currentNode.getNodeType() == 1) {
                Element currentElement = (Element) currentNode;
                if (currentElement.getTagName().equals(PARAM_TAG)) {
                    setParameter(currentElement, propSetter, props2);
                } else {
                    parseUnrecognizedElement(instance, currentElement, props2);
                }
            }
        }
        return instance;
    }
}
