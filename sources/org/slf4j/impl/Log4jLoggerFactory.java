package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.LogManager;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class Log4jLoggerFactory implements ILoggerFactory {
    ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap();

    public Logger getLogger(String name) {
        org.apache.log4j.Logger log4jLogger;
        Logger slf4jLogger = (Logger) this.loggerMap.get(name);
        if (slf4jLogger != null) {
            return slf4jLogger;
        }
        if (name.equalsIgnoreCase(Logger.ROOT_LOGGER_NAME)) {
            log4jLogger = LogManager.getRootLogger();
        } else {
            log4jLogger = LogManager.getLogger(name);
        }
        Logger newInstance = new Log4jLoggerAdapter(log4jLogger);
        Logger oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
        if (oldInstance != null) {
            newInstance = oldInstance;
        }
        return newInstance;
    }
}
