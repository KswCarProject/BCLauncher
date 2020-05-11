package com.touchus.benchilauncher.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.backaudio.android.driver.beans.CarRunInfo;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.views.CarWarnInfoView;
import com.touchus.benchilauncher.views.YiBiaoView;
import com.touchus.publicutils.sysconst.BenzModel;
import java.lang.ref.WeakReference;

public class YiBiaoFragment extends BaseFragment {
    private ImageView car_body;
    private ImageView car_bonnet;
    private CarWarnInfoView car_brake;
    private ImageView car_headlamp;
    private ImageView car_lamplet;
    private ImageView car_lb_door;
    private ImageView car_lf_door;
    private CarWarnInfoView car_outsidetemp;
    private ImageView car_rb_door;
    private ImageView car_rearbox;
    private CarWarnInfoView car_remainkon;
    private ImageView car_rf_door;
    private CarWarnInfoView car_safety_belt;
    private ImageView car_stoplight;
    private boolean iUpdate;
    private Launcher launcher;
    private LauncherApplication mApp;
    private Context mContext;
    private View mView;
    private FragmentManager manager;
    private MeterHandler meterHandler = new MeterHandler(this);
    private float rSpeed = 0.0f;
    private float speed = 0.0f;
    private float suduAngle = -129.0f;
    private YiBiaoView suduYiBiao;
    private LinearLayout warning_ll;
    private float zhuanSAngle = -143.0f;
    private YiBiaoView zhuanSuYiBiao;

    public static class MeterHandler extends Handler {
        private WeakReference<YiBiaoFragment> target;

        public MeterHandler(YiBiaoFragment instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.target.get() != null) {
                ((YiBiaoFragment) this.target.get()).handlerMsg(msg);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        if (this.iUpdate) {
            if (msg.what == 1008) {
                CarRunInfo info = (CarRunInfo) msg.getData().getParcelable(SysConst.FLAG_RUNNING_STATE);
                if (info != null) {
                    this.speed = (float) info.getCurSpeed();
                    this.rSpeed = (float) info.getRevolutions();
                    speedToAngle();
                    setYiBiaoAngle();
                    settingRunningInfo(info, (CarBaseInfo) null);
                }
            } else if (msg.what == 1007) {
                settingRunningInfo((CarRunInfo) null, (CarBaseInfo) msg.getData().getParcelable(SysConst.FLAG_CAR_BASE_INFO));
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.launcher = (Launcher) getActivity();
        this.mApp = (LauncherApplication) this.launcher.getApplication();
    }

    public void changeFragment(int rootID, Fragment to) {
        this.manager = this.launcher.getFragmentManager();
        FragmentTransaction transaction = this.manager.beginTransaction();
        transaction.replace(rootID, to);
        transaction.commit();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("", String.valueOf(getClass().getName()) + " onCreateView ");
        this.mView = inflater.inflate(R.layout.fragment_yibiao, (ViewGroup) null);
        initView();
        return this.mView;
    }

    public void onStart() {
        Log.e("", String.valueOf(getClass().getName()) + " onStart ");
        if (this.mApp.isSUV) {
            this.car_body.setImageResource(R.drawable.suvdoor_normal);
        } else {
            this.car_body.setImageResource(R.drawable.cardoor_normal);
        }
        super.onStart();
    }

    public void onStop() {
        Log.e("", String.valueOf(getClass().getName()) + " onStop = ");
        super.onStop();
    }

    public void onResume() {
        Log.e("", String.valueOf(getClass().getName()) + " onResume ");
        super.onResume();
        initData();
    }

    private void initView() {
        this.suduYiBiao = (YiBiaoView) this.mView.findViewById(R.id.yibiao_sudu);
        this.zhuanSuYiBiao = (YiBiaoView) this.mView.findViewById(R.id.yibiao_zhuansu);
        this.car_body = (ImageView) this.mView.findViewById(R.id.car_body);
        this.car_headlamp = (ImageView) this.mView.findViewById(R.id.car_headlamp);
        this.car_lamplet = (ImageView) this.mView.findViewById(R.id.car_lamplet);
        this.car_bonnet = (ImageView) this.mView.findViewById(R.id.car_bonnet);
        this.car_lf_door = (ImageView) this.mView.findViewById(R.id.car_lf_door);
        this.car_rf_door = (ImageView) this.mView.findViewById(R.id.car_rf_door);
        this.car_lb_door = (ImageView) this.mView.findViewById(R.id.car_lb_door);
        this.car_rb_door = (ImageView) this.mView.findViewById(R.id.car_rb_door);
        this.car_rearbox = (ImageView) this.mView.findViewById(R.id.car_rearbox);
        this.car_stoplight = (ImageView) this.mView.findViewById(R.id.car_stoplight);
        this.car_remainkon = (CarWarnInfoView) this.mView.findViewById(R.id.car_remainkon);
        this.car_safety_belt = (CarWarnInfoView) this.mView.findViewById(R.id.car_safety_belt);
        this.car_outsidetemp = (CarWarnInfoView) this.mView.findViewById(R.id.car_outsidetemp);
        this.car_brake = (CarWarnInfoView) this.mView.findViewById(R.id.car_brake);
        this.warning_ll = (LinearLayout) this.mView.findViewById(R.id.warning_ll);
        this.car_remainkon.setCarInfo(false, R.drawable.car_remainkon_n, String.format(getString(R.string.remainder_range), new Object[]{0}));
        this.car_brake.setCarInfo(true, R.drawable.car_brake_h, getString(R.string.handbrake_on));
        this.car_safety_belt.setCarInfo(false, R.drawable.car_safety_belt_n, getString(R.string.safetybelt_off));
        setImageState(this.car_headlamp, false);
        setImageState(this.car_lamplet, false);
        setImageState(this.car_bonnet, false);
        setImageState(this.car_lf_door, false);
        setImageState(this.car_rf_door, false);
        setImageState(this.car_lb_door, false);
        setImageState(this.car_rb_door, false);
        setImageState(this.car_rearbox, false);
        setImageState(this.car_stoplight, false);
        if (BenzModel.benzCan != BenzModel.EBenzCAN.XBS || BenzModel.benzTpye == BenzModel.EBenzTpye.GLA) {
            this.car_remainkon.setVisibility(8);
            this.car_safety_belt.setVisibility(0);
            return;
        }
        this.car_remainkon.setVisibility(0);
        this.car_safety_belt.setVisibility(8);
    }

    private void setImageState(ImageView imageView, boolean isopen) {
        if (isopen) {
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(4);
        }
    }

    private void initData() {
        setYiBiaoAngle();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void setSpeed() {
        this.speed = (float) this.mApp.speed;
        this.rSpeed = (float) this.mApp.rSpeed;
        speedToAngle();
        setYiBiaoAngle();
        settingRunningInfo(this.mApp.carRunInfo, this.mApp.carBaseInfo);
    }

    private void settingRunningInfo(CarRunInfo runInfo, CarBaseInfo baseInfo) {
        if (getActivity() != null) {
            if (runInfo != null) {
                setRuningInfo(runInfo);
            }
            if (baseInfo != null) {
                setBaseinfo(baseInfo);
            }
        }
    }

    private void setBaseinfo(CarBaseInfo baseInfo) {
        if (baseInfo.isiBrake()) {
            this.car_brake.setCarInfo(true, R.drawable.car_brake_h, getString(R.string.handbrake_on));
        } else {
            this.car_brake.setCarInfo(false, R.drawable.car_brake_n, getString(R.string.handbrake_off));
        }
        if (baseInfo.isiSafetyBelt()) {
            this.car_safety_belt.setCarInfo(true, R.drawable.car_safety_belt_h, getString(R.string.safetybelt_off));
        } else {
            this.car_safety_belt.setCarInfo(false, R.drawable.car_safety_belt_n, getString(R.string.safetybelt_on));
        }
        setImageState(this.car_headlamp, baseInfo.isiDistantLightOpen());
        setImageState(this.car_lamplet, baseInfo.isiNearLightOpen());
        setImageState(this.car_bonnet, baseInfo.isiFront());
        setImageState(this.car_lf_door, baseInfo.isiLeftFrontOpen());
        setImageState(this.car_rf_door, baseInfo.isiRightFrontOpen());
        setImageState(this.car_lb_door, baseInfo.isiLeftBackOpen());
        setImageState(this.car_rb_door, baseInfo.isiRightBackOpen());
        setImageState(this.car_rearbox, baseInfo.isiBack());
    }

    private void setRuningInfo(CarRunInfo runInfo) {
        if (runInfo.getMileage() >= 2047 || runInfo.getMileage() <= 0) {
            this.car_remainkon.setCarInfo(false, R.drawable.car_remainkon_n, String.format(getString(R.string.remainder_range), new Object[]{"----"}));
            return;
        }
        this.car_remainkon.setCarInfo(false, R.drawable.car_remainkon_n, String.format(getString(R.string.remainder_range), new Object[]{Integer.valueOf(runInfo.getMileage())}));
    }

    private void setYiBiaoAngle() {
        this.suduYiBiao.setAngle(this.suduAngle);
        this.zhuanSuYiBiao.setAngle(this.zhuanSAngle);
    }

    private void speedToAngle() {
        this.suduAngle = this.speed - 130.0f;
        if (this.rSpeed < 0.0f || this.rSpeed > 8000.0f) {
            this.zhuanSAngle = -143.0f;
        } else {
            this.zhuanSAngle = ((this.rSpeed * 360.0f) / 10000.0f) - 144.0f;
        }
    }

    public boolean onBack() {
        return false;
    }

    public void onHiddenChanged(boolean hidden) {
        Log.e("", String.valueOf(getClass().getName()) + " onHiddenChanged = " + hidden);
        if (hidden) {
            this.iUpdate = false;
            this.mApp.unregisterHandler(this.meterHandler);
            setSpeed();
        } else {
            this.iUpdate = true;
            this.mApp.registerHandler(this.meterHandler);
            if (this.mApp.isSUV) {
                this.car_body.setImageResource(R.drawable.suvdoor_normal);
            } else {
                this.car_body.setImageResource(R.drawable.cardoor_normal);
            }
        }
        super.onHiddenChanged(hidden);
    }
}
