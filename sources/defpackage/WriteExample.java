package defpackage;

import a_vcard.android.syncml.pim.vcard.ContactStruct;
import a_vcard.android.syncml.pim.vcard.VCardComposer;
import a_vcard.android.syncml.pim.vcard.VCardParser_V21;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/* renamed from: WriteExample  reason: default package */
public class WriteExample {
    public static void main(String[] args) throws Exception {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("example.vcard"), VCardParser_V21.DEFAULT_CHARSET);
        VCardComposer composer = new VCardComposer();
        ContactStruct contact1 = new ContactStruct();
        contact1.name = "Neo";
        contact1.company = "The Company";
        contact1.addPhone(2, "+123456789", (String) null, true);
        writer.write(composer.createVCard(contact1, 2));
        writer.write("\n");
        writer.close();
    }
}
