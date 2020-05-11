package defpackage;

import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;
import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/* renamed from: ReadExample  reason: default package */
public class ReadExample {
    public static void main(String[] args) throws Exception {
        VCardParser parser = new VCardParser();
        VDataBuilder builder = new VDataBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("example.vcard"), VCardParser_V21.DEFAULT_CHARSET));
        String vcardString = "";
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            vcardString = vcardString + line + "\n";
        }
        reader.close();
        if (!parser.parse(vcardString, VCardParser_V21.DEFAULT_CHARSET, builder)) {
            throw new VCardException("Could not parse vCard file: " + "example.vcard");
        }
        for (VNode contact : builder.vNodeList) {
            String name = null;
            Iterator i$ = contact.propList.iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                PropertyNode prop = i$.next();
                if ("FN".equals(prop.propName)) {
                    name = prop.propValue;
                    break;
                }
            }
            System.out.println("Found contact: " + name);
        }
    }
}
