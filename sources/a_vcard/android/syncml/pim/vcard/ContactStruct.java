package a_vcard.android.syncml.pim.vcard;

import a_vcard.android.provider.Contacts;
import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.telephony.PhoneNumberUtils;
import a_vcard.android.text.TextUtils;
import a_vcard.android.util.Log;
import com.touchus.benchilauncher.bean.GuideInfoExtraKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ContactStruct {
    private static final String LOG_TAG = "ContactStruct";
    public static final int NAME_ORDER_TYPE_ENGLISH = 0;
    public static final int NAME_ORDER_TYPE_JAPANESE = 1;
    @Deprecated
    public String company;
    public List<ContactMethod> contactmethodList;
    public Map<String, List<String>> extensionMap;
    public String name;
    public List<String> notes = new ArrayList();
    public List<OrganizationData> organizationList;
    public List<PhoneData> phoneList;
    public String phoneticName;
    public byte[] photoBytes;
    public String photoType;
    public String title;

    public static class ContactMethod {
        public String data;
        public boolean isPrimary;
        public int kind;
        public String label;
        public int type;
    }

    public static class OrganizationData {
        public String companyName;
        public boolean isPrimary;
        public String positionName;
        public int type;
    }

    public static class PhoneData {
        public String data;
        public boolean isPrimary;
        public String label;
        public int type;
    }

    public void addPhone(int type, String data, String label, boolean isPrimary) {
        if (this.phoneList == null) {
            this.phoneList = new ArrayList();
        }
        PhoneData phoneData = new PhoneData();
        phoneData.type = type;
        StringBuilder builder = new StringBuilder();
        String trimed = data.trim();
        int length = trimed.length();
        for (int i = 0; i < length; i++) {
            char ch = trimed.charAt(i);
            if (('0' <= ch && ch <= '9') || (i == 0 && ch == '+')) {
                builder.append(ch);
            }
        }
        phoneData.data = PhoneNumberUtils.formatNumber(builder.toString());
        phoneData.label = label;
        phoneData.isPrimary = isPrimary;
        this.phoneList.add(phoneData);
    }

    public void addContactmethod(int kind, int type, String data, String label, boolean isPrimary) {
        if (this.contactmethodList == null) {
            this.contactmethodList = new ArrayList();
        }
        ContactMethod contactMethod = new ContactMethod();
        contactMethod.kind = kind;
        contactMethod.type = type;
        contactMethod.data = data;
        contactMethod.label = label;
        contactMethod.isPrimary = isPrimary;
        this.contactmethodList.add(contactMethod);
    }

    public void addOrganization(int type, String companyName, String positionName, boolean isPrimary) {
        if (this.organizationList == null) {
            this.organizationList = new ArrayList();
        }
        OrganizationData organizationData = new OrganizationData();
        organizationData.type = type;
        organizationData.companyName = companyName;
        organizationData.positionName = positionName;
        organizationData.isPrimary = isPrimary;
        this.organizationList.add(organizationData);
    }

    public void setPosition(String positionValue) {
        if (this.organizationList == null) {
            this.organizationList = new ArrayList();
        }
        int size = this.organizationList.size();
        if (size == 0) {
            addOrganization(2, "", (String) null, false);
            size = 1;
        }
        this.organizationList.get(size - 1).positionName = positionValue;
    }

    public void addExtension(PropertyNode propertyNode) {
        List<String> list;
        if (propertyNode.propValue.length() != 0) {
            String name2 = propertyNode.propName;
            if (this.extensionMap == null) {
                this.extensionMap = new HashMap();
            }
            if (!this.extensionMap.containsKey(name2)) {
                list = new ArrayList<>();
                this.extensionMap.put(name2, list);
            } else {
                list = this.extensionMap.get(name2);
            }
            list.add(propertyNode.encode());
        }
    }

    private static String getNameFromNProperty(List<String> elems, int nameOrderType) {
        String first;
        String second;
        int size = elems.size();
        if (size > 1) {
            StringBuilder builder = new StringBuilder();
            boolean builderIsEmpty = true;
            if (size > 3 && elems.get(3).length() > 0) {
                builder.append(elems.get(3));
                builderIsEmpty = false;
            }
            if (nameOrderType == 1) {
                first = elems.get(0);
                second = elems.get(1);
            } else {
                first = elems.get(1);
                second = elems.get(0);
            }
            if (first.length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(first);
                builderIsEmpty = false;
            }
            if (size > 2 && elems.get(2).length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(elems.get(2));
                builderIsEmpty = false;
            }
            if (second.length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(second);
                builderIsEmpty = false;
            }
            if (size > 4 && elems.get(4).length() > 0) {
                if (!builderIsEmpty) {
                    builder.append(' ');
                }
                builder.append(elems.get(4));
            }
            return builder.toString();
        } else if (size == 1) {
            return elems.get(0);
        } else {
            return "";
        }
    }

    public static ContactStruct constructContactFromVNode(VNode node, int nameOrderType) {
        String first;
        String second;
        String address;
        if (!node.VName.equals("VCARD")) {
            Log.e(LOG_TAG, "Non VCARD data is inserted.");
            return null;
        }
        String fullName = null;
        String nameFromNProperty = null;
        String xPhoneticFirstName = null;
        String xPhoneticMiddleName = null;
        String xPhoneticLastName = null;
        ContactStruct contact = new ContactStruct();
        boolean prefIsSetAddress = false;
        boolean prefIsSetPhone = false;
        boolean prefIsSetEmail = false;
        boolean prefIsSetOrganization = false;
        Iterator<PropertyNode> it = node.propList.iterator();
        while (it.hasNext()) {
            PropertyNode propertyNode = it.next();
            String name2 = propertyNode.propName;
            if (!TextUtils.isEmpty(propertyNode.propValue) && !name2.equals("VERSION")) {
                if (name2.equals("FN")) {
                    fullName = propertyNode.propValue;
                } else if (name2.equals("NAME") && fullName == null) {
                    fullName = propertyNode.propValue;
                } else if (name2.equals("N")) {
                    nameFromNProperty = getNameFromNProperty(propertyNode.propValue_vector, nameOrderType);
                } else if (name2.equals("SORT-STRING")) {
                    contact.phoneticName = propertyNode.propValue;
                } else if (name2.equals("SOUND")) {
                    if (!propertyNode.paramMap_TYPE.contains("X-IRMC-N") || contact.phoneticName != null) {
                        contact.addExtension(propertyNode);
                    } else {
                        StringBuilder builder = new StringBuilder();
                        String value = propertyNode.propValue;
                        int length = value.length();
                        for (int i = 0; i < length; i++) {
                            char ch = value.charAt(i);
                            if (ch != ';') {
                                builder.append(ch);
                            }
                        }
                        contact.phoneticName = builder.toString();
                    }
                } else if (name2.equals("ADR")) {
                    boolean valuesAreAllEmpty = true;
                    Iterator i$ = propertyNode.propValue_vector.iterator();
                    while (true) {
                        if (i$.hasNext()) {
                            if (i$.next().length() > 0) {
                                valuesAreAllEmpty = false;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!valuesAreAllEmpty) {
                        int kind = 2;
                        int type = -1;
                        String label = "";
                        boolean isPrimary = false;
                        for (String typeString : propertyNode.paramMap_TYPE) {
                            if (typeString.equals("PREF") && !prefIsSetAddress) {
                                prefIsSetAddress = true;
                                isPrimary = true;
                            } else if (typeString.equalsIgnoreCase("HOME")) {
                                type = 1;
                                label = "";
                            } else if (typeString.equalsIgnoreCase("WORK") || typeString.equalsIgnoreCase("COMPANY")) {
                                type = 2;
                                label = "";
                            } else if (typeString.equalsIgnoreCase("POSTAL")) {
                                kind = 2;
                            } else if (!typeString.equalsIgnoreCase("PARCEL") && !typeString.equalsIgnoreCase("DOM") && !typeString.equalsIgnoreCase("INTL")) {
                                if (typeString.toUpperCase().startsWith("X-") && type < 0) {
                                    type = 0;
                                    label = typeString.substring(2);
                                } else if (type < 0) {
                                    type = 0;
                                    label = typeString;
                                }
                            }
                        }
                        if (type < 0) {
                            type = 1;
                        }
                        List<String> list = propertyNode.propValue_vector;
                        int size = list.size();
                        if (size > 1) {
                            StringBuilder builder2 = new StringBuilder();
                            boolean builderIsEmpty = true;
                            if (Locale.getDefault().getCountry().equals(Locale.JAPAN.getCountry())) {
                                for (int i2 = size - 1; i2 >= 0; i2--) {
                                    String addressPart = list.get(i2);
                                    if (addressPart.length() > 0) {
                                        if (!builderIsEmpty) {
                                            builder2.append(' ');
                                        }
                                        builder2.append(addressPart);
                                        builderIsEmpty = false;
                                    }
                                }
                            } else {
                                for (int i3 = 0; i3 < size; i3++) {
                                    String addressPart2 = list.get(i3);
                                    if (addressPart2.length() > 0) {
                                        if (!builderIsEmpty) {
                                            builder2.append(' ');
                                        }
                                        builder2.append(addressPart2);
                                        builderIsEmpty = false;
                                    }
                                }
                            }
                            address = builder2.toString().trim();
                        } else {
                            address = propertyNode.propValue;
                        }
                        contact.addContactmethod(kind, type, address, label, isPrimary);
                    }
                } else if (name2.equals("ORG")) {
                    boolean isPrimary2 = false;
                    for (String typeString2 : propertyNode.paramMap_TYPE) {
                        if (typeString2.equals("PREF") && !prefIsSetOrganization) {
                            prefIsSetOrganization = true;
                            isPrimary2 = true;
                        }
                    }
                    List<String> list2 = propertyNode.propValue_vector;
                    int size2 = list2.size();
                    StringBuilder builder3 = new StringBuilder();
                    Iterator<String> iter = list2.iterator();
                    while (iter.hasNext()) {
                        builder3.append(iter.next());
                        if (iter.hasNext()) {
                            builder3.append(' ');
                        }
                    }
                    contact.addOrganization(1, builder3.toString(), "", isPrimary2);
                } else if (name2.equals("TITLE")) {
                    contact.setPosition(propertyNode.propValue);
                } else if (name2.equals("ROLE")) {
                    contact.setPosition(propertyNode.propValue);
                } else if (name2.equals("PHOTO")) {
                    String valueType = propertyNode.paramMap.getAsString("VALUE");
                    if (valueType == null || !valueType.equals("URL")) {
                        contact.photoBytes = propertyNode.propValue_bytes;
                        String type2 = propertyNode.paramMap.getAsString(GuideInfoExtraKey.TYPE);
                        if (type2 != null) {
                            contact.photoType = type2;
                        }
                    }
                } else if (name2.equals("LOGO")) {
                    String valueType2 = propertyNode.paramMap.getAsString("VALUE");
                    if ((valueType2 == null || !valueType2.equals("URL")) && contact.photoBytes == null) {
                        contact.photoBytes = propertyNode.propValue_bytes;
                        String type3 = propertyNode.paramMap.getAsString(GuideInfoExtraKey.TYPE);
                        if (type3 != null) {
                            contact.photoType = type3;
                        }
                    }
                } else if (name2.equals("EMAIL")) {
                    int type4 = -1;
                    String label2 = null;
                    boolean isPrimary3 = false;
                    for (String typeString3 : propertyNode.paramMap_TYPE) {
                        if (typeString3.equals("PREF") && !prefIsSetEmail) {
                            prefIsSetEmail = true;
                            isPrimary3 = true;
                        } else if (typeString3.equalsIgnoreCase("HOME")) {
                            type4 = 1;
                        } else if (typeString3.equalsIgnoreCase("WORK")) {
                            type4 = 2;
                        } else if (typeString3.equalsIgnoreCase("CELL")) {
                            type4 = 0;
                            label2 = Contacts.ContactMethodsColumns.MOBILE_EMAIL_TYPE_NAME;
                        } else if (typeString3.toUpperCase().startsWith("X-") && type4 < 0) {
                            type4 = 0;
                            label2 = typeString3.substring(2);
                        } else if (type4 < 0) {
                            type4 = 0;
                            label2 = typeString3;
                        }
                    }
                    if (type4 < 0) {
                        type4 = 3;
                    }
                    contact.addContactmethod(1, type4, propertyNode.propValue, label2, isPrimary3);
                } else if (name2.equals("TEL")) {
                    int type5 = -1;
                    String label3 = null;
                    boolean isPrimary4 = false;
                    boolean isFax = false;
                    for (String typeString4 : propertyNode.paramMap_TYPE) {
                        if (typeString4.equals("PREF") && !prefIsSetPhone) {
                            prefIsSetPhone = true;
                            isPrimary4 = true;
                        } else if (typeString4.equalsIgnoreCase("HOME")) {
                            type5 = 1;
                        } else if (typeString4.equalsIgnoreCase("WORK")) {
                            type5 = 3;
                        } else if (typeString4.equalsIgnoreCase("CELL")) {
                            type5 = 2;
                        } else if (typeString4.equalsIgnoreCase("PAGER")) {
                            type5 = 6;
                        } else if (typeString4.equalsIgnoreCase("FAX")) {
                            isFax = true;
                        } else if (!typeString4.equalsIgnoreCase("VOICE") && !typeString4.equalsIgnoreCase("MSG")) {
                            if (typeString4.toUpperCase().startsWith("X-") && type5 < 0) {
                                type5 = 0;
                                label3 = typeString4.substring(2);
                            } else if (type5 < 0) {
                                type5 = 0;
                                label3 = typeString4;
                            }
                        }
                    }
                    if (type5 < 0) {
                        type5 = 1;
                    }
                    if (isFax) {
                        if (type5 == 1) {
                            type5 = 5;
                        } else if (type5 == 3) {
                            type5 = 4;
                        }
                    }
                    contact.addPhone(type5, propertyNode.propValue, label3, isPrimary4);
                } else if (name2.equals("NOTE")) {
                    contact.notes.add(propertyNode.propValue);
                } else if (name2.equals("BDAY")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("URL")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("REV")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("UID")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("KEY")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("MAILER")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("TZ")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("GEO")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("NICKNAME")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("CLASS")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("PROFILE")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("CATEGORIES")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("SOURCE")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("PRODID")) {
                    contact.addExtension(propertyNode);
                } else if (name2.equals("X-PHONETIC-FIRST-NAME")) {
                    xPhoneticFirstName = propertyNode.propValue;
                } else if (name2.equals("X-PHONETIC-MIDDLE-NAME")) {
                    xPhoneticMiddleName = propertyNode.propValue;
                } else if (name2.equals("X-PHONETIC-LAST-NAME")) {
                    xPhoneticLastName = propertyNode.propValue;
                } else {
                    contact.addExtension(propertyNode);
                }
            }
        }
        if (fullName != null) {
            contact.name = fullName;
        } else if (nameFromNProperty != null) {
            contact.name = nameFromNProperty;
        } else {
            contact.name = "";
        }
        if (contact.phoneticName == null && !(xPhoneticFirstName == null && xPhoneticMiddleName == null && xPhoneticLastName == null)) {
            if (nameOrderType == 1) {
                first = xPhoneticLastName;
                second = xPhoneticFirstName;
            } else {
                first = xPhoneticFirstName;
                second = xPhoneticLastName;
            }
            StringBuilder builder4 = new StringBuilder();
            if (first != null) {
                builder4.append(first);
            }
            if (xPhoneticMiddleName != null) {
                builder4.append(xPhoneticMiddleName);
            }
            if (second != null) {
                builder4.append(second);
            }
            contact.phoneticName = builder4.toString();
        }
        if (contact.phoneticName != null) {
            contact.phoneticName = contact.phoneticName.trim();
        }
        if (!prefIsSetPhone && contact.phoneList != null && contact.phoneList.size() > 0) {
            contact.phoneList.get(0).isPrimary = true;
        }
        if (!prefIsSetAddress && contact.contactmethodList != null) {
            Iterator i$2 = contact.contactmethodList.iterator();
            while (true) {
                if (!i$2.hasNext()) {
                    break;
                }
                ContactMethod contactMethod = i$2.next();
                if (contactMethod.kind == 2) {
                    contactMethod.isPrimary = true;
                    break;
                }
            }
        }
        if (!prefIsSetEmail && contact.contactmethodList != null) {
            Iterator i$3 = contact.contactmethodList.iterator();
            while (true) {
                if (!i$3.hasNext()) {
                    break;
                }
                ContactMethod contactMethod2 = i$3.next();
                if (contactMethod2.kind == 1) {
                    contactMethod2.isPrimary = true;
                    break;
                }
            }
        }
        if (prefIsSetOrganization || contact.organizationList == null || contact.organizationList.size() <= 0) {
            return contact;
        }
        contact.organizationList.get(0).isPrimary = true;
        return contact;
    }

    public String displayString() {
        if (this.name.length() > 0) {
            return this.name;
        }
        if (this.contactmethodList != null && this.contactmethodList.size() > 0) {
            for (ContactMethod contactMethod : this.contactmethodList) {
                if (contactMethod.kind == 1 && contactMethod.isPrimary) {
                    return contactMethod.data;
                }
            }
        }
        if (this.phoneList != null && this.phoneList.size() > 0) {
            for (PhoneData phoneData : this.phoneList) {
                if (phoneData.isPrimary) {
                    return phoneData.data;
                }
            }
        }
        return "";
    }

    public boolean isIgnorable() {
        return TextUtils.isEmpty(this.name) && TextUtils.isEmpty(this.phoneticName) && (this.phoneList == null || this.phoneList.size() == 0) && (this.contactmethodList == null || this.contactmethodList.size() == 0);
    }
}
