package com.touchus.benchilauncher.activity.main.left;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.beans.CarRunInfo;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.publicutils.sysconst.BenzModel;
import java.lang.ref.WeakReference;

public class RunningFrag extends Fragment {
    private LinearLayout curSpeed_ll;
    private LauncherApplication mApp;
    private RunningHandler mHandler = new RunningHandler(this);
    private TextView mRemain;
    private TextView mSpeed;
    private LinearLayout mSpeedLayout;
    private TextView mSum;
    private TextView mTemperature;
    private LinearLayout mileage_ll;
    private TextView mzhuansu;
    private TextView running_speed_none;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.main_left_running, container, false);
        findID(mView);
        init();
        return mView;
    }

    public void onStart() {
        settingRunningInfo(this.mApp.carRunInfo);
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mApp.unregisterHandler(this.mHandler);
    }

    private void findID(View mView) {
        this.mSpeed = (TextView) mView.findViewById(R.id.running_speed);
        this.running_speed_none = (TextView) mView.findViewById(R.id.running_speed_none);
        this.mRemain = (TextView) mView.findViewById(R.id.running_remain);
        this.mSum = (TextView) mView.findViewById(R.id.running_sum);
        this.mTemperature = (TextView) mView.findViewById(R.id.running_temperature);
        this.mzhuansu = (TextView) mView.findViewById(R.id.running_zhuansu);
        this.mSpeedLayout = (LinearLayout) mView.findViewById(R.id.running_speed_ll);
        this.curSpeed_ll = (LinearLayout) mView.findViewById(R.id.curSpeed_ll);
        this.mileage_ll = (LinearLayout) mView.findViewById(R.id.mileage_ll);
        this.mSpeedLayout.setVisibility(8);
        Log.e("", "BenzModel.benzTpye = " + BenzModel.benzTpye);
    }

    private void init() {
        this.mApp = (LauncherApplication) getActivity().getApplication();
        this.mApp.registerHandler(this.mHandler);
    }

    /* access modifiers changed from: private */
    public void settingRunningInfo(CarRunInfo info) {
        if (BenzModel.benzCan != BenzModel.EBenzCAN.XBS || BenzModel.benzTpye == BenzModel.EBenzTpye.GLA) {
            this.mileage_ll.setVisibility(8);
            this.curSpeed_ll.setVisibility(0);
        } else {
            this.mileage_ll.setVisibility(0);
            this.curSpeed_ll.setVisibility(8);
        }
        if (info == null) {
            this.running_speed_none.setText("----");
            this.mRemain.setText("----");
            this.mSum.setText("----");
            this.mTemperature.setText("----");
        } else if (BenzModel.benzCan != BenzModel.EBenzCAN.XBS || BenzModel.benzTpye == BenzModel.EBenzTpye.GLA) {
            if (info.getCurSpeed() >= 0) {
                this.mSpeedLayout.setVisibility(0);
                this.running_speed_none.setVisibility(4);
                this.mSpeed.setText(new StringBuilder(String.valueOf(info.getCurSpeed())).toString());
            } else {
                this.mSpeedLayout.setVisibility(4);
                this.running_speed_none.setVisibility(0);
                this.running_speed_none.setText("----");
            }
            if (info.getOutsideTemp() <= -40.0d || info.getOutsideTemp() >= 87.5d) {
                this.mTemperature.setText("----");
            } else {
                this.mTemperature.setText(String.valueOf(info.getOutsideTemp()) + " ℃");
            }
            if (info.getTotalMileage() > 0) {
                this.mSum.setText(String.valueOf(info.getTotalMileage() / 10) + " km");
            } else {
                this.mSum.setText("----");
            }
        } else if (info.getMileage() >= 2047 || info.getMileage() <= 0) {
            this.running_speed_none.setText("----");
            this.mRemain.setText("----");
            this.mSum.setText("----");
            this.mzhuansu.setText("----");
            this.mTemperature.setText("----");
        } else {
            this.mSpeed.setText(new StringBuilder(String.valueOf(info.getCurSpeed())).toString());
            this.mRemain.setText(String.valueOf(info.getMileage()) + " km");
            this.mzhuansu.setText(String.valueOf(info.getRevolutions()) + " rpm");
            this.mSum.setText(String.valueOf(info.getTotalMileage() / 10) + " km");
            this.mTemperature.setText(String.valueOf(info.getOutsideTemp()) + " ℃");
        }
    }

    public static class RunningHandler extends Handler {
        private WeakReference<RunningFrag> target;

        public RunningHandler(RunningFrag instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null && msg.what == 1008) {
                ((RunningFrag) this.target.get()).settingRunningInfo((CarRunInfo) msg.getData().getParcelable(SysConst.FLAG_RUNNING_STATE));
            }
        }
    }
}
