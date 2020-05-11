package a_vcard.android.syncml.pim;

import java.util.List;

public interface VBuilder {
    void end();

    void endProperty();

    void endRecord();

    void propertyGroup(String str);

    void propertyName(String str);

    void propertyParamType(String str);

    void propertyParamValue(String str);

    void propertyValues(List<String> list);

    void start();

    void startProperty();

    void startRecord(String str);
}
