package javax.mail;

import java.util.Vector;
import javax.mail.Flags;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.MailEvent;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.search.SearchTerm;

public abstract class Folder {
    public static final int HOLDS_FOLDERS = 2;
    public static final int HOLDS_MESSAGES = 1;
    public static final int READ_ONLY = 1;
    public static final int READ_WRITE = 2;
    private volatile Vector connectionListeners = null;
    private volatile Vector folderListeners = null;
    private volatile Vector messageChangedListeners = null;
    private volatile Vector messageCountListeners = null;
    protected int mode = -1;
    private EventQueue q;
    private Object qLock = new Object();
    protected Store store;

    public abstract void appendMessages(Message[] messageArr) throws MessagingException;

    public abstract void close(boolean z) throws MessagingException;

    public abstract boolean create(int i) throws MessagingException;

    public abstract boolean delete(boolean z) throws MessagingException;

    public abstract boolean exists() throws MessagingException;

    public abstract Message[] expunge() throws MessagingException;

    public abstract Folder getFolder(String str) throws MessagingException;

    public abstract String getFullName();

    public abstract Message getMessage(int i) throws MessagingException;

    public abstract int getMessageCount() throws MessagingException;

    public abstract String getName();

    public abstract Folder getParent() throws MessagingException;

    public abstract Flags getPermanentFlags();

    public abstract char getSeparator() throws MessagingException;

    public abstract int getType() throws MessagingException;

    public abstract boolean hasNewMessages() throws MessagingException;

    public abstract boolean isOpen();

    public abstract Folder[] list(String str) throws MessagingException;

    public abstract void open(int i) throws MessagingException;

    public abstract boolean renameTo(Folder folder) throws MessagingException;

    protected Folder(Store store2) {
        this.store = store2;
    }

    public URLName getURLName() throws MessagingException {
        URLName storeURL = getStore().getURLName();
        String fullname = getFullName();
        StringBuffer encodedName = new StringBuffer();
        char separator = getSeparator();
        if (fullname != null) {
            encodedName.append(fullname);
        }
        return new URLName(storeURL.getProtocol(), storeURL.getHost(), storeURL.getPort(), encodedName.toString(), storeURL.getUsername(), (String) null);
    }

    public Store getStore() {
        return this.store;
    }

    public Folder[] listSubscribed(String pattern) throws MessagingException {
        return list(pattern);
    }

    public Folder[] list() throws MessagingException {
        return list("%");
    }

    public Folder[] listSubscribed() throws MessagingException {
        return listSubscribed("%");
    }

    public boolean isSubscribed() {
        return true;
    }

    public void setSubscribed(boolean subscribe) throws MessagingException {
        throw new MethodNotSupportedException();
    }

    public int getMode() {
        if (isOpen()) {
            return this.mode;
        }
        throw new IllegalStateException("Folder not open");
    }

    public synchronized int getNewMessageCount() throws MessagingException {
        int newmsgs;
        if (!isOpen()) {
            newmsgs = -1;
        } else {
            newmsgs = 0;
            int total = getMessageCount();
            for (int i = 1; i <= total; i++) {
                try {
                    if (getMessage(i).isSet(Flags.Flag.RECENT)) {
                        newmsgs++;
                    }
                } catch (MessageRemovedException e) {
                }
            }
        }
        return newmsgs;
    }

    public synchronized int getUnreadMessageCount() throws MessagingException {
        int unread;
        if (!isOpen()) {
            unread = -1;
        } else {
            unread = 0;
            int total = getMessageCount();
            for (int i = 1; i <= total; i++) {
                try {
                    if (!getMessage(i).isSet(Flags.Flag.SEEN)) {
                        unread++;
                    }
                } catch (MessageRemovedException e) {
                }
            }
        }
        return unread;
    }

    public synchronized int getDeletedMessageCount() throws MessagingException {
        int deleted;
        if (!isOpen()) {
            deleted = -1;
        } else {
            deleted = 0;
            int total = getMessageCount();
            for (int i = 1; i <= total; i++) {
                try {
                    if (getMessage(i).isSet(Flags.Flag.DELETED)) {
                        deleted++;
                    }
                } catch (MessageRemovedException e) {
                }
            }
        }
        return deleted;
    }

    public synchronized Message[] getMessages(int start, int end) throws MessagingException {
        Message[] msgs;
        msgs = new Message[((end - start) + 1)];
        for (int i = start; i <= end; i++) {
            msgs[i - start] = getMessage(i);
        }
        return msgs;
    }

    public synchronized Message[] getMessages(int[] msgnums) throws MessagingException {
        Message[] msgs;
        int len = msgnums.length;
        msgs = new Message[len];
        for (int i = 0; i < len; i++) {
            msgs[i] = getMessage(msgnums[i]);
        }
        return msgs;
    }

    public synchronized Message[] getMessages() throws MessagingException {
        Message[] msgs;
        if (!isOpen()) {
            throw new IllegalStateException("Folder not open");
        }
        int total = getMessageCount();
        msgs = new Message[total];
        for (int i = 1; i <= total; i++) {
            msgs[i - 1] = getMessage(i);
        }
        return msgs;
    }

    public void fetch(Message[] msgs, FetchProfile fp) throws MessagingException {
    }

    public synchronized void setFlags(Message[] msgs, Flags flag, boolean value) throws MessagingException {
        for (int i = 0; i < msgs.length; i++) {
            try {
                msgs[i].setFlags(flag, value);
            } catch (MessageRemovedException e) {
            }
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void setFlags(int r4, int r5, javax.mail.Flags r6, boolean r7) throws javax.mail.MessagingException {
        /*
            r3 = this;
            monitor-enter(r3)
            r0 = r4
        L_0x0002:
            if (r0 <= r5) goto L_0x0006
            monitor-exit(r3)
            return
        L_0x0006:
            javax.mail.Message r1 = r3.getMessage(r0)     // Catch:{ MessageRemovedException -> 0x0013, all -> 0x0010 }
            r1.setFlags(r6, r7)     // Catch:{ MessageRemovedException -> 0x0013, all -> 0x0010 }
        L_0x000d:
            int r0 = r0 + 1
            goto L_0x0002
        L_0x0010:
            r2 = move-exception
            monitor-exit(r3)
            throw r2
        L_0x0013:
            r2 = move-exception
            goto L_0x000d
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Folder.setFlags(int, int, javax.mail.Flags, boolean):void");
    }

    public synchronized void setFlags(int[] msgnums, Flags flag, boolean value) throws MessagingException {
        for (int i = 0; i < msgnums.length; i++) {
            try {
                getMessage(msgnums[i]).setFlags(flag, value);
            } catch (MessageRemovedException e) {
            }
        }
    }

    public void copyMessages(Message[] msgs, Folder folder) throws MessagingException {
        if (!folder.exists()) {
            throw new FolderNotFoundException(String.valueOf(folder.getFullName()) + " does not exist", folder);
        }
        folder.appendMessages(msgs);
    }

    public Message[] search(SearchTerm term) throws MessagingException {
        return search(term, getMessages());
    }

    public Message[] search(SearchTerm term, Message[] msgs) throws MessagingException {
        Vector matchedMsgs = new Vector();
        for (int i = 0; i < msgs.length; i++) {
            try {
                if (msgs[i].match(term)) {
                    matchedMsgs.addElement(msgs[i]);
                }
            } catch (MessageRemovedException e) {
            }
        }
        Message[] m = new Message[matchedMsgs.size()];
        matchedMsgs.copyInto(m);
        return m;
    }

    public synchronized void addConnectionListener(ConnectionListener l) {
        if (this.connectionListeners == null) {
            this.connectionListeners = new Vector();
        }
        this.connectionListeners.addElement(l);
    }

    public synchronized void removeConnectionListener(ConnectionListener l) {
        if (this.connectionListeners != null) {
            this.connectionListeners.removeElement(l);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyConnectionListeners(int type) {
        if (this.connectionListeners != null) {
            queueEvent(new ConnectionEvent(this, type), this.connectionListeners);
        }
        if (type == 3) {
            terminateQueue();
        }
    }

    public synchronized void addFolderListener(FolderListener l) {
        if (this.folderListeners == null) {
            this.folderListeners = new Vector();
        }
        this.folderListeners.addElement(l);
    }

    public synchronized void removeFolderListener(FolderListener l) {
        if (this.folderListeners != null) {
            this.folderListeners.removeElement(l);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyFolderListeners(int type) {
        if (this.folderListeners != null) {
            queueEvent(new FolderEvent(this, this, type), this.folderListeners);
        }
        this.store.notifyFolderListeners(type, this);
    }

    /* access modifiers changed from: protected */
    public void notifyFolderRenamedListeners(Folder folder) {
        if (this.folderListeners != null) {
            queueEvent(new FolderEvent(this, this, folder, 3), this.folderListeners);
        }
        this.store.notifyFolderRenamedListeners(this, folder);
    }

    public synchronized void addMessageCountListener(MessageCountListener l) {
        if (this.messageCountListeners == null) {
            this.messageCountListeners = new Vector();
        }
        this.messageCountListeners.addElement(l);
    }

    public synchronized void removeMessageCountListener(MessageCountListener l) {
        if (this.messageCountListeners != null) {
            this.messageCountListeners.removeElement(l);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyMessageAddedListeners(Message[] msgs) {
        if (this.messageCountListeners != null) {
            queueEvent(new MessageCountEvent(this, 1, false, msgs), this.messageCountListeners);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyMessageRemovedListeners(boolean removed, Message[] msgs) {
        if (this.messageCountListeners != null) {
            queueEvent(new MessageCountEvent(this, 2, removed, msgs), this.messageCountListeners);
        }
    }

    public synchronized void addMessageChangedListener(MessageChangedListener l) {
        if (this.messageChangedListeners == null) {
            this.messageChangedListeners = new Vector();
        }
        this.messageChangedListeners.addElement(l);
    }

    public synchronized void removeMessageChangedListener(MessageChangedListener l) {
        if (this.messageChangedListeners != null) {
            this.messageChangedListeners.removeElement(l);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyMessageChangedListeners(int type, Message msg) {
        if (this.messageChangedListeners != null) {
            queueEvent(new MessageChangedEvent(this, type, msg), this.messageChangedListeners);
        }
    }

    private void queueEvent(MailEvent event, Vector vector) {
        synchronized (this.qLock) {
            if (this.q == null) {
                this.q = new EventQueue();
            }
        }
        this.q.enqueue(event, (Vector) vector.clone());
    }

    static class TerminatorEvent extends MailEvent {
        private static final long serialVersionUID = 3765761925441296565L;

        TerminatorEvent() {
            super(new Object());
        }

        public void dispatch(Object listener) {
            Thread.currentThread().interrupt();
        }
    }

    private void terminateQueue() {
        synchronized (this.qLock) {
            if (this.q != null) {
                Vector dummyListeners = new Vector();
                dummyListeners.setSize(1);
                this.q.enqueue(new TerminatorEvent(), dummyListeners);
                this.q = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        terminateQueue();
    }

    public String toString() {
        String s = getFullName();
        return s != null ? s : super.toString();
    }
}
