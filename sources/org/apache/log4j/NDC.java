package org.apache.log4j;

import java.util.Hashtable;
import java.util.Stack;

public class NDC {
    static final int REAP_THRESHOLD = 5;
    static Hashtable ht = new Hashtable();
    static int pushCounter = 0;

    private NDC() {
    }

    private static Stack getCurrentStack() {
        if (ht != null) {
            return (Stack) ht.get(Thread.currentThread());
        }
        return null;
    }

    public static void clear() {
        Stack stack = getCurrentStack();
        if (stack != null) {
            stack.setSize(0);
        }
    }

    public static Stack cloneStack() {
        Stack stack = getCurrentStack();
        if (stack == null) {
            return null;
        }
        return (Stack) stack.clone();
    }

    public static void inherit(Stack stack) {
        if (stack != null) {
            ht.put(Thread.currentThread(), stack);
        }
    }

    public static String get() {
        Stack s = getCurrentStack();
        if (s == null || s.isEmpty()) {
            return null;
        }
        return ((DiagnosticContext) s.peek()).fullMessage;
    }

    public static int getDepth() {
        Stack stack = getCurrentStack();
        if (stack == null) {
            return 0;
        }
        return stack.size();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0043, code lost:
        r3 = r5.size();
        r1 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0048, code lost:
        if (r1 >= r3) goto L_0x0004;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004a, code lost:
        r4 = (java.lang.Thread) r5.elementAt(r1);
        org.apache.log4j.helpers.LogLog.debug(new java.lang.StringBuffer().append("Lazy NDC removal for thread [").append(r4.getName()).append("] (").append(ht.size()).append(").").toString());
        ht.remove(r4);
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void lazyRemove() {
        /*
            java.util.Hashtable r6 = ht
            if (r6 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            java.util.Hashtable r7 = ht
            monitor-enter(r7)
            int r6 = pushCounter     // Catch:{ all -> 0x0013 }
            int r6 = r6 + 1
            pushCounter = r6     // Catch:{ all -> 0x0013 }
            r8 = 5
            if (r6 > r8) goto L_0x0016
            monitor-exit(r7)     // Catch:{ all -> 0x0013 }
            goto L_0x0004
        L_0x0013:
            r6 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x0013 }
            throw r6
        L_0x0016:
            r6 = 0
            pushCounter = r6     // Catch:{ all -> 0x0013 }
            r2 = 0
            java.util.Vector r5 = new java.util.Vector     // Catch:{ all -> 0x0013 }
            r5.<init>()     // Catch:{ all -> 0x0013 }
            java.util.Hashtable r6 = ht     // Catch:{ all -> 0x0013 }
            java.util.Enumeration r0 = r6.keys()     // Catch:{ all -> 0x0013 }
        L_0x0025:
            boolean r6 = r0.hasMoreElements()     // Catch:{ all -> 0x0013 }
            if (r6 == 0) goto L_0x0042
            r6 = 4
            if (r2 > r6) goto L_0x0042
            java.lang.Object r4 = r0.nextElement()     // Catch:{ all -> 0x0013 }
            java.lang.Thread r4 = (java.lang.Thread) r4     // Catch:{ all -> 0x0013 }
            boolean r6 = r4.isAlive()     // Catch:{ all -> 0x0013 }
            if (r6 == 0) goto L_0x003d
            int r2 = r2 + 1
            goto L_0x0025
        L_0x003d:
            r2 = 0
            r5.addElement(r4)     // Catch:{ all -> 0x0013 }
            goto L_0x0025
        L_0x0042:
            monitor-exit(r7)     // Catch:{ all -> 0x0013 }
            int r3 = r5.size()
            r1 = 0
        L_0x0048:
            if (r1 >= r3) goto L_0x0004
            java.lang.Object r4 = r5.elementAt(r1)
            java.lang.Thread r4 = (java.lang.Thread) r4
            java.lang.StringBuffer r6 = new java.lang.StringBuffer
            r6.<init>()
            java.lang.String r7 = "Lazy NDC removal for thread ["
            java.lang.StringBuffer r6 = r6.append(r7)
            java.lang.String r7 = r4.getName()
            java.lang.StringBuffer r6 = r6.append(r7)
            java.lang.String r7 = "] ("
            java.lang.StringBuffer r6 = r6.append(r7)
            java.util.Hashtable r7 = ht
            int r7 = r7.size()
            java.lang.StringBuffer r6 = r6.append(r7)
            java.lang.String r7 = ")."
            java.lang.StringBuffer r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            org.apache.log4j.helpers.LogLog.debug(r6)
            java.util.Hashtable r6 = ht
            r6.remove(r4)
            int r1 = r1 + 1
            goto L_0x0048
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.NDC.lazyRemove():void");
    }

    public static String pop() {
        Stack stack = getCurrentStack();
        if (stack == null || stack.isEmpty()) {
            return "";
        }
        return ((DiagnosticContext) stack.pop()).message;
    }

    public static String peek() {
        Stack stack = getCurrentStack();
        if (stack == null || stack.isEmpty()) {
            return "";
        }
        return ((DiagnosticContext) stack.peek()).message;
    }

    public static void push(String message) {
        Stack stack = getCurrentStack();
        if (stack == null) {
            DiagnosticContext dc = new DiagnosticContext(message, (DiagnosticContext) null);
            Stack stack2 = new Stack();
            ht.put(Thread.currentThread(), stack2);
            stack2.push(dc);
        } else if (stack.isEmpty()) {
            stack.push(new DiagnosticContext(message, (DiagnosticContext) null));
        } else {
            stack.push(new DiagnosticContext(message, (DiagnosticContext) stack.peek()));
        }
    }

    public static void remove() {
        if (ht != null) {
            ht.remove(Thread.currentThread());
            lazyRemove();
        }
    }

    public static void setMaxDepth(int maxDepth) {
        Stack stack = getCurrentStack();
        if (stack != null && maxDepth < stack.size()) {
            stack.setSize(maxDepth);
        }
    }

    private static class DiagnosticContext {
        String fullMessage;
        String message;

        DiagnosticContext(String message2, DiagnosticContext parent) {
            this.message = message2;
            if (parent != null) {
                this.fullMessage = new StringBuffer().append(parent.fullMessage).append(' ').append(message2).toString();
            } else {
                this.fullMessage = message2;
            }
        }
    }
}
