package com.touchus.benchilauncher.activity.main.left;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import java.lang.ref.WeakReference;

public class LightFrag extends Fragment {
    private LauncherApplication app;
    private LightHandler mHandler = new LightHandler(this);
    private ImageView mImgCarLight;
    private TextView mLightDesc;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_left_light, container);
        this.mImgCarLight = (ImageView) view.findViewById(R.id.img_carlight);
        this.mLightDesc = (TextView) view.findViewById(R.id.light_desc);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onStart() {
        this.app = (LauncherApplication) getActivity().getApplication();
        this.app.registerHandler(this.mHandler);
        settingLightInfo(this.app.carBaseInfo);
        super.onStart();
    }

    public void onStop() {
        this.app.unregisterHandler(this.mHandler);
        super.onStop();
    }

    static class LightHandler extends Handler {
        private WeakReference<LightFrag> target;

        public LightHandler(LightFrag instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((LightFrag) this.target.get()).handMsg(msg);
            }
        }
    }

    public void handMsg(Message msg) {
        if (getActivity() != null && msg.what == 1007) {
            settingLightInfo((CarBaseInfo) msg.getData().getParcelable(SysConst.FLAG_CAR_BASE_INFO));
        }
    }

    private void settingLightInfo(CarBaseInfo info) {
        if (this.app.isSUV) {
            this.mImgCarLight.setImageResource(R.drawable.suvlight_none);
        } else {
            this.mImgCarLight.setImageResource(R.drawable.carlight_none);
        }
        if (info != null) {
            if (info.isiDistantLightOpen()) {
                if (this.app.isSUV) {
                    this.mImgCarLight.setImageResource(R.drawable.suvlight_big_light);
                } else {
                    this.mImgCarLight.setImageResource(R.drawable.carlight_big_light);
                }
                this.mLightDesc.setText(getString(R.string.msg_car_light_big));
            } else if (info.isiNearLightOpen()) {
                if (this.app.isSUV) {
                    this.mImgCarLight.setImageResource(R.drawable.suvlight_small_light);
                } else {
                    this.mImgCarLight.setImageResource(R.drawable.carlight_small_light);
                }
                this.mLightDesc.setText(getString(R.string.msg_car_light_small));
            } else {
                this.mLightDesc.setText(getString(R.string.msg_car_light_none));
            }
        }
    }
}
