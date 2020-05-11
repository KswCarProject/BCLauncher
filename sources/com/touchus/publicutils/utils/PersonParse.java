package com.touchus.publicutils.utils;

import a_vcard.android.provider.Contacts;
import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import android.util.Log;
import android.util.Xml;
import com.touchus.publicutils.bean.Person;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class PersonParse {
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004c A[SYNTHETIC, Splitter:B:24:0x004c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getFileName(java.lang.String r10) {
        /*
            java.lang.String r1 = ""
            java.io.File r3 = new java.io.File
            r3.<init>(r10)
            r5 = 0
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ Exception -> 0x003c, all -> 0x0049 }
            r6.<init>(r3)     // Catch:{ Exception -> 0x003c, all -> 0x0049 }
            if (r6 == 0) goto L_0x0022
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r4.<init>(r6)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r0.<init>(r4)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
        L_0x0019:
            java.lang.String r7 = r0.readLine()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            if (r7 != 0) goto L_0x002a
            r6.close()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
        L_0x0022:
            if (r6 == 0) goto L_0x0027
            r6.close()     // Catch:{ IOException -> 0x0055 }
        L_0x0027:
            r5 = r6
            r8 = r1
        L_0x0029:
            return r8
        L_0x002a:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.String r9 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r8.<init>(r9)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.StringBuilder r8 = r8.append(r7)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            java.lang.String r1 = r8.toString()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            goto L_0x0019
        L_0x003c:
            r2 = move-exception
        L_0x003d:
            if (r5 == 0) goto L_0x0042
            r5.close()     // Catch:{ IOException -> 0x0044 }
        L_0x0042:
            r8 = 0
            goto L_0x0029
        L_0x0044:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0042
        L_0x0049:
            r8 = move-exception
        L_0x004a:
            if (r5 == 0) goto L_0x004f
            r5.close()     // Catch:{ IOException -> 0x0050 }
        L_0x004f:
            throw r8
        L_0x0050:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x004f
        L_0x0055:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0027
        L_0x005a:
            r8 = move-exception
            r5 = r6
            goto L_0x004a
        L_0x005d:
            r2 = move-exception
            r5 = r6
            goto L_0x003d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.touchus.publicutils.utils.PersonParse.getFileName(java.lang.String):java.lang.String");
    }

    public static ArrayList<Person> getPersons(InputStream xml) throws Exception {
        ArrayList<Person> persons = null;
        Person person = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, VCardParser_V21.DEFAULT_CHARSET);
        for (int event = pullParser.getEventType(); event != 1; event = pullParser.next()) {
            switch (event) {
                case 0:
                    persons = new ArrayList<>();
                    break;
                case 2:
                    if (Contacts.OrganizationColumns.PERSON_ID.equals(pullParser.getName())) {
                        long id = Long.valueOf(pullParser.getAttributeValue(0)).longValue();
                        person = new Person();
                        person.setId(String.valueOf(id));
                    }
                    if (Contacts.PeopleColumns.NAME.equals(pullParser.getName())) {
                        person.setName(pullParser.nextText());
                    }
                    if ("phone".equals(pullParser.getName())) {
                        person.setPhone(new String(pullParser.nextText()));
                    }
                    if ("flag".equals(pullParser.getName())) {
                        person.setFlag(Integer.valueOf(pullParser.nextText()).intValue());
                    }
                    if (!"remark".equals(pullParser.getName())) {
                        break;
                    } else {
                        person.setRemark(new String(pullParser.nextText()));
                        break;
                    }
                case 3:
                    if (!Contacts.OrganizationColumns.PERSON_ID.equals(pullParser.getName())) {
                        break;
                    } else {
                        persons.add(person);
                        person = null;
                        break;
                    }
            }
        }
        Log.d("onPhoneBookList::", "getHistoryList::" + persons.size());
        return persons;
    }

    public static void save(ArrayList<Person> persons, OutputStream out) throws Exception {
        Person person1 = null;
        try {
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(out, VCardParser_V21.DEFAULT_CHARSET);
            serializer.startDocument(VCardParser_V21.DEFAULT_CHARSET, true);
            serializer.startTag((String) null, "persons");
            Iterator<Person> it = persons.iterator();
            while (it.hasNext()) {
                Person person = it.next();
                person1 = person;
                Log.e("onPhoneBookList", "onPhoneBookList persons ===== " + person);
                serializer.startTag((String) null, Contacts.OrganizationColumns.PERSON_ID);
                serializer.attribute((String) null, "id", person.getId().toString());
                serializer.startTag((String) null, Contacts.PeopleColumns.NAME);
                serializer.text(person.getName());
                serializer.endTag((String) null, Contacts.PeopleColumns.NAME);
                serializer.startTag((String) null, "phone");
                serializer.text(person.getPhone());
                serializer.endTag((String) null, "phone");
                serializer.startTag((String) null, "flag");
                serializer.text(new StringBuilder(String.valueOf(person.getFlag())).toString());
                serializer.endTag((String) null, "flag");
                serializer.startTag((String) null, "remark");
                serializer.text(person.getRemark());
                serializer.endTag((String) null, "remark");
                serializer.endTag((String) null, Contacts.OrganizationColumns.PERSON_ID);
            }
            serializer.endTag((String) null, "persons");
            serializer.endDocument();
            out.flush();
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            if (person1 != null) {
                persons.remove(person1);
                save(persons, out);
            }
            e.printStackTrace();
            if (out != null) {
                out.close();
            }
        } catch (Throwable th) {
            if (out != null) {
                out.close();
            }
            throw th;
        }
    }
}
