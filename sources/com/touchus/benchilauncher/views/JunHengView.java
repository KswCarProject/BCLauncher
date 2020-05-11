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
public class JunHengView extends View {
    private Drawable clockDrawable;
    private Thread clockThread;
    private Drawable hourDrawable;
    /* access modifiers changed from: private */
    public boolean isChange;
    public boolean istuodong;
    private int mViewCenterX;
    private int mViewCenterY;
    int mun;
    private Paint paint;
    private Time time;

    public JunHengView(Context context) {
        this(context, (AttributeSet) null);
    }

    public JunHengView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void settingCurrentClockPic(boolean flag) {
        if (flag) {
            this.clockDrawable = getResources().getDrawable(R.drawable.junheng_yuandi_h);
            this.hourDrawable = getResources().getDrawable(R.drawable.junheng_zhizhen_h);
            return;
        }
        this.clockDrawable = getResources().getDrawable(R.drawable.junheng_yuandi_n);
        this.hourDrawable = getResources().getDrawable(R.drawable.junheng_zhizhen_n);
    }

    public JunHengView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.istuodong = false;
        this.mun = 0;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyClockStyleable, defStyle, 0);
        this.clockDrawable = ta.getDrawable(0);
        this.hourDrawable = ta.getDrawable(1);
        ta.recycle();
        this.paint = new Paint();
        this.paint.setColor(Color.parseColor("#000000"));
        this.paint.setTypeface(Typeface.DEFAULT_BOLD);
        this.paint.setFakeBoldText(true);
        this.paint.setAntiAlias(true);
        this.time = new Time();
        this.clockThread = new Thread() {
            public void run() {
                while (JunHengView.this.isChange) {
                    JunHengView.this.postInvalidate();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.time.setToNow();
        this.mViewCenterX = (getRight() - getLeft()) / 2;
        this.mViewCenterY = (getBottom() - getTop()) / 2;
        Drawable dial = this.clockDrawable;
        int h = dial.getIntrinsicHeight();
        int w = dial.getIntrinsicWidth();
        if (getRight() - getLeft() < w || getBottom() - getTop() < h) {
            float scale = Math.min(((float) (getRight() - getLeft())) / ((float) w), ((float) (getBottom() - getTop())) / ((float) h));
            canvas.save();
            canvas.scale(scale, scale, (float) this.mViewCenterX, (float) this.mViewCenterY);
        }
        if (this.isChange) {
            dial.setBounds(this.mViewCenterX - (w / 2), this.mViewCenterY - (h / 2), this.mViewCenterX + (w / 2), this.mViewCenterY + (h / 2));
        }
        dial.draw(canvas);
        canvas.save();
        canvas.rotate(15.0f * ((float) setMun(this.mun)), (float) this.mViewCenterX, (float) this.mViewCenterY);
        Drawable mHour = this.hourDrawable;
        int h2 = mHour.getIntrinsicHeight();
        int w2 = mHour.getIntrinsicWidth();
        if (this.isChange) {
            mHour.setBounds(this.mViewCenterX - (w2 / 2), (this.mViewCenterY - h2) + 70, this.mViewCenterX + (w2 / 2), this.mViewCenterY + 70);
        }
        mHour.draw(canvas);
        canvas.restore();
        canvas.save();
    }

    public int setMun(int mun1) {
        if (mun1 >= 0 && mun1 <= 10) {
            this.mun = mun1;
        } else if (mun1 >= -10 && mun1 <= -1) {
            this.mun = mun1 + 24;
        }
        return this.mun;
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
