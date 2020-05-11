package org.apache.harmony.awt.datatransfer;

public class DataTransferThread extends Thread {
    private final DTK dtk;

    public DataTransferThread(DTK dtk2) {
        super("AWT-DataTransferThread");
        setDaemon(true);
        this.dtk = dtk2;
    }

    public void run() {
        synchronized (this) {
            try {
                this.dtk.initDragAndDrop();
                notifyAll();
            } catch (Throwable th) {
                notifyAll();
                throw th;
            }
        }
        this.dtk.runEventLoop();
    }

    public void start() {
        synchronized (this) {
            super.start();
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
