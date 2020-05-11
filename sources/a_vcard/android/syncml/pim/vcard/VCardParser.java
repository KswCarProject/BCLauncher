package a_vcard.android.syncml.pim.vcard;

import a_vcard.android.syncml.pim.VDataBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class VCardParser {
    private static final String TAG = "VCardParser";
    public static final String VERSION_VCARD21 = "vcard2.1";
    public static final int VERSION_VCARD21_INT = 1;
    public static final String VERSION_VCARD30 = "vcard3.0";
    public static final int VERSION_VCARD30_INT = 2;
    VCardParser_V21 mParser = null;
    String mVersion = null;

    private void judgeVersion(String vcardStr) {
        if (this.mVersion == null) {
            int verIdx = vcardStr.indexOf("\nVERSION:");
            if (verIdx == -1) {
                this.mVersion = VERSION_VCARD21;
            } else {
                String verStr = vcardStr.substring(verIdx, vcardStr.indexOf("\n", verIdx + 1));
                if (verStr.indexOf("2.1") > 0) {
                    this.mVersion = VERSION_VCARD21;
                } else if (verStr.indexOf("3.0") > 0) {
                    this.mVersion = VERSION_VCARD30;
                } else {
                    this.mVersion = VERSION_VCARD21;
                }
            }
        }
        if (this.mVersion.equals(VERSION_VCARD21)) {
            this.mParser = new VCardParser_V21();
        }
        if (this.mVersion.equals(VERSION_VCARD30)) {
            this.mParser = new VCardParser_V30();
        }
    }

    private String verifyVCard(String vcardStr) {
        judgeVersion(vcardStr);
        String[] strlist = vcardStr.replaceAll("\r\n", "\n").split("\n");
        StringBuilder v21str = new StringBuilder("");
        for (int i = 0; i < strlist.length; i++) {
            if (strlist[i].indexOf(":") >= 0) {
                v21str.append(strlist[i]).append("\r\n");
            } else if (strlist[i].length() != 0 || strlist[i + 1].indexOf(":") <= 0) {
                v21str.append(" ").append(strlist[i]).append("\r\n");
            } else {
                v21str.append(strlist[i]).append("\r\n");
            }
        }
        return v21str.toString();
    }

    private void setVersion(String version) {
        this.mVersion = version;
    }

    public boolean parse(String vcardStr, String encoding, VDataBuilder builder) throws VCardException, IOException {
        String vcardStr2 = verifyVCard(vcardStr);
        if (this.mParser.parse(new ByteArrayInputStream(vcardStr2.getBytes(encoding)), encoding, builder)) {
            return true;
        }
        if (this.mVersion.equals(VERSION_VCARD21)) {
            setVersion(VERSION_VCARD30);
            return parse(vcardStr2, builder);
        }
        throw new VCardException("parse failed.(even use 3.0 parser)");
    }

    public boolean parse(String vcardStr, VDataBuilder builder) throws VCardException, IOException {
        return parse(vcardStr, "US-ASCII", builder);
    }
}
