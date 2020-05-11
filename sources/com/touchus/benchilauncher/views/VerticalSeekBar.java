package com.touchus.benchilauncher.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.util.TimeUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.AbsSeekBar;
import org.slf4j.spi.LocationAwareLogger;

public class VerticalSeekBar extends AbsSeekBar {
    private int height;
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private Drawable mThumb;
    boolean mfromUser;
    private int width;

    public interface OnSeekBarChangeListener {
        void onProgressChanged(VerticalSeekBar verticalSeekBar, int i, boolean z);

        void onStartTrackingTouch(VerticalSeekBar verticalSeekBar);

        void onStopTrackingTouch(VerticalSeekBar verticalSeekBar);
    }

    public VerticalSeekBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 16842875);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mfromUser = false;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        this.mOnSeekBarChangeListener = l;
    }

    /* access modifiers changed from: package-private */
    public void onStartTrackingTouch() {
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void onStopTrackingTouch() {
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void onProgressRefresh(float scale, boolean fromUser) {
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getHeight(), thumb, scale, Integer.MIN_VALUE);
            invalidate();
        }
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onProgressChanged(this, getProgress(), this.mfromUser);
        }
    }

    private void setThumbPos(int w, Drawable thumb, float scale, int gap) {
        int topBound;
        int bottomBound;
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        int thumbPos = (int) (((float) ((((getPaddingLeft() + w) - getPaddingRight()) - thumbWidth) + (getThumbOffset() / 2))) * scale);
        if (gap == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            topBound = oldBounds.top;
            bottomBound = oldBounds.bottom;
        } else {
            topBound = gap;
            bottomBound = gap + thumbHeight;
        }
        thumb.setBounds(thumbPos, topBound, thumbPos + thumbWidth, bottomBound);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas c) {
        c.rotate(-90.0f);
        c.translate((float) (-this.height), 0.0f);
        super.onDraw(c);
    }

    /* access modifiers changed from: protected */
    public synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.height = 258;
        this.width = 20;
        setMeasuredDimension(this.width, this.height);
    }

    public void setThumb(Drawable thumb) {
        this.mThumb = thumb;
        super.setThumb(thumb);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldw, oldh);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                setPressed(true);
                onStartTrackingTouch();
                trackTouchEvent(event);
                break;
            case 1:
                trackTouchEvent(event);
                onStopTrackingTouch();
                setPressed(false);
                break;
            case 2:
                trackTouchEvent(event);
                attemptClaimDrag();
                this.mfromUser = true;
                break;
            case 3:
                onStopTrackingTouch();
                setPressed(false);
                this.mfromUser = false;
                break;
        }
        return true;
    }

    private void trackTouchEvent(MotionEvent event) {
        float scale;
        int Height = getHeight();
        int available = (Height - getPaddingBottom()) - getPaddingTop();
        int Y = (int) event.getY();
        if (Y > Height - getPaddingBottom()) {
            scale = 0.0f;
        } else if (Y < getPaddingTop()) {
            scale = 1.0f;
        } else {
            scale = ((float) ((Height - getPaddingBottom()) - Y)) / ((float) available);
        }
        setProgress((int) (scale * ((float) getMax())));
    }

    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        KeyEvent newEvent;
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case TimeUtils.HUNDRED_DAY_FIELD_LEN:
                newEvent = new KeyEvent(0, 22);
                break;
            case LocationAwareLogger.INFO_INT /*20*/:
                newEvent = new KeyEvent(0, 21);
                break;
            case 21:
                newEvent = new KeyEvent(0, 20);
                break;
            case 22:
                newEvent = new KeyEvent(0, 19);
                break;
            default:
                newEvent = new KeyEvent(0, event.getKeyCode());
                break;
        }
        return newEvent.dispatch(this);
    }
}
