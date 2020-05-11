package com.touchus.benchilauncher.activity.main.left;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.inface.IDoorStateParent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CardoorFrag extends Fragment {
    private LauncherApplication app;
    public ImageView mBehind;
    public TextView mDoorDes;
    public TextView mDoorsDes;
    public ImageView mFront;
    public CardoorHandler mHandler = new CardoorHandler(this);
    public ImageView mImgState;
    public ImageView mLeftBehind;
    public ImageView mLeftFront;
    public ImageView mNormal;
    public ImageView mRightBehind;
    public ImageView mRightFront;
    public RelativeLayout mRlOverlook;
    public RelativeLayout mRlState;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_left_cardoor, container, false);
        this.mRlState = (RelativeLayout) view.findViewById(R.id.cardoor_status2);
        this.mImgState = (ImageView) view.findViewById(R.id.cardoor_qian_open);
        this.mRlOverlook = (RelativeLayout) view.findViewById(R.id.cardoor_status1);
        this.mRightBehind = (ImageView) view.findViewById(R.id.cardoor_right2);
        this.mRightFront = (ImageView) view.findViewById(R.id.cardoor_right1);
        this.mLeftBehind = (ImageView) view.findViewById(R.id.cardoor_left2);
        this.mLeftFront = (ImageView) view.findViewById(R.id.cardoor_left1);
        this.mBehind = (ImageView) view.findViewById(R.id.cardoor_hou);
        this.mFront = (ImageView) view.findViewById(R.id.cardoor_qian);
        this.mNormal = (ImageView) view.findViewById(R.id.cardoor_normal);
        this.mDoorDes = (TextView) view.findViewById(R.id.doorDesc);
        this.mDoorsDes = (TextView) view.findViewById(R.id.doorsDesc);
        return view;
    }

    public void onStart() {
        super.onStart();
        this.app = (LauncherApplication) getActivity().getApplication();
        initDoorView();
        this.app.registerHandler(this.mHandler);
    }

    public void onStop() {
        super.onStop();
        this.app = (LauncherApplication) getActivity().getApplication();
        this.app.unregisterHandler(this.mHandler);
    }

    private void initDoorView() {
        if (this.app.carBaseInfo == null) {
            hideAll();
        } else {
            settingDoorView(this.app.carBaseInfo);
        }
    }

    private void settingDoorView(CarBaseInfo info) {
        ArrayList<Integer> openState = new ArrayList<>();
        if (info.isiLeftBackOpen()) {
            openState.add(2);
        }
        if (info.isiLeftFrontOpen()) {
            openState.add(3);
        }
        if (info.isiRightBackOpen()) {
            openState.add(4);
        }
        if (info.isiRightFrontOpen()) {
            openState.add(5);
        }
        if (info.isiFront()) {
            openState.add(0);
        }
        if (info.isiBack()) {
            openState.add(1);
        }
        hideAll();
        if (openState.size() == 0) {
            resetLeftShowInfo();
        } else if (openState.size() == 1) {
            openDoor(openState.get(0).intValue());
            showMe();
        } else if (openState.size() > 1) {
            this.mDoorsDes.setVisibility(0);
            for (int i = 0; i < openState.size(); i++) {
                openDoors(openState.get(i).intValue());
            }
            showMe();
        }
    }

    private void showMe() {
        if (getActivity() != null) {
            ((IDoorStateParent) getActivity()).showCardoor();
        }
    }

    private void resetLeftShowInfo() {
        if (getActivity() != null) {
            ((IDoorStateParent) getActivity()).resetLeftLayoutToRightState();
        }
    }

    private void openDoor(int door) {
        if (getActivity() != null) {
            this.mImgState.setVisibility(0);
            this.mDoorDes.setVisibility(0);
            if (this.app.isSUV) {
                switch (door) {
                    case 0:
                        this.mImgState.setImageResource(R.drawable.suvdoor_qian_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_front));
                        return;
                    case 1:
                        this.mImgState.setImageResource(R.drawable.suvdoor_hou_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_back));
                        return;
                    case 2:
                        this.mImgState.setImageResource(R.drawable.suvdoor_left1_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_left_back));
                        return;
                    case 3:
                        this.mImgState.setImageResource(R.drawable.suvdoor_left2_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_left_front));
                        return;
                    case 4:
                        this.mImgState.setImageResource(R.drawable.suvdoor_right1_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_right_back));
                        return;
                    case 5:
                        this.mImgState.setImageResource(R.drawable.suvdoor_right2_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_right_front));
                        return;
                    default:
                        return;
                }
            } else {
                switch (door) {
                    case 0:
                        this.mImgState.setImageResource(R.drawable.cardoor_qian_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_front));
                        return;
                    case 1:
                        this.mImgState.setImageResource(R.drawable.cardoor_hou_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_back));
                        return;
                    case 2:
                        this.mImgState.setImageResource(R.drawable.cardoor_left1_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_left_back));
                        return;
                    case 3:
                        this.mImgState.setImageResource(R.drawable.cardoor_left2_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_left_front));
                        return;
                    case 4:
                        this.mImgState.setImageResource(R.drawable.cardoor_right1_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_right_back));
                        return;
                    case 5:
                        this.mImgState.setImageResource(R.drawable.cardoor_right2_open);
                        this.mDoorDes.setText(getString(R.string.msg_car_door_right_front));
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private void openDoors(int door) {
        this.mNormal.setVisibility(0);
        if (this.app.isSUV) {
            this.mNormal.setImageResource(R.drawable.suvdoor_normal);
        } else {
            this.mNormal.setImageResource(R.drawable.cardoor_normal);
        }
        switch (door) {
            case 0:
                this.mFront.setVisibility(0);
                return;
            case 1:
                if (this.app.isSUV) {
                    this.mBehind.setImageResource(R.drawable.suvdoor_hou);
                } else {
                    this.mBehind.setImageResource(R.drawable.cardoor_hou);
                }
                this.mBehind.setVisibility(0);
                return;
            case 2:
                this.mLeftBehind.setVisibility(0);
                return;
            case 3:
                this.mLeftFront.setVisibility(0);
                return;
            case 4:
                this.mRightBehind.setVisibility(0);
                return;
            case 5:
                this.mRightFront.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private void hideAll() {
        this.mNormal.setVisibility(8);
        this.mBehind.setVisibility(8);
        this.mFront.setVisibility(8);
        this.mLeftBehind.setVisibility(8);
        this.mLeftFront.setVisibility(8);
        this.mRightBehind.setVisibility(8);
        this.mRightFront.setVisibility(8);
        this.mImgState.setVisibility(8);
        this.mDoorDes.setVisibility(8);
        this.mDoorsDes.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void handleMesage(Message msg) {
        if (getActivity() != null && msg.what == 1007) {
            settingDoorView((CarBaseInfo) msg.getData().getParcelable(SysConst.FLAG_CAR_BASE_INFO));
        }
    }

    static class CardoorHandler extends Handler {
        private WeakReference<CardoorFrag> target;

        public CardoorHandler(CardoorFrag instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((CardoorFrag) this.target.get()).handleMesage(msg);
            }
        }
    }
}
