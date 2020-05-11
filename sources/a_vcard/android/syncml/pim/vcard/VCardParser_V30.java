package a_vcard.android.syncml.pim.vcard;

import a_vcard.android.util.Log;
import com.touchus.benchilauncher.bean.GuideInfoExtraKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class VCardParser_V30 extends VCardParser_V21 {
    private static final String LOG_TAG = "VCardParser_V30";
    private static final HashSet<String> acceptablePropsWithParam = new HashSet<>(Arrays.asList(new String[]{"BEGIN", "LOGO", "PHOTO", "LABEL", "FN", "TITLE", "SOUND", "VERSION", "TEL", "EMAIL", "TZ", "GEO", "NOTE", "URL", "BDAY", "ROLE", "REV", "UID", "KEY", "MAILER", "NAME", "PROFILE", "SOURCE", "NICKNAME", "CLASS", "SORT-STRING", "CATEGORIES", "PRODID"}));
    private static final HashSet<String> acceptablePropsWithoutParam = new HashSet<>();
    private static final HashSet<String> sAcceptableEncodingV30 = new HashSet<>(Arrays.asList(new String[]{"7BIT", "8BIT", "BASE64", "B"}));
    private String mPreviousLine;

    /* access modifiers changed from: protected */
    public String getVersion() {
        return "3.0";
    }

    /* access modifiers changed from: protected */
    public boolean isValidPropertyName(String propertyName) {
        if (acceptablePropsWithParam.contains(propertyName) || acceptablePropsWithoutParam.contains(propertyName) || propertyName.startsWith("X-") || this.mWarningValueMap.contains(propertyName)) {
            return true;
        }
        this.mWarningValueMap.add(propertyName);
        Log.w(LOG_TAG, "Property name unsupported by vCard 3.0: " + propertyName);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isValidEncoding(String encoding) {
        return sAcceptableEncodingV30.contains(encoding.toUpperCase());
    }

    /* access modifiers changed from: protected */
    public String getLine() throws IOException {
        if (this.mPreviousLine == null) {
            return this.mReader.readLine();
        }
        String ret = this.mPreviousLine;
        this.mPreviousLine = null;
        return ret;
    }

    /* access modifiers changed from: protected */
    public String getNonEmptyLine() throws IOException, VCardException {
        StringBuilder builder = null;
        while (true) {
            String line = this.mReader.readLine();
            if (line == null) {
                if (builder != null) {
                    return builder.toString();
                }
                if (this.mPreviousLine != null) {
                    String str = this.mPreviousLine;
                    this.mPreviousLine = null;
                    return str;
                }
                throw new VCardException("Reached end of buffer.");
            } else if (line.length() == 0) {
                if (builder != null) {
                    return builder.toString();
                }
                if (this.mPreviousLine != null) {
                    String str2 = this.mPreviousLine;
                    this.mPreviousLine = null;
                    return str2;
                }
            } else if (line.charAt(0) == ' ' || line.charAt(0) == 9) {
                if (builder != null) {
                    builder.append(line.substring(1));
                } else if (this.mPreviousLine != null) {
                    builder = new StringBuilder();
                    builder.append(this.mPreviousLine);
                    this.mPreviousLine = null;
                    builder.append(line.substring(1));
                } else {
                    throw new VCardException("Space exists at the beginning of the line");
                }
            } else if (this.mPreviousLine == null) {
                this.mPreviousLine = line;
                if (builder != null) {
                    return builder.toString();
                }
            } else {
                String str3 = this.mPreviousLine;
                this.mPreviousLine = line;
                return str3;
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean readBeginVCard(boolean allowGarbage) throws IOException, VCardException {
        return super.readBeginVCard(allowGarbage);
    }

    /* access modifiers changed from: protected */
    public void readEndVCard(boolean useCache, boolean allowGarbage) throws IOException, VCardException {
        super.readEndVCard(useCache, allowGarbage);
    }

    /* access modifiers changed from: protected */
    public void handleParams(String params) throws VCardException {
        try {
            super.handleParams(params);
        } catch (VCardException e) {
            String[] strArray = params.split("=", 2);
            if (strArray.length == 2) {
                handleAnyParam(strArray[0], strArray[1]);
                return;
            }
            throw new VCardException("Unknown params value: " + params);
        }
    }

    /* access modifiers changed from: protected */
    public void handleAnyParam(String paramName, String paramValue) {
        super.handleAnyParam(paramName, paramValue);
    }

    /* access modifiers changed from: protected */
    public void handleType(String ptypevalues) {
        String[] ptypeArray = ptypevalues.split(",");
        this.mBuilder.propertyParamType(GuideInfoExtraKey.TYPE);
        for (String value : ptypeArray) {
            if (value.length() < 2 || !value.startsWith("\"") || !value.endsWith("\"")) {
                this.mBuilder.propertyParamValue(value);
            } else {
                this.mBuilder.propertyParamValue(value.substring(1, value.length() - 1));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleAgent(String propertyValue) throws VCardException {
        throw new VCardException("AGENT in vCard 3.0 is not supported yet.");
    }

    /* access modifiers changed from: protected */
    public String getBase64(String firstString) throws IOException, VCardException {
        StringBuilder builder = new StringBuilder();
        builder.append(firstString);
        while (true) {
            String line = getLine();
            if (line != null) {
                if (line.length() != 0) {
                    if (!line.startsWith(" ") && !line.startsWith("\t")) {
                        this.mPreviousLine = line;
                        break;
                    }
                    builder.append(line);
                } else {
                    break;
                }
            } else {
                throw new VCardException("File ended during parsing BASE64 binary");
            }
        }
        return builder.toString();
    }

    /* access modifiers changed from: protected */
    public String maybeUnescapeText(String text) {
        StringBuilder builder = new StringBuilder();
        int length = text.length();
        int i = 0;
        while (i < length) {
            char ch = text.charAt(i);
            if (ch != '\\' || i >= length - 1) {
                builder.append(ch);
            } else {
                i++;
                char next_ch = text.charAt(i);
                if (next_ch == 'n' || next_ch == 'N') {
                    builder.append("\r\n");
                } else {
                    builder.append(next_ch);
                }
            }
            i++;
        }
        return builder.toString();
    }

    /* access modifiers changed from: protected */
    public String maybeUnescape(char ch) {
        if (ch == 'n' || ch == 'N') {
            return "\r\n";
        }
        return String.valueOf(ch);
    }
}
