package com.sun.mail.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SocketFetcher {
    private SocketFetcher() {
    }

    public static Socket getSocket(String host, int port, Properties props, String prefix, boolean useSSL) throws IOException {
        if (prefix == null) {
            prefix = "socket";
        }
        if (props == null) {
            props = new Properties();
        }
        String s = props.getProperty(String.valueOf(prefix) + ".connectiontimeout", (String) null);
        int cto = -1;
        if (s != null) {
            try {
                cto = Integer.parseInt(s);
            } catch (NumberFormatException e) {
            }
        }
        Socket socket = null;
        String timeout = props.getProperty(String.valueOf(prefix) + ".timeout", (String) null);
        String localaddrstr = props.getProperty(String.valueOf(prefix) + ".localaddress", (String) null);
        InetAddress localaddr = null;
        if (localaddrstr != null) {
            localaddr = InetAddress.getByName(localaddrstr);
        }
        String localportstr = props.getProperty(String.valueOf(prefix) + ".localport", (String) null);
        int localport = 0;
        if (localportstr != null) {
            try {
                localport = Integer.parseInt(localportstr);
            } catch (NumberFormatException e2) {
            }
        }
        String fallback = props.getProperty(String.valueOf(prefix) + ".socketFactory.fallback", (String) null);
        boolean fb = fallback == null || !fallback.equalsIgnoreCase("false");
        String sfClass = props.getProperty(String.valueOf(prefix) + ".socketFactory.class", (String) null);
        int sfPort = -1;
        try {
            SocketFactory sf = getSocketFactory(sfClass);
            if (sf != null) {
                String sfPortStr = props.getProperty(String.valueOf(prefix) + ".socketFactory.port", (String) null);
                if (sfPortStr != null) {
                    try {
                        sfPort = Integer.parseInt(sfPortStr);
                    } catch (NumberFormatException e3) {
                    }
                }
                if (sfPort == -1) {
                    sfPort = port;
                }
                socket = createSocket(localaddr, localport, host, sfPort, cto, sf, useSSL);
            }
        } catch (SocketTimeoutException sex) {
            throw sex;
        } catch (Exception e4) {
            ex = e4;
            if (!fb) {
                if (ex instanceof InvocationTargetException) {
                    Throwable t = ((InvocationTargetException) ex).getTargetException();
                    if (t instanceof Exception) {
                        ex = (Exception) t;
                    }
                }
                if (ex instanceof IOException) {
                    throw ((IOException) ex);
                }
                IOException iOException = new IOException("Couldn't connect using \"" + sfClass + "\" socket factory to host, port: " + host + ", " + sfPort + "; Exception: " + ex);
                iOException.initCause(ex);
                throw iOException;
            }
        }
        if (socket == null) {
            socket = createSocket(localaddr, localport, host, port, cto, (SocketFactory) null, useSSL);
        }
        int to = -1;
        if (timeout != null) {
            try {
                to = Integer.parseInt(timeout);
            } catch (NumberFormatException e5) {
            }
        }
        if (to >= 0) {
            socket.setSoTimeout(to);
        }
        configureSSLSocket(socket, props, prefix);
        return socket;
    }

    public static Socket getSocket(String host, int port, Properties props, String prefix) throws IOException {
        return getSocket(host, port, props, prefix, false);
    }

    private static Socket createSocket(InetAddress localaddr, int localport, String host, int port, int cto, SocketFactory sf, boolean useSSL) throws IOException {
        Socket socket;
        if (sf != null) {
            socket = sf.createSocket();
        } else if (useSSL) {
            socket = SSLSocketFactory.getDefault().createSocket();
        } else {
            socket = new Socket();
        }
        if (localaddr != null) {
            socket.bind(new InetSocketAddress(localaddr, localport));
        }
        if (cto >= 0) {
            socket.connect(new InetSocketAddress(host, port), cto);
        } else {
            socket.connect(new InetSocketAddress(host, port));
        }
        return socket;
    }

    private static SocketFactory getSocketFactory(String sfClass) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (sfClass == null || sfClass.length() == 0) {
            return null;
        }
        ClassLoader cl = getContextClassLoader();
        Class clsSockFact = null;
        if (cl != null) {
            try {
                clsSockFact = cl.loadClass(sfClass);
            } catch (ClassNotFoundException e) {
            }
        }
        if (clsSockFact == null) {
            clsSockFact = Class.forName(sfClass);
        }
        return (SocketFactory) clsSockFact.getMethod("getDefault", new Class[0]).invoke(new Object(), new Object[0]);
    }

    public static Socket startTLS(Socket socket) throws IOException {
        return startTLS(socket, new Properties(), "socket");
    }

    /* JADX WARNING: type inference failed for: r9v0, types: [java.lang.Throwable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.net.Socket startTLS(java.net.Socket r12, java.util.Properties r13, java.lang.String r14) throws java.io.IOException {
        /*
            java.net.InetAddress r1 = r12.getInetAddress()
            java.lang.String r3 = r1.getHostName()
            int r5 = r12.getPort()
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0042 }
            java.lang.String r11 = java.lang.String.valueOf(r14)     // Catch:{ Exception -> 0x0042 }
            r10.<init>(r11)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r11 = ".socketFactory.class"
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x0042 }
            r11 = 0
            java.lang.String r7 = r13.getProperty(r10, r11)     // Catch:{ Exception -> 0x0042 }
            javax.net.SocketFactory r6 = getSocketFactory(r7)     // Catch:{ Exception -> 0x0042 }
            if (r6 == 0) goto L_0x003b
            boolean r10 = r6 instanceof javax.net.ssl.SSLSocketFactory     // Catch:{ Exception -> 0x0042 }
            if (r10 == 0) goto L_0x003b
            r0 = r6
            javax.net.ssl.SSLSocketFactory r0 = (javax.net.ssl.SSLSocketFactory) r0     // Catch:{ Exception -> 0x0042 }
            r8 = r0
        L_0x0032:
            r10 = 1
            java.net.Socket r12 = r8.createSocket(r12, r3, r5, r10)     // Catch:{ Exception -> 0x0042 }
            configureSSLSocket(r12, r13, r14)     // Catch:{ Exception -> 0x0042 }
            return r12
        L_0x003b:
            javax.net.SocketFactory r8 = javax.net.ssl.SSLSocketFactory.getDefault()     // Catch:{ Exception -> 0x0042 }
            javax.net.ssl.SSLSocketFactory r8 = (javax.net.ssl.SSLSocketFactory) r8     // Catch:{ Exception -> 0x0042 }
            goto L_0x0032
        L_0x0042:
            r2 = move-exception
            boolean r10 = r2 instanceof java.lang.reflect.InvocationTargetException
            if (r10 == 0) goto L_0x0055
            r10 = r2
            java.lang.reflect.InvocationTargetException r10 = (java.lang.reflect.InvocationTargetException) r10
            java.lang.Throwable r9 = r10.getTargetException()
            boolean r10 = r9 instanceof java.lang.Exception
            if (r10 == 0) goto L_0x0055
            r2 = r9
            java.lang.Exception r2 = (java.lang.Exception) r2
        L_0x0055:
            boolean r10 = r2 instanceof java.io.IOException
            if (r10 == 0) goto L_0x005c
            java.io.IOException r2 = (java.io.IOException) r2
            throw r2
        L_0x005c:
            java.io.IOException r4 = new java.io.IOException
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = "Exception in startTLS: host "
            r10.<init>(r11)
            java.lang.StringBuilder r10 = r10.append(r3)
            java.lang.String r11 = ", port "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r5)
            java.lang.String r11 = "; Exception: "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r2)
            java.lang.String r10 = r10.toString()
            r4.<init>(r10)
            r4.initCause(r2)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.util.SocketFetcher.startTLS(java.net.Socket, java.util.Properties, java.lang.String):java.net.Socket");
    }

    private static void configureSSLSocket(Socket socket, Properties props, String prefix) {
        if (socket instanceof SSLSocket) {
            SSLSocket sslsocket = (SSLSocket) socket;
            String protocols = props.getProperty(String.valueOf(prefix) + ".ssl.protocols", (String) null);
            if (protocols != null) {
                sslsocket.setEnabledProtocols(stringArray(protocols));
            } else {
                sslsocket.setEnabledProtocols(new String[]{"TLSv1"});
            }
            String ciphers = props.getProperty(String.valueOf(prefix) + ".ssl.ciphersuites", (String) null);
            if (ciphers != null) {
                sslsocket.setEnabledCipherSuites(stringArray(ciphers));
            }
        }
    }

    private static String[] stringArray(String s) {
        StringTokenizer st = new StringTokenizer(s);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
        return (String[]) tokens.toArray(new String[tokens.size()]);
    }

    private static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    return Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e) {
                    return null;
                }
            }
        });
    }
}
