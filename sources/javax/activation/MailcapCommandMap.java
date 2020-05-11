package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MailcapFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MailcapCommandMap extends CommandMap {
    private static final int PROG = 0;
    private static MailcapFile defDB = null;
    private MailcapFile[] DB;

    public MailcapCommandMap() {
        MailcapFile mf;
        List dbv = new ArrayList(5);
        dbv.add((Object) null);
        LogSupport.log("MailcapCommandMap: load HOME");
        try {
            String user_home = System.getProperty("user.home");
            if (!(user_home == null || (mf = loadFile(String.valueOf(user_home) + File.separator + ".mailcap")) == null)) {
                dbv.add(mf);
            }
        } catch (SecurityException e) {
        }
        LogSupport.log("MailcapCommandMap: load SYS");
        try {
            MailcapFile mf2 = loadFile(String.valueOf(System.getProperty("java.home")) + File.separator + "lib" + File.separator + "mailcap");
            if (mf2 != null) {
                dbv.add(mf2);
            }
        } catch (SecurityException e2) {
        }
        LogSupport.log("MailcapCommandMap: load JAR");
        loadAllResources(dbv, "mailcap");
        LogSupport.log("MailcapCommandMap: load DEF");
        synchronized (MailcapCommandMap.class) {
            if (defDB == null) {
                defDB = loadResource("mailcap.default");
            }
        }
        if (defDB != null) {
            dbv.add(defDB);
        }
        this.DB = new MailcapFile[dbv.size()];
        this.DB = (MailcapFile[]) dbv.toArray(this.DB);
    }

    private MailcapFile loadResource(String name) {
        InputStream clis = null;
        try {
            clis = SecuritySupport.getResourceAsStream(getClass(), name);
            if (clis != null) {
                MailcapFile mf = new MailcapFile(clis);
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: successfully loaded mailcap file: " + name);
                }
                if (clis == null) {
                    return mf;
                }
                try {
                    clis.close();
                    return mf;
                } catch (IOException e) {
                    return mf;
                }
            } else {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: not loading mailcap file: " + name);
                }
                if (clis != null) {
                    try {
                        clis.close();
                    } catch (IOException e2) {
                    }
                }
                return null;
            }
        } catch (IOException e3) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: can't load " + name, e3);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e4) {
                }
            }
        } catch (SecurityException sex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: can't load " + name, sex);
            }
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e5) {
                }
            }
        } catch (Throwable th) {
            if (clis != null) {
                try {
                    clis.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    private void loadAllResources(List v, String name) {
        URL[] urls;
        boolean anyLoaded = false;
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            if (cld != null) {
                urls = SecuritySupport.getResources(cld, name);
            } else {
                urls = SecuritySupport.getSystemResources(name);
            }
            if (urls != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MailcapCommandMap: getResources");
                }
                for (URL url : urls) {
                    InputStream clis = null;
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("MailcapCommandMap: URL " + url);
                    }
                    try {
                        clis = SecuritySupport.openStream(url);
                        if (clis != null) {
                            v.add(new MailcapFile(clis));
                            anyLoaded = true;
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("MailcapCommandMap: successfully loaded mailcap file from URL: " + url);
                            }
                        } else if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: not loading mailcap file from URL: " + url);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e) {
                            }
                        }
                    } catch (IOException ioex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: can't load " + url, ioex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e2) {
                            }
                        }
                    } catch (SecurityException sex) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("MailcapCommandMap: can't load " + url, sex);
                        }
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e3) {
                            }
                        }
                    } catch (Throwable th) {
                        if (clis != null) {
                            try {
                                clis.close();
                            } catch (IOException e4) {
                            }
                        }
                        throw th;
                    }
                }
            }
        } catch (Exception ex) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: can't load " + name, ex);
            }
        }
        if (!anyLoaded) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: !anyLoaded");
            }
            MailcapFile mf = loadResource("/" + name);
            if (mf != null) {
                v.add(mf);
            }
        }
    }

    private MailcapFile loadFile(String name) {
        try {
            return new MailcapFile(name);
        } catch (IOException e) {
            return null;
        }
    }

    public MailcapCommandMap(String fileName) throws IOException {
        this();
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: load PROG from " + fileName);
        }
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile(fileName);
        }
    }

    public MailcapCommandMap(InputStream is) {
        this();
        LogSupport.log("MailcapCommandMap: load PROG");
        if (this.DB[0] == null) {
            try {
                this.DB[0] = new MailcapFile(is);
            } catch (IOException e) {
            }
        }
    }

    public synchronized CommandInfo[] getPreferredCommands(String mimeType) {
        List cmdList;
        Map cmdMap;
        Map cmdMap2;
        cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (!(this.DB[i] == null || (cmdMap2 = this.DB[i].getMailcapList(mimeType)) == null)) {
                appendPrefCmdsToList(cmdMap2, cmdList);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (!(this.DB[i2] == null || (cmdMap = this.DB[i2].getMailcapFallbackList(mimeType)) == null)) {
                appendPrefCmdsToList(cmdMap, cmdList);
            }
        }
        return (CommandInfo[]) cmdList.toArray(new CommandInfo[cmdList.size()]);
    }

    private void appendPrefCmdsToList(Map cmdHash, List cmdList) {
        for (String verb : cmdHash.keySet()) {
            if (!checkForVerb(cmdList, verb)) {
                cmdList.add(new CommandInfo(verb, (String) ((List) cmdHash.get(verb)).get(0)));
            }
        }
    }

    private boolean checkForVerb(List cmdList, String verb) {
        Iterator ee = cmdList.iterator();
        while (ee.hasNext()) {
            if (((CommandInfo) ee.next()).getCommandName().equals(verb)) {
                return true;
            }
        }
        return false;
    }

    public synchronized CommandInfo[] getAllCommands(String mimeType) {
        List cmdList;
        Map cmdMap;
        Map cmdMap2;
        cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (!(this.DB[i] == null || (cmdMap2 = this.DB[i].getMailcapList(mimeType)) == null)) {
                appendCmdsToList(cmdMap2, cmdList);
            }
        }
        for (int i2 = 0; i2 < this.DB.length; i2++) {
            if (!(this.DB[i2] == null || (cmdMap = this.DB[i2].getMailcapFallbackList(mimeType)) == null)) {
                appendCmdsToList(cmdMap, cmdList);
            }
        }
        return (CommandInfo[]) cmdList.toArray(new CommandInfo[cmdList.size()]);
    }

    private void appendCmdsToList(Map typeHash, List cmdList) {
        for (String verb : typeHash.keySet()) {
            for (String cmd : (List) typeHash.get(verb)) {
                cmdList.add(new CommandInfo(verb, cmd));
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0049, code lost:
        if (r5.DB[r2] != null) goto L_0x004e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x004b, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x004e, code lost:
        r1 = r5.DB[r2].getMailcapFallbackList(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0056, code lost:
        if (r1 == null) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0058, code lost:
        r3 = (java.util.List) r1.get(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x005e, code lost:
        if (r3 == null) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0060, code lost:
        r0 = (java.lang.String) r3.get(0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0067, code lost:
        if (r0 == null) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0069, code lost:
        r4 = new javax.activation.CommandInfo(r7, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000f, code lost:
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0013, code lost:
        if (r2 < r5.DB.length) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized javax.activation.CommandInfo getCommand(java.lang.String r6, java.lang.String r7) {
        /*
            r5 = this;
            monitor-enter(r5)
            if (r6 == 0) goto L_0x0009
            java.util.Locale r4 = java.util.Locale.ENGLISH     // Catch:{ all -> 0x0042 }
            java.lang.String r6 = r6.toLowerCase(r4)     // Catch:{ all -> 0x0042 }
        L_0x0009:
            r2 = 0
        L_0x000a:
            com.sun.activation.registries.MailcapFile[] r4 = r5.DB     // Catch:{ all -> 0x0042 }
            int r4 = r4.length     // Catch:{ all -> 0x0042 }
            if (r2 < r4) goto L_0x0018
            r2 = 0
        L_0x0010:
            com.sun.activation.registries.MailcapFile[] r4 = r5.DB     // Catch:{ all -> 0x0042 }
            int r4 = r4.length     // Catch:{ all -> 0x0042 }
            if (r2 < r4) goto L_0x0045
            r4 = 0
        L_0x0016:
            monitor-exit(r5)
            return r4
        L_0x0018:
            com.sun.activation.registries.MailcapFile[] r4 = r5.DB     // Catch:{ all -> 0x0042 }
            r4 = r4[r2]     // Catch:{ all -> 0x0042 }
            if (r4 != 0) goto L_0x0021
        L_0x001e:
            int r2 = r2 + 1
            goto L_0x000a
        L_0x0021:
            com.sun.activation.registries.MailcapFile[] r4 = r5.DB     // Catch:{ all -> 0x0042 }
            r4 = r4[r2]     // Catch:{ all -> 0x0042 }
            java.util.Map r1 = r4.getMailcapList(r6)     // Catch:{ all -> 0x0042 }
            if (r1 == 0) goto L_0x001e
            java.lang.Object r3 = r1.get(r7)     // Catch:{ all -> 0x0042 }
            java.util.List r3 = (java.util.List) r3     // Catch:{ all -> 0x0042 }
            if (r3 == 0) goto L_0x001e
            r4 = 0
            java.lang.Object r0 = r3.get(r4)     // Catch:{ all -> 0x0042 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x001e
            javax.activation.CommandInfo r4 = new javax.activation.CommandInfo     // Catch:{ all -> 0x0042 }
            r4.<init>(r7, r0)     // Catch:{ all -> 0x0042 }
            goto L_0x0016
        L_0x0042:
            r4 = move-exception
            monitor-exit(r5)
            throw r4
        L_0x0045:
            com.sun.activation.registries.MailcapFile[] r4 = r5.DB     // Catch:{ all -> 0x0042 }
            r4 = r4[r2]     // Catch:{ all -> 0x0042 }
            if (r4 != 0) goto L_0x004e
        L_0x004b:
            int r2 = r2 + 1
            goto L_0x0010
        L_0x004e:
            com.sun.activation.registries.MailcapFile[] r4 = r5.DB     // Catch:{ all -> 0x0042 }
            r4 = r4[r2]     // Catch:{ all -> 0x0042 }
            java.util.Map r1 = r4.getMailcapFallbackList(r6)     // Catch:{ all -> 0x0042 }
            if (r1 == 0) goto L_0x004b
            java.lang.Object r3 = r1.get(r7)     // Catch:{ all -> 0x0042 }
            java.util.List r3 = (java.util.List) r3     // Catch:{ all -> 0x0042 }
            if (r3 == 0) goto L_0x004b
            r4 = 0
            java.lang.Object r0 = r3.get(r4)     // Catch:{ all -> 0x0042 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x004b
            javax.activation.CommandInfo r4 = new javax.activation.CommandInfo     // Catch:{ all -> 0x0042 }
            r4.<init>(r7, r0)     // Catch:{ all -> 0x0042 }
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.activation.MailcapCommandMap.getCommand(java.lang.String, java.lang.String):javax.activation.CommandInfo");
    }

    public synchronized void addMailcap(String mail_cap) {
        LogSupport.log("MailcapCommandMap: add to PROG");
        if (this.DB[0] == null) {
            this.DB[0] = new MailcapFile();
        }
        this.DB[0].appendToMailcap(mail_cap);
    }

    public synchronized DataContentHandler createDataContentHandler(String mimeType) {
        DataContentHandler dch;
        List v;
        List v2;
        if (LogSupport.isLoggable()) {
            LogSupport.log("MailcapCommandMap: createDataContentHandler for " + mimeType);
        }
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        int i = 0;
        while (true) {
            if (i >= this.DB.length) {
                int i2 = 0;
                while (true) {
                    if (i2 >= this.DB.length) {
                        dch = null;
                        break;
                    }
                    if (this.DB[i2] != null) {
                        if (LogSupport.isLoggable()) {
                            LogSupport.log("  search fallback DB #" + i2);
                        }
                        Map cmdMap = this.DB[i2].getMailcapFallbackList(mimeType);
                        if (!(cmdMap == null || (v = (List) cmdMap.get("content-handler")) == null || (dch = getDataContentHandler((String) v.get(0))) == null)) {
                            break;
                        }
                    }
                    i2++;
                }
            } else {
                if (this.DB[i] != null) {
                    if (LogSupport.isLoggable()) {
                        LogSupport.log("  search DB #" + i);
                    }
                    Map cmdMap2 = this.DB[i].getMailcapList(mimeType);
                    if (!(cmdMap2 == null || (v2 = (List) cmdMap2.get("content-handler")) == null || (dch = getDataContentHandler((String) v2.get(0))) == null)) {
                        break;
                    }
                }
                i++;
            }
        }
        return dch;
    }

    private DataContentHandler getDataContentHandler(String name) {
        Class cl;
        if (LogSupport.isLoggable()) {
            LogSupport.log("    got content-handler");
        }
        if (LogSupport.isLoggable()) {
            LogSupport.log("      class " + name);
        }
        try {
            ClassLoader cld = SecuritySupport.getContextClassLoader();
            if (cld == null) {
                cld = getClass().getClassLoader();
            }
            try {
                cl = cld.loadClass(name);
            } catch (Exception e) {
                cl = Class.forName(name);
            }
            if (cl != null) {
                return (DataContentHandler) cl.newInstance();
            }
        } catch (IllegalAccessException e2) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e2);
            }
        } catch (ClassNotFoundException e3) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e3);
            }
        } catch (InstantiationException e4) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("Can't load DCH " + name, e4);
            }
        }
        return null;
    }

    public synchronized String[] getMimeTypes() {
        List mtList;
        String[] ts;
        mtList = new ArrayList();
        for (int i = 0; i < this.DB.length; i++) {
            if (!(this.DB[i] == null || (ts = this.DB[i].getMimeTypes()) == null)) {
                for (int j = 0; j < ts.length; j++) {
                    if (!mtList.contains(ts[j])) {
                        mtList.add(ts[j]);
                    }
                }
            }
        }
        return (String[]) mtList.toArray(new String[mtList.size()]);
    }

    public synchronized String[] getNativeCommands(String mimeType) {
        List cmdList;
        String[] cmds;
        cmdList = new ArrayList();
        if (mimeType != null) {
            mimeType = mimeType.toLowerCase(Locale.ENGLISH);
        }
        for (int i = 0; i < this.DB.length; i++) {
            if (!(this.DB[i] == null || (cmds = this.DB[i].getNativeCommands(mimeType)) == null)) {
                for (int j = 0; j < cmds.length; j++) {
                    if (!cmdList.contains(cmds[j])) {
                        cmdList.add(cmds[j]);
                    }
                }
            }
        }
        return (String[]) cmdList.toArray(new String[cmdList.size()]);
    }
}
