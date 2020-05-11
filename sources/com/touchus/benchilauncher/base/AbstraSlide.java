package com.touchus.benchilauncher.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class AbstraSlide extends ViewGroup {
    public View mChild;
    public OnSlipeListener mOnSlipeListener;
    public Scroller mScroller;

    public interface OnSlipeListener {
        void lesten(int i);
    }

    public AbstraSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mScroller = new Scroller(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mChild = getChildAt(0);
        ViewGroup.LayoutParams layoutParams = this.mChild.getLayoutParams();
        int width = layoutParams.width;
        int hight = layoutParams.height;
        this.mChild.measure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(hight, 1073741824));
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mChild.layout(0, 0, this.mChild.getMeasuredWidth(), this.mChild.getMeasuredHeight());
    }

    public void smoothScrollXTo(int endX, int duration) {
        smoothScrollTo(endX, 0, duration);
    }

    public void smoothScrollYTo(int endY, int duration) {
        smoothScrollTo(0, endY, duration);
    }

    public void smoothScrollTo(int endX, int endY, int dutation) {
        int startX = getScrollX();
        int startY = getScrollY();
        this.mScroller.startScroll(startX, startY, endX - startX, endY - startY, dutation);
        invalidate();
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            invalidate();
        }
    }

    public void setOnSlideListener(OnSlipeListener l) {
        this.mOnSlipeListener = l;
    }
}
