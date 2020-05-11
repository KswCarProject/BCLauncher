package com.touchus.benchilauncher.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import com.touchus.benchilauncher.R;

@SuppressLint({"HandlerLeak"})
public class ClockView extends View {
    private Drawable clockDrawable;
    private Thread clockThread;
    private int hour;
    private Drawable hourDrawable;
    /* access modifiers changed from: private */
    public boolean isChange;
    private boolean isSetTime;
    private int minute;
    private Drawable minuteDrawable;
    private Paint paint;
    private Time time;

    public ClockView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.isSetTime = false;
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyClockStyleable, defStyle, 0);
        this.clockDrawable = ta.getDrawable(0);
        this.hourDrawable = ta.getDrawable(1);
        this.minuteDrawable = ta.getDrawable(2);
        ta.recycle();
        this.paint = new Paint();
        this.paint.setColor(Color.parseColor("#000000"));
        this.paint.setTypeface(Typeface.DEFAULT_BOLD);
        this.paint.setFakeBoldText(true);
        this.paint.setAntiAlias(true);
        this.time = new Time();
        this.clockThread = new Thread() {
            public void run() {
                while (ClockView.this.isChange) {
                    ClockView.this.postInvalidate();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void setTime(boolean isSetTime2, int h, int m) {
        this.isSetTime = isSetTime2;
        this.hour = h;
        this.minute = m;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.isSetTime) {
            this.time.hour = this.hour;
            this.time.minute = this.minute;
        }
        int viewCenterX = (getRight() - getLeft()) / 2;
        int viewCenterY = (getBottom() - getTop()) / 2;
        Drawable dial = this.clockDrawable;
        int h = dial.getIntrinsicHeight();
        int w = dial.getIntrinsicWidth();
        if (getRight() - getLeft() < w || getBottom() - getTop() < h) {
            float scale = Math.min(((float) (getRight() - getLeft())) / ((float) w), ((float) (getBottom() - getTop())) / ((float) h));
            canvas.save();
            canvas.scale(scale, scale, (float) viewCenterX, (float) viewCenterY);
        }
        if (this.isChange) {
            dial.setBounds(viewCenterX - (w / 2), viewCenterY - (h / 2), (w / 2) + viewCenterX, (h / 2) + viewCenterY);
        }
        dial.draw(canvas);
        canvas.save();
        canvas.rotate(((((float) this.time.hour) + (((float) this.time.minute) / 60.0f)) / 12.0f) * 360.0f, (float) viewCenterX, (float) viewCenterY);
        Drawable mHour = this.hourDrawable;
        int h2 = mHour.getIntrinsicHeight();
        int w2 = mHour.getIntrinsicWidth();
        if (this.isChange) {
            mHour.setBounds(viewCenterX - (w2 / 2), viewCenterY - (h2 / 2), (w2 / 2) + viewCenterX, (h2 / 2) + viewCenterY);
        }
        mHour.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate(((((float) this.time.minute) + (((float) this.time.second) / 60.0f)) / 60.0f) * 360.0f, (float) viewCenterX, (float) viewCenterY);
        Drawable mMinute = this.minuteDrawable;
        if (this.isChange) {
            int w3 = mMinute.getIntrinsicWidth();
            int h3 = mMinute.getIntrinsicHeight();
            mMinute.setBounds(viewCenterX - (w3 / 2), viewCenterY - (h3 / 2), (w3 / 2) + viewCenterX, (h3 / 2) + viewCenterY);
        }
        mMinute.draw(canvas);
        canvas.restore();
        canvas.save();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.isChange = true;
        this.clockThread.start();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isChange = false;
    }
}
