package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import android.support.v4.widget.ViewDragHelper;

public class MediaInfoProtocol extends BaseMultilineProtocol {
    private String album;
    private String artist;
    private String genre;
    private boolean isAnalyzed = false;
    private int number;
    private int playTime;
    private String title;
    private int totalNumber;

    public void setNumber(int number2) {
        this.number = number2;
    }

    public void setTotalNumber(int totalNumber2) {
        this.totalNumber = totalNumber2;
    }

    public void setPlayTime(int playTime2) {
        this.playTime = playTime2;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void analyze() {
        /*
            r13 = this;
            java.util.List r9 = super.getUnits()
            java.util.Iterator r9 = r9.iterator()
        L_0x0008:
            boolean r10 = r9.hasNext()
            if (r10 != 0) goto L_0x000f
            return
        L_0x000f:
            java.lang.Object r4 = r9.next()
            r0 = r4
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoUnitProtocol r0 = (com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoUnitProtocol) r0     // Catch:{ Exception -> 0x0032 }
            r8 = r0
            byte[] r6 = r8.getPlayload()     // Catch:{ Exception -> 0x0032 }
            java.lang.String r1 = r13.getCharSet(r6)     // Catch:{ Exception -> 0x0032 }
            r10 = 0
            byte r10 = r6[r10]     // Catch:{ Exception -> 0x0032 }
            switch(r10) {
                case 1: goto L_0x0026;
                case 2: goto L_0x0037;
                case 3: goto L_0x0043;
                case 4: goto L_0x004f;
                case 5: goto L_0x005f;
                case 6: goto L_0x006f;
                case 7: goto L_0x007b;
                default: goto L_0x0025;
            }     // Catch:{ Exception -> 0x0032 }
        L_0x0025:
            goto L_0x0008
        L_0x0026:
            java.lang.String r10 = new java.lang.String     // Catch:{ Exception -> 0x0032 }
            r11 = 3
            int r12 = r6.length     // Catch:{ Exception -> 0x0032 }
            int r12 = r12 + -3
            r10.<init>(r6, r11, r12, r1)     // Catch:{ Exception -> 0x0032 }
            r13.title = r10     // Catch:{ Exception -> 0x0032 }
            goto L_0x0008
        L_0x0032:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0008
        L_0x0037:
            java.lang.String r10 = new java.lang.String     // Catch:{ Exception -> 0x0032 }
            r11 = 3
            int r12 = r6.length     // Catch:{ Exception -> 0x0032 }
            int r12 = r12 + -3
            r10.<init>(r6, r11, r12, r1)     // Catch:{ Exception -> 0x0032 }
            r13.artist = r10     // Catch:{ Exception -> 0x0032 }
            goto L_0x0008
        L_0x0043:
            java.lang.String r10 = new java.lang.String     // Catch:{ Exception -> 0x0032 }
            r11 = 3
            int r12 = r6.length     // Catch:{ Exception -> 0x0032 }
            int r12 = r12 + -3
            r10.<init>(r6, r11, r12, r1)     // Catch:{ Exception -> 0x0032 }
            r13.album = r10     // Catch:{ Exception -> 0x0032 }
            goto L_0x0008
        L_0x004f:
            java.lang.String r3 = new java.lang.String     // Catch:{ Exception -> 0x0032 }
            r10 = 3
            int r11 = r6.length     // Catch:{ Exception -> 0x0032 }
            int r11 = r11 + -3
            r3.<init>(r6, r10, r11, r1)     // Catch:{ Exception -> 0x0032 }
            int r10 = java.lang.Integer.parseInt(r3)     // Catch:{ Exception -> 0x0032 }
            r13.number = r10     // Catch:{ Exception -> 0x0032 }
            goto L_0x0008
        L_0x005f:
            java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x0032 }
            r10 = 3
            int r11 = r6.length     // Catch:{ Exception -> 0x0032 }
            int r11 = r11 + -3
            r7.<init>(r6, r10, r11, r1)     // Catch:{ Exception -> 0x0032 }
            int r10 = java.lang.Integer.parseInt(r7)     // Catch:{ Exception -> 0x0032 }
            r13.totalNumber = r10     // Catch:{ Exception -> 0x0032 }
            goto L_0x0008
        L_0x006f:
            java.lang.String r10 = new java.lang.String     // Catch:{ Exception -> 0x0032 }
            r11 = 3
            int r12 = r6.length     // Catch:{ Exception -> 0x0032 }
            int r12 = r12 + -3
            r10.<init>(r6, r11, r12, r1)     // Catch:{ Exception -> 0x0032 }
            r13.genre = r10     // Catch:{ Exception -> 0x0032 }
            goto L_0x0008
        L_0x007b:
            java.lang.String r5 = new java.lang.String     // Catch:{ Exception -> 0x0032 }
            r10 = 3
            int r11 = r6.length     // Catch:{ Exception -> 0x0032 }
            int r11 = r11 + -3
            r5.<init>(r6, r10, r11, r1)     // Catch:{ Exception -> 0x0032 }
            int r10 = java.lang.Integer.parseInt(r5)     // Catch:{ Exception -> 0x0032 }
            r13.playTime = r10     // Catch:{ Exception -> 0x0032 }
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoProtocol.analyze():void");
    }

    private String getCharSet(byte[] playload) {
        switch (playload[1] + (playload[2] << 8)) {
            case 3:
                return "ASCII";
            case 4:
                return "ISO-8859-1";
            case ViewDragHelper.EDGE_ALL:
                return "JIS-X0201";
            case 17:
                return "SHIFT-JIS";
            case 36:
                return "KS-C-5601-1987";
            case 106:
                return VCardParser_V21.DEFAULT_CHARSET;
            case 1000:
                return "UCS2";
            case 1013:
                return "UTF-16BE";
            case 2025:
                return "GB2312";
            case 2026:
                return "BIG5";
            default:
                return null;
        }
    }

    public String getTitle() {
        if (!this.isAnalyzed) {
            analyze();
        }
        return this.title;
    }

    public String getArtist() {
        if (!this.isAnalyzed) {
            analyze();
        }
        return this.artist;
    }

    public String getAlbum() {
        if (!this.isAnalyzed) {
            analyze();
        }
        return this.album;
    }

    public int getNumber() {
        if (!this.isAnalyzed) {
            analyze();
        }
        return this.number;
    }

    public int getTotalNumber() {
        if (!this.isAnalyzed) {
            analyze();
        }
        return this.totalNumber;
    }

    public String getGenre() {
        if (!this.isAnalyzed) {
            analyze();
        }
        return this.genre;
    }

    public int getPlayTime() {
        if (!this.isAnalyzed) {
            analyze();
        }
        return this.playTime;
    }

    public void setAnalyzed(boolean isAnalyzed2) {
        this.isAnalyzed = isAnalyzed2;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public void setArtist(String singer) {
        this.artist = singer;
    }
}
