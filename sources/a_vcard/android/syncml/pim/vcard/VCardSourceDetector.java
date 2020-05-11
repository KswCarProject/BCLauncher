package a_vcard.android.syncml.pim.vcard;

import a_vcard.android.syncml.pim.VBuilder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VCardSourceDetector implements VBuilder {
    private static Set<String> APPLE_SIGNS = new HashSet(Arrays.asList(new String[]{"X-PHONETIC-FIRST-NAME", "X-PHONETIC-MIDDLE-NAME", "X-PHONETIC-LAST-NAME", "X-ABADR", "X-ABUID"}));
    private static Set<String> FOMA_SIGNS = new HashSet(Arrays.asList(new String[]{"X-SD-VERN", "X-SD-FORMAT_VER", "X-SD-CATEGORIES", "X-SD-CLASS", "X-SD-DCREATED", "X-SD-DESCRIPTION"}));
    private static Set<String> JAPANESE_MOBILE_PHONE_SIGNS = new HashSet(Arrays.asList(new String[]{"X-GNO", "X-GN", "X-REDUCTION"}));
    static final int TYPE_APPLE = 1;
    static final int TYPE_FOMA = 3;
    private static String TYPE_FOMA_CHARSET_SIGN = "X-SD-CHAR_CODE";
    static final int TYPE_JAPANESE_MOBILE_PHONE = 2;
    static final int TYPE_UNKNOWN = 0;
    static final int TYPE_WINDOWS_MOBILE_JP = 4;
    private static Set<String> WINDOWS_MOBILE_PHONE_SIGNS = new HashSet(Arrays.asList(new String[]{"X-MICROSOFT-ASST_TEL", "X-MICROSOFT-ASSISTANT", "X-MICROSOFT-OFFICELOC"}));
    private boolean mNeedParseSpecifiedCharset;
    private String mSpecifiedCharset;
    private int mType = 0;

    public void start() {
    }

    public void end() {
    }

    public void startRecord(String type) {
    }

    public void startProperty() {
        this.mNeedParseSpecifiedCharset = false;
    }

    public void endProperty() {
    }

    public void endRecord() {
    }

    public void propertyGroup(String group) {
    }

    public void propertyName(String name) {
        if (name.equalsIgnoreCase(TYPE_FOMA_CHARSET_SIGN)) {
            this.mType = 3;
            this.mNeedParseSpecifiedCharset = true;
        } else if (this.mType != 0) {
        } else {
            if (WINDOWS_MOBILE_PHONE_SIGNS.contains(name)) {
                this.mType = 4;
            } else if (FOMA_SIGNS.contains(name)) {
                this.mType = 3;
            } else if (JAPANESE_MOBILE_PHONE_SIGNS.contains(name)) {
                this.mType = 2;
            } else if (APPLE_SIGNS.contains(name)) {
                this.mType = 1;
            }
        }
    }

    public void propertyParamType(String type) {
    }

    public void propertyParamValue(String value) {
    }

    public void propertyValues(List<String> values) {
        if (this.mNeedParseSpecifiedCharset && values.size() > 0) {
            this.mSpecifiedCharset = values.get(0);
        }
    }

    /* access modifiers changed from: package-private */
    public int getType() {
        return this.mType;
    }

    public String getEstimatedCharset() {
        if (this.mSpecifiedCharset != null) {
            return this.mSpecifiedCharset;
        }
        switch (this.mType) {
            case 1:
                return VCardParser_V21.DEFAULT_CHARSET;
            case 2:
            case 3:
            case 4:
                return "SHIFT_JIS";
            default:
                return null;
        }
    }
}
