package com.touchus.benchilauncher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.touchus.benchilauncher.R;

public class YiBiaoView extends View {
    private float angle;
    private Drawable bgImage;
    private boolean isChange;
    private Drawable neiYuanView;
    private Paint paint;
    private Drawable pointView;
    private int viewCenterX;
    private int viewCenterY;

    public YiBiaoView(Context context) {
        this(context, (AttributeSet) null);
    }

    public YiBiaoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YiBiaoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.viewCenterX = 189;
        this.viewCenterY = 189;
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyYibiaoStyleable, defStyle, 0);
        this.bgImage = ta.getDrawable(0);
        this.pointView = ta.getDrawable(2);
        this.neiYuanView = ta.getDrawable(1);
        ta.recycle();
        this.paint = new Paint();
        this.paint.setColor(Color.parseColor("#000000"));
        this.paint.setTypeface(Typeface.DEFAULT_BOLD);
        this.paint.setFakeBoldText(true);
        this.paint.setAntiAlias(true);
    }

    public void setAngle(float angle2) {
        this.angle = angle2;
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable dial = this.bgImage;
        int h = dial.getIntrinsicHeight();
        int w = dial.getIntrinsicWidth();
        if (this.isChange) {
            dial.setBounds(this.viewCenterX - (w / 2), this.viewCenterY - (h / 2), this.viewCenterX + (w / 2), this.viewCenterY + (h / 2));
        }
        dial.draw(canvas);
        canvas.save();
        canvas.rotate(this.angle, (float) this.viewCenterX, (float) this.viewCenterY);
        Drawable mHour = this.pointView;
        int h2 = mHour.getIntrinsicHeight();
        int w2 = mHour.getIntrinsicWidth();
        if (this.isChange) {
            mHour.setBounds(this.viewCenterX - (w2 / 2), this.viewCenterY - (h2 / 2), this.viewCenterX + (w2 / 2), this.viewCenterY + (h2 / 2));
        }
        mHour.draw(canvas);
        canvas.restore();
        canvas.save();
        Drawable neiYuan = this.neiYuanView;
        int h3 = neiYuan.getIntrinsicHeight();
        int w3 = neiYuan.getIntrinsicWidth();
        neiYuan.setBounds(this.viewCenterX - (w3 / 2), this.viewCenterY - (h3 / 2), this.viewCenterX + (w3 / 2), this.viewCenterY + (h3 / 2));
        neiYuan.draw(canvas);
        canvas.restore();
        canvas.save();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        Log.e("YiBiaoF", "onAttachedToWindow");
        super.onAttachedToWindow();
        this.isChange = true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        Log.e("YiBiaoF", "onDetachedFromWindow");
        super.onDetachedFromWindow();
        this.isChange = false;
    }
}
