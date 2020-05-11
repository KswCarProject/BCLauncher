package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessageAware;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.MultipartDataSource;

public class MimeMultipart extends Multipart {
    private static boolean bmparse;
    private static boolean ignoreMissingBoundaryParameter;
    private static boolean ignoreMissingEndBoundary;
    private boolean complete;
    protected DataSource ds;
    protected boolean parsed;
    private String preamble;

    static {
        boolean z;
        boolean z2 = false;
        ignoreMissingEndBoundary = true;
        ignoreMissingBoundaryParameter = true;
        bmparse = true;
        try {
            String s = System.getProperty("mail.mime.multipart.ignoremissingendboundary");
            ignoreMissingEndBoundary = s == null || !s.equalsIgnoreCase("false");
            String s2 = System.getProperty("mail.mime.multipart.ignoremissingboundaryparameter");
            if (s2 == null || !s2.equalsIgnoreCase("false")) {
                z = true;
            } else {
                z = false;
            }
            ignoreMissingBoundaryParameter = z;
            String s3 = System.getProperty("mail.mime.multipart.bmparse");
            if (s3 == null || !s3.equalsIgnoreCase("false")) {
                z2 = true;
            }
            bmparse = z2;
        } catch (SecurityException e) {
        }
    }

    public MimeMultipart() {
        this("mixed");
    }

    public MimeMultipart(String subtype) {
        this.ds = null;
        this.parsed = true;
        this.complete = true;
        this.preamble = null;
        String boundary = UniqueValue.getUniqueBoundaryValue();
        ContentType cType = new ContentType("multipart", subtype, (ParameterList) null);
        cType.setParameter("boundary", boundary);
        this.contentType = cType.toString();
    }

    public MimeMultipart(DataSource ds2) throws MessagingException {
        this.ds = null;
        this.parsed = true;
        this.complete = true;
        this.preamble = null;
        if (ds2 instanceof MessageAware) {
            setParent(((MessageAware) ds2).getMessageContext().getPart());
        }
        if (ds2 instanceof MultipartDataSource) {
            setMultipartDataSource((MultipartDataSource) ds2);
            return;
        }
        this.parsed = false;
        this.ds = ds2;
        this.contentType = ds2.getContentType();
    }

    public synchronized void setSubType(String subtype) throws MessagingException {
        ContentType cType = new ContentType(this.contentType);
        cType.setSubType(subtype);
        this.contentType = cType.toString();
    }

    public synchronized int getCount() throws MessagingException {
        parse();
        return super.getCount();
    }

    public synchronized BodyPart getBodyPart(int index) throws MessagingException {
        parse();
        return super.getBodyPart(index);
    }

    public synchronized BodyPart getBodyPart(String CID) throws MessagingException {
        MimeBodyPart part;
        parse();
        int count = getCount();
        int i = 0;
        while (true) {
            if (i < count) {
                part = (MimeBodyPart) getBodyPart(i);
                String s = part.getContentID();
                if (s != null && s.equals(CID)) {
                    break;
                }
                i++;
            } else {
                part = null;
                break;
            }
        }
        return part;
    }

    public boolean removeBodyPart(BodyPart part) throws MessagingException {
        parse();
        return super.removeBodyPart(part);
    }

    public void removeBodyPart(int index) throws MessagingException {
        parse();
        super.removeBodyPart(index);
    }

    public synchronized void addBodyPart(BodyPart part) throws MessagingException {
        parse();
        super.addBodyPart(part);
    }

    public synchronized void addBodyPart(BodyPart part, int index) throws MessagingException {
        parse();
        super.addBodyPart(part, index);
    }

    public synchronized boolean isComplete() throws MessagingException {
        parse();
        return this.complete;
    }

    public synchronized String getPreamble() throws MessagingException {
        parse();
        return this.preamble;
    }

    public synchronized void setPreamble(String preamble2) throws MessagingException {
        this.preamble = preamble2;
    }

    /* access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        for (int i = 0; i < this.parts.size(); i++) {
            ((MimeBodyPart) this.parts.elementAt(i)).updateHeaders();
        }
    }

    public synchronized void writeTo(OutputStream os) throws IOException, MessagingException {
        parse();
        String boundary = "--" + new ContentType(this.contentType).getParameter("boundary");
        LineOutputStream los = new LineOutputStream(os);
        if (this.preamble != null) {
            byte[] pb = ASCIIUtility.getBytes(this.preamble);
            los.write(pb);
            if (!(pb.length <= 0 || pb[pb.length - 1] == 13 || pb[pb.length - 1] == 10)) {
                los.writeln();
            }
        }
        for (int i = 0; i < this.parts.size(); i++) {
            los.writeln(boundary);
            ((MimeBodyPart) this.parts.elementAt(i)).writeTo(os);
            los.writeln();
        }
        los.writeln(String.valueOf(boundary) + "--");
    }

    /* JADX WARNING: type inference failed for: r33v39, types: [int] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x0193 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void parse() throws javax.mail.MessagingException {
        /*
            r40 = this;
            monitor-enter(r40)
            r0 = r40
            boolean r0 = r0.parsed     // Catch:{ all -> 0x0013 }
            r33 = r0
            if (r33 == 0) goto L_0x000b
        L_0x0009:
            monitor-exit(r40)
            return
        L_0x000b:
            boolean r33 = bmparse     // Catch:{ all -> 0x0013 }
            if (r33 == 0) goto L_0x0016
            r40.parsebm()     // Catch:{ all -> 0x0013 }
            goto L_0x0009
        L_0x0013:
            r33 = move-exception
            monitor-exit(r40)
            throw r33
        L_0x0016:
            r24 = 0
            r32 = 0
            r34 = 0
            r18 = 0
            r0 = r40
            javax.activation.DataSource r0 = r0.ds     // Catch:{ Exception -> 0x00bb }
            r33 = r0
            java.io.InputStream r24 = r33.getInputStream()     // Catch:{ Exception -> 0x00bb }
            r0 = r24
            boolean r0 = r0 instanceof java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x00bb }
            r33 = r0
            if (r33 != 0) goto L_0x004b
            r0 = r24
            boolean r0 = r0 instanceof java.io.BufferedInputStream     // Catch:{ Exception -> 0x00bb }
            r33 = r0
            if (r33 != 0) goto L_0x004b
            r0 = r24
            boolean r0 = r0 instanceof javax.mail.internet.SharedInputStream     // Catch:{ Exception -> 0x00bb }
            r33 = r0
            if (r33 != 0) goto L_0x004b
            java.io.BufferedInputStream r25 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x00bb }
            r0 = r25
            r1 = r24
            r0.<init>(r1)     // Catch:{ Exception -> 0x00bb }
            r24 = r25
        L_0x004b:
            r0 = r24
            boolean r0 = r0 instanceof javax.mail.internet.SharedInputStream     // Catch:{ all -> 0x0013 }
            r33 = r0
            if (r33 == 0) goto L_0x0059
            r0 = r24
            javax.mail.internet.SharedInputStream r0 = (javax.mail.internet.SharedInputStream) r0     // Catch:{ all -> 0x0013 }
            r32 = r0
        L_0x0059:
            javax.mail.internet.ContentType r15 = new javax.mail.internet.ContentType     // Catch:{ all -> 0x0013 }
            r0 = r40
            java.lang.String r0 = r0.contentType     // Catch:{ all -> 0x0013 }
            r33 = r0
            r0 = r33
            r15.<init>(r0)     // Catch:{ all -> 0x0013 }
            r11 = 0
            java.lang.String r33 = "boundary"
            r0 = r33
            java.lang.String r12 = r15.getParameter(r0)     // Catch:{ all -> 0x0013 }
            if (r12 == 0) goto L_0x00ca
            java.lang.StringBuilder r33 = new java.lang.StringBuilder     // Catch:{ all -> 0x0013 }
            java.lang.String r36 = "--"
            r0 = r33
            r1 = r36
            r0.<init>(r1)     // Catch:{ all -> 0x0013 }
            r0 = r33
            java.lang.StringBuilder r33 = r0.append(r12)     // Catch:{ all -> 0x0013 }
            java.lang.String r11 = r33.toString()     // Catch:{ all -> 0x0013 }
        L_0x0086:
            com.sun.mail.util.LineInputStream r27 = new com.sun.mail.util.LineInputStream     // Catch:{ IOException -> 0x00a7 }
            r0 = r27
            r1 = r24
            r0.<init>(r1)     // Catch:{ IOException -> 0x00a7 }
            r31 = 0
            r29 = 0
        L_0x0093:
            java.lang.String r28 = r27.readLine()     // Catch:{ IOException -> 0x00a7 }
            if (r28 != 0) goto L_0x00da
        L_0x0099:
            if (r28 != 0) goto L_0x015b
            javax.mail.MessagingException r33 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x00a7 }
            java.lang.String r36 = "Missing start boundary"
            r0 = r33
            r1 = r36
            r0.<init>(r1)     // Catch:{ IOException -> 0x00a7 }
            throw r33     // Catch:{ IOException -> 0x00a7 }
        L_0x00a7:
            r26 = move-exception
            javax.mail.MessagingException r33 = new javax.mail.MessagingException     // Catch:{ all -> 0x00b6 }
            java.lang.String r36 = "IO Error"
            r0 = r33
            r1 = r36
            r2 = r26
            r0.<init>(r1, r2)     // Catch:{ all -> 0x00b6 }
            throw r33     // Catch:{ all -> 0x00b6 }
        L_0x00b6:
            r33 = move-exception
            r24.close()     // Catch:{ IOException -> 0x0312 }
        L_0x00ba:
            throw r33     // Catch:{ all -> 0x0013 }
        L_0x00bb:
            r21 = move-exception
            javax.mail.MessagingException r33 = new javax.mail.MessagingException     // Catch:{ all -> 0x0013 }
            java.lang.String r36 = "No inputstream from datasource"
            r0 = r33
            r1 = r36
            r2 = r21
            r0.<init>(r1, r2)     // Catch:{ all -> 0x0013 }
            throw r33     // Catch:{ all -> 0x0013 }
        L_0x00ca:
            boolean r33 = ignoreMissingBoundaryParameter     // Catch:{ all -> 0x0013 }
            if (r33 != 0) goto L_0x0086
            javax.mail.MessagingException r33 = new javax.mail.MessagingException     // Catch:{ all -> 0x0013 }
            java.lang.String r36 = "Missing boundary parameter"
            r0 = r33
            r1 = r36
            r0.<init>(r1)     // Catch:{ all -> 0x0013 }
            throw r33     // Catch:{ all -> 0x0013 }
        L_0x00da:
            int r33 = r28.length()     // Catch:{ IOException -> 0x00a7 }
            int r23 = r33 + -1
        L_0x00e0:
            if (r23 >= 0) goto L_0x0130
        L_0x00e2:
            r33 = 0
            int r36 = r23 + 1
            r0 = r28
            r1 = r33
            r2 = r36
            java.lang.String r28 = r0.substring(r1, r2)     // Catch:{ IOException -> 0x00a7 }
            if (r11 == 0) goto L_0x0147
            r0 = r28
            boolean r33 = r0.equals(r11)     // Catch:{ IOException -> 0x00a7 }
            if (r33 != 0) goto L_0x0099
        L_0x00fa:
            int r33 = r28.length()     // Catch:{ IOException -> 0x00a7 }
            if (r33 <= 0) goto L_0x0093
            if (r29 != 0) goto L_0x010e
            java.lang.String r33 = "line.separator"
            java.lang.String r36 = "\n"
            r0 = r33
            r1 = r36
            java.lang.String r29 = java.lang.System.getProperty(r0, r1)     // Catch:{ SecurityException -> 0x0157 }
        L_0x010e:
            if (r31 != 0) goto L_0x011f
            java.lang.StringBuffer r31 = new java.lang.StringBuffer     // Catch:{ IOException -> 0x00a7 }
            int r33 = r28.length()     // Catch:{ IOException -> 0x00a7 }
            int r33 = r33 + 2
            r0 = r31
            r1 = r33
            r0.<init>(r1)     // Catch:{ IOException -> 0x00a7 }
        L_0x011f:
            r0 = r31
            r1 = r28
            java.lang.StringBuffer r33 = r0.append(r1)     // Catch:{ IOException -> 0x00a7 }
            r0 = r33
            r1 = r29
            r0.append(r1)     // Catch:{ IOException -> 0x00a7 }
            goto L_0x0093
        L_0x0130:
            r0 = r28
            r1 = r23
            char r14 = r0.charAt(r1)     // Catch:{ IOException -> 0x00a7 }
            r33 = 32
            r0 = r33
            if (r14 == r0) goto L_0x0144
            r33 = 9
            r0 = r33
            if (r14 != r0) goto L_0x00e2
        L_0x0144:
            int r23 = r23 + -1
            goto L_0x00e0
        L_0x0147:
            java.lang.String r33 = "--"
            r0 = r28
            r1 = r33
            boolean r33 = r0.startsWith(r1)     // Catch:{ IOException -> 0x00a7 }
            if (r33 == 0) goto L_0x00fa
            r11 = r28
            goto L_0x0099
        L_0x0157:
            r21 = move-exception
            java.lang.String r29 = "\n"
            goto L_0x010e
        L_0x015b:
            if (r31 == 0) goto L_0x0167
            java.lang.String r33 = r31.toString()     // Catch:{ IOException -> 0x00a7 }
            r0 = r33
            r1 = r40
            r1.preamble = r0     // Catch:{ IOException -> 0x00a7 }
        L_0x0167:
            byte[] r9 = com.sun.mail.util.ASCIIUtility.getBytes((java.lang.String) r11)     // Catch:{ IOException -> 0x00a7 }
            int r8 = r9.length     // Catch:{ IOException -> 0x00a7 }
            r16 = 0
        L_0x016e:
            if (r16 == 0) goto L_0x017d
        L_0x0170:
            r24.close()     // Catch:{ IOException -> 0x0315 }
        L_0x0173:
            r33 = 1
            r0 = r33
            r1 = r40
            r1.parsed = r0     // Catch:{ all -> 0x0013 }
            goto L_0x0009
        L_0x017d:
            r22 = 0
            if (r32 == 0) goto L_0x01ac
            long r34 = r32.getPosition()     // Catch:{ IOException -> 0x00a7 }
        L_0x0185:
            java.lang.String r28 = r27.readLine()     // Catch:{ IOException -> 0x00a7 }
            if (r28 == 0) goto L_0x0191
            int r33 = r28.length()     // Catch:{ IOException -> 0x00a7 }
            if (r33 > 0) goto L_0x0185
        L_0x0191:
            if (r28 != 0) goto L_0x01b4
            boolean r33 = ignoreMissingEndBoundary     // Catch:{ IOException -> 0x00a7 }
            if (r33 != 0) goto L_0x01a3
            javax.mail.MessagingException r33 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x00a7 }
            java.lang.String r36 = "missing multipart end boundary"
            r0 = r33
            r1 = r36
            r0.<init>(r1)     // Catch:{ IOException -> 0x00a7 }
            throw r33     // Catch:{ IOException -> 0x00a7 }
        L_0x01a3:
            r33 = 0
            r0 = r33
            r1 = r40
            r1.complete = r0     // Catch:{ IOException -> 0x00a7 }
            goto L_0x0170
        L_0x01ac:
            r0 = r40
            r1 = r24
            javax.mail.internet.InternetHeaders r22 = r0.createInternetHeaders(r1)     // Catch:{ IOException -> 0x00a7 }
        L_0x01b4:
            boolean r33 = r24.markSupported()     // Catch:{ IOException -> 0x00a7 }
            if (r33 != 0) goto L_0x01c6
            javax.mail.MessagingException r33 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x00a7 }
            java.lang.String r36 = "Stream doesn't support mark"
            r0 = r33
            r1 = r36
            r0.<init>(r1)     // Catch:{ IOException -> 0x00a7 }
            throw r33     // Catch:{ IOException -> 0x00a7 }
        L_0x01c6:
            r13 = 0
            if (r32 != 0) goto L_0x022b
            java.io.ByteArrayOutputStream r13 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x00a7 }
            r13.<init>()     // Catch:{ IOException -> 0x00a7 }
        L_0x01ce:
            r10 = 1
            r17 = -1
            r20 = -1
        L_0x01d3:
            if (r10 == 0) goto L_0x029d
            int r33 = r8 + 4
            r0 = r33
            int r0 = r0 + 1000
            r33 = r0
            r0 = r24
            r1 = r33
            r0.mark(r1)     // Catch:{ IOException -> 0x00a7 }
            r23 = 0
        L_0x01e6:
            r0 = r23
            if (r0 < r8) goto L_0x0230
        L_0x01ea:
            r0 = r23
            if (r0 != r8) goto L_0x027a
            int r7 = r24.read()     // Catch:{ IOException -> 0x00a7 }
            r33 = 45
            r0 = r33
            if (r7 != r0) goto L_0x0249
            int r33 = r24.read()     // Catch:{ IOException -> 0x00a7 }
            r36 = 45
            r0 = r33
            r1 = r36
            if (r0 != r1) goto L_0x0249
            r33 = 1
            r0 = r33
            r1 = r40
            r1.complete = r0     // Catch:{ IOException -> 0x00a7 }
            r16 = 1
        L_0x020e:
            if (r32 == 0) goto L_0x0302
            r0 = r32
            r1 = r34
            r3 = r18
            java.io.InputStream r33 = r0.newStream(r1, r3)     // Catch:{ IOException -> 0x00a7 }
            r0 = r40
            r1 = r33
            javax.mail.internet.MimeBodyPart r30 = r0.createMimeBodyPart(r1)     // Catch:{ IOException -> 0x00a7 }
        L_0x0222:
            r0 = r40
            r1 = r30
            super.addBodyPart(r1)     // Catch:{ IOException -> 0x00a7 }
            goto L_0x016e
        L_0x022b:
            long r18 = r32.getPosition()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x01ce
        L_0x0230:
            int r33 = r24.read()     // Catch:{ IOException -> 0x00a7 }
            byte r36 = r9[r23]     // Catch:{ IOException -> 0x00a7 }
            r0 = r36
            r0 = r0 & 255(0xff, float:3.57E-43)
            r36 = r0
            r0 = r33
            r1 = r36
            if (r0 != r1) goto L_0x01ea
            int r23 = r23 + 1
            goto L_0x01e6
        L_0x0245:
            int r7 = r24.read()     // Catch:{ IOException -> 0x00a7 }
        L_0x0249:
            r33 = 32
            r0 = r33
            if (r7 == r0) goto L_0x0245
            r33 = 9
            r0 = r33
            if (r7 == r0) goto L_0x0245
            r33 = 10
            r0 = r33
            if (r7 == r0) goto L_0x020e
            r33 = 13
            r0 = r33
            if (r7 != r0) goto L_0x027a
            r33 = 1
            r0 = r24
            r1 = r33
            r0.mark(r1)     // Catch:{ IOException -> 0x00a7 }
            int r33 = r24.read()     // Catch:{ IOException -> 0x00a7 }
            r36 = 10
            r0 = r33
            r1 = r36
            if (r0 == r1) goto L_0x020e
            r24.reset()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x020e
        L_0x027a:
            r24.reset()     // Catch:{ IOException -> 0x00a7 }
            if (r13 == 0) goto L_0x029d
            r33 = -1
            r0 = r17
            r1 = r33
            if (r0 == r1) goto L_0x029d
            r0 = r17
            r13.write(r0)     // Catch:{ IOException -> 0x00a7 }
            r33 = -1
            r0 = r20
            r1 = r33
            if (r0 == r1) goto L_0x0299
            r0 = r20
            r13.write(r0)     // Catch:{ IOException -> 0x00a7 }
        L_0x0299:
            r20 = -1
            r17 = r20
        L_0x029d:
            int r6 = r24.read()     // Catch:{ IOException -> 0x00a7 }
            if (r6 >= 0) goto L_0x02bf
            boolean r33 = ignoreMissingEndBoundary     // Catch:{ IOException -> 0x00a7 }
            if (r33 != 0) goto L_0x02b3
            javax.mail.MessagingException r33 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x00a7 }
            java.lang.String r36 = "missing multipart end boundary"
            r0 = r33
            r1 = r36
            r0.<init>(r1)     // Catch:{ IOException -> 0x00a7 }
            throw r33     // Catch:{ IOException -> 0x00a7 }
        L_0x02b3:
            r33 = 0
            r0 = r33
            r1 = r40
            r1.complete = r0     // Catch:{ IOException -> 0x00a7 }
            r16 = 1
            goto L_0x020e
        L_0x02bf:
            r33 = 13
            r0 = r33
            if (r6 == r0) goto L_0x02cb
            r33 = 10
            r0 = r33
            if (r6 != r0) goto L_0x02fa
        L_0x02cb:
            r10 = 1
            if (r32 == 0) goto L_0x02d6
            long r36 = r32.getPosition()     // Catch:{ IOException -> 0x00a7 }
            r38 = 1
            long r18 = r36 - r38
        L_0x02d6:
            r17 = r6
            r33 = 13
            r0 = r33
            if (r6 != r0) goto L_0x01d3
            r33 = 1
            r0 = r24
            r1 = r33
            r0.mark(r1)     // Catch:{ IOException -> 0x00a7 }
            int r6 = r24.read()     // Catch:{ IOException -> 0x00a7 }
            r33 = 10
            r0 = r33
            if (r6 != r0) goto L_0x02f5
            r20 = r6
            goto L_0x01d3
        L_0x02f5:
            r24.reset()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x01d3
        L_0x02fa:
            r10 = 0
            if (r13 == 0) goto L_0x01d3
            r13.write(r6)     // Catch:{ IOException -> 0x00a7 }
            goto L_0x01d3
        L_0x0302:
            byte[] r33 = r13.toByteArray()     // Catch:{ IOException -> 0x00a7 }
            r0 = r40
            r1 = r22
            r2 = r33
            javax.mail.internet.MimeBodyPart r30 = r0.createMimeBodyPart(r1, r2)     // Catch:{ IOException -> 0x00a7 }
            goto L_0x0222
        L_0x0312:
            r36 = move-exception
            goto L_0x00ba
        L_0x0315:
            r33 = move-exception
            goto L_0x0173
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MimeMultipart.parse():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:221:0x01c3 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void parsebm() throws javax.mail.MessagingException {
        /*
            r48 = this;
            monitor-enter(r48)
            r0 = r48
            boolean r0 = r0.parsed     // Catch:{ all -> 0x00a8 }
            r43 = r0
            if (r43 == 0) goto L_0x000b
        L_0x0009:
            monitor-exit(r48)
            return
        L_0x000b:
            r25 = 0
            r38 = 0
            r40 = 0
            r18 = 0
            r0 = r48
            javax.activation.DataSource r0 = r0.ds     // Catch:{ Exception -> 0x00ab }
            r43 = r0
            java.io.InputStream r25 = r43.getInputStream()     // Catch:{ Exception -> 0x00ab }
            r0 = r25
            boolean r0 = r0 instanceof java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x00ab }
            r43 = r0
            if (r43 != 0) goto L_0x0040
            r0 = r25
            boolean r0 = r0 instanceof java.io.BufferedInputStream     // Catch:{ Exception -> 0x00ab }
            r43 = r0
            if (r43 != 0) goto L_0x0040
            r0 = r25
            boolean r0 = r0 instanceof javax.mail.internet.SharedInputStream     // Catch:{ Exception -> 0x00ab }
            r43 = r0
            if (r43 != 0) goto L_0x0040
            java.io.BufferedInputStream r26 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x00ab }
            r0 = r26
            r1 = r25
            r0.<init>(r1)     // Catch:{ Exception -> 0x00ab }
            r25 = r26
        L_0x0040:
            r0 = r25
            boolean r0 = r0 instanceof javax.mail.internet.SharedInputStream     // Catch:{ all -> 0x00a8 }
            r43 = r0
            if (r43 == 0) goto L_0x004e
            r0 = r25
            javax.mail.internet.SharedInputStream r0 = (javax.mail.internet.SharedInputStream) r0     // Catch:{ all -> 0x00a8 }
            r38 = r0
        L_0x004e:
            javax.mail.internet.ContentType r15 = new javax.mail.internet.ContentType     // Catch:{ all -> 0x00a8 }
            r0 = r48
            java.lang.String r0 = r0.contentType     // Catch:{ all -> 0x00a8 }
            r43 = r0
            r0 = r43
            r15.<init>(r0)     // Catch:{ all -> 0x00a8 }
            r11 = 0
            java.lang.String r43 = "boundary"
            r0 = r43
            java.lang.String r12 = r15.getParameter(r0)     // Catch:{ all -> 0x00a8 }
            if (r12 == 0) goto L_0x00ba
            java.lang.StringBuilder r43 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a8 }
            java.lang.String r44 = "--"
            r43.<init>(r44)     // Catch:{ all -> 0x00a8 }
            r0 = r43
            java.lang.StringBuilder r43 = r0.append(r12)     // Catch:{ all -> 0x00a8 }
            java.lang.String r11 = r43.toString()     // Catch:{ all -> 0x00a8 }
        L_0x0077:
            com.sun.mail.util.LineInputStream r31 = new com.sun.mail.util.LineInputStream     // Catch:{ IOException -> 0x0094 }
            r0 = r31
            r1 = r25
            r0.<init>(r1)     // Catch:{ IOException -> 0x0094 }
            r35 = 0
            r33 = 0
        L_0x0084:
            java.lang.String r32 = r31.readLine()     // Catch:{ IOException -> 0x0094 }
            if (r32 != 0) goto L_0x00c6
        L_0x008a:
            if (r32 != 0) goto L_0x0143
            javax.mail.MessagingException r43 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x0094 }
            java.lang.String r44 = "Missing start boundary"
            r43.<init>(r44)     // Catch:{ IOException -> 0x0094 }
            throw r43     // Catch:{ IOException -> 0x0094 }
        L_0x0094:
            r29 = move-exception
            javax.mail.MessagingException r43 = new javax.mail.MessagingException     // Catch:{ all -> 0x00a3 }
            java.lang.String r44 = "IO Error"
            r0 = r43
            r1 = r44
            r2 = r29
            r0.<init>(r1, r2)     // Catch:{ all -> 0x00a3 }
            throw r43     // Catch:{ all -> 0x00a3 }
        L_0x00a3:
            r43 = move-exception
            r25.close()     // Catch:{ IOException -> 0x03db }
        L_0x00a7:
            throw r43     // Catch:{ all -> 0x00a8 }
        L_0x00a8:
            r43 = move-exception
            monitor-exit(r48)
            throw r43
        L_0x00ab:
            r20 = move-exception
            javax.mail.MessagingException r43 = new javax.mail.MessagingException     // Catch:{ all -> 0x00a8 }
            java.lang.String r44 = "No inputstream from datasource"
            r0 = r43
            r1 = r44
            r2 = r20
            r0.<init>(r1, r2)     // Catch:{ all -> 0x00a8 }
            throw r43     // Catch:{ all -> 0x00a8 }
        L_0x00ba:
            boolean r43 = ignoreMissingBoundaryParameter     // Catch:{ all -> 0x00a8 }
            if (r43 != 0) goto L_0x0077
            javax.mail.MessagingException r43 = new javax.mail.MessagingException     // Catch:{ all -> 0x00a8 }
            java.lang.String r44 = "Missing boundary parameter"
            r43.<init>(r44)     // Catch:{ all -> 0x00a8 }
            throw r43     // Catch:{ all -> 0x00a8 }
        L_0x00c6:
            int r43 = r32.length()     // Catch:{ IOException -> 0x0094 }
            int r24 = r43 + -1
        L_0x00cc:
            if (r24 >= 0) goto L_0x0118
        L_0x00ce:
            r43 = 0
            int r44 = r24 + 1
            r0 = r32
            r1 = r43
            r2 = r44
            java.lang.String r32 = r0.substring(r1, r2)     // Catch:{ IOException -> 0x0094 }
            if (r11 == 0) goto L_0x012f
            r0 = r32
            boolean r43 = r0.equals(r11)     // Catch:{ IOException -> 0x0094 }
            if (r43 != 0) goto L_0x008a
        L_0x00e6:
            int r43 = r32.length()     // Catch:{ IOException -> 0x0094 }
            if (r43 <= 0) goto L_0x0084
            if (r33 != 0) goto L_0x00f6
            java.lang.String r43 = "line.separator"
            java.lang.String r44 = "\n"
            java.lang.String r33 = java.lang.System.getProperty(r43, r44)     // Catch:{ SecurityException -> 0x013f }
        L_0x00f6:
            if (r35 != 0) goto L_0x0107
            java.lang.StringBuffer r35 = new java.lang.StringBuffer     // Catch:{ IOException -> 0x0094 }
            int r43 = r32.length()     // Catch:{ IOException -> 0x0094 }
            int r43 = r43 + 2
            r0 = r35
            r1 = r43
            r0.<init>(r1)     // Catch:{ IOException -> 0x0094 }
        L_0x0107:
            r0 = r35
            r1 = r32
            java.lang.StringBuffer r43 = r0.append(r1)     // Catch:{ IOException -> 0x0094 }
            r0 = r43
            r1 = r33
            r0.append(r1)     // Catch:{ IOException -> 0x0094 }
            goto L_0x0084
        L_0x0118:
            r0 = r32
            r1 = r24
            char r14 = r0.charAt(r1)     // Catch:{ IOException -> 0x0094 }
            r43 = 32
            r0 = r43
            if (r14 == r0) goto L_0x012c
            r43 = 9
            r0 = r43
            if (r14 != r0) goto L_0x00ce
        L_0x012c:
            int r24 = r24 + -1
            goto L_0x00cc
        L_0x012f:
            java.lang.String r43 = "--"
            r0 = r32
            r1 = r43
            boolean r43 = r0.startsWith(r1)     // Catch:{ IOException -> 0x0094 }
            if (r43 == 0) goto L_0x00e6
            r11 = r32
            goto L_0x008a
        L_0x013f:
            r20 = move-exception
            java.lang.String r33 = "\n"
            goto L_0x00f6
        L_0x0143:
            if (r35 == 0) goto L_0x014f
            java.lang.String r43 = r35.toString()     // Catch:{ IOException -> 0x0094 }
            r0 = r43
            r1 = r48
            r1.preamble = r0     // Catch:{ IOException -> 0x0094 }
        L_0x014f:
            byte[] r10 = com.sun.mail.util.ASCIIUtility.getBytes((java.lang.String) r11)     // Catch:{ IOException -> 0x0094 }
            int r9 = r10.length     // Catch:{ IOException -> 0x0094 }
            r43 = 256(0x100, float:3.59E-43)
            r0 = r43
            int[] r8 = new int[r0]     // Catch:{ IOException -> 0x0094 }
            r24 = 0
        L_0x015c:
            r0 = r24
            if (r0 < r9) goto L_0x017f
            int[] r0 = new int[r9]     // Catch:{ IOException -> 0x0094 }
            r22 = r0
            r24 = r9
        L_0x0166:
            if (r24 > 0) goto L_0x0188
            int r43 = r9 + -1
            r44 = 1
            r22[r43] = r44     // Catch:{ IOException -> 0x0094 }
            r16 = 0
        L_0x0170:
            if (r16 == 0) goto L_0x01ad
        L_0x0172:
            r25.close()     // Catch:{ IOException -> 0x03de }
        L_0x0175:
            r43 = 1
            r0 = r43
            r1 = r48
            r1.parsed = r0     // Catch:{ all -> 0x00a8 }
            goto L_0x0009
        L_0x017f:
            byte r43 = r10[r24]     // Catch:{ IOException -> 0x0094 }
            int r44 = r24 + 1
            r8[r43] = r44     // Catch:{ IOException -> 0x0094 }
            int r24 = r24 + 1
            goto L_0x015c
        L_0x0188:
            int r30 = r9 + -1
        L_0x018a:
            r0 = r30
            r1 = r24
            if (r0 >= r1) goto L_0x0195
        L_0x0190:
            if (r30 > 0) goto L_0x01a8
        L_0x0192:
            int r24 = r24 + -1
            goto L_0x0166
        L_0x0195:
            byte r43 = r10[r30]     // Catch:{ IOException -> 0x0094 }
            int r44 = r30 - r24
            byte r44 = r10[r44]     // Catch:{ IOException -> 0x0094 }
            r0 = r43
            r1 = r44
            if (r0 != r1) goto L_0x0192
            int r43 = r30 + -1
            r22[r43] = r24     // Catch:{ IOException -> 0x0094 }
            int r30 = r30 + -1
            goto L_0x018a
        L_0x01a8:
            int r30 = r30 + -1
            r22[r30] = r24     // Catch:{ IOException -> 0x0094 }
            goto L_0x0190
        L_0x01ad:
            r23 = 0
            if (r38 == 0) goto L_0x01d8
            long r40 = r38.getPosition()     // Catch:{ IOException -> 0x0094 }
        L_0x01b5:
            java.lang.String r32 = r31.readLine()     // Catch:{ IOException -> 0x0094 }
            if (r32 == 0) goto L_0x01c1
            int r43 = r32.length()     // Catch:{ IOException -> 0x0094 }
            if (r43 > 0) goto L_0x01b5
        L_0x01c1:
            if (r32 != 0) goto L_0x01e0
            boolean r43 = ignoreMissingEndBoundary     // Catch:{ IOException -> 0x0094 }
            if (r43 != 0) goto L_0x01cf
            javax.mail.MessagingException r43 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x0094 }
            java.lang.String r44 = "missing multipart end boundary"
            r43.<init>(r44)     // Catch:{ IOException -> 0x0094 }
            throw r43     // Catch:{ IOException -> 0x0094 }
        L_0x01cf:
            r43 = 0
            r0 = r43
            r1 = r48
            r1.complete = r0     // Catch:{ IOException -> 0x0094 }
            goto L_0x0172
        L_0x01d8:
            r0 = r48
            r1 = r25
            javax.mail.internet.InternetHeaders r23 = r0.createInternetHeaders(r1)     // Catch:{ IOException -> 0x0094 }
        L_0x01e0:
            boolean r43 = r25.markSupported()     // Catch:{ IOException -> 0x0094 }
            if (r43 != 0) goto L_0x01ee
            javax.mail.MessagingException r43 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x0094 }
            java.lang.String r44 = "Stream doesn't support mark"
            r43.<init>(r44)     // Catch:{ IOException -> 0x0094 }
            throw r43     // Catch:{ IOException -> 0x0094 }
        L_0x01ee:
            r13 = 0
            if (r38 != 0) goto L_0x0231
            java.io.ByteArrayOutputStream r13 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0094 }
            r13.<init>()     // Catch:{ IOException -> 0x0094 }
        L_0x01f6:
            byte[] r0 = new byte[r9]     // Catch:{ IOException -> 0x0094 }
            r28 = r0
            byte[] r0 = new byte[r9]     // Catch:{ IOException -> 0x0094 }
            r37 = r0
            r27 = 0
            r36 = 0
            r21 = 1
        L_0x0204:
            int r43 = r9 + 4
            r0 = r43
            int r0 = r0 + 1000
            r43 = r0
            r0 = r25
            r1 = r43
            r0.mark(r1)     // Catch:{ IOException -> 0x0094 }
            r17 = 0
            r43 = 0
            r0 = r25
            r1 = r28
            r2 = r43
            int r27 = readFully(r0, r1, r2, r9)     // Catch:{ IOException -> 0x0094 }
            r0 = r27
            if (r0 >= r9) goto L_0x0263
            boolean r43 = ignoreMissingEndBoundary     // Catch:{ IOException -> 0x0094 }
            if (r43 != 0) goto L_0x0236
            javax.mail.MessagingException r43 = new javax.mail.MessagingException     // Catch:{ IOException -> 0x0094 }
            java.lang.String r44 = "missing multipart end boundary"
            r43.<init>(r44)     // Catch:{ IOException -> 0x0094 }
            throw r43     // Catch:{ IOException -> 0x0094 }
        L_0x0231:
            long r18 = r38.getPosition()     // Catch:{ IOException -> 0x0094 }
            goto L_0x01f6
        L_0x0236:
            if (r38 == 0) goto L_0x023c
            long r18 = r38.getPosition()     // Catch:{ IOException -> 0x0094 }
        L_0x023c:
            r43 = 0
            r0 = r43
            r1 = r48
            r1.complete = r0     // Catch:{ IOException -> 0x0094 }
            r16 = 1
        L_0x0246:
            if (r38 == 0) goto L_0x03a5
            r0 = r38
            r1 = r40
            r3 = r18
            java.io.InputStream r43 = r0.newStream(r1, r3)     // Catch:{ IOException -> 0x0094 }
            r0 = r48
            r1 = r43
            javax.mail.internet.MimeBodyPart r34 = r0.createMimeBodyPart(r1)     // Catch:{ IOException -> 0x0094 }
        L_0x025a:
            r0 = r48
            r1 = r34
            super.addBodyPart(r1)     // Catch:{ IOException -> 0x0094 }
            goto L_0x0170
        L_0x0263:
            int r24 = r9 + -1
        L_0x0265:
            if (r24 >= 0) goto L_0x02d1
        L_0x0267:
            if (r24 >= 0) goto L_0x0316
            r17 = 0
            if (r21 != 0) goto L_0x0299
            int r43 = r36 + -1
            byte r6 = r37[r43]     // Catch:{ IOException -> 0x0094 }
            r43 = 13
            r0 = r43
            if (r6 == r0) goto L_0x027d
            r43 = 10
            r0 = r43
            if (r6 != r0) goto L_0x0299
        L_0x027d:
            r17 = 1
            r43 = 10
            r0 = r43
            if (r6 != r0) goto L_0x0299
            r43 = 2
            r0 = r36
            r1 = r43
            if (r0 < r1) goto L_0x0299
            int r43 = r36 + -2
            byte r6 = r37[r43]     // Catch:{ IOException -> 0x0094 }
            r43 = 13
            r0 = r43
            if (r6 != r0) goto L_0x0299
            r17 = 2
        L_0x0299:
            if (r21 != 0) goto L_0x029d
            if (r17 <= 0) goto L_0x0314
        L_0x029d:
            if (r38 == 0) goto L_0x02af
            long r44 = r38.getPosition()     // Catch:{ IOException -> 0x0094 }
            long r0 = (long) r9     // Catch:{ IOException -> 0x0094 }
            r46 = r0
            long r44 = r44 - r46
            r0 = r17
            long r0 = (long) r0     // Catch:{ IOException -> 0x0094 }
            r46 = r0
            long r18 = r44 - r46
        L_0x02af:
            int r7 = r25.read()     // Catch:{ IOException -> 0x0094 }
            r43 = 45
            r0 = r43
            if (r7 != r0) goto L_0x02e2
            int r43 = r25.read()     // Catch:{ IOException -> 0x0094 }
            r44 = 45
            r0 = r43
            r1 = r44
            if (r0 != r1) goto L_0x02e2
            r43 = 1
            r0 = r43
            r1 = r48
            r1.complete = r0     // Catch:{ IOException -> 0x0094 }
            r16 = 1
            goto L_0x0246
        L_0x02d1:
            byte r43 = r28[r24]     // Catch:{ IOException -> 0x0094 }
            byte r44 = r10[r24]     // Catch:{ IOException -> 0x0094 }
            r0 = r43
            r1 = r44
            if (r0 != r1) goto L_0x0267
            int r24 = r24 + -1
            goto L_0x0265
        L_0x02de:
            int r7 = r25.read()     // Catch:{ IOException -> 0x0094 }
        L_0x02e2:
            r43 = 32
            r0 = r43
            if (r7 == r0) goto L_0x02de
            r43 = 9
            r0 = r43
            if (r7 == r0) goto L_0x02de
            r43 = 10
            r0 = r43
            if (r7 == r0) goto L_0x0246
            r43 = 13
            r0 = r43
            if (r7 != r0) goto L_0x0314
            r43 = 1
            r0 = r25
            r1 = r43
            r0.mark(r1)     // Catch:{ IOException -> 0x0094 }
            int r43 = r25.read()     // Catch:{ IOException -> 0x0094 }
            r44 = 10
            r0 = r43
            r1 = r44
            if (r0 == r1) goto L_0x0246
            r25.reset()     // Catch:{ IOException -> 0x0094 }
            goto L_0x0246
        L_0x0314:
            r24 = 0
        L_0x0316:
            int r43 = r24 + 1
            byte r44 = r28[r24]     // Catch:{ IOException -> 0x0094 }
            r44 = r44 & 127(0x7f, float:1.78E-43)
            r44 = r8[r44]     // Catch:{ IOException -> 0x0094 }
            int r43 = r43 - r44
            r44 = r22[r24]     // Catch:{ IOException -> 0x0094 }
            int r39 = java.lang.Math.max(r43, r44)     // Catch:{ IOException -> 0x0094 }
            r43 = 2
            r0 = r39
            r1 = r43
            if (r0 >= r1) goto L_0x037c
            if (r38 != 0) goto L_0x0345
            r43 = 1
            r0 = r36
            r1 = r43
            if (r0 <= r1) goto L_0x0345
            r43 = 0
            int r44 = r36 + -1
            r0 = r37
            r1 = r43
            r2 = r44
            r13.write(r0, r1, r2)     // Catch:{ IOException -> 0x0094 }
        L_0x0345:
            r25.reset()     // Catch:{ IOException -> 0x0094 }
            r44 = 1
            r0 = r48
            r1 = r25
            r2 = r44
            r0.skipFully(r1, r2)     // Catch:{ IOException -> 0x0094 }
            r43 = 1
            r0 = r36
            r1 = r43
            if (r0 < r1) goto L_0x0371
            r43 = 0
            int r44 = r36 + -1
            byte r44 = r37[r44]     // Catch:{ IOException -> 0x0094 }
            r37[r43] = r44     // Catch:{ IOException -> 0x0094 }
            r43 = 1
            r44 = 0
            byte r44 = r28[r44]     // Catch:{ IOException -> 0x0094 }
            r37[r43] = r44     // Catch:{ IOException -> 0x0094 }
            r36 = 2
        L_0x036d:
            r21 = 0
            goto L_0x0204
        L_0x0371:
            r43 = 0
            r44 = 0
            byte r44 = r28[r44]     // Catch:{ IOException -> 0x0094 }
            r37[r43] = r44     // Catch:{ IOException -> 0x0094 }
            r36 = 1
            goto L_0x036d
        L_0x037c:
            if (r36 <= 0) goto L_0x038b
            if (r38 != 0) goto L_0x038b
            r43 = 0
            r0 = r37
            r1 = r43
            r2 = r36
            r13.write(r0, r1, r2)     // Catch:{ IOException -> 0x0094 }
        L_0x038b:
            r36 = r39
            r25.reset()     // Catch:{ IOException -> 0x0094 }
            r0 = r36
            long r0 = (long) r0     // Catch:{ IOException -> 0x0094 }
            r44 = r0
            r0 = r48
            r1 = r25
            r2 = r44
            r0.skipFully(r1, r2)     // Catch:{ IOException -> 0x0094 }
            r42 = r28
            r28 = r37
            r37 = r42
            goto L_0x036d
        L_0x03a5:
            int r43 = r36 - r17
            if (r43 <= 0) goto L_0x03b6
            r43 = 0
            int r44 = r36 - r17
            r0 = r37
            r1 = r43
            r2 = r44
            r13.write(r0, r1, r2)     // Catch:{ IOException -> 0x0094 }
        L_0x03b6:
            r0 = r48
            boolean r0 = r0.complete     // Catch:{ IOException -> 0x0094 }
            r43 = r0
            if (r43 != 0) goto L_0x03cb
            if (r27 <= 0) goto L_0x03cb
            r43 = 0
            r0 = r28
            r1 = r43
            r2 = r27
            r13.write(r0, r1, r2)     // Catch:{ IOException -> 0x0094 }
        L_0x03cb:
            byte[] r43 = r13.toByteArray()     // Catch:{ IOException -> 0x0094 }
            r0 = r48
            r1 = r23
            r2 = r43
            javax.mail.internet.MimeBodyPart r34 = r0.createMimeBodyPart(r1, r2)     // Catch:{ IOException -> 0x0094 }
            goto L_0x025a
        L_0x03db:
            r44 = move-exception
            goto L_0x00a7
        L_0x03de:
            r43 = move-exception
            goto L_0x0175
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MimeMultipart.parsebm():void");
    }

    private static int readFully(InputStream in, byte[] buf, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }
        int total = 0;
        while (len > 0) {
            int bsize = in.read(buf, off, len);
            if (bsize <= 0) {
                break;
            }
            off += bsize;
            total += bsize;
            len -= bsize;
        }
        if (total <= 0) {
            return -1;
        }
        return total;
    }

    private void skipFully(InputStream in, long offset) throws IOException {
        while (offset > 0) {
            long cur = in.skip(offset);
            if (cur <= 0) {
                throw new EOFException("can't skip");
            }
            offset -= cur;
        }
    }

    /* access modifiers changed from: protected */
    public InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }

    /* access modifiers changed from: protected */
    public MimeBodyPart createMimeBodyPart(InternetHeaders headers, byte[] content) throws MessagingException {
        return new MimeBodyPart(headers, content);
    }

    /* access modifiers changed from: protected */
    public MimeBodyPart createMimeBodyPart(InputStream is) throws MessagingException {
        return new MimeBodyPart(is);
    }
}
