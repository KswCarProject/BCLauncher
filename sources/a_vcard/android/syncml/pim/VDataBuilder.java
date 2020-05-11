package a_vcard.android.syncml.pim;

import a_vcard.android.content.ContentValues;
import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import a_vcard.android.util.Log;
import com.touchus.benchilauncher.bean.GuideInfoExtraKey;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.QuotedPrintableCodec;

public class VDataBuilder implements VBuilder {
    public static String DEFAULT_CHARSET = VCardParser_V21.DEFAULT_CHARSET;
    private static String LOG_TAG = "VDATABuilder";
    private String mCurrentParamType;
    private PropertyNode mCurrentPropNode;
    private VNode mCurrentVNode;
    private int mNodeListPos;
    private String mSourceCharset;
    private boolean mStrictLineBreakParsing;
    private String mTargetCharset;
    public List<VNode> vNodeList;

    public VDataBuilder() {
        this(DEFAULT_CHARSET, DEFAULT_CHARSET, false);
    }

    public VDataBuilder(String charset, boolean strictLineBreakParsing) {
        this((String) null, charset, strictLineBreakParsing);
    }

    public VDataBuilder(String sourceCharset, String targetCharset, boolean strictLineBreakParsing) {
        this.vNodeList = new ArrayList();
        this.mNodeListPos = 0;
        if (sourceCharset != null) {
            this.mSourceCharset = sourceCharset;
        } else {
            this.mSourceCharset = DEFAULT_CHARSET;
        }
        if (targetCharset != null) {
            this.mTargetCharset = targetCharset;
        } else {
            this.mTargetCharset = DEFAULT_CHARSET;
        }
        this.mStrictLineBreakParsing = strictLineBreakParsing;
    }

    public void start() {
    }

    public void end() {
    }

    public void startRecord(String type) {
        VNode vnode = new VNode();
        vnode.parseStatus = 1;
        vnode.VName = type;
        this.vNodeList.add(vnode);
        this.mNodeListPos = this.vNodeList.size() - 1;
        this.mCurrentVNode = this.vNodeList.get(this.mNodeListPos);
    }

    public void endRecord() {
        this.vNodeList.get(this.mNodeListPos).parseStatus = 0;
        while (this.mNodeListPos > 0) {
            this.mNodeListPos--;
            if (this.vNodeList.get(this.mNodeListPos).parseStatus == 1) {
                break;
            }
        }
        this.mCurrentVNode = this.vNodeList.get(this.mNodeListPos);
    }

    public void startProperty() {
        this.mCurrentPropNode = new PropertyNode();
    }

    public void endProperty() {
        this.mCurrentVNode.propList.add(this.mCurrentPropNode);
    }

    public void propertyName(String name) {
        this.mCurrentPropNode.propName = name;
    }

    public void propertyGroup(String group) {
        this.mCurrentPropNode.propGroupSet.add(group);
    }

    public void propertyParamType(String type) {
        this.mCurrentParamType = type;
    }

    public void propertyParamValue(String value) {
        if (this.mCurrentParamType == null || this.mCurrentParamType.equalsIgnoreCase(GuideInfoExtraKey.TYPE)) {
            this.mCurrentPropNode.paramMap_TYPE.add(value);
        } else {
            this.mCurrentPropNode.paramMap.put(this.mCurrentParamType, value);
        }
        this.mCurrentParamType = null;
    }

    private String encodeString(String originalString, String targetCharset) {
        if (this.mSourceCharset.equalsIgnoreCase(targetCharset)) {
            return originalString;
        }
        ByteBuffer byteBuffer = Charset.forName(this.mSourceCharset).encode(originalString);
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        try {
            return new String(bytes, targetCharset);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "Failed to encode: charset=" + targetCharset);
            return new String(bytes);
        }
    }

    private String handleOneValue(String value, String targetCharset, String encoding) {
        String[] lines;
        byte[] bytes;
        if (encoding != null) {
            if (encoding.equals("BASE64") || encoding.equals("B")) {
                this.mCurrentPropNode.propValue_bytes = Base64.decodeBase64(value.getBytes());
                return value;
            } else if (encoding.equals("QUOTED-PRINTABLE")) {
                String quotedPrintable = value.replaceAll("= ", " ").replaceAll("=\t", "\t");
                if (this.mStrictLineBreakParsing) {
                    lines = quotedPrintable.split("\r\n");
                } else {
                    StringBuilder builder = new StringBuilder();
                    int length = quotedPrintable.length();
                    ArrayList<String> list = new ArrayList<>();
                    int i = 0;
                    while (i < length) {
                        char ch = quotedPrintable.charAt(i);
                        if (ch == 10) {
                            list.add(builder.toString());
                            builder = new StringBuilder();
                        } else if (ch == 13) {
                            list.add(builder.toString());
                            builder = new StringBuilder();
                            if (i < length - 1 && quotedPrintable.charAt(i + 1) == 10) {
                                i++;
                            }
                        } else {
                            builder.append(ch);
                        }
                        i++;
                    }
                    String finalLine = builder.toString();
                    if (finalLine.length() > 0) {
                        list.add(finalLine);
                    }
                    lines = (String[]) list.toArray(new String[0]);
                }
                StringBuilder builder2 = new StringBuilder();
                for (String line : lines) {
                    if (line.endsWith("=")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    builder2.append(line);
                }
                try {
                    bytes = builder2.toString().getBytes(this.mSourceCharset);
                } catch (UnsupportedEncodingException e) {
                    Log.e(LOG_TAG, "Failed to encode: charset=" + this.mSourceCharset);
                    bytes = builder2.toString().getBytes();
                }
                try {
                    byte[] bytes2 = QuotedPrintableCodec.decodeQuotedPrintable(bytes);
                    try {
                        return new String(bytes2, targetCharset);
                    } catch (UnsupportedEncodingException e2) {
                        Log.e(LOG_TAG, "Failed to encode: charset=" + targetCharset);
                        return new String(bytes2);
                    }
                } catch (DecoderException e3) {
                    Log.e(LOG_TAG, "Failed to decode quoted-printable: " + e3);
                    return "";
                }
            }
        }
        return encodeString(value, targetCharset);
    }

    public void propertyValues(List<String> values) {
        if (values == null || values.size() == 0) {
            this.mCurrentPropNode.propValue_bytes = null;
            this.mCurrentPropNode.propValue_vector.clear();
            this.mCurrentPropNode.propValue_vector.add("");
            this.mCurrentPropNode.propValue = "";
            return;
        }
        ContentValues paramMap = this.mCurrentPropNode.paramMap;
        String targetCharset = DEFAULT_CHARSET;
        String encoding = paramMap.getAsString("ENCODING");
        if (targetCharset == null || targetCharset.length() == 0) {
            targetCharset = this.mTargetCharset;
        }
        for (String value : values) {
            this.mCurrentPropNode.propValue_vector.add(handleOneValue(value, targetCharset, encoding));
        }
        this.mCurrentPropNode.propValue = listToString(this.mCurrentPropNode.propValue_vector);
    }

    private String listToString(List<String> list) {
        int size = list.size();
        if (size > 1) {
            StringBuilder typeListB = new StringBuilder();
            for (String type : list) {
                typeListB.append(type).append(";");
            }
            int len = typeListB.length();
            if (len <= 0 || typeListB.charAt(len - 1) != ';') {
                return typeListB.toString();
            }
            return typeListB.substring(0, len - 1);
        } else if (size == 1) {
            return list.get(0);
        } else {
            return "";
        }
    }

    public String getResult() {
        return null;
    }
}
