package com.touchus.benchilauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.AbstraSlide;

public class MenuSlide extends AbstraSlide {
    public static final int STATE_DOWN0 = 0;
    public static final int STATE_DOWN1 = 1;
    public static final int STATE_HIDE_NEXT5V = 8;
    public static final int STATE_UP020 = 2;
    public static final int STATE_UP021 = 3;
    public static final int STATE_UP120 = 4;
    public static final int STATE_UP121 = 5;
    public static final int STATE_UP122 = 9;
    public static final int STATE_UP221 = 10;
    public static final int STATE_UP222 = 11;
    public static final int STATE_V421 = 6;
    public static final int STATE_V520 = 7;
    public static int mChildWithB = 230;
    public static int mChildWithS = 138;
    private static int pagesize = 5;
    int childNum = 10;
    public float mDownX;
    public float mDownY;
    public int mPage = 0;
    PageListener mPageListener;
    public float mStartX;

    public interface PageListener {
        void pageListener(int i);
    }

    public void setPageListener(PageListener listener) {
        this.mPageListener = listener;
    }

    public MenuSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                this.mStartX = ev.getX();
                this.mDownX = ev.getX();
                this.mDownY = ev.getY();
                break;
            case 2:
                float moveX = ev.getX();
                float moveY = ev.getY();
                if (Math.abs(moveX - this.mDownX) > Math.abs(moveY - this.mDownY)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void setPagesize(int mpagesize) {
        if (mpagesize != 5) {
            mChildWithS = 166;
            mChildWithB = 258;
            pagesize = 3;
        } else if (SysConst.UI_TYPE == 1) {
            mChildWithS = 145;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 1:
                float distance = ev.getX() - this.mStartX;
                switch (this.mPage) {
                    case 0:
                        if (distance >= -80.0f) {
                            smoothScrollXTo(0, 100);
                            if (this.mOnSlipeListener != null) {
                                this.mOnSlipeListener.lesten(2);
                                break;
                            }
                        } else {
                            smoothScrollXTo(mChildWithS * pagesize, 500);
                            this.mPage = 1;
                            if (this.mOnSlipeListener != null) {
                                this.mOnSlipeListener.lesten(3);
                            }
                            if (this.mPageListener != null) {
                                this.mPageListener.pageListener(1);
                                break;
                            }
                        }
                        break;
                    case 1:
                        if (distance <= 80.0f) {
                            smoothScrollXTo(mChildWithS * pagesize, 100);
                            this.mPage = 1;
                            if (this.mOnSlipeListener != null) {
                                this.mOnSlipeListener.lesten(5);
                                break;
                            }
                        } else {
                            smoothScrollXTo(0, 500);
                            this.mPage = 0;
                            if (this.mOnSlipeListener != null) {
                                this.mOnSlipeListener.lesten(4);
                            }
                            if (this.mPageListener != null) {
                                this.mPageListener.pageListener(0);
                                break;
                            }
                        }
                        break;
                    case 2:
                        if (distance <= 80.0f) {
                            smoothScrollXTo(mChildWithS * pagesize * 2, 100);
                            if (this.mOnSlipeListener != null) {
                                this.mOnSlipeListener.lesten(11);
                                break;
                            }
                        } else {
                            smoothScrollXTo(mChildWithS * pagesize, 500);
                            this.mPage = 1;
                            if (this.mOnSlipeListener != null) {
                                this.mOnSlipeListener.lesten(10);
                            }
                            if (this.mPageListener != null) {
                                this.mPageListener.pageListener(1);
                                break;
                            }
                        }
                        break;
                }
            case 2:
                float moveX = ev.getX();
                int diffX = (int) ((this.mDownX - moveX) + 0.5f);
                int finalX = getScrollX() + diffX;
                if (finalX <= (mChildWithS * pagesize) + 20) {
                    if (finalX >= -20) {
                        scrollBy(diffX, 0);
                        this.mDownX = moveX;
                        break;
                    } else {
                        scrollTo(-20, 0);
                        break;
                    }
                } else {
                    scrollTo((mChildWithS * pagesize) + 20, 0);
                    break;
                }
        }
        return true;
    }

    public void slide2Page(int page, int duration) {
        switch (page) {
            case 0:
                smoothScrollXTo(0, duration);
                break;
            case 1:
                smoothScrollXTo(mChildWithS * pagesize, duration);
                break;
            case 2:
                smoothScrollXTo(mChildWithS * pagesize * 2, duration);
                break;
        }
        this.mPage = page;
    }
}
