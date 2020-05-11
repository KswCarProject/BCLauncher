package a_vcard.android.syncml.pim.vcard;

import a_vcard.android.syncml.pim.VBuilder;
import a_vcard.android.util.Log;
import com.touchus.benchilauncher.bean.GuideInfoExtraKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class VCardParser_V21 {
    public static final String DEFAULT_CHARSET = "UTF-8";
    private static final String LOG_TAG = "VCardParser_V21";
    private static final int STATE_GROUP_OR_PROPNAME = 0;
    private static final int STATE_PARAMS = 1;
    private static final int STATE_PARAMS_IN_DQUOTE = 2;
    private static final HashSet<String> sAvailableEncodingV21 = new HashSet<>(Arrays.asList(new String[]{"7BIT", "8BIT", "QUOTED-PRINTABLE", "BASE64", "B"}));
    private static final HashSet<String> sAvailablePropertyNameV21 = new HashSet<>(Arrays.asList(new String[]{"BEGIN", "LOGO", "PHOTO", "LABEL", "FN", "TITLE", "SOUND", "VERSION", "TEL", "EMAIL", "TZ", "GEO", "NOTE", "URL", "BDAY", "ROLE", "REV", "UID", "KEY", "MAILER"}));
    private static final HashSet<String> sKnownTypeSet = new HashSet<>(Arrays.asList(new String[]{"DOM", "INTL", "POSTAL", "PARCEL", "HOME", "WORK", "PREF", "VOICE", "FAX", "MSG", "CELL", "PAGER", "BBS", "MODEM", "CAR", "ISDN", "VIDEO", "AOL", "APPLELINK", "ATTMAIL", "CIS", "EWORLD", "INTERNET", "IBMMAIL", "MCIMAIL", "POWERSHARE", "PRODIGY", "TLX", "X400", "GIF", "CGM", "WMF", "BMP", "MET", "PMB", "DIB", "PICT", "TIFF", "PDF", "PS", "JPEG", "QTIME", "MPEG", "MPEG2", "AVI", "WAVE", "AIFF", "PCM", "X509", "PGP"}));
    private static final HashSet<String> sKnownValueSet = new HashSet<>(Arrays.asList(new String[]{"INLINE", "URL", "CONTENT-ID", "CID"}));
    protected VBuilder mBuilder = null;
    private boolean mCanceled;
    protected String mEncoding = null;
    private int mNestCount;
    private String mPreviousLine;
    protected BufferedReader mReader;
    private long mTimeEndProperty;
    private long mTimeEndRecord;
    private long mTimeHandlePropertyValue1;
    private long mTimeHandlePropertyValue2;
    private long mTimeHandlePropertyValue3;
    private long mTimeParseItem1;
    private long mTimeParseItem2;
    private long mTimeParseItem3;
    private long mTimeParseItems;
    private long mTimeStartProperty;
    private long mTimeStartRecord;
    private long mTimeTotal;
    protected HashSet<String> mWarningValueMap = new HashSet<>();
    protected final String sDefaultEncoding = "8BIT";

    public VCardParser_V21() {
    }

    public VCardParser_V21(VCardSourceDetector detector) {
        if (detector != null && detector.getType() == 3) {
            this.mNestCount = 1;
        }
    }

    /* access modifiers changed from: protected */
    public void parseVCardFile() throws IOException, VCardException {
        boolean firstReading = true;
        while (!this.mCanceled && parseOneVCard(firstReading)) {
            firstReading = false;
        }
        if (this.mNestCount > 0) {
            boolean useCache = true;
            for (int i = 0; i < this.mNestCount; i++) {
                readEndVCard(useCache, true);
                useCache = false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getVersion() {
        return "2.1";
    }

    /* access modifiers changed from: protected */
    public boolean isValidPropertyName(String propertyName) {
        if (sAvailablePropertyNameV21.contains(propertyName.toUpperCase()) || propertyName.startsWith("X-") || this.mWarningValueMap.contains(propertyName)) {
            return true;
        }
        this.mWarningValueMap.add(propertyName);
        Log.w(LOG_TAG, "Property name unsupported by vCard 2.1: " + propertyName);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isValidEncoding(String encoding) {
        return sAvailableEncodingV21.contains(encoding.toUpperCase());
    }

    /* access modifiers changed from: protected */
    public String getLine() throws IOException {
        return this.mReader.readLine();
    }

    /* access modifiers changed from: protected */
    public String getNonEmptyLine() throws IOException, VCardException {
        String line;
        do {
            line = getLine();
            if (line == null) {
                throw new VCardException("Reached end of buffer.");
            }
        } while (line.trim().length() <= 0);
        return line;
    }

    private boolean parseOneVCard(boolean firstReading) throws IOException, VCardException {
        boolean allowGarbage = false;
        if (firstReading && this.mNestCount > 0) {
            for (int i = 0; i < this.mNestCount; i++) {
                if (!readBeginVCard(allowGarbage)) {
                    return false;
                }
                allowGarbage = true;
            }
        }
        if (!readBeginVCard(allowGarbage)) {
            return false;
        }
        if (this.mBuilder != null) {
            long start = System.currentTimeMillis();
            this.mBuilder.startRecord("VCARD");
            this.mTimeStartRecord += System.currentTimeMillis() - start;
        }
        long start2 = System.currentTimeMillis();
        parseItems();
        this.mTimeParseItems += System.currentTimeMillis() - start2;
        readEndVCard(true, false);
        if (this.mBuilder != null) {
            long start3 = System.currentTimeMillis();
            this.mBuilder.endRecord();
            this.mTimeEndRecord += System.currentTimeMillis() - start3;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean readBeginVCard(boolean allowGarbage) throws IOException, VCardException {
        while (true) {
            String line = getLine();
            if (line == null) {
                return false;
            }
            if (line.trim().length() > 0) {
                String[] strArray = line.split(":", 2);
                if (strArray.length == 2 && strArray[0].trim().equalsIgnoreCase("BEGIN") && strArray[1].trim().equalsIgnoreCase("VCARD")) {
                    return true;
                }
                if (!allowGarbage) {
                    if (this.mNestCount > 0) {
                        this.mPreviousLine = line;
                        return false;
                    }
                    throw new VCardException("Expected String \"BEGIN:VCARD\" did not come (Instead, \"" + line + "\" came)");
                } else if (!allowGarbage) {
                    throw new VCardException("Reached where must not be reached.");
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void readEndVCard(boolean useCache, boolean allowGarbage) throws IOException, VCardException {
        String line;
        do {
            if (useCache) {
                line = this.mPreviousLine;
            } else {
                do {
                    line = getLine();
                    if (line == null) {
                        throw new VCardException("Expected END:VCARD was not found.");
                    }
                } while (line.trim().length() <= 0);
            }
            String[] strArray = line.split(":", 2);
            if (strArray.length == 2 && strArray[0].trim().equalsIgnoreCase("END") && strArray[1].trim().equalsIgnoreCase("VCARD")) {
                return;
            }
            if (!allowGarbage) {
                throw new VCardException("END:VCARD != \"" + this.mPreviousLine + "\"");
            }
            useCache = false;
        } while (allowGarbage);
    }

    /* access modifiers changed from: protected */
    public void parseItems() throws IOException, VCardException {
        if (this.mBuilder != null) {
            long start = System.currentTimeMillis();
            this.mBuilder.startProperty();
            this.mTimeStartProperty += System.currentTimeMillis() - start;
        }
        boolean ended = parseItem();
        if (this.mBuilder != null && !ended) {
            long start2 = System.currentTimeMillis();
            this.mBuilder.endProperty();
            this.mTimeEndProperty += System.currentTimeMillis() - start2;
        }
        while (!ended) {
            if (this.mBuilder != null) {
                long start3 = System.currentTimeMillis();
                this.mBuilder.startProperty();
                this.mTimeStartProperty += System.currentTimeMillis() - start3;
            }
            ended = parseItem();
            if (this.mBuilder != null && !ended) {
                long start4 = System.currentTimeMillis();
                this.mBuilder.endProperty();
                this.mTimeEndProperty += System.currentTimeMillis() - start4;
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean parseItem() throws IOException, VCardException {
        this.mEncoding = "8BIT";
        String line = getNonEmptyLine();
        long start = System.currentTimeMillis();
        String[] propertyNameAndValue = separateLineAndHandleGroup(line);
        if (propertyNameAndValue == null) {
            return true;
        }
        if (propertyNameAndValue.length != 2) {
            throw new VCardException("Invalid line \"" + line + "\"");
        }
        String propertyName = propertyNameAndValue[0].toUpperCase();
        String propertyValue = propertyNameAndValue[1];
        this.mTimeParseItem1 += System.currentTimeMillis() - start;
        if (propertyName.equals("ADR") || propertyName.equals("ORG") || propertyName.equals("N")) {
            long start2 = System.currentTimeMillis();
            handleMultiplePropertyValue(propertyName, propertyValue);
            this.mTimeParseItem3 += System.currentTimeMillis() - start2;
            return false;
        } else if (propertyName.equals("AGENT")) {
            handleAgent(propertyValue);
            return false;
        } else if (!isValidPropertyName(propertyName)) {
            throw new VCardException("Unknown property name: \"" + propertyName + "\"");
        } else if (propertyName.equals("BEGIN")) {
            if (propertyValue.equals("VCARD")) {
                throw new VCardNestedException("This vCard has nested vCard data in it.");
            }
            throw new VCardException("Unknown BEGIN type: " + propertyValue);
        } else if (!propertyName.equals("VERSION") || propertyValue.equals(getVersion())) {
            long start3 = System.currentTimeMillis();
            handlePropertyValue(propertyName, propertyValue);
            this.mTimeParseItem2 += System.currentTimeMillis() - start3;
            return false;
        } else {
            throw new VCardVersionException("Incompatible version: " + propertyValue + " != " + getVersion());
        }
    }

    /* access modifiers changed from: protected */
    public String[] separateLineAndHandleGroup(String line) throws VCardException {
        int length = line.length();
        int state = 0;
        int nameIndex = 0;
        String[] propertyNameAndValue = new String[2];
        for (int i = 0; i < length; i++) {
            char ch = line.charAt(i);
            switch (state) {
                case 0:
                    if (ch == ':') {
                        String propertyName = line.substring(nameIndex, i);
                        if (propertyName.equalsIgnoreCase("END")) {
                            this.mPreviousLine = line;
                            return null;
                        }
                        if (this.mBuilder != null) {
                            this.mBuilder.propertyName(propertyName);
                        }
                        propertyNameAndValue[0] = propertyName;
                        if (i < length - 1) {
                            propertyNameAndValue[1] = line.substring(i + 1);
                            return propertyNameAndValue;
                        }
                        propertyNameAndValue[1] = "";
                        return propertyNameAndValue;
                    } else if (ch == '.') {
                        String groupName = line.substring(nameIndex, i);
                        if (this.mBuilder != null) {
                            this.mBuilder.propertyGroup(groupName);
                        }
                        nameIndex = i + 1;
                        break;
                    } else if (ch == ';') {
                        String propertyName2 = line.substring(nameIndex, i);
                        if (!propertyName2.equalsIgnoreCase("END")) {
                            if (this.mBuilder != null) {
                                this.mBuilder.propertyName(propertyName2);
                            }
                            propertyNameAndValue[0] = propertyName2;
                            nameIndex = i + 1;
                            state = 1;
                            break;
                        } else {
                            this.mPreviousLine = line;
                            return null;
                        }
                    } else {
                        continue;
                    }
                case 1:
                    if (ch != '\"') {
                        if (ch != ';') {
                            if (ch != ':') {
                                break;
                            } else {
                                handleParams(line.substring(nameIndex, i));
                                if (i < length - 1) {
                                    propertyNameAndValue[1] = line.substring(i + 1);
                                    return propertyNameAndValue;
                                }
                                propertyNameAndValue[1] = "";
                                return propertyNameAndValue;
                            }
                        } else {
                            handleParams(line.substring(nameIndex, i));
                            nameIndex = i + 1;
                            break;
                        }
                    } else {
                        state = 2;
                        break;
                    }
                case 2:
                    if (ch != '\"') {
                        break;
                    } else {
                        state = 1;
                        break;
                    }
            }
        }
        throw new VCardException("Invalid line: \"" + line + "\"");
    }

    /* access modifiers changed from: protected */
    public void handleParams(String params) throws VCardException {
        String[] strArray = params.split("=", 2);
        if (strArray.length == 2) {
            String paramName = strArray[0].trim();
            String paramValue = strArray[1].trim();
            if (paramName.equals(GuideInfoExtraKey.TYPE)) {
                handleType(paramValue);
            } else if (paramName.equals("VALUE")) {
                handleValue(paramValue);
            } else if (paramName.equals("ENCODING")) {
                handleEncoding(paramValue);
            } else if (paramName.equals("CHARSET")) {
                handleCharset(paramValue);
            } else if (paramName.equals("LANGUAGE")) {
                handleLanguage(paramValue);
            } else if (paramName.startsWith("X-")) {
                handleAnyParam(paramName, paramValue);
            } else {
                throw new VCardException("Unknown type \"" + paramName + "\"");
            }
        } else {
            handleType(strArray[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void handleType(String ptypeval) {
        String upperTypeValue = ptypeval;
        if (!sKnownTypeSet.contains(upperTypeValue) && !upperTypeValue.startsWith("X-") && !this.mWarningValueMap.contains(ptypeval)) {
            this.mWarningValueMap.add(ptypeval);
            Log.w(LOG_TAG, "Type unsupported by vCard 2.1: " + ptypeval);
        }
        if (this.mBuilder != null) {
            this.mBuilder.propertyParamType(GuideInfoExtraKey.TYPE);
            this.mBuilder.propertyParamValue(upperTypeValue);
        }
    }

    /* access modifiers changed from: protected */
    public void handleValue(String pvalueval) throws VCardException {
        if (!sKnownValueSet.contains(pvalueval.toUpperCase()) && !pvalueval.startsWith("X-")) {
            throw new VCardException("Unknown value \"" + pvalueval + "\"");
        } else if (this.mBuilder != null) {
            this.mBuilder.propertyParamType("VALUE");
            this.mBuilder.propertyParamValue(pvalueval);
        }
    }

    /* access modifiers changed from: protected */
    public void handleEncoding(String pencodingval) throws VCardException {
        if (isValidEncoding(pencodingval) || pencodingval.startsWith("X-")) {
            if (this.mBuilder != null) {
                this.mBuilder.propertyParamType("ENCODING");
                this.mBuilder.propertyParamValue(pencodingval);
            }
            this.mEncoding = pencodingval;
            return;
        }
        throw new VCardException("Unknown encoding \"" + pencodingval + "\"");
    }

    /* access modifiers changed from: protected */
    public void handleCharset(String charsetval) {
        if (this.mBuilder != null) {
            this.mBuilder.propertyParamType("CHARSET");
            this.mBuilder.propertyParamValue(charsetval);
        }
    }

    /* access modifiers changed from: protected */
    public void handleLanguage(String langval) throws VCardException {
        String[] strArray = langval.split("-");
        if (strArray.length > 2) {
            throw new VCardException("Invalid Language: \"" + langval + "\"");
        }
        String tmp = strArray[0];
        int length = tmp.length();
        for (int i = 0; i < length; i++) {
            if (!isLetter(tmp.charAt(i))) {
                throw new VCardException("Invalid Language: \"" + langval + "\"");
            }
        }
        if (strArray.length > 1) {
            String tmp2 = strArray[1];
            int length2 = tmp2.length();
            for (int i2 = 0; i2 < length2; i2++) {
                if (!isLetter(tmp2.charAt(i2))) {
                    throw new VCardException("Invalid Language: \"" + langval + "\"");
                }
            }
        }
        if (this.mBuilder != null) {
            this.mBuilder.propertyParamType("LANGUAGE");
            this.mBuilder.propertyParamValue(langval);
        }
    }

    /* access modifiers changed from: protected */
    public void handleAnyParam(String paramName, String paramValue) {
        if (this.mBuilder != null) {
            this.mBuilder.propertyParamType(paramName);
            this.mBuilder.propertyParamValue(paramValue);
        }
    }

    /* access modifiers changed from: protected */
    public void handlePropertyValue(String propertyName, String propertyValue) throws IOException, VCardException {
        if (this.mEncoding.equalsIgnoreCase("QUOTED-PRINTABLE")) {
            long start = System.currentTimeMillis();
            String result = getQuotedPrintable(propertyValue);
            if (this.mBuilder != null) {
                ArrayList<String> v = new ArrayList<>();
                v.add(result);
                this.mBuilder.propertyValues(v);
            }
            this.mTimeHandlePropertyValue2 += System.currentTimeMillis() - start;
        } else if (this.mEncoding.equalsIgnoreCase("BASE64") || this.mEncoding.equalsIgnoreCase("B")) {
            long start2 = System.currentTimeMillis();
            try {
                String result2 = getBase64(propertyValue);
                if (this.mBuilder != null) {
                    ArrayList<String> v2 = new ArrayList<>();
                    v2.add(result2);
                    this.mBuilder.propertyValues(v2);
                }
            } catch (OutOfMemoryError e) {
                Log.e(LOG_TAG, "OutOfMemoryError happened during parsing BASE64 data!");
                if (this.mBuilder != null) {
                    this.mBuilder.propertyValues((List<String>) null);
                }
            }
            this.mTimeHandlePropertyValue3 += System.currentTimeMillis() - start2;
        } else {
            if (this.mEncoding != null && !this.mEncoding.equalsIgnoreCase("7BIT") && !this.mEncoding.equalsIgnoreCase("8BIT") && !this.mEncoding.toUpperCase().startsWith("X-")) {
                Log.w(LOG_TAG, "The encoding unsupported by vCard spec: \"" + this.mEncoding + "\".");
            }
            long start3 = System.currentTimeMillis();
            if (this.mBuilder != null) {
                ArrayList<String> v3 = new ArrayList<>();
                v3.add(maybeUnescapeText(propertyValue));
                this.mBuilder.propertyValues(v3);
            }
            this.mTimeHandlePropertyValue1 += System.currentTimeMillis() - start3;
        }
    }

    /* access modifiers changed from: protected */
    public String getQuotedPrintable(String firstString) throws IOException, VCardException {
        if (!firstString.trim().endsWith("=")) {
            return firstString;
        }
        int pos = firstString.length() - 1;
        do {
        } while (firstString.charAt(pos) != '=');
        StringBuilder builder = new StringBuilder();
        builder.append(firstString.substring(0, pos + 1));
        builder.append("\r\n");
        while (true) {
            String line = getLine();
            if (line == null) {
                throw new VCardException("File ended during parsing quoted-printable String");
            } else if (line.trim().endsWith("=")) {
                int pos2 = line.length() - 1;
                do {
                } while (line.charAt(pos2) != '=');
                builder.append(line.substring(0, pos2 + 1));
                builder.append("\r\n");
            } else {
                builder.append(line);
                return builder.toString();
            }
        }
    }

    /* access modifiers changed from: protected */
    public String getBase64(String firstString) throws IOException, VCardException {
        StringBuilder builder = new StringBuilder();
        builder.append(firstString);
        while (true) {
            String line = getLine();
            if (line == null) {
                throw new VCardException("File ended during parsing BASE64 binary");
            } else if (line.length() == 0) {
                return builder.toString();
            } else {
                builder.append(line);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleMultiplePropertyValue(String propertyName, String propertyValue) throws IOException, VCardException {
        if (this.mEncoding.equalsIgnoreCase("QUOTED-PRINTABLE")) {
            propertyValue = getQuotedPrintable(propertyValue);
        }
        if (this.mBuilder != null) {
            StringBuilder builder = new StringBuilder();
            ArrayList<String> list = new ArrayList<>();
            int length = propertyValue.length();
            int i = 0;
            while (i < length) {
                char ch = propertyValue.charAt(i);
                if (ch == '\\' && i < length - 1) {
                    String unescapedString = maybeUnescape(propertyValue.charAt(i + 1));
                    if (unescapedString != null) {
                        builder.append(unescapedString);
                        i++;
                    } else {
                        builder.append(ch);
                    }
                } else if (ch == ';') {
                    list.add(builder.toString());
                    builder = new StringBuilder();
                } else {
                    builder.append(ch);
                }
                i++;
            }
            list.add(builder.toString());
            this.mBuilder.propertyValues(list);
        }
    }

    /* access modifiers changed from: protected */
    public void handleAgent(String propertyValue) throws VCardException {
        throw new VCardException("AGENT Property is not supported.");
    }

    /* access modifiers changed from: protected */
    public String maybeUnescapeText(String text) {
        return text;
    }

    /* access modifiers changed from: protected */
    public String maybeUnescape(char ch) {
        if (ch == '\\' || ch == ';' || ch == ':' || ch == ',') {
            return String.valueOf(ch);
        }
        return null;
    }

    public boolean parse(InputStream is, String charset, VBuilder builder) throws IOException, VCardException {
        this.mReader = new CustomBufferedReader(new InputStreamReader(is, charset));
        this.mBuilder = builder;
        long start = System.currentTimeMillis();
        if (this.mBuilder != null) {
            this.mBuilder.start();
        }
        parseVCardFile();
        if (this.mBuilder != null) {
            this.mBuilder.end();
        }
        this.mTimeTotal += System.currentTimeMillis() - start;
        return true;
    }

    public boolean parse(InputStream is, VBuilder builder) throws IOException, VCardException {
        return parse(is, DEFAULT_CHARSET, builder);
    }

    public void cancel() {
        this.mCanceled = true;
    }

    public void parse(InputStream is, String charset, VBuilder builder, boolean canceled) throws IOException, VCardException {
        this.mCanceled = canceled;
        parse(is, charset, builder);
    }

    public void showDebugInfo() {
        Log.d(LOG_TAG, "total parsing time:  " + this.mTimeTotal + " ms");
        if (this.mReader instanceof CustomBufferedReader) {
            Log.d(LOG_TAG, "total readLine time: " + ((CustomBufferedReader) this.mReader).getTotalmillisecond() + " ms");
        }
        Log.d(LOG_TAG, "mTimeStartRecord: " + this.mTimeStartRecord + " ms");
        Log.d(LOG_TAG, "mTimeEndRecord: " + this.mTimeEndRecord + " ms");
        Log.d(LOG_TAG, "mTimeParseItem1: " + this.mTimeParseItem1 + " ms");
        Log.d(LOG_TAG, "mTimeParseItem2: " + this.mTimeParseItem2 + " ms");
        Log.d(LOG_TAG, "mTimeParseItem3: " + this.mTimeParseItem3 + " ms");
        Log.d(LOG_TAG, "mTimeHandlePropertyValue1: " + this.mTimeHandlePropertyValue1 + " ms");
        Log.d(LOG_TAG, "mTimeHandlePropertyValue2: " + this.mTimeHandlePropertyValue2 + " ms");
        Log.d(LOG_TAG, "mTimeHandlePropertyValue3: " + this.mTimeHandlePropertyValue3 + " ms");
    }

    private boolean isLetter(char ch) {
        if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) {
            return false;
        }
        return true;
    }
}
