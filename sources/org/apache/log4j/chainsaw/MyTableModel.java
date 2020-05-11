package org.apache.log4j.chainsaw;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

class MyTableModel extends AbstractTableModel {
    private static final String[] COL_NAMES = {"Time", "Priority", "Trace", "Category", "NDC", "Message"};
    private static final DateFormat DATE_FORMATTER = DateFormat.getDateTimeInstance(3, 2);
    private static final EventDetails[] EMPTY_LIST = new EventDetails[0];
    private static final Logger LOG;
    private static final Comparator MY_COMP = new Comparator() {
        public int compare(Object aObj1, Object aObj2) {
            if (aObj1 == null && aObj2 == null) {
                return 0;
            }
            if (aObj1 == null) {
                return -1;
            }
            if (aObj2 == null) {
                return 1;
            }
            if (((EventDetails) aObj1).getTimeStamp() < ((EventDetails) aObj2).getTimeStamp()) {
                return 1;
            }
            return -1;
        }
    };
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Object;
    static Class class$org$apache$log4j$chainsaw$MyTableModel;
    private final SortedSet mAllEvents = new TreeSet(MY_COMP);
    private String mCategoryFilter = "";
    private EventDetails[] mFilteredEvents = EMPTY_LIST;
    private final Object mLock = new Object();
    private String mMessageFilter = "";
    private String mNDCFilter = "";
    private boolean mPaused = false;
    private final List mPendingEvents = new ArrayList();
    private Priority mPriorityFilter = Priority.DEBUG;
    private String mThreadFilter = "";

    static Object access$000(MyTableModel x0) {
        return x0.mLock;
    }

    static boolean access$100(MyTableModel x0) {
        return x0.mPaused;
    }

    static List access$200(MyTableModel x0) {
        return x0.mPendingEvents;
    }

    static SortedSet access$300(MyTableModel x0) {
        return x0.mAllEvents;
    }

    static boolean access$400(MyTableModel x0, EventDetails x1) {
        return x0.matchFilter(x1);
    }

    static void access$500(MyTableModel x0, boolean x1) {
        x0.updateFilteredEvents(x1);
    }

    static {
        Class cls;
        if (class$org$apache$log4j$chainsaw$MyTableModel == null) {
            cls = class$("org.apache.log4j.chainsaw.MyTableModel");
            class$org$apache$log4j$chainsaw$MyTableModel = cls;
        } else {
            cls = class$org$apache$log4j$chainsaw$MyTableModel;
        }
        LOG = Logger.getLogger(cls);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private class Processor implements Runnable {
        private final MyTableModel this$0;

        private Processor(MyTableModel myTableModel) {
            this.this$0 = myTableModel;
        }

        Processor(MyTableModel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                synchronized (MyTableModel.access$000(this.this$0)) {
                    if (!MyTableModel.access$100(this.this$0)) {
                        boolean toHead = true;
                        boolean needUpdate = false;
                        for (EventDetails event : MyTableModel.access$200(this.this$0)) {
                            MyTableModel.access$300(this.this$0).add(event);
                            if (!toHead || event != MyTableModel.access$300(this.this$0).first()) {
                                toHead = false;
                            } else {
                                toHead = true;
                            }
                            if (needUpdate || MyTableModel.access$400(this.this$0, event)) {
                                needUpdate = true;
                            } else {
                                needUpdate = false;
                            }
                        }
                        MyTableModel.access$200(this.this$0).clear();
                        if (needUpdate) {
                            MyTableModel.access$500(this.this$0, toHead);
                        }
                    }
                }
            }
        }
    }

    MyTableModel() {
        Thread t = new Thread(new Processor(this, (AnonymousClass1) null));
        t.setDaemon(true);
        t.start();
    }

    public int getRowCount() {
        int length;
        synchronized (this.mLock) {
            length = this.mFilteredEvents.length;
        }
        return length;
    }

    public int getColumnCount() {
        return COL_NAMES.length;
    }

    public String getColumnName(int aCol) {
        return COL_NAMES[aCol];
    }

    public Class getColumnClass(int aCol) {
        if (aCol == 2) {
            if (class$java$lang$Boolean != null) {
                return class$java$lang$Boolean;
            }
            Class class$ = class$("java.lang.Boolean");
            class$java$lang$Boolean = class$;
            return class$;
        } else if (class$java$lang$Object != null) {
            return class$java$lang$Object;
        } else {
            Class class$2 = class$("java.lang.Object");
            class$java$lang$Object = class$2;
            return class$2;
        }
    }

    public Object getValueAt(int aRow, int aCol) {
        Object ndc;
        synchronized (this.mLock) {
            EventDetails event = this.mFilteredEvents[aRow];
            if (aCol == 0) {
                ndc = DATE_FORMATTER.format(new Date(event.getTimeStamp()));
            } else if (aCol == 1) {
                ndc = event.getPriority();
            } else if (aCol == 2) {
                ndc = event.getThrowableStrRep() == null ? Boolean.FALSE : Boolean.TRUE;
            } else if (aCol == 3) {
                ndc = event.getCategoryName();
            } else {
                ndc = aCol == 4 ? event.getNDC() : event.getMessage();
            }
        }
        return ndc;
    }

    public void setPriorityFilter(Priority aPriority) {
        synchronized (this.mLock) {
            this.mPriorityFilter = aPriority;
            updateFilteredEvents(false);
        }
    }

    public void setThreadFilter(String aStr) {
        synchronized (this.mLock) {
            this.mThreadFilter = aStr.trim();
            updateFilteredEvents(false);
        }
    }

    public void setMessageFilter(String aStr) {
        synchronized (this.mLock) {
            this.mMessageFilter = aStr.trim();
            updateFilteredEvents(false);
        }
    }

    public void setNDCFilter(String aStr) {
        synchronized (this.mLock) {
            this.mNDCFilter = aStr.trim();
            updateFilteredEvents(false);
        }
    }

    public void setCategoryFilter(String aStr) {
        synchronized (this.mLock) {
            this.mCategoryFilter = aStr.trim();
            updateFilteredEvents(false);
        }
    }

    public void addEvent(EventDetails aEvent) {
        synchronized (this.mLock) {
            this.mPendingEvents.add(aEvent);
        }
    }

    public void clear() {
        synchronized (this.mLock) {
            this.mAllEvents.clear();
            this.mFilteredEvents = new EventDetails[0];
            this.mPendingEvents.clear();
            fireTableDataChanged();
        }
    }

    public void toggle() {
        synchronized (this.mLock) {
            this.mPaused = !this.mPaused;
        }
    }

    public boolean isPaused() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPaused;
        }
        return z;
    }

    public EventDetails getEventDetails(int aRow) {
        EventDetails eventDetails;
        synchronized (this.mLock) {
            eventDetails = this.mFilteredEvents[aRow];
        }
        return eventDetails;
    }

    private void updateFilteredEvents(boolean aInsertedToFront) {
        EventDetails lastFirst;
        long start = System.currentTimeMillis();
        List filtered = new ArrayList();
        int size = this.mAllEvents.size();
        for (EventDetails event : this.mAllEvents) {
            if (matchFilter(event)) {
                filtered.add(event);
            }
        }
        if (this.mFilteredEvents.length == 0) {
            lastFirst = null;
        } else {
            lastFirst = this.mFilteredEvents[0];
        }
        this.mFilteredEvents = (EventDetails[]) filtered.toArray(EMPTY_LIST);
        if (!aInsertedToFront || lastFirst == null) {
            fireTableDataChanged();
        } else {
            int index = filtered.indexOf(lastFirst);
            if (index < 1) {
                LOG.warn("In strange state");
                fireTableDataChanged();
            } else {
                fireTableRowsInserted(0, index - 1);
            }
        }
        LOG.debug(new StringBuffer().append("Total time [ms]: ").append(System.currentTimeMillis() - start).append(" in update, size: ").append(size).toString());
    }

    private boolean matchFilter(EventDetails aEvent) {
        if (!aEvent.getPriority().isGreaterOrEqual(this.mPriorityFilter) || aEvent.getThreadName().indexOf(this.mThreadFilter) < 0 || aEvent.getCategoryName().indexOf(this.mCategoryFilter) < 0 || (this.mNDCFilter.length() != 0 && (aEvent.getNDC() == null || aEvent.getNDC().indexOf(this.mNDCFilter) < 0))) {
            return false;
        }
        String rm = aEvent.getMessage();
        if (rm == null) {
            if (this.mMessageFilter.length() == 0) {
                return true;
            }
            return false;
        } else if (rm.indexOf(this.mMessageFilter) < 0) {
            return false;
        } else {
            return true;
        }
    }
}
