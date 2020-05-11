package com.sun.mail.util;

import java.io.OutputStream;
import org.apache.log4j.Priority;

public class BEncoderStream extends BASE64EncoderStream {
    public BEncoderStream(OutputStream out) {
        super(out, Priority.OFF_INT);
    }

    public static int encodedLength(byte[] b) {
        return ((b.length + 2) / 3) * 4;
    }
}
