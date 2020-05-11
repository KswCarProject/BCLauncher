package com.touchus.benchilauncher.activity.main;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.beans.AirInfo;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseHandlerFragment;
import com.touchus.publicutils.utils.UtilTools;

public class ButtomBar extends BaseHandlerFragment implements View.OnClickListener {
    TextView air_state;
    private LauncherApplication app;
    private ImageButton backIbtn;
    private ImageButton homeIbtn;
    ImageView mAc;
    ImageView mAirfan;
    ImageView mAuto;
    Launcher mMainActivity;
    ImageView mMax;
    TextView mTempL;
    TextView mTempR;
    private ImageButton screenControlIbtn;
    /* access modifiers changed from: private */
    public LinearLayout tempLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainActivity = (Launcher) getActivity();
        View mView = View.inflate(this.mMainActivity, R.layout.main_bottom_bar, (ViewGroup) null);
        this.homeIbtn = (ImageButton) mView.findViewById(R.id.buttom_home_img);
        this.backIbtn = (ImageButton) mView.findViewById(R.id.buttom_back);
        this.screenControlIbtn = (ImageButton) mView.findViewById(R.id.button_daytime);
        this.mAuto = (ImageView) mView.findViewById(R.id.buttom_auto);
        this.mAc = (ImageView) mView.findViewById(R.id.button_ac);
        this.mAirfan = (ImageView) mView.findViewById(R.id.buttom_airfan);
        this.mMax = (ImageView) mView.findViewById(R.id.buttom_max);
        this.air_state = (TextView) mView.findViewById(R.id.air_state);
        this.mTempL = (TextView) mView.findViewById(R.id.buttom_temp_m);
        this.mTempR = (TextView) mView.findViewById(R.id.buttom_temp_r);
        this.tempLayout = (LinearLayout) mView.findViewById(R.id.tempLayout);
        this.app = (LauncherApplication) getActivity().getApplication();
        this.homeIbtn.setOnClickListener(this);
        this.backIbtn.setOnClickListener(this);
        this.screenControlIbtn.setOnClickListener(this);
        return mView;
    }

    public void onStart() {
        handleAirInfo(this.app.airInfo);
        super.onStart();
    }

    public void onClick(View v) {
        if (this.app.service.iIsInAndroid) {
            switch (v.getId()) {
                case R.id.buttom_home_img:
                    handleHomeAction();
                    return;
                case R.id.buttom_back:
                    handleBackAction();
                    return;
                case R.id.button_daytime:
                    if (!UtilTools.isFastDoubleClick()) {
                        this.app.closeOrWakeupScreen(true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void settingTempLayoutHideOrShow(final boolean iShow) {
        if (getActivity() != null) {
            if (!iShow) {
                this.tempLayout.setVisibility(iShow ? 0 : 8);
            } else {
                this.mMyHandler.postDelayed(new Runnable() {
                    public void run() {
                        ButtomBar.this.tempLayout.setVisibility(iShow ? 0 : 8);
                    }
                }, 500);
            }
        }
    }

    private void setAuto(boolean iFlatWind, boolean iDownWind, boolean iMaxFrontWind, boolean iFrontWind) {
        stateChanged();
        if (iFlatWind && iDownWind && iFrontWind) {
            this.mAuto.setImageResource(R.drawable.buttom_bar_front_demist_front_down_wind);
        } else if (iFlatWind && iDownWind && !iFrontWind) {
            this.mAuto.setImageResource(R.drawable.buttom_bar_front_down_wind);
        } else if (iFlatWind && !iDownWind && !iFrontWind) {
            this.mAuto.setImageResource(R.drawable.buttom_bar_front_wind);
        } else if (!iFlatWind && iDownWind && !iFrontWind) {
            this.mAuto.setImageResource(R.drawable.buttom_bar_down_wind);
        } else if (!iFlatWind && iDownWind && iFrontWind) {
            this.mAuto.setImageResource(R.drawable.buttom_bar_front_demist_down_wind);
        } else if (iFlatWind && !iDownWind && iFrontWind) {
            this.mAuto.setImageResource(R.drawable.buttom_bar_front_demist_front_wind);
        } else if (!iFlatWind && !iDownWind && iFrontWind) {
            this.mAuto.setImageResource(R.drawable.buttom_bar_front_demist);
        }
    }

    private void setAc(boolean ac) {
        stateChanged();
        if (ac) {
            this.mAc.setImageResource(R.drawable.btm_acon);
        } else {
            this.mAc.setImageResource(R.drawable.btm_acoff);
        }
    }

    private void setAirfan(double airfan) {
        stateChanged();
        if (airfan == 0.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_auto);
        } else if (airfan == 0.5d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_05);
        } else if (airfan == 1.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_1);
        } else if (airfan == 1.5d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_15);
        } else if (airfan == 2.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_2);
        } else if (airfan == 2.5d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_25);
        } else if (airfan == 3.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_3);
        } else if (airfan == 3.5d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_35);
        } else if (airfan == 4.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_4);
        } else if (airfan == 4.5d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_45);
        } else if (airfan == 5.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_5);
        } else if (airfan == 5.5d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_55);
        } else if (airfan == 6.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_6);
        } else if (airfan == 6.5d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_65);
        } else if (airfan >= 7.0d) {
            this.mAirfan.setImageResource(R.drawable.liuliang_7);
        }
    }

    private void setTempL(double tempL) {
        stateChanged();
        if (tempL < 16.0d) {
            this.mTempL.setText("LO");
        } else if (tempL > 28.0d) {
            this.mTempL.setText("HI");
        } else {
            this.mTempL.setText(String.valueOf(tempL) + "℃");
        }
    }

    private void setTempR(double tempR) {
        stateChanged();
        if (tempR < 16.0d) {
            this.mTempR.setText("LO");
        } else if (tempR > 28.0d) {
            this.mTempR.setText("HI");
        } else {
            this.mTempR.setText(String.valueOf(tempR) + "℃");
        }
    }

    private void handleBackAction() {
        if (this.mMainActivity != null && LauncherApplication.getInstance().mHomeAndBackEnable) {
            this.mMainActivity.handleBackAction();
        }
    }

    private void handleHomeAction() {
        if (this.mMainActivity != null && LauncherApplication.getInstance().mHomeAndBackEnable) {
            Log.e("", "Menu start time = " + System.currentTimeMillis());
            this.mMainActivity.handHomeAction();
        }
    }

    private void handleAirInfo(AirInfo airInfo) {
        if (this.app.isAirhide) {
            this.air_state.setVisibility(4);
        } else if (airInfo != null && getActivity() != null) {
            if (!airInfo.isiAirOpen()) {
                this.air_state.setVisibility(0);
                stateChanged();
                return;
            }
            this.air_state.setVisibility(8);
            setTempL(airInfo.getLeftTemp());
            setAc(airInfo.isiACOpen());
            setAirfan(airInfo.getLevel());
            setTempR(airInfo.getRightTemp());
            setAuto(airInfo.isiFlatWind(), airInfo.isiDownWind(), airInfo.isiMaxFrontWind(), airInfo.isiFrontWind());
            if (airInfo.isiAuto1()) {
                this.mAirfan.setImageResource(R.drawable.liuliang_auto);
            }
            if (airInfo.isiAuto2()) {
                this.mAuto.setImageResource(R.drawable.buttom_bar_wind_auto);
            }
            if (airInfo.isiMaxFrontWind()) {
                this.mMax.setVisibility(0);
                this.mAuto.setVisibility(4);
                this.mAc.setVisibility(4);
                this.mAirfan.setVisibility(4);
                this.mTempL.setVisibility(4);
                this.mTempR.setVisibility(4);
            } else {
                this.mMax.setVisibility(8);
                this.mAuto.setVisibility(0);
                this.mAc.setVisibility(0);
                this.mAirfan.setVisibility(0);
                this.mTempL.setVisibility(0);
                this.mTempR.setVisibility(0);
            }
            stateChanged();
        }
    }

    public void handlerMsg(Message msg) {
        switch (msg.what) {
            case 1012:
                handleAirInfo((AirInfo) msg.getData().getParcelable(SysConst.FLAG_AIR_INFO));
                return;
            case SysConst.IDRVIER_STATE:
                byte btnCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
                if (btnCode == Mainboard.EIdriverEnum.BACK.getCode()) {
                    handleBackAction();
                    return;
                } else if (btnCode == Mainboard.EIdriverEnum.HOME.getCode() || btnCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                    handleHomeAction();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void stateChanged() {
        if (this.mMainActivity != null && !this.app.iIsVideoShow) {
            this.mMainActivity.showButtom();
        }
    }
}
