package com.touchus.publicutils.utils;

import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibVcard {
    public static final int CALL_TYPE_IN = 1412;
    public static final int CALL_TYPE_MISS = 1414;
    public static final int CALL_TYPE_OUT = 1413;
    boolean isgetend = false;
    boolean isgethead = false;
    private List<VcardUnit> list = new ArrayList();
    private VcardUnit unit = null;

    public LibVcard(byte[] vcardData) throws Exception {
        if (vcardData != null) {
            int startIndex = 0;
            for (int i = 0; i < vcardData.length; i++) {
                if (vcardData[i] == 10 && i - 1 >= 0 && i - 1 < vcardData.length && vcardData[i - 1] == 13 && i - 2 >= 0 && i - 2 < vcardData.length && vcardData[i - 2] != 61) {
                    int endIndex = i - 2;
                    byte[] line = new byte[((endIndex - startIndex) + 1)];
                    int t = startIndex;
                    int tt = 0;
                    while (t <= endIndex && tt < line.length) {
                        line[tt] = vcardData[t];
                        t++;
                        tt++;
                    }
                    processLine(line);
                    startIndex = endIndex + 3;
                }
            }
        }
    }

    private void processLine(byte[] line) throws Exception {
        String name;
        String lineStr = new String(line);
        Log.e("VcardUnit", lineStr);
        if (!this.isgethead) {
            if ("BEGIN:VCARD".equals(lineStr)) {
                this.unit = new VcardUnit();
                this.isgethead = true;
            }
        } else if (this.isgetend) {
        } else {
            if ("END:VCARD".equals(lineStr)) {
                if (this.unit != null && this.unit.isvalid()) {
                    this.list.add(this.unit);
                }
                this.isgethead = false;
                this.isgetend = false;
                return;
            }
            if (lineStr.startsWith("N") || lineStr.startsWith("FN")) {
                Matcher m = Pattern.compile("CHARSET=([0-9A-Za-z\\-]+)").matcher(lineStr);
                String encoding = VCardParser_V21.DEFAULT_CHARSET;
                if (m.find()) {
                    encoding = m.group(1);
                }
                String newStr = new String(line, encoding);
                String name2 = newStr.substring(newStr.indexOf(":") + 1);
                if (newStr.indexOf("ENCODING=QUOTED-PRINTABLE") != -1) {
                    String[] namesp = name2.split(";");
                    if (namesp != null && namesp.length >= 3) {
                        name2 = String.valueOf(namesp[0]) + namesp[2] + namesp[1];
                    }
                    name = QuotedPrintable.decode(name2.replace(";", "").getBytes(), encoding);
                } else {
                    String[] namesp2 = name2.split(";");
                    if (namesp2 != null && namesp2.length >= 3) {
                        name2 = String.valueOf(namesp2[0]) + namesp2[2] + namesp2[1];
                    }
                    name = name2.replace(";", "");
                }
                this.unit.setName(name);
            }
            if (lineStr.indexOf("TEL") == 0) {
                Matcher m2 = Pattern.compile("TEL[;a-zA-Z\\=\\-]*:([0-9\\-\\+ ]+)").matcher(lineStr);
                if (m2.find()) {
                    String number = m2.group(1).replace("-", "").replace(" ", "");
                    if (number.startsWith("+86")) {
                        number = number.substring(3);
                    }
                    this.unit.appendNumber(number);
                    if (this.unit.getName() == null || "".equals(this.unit.getName())) {
                        this.unit.setName(number);
                    }
                }
            }
            if (lineStr.indexOf("X-IRMC-CALL-DATETIME") == 0) {
                String type = lineStr.substring(lineStr.indexOf(";") + 1, lineStr.indexOf(":"));
                if ("MISSED".equals(type)) {
                    this.unit.setFlag(1414);
                } else if ("DIALED".equals(type)) {
                    this.unit.setFlag(1413);
                } else {
                    this.unit.setFlag(1412);
                }
                String date = lineStr.substring(lineStr.indexOf(":") + 1, lineStr.lastIndexOf("T"));
                String time = lineStr.substring(lineStr.lastIndexOf("T") + 1);
                this.unit.setRemark(date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6) + " " + time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4));
            }
        }
    }

    public List<VcardUnit> getList() {
        return this.list;
    }
}
