package org.apache.log4j.rewrite;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

public class MapRewritePolicy implements RewritePolicy {
    public LoggingEvent rewrite(LoggingEvent source) {
        Category logger;
        Object msg = source.getMessage();
        if (!(msg instanceof Map)) {
            return source;
        }
        Map props = new HashMap(source.getProperties());
        Map eventProps = (Map) msg;
        Object newMsg = eventProps.get("message");
        if (newMsg == null) {
            newMsg = msg;
        }
        for (Map.Entry entry : eventProps.entrySet()) {
            if (!"message".equals(entry.getKey())) {
                props.put(entry.getKey(), entry.getValue());
            }
        }
        String fQNOfLoggerClass = source.getFQNOfLoggerClass();
        if (source.getLogger() != null) {
            logger = source.getLogger();
        } else {
            logger = Logger.getLogger(source.getLoggerName());
        }
        return new LoggingEvent(fQNOfLoggerClass, logger, source.getTimeStamp(), source.getLevel(), newMsg, source.getThreadName(), source.getThrowableInformation(), source.getNDC(), source.getLocationInformation(), props);
    }
}
