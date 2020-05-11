package com.touchus.benchilauncher.activity.main.left;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.bean.GuideInfoEntry;
import com.touchus.benchilauncher.bean.GuideInfoExtraKey;
import com.touchus.benchilauncher.utils.Utiltools;
import java.lang.ref.WeakReference;

public class NaviInfoFrag extends Fragment {
    private LauncherApplication app;
    private TextView cameraIcon;
    private TextView cur_road_name;
    private Context mContext;
    private NaviInfoHandler mHandler = new NaviInfoHandler(this);
    private TextView next_road_name;
    private TextView seg_remain_dis;
    private TextView speedIcon;
    private ImageView turnIcon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_left_navi, container, false);
        this.mContext = getActivity();
        this.seg_remain_dis = (TextView) view.findViewById(R.id.seg_remain_dis);
        this.cameraIcon = (TextView) view.findViewById(R.id.camera_img);
        this.speedIcon = (TextView) view.findViewById(R.id.speed_img);
        this.turnIcon = (ImageView) view.findViewById(R.id.turnicon);
        this.cur_road_name = (TextView) view.findViewById(R.id.cur_road_name);
        this.next_road_name = (TextView) view.findViewById(R.id.next_road_name);
        return view;
    }

    public void onStart() {
        super.onStart();
        this.app = (LauncherApplication) getActivity().getApplication();
        this.app.registerHandler(this.mHandler);
    }

    public void onStop() {
        super.onStop();
        this.app = (LauncherApplication) getActivity().getApplication();
        this.app.unregisterHandler(this.mHandler);
    }

    /* access modifiers changed from: private */
    public void handleMesage(Message msg) {
        String str;
        if (getActivity() != null) {
            if (msg.what == 1045) {
                Bundle bundle = msg.getData();
                GuideInfoEntry guideInfoEntry = new GuideInfoEntry();
                guideInfoEntry.ROUTE_ALL_TIME = bundle.getInt(GuideInfoExtraKey.ROUTE_ALL_TIME, 0);
                guideInfoEntry.CAMERA_DIST = bundle.getInt(GuideInfoExtraKey.CAMERA_DIST, 0);
                guideInfoEntry.SEG_REMAIN_DIS = bundle.getInt(GuideInfoExtraKey.SEG_REMAIN_DIS, 0);
                guideInfoEntry.LIMITED_SPEED = bundle.getInt(GuideInfoExtraKey.LIMITED_SPEED, 0);
                guideInfoEntry.ICON = bundle.getInt(GuideInfoExtraKey.ICON, 0);
                guideInfoEntry.ROUTE_REMAIN_DIS = bundle.getInt(GuideInfoExtraKey.ROUTE_REMAIN_DIS, 0);
                guideInfoEntry.CUR_ROAD_NAME = bundle.getString(GuideInfoExtraKey.CUR_ROAD_NAME);
                guideInfoEntry.ROUTE_ALL_DIS = bundle.getInt(GuideInfoExtraKey.ROUTE_ALL_DIS, 0);
                guideInfoEntry.CUR_SPEED = bundle.getInt(GuideInfoExtraKey.CUR_SPEED, 0);
                guideInfoEntry.ROUTE_REMAIN_TIME = bundle.getInt(GuideInfoExtraKey.ROUTE_REMAIN_TIME, 0);
                guideInfoEntry.NEXT_ROAD_NAME = bundle.getString(GuideInfoExtraKey.NEXT_ROAD_NAME);
                guideInfoEntry.CAMERA_SPEED = bundle.getInt(GuideInfoExtraKey.CAMERA_SPEED, 0);
                guideInfoEntry.CAMERA_TYPE = bundle.getInt(GuideInfoExtraKey.CAMERA_TYPE, 0);
                guideInfoEntry.CAMERA_INDEX = bundle.getInt(GuideInfoExtraKey.CAMERA_INDEX, 0);
                int dis = bundle.getInt(GuideInfoExtraKey.SEG_REMAIN_DIS, 0);
                double dis_tmp = ((double) dis) / 1000.0d;
                TextView textView = this.seg_remain_dis;
                String string = this.mContext.getString(R.string.string_navi_dis);
                Object[] objArr = new Object[1];
                if (dis_tmp < 1.0d) {
                    str = String.valueOf(dis) + "米";
                } else {
                    str = String.valueOf(String.format("%.1f", new Object[]{Double.valueOf(dis_tmp)})) + "公里";
                }
                objArr[0] = str;
                textView.setText(String.format(string, objArr));
                this.turnIcon.setImageResource(Utiltools.getResId(this.mContext, "sou" + bundle.getInt(GuideInfoExtraKey.ICON, 0)));
                this.cur_road_name.setText(String.format(this.mContext.getString(R.string.string_navi_from), new Object[]{bundle.getString(GuideInfoExtraKey.CUR_ROAD_NAME)}));
                this.next_road_name.setText(String.format(this.mContext.getString(R.string.string_navi_to), new Object[]{bundle.getString(GuideInfoExtraKey.NEXT_ROAD_NAME)}));
                setCameraType(guideInfoEntry.LIMITED_SPEED, guideInfoEntry.CAMERA_INDEX, guideInfoEntry.CAMERA_TYPE, guideInfoEntry.CAMERA_SPEED);
                this.turnIcon.setVisibility(0);
                this.cur_road_name.setVisibility(0);
                this.next_road_name.setVisibility(0);
            } else if (msg.what == 1046) {
                this.turnIcon.setVisibility(0);
                this.cur_road_name.setVisibility(0);
                this.next_road_name.setVisibility(0);
                this.cameraIcon.setVisibility(8);
                this.speedIcon.setVisibility(8);
            } else if (msg.what == 1047) {
                this.seg_remain_dis.setText("到达目的地");
                this.turnIcon.setVisibility(8);
                this.cur_road_name.setVisibility(8);
                this.next_road_name.setVisibility(8);
                this.cameraIcon.setVisibility(8);
                this.speedIcon.setVisibility(8);
            }
        }
    }

    private void setCameraType(int limited_speed, int camera_index, int camera_type, int camera_speed) {
        String iconRes;
        Log.e("", "CameraType ： limited_speed = " + limited_speed + "，camera_index = " + camera_index + "，camera_type = " + camera_type + "，camera_speed = " + camera_speed);
        if (limited_speed <= 0) {
            this.speedIcon.setVisibility(8);
        } else {
            this.speedIcon.setVisibility(0);
            this.speedIcon.setText(new StringBuilder().append(limited_speed).toString());
        }
        if (camera_index == -1) {
            this.cameraIcon.setVisibility(8);
            return;
        }
        switch (camera_type) {
            case 2:
                iconRes = "auto_navi_view_traffic_day";
                break;
            case 4:
                iconRes = "auto_navi_view_bus_day";
                break;
            default:
                iconRes = "auto_navi_view_camera_day";
                break;
        }
        this.cameraIcon.setBackgroundResource(Utiltools.getResId(this.mContext, iconRes));
        this.cameraIcon.setVisibility(0);
        if (camera_speed > 0) {
            this.speedIcon.setText(new StringBuilder().append(camera_speed).toString());
        }
    }

    static class NaviInfoHandler extends Handler {
        private WeakReference<NaviInfoFrag> target;

        public NaviInfoHandler(NaviInfoFrag instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((NaviInfoFrag) this.target.get()).handleMesage(msg);
            }
        }
    }
}
