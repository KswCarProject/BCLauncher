package org.apache.log4j.helpers;

import java.io.FilterWriter;
import java.io.Writer;
import org.apache.log4j.spi.ErrorHandler;

public class QuietWriter extends FilterWriter {
    protected ErrorHandler errorHandler;

    public QuietWriter(Writer writer, ErrorHandler errorHandler2) {
        super(writer);
        setErrorHandler(errorHandler2);
    }

    public void write(String string) {
        if (string != null) {
            try {
                this.out.write(string);
            } catch (Exception e) {
                this.errorHandler.error(new StringBuffer().append("Failed to write [").append(string).append("].").toString(), e, 1);
            }
        }
    }

    public void flush() {
        try {
            this.out.flush();
        } catch (Exception e) {
            this.errorHandler.error("Failed to flush writer,", e, 2);
        }
    }

    public void setErrorHandler(ErrorHandler eh) {
        if (eh == null) {
            throw new IllegalArgumentException("Attempted to set null ErrorHandler.");
        }
        this.errorHandler = eh;
    }
}