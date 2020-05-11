package com.touchus.benchilauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.base.AbstraSlide;

public class LeftStateSlide extends AbstraSlide {
    public static int mChildWith = 475;
    public static int mPage = 0;
    public boolean iHandleTouchEvent = true;
    public float mDownX;
    public float mDownY;
    private boolean mScrolling;
    public int mSpace = 20;
    public float mStartX;

    public LeftStateSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.iHandleTouchEvent) {
            switch (event.getAction()) {
                case 0:
                    this.mStartX = event.getX();
                    this.mDownX = event.getX();
                    this.mDownY = event.getY();
                    break;
                case 1:
                    float distance = this.mDownX - this.mStartX;
                    switch (mPage) {
                        case 0:
                            if (distance > ((float) (-this.mSpace))) {
                                smoothScrollXTo(0, 100);
                            }
                            if (distance < ((float) (-this.mSpace))) {
                                smoothScrollXTo(mChildWith * 1, 300);
                                mPage = 1;
                                break;
                            }
                            break;
                        case 1:
                            if (distance <= ((float) this.mSpace)) {
                                if (distance >= ((float) (-this.mSpace))) {
                                    smoothScrollXTo(mChildWith * 1, 100);
                                    break;
                                } else {
                                    mPage = 2;
                                    smoothScrollXTo(mChildWith * 2, 100);
                                    break;
                                }
                            } else {
                                smoothScrollXTo(0, 300);
                                mPage = 0;
                                break;
                            }
                        case 2:
                            if (this.mDownX - this.mStartX <= ((float) this.mSpace)) {
                                if (distance >= ((float) (-this.mSpace))) {
                                    if (this.mDownX - this.mStartX < ((float) this.mSpace)) {
                                        smoothScrollXTo(mChildWith * 2, 100);
                                        break;
                                    }
                                } else {
                                    mPage = 3;
                                    smoothScrollXTo(mChildWith * 3, 100);
                                    break;
                                }
                            } else {
                                smoothScrollXTo(mChildWith * 1, 300);
                                mPage = 1;
                                break;
                            }
                            break;
                        case 3:
                            if (LauncherApplication.pageCount <= 4) {
                                if (this.mDownX - this.mStartX <= ((float) this.mSpace)) {
                                    if (this.mDownX - this.mStartX < ((float) this.mSpace)) {
                                        smoothScrollXTo(mChildWith * 3, 100);
                                        break;
                                    }
                                } else {
                                    smoothScrollXTo(mChildWith * 2, 300);
                                    mPage = 2;
                                    break;
                                }
                            } else if (this.mDownX - this.mStartX <= ((float) this.mSpace)) {
                                if (distance >= ((float) (-this.mSpace))) {
                                    if (this.mDownX - this.mStartX < ((float) this.mSpace)) {
                                        smoothScrollXTo(mChildWith * 3, 100);
                                        break;
                                    }
                                } else {
                                    mPage = 4;
                                    smoothScrollXTo(mChildWith * 4, 100);
                                    break;
                                }
                            } else {
                                smoothScrollXTo(mChildWith * 2, 300);
                                mPage = 2;
                                break;
                            }
                            break;
                        case 4:
                            if (LauncherApplication.pageCount != 4) {
                                if (this.mDownX - this.mStartX <= ((float) this.mSpace)) {
                                    if (this.mDownX - this.mStartX < ((float) this.mSpace)) {
                                        smoothScrollXTo(mChildWith * 4, 100);
                                        break;
                                    }
                                } else {
                                    smoothScrollXTo(mChildWith * 3, 300);
                                    mPage = 3;
                                    break;
                                }
                            } else {
                                smoothScrollXTo(mChildWith * 3, 300);
                                mPage = 3;
                                break;
                            }
                            break;
                    }
                case 2:
                    float moveX = event.getX();
                    int diffX = (int) ((this.mDownX - moveX) + 0.5f);
                    int finalX = getScrollX() + diffX;
                    if (finalX >= -20) {
                        if (finalX <= (mChildWith * (LauncherApplication.pageCount - 1)) + 20) {
                            scrollBy(diffX, 0);
                            this.mDownX = moveX;
                            break;
                        } else {
                            scrollTo((mChildWith * (LauncherApplication.pageCount - 1)) + 20, 0);
                            break;
                        }
                    } else {
                        scrollTo(-20, 0);
                        break;
                    }
            }
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mStartX = event.getX();
                this.mDownX = event.getX();
                this.mScrolling = false;
                break;
            case 1:
                this.mScrolling = false;
                break;
            case 2:
                if (Math.abs(this.mStartX - event.getX()) < ((float) ViewConfiguration.get(getContext()).getScaledTouchSlop())) {
                    this.mScrolling = false;
                    break;
                } else {
                    this.mScrolling = true;
                    break;
                }
        }
        return this.mScrolling;
    }

    public void changeToCurrentView() {
        scrollTo(mPage * 475, 0);
    }
}
