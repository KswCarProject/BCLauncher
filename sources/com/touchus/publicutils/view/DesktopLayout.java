package com.touchus.publicutils.view;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.touchus.publicutils.R;

public class DesktopLayout extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    public static DesktopLayout instance;
    static WindowManager mWindowManager;
    /* access modifiers changed from: private */
    public ImageView back;
    Context context;
    Runnable hideBgRun = new Runnable() {
        public void run() {
            DesktopLayout.this.view.setBackgroundResource(R.drawable.float_bg2);
        }
    };
    Runnable hideBtnRun = new Runnable() {
        public void run() {
            boolean unused = DesktopLayout.this.hideBtn();
        }
    };
    /* access modifiers changed from: private */
    public ImageView home;
    boolean isHide = false;
    Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public ImageView right_push;
    /* access modifiers changed from: private */
    public View view;

    public static void show(Context context2) {
        if (instance == null) {
            instance = new DesktopLayout(context2);
            try {
                mWindowManager.addView(instance, getLayout());
            } catch (Exception e) {
                Log.d("launcherlog", "canbox:error:mWindowManager.addView(mDesktopLayout)");
            }
        }
    }

    public static void hide() {
        if (instance != null) {
            try {
                mWindowManager.removeViewImmediate(instance);
                instance = null;
            } catch (Exception e) {
                Log.d("launcherlog", "canbox:error:mWindowManager.removeView(mDesktopLayout)");
            }
        }
    }

    private DesktopLayout(Context context2) {
        super(context2);
        this.context = context2;
        mWindowManager = (WindowManager) context2.getSystemService("window");
        setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        this.view = LayoutInflater.from(context2).inflate(R.layout.desklayout, (ViewGroup) null);
        this.right_push = (ImageView) this.view.findViewById(R.id.right_push);
        this.home = (ImageView) this.view.findViewById(R.id.home);
        this.back = (ImageView) this.view.findViewById(R.id.back);
        initEvent();
        addView(this.view);
    }

    private void initEvent() {
        this.right_push.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("DesktopLayout", "MotionEvent event = " + event);
                if (event.getAction() == 0) {
                    DesktopLayout.this.view.setBackgroundResource(R.drawable.float_bg1);
                    DesktopLayout.this.isHide = false;
                    DesktopLayout.this.setBtnhide();
                    DesktopLayout.this.right_push.setVisibility(8);
                    DesktopLayout.this.home.setVisibility(0);
                    DesktopLayout.this.back.setVisibility(0);
                    return true;
                }
                event.getAction();
                return true;
            }
        });
        setOnTouchListener(this);
        this.right_push.setVisibility(8);
        this.home.setOnClickListener(this);
        this.back.setOnClickListener(this);
        setBtnhide();
    }

    public void onClick(View view2) {
        int id = view2.getId();
        if (id == R.id.home) {
            sendKeyeventToSystem(3);
            hideBtn();
        } else if (id == R.id.back) {
            sendKeyeventToSystem(4);
            setBtnhide();
        }
    }

    /* access modifiers changed from: private */
    public void setBtnhide() {
        this.mHandler.removeCallbacks(this.hideBgRun);
        this.mHandler.removeCallbacks(this.hideBtnRun);
        this.mHandler.postDelayed(this.hideBgRun, 3000);
        this.mHandler.postDelayed(this.hideBtnRun, 3000);
    }

    /* access modifiers changed from: private */
    public boolean hideBtn() {
        Log.e("DesktopLayout", "isHide = " + this.isHide);
        if (this.isHide) {
            return false;
        }
        this.isHide = true;
        this.mHandler.removeCallbacks(this.hideBtnRun);
        this.right_push.setVisibility(0);
        this.home.setVisibility(8);
        this.back.setVisibility(8);
        return this.isHide;
    }

    private void sendKeyeventToSystem(int keyeventCode) {
        if (keyeventCode >= 0) {
            final int keycode = keyeventCode;
            new Thread(new Runnable() {
                public void run() {
                    new Instrumentation().sendKeyDownUpSync(keycode);
                }
            }).start();
        }
    }

    private static WindowManager.LayoutParams getLayout() {
        WindowManager.LayoutParams mLayout = new WindowManager.LayoutParams();
        mLayout.type = 2003;
        mLayout.flags = 262184;
        mLayout.format = 1;
        mLayout.gravity = 83;
        mLayout.width = -2;
        mLayout.height = -2;
        return mLayout;
    }

    public boolean onTouch(View v, MotionEvent event) {
        Log.e("DesktopLayout", "out MotionEvent event = " + event);
        if (event.getAction() == 4) {
            return hideBtn();
        }
        return false;
    }
}
