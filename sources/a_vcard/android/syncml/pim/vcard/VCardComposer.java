package a_vcard.android.syncml.pim.vcard;

import a_vcard.android.syncml.pim.vcard.ContactStruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

public class VCardComposer {
    private static final String TAG = "VCardComposer";
    public static final int VERSION_VCARD21_INT = 1;
    public static final int VERSION_VCARD30_INT = 2;
    private static final HashMap<Integer, String> emailTypeMap = new HashMap<>();
    private static final HashSet<String> emailTypes = new HashSet<>(Arrays.asList(new String[]{"CELL", "AOL", "APPLELINK", "ATTMAIL", "CIS", "EWORLD", "INTERNET", "IBMMAIL", "MCIMAIL", "POWERSHARE", "PRODIGY", "TLX", "X400"}));
    private static final HashMap<Integer, String> phoneTypeMap = new HashMap<>();
    private static final HashSet<String> phoneTypes = new HashSet<>(Arrays.asList(new String[]{"PREF", "WORK", "HOME", "VOICE", "FAX", "MSG", "CELL", "PAGER", "BBS", "MODEM", "CAR", "ISDN", "VIDEO"}));
    private String mNewline;
    private StringBuilder mResult;

    static {
        phoneTypeMap.put(1, "HOME");
        phoneTypeMap.put(2, "CELL");
        phoneTypeMap.put(3, "WORK");
        phoneTypeMap.put(4, "WORK;FAX");
        phoneTypeMap.put(5, "HOME;FAX");
        phoneTypeMap.put(6, "PAGER");
        phoneTypeMap.put(7, "X-OTHER");
        emailTypeMap.put(1, "HOME");
        emailTypeMap.put(2, "WORK");
    }

    public String createVCard(ContactStruct struct, int vcardversion) throws VCardException {
        this.mResult = new StringBuilder();
        if (struct.name == null || struct.name.trim().equals("")) {
            throw new VCardException(" struct.name MUST have value.");
        }
        if (vcardversion == 1) {
            this.mNewline = "\r\n";
        } else if (vcardversion == 2) {
            this.mNewline = "\n";
        } else {
            throw new VCardException(" version not match VERSION_VCARD21 or VERSION_VCARD30.");
        }
        this.mResult.append("BEGIN:VCARD").append(this.mNewline);
        if (vcardversion == 1) {
            this.mResult.append("VERSION:2.1").append(this.mNewline);
        } else {
            this.mResult.append("VERSION:3.0").append(this.mNewline);
        }
        if (!isNull(struct.name)) {
            appendNameStr(struct.name);
        }
        if (!isNull(struct.company)) {
            this.mResult.append("ORG:").append(struct.company).append(this.mNewline);
        }
        if (struct.notes.size() > 0 && !isNull(struct.notes.get(0))) {
            this.mResult.append("NOTE:").append(foldingString(struct.notes.get(0), vcardversion)).append(this.mNewline);
        }
        if (!isNull(struct.title)) {
            this.mResult.append("TITLE:").append(foldingString(struct.title, vcardversion)).append(this.mNewline);
        }
        if (struct.photoBytes != null) {
            appendPhotoStr(struct.photoBytes, struct.photoType, vcardversion);
        }
        if (struct.phoneList != null) {
            appendPhoneStr(struct.phoneList, vcardversion);
        }
        if (struct.contactmethodList != null) {
            appendContactMethodStr(struct.contactmethodList, vcardversion);
        }
        this.mResult.append("END:VCARD").append(this.mNewline);
        return this.mResult.toString();
    }

    private String foldingString(String str, int version) {
        if (str.endsWith("\r\n")) {
            str = str.substring(0, str.length() - 2);
        } else if (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        String str2 = str.replaceAll("\r\n", "\n");
        if (version == 1) {
            return str2.replaceAll("\n", "\r\n ");
        }
        if (version == 2) {
            return str2.replaceAll("\n", "\n ");
        }
        return null;
    }

    private void appendPhotoStr(byte[] bytes, String type, int version) throws VCardException {
        String type2;
        String encodingStr;
        try {
            String value = foldingString(new String(Base64.encodeBase64(bytes, true)), version);
            if (isNull(type) || type.toUpperCase().indexOf("JPEG") >= 0) {
                type2 = "JPEG";
            } else if (type.toUpperCase().indexOf("GIF") >= 0) {
                type2 = "GIF";
            } else if (type.toUpperCase().indexOf("BMP") >= 0) {
                type2 = "BMP";
            } else {
                int indexOfSlash = type.indexOf("/");
                if (indexOfSlash >= 0) {
                    type2 = type.substring(indexOfSlash + 1).toUpperCase();
                } else {
                    type2 = type.toUpperCase();
                }
            }
            this.mResult.append("LOGO;TYPE=").append(type2);
            if (version == 1) {
                encodingStr = ";ENCODING=BASE64:";
                value = value + this.mNewline;
            } else if (version == 2) {
                encodingStr = ";ENCODING=b:";
            } else {
                return;
            }
            this.mResult.append(encodingStr).append(value).append(this.mNewline);
        } catch (Exception e) {
            throw new VCardException(e.getMessage());
        }
    }

    private boolean isNull(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        }
        return false;
    }

    private void appendNameStr(String name) {
        this.mResult.append("FN:").append(name).append(this.mNewline);
        this.mResult.append("N:").append(name).append(this.mNewline);
    }

    private void appendPhoneStr(List<ContactStruct.PhoneData> phoneList, int version) {
        HashMap<String, String> numMap = new HashMap<>();
        String joinMark = version == 1 ? ";" : ",";
        for (ContactStruct.PhoneData phone : phoneList) {
            if (!isNull(phone.data)) {
                String type = getPhoneTypeStr(phone);
                if (version == 2 && type.indexOf(";") != -1) {
                    type = type.replace(";", ",");
                }
                if (numMap.containsKey(phone.data)) {
                    type = numMap.get(phone.data) + joinMark + type;
                }
                numMap.put(phone.data, type);
            }
        }
        for (Map.Entry<String, String> num : numMap.entrySet()) {
            if (version == 1) {
                this.mResult.append("TEL;");
            } else {
                this.mResult.append("TEL;TYPE=");
            }
            this.mResult.append(num.getValue()).append(":").append(num.getKey()).append(this.mNewline);
        }
    }

    private String getPhoneTypeStr(ContactStruct.PhoneData phone) {
        int phoneType = phone.type;
        if (phoneTypeMap.containsKey(Integer.valueOf(phoneType))) {
            return phoneTypeMap.get(Integer.valueOf(phoneType));
        }
        if (phoneType != 0) {
            return "VOICE";
        }
        String label = phone.label.toUpperCase();
        if (phoneTypes.contains(label) || label.startsWith("X-")) {
            return label;
        }
        return "X-CUSTOM-" + label;
    }

    private void appendContactMethodStr(List<ContactStruct.ContactMethod> contactMList, int version) {
        HashMap<String, String> emailMap = new HashMap<>();
        String joinMark = version == 1 ? ";" : ",";
        for (ContactStruct.ContactMethod contactMethod : contactMList) {
            switch (contactMethod.kind) {
                case 1:
                    String mailType = "INTERNET";
                    if (isNull(contactMethod.data)) {
                        break;
                    } else {
                        int methodType = new Integer(contactMethod.type).intValue();
                        if (emailTypeMap.containsKey(Integer.valueOf(methodType))) {
                            mailType = emailTypeMap.get(Integer.valueOf(methodType));
                        } else if (!isNull(contactMethod.label) && emailTypes.contains(contactMethod.label.toUpperCase())) {
                            mailType = contactMethod.label.toUpperCase();
                        }
                        if (emailMap.containsKey(contactMethod.data)) {
                            mailType = emailMap.get(contactMethod.data) + joinMark + mailType;
                        }
                        emailMap.put(contactMethod.data, mailType);
                        break;
                    }
                case 2:
                    if (isNull(contactMethod.data)) {
                        break;
                    } else {
                        this.mResult.append("ADR;TYPE=POSTAL:").append(foldingString(contactMethod.data, version)).append(this.mNewline);
                        break;
                    }
            }
        }
        for (Map.Entry<String, String> email : emailMap.entrySet()) {
            if (version == 1) {
                this.mResult.append("EMAIL;");
            } else {
                this.mResult.append("EMAIL;TYPE=");
            }
            this.mResult.append(email.getValue()).append(":").append(email.getKey()).append(this.mNewline);
        }
    }
}
