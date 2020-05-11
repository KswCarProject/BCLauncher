package com.touchus.benchilauncher.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.SpUtilK;

public class CrossView extends View {
    private Point cp;
    private boolean drawLine = true;
    private Boolean isCilkshizi = false;
    private Bitmap mBitmap;
    private Context mContext;
    private float[] mLineA;
    private float[] mLineB;
    private Paint paint;
    public float parantX;
    public float parantY;
    int sen = 50;

    public CrossView(Context context, View parent) {
        super(context);
        this.mContext = context;
        init(parent);
    }

    public void init(View parent) {
        if (this.paint == null || this.cp == null) {
            this.paint = new Paint();
            this.cp = new Point();
        }
        setFocusable(true);
        this.parantY = (float) parent.getHeight();
        this.parantX = (float) parent.getWidth();
        this.mLineA = new float[]{0.0f, this.parantY / 2.0f, this.parantX, this.parantY / 2.0f};
        this.mLineB = new float[]{this.parantX / 2.0f, this.parantY, this.parantX / 2.0f, 0.0f};
        this.cp.x = (int) ((this.parantX / 2.0f) + 0.5f);
        this.cp.y = (int) ((this.parantY / 2.0f) + 0.5f);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        if (this.drawLine) {
            this.paint.setColor(-1);
            this.paint.setStrokeWidth(5.0f);
        }
        canvas.drawLine(this.mLineA[0], this.mLineA[1], this.mLineA[2], this.mLineA[3], this.paint);
        canvas.drawLine(this.mLineB[0], this.mLineB[1], this.mLineB[2], this.mLineB[3], this.paint);
        this.paint.setColor(-1);
        if (this.isCilkshizi.booleanValue()) {
            this.mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zuoyou_shizi_h);
        } else {
            this.mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zuoyou_shizi_n);
        }
        canvas.drawBitmap(this.mBitmap, (float) (this.cp.x - 16), (float) (this.cp.y - 17), this.paint);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) (event.getX() + 0.5f);
        int Y = (int) (event.getY() + 0.5f);
        switch (event.getAction()) {
            case 0:
            case 2:
                this.isCilkshizi = true;
                Log.d("XY: ", "X" + X + "-----" + "Y" + Y);
                if (((float) X) >= this.parantX) {
                    X = (int) this.parantX;
                } else if (X <= 0) {
                    X = 0;
                }
                if (((float) Y) >= this.parantY) {
                    Y = (int) this.parantY;
                } else if (Y <= 0) {
                    Y = 0;
                }
                this.drawLine = false;
                this.cp.x = X;
                this.cp.y = Y;
                this.mLineA[1] = (float) Y;
                this.mLineA[3] = (float) Y;
                this.mLineB[0] = (float) X;
                this.mLineB[2] = (float) X;
                break;
            case 1:
                this.drawLine = true;
                this.isCilkshizi = false;
                break;
        }
        invalidate();
        return true;
    }

    public float getPravX() {
        return (float) this.cp.x;
    }

    public float getPravY() {
        return (float) this.cp.y;
    }

    public void onCilkCrossView(int x, int y) {
        int X = (int) ((this.parantX / 2.0f) + ((float) (x * 7)));
        int Y = (int) ((this.parantY / 2.0f) + ((float) (y * 9)));
        if (((float) X) >= this.parantX) {
            X = (int) (this.parantX + 0.5f);
        } else if (X <= 0) {
            X = 0;
        }
        if (((float) Y) >= this.parantY) {
            Y = (int) (this.parantY + 0.5f);
        } else if (Y <= 0) {
            Y = 0;
        }
        this.drawLine = true;
        this.cp.x = X;
        this.cp.y = Y;
        this.mLineA[1] = (float) Y;
        this.mLineA[3] = (float) Y;
        this.mLineB[0] = (float) X;
        this.mLineB[2] = (float) X;
        invalidate();
    }

    public void setIsCilkshizi(Boolean flag) {
        this.isCilkshizi = flag;
        new SpUtilK(this.mContext).putBoolean(SysConst.IS_CILK_SHIZI, this.isCilkshizi.booleanValue());
    }
}
