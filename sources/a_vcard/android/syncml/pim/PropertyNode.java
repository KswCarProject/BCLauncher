package a_vcard.android.syncml.pim;

import a_vcard.android.content.ContentValues;
import com.touchus.benchilauncher.bean.GuideInfoExtraKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;

public class PropertyNode {
    public ContentValues paramMap;
    public Set<String> paramMap_TYPE;
    public Set<String> propGroupSet;
    public String propName;
    public String propValue;
    public byte[] propValue_bytes;
    public List<String> propValue_vector;

    public PropertyNode() {
        this.propName = "";
        this.propValue = "";
        this.propValue_vector = new ArrayList();
        this.paramMap = new ContentValues();
        this.paramMap_TYPE = new HashSet();
        this.propGroupSet = new HashSet();
    }

    public PropertyNode(String propName2, String propValue2, List<String> propValue_vector2, byte[] propValue_bytes2, ContentValues paramMap2, Set<String> paramMap_TYPE2, Set<String> propGroupSet2) {
        if (propName2 != null) {
            this.propName = propName2;
        } else {
            this.propName = "";
        }
        if (propValue2 != null) {
            this.propValue = propValue2;
        } else {
            this.propValue = "";
        }
        if (propValue_vector2 != null) {
            this.propValue_vector = propValue_vector2;
        } else {
            this.propValue_vector = new ArrayList();
        }
        this.propValue_bytes = propValue_bytes2;
        if (paramMap2 != null) {
            this.paramMap = paramMap2;
        } else {
            this.paramMap = new ContentValues();
        }
        if (paramMap_TYPE2 != null) {
            this.paramMap_TYPE = paramMap_TYPE2;
        } else {
            this.paramMap_TYPE = new HashSet();
        }
        if (propGroupSet2 != null) {
            this.propGroupSet = propGroupSet2;
        } else {
            this.propGroupSet = new HashSet();
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PropertyNode)) {
            return false;
        }
        PropertyNode node = (PropertyNode) obj;
        if (this.propName == null || !this.propName.equals(node.propName) || !this.paramMap.equals(node.paramMap) || !this.paramMap_TYPE.equals(node.paramMap_TYPE) || !this.propGroupSet.equals(node.propGroupSet)) {
            return false;
        }
        if (this.propValue_bytes != null && Arrays.equals(this.propValue_bytes, node.propValue_bytes)) {
            return true;
        }
        if (!this.propValue.equals(node.propValue)) {
            return false;
        }
        if (this.propValue_vector.equals(node.propValue_vector) || this.propValue_vector.size() == 1 || node.propValue_vector.size() == 1) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("propName: ");
        builder.append(this.propName);
        builder.append(", paramMap: ");
        builder.append(this.paramMap.toString());
        builder.append(", propmMap_TYPE: ");
        builder.append(this.paramMap_TYPE.toString());
        builder.append(", propGroupSet: ");
        builder.append(this.propGroupSet.toString());
        if (this.propValue_vector != null && this.propValue_vector.size() > 1) {
            builder.append(", propValue_vector size: ");
            builder.append(this.propValue_vector.size());
        }
        if (this.propValue_bytes != null) {
            builder.append(", propValue_bytes size: ");
            builder.append(this.propValue_bytes.length);
        }
        builder.append(", propValue: ");
        builder.append(this.propValue);
        return builder.toString();
    }

    public String encode() {
        StringBuilder builder = new StringBuilder();
        if (this.propName.length() > 0) {
            builder.append("propName:[");
            builder.append(this.propName);
            builder.append("],");
        }
        int size = this.propGroupSet.size();
        if (size > 0) {
            Set<String> set = this.propGroupSet;
            builder.append("propGroup:[");
            int i = 0;
            for (String group : set) {
                builder.append(group);
                if (i < size - 1) {
                    builder.append(",");
                }
                i++;
            }
            builder.append("],");
        }
        if (this.paramMap.size() > 0 || this.paramMap_TYPE.size() > 0) {
            ContentValues values = this.paramMap;
            builder.append("paramMap:[");
            int size2 = this.paramMap.size();
            int i2 = 0;
            for (Map.Entry<String, Object> entry : values.valueSet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue().toString().replaceAll("\\\\", "\\\\\\\\").replaceAll(",", "\\\\,"));
                if (i2 < size2 - 1) {
                    builder.append(",");
                }
                i2++;
            }
            Set<String> set2 = this.paramMap_TYPE;
            int size3 = this.paramMap_TYPE.size();
            if (i2 > 0 && size3 > 0) {
                builder.append(",");
            }
            int i3 = 0;
            for (String type : set2) {
                builder.append("TYPE=");
                builder.append(type.replaceAll("\\\\", "\\\\\\\\").replaceAll(",", "\\\\,"));
                if (i3 < size3 - 1) {
                    builder.append(",");
                }
                i3++;
            }
            builder.append("],");
        }
        int size4 = this.propValue_vector.size();
        if (size4 > 0) {
            builder.append("propValue:[");
            List<String> list = this.propValue_vector;
            for (int i4 = 0; i4 < size4; i4++) {
                builder.append(list.get(i4).replaceAll("\\\\", "\\\\\\\\").replaceAll(",", "\\\\,"));
                if (i4 < size4 - 1) {
                    builder.append(",");
                }
            }
            builder.append("],");
        }
        return builder.toString();
    }

    public static PropertyNode decode(String encodedString) {
        PropertyNode propertyNode = new PropertyNode();
        String trimed = encodedString.trim();
        if (trimed.length() != 0) {
            for (String elem : trimed.split("],")) {
                int index = elem.indexOf(91);
                String name = elem.substring(0, index - 1);
                String[] values = Pattern.compile("(?<!\\\\),").split(elem.substring(index + 1), -1);
                if (name.equals("propName")) {
                    propertyNode.propName = values[0];
                } else if (name.equals("propGroupSet")) {
                    for (String value : values) {
                        propertyNode.propGroupSet.add(value);
                    }
                } else if (name.equals("paramMap")) {
                    ContentValues paramMap2 = propertyNode.paramMap;
                    Set<String> paramMap_TYPE2 = propertyNode.paramMap_TYPE;
                    for (String value2 : values) {
                        String[] tmp = value2.split("=", 2);
                        String mapKey = tmp[0];
                        String mapValue = tmp[1].replaceAll("\\\\,", ",").replaceAll("\\\\\\\\", "\\\\");
                        if (mapKey.equalsIgnoreCase(GuideInfoExtraKey.TYPE)) {
                            paramMap_TYPE2.add(mapValue);
                        } else {
                            paramMap2.put(mapKey, mapValue);
                        }
                    }
                } else if (name.equals("propValue")) {
                    StringBuilder builder = new StringBuilder();
                    List<String> list = propertyNode.propValue_vector;
                    int length = values.length;
                    for (int i = 0; i < length; i++) {
                        String normValue = values[i].replaceAll("\\\\,", ",").replaceAll("\\\\\\\\", "\\\\");
                        list.add(normValue);
                        builder.append(normValue);
                        if (i < length - 1) {
                            builder.append(";");
                        }
                    }
                    propertyNode.propValue = builder.toString();
                }
            }
            String encoding = propertyNode.paramMap.getAsString("ENCODING");
            if (encoding != null && (encoding.equalsIgnoreCase("BASE64") || encoding.equalsIgnoreCase("B"))) {
                propertyNode.propValue_bytes = Base64.decodeBase64(propertyNode.propValue_vector.get(0).getBytes());
            }
        }
        return propertyNode;
    }
}
