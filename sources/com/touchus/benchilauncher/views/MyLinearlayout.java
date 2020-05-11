package com.touchus.benchilauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import com.touchus.benchilauncher.LauncherApplication;

public class MyLinearlayout extends LinearLayout {
    private LauncherApplication app;
    private Context mcontext;

    public MyLinearlayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public MyLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearlayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mcontext = context;
        this.app = (LauncherApplication) this.mcontext.getApplicationContext();
        Log.e("", "listenlog MyLinearlayout creat:");
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
