package a_vcard.android.text;

import a_vcard.com.android.internal.util.ArrayUtils;
import java.lang.reflect.Array;

public class SpannableStringBuilder implements Spannable, Editable {
    private static final int END_MASK = 15;
    private static final int MARK = 1;
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];
    private static final int PARAGRAPH = 3;
    private static final int POINT = 2;
    private static final int START_MASK = 240;
    private static final int START_SHIFT = 4;
    private InputFilter[] mFilters;
    private int mGapLength;
    private int mGapStart;
    private int mSpanCount;
    private int[] mSpanEnds;
    private int[] mSpanFlags;
    private int[] mSpanStarts;
    private Object[] mSpans;
    private char[] mText;

    public SpannableStringBuilder() {
        this("");
    }

    public SpannableStringBuilder(CharSequence text) {
        this(text, 0, text.length());
    }

    public SpannableStringBuilder(CharSequence text, int start, int end) {
        this.mFilters = NO_FILTERS;
        int srclen = end - start;
        int len = ArrayUtils.idealCharArraySize(srclen + 1);
        this.mText = new char[len];
        this.mGapStart = srclen;
        this.mGapLength = len - srclen;
        TextUtils.getChars(text, start, end, this.mText, 0);
        this.mSpanCount = 0;
        int alloc = ArrayUtils.idealIntArraySize(0);
        this.mSpans = new Object[alloc];
        this.mSpanStarts = new int[alloc];
        this.mSpanEnds = new int[alloc];
        this.mSpanFlags = new int[alloc];
        if (text instanceof Spanned) {
            Spanned sp = (Spanned) text;
            Object[] spans = sp.getSpans(start, end, Object.class);
            for (int i = 0; i < spans.length; i++) {
                if (!(spans[i] instanceof NoCopySpan)) {
                    int st = sp.getSpanStart(spans[i]) - start;
                    int en = sp.getSpanEnd(spans[i]) - start;
                    int fl = sp.getSpanFlags(spans[i]);
                    st = st < 0 ? 0 : st;
                    st = st > end - start ? end - start : st;
                    en = en < 0 ? 0 : en;
                    setSpan(spans[i], st, en > end - start ? end - start : en, fl);
                }
            }
        }
    }

    public static SpannableStringBuilder valueOf(CharSequence source) {
        if (source instanceof SpannableStringBuilder) {
            return (SpannableStringBuilder) source;
        }
        return new SpannableStringBuilder(source);
    }

    public char charAt(int where) {
        int len = length();
        if (where < 0) {
            throw new IndexOutOfBoundsException("charAt: " + where + " < 0");
        } else if (where >= len) {
            throw new IndexOutOfBoundsException("charAt: " + where + " >= length " + len);
        } else if (where >= this.mGapStart) {
            return this.mText[this.mGapLength + where];
        } else {
            return this.mText[where];
        }
    }

    public int length() {
        return this.mText.length - this.mGapLength;
    }

    private void resizeFor(int size) {
        int newlen = ArrayUtils.idealCharArraySize(size + 1);
        char[] newtext = new char[newlen];
        int after = this.mText.length - (this.mGapStart + this.mGapLength);
        System.arraycopy(this.mText, 0, newtext, 0, this.mGapStart);
        System.arraycopy(this.mText, this.mText.length - after, newtext, newlen - after, after);
        for (int i = 0; i < this.mSpanCount; i++) {
            if (this.mSpanStarts[i] > this.mGapStart) {
                int[] iArr = this.mSpanStarts;
                iArr[i] = iArr[i] + (newlen - this.mText.length);
            }
            if (this.mSpanEnds[i] > this.mGapStart) {
                int[] iArr2 = this.mSpanEnds;
                iArr2[i] = iArr2[i] + (newlen - this.mText.length);
            }
        }
        int oldlen = this.mText.length;
        this.mText = newtext;
        this.mGapLength += this.mText.length - oldlen;
        if (this.mGapLength < 1) {
            new Exception("mGapLength < 1").printStackTrace();
        }
    }

    private void moveGapTo(int where) {
        int flag;
        int flag2;
        if (where != this.mGapStart) {
            boolean atend = where == length();
            if (where < this.mGapStart) {
                int overlap = this.mGapStart - where;
                System.arraycopy(this.mText, where, this.mText, (this.mGapStart + this.mGapLength) - overlap, overlap);
            } else {
                int overlap2 = where - this.mGapStart;
                System.arraycopy(this.mText, (this.mGapLength + where) - overlap2, this.mText, this.mGapStart, overlap2);
            }
            for (int i = 0; i < this.mSpanCount; i++) {
                int start = this.mSpanStarts[i];
                int end = this.mSpanEnds[i];
                if (start > this.mGapStart) {
                    start -= this.mGapLength;
                }
                if (start > where) {
                    start += this.mGapLength;
                } else if (start == where && ((flag = (this.mSpanFlags[i] & START_MASK) >> 4) == 2 || (atend && flag == 3))) {
                    start += this.mGapLength;
                }
                if (end > this.mGapStart) {
                    end -= this.mGapLength;
                }
                if (end > where) {
                    end += this.mGapLength;
                } else if (end == where && ((flag2 = this.mSpanFlags[i] & 15) == 2 || (atend && flag2 == 3))) {
                    end += this.mGapLength;
                }
                this.mSpanStarts[i] = start;
                this.mSpanEnds[i] = end;
            }
            this.mGapStart = where;
        }
    }

    public SpannableStringBuilder insert(int where, CharSequence tb, int start, int end) {
        return replace(where, where, tb, start, end);
    }

    public SpannableStringBuilder insert(int where, CharSequence tb) {
        return replace(where, where, tb, 0, tb.length());
    }

    public SpannableStringBuilder delete(int start, int end) {
        SpannableStringBuilder ret = replace(start, end, (CharSequence) "", 0, 0);
        if (this.mGapLength > length() * 2) {
            resizeFor(length());
        }
        return ret;
    }

    public void clear() {
        replace(0, length(), (CharSequence) "", 0, 0);
    }

    public void clearSpans() {
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            Object what = this.mSpans[i];
            int ostart = this.mSpanStarts[i];
            int oend = this.mSpanEnds[i];
            if (ostart > this.mGapStart) {
                ostart -= this.mGapLength;
            }
            if (oend > this.mGapStart) {
                oend -= this.mGapLength;
            }
            this.mSpanCount = i;
            this.mSpans[i] = null;
            sendSpanRemoved(what, ostart, oend);
        }
    }

    public SpannableStringBuilder append(CharSequence text) {
        int length = length();
        return replace(length, length, text, 0, text.length());
    }

    public SpannableStringBuilder append(CharSequence text, int start, int end) {
        int length = length();
        return replace(length, length, text, start, end);
    }

    public SpannableStringBuilder append(char text) {
        return append((CharSequence) String.valueOf(text));
    }

    private int change(int start, int end, CharSequence tb, int tbstart, int tbend) {
        return change(true, start, end, tb, tbstart, tbend);
    }

    private int change(boolean notify, int start, int end, CharSequence tb, int tbstart, int tbend) {
        int st;
        int en;
        checkRange("replace", start, end);
        int ret = tbend - tbstart;
        TextWatcher[] recipients = null;
        if (notify) {
            recipients = sendTextWillChange(start, end - start, tbend - tbstart);
        }
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            if ((this.mSpanFlags[i] & 51) == 51) {
                int st2 = this.mSpanStarts[i];
                if (st2 > this.mGapStart) {
                    st2 -= this.mGapLength;
                }
                int en2 = this.mSpanEnds[i];
                if (en2 > this.mGapStart) {
                    en2 -= this.mGapLength;
                }
                int ost = st;
                int oen = en;
                int clen = length();
                if (st > start && st <= end) {
                    st = end;
                    while (st < clen) {
                        if (st > end) {
                            if (charAt(st - 1) == 10) {
                                break;
                            }
                        }
                        st++;
                    }
                }
                if (en > start && en <= end) {
                    en = end;
                    while (en < clen) {
                        if (en > end) {
                            if (charAt(en - 1) == 10) {
                                break;
                            }
                        }
                        en++;
                    }
                }
                if (st != ost || en != oen) {
                    setSpan(this.mSpans[i], st, en, this.mSpanFlags[i]);
                }
            }
        }
        moveGapTo(end);
        if (tbend - tbstart >= this.mGapLength + (end - start)) {
            resizeFor((((this.mText.length - this.mGapLength) + tbend) - tbstart) - (end - start));
        }
        this.mGapStart += (tbend - tbstart) - (end - start);
        this.mGapLength -= (tbend - tbstart) - (end - start);
        if (this.mGapLength < 1) {
            new Exception("mGapLength < 1").printStackTrace();
        }
        TextUtils.getChars(tb, tbstart, tbend, this.mText, start);
        if (tb instanceof Spanned) {
            Spanned sp = (Spanned) tb;
            Object[] spans = sp.getSpans(tbstart, tbend, Object.class);
            for (int i2 = 0; i2 < spans.length; i2++) {
                int st3 = sp.getSpanStart(spans[i2]);
                int en3 = sp.getSpanEnd(spans[i2]);
                if (st3 < tbstart) {
                    st3 = tbstart;
                }
                if (en3 > tbend) {
                    en3 = tbend;
                }
                if (getSpanStart(spans[i2]) < 0) {
                    setSpan(false, spans[i2], (st3 - tbstart) + start, (en3 - tbstart) + start, sp.getSpanFlags(spans[i2]));
                }
            }
        }
        if (tbend <= tbstart || end - start != 0) {
            boolean atend = this.mGapStart + this.mGapLength == this.mText.length;
            for (int i3 = this.mSpanCount - 1; i3 >= 0; i3--) {
                if (this.mSpanStarts[i3] >= start && this.mSpanStarts[i3] < this.mGapStart + this.mGapLength) {
                    int flag = (this.mSpanFlags[i3] & START_MASK) >> 4;
                    if (flag == 2 || (flag == 3 && atend)) {
                        this.mSpanStarts[i3] = this.mGapStart + this.mGapLength;
                    } else {
                        this.mSpanStarts[i3] = start;
                    }
                }
                if (this.mSpanEnds[i3] >= start && this.mSpanEnds[i3] < this.mGapStart + this.mGapLength) {
                    int flag2 = this.mSpanFlags[i3] & 15;
                    if (flag2 == 2 || (flag2 == 3 && atend)) {
                        this.mSpanEnds[i3] = this.mGapStart + this.mGapLength;
                    } else {
                        this.mSpanEnds[i3] = start;
                    }
                }
                if (this.mSpanEnds[i3] < this.mSpanStarts[i3]) {
                    System.arraycopy(this.mSpans, i3 + 1, this.mSpans, i3, this.mSpanCount - (i3 + 1));
                    System.arraycopy(this.mSpanStarts, i3 + 1, this.mSpanStarts, i3, this.mSpanCount - (i3 + 1));
                    System.arraycopy(this.mSpanEnds, i3 + 1, this.mSpanEnds, i3, this.mSpanCount - (i3 + 1));
                    System.arraycopy(this.mSpanFlags, i3 + 1, this.mSpanFlags, i3, this.mSpanCount - (i3 + 1));
                    this.mSpanCount--;
                }
            }
            if (notify) {
                sendTextChange(recipients, start, end - start, tbend - tbstart);
                sendTextHasChanged(recipients);
            }
        } else if (notify) {
            sendTextChange(recipients, start, end - start, tbend - tbstart);
            sendTextHasChanged(recipients);
        }
        return ret;
    }

    public SpannableStringBuilder replace(int start, int end, CharSequence tb) {
        return replace(start, end, tb, 0, tb.length());
    }

    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart, int tbend) {
        int filtercount = this.mFilters.length;
        for (int i = 0; i < filtercount; i++) {
            CharSequence repl = this.mFilters[i].filter(tb, tbstart, tbend, this, start, end);
            if (repl != null) {
                tb = repl;
                tbstart = 0;
                tbend = repl.length();
            }
        }
        if (!(end == start && tbstart == tbend)) {
            if (end == start || tbstart == tbend) {
                change(start, end, tb, tbstart, tbend);
            } else {
                int selstart = Selection.getSelectionStart(this);
                int selend = Selection.getSelectionEnd(this);
                checkRange("replace", start, end);
                moveGapTo(end);
                TextWatcher[] recipients = sendTextWillChange(start, end - start, tbend - tbstart);
                int origlen = end - start;
                if (this.mGapLength < 2) {
                    resizeFor(length() + 1);
                }
                for (int i2 = this.mSpanCount - 1; i2 >= 0; i2--) {
                    if (this.mSpanStarts[i2] == this.mGapStart) {
                        int[] iArr = this.mSpanStarts;
                        iArr[i2] = iArr[i2] + 1;
                    }
                    if (this.mSpanEnds[i2] == this.mGapStart) {
                        int[] iArr2 = this.mSpanEnds;
                        iArr2[i2] = iArr2[i2] + 1;
                    }
                }
                this.mText[this.mGapStart] = ' ';
                this.mGapStart++;
                this.mGapLength--;
                if (this.mGapLength < 1) {
                    new Exception("mGapLength < 1").printStackTrace();
                }
                int inserted = change(false, start + 1, start + 1, tb, tbstart, tbend);
                change(false, start, start + 1, "", 0, 0);
                change(false, start + inserted, ((start + inserted) + ((end + 1) - start)) - 1, "", 0, 0);
                if (selstart <= start || selstart >= end) {
                } else {
                    int selstart2 = ((int) ((((long) inserted) * ((long) (selstart - start))) / ((long) (end - start)))) + start;
                    setSpan(false, Selection.SELECTION_START, selstart2, selstart2, 34);
                }
                if (selend > start && selend < end) {
                    int selend2 = ((int) ((((long) inserted) * ((long) (selend - start))) / ((long) (end - start)))) + start;
                    setSpan(false, Selection.SELECTION_END, selend2, selend2, 34);
                }
                sendTextChange(recipients, start, origlen, inserted);
                sendTextHasChanged(recipients);
            }
        }
        return this;
    }

    public void setSpan(Object what, int start, int end, int flags) {
        setSpan(true, what, start, end, flags);
    }

    private void setSpan(boolean send, Object what, int start, int end, int flags) {
        int flag;
        int flag2;
        int nstart = start;
        int nend = end;
        checkRange("setSpan", start, end);
        if (!((flags & START_MASK) != 48 || start == 0 || start == length())) {
            if (charAt(start - 1) != 10) {
                throw new RuntimeException("PARAGRAPH span must start at paragraph boundary");
            }
        }
        if (!((flags & 15) != 3 || end == 0 || end == length())) {
            if (charAt(end - 1) != 10) {
                throw new RuntimeException("PARAGRAPH span must end at paragraph boundary");
            }
        }
        if (start > this.mGapStart) {
            start += this.mGapLength;
        } else if (start == this.mGapStart && ((flag = (flags & START_MASK) >> 4) == 2 || (flag == 3 && start == length()))) {
            start += this.mGapLength;
        }
        if (end > this.mGapStart) {
            end += this.mGapLength;
        } else if (end == this.mGapStart && ((flag2 = flags & 15) == 2 || (flag2 == 3 && end == length()))) {
            end += this.mGapLength;
        }
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        for (int i = 0; i < count; i++) {
            if (spans[i] == what) {
                int ostart = this.mSpanStarts[i];
                int oend = this.mSpanEnds[i];
                if (ostart > this.mGapStart) {
                    ostart -= this.mGapLength;
                }
                if (oend > this.mGapStart) {
                    oend -= this.mGapLength;
                }
                this.mSpanStarts[i] = start;
                this.mSpanEnds[i] = end;
                this.mSpanFlags[i] = flags;
                if (send) {
                    sendSpanChanged(what, ostart, oend, nstart, nend);
                    return;
                }
                return;
            }
        }
        if (this.mSpanCount + 1 >= this.mSpans.length) {
            int newsize = ArrayUtils.idealIntArraySize(this.mSpanCount + 1);
            Object[] newspans = new Object[newsize];
            int[] newspanstarts = new int[newsize];
            int[] newspanends = new int[newsize];
            int[] newspanflags = new int[newsize];
            System.arraycopy(this.mSpans, 0, newspans, 0, this.mSpanCount);
            System.arraycopy(this.mSpanStarts, 0, newspanstarts, 0, this.mSpanCount);
            System.arraycopy(this.mSpanEnds, 0, newspanends, 0, this.mSpanCount);
            System.arraycopy(this.mSpanFlags, 0, newspanflags, 0, this.mSpanCount);
            this.mSpans = newspans;
            this.mSpanStarts = newspanstarts;
            this.mSpanEnds = newspanends;
            this.mSpanFlags = newspanflags;
        }
        this.mSpans[this.mSpanCount] = what;
        this.mSpanStarts[this.mSpanCount] = start;
        this.mSpanEnds[this.mSpanCount] = end;
        this.mSpanFlags[this.mSpanCount] = flags;
        this.mSpanCount++;
        if (send) {
            sendSpanAdded(what, nstart, nend);
        }
    }

    public void removeSpan(Object what) {
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            if (this.mSpans[i] == what) {
                int ostart = this.mSpanStarts[i];
                int oend = this.mSpanEnds[i];
                if (ostart > this.mGapStart) {
                    ostart -= this.mGapLength;
                }
                if (oend > this.mGapStart) {
                    oend -= this.mGapLength;
                }
                int count = this.mSpanCount - (i + 1);
                System.arraycopy(this.mSpans, i + 1, this.mSpans, i, count);
                System.arraycopy(this.mSpanStarts, i + 1, this.mSpanStarts, i, count);
                System.arraycopy(this.mSpanEnds, i + 1, this.mSpanEnds, i, count);
                System.arraycopy(this.mSpanFlags, i + 1, this.mSpanFlags, i, count);
                this.mSpanCount--;
                this.mSpans[this.mSpanCount] = null;
                sendSpanRemoved(what, ostart, oend);
                return;
            }
        }
    }

    public int getSpanStart(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                int where = this.mSpanStarts[i];
                if (where > this.mGapStart) {
                    return where - this.mGapLength;
                }
                return where;
            }
        }
        return -1;
    }

    public int getSpanEnd(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                int where = this.mSpanEnds[i];
                if (where > this.mGapStart) {
                    return where - this.mGapLength;
                }
                return where;
            }
        }
        return -1;
    }

    public int getSpanFlags(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return this.mSpanFlags[i];
            }
        }
        return 0;
    }

    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind) {
        int count;
        int spanCount = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] starts = this.mSpanStarts;
        int[] ends = this.mSpanEnds;
        int[] flags = this.mSpanFlags;
        int gapstart = this.mGapStart;
        int gaplen = this.mGapLength;
        T[] tArr = null;
        Object ret1 = null;
        int i = 0;
        int count2 = 0;
        while (i < spanCount) {
            int spanStart = starts[i];
            int spanEnd = ends[i];
            if (spanStart > gapstart) {
                spanStart -= gaplen;
            }
            if (spanEnd > gapstart) {
                spanEnd -= gaplen;
            }
            if (spanStart > queryEnd) {
                count = count2;
            } else if (spanEnd < queryStart) {
                count = count2;
            } else {
                if (!(spanStart == spanEnd || queryStart == queryEnd)) {
                    if (spanStart == queryEnd) {
                        count = count2;
                    } else if (spanEnd == queryStart) {
                        count = count2;
                    }
                }
                if (kind != null && !kind.isInstance(spans[i])) {
                    count = count2;
                } else if (count2 == 0) {
                    ret1 = spans[i];
                    count = count2 + 1;
                } else {
                    if (count2 == 1) {
                        tArr = (Object[]) Array.newInstance(kind, (spanCount - i) + 1);
                        tArr[0] = ret1;
                    }
                    int prio = flags[i] & Spanned.SPAN_PRIORITY;
                    if (prio != 0) {
                        int j = 0;
                        while (j < count2 && prio <= (getSpanFlags(tArr[j]) & Spanned.SPAN_PRIORITY)) {
                            j++;
                        }
                        System.arraycopy(tArr, j, tArr, j + 1, count2 - j);
                        tArr[j] = spans[i];
                        count = count2 + 1;
                    } else {
                        count = count2 + 1;
                        tArr[count2] = spans[i];
                    }
                }
            }
            i++;
            count2 = count;
        }
        if (count2 == 0) {
            return ArrayUtils.emptyArray(kind);
        }
        if (count2 == 1) {
            T[] tArr2 = (Object[]) Array.newInstance(kind, 1);
            tArr2[0] = ret1;
            return (Object[]) tArr2;
        } else if (count2 == tArr.length) {
            return (Object[]) tArr;
        } else {
            Object[] nret = (Object[]) Array.newInstance(kind, count2);
            System.arraycopy(tArr, 0, nret, 0, count2);
            return nret;
        }
    }

    public int nextSpanTransition(int start, int limit, Class kind) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] starts = this.mSpanStarts;
        int[] ends = this.mSpanEnds;
        int gapstart = this.mGapStart;
        int gaplen = this.mGapLength;
        if (kind == null) {
            kind = Object.class;
        }
        for (int i = 0; i < count; i++) {
            int st = starts[i];
            int en = ends[i];
            if (st > gapstart) {
                st -= gaplen;
            }
            if (en > gapstart) {
                en -= gaplen;
            }
            if (st > start && st < limit && kind.isInstance(spans[i])) {
                limit = st;
            }
            if (en > start && en < limit && kind.isInstance(spans[i])) {
                limit = en;
            }
        }
        return limit;
    }

    public CharSequence subSequence(int start, int end) {
        return new SpannableStringBuilder(this, start, end);
    }

    public void getChars(int start, int end, char[] dest, int destoff) {
        checkRange("getChars", start, end);
        if (end <= this.mGapStart) {
            System.arraycopy(this.mText, start, dest, destoff, end - start);
        } else if (start >= this.mGapStart) {
            System.arraycopy(this.mText, this.mGapLength + start, dest, destoff, end - start);
        } else {
            System.arraycopy(this.mText, start, dest, destoff, this.mGapStart - start);
            System.arraycopy(this.mText, this.mGapStart + this.mGapLength, dest, (this.mGapStart - start) + destoff, end - this.mGapStart);
        }
    }

    public String toString() {
        int len = length();
        char[] buf = new char[len];
        getChars(0, len, buf, 0);
        return new String(buf);
    }

    private TextWatcher[] sendTextWillChange(int start, int before, int after) {
        TextWatcher[] recip = (TextWatcher[]) getSpans(start, start + before, TextWatcher.class);
        for (TextWatcher beforeTextChanged : recip) {
            beforeTextChanged.beforeTextChanged(this, start, before, after);
        }
        return recip;
    }

    private void sendTextChange(TextWatcher[] recip, int start, int before, int after) {
        for (TextWatcher onTextChanged : recip) {
            onTextChanged.onTextChanged(this, start, before, after);
        }
    }

    private void sendTextHasChanged(TextWatcher[] recip) {
        for (TextWatcher afterTextChanged : recip) {
            afterTextChanged.afterTextChanged(this);
        }
    }

    private void sendSpanAdded(Object what, int start, int end) {
        for (SpanWatcher onSpanAdded : (SpanWatcher[]) getSpans(start, end, SpanWatcher.class)) {
            onSpanAdded.onSpanAdded(this, what, start, end);
        }
    }

    private void sendSpanRemoved(Object what, int start, int end) {
        for (SpanWatcher onSpanRemoved : (SpanWatcher[]) getSpans(start, end, SpanWatcher.class)) {
            onSpanRemoved.onSpanRemoved(this, what, start, end);
        }
    }

    private void sendSpanChanged(Object what, int s, int e, int st, int en) {
        for (SpanWatcher onSpanChanged : (SpanWatcher[]) getSpans(Math.min(s, st), Math.max(e, en), SpanWatcher.class)) {
            onSpanChanged.onSpanChanged(this, what, s, e, st, en);
        }
    }

    private static String region(int start, int end) {
        return "(" + start + " ... " + end + ")";
    }

    private void checkRange(String operation, int start, int end) {
        if (end < start) {
            throw new IndexOutOfBoundsException(operation + " " + region(start, end) + " has end before start");
        }
        int len = length();
        if (start > len || end > len) {
            throw new IndexOutOfBoundsException(operation + " " + region(start, end) + " ends beyond length " + len);
        } else if (start < 0 || end < 0) {
            throw new IndexOutOfBoundsException(operation + " " + region(start, end) + " starts before 0");
        }
    }

    private boolean isprint(char c) {
        if (c < ' ' || c > '~') {
            return false;
        }
        return true;
    }

    public void setFilters(InputFilter[] filters) {
        if (filters == null) {
            throw new IllegalArgumentException();
        }
        this.mFilters = filters;
    }

    public InputFilter[] getFilters() {
        return this.mFilters;
    }
}
