package com.touchus.benchilauncher.activity.main.left;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.benchilauncher.views.ClockView;
import com.touchus.publicutils.utils.UtilTools;

public class LeftClockFrag extends Fragment implements View.OnClickListener {
    private LauncherApplication app;
    /* access modifiers changed from: private */
    public int clickCount = 0;
    private ClockView clockView;
    Handler mHandler = new Handler();
    Thread resetClickCountThread = new Thread(new Runnable() {
        public void run() {
            LeftClockFrag.this.clickCount = 0;
        }
    });

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_left_clock, container, false);
        this.clockView = (ClockView) mView.findViewById(R.id.clock);
        this.clockView.setOnClickListener(this);
        this.app = (LauncherApplication) getActivity().getApplication();
        return mView;
    }

    public void onClick(View v) {
        Log.e("test", "click");
        if (!UtilTools.isFastDoubleClick()) {
            this.clickCount = 0;
        } else {
            this.clickCount++;
            if (this.clickCount >= 4) {
                this.clickCount = 0;
                if (!this.app.isTestOpen) {
                    ToastTool.showShortToast(getActivity(), "test open");
                    this.app.isTestOpen = true;
                    LauncherApplication.mSpUtils.putBoolean("test", this.app.isTestOpen);
                } else {
                    ToastTool.showShortToast(getActivity(), "test close");
                    this.app.isTestOpen = false;
                    LauncherApplication.mSpUtils.putBoolean("test", this.app.isTestOpen);
                }
            }
        }
        if (this.clickCount != 0) {
            this.mHandler.removeCallbacks(this.resetClickCountThread);
            this.mHandler.postDelayed(this.resetClickCountThread, 1000);
        }
    }

    public void updateCurrentTime() {
        if (getActivity() != null) {
            this.clockView.setTime(true, this.app.hour, this.app.min);
        }
    }

    public void onStart() {
        this.clockView.setTime(true, this.app.hour, this.app.min);
        super.onStart();
    }
}
