package org.apache.log4j.helpers;

import java.io.Writer;
import org.apache.log4j.spi.ErrorHandler;

public class SyslogQuietWriter extends QuietWriter {
    int level;
    int syslogFacility;

    public SyslogQuietWriter(Writer writer, int syslogFacility2, ErrorHandler eh) {
        super(writer, eh);
        this.syslogFacility = syslogFacility2;
    }

    public void setLevel(int level2) {
        this.level = level2;
    }

    public void setSyslogFacility(int syslogFacility2) {
        this.syslogFacility = syslogFacility2;
    }

    public void write(String string) {
        super.write(new StringBuffer().append("<").append(this.syslogFacility | this.level).append(">").append(string).toString());
    }
}
