package org.apache.log4j;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;

public class AsyncAppender extends AppenderSkeleton implements AppenderAttachable {
    public static final int DEFAULT_BUFFER_SIZE = 128;
    AppenderAttachableImpl aai = this.appenders;
    private final AppenderAttachableImpl appenders = new AppenderAttachableImpl();
    private boolean blocking = true;
    private final List buffer = new ArrayList();
    private int bufferSize = 128;
    private final Map discardMap = new HashMap();
    private final Thread dispatcher = new Thread(new Dispatcher(this, this.buffer, this.discardMap, this.appenders));
    private boolean locationInfo = false;

    public AsyncAppender() {
        this.dispatcher.setDaemon(true);
        this.dispatcher.setName(new StringBuffer().append("AsyncAppender-Dispatcher-").append(this.dispatcher.getName()).toString());
        this.dispatcher.start();
    }

    public void addAppender(Appender newAppender) {
        synchronized (this.appenders) {
            this.appenders.addAppender(newAppender);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        r2 = r9.getLoggerName();
        r4 = (org.apache.log4j.AsyncAppender.DiscardSummary) r8.discardMap.get(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0078, code lost:
        if (r4 != null) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x007a, code lost:
        r8.discardMap.put(r2, new org.apache.log4j.AsyncAppender.DiscardSummary(r9));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x008e, code lost:
        r4.add(r9);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void append(org.apache.log4j.spi.LoggingEvent r9) {
        /*
            r8 = this;
            java.lang.Thread r5 = r8.dispatcher
            if (r5 == 0) goto L_0x0010
            java.lang.Thread r5 = r8.dispatcher
            boolean r5 = r5.isAlive()
            if (r5 == 0) goto L_0x0010
            int r5 = r8.bufferSize
            if (r5 > 0) goto L_0x001d
        L_0x0010:
            org.apache.log4j.helpers.AppenderAttachableImpl r6 = r8.appenders
            monitor-enter(r6)
            org.apache.log4j.helpers.AppenderAttachableImpl r5 = r8.appenders     // Catch:{ all -> 0x001a }
            r5.appendLoopOnAppenders(r9)     // Catch:{ all -> 0x001a }
            monitor-exit(r6)     // Catch:{ all -> 0x001a }
        L_0x0019:
            return
        L_0x001a:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x001a }
            throw r5
        L_0x001d:
            r9.getNDC()
            r9.getThreadName()
            r9.getMDCCopy()
            boolean r5 = r8.locationInfo
            if (r5 == 0) goto L_0x002d
            r9.getLocationInformation()
        L_0x002d:
            r9.getRenderedMessage()
            r9.getThrowableStrRep()
            java.util.List r6 = r8.buffer
            monitor-enter(r6)
        L_0x0036:
            java.util.List r5 = r8.buffer     // Catch:{ all -> 0x004e }
            int r3 = r5.size()     // Catch:{ all -> 0x004e }
            int r5 = r8.bufferSize     // Catch:{ all -> 0x004e }
            if (r3 >= r5) goto L_0x0051
            java.util.List r5 = r8.buffer     // Catch:{ all -> 0x004e }
            r5.add(r9)     // Catch:{ all -> 0x004e }
            if (r3 != 0) goto L_0x004c
            java.util.List r5 = r8.buffer     // Catch:{ all -> 0x004e }
            r5.notifyAll()     // Catch:{ all -> 0x004e }
        L_0x004c:
            monitor-exit(r6)     // Catch:{ all -> 0x004e }
            goto L_0x0019
        L_0x004e:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x004e }
            throw r5
        L_0x0051:
            r0 = 1
            boolean r5 = r8.blocking     // Catch:{ all -> 0x004e }
            if (r5 == 0) goto L_0x006a
            boolean r5 = java.lang.Thread.interrupted()     // Catch:{ all -> 0x004e }
            if (r5 != 0) goto L_0x006a
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x004e }
            java.lang.Thread r7 = r8.dispatcher     // Catch:{ all -> 0x004e }
            if (r5 == r7) goto L_0x006a
            java.util.List r5 = r8.buffer     // Catch:{ InterruptedException -> 0x0085 }
            r5.wait()     // Catch:{ InterruptedException -> 0x0085 }
            r0 = 0
        L_0x006a:
            if (r0 == 0) goto L_0x0036
            java.lang.String r2 = r9.getLoggerName()     // Catch:{ all -> 0x004e }
            java.util.Map r5 = r8.discardMap     // Catch:{ all -> 0x004e }
            java.lang.Object r4 = r5.get(r2)     // Catch:{ all -> 0x004e }
            org.apache.log4j.AsyncAppender$DiscardSummary r4 = (org.apache.log4j.AsyncAppender.DiscardSummary) r4     // Catch:{ all -> 0x004e }
            if (r4 != 0) goto L_0x008e
            org.apache.log4j.AsyncAppender$DiscardSummary r4 = new org.apache.log4j.AsyncAppender$DiscardSummary     // Catch:{ all -> 0x004e }
            r4.<init>(r9)     // Catch:{ all -> 0x004e }
            java.util.Map r5 = r8.discardMap     // Catch:{ all -> 0x004e }
            r5.put(r2, r4)     // Catch:{ all -> 0x004e }
            goto L_0x004c
        L_0x0085:
            r1 = move-exception
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x004e }
            r5.interrupt()     // Catch:{ all -> 0x004e }
            goto L_0x006a
        L_0x008e:
            r4.add(r9)     // Catch:{ all -> 0x004e }
            goto L_0x004c
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.AsyncAppender.append(org.apache.log4j.spi.LoggingEvent):void");
    }

    public void close() {
        synchronized (this.buffer) {
            this.closed = true;
            this.buffer.notifyAll();
        }
        try {
            this.dispatcher.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogLog.error("Got an InterruptedException while waiting for the dispatcher to finish.", e);
        }
        synchronized (this.appenders) {
            Enumeration iter = this.appenders.getAllAppenders();
            if (iter != null) {
                while (iter.hasMoreElements()) {
                    Object next = iter.nextElement();
                    if (next instanceof Appender) {
                        ((Appender) next).close();
                    }
                }
            }
        }
    }

    public Enumeration getAllAppenders() {
        Enumeration allAppenders;
        synchronized (this.appenders) {
            allAppenders = this.appenders.getAllAppenders();
        }
        return allAppenders;
    }

    public Appender getAppender(String name) {
        Appender appender;
        synchronized (this.appenders) {
            appender = this.appenders.getAppender(name);
        }
        return appender;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public boolean isAttached(Appender appender) {
        boolean isAttached;
        synchronized (this.appenders) {
            isAttached = this.appenders.isAttached(appender);
        }
        return isAttached;
    }

    public boolean requiresLayout() {
        return false;
    }

    public void removeAllAppenders() {
        synchronized (this.appenders) {
            this.appenders.removeAllAppenders();
        }
    }

    public void removeAppender(Appender appender) {
        synchronized (this.appenders) {
            this.appenders.removeAppender(appender);
        }
    }

    public void removeAppender(String name) {
        synchronized (this.appenders) {
            this.appenders.removeAppender(name);
        }
    }

    public void setLocationInfo(boolean flag) {
        this.locationInfo = flag;
    }

    public void setBufferSize(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException("size");
        }
        synchronized (this.buffer) {
            if (size < 1) {
                size = 1;
            }
            this.bufferSize = size;
            this.buffer.notifyAll();
        }
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBlocking(boolean value) {
        synchronized (this.buffer) {
            this.blocking = value;
            this.buffer.notifyAll();
        }
    }

    public boolean getBlocking() {
        return this.blocking;
    }

    private static final class DiscardSummary {
        private int count = 1;
        private LoggingEvent maxEvent;

        public DiscardSummary(LoggingEvent event) {
            this.maxEvent = event;
        }

        public void add(LoggingEvent event) {
            if (event.getLevel().toInt() > this.maxEvent.getLevel().toInt()) {
                this.maxEvent = event;
            }
            this.count++;
        }

        public LoggingEvent createEvent() {
            return new LoggingEvent("org.apache.log4j.AsyncAppender.DONT_REPORT_LOCATION", Logger.getLogger(this.maxEvent.getLoggerName()), this.maxEvent.getLevel(), MessageFormat.format("Discarded {0} messages due to full event buffer including: {1}", new Object[]{new Integer(this.count), this.maxEvent.getMessage()}), (Throwable) null);
        }
    }

    private static class Dispatcher implements Runnable {
        private final AppenderAttachableImpl appenders;
        private final List buffer;
        private final Map discardMap;
        private final AsyncAppender parent;

        public Dispatcher(AsyncAppender parent2, List buffer2, Map discardMap2, AppenderAttachableImpl appenders2) {
            this.parent = parent2;
            this.buffer = buffer2;
            this.appenders = appenders2;
            this.discardMap = discardMap2;
        }

        public void run() {
            boolean isActive = true;
            while (isActive) {
                LoggingEvent[] events = null;
                try {
                    synchronized (this.buffer) {
                        int bufferSize = this.buffer.size();
                        isActive = !this.parent.closed;
                        while (bufferSize == 0 && isActive) {
                            this.buffer.wait();
                            bufferSize = this.buffer.size();
                            isActive = !this.parent.closed;
                        }
                        if (bufferSize > 0) {
                            events = new LoggingEvent[(this.discardMap.size() + bufferSize)];
                            this.buffer.toArray(events);
                            int index = bufferSize;
                            for (DiscardSummary createEvent : this.discardMap.values()) {
                                events[index] = createEvent.createEvent();
                                index++;
                            }
                            this.buffer.clear();
                            this.discardMap.clear();
                            this.buffer.notifyAll();
                        }
                    }
                    if (events != null) {
                        for (LoggingEvent appendLoopOnAppenders : events) {
                            synchronized (this.appenders) {
                                this.appenders.appendLoopOnAppenders(appendLoopOnAppenders);
                            }
                        }
                        continue;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
