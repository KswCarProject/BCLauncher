package org.apache.log4j.net;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.helpers.LogLog;

public class SocketHubAppender extends AppenderSkeleton {
    static final int DEFAULT_PORT = 4560;
    public static final String ZONE = "_log4j_obj_tcpaccept_appender.local.";
    private boolean advertiseViaMulticastDNS;
    private String application;
    private CyclicBuffer buffer = null;
    private boolean locationInfo = false;
    private Vector oosList = new Vector();
    private int port = 4560;
    private ServerMonitor serverMonitor = null;
    private ServerSocket serverSocket;
    private ZeroConfSupport zeroConf;

    static ServerSocket access$000(SocketHubAppender x0) {
        return x0.serverSocket;
    }

    static ServerSocket access$002(SocketHubAppender x0, ServerSocket x1) {
        x0.serverSocket = x1;
        return x1;
    }

    static CyclicBuffer access$100(SocketHubAppender x0) {
        return x0.buffer;
    }

    public SocketHubAppender() {
    }

    public SocketHubAppender(int _port) {
        this.port = _port;
        startServer();
    }

    public void activateOptions() {
        if (this.advertiseViaMulticastDNS) {
            this.zeroConf = new ZeroConfSupport(ZONE, this.port, getName());
            this.zeroConf.advertise();
        }
        startServer();
    }

    public synchronized void close() {
        if (!this.closed) {
            LogLog.debug(new StringBuffer().append("closing SocketHubAppender ").append(getName()).toString());
            this.closed = true;
            if (this.advertiseViaMulticastDNS) {
                this.zeroConf.unadvertise();
            }
            cleanUp();
            LogLog.debug(new StringBuffer().append("SocketHubAppender ").append(getName()).append(" closed").toString());
        }
    }

    public void cleanUp() {
        LogLog.debug("stopping ServerSocket");
        this.serverMonitor.stopMonitor();
        this.serverMonitor = null;
        LogLog.debug("closing client connections");
        while (this.oosList.size() != 0) {
            ObjectOutputStream oos = (ObjectOutputStream) this.oosList.elementAt(0);
            if (oos != null) {
                try {
                    oos.close();
                } catch (InterruptedIOException e) {
                    Thread.currentThread().interrupt();
                    LogLog.error("could not close oos.", e);
                } catch (IOException e2) {
                    LogLog.error("could not close oos.", e2);
                }
                this.oosList.removeElementAt(0);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.io.ObjectOutputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void append(org.apache.log4j.spi.LoggingEvent r7) {
        /*
            r6 = this;
            if (r7 == 0) goto L_0x002c
            boolean r4 = r6.locationInfo
            if (r4 == 0) goto L_0x0009
            r7.getLocationInformation()
        L_0x0009:
            java.lang.String r4 = r6.application
            if (r4 == 0) goto L_0x0014
            java.lang.String r4 = "application"
            java.lang.String r5 = r6.application
            r7.setProperty(r4, r5)
        L_0x0014:
            r7.getNDC()
            r7.getThreadName()
            r7.getMDCCopy()
            r7.getRenderedMessage()
            r7.getThrowableStrRep()
            org.apache.log4j.helpers.CyclicBuffer r4 = r6.buffer
            if (r4 == 0) goto L_0x002c
            org.apache.log4j.helpers.CyclicBuffer r4 = r6.buffer
            r4.add(r7)
        L_0x002c:
            if (r7 == 0) goto L_0x0036
            java.util.Vector r4 = r6.oosList
            int r4 = r4.size()
            if (r4 != 0) goto L_0x0037
        L_0x0036:
            return
        L_0x0037:
            r3 = 0
        L_0x0038:
            java.util.Vector r4 = r6.oosList
            int r4 = r4.size()
            if (r3 >= r4) goto L_0x0036
            r2 = 0
            java.util.Vector r4 = r6.oosList     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0072 }
            java.lang.Object r4 = r4.elementAt(r3)     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0072 }
            r0 = r4
            java.io.ObjectOutputStream r0 = (java.io.ObjectOutputStream) r0     // Catch:{ ArrayIndexOutOfBoundsException -> 0x0072 }
            r2 = r0
        L_0x004b:
            if (r2 == 0) goto L_0x0036
            r2.writeObject(r7)     // Catch:{ IOException -> 0x0059 }
            r2.flush()     // Catch:{ IOException -> 0x0059 }
            r2.reset()     // Catch:{ IOException -> 0x0059 }
        L_0x0056:
            int r3 = r3 + 1
            goto L_0x0038
        L_0x0059:
            r1 = move-exception
            boolean r4 = r1 instanceof java.io.InterruptedIOException
            if (r4 == 0) goto L_0x0065
            java.lang.Thread r4 = java.lang.Thread.currentThread()
            r4.interrupt()
        L_0x0065:
            java.util.Vector r4 = r6.oosList
            r4.removeElementAt(r3)
            java.lang.String r4 = "dropped connection"
            org.apache.log4j.helpers.LogLog.debug(r4)
            int r3 = r3 + -1
            goto L_0x0056
        L_0x0072:
            r4 = move-exception
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.net.SocketHubAppender.append(org.apache.log4j.spi.LoggingEvent):void");
    }

    public boolean requiresLayout() {
        return false;
    }

    public void setPort(int _port) {
        this.port = _port;
    }

    public void setApplication(String lapp) {
        this.application = lapp;
    }

    public String getApplication() {
        return this.application;
    }

    public int getPort() {
        return this.port;
    }

    public void setBufferSize(int _bufferSize) {
        this.buffer = new CyclicBuffer(_bufferSize);
    }

    public int getBufferSize() {
        if (this.buffer == null) {
            return 0;
        }
        return this.buffer.getMaxSize();
    }

    public void setLocationInfo(boolean _locationInfo) {
        this.locationInfo = _locationInfo;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void setAdvertiseViaMulticastDNS(boolean advertiseViaMulticastDNS2) {
        this.advertiseViaMulticastDNS = advertiseViaMulticastDNS2;
    }

    public boolean isAdvertiseViaMulticastDNS() {
        return this.advertiseViaMulticastDNS;
    }

    private void startServer() {
        this.serverMonitor = new ServerMonitor(this, this.port, this.oosList);
    }

    /* access modifiers changed from: protected */
    public ServerSocket createServerSocket(int socketPort) throws IOException {
        return new ServerSocket(socketPort);
    }

    private class ServerMonitor implements Runnable {
        private boolean keepRunning = true;
        private Thread monitorThread = new Thread(this);
        private Vector oosList;
        private int port;
        private final SocketHubAppender this$0;

        public ServerMonitor(SocketHubAppender socketHubAppender, int _port, Vector _oosList) {
            this.this$0 = socketHubAppender;
            this.port = _port;
            this.oosList = _oosList;
            this.monitorThread.setDaemon(true);
            this.monitorThread.setName(new StringBuffer().append("SocketHubAppender-Monitor-").append(this.port).toString());
            this.monitorThread.start();
        }

        public synchronized void stopMonitor() {
            if (this.keepRunning) {
                LogLog.debug("server monitor thread shutting down");
                this.keepRunning = false;
                try {
                    if (SocketHubAppender.access$000(this.this$0) != null) {
                        SocketHubAppender.access$000(this.this$0).close();
                        SocketHubAppender.access$002(this.this$0, (ServerSocket) null);
                    }
                } catch (IOException e) {
                }
                try {
                    this.monitorThread.join();
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                }
                this.monitorThread = null;
                LogLog.debug("server monitor thread shut down");
            }
            return;
        }

        private void sendCachedEvents(ObjectOutputStream stream) throws IOException {
            if (SocketHubAppender.access$100(this.this$0) != null) {
                for (int i = 0; i < SocketHubAppender.access$100(this.this$0).length(); i++) {
                    stream.writeObject(SocketHubAppender.access$100(this.this$0).get(i));
                }
                stream.flush();
                stream.reset();
            }
        }

        public void run() {
            SocketHubAppender.access$002(this.this$0, (ServerSocket) null);
            try {
                SocketHubAppender.access$002(this.this$0, this.this$0.createServerSocket(this.port));
                SocketHubAppender.access$000(this.this$0).setSoTimeout(1000);
                try {
                    SocketHubAppender.access$000(this.this$0).setSoTimeout(1000);
                    while (this.keepRunning) {
                        try {
                            Socket socket = null;
                            try {
                                socket = SocketHubAppender.access$000(this.this$0).accept();
                            } catch (InterruptedIOException e) {
                            } catch (SocketException e2) {
                                LogLog.error("exception accepting socket, shutting down server socket.", e2);
                                this.keepRunning = false;
                            } catch (IOException e3) {
                                LogLog.error("exception accepting socket.", e3);
                            }
                            if (socket != null) {
                                InetAddress remoteAddress = socket.getInetAddress();
                                LogLog.debug(new StringBuffer().append("accepting connection from ").append(remoteAddress.getHostName()).append(" (").append(remoteAddress.getHostAddress()).append(")").toString());
                                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                if (SocketHubAppender.access$100(this.this$0) != null && SocketHubAppender.access$100(this.this$0).length() > 0) {
                                    sendCachedEvents(oos);
                                }
                                this.oosList.addElement(oos);
                            }
                        } catch (IOException e4) {
                            if (e4 instanceof InterruptedIOException) {
                                Thread.currentThread().interrupt();
                            }
                            LogLog.error("exception creating output stream on socket.", e4);
                        } catch (Throwable th) {
                            try {
                                SocketHubAppender.access$000(this.this$0).close();
                            } catch (InterruptedIOException e5) {
                                Thread.currentThread().interrupt();
                            } catch (IOException e6) {
                            }
                            throw th;
                        }
                    }
                    try {
                        SocketHubAppender.access$000(this.this$0).close();
                    } catch (InterruptedIOException e7) {
                        Thread.currentThread().interrupt();
                    } catch (IOException e8) {
                    }
                } catch (SocketException e9) {
                    LogLog.error("exception setting timeout, shutting down server socket.", e9);
                    try {
                        SocketHubAppender.access$000(this.this$0).close();
                    } catch (InterruptedIOException e10) {
                        Thread.currentThread().interrupt();
                    } catch (IOException e11) {
                    }
                }
            } catch (Exception e12) {
                if ((e12 instanceof InterruptedIOException) || (e12 instanceof InterruptedException)) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("exception setting timeout, shutting down server socket.", e12);
                this.keepRunning = false;
            }
        }
    }
}
