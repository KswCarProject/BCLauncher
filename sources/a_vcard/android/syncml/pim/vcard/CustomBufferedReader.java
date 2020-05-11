package a_vcard.android.syncml.pim.vcard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/* compiled from: VCardParser_V21 */
class CustomBufferedReader extends BufferedReader {
    private long mTime;

    public CustomBufferedReader(Reader in) {
        super(in);
    }

    public String readLine() throws IOException {
        long start = System.currentTimeMillis();
        String ret = super.readLine();
        this.mTime += System.currentTimeMillis() - start;
        return ret;
    }

    public long getTotalmillisecond() {
        return this.mTime;
    }
}
