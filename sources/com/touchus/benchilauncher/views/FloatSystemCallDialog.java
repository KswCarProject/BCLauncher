package com.touchus.benchilauncher.views;

import android.app.Instrumentation;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.service.BluetoothService;
import com.touchus.publicutils.sysconst.VoiceAssistantConst;
import com.touchus.publicutils.utils.TimeUtils;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloatSystemCallDialog implements View.OnClickListener {
    private static FloatSystemCallDialog instance = null;
    private static AtomicBoolean isAdded = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public static Logger logger = LoggerFactory.getLogger(FloatSystemCallDialog.class);
    private static AtomicInteger showingStatus = new AtomicInteger(FloatShowST.DEFAULT.ordinal());
    /* access modifiers changed from: private */
    public LauncherApplication app;
    private String callingPhoneNumber = "-1";
    private ImageButton leftButton;
    private View mCallFloatView = null;
    /* access modifiers changed from: private */
    public Context mContext;
    private BluetoothHandler mHandler = new BluetoothHandler(this);
    private WindowManager.LayoutParams params;
    private LinearLayout phoneLayout;
    private ImageButton rightButton;
    private TextView talkingPhoneNumber;
    /* access modifiers changed from: private */
    public TextView talkingphoneStatus;
    private WindowManager wm;

    public enum FloatShowST {
        DEFAULT,
        INCOMING,
        TALKING,
        OUTCALLING,
        HIDEN
    }

    public FloatSystemCallDialog() {
        logger.debug("FloatSystemCallDialog FloatSystemCallDialog()");
    }

    public static FloatSystemCallDialog getInstance() {
        if (instance == null) {
            instance = new FloatSystemCallDialog();
        }
        return instance;
    }

    public FloatShowST getShowStatus() {
        return FloatShowST.values()[showingStatus.get()];
    }

    public void setShowStatus(FloatShowST value) {
        logger.debug("FloatSystemCallDialog setShowStatus showingStatus: " + value);
        showingStatus.set(value.ordinal());
        logger.debug("FloatSystemCallDialog setShowStatus showingStatus: " + getShowStatus());
    }

    public String getCallingPhonenumber() {
        return this.callingPhoneNumber;
    }

    public synchronized void setCallingPhonenumber(String phoneNumber) {
        this.callingPhoneNumber = phoneNumber;
    }

    public void showFloatCallView(Context context) {
        try {
            this.mContext = context.getApplicationContext();
            this.app = (LauncherApplication) context.getApplicationContext();
            logger.debug("FloatSystemCallDialog showFloatCallView()  PhoneNumber=" + this.app.phoneNumber);
            this.wm = (WindowManager) this.mContext.getSystemService("window");
            this.params = new WindowManager.LayoutParams();
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            this.params.height = 235;
            this.params.width = 700;
            this.params.format = 1;
            this.params.flags = 40;
            this.params.type = VoiceAssistantConst.VALUE_APP_ONLINE_MUSIC;
            if (this.mCallFloatView != null) {
                try {
                    isAdded.set(false);
                    this.wm.removeView(this.mCallFloatView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.mCallFloatView = null;
            }
            this.mCallFloatView = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_call_phone, (ViewGroup) null);
            this.talkingPhoneNumber = (TextView) this.mCallFloatView.findViewById(R.id.tv_dialog_phoneNum);
            this.talkingphoneStatus = (TextView) this.mCallFloatView.findViewById(R.id.tv_dialog_yunyingshang);
            this.rightButton = (ImageButton) this.mCallFloatView.findViewById(R.id.img_dialog_end);
            this.leftButton = (ImageButton) this.mCallFloatView.findViewById(R.id.img_dialog_send);
            this.phoneLayout = (LinearLayout) this.mCallFloatView.findViewById(R.id.phonenum);
            if (!isAdded.get()) {
                this.wm.addView(this.mCallFloatView, this.params);
                isAdded.set(true);
            }
            if (TextUtils.isEmpty(this.app.phoneNumber)) {
                this.talkingPhoneNumber.setText(this.mContext.getString(R.string.bluetooth_unknow_number));
            } else {
                this.talkingPhoneNumber.setText(this.app.phoneName);
            }
            this.leftButton.setOnClickListener(this);
            this.rightButton.setOnClickListener(this);
            this.phoneLayout.setOnClickListener(this);
        } catch (Exception e2) {
            logger.debug("FloatSystemCallDialog Exception:" + e2);
            e2.printStackTrace();
            showingStatus.set(FloatShowST.DEFAULT.ordinal());
        }
        if (BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
            this.talkingphoneStatus.setText(String.valueOf(this.mContext.getString(R.string.bluetooth_incoming)) + " ");
        } else if (BluetoothService.bluetoothStatus == EPhoneStatus.CALLING_OUT) {
            this.talkingphoneStatus.setText(String.valueOf(this.mContext.getString(R.string.bluetooth_outgoing)) + " ");
            setButtonState(true);
        }
        if (BluetoothService.currentCallingType == 1412 && !TextUtils.isEmpty(this.app.phoneNumber)) {
            setButtonState(BluetoothService.talkingflag);
        } else if (BluetoothService.currentCallingType == 1413) {
            setButtonState(true);
        }
        this.app.registerHandler(this.mHandler);
        this.app.isCallDialog = true;
    }

    private void setButtonState(boolean flag) {
        if (flag) {
            this.leftButton.setClickable(false);
            this.leftButton.setSelected(false);
            this.leftButton.setEnabled(false);
            this.rightButton.setSelected(true);
            return;
        }
        this.leftButton.setClickable(true);
        this.leftButton.setSelected(true);
        this.leftButton.setEnabled(true);
        this.rightButton.setSelected(false);
    }

    public void freshCallingTime() {
        if (this.mCallFloatView != null) {
            logger.debug("TIME_FLAG" + TimeUtils.secToTimeString((long) this.app.btservice.calltime));
            this.talkingphoneStatus.setText(String.format(this.mContext.getString(R.string.bluetooth_talking), new Object[]{TimeUtils.secToTimeString((long) this.app.btservice.calltime)}));
            updateTalkStatus();
        }
    }

    private void modifyName() {
        if (this.mCallFloatView != null) {
            this.talkingPhoneNumber.setText(this.app.phoneName);
        }
    }

    public void clearView() {
        if (this.mCallFloatView != null) {
            try {
                if (isAdded.get()) {
                    this.wm.removeViewImmediate(this.mCallFloatView);
                    isAdded.set(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mCallFloatView = null;
            isAdded.set(false);
        }
    }

    public void dissShow() {
        if (this.app.currentDialog4 != null) {
            this.app.currentDialog4.dismiss();
        }
        if (this.mCallFloatView != null) {
            this.app.unregisterHandler(this.mHandler);
            this.app.isCallDialog = false;
            if (showingStatus.get() == FloatShowST.HIDEN.ordinal()) {
                logger.debug("FloatSystemCallDialog HIDEN  return");
                try {
                    if (isAdded.get()) {
                        this.wm.removeViewImmediate(this.mCallFloatView);
                        isAdded.set(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showingStatus.set(FloatShowST.DEFAULT.ordinal());
                this.mCallFloatView = null;
                return;
            }
            showingStatus.set(FloatShowST.DEFAULT.ordinal());
            this.app.btservice.histalkflag = false;
            this.app.btservice.accFlag = false;
            this.app.btservice.exitsource();
            try {
                if (isAdded.get()) {
                    this.wm.removeViewImmediate(this.mCallFloatView);
                    isAdded.set(false);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.mCallFloatView = null;
            isAdded.set(false);
        }
    }

    public void hiden() {
        if (this.app.currentDialog4 != null) {
            this.app.currentDialog4.dismiss();
        }
        if (this.mCallFloatView != null) {
            this.app.unregisterHandler(this.mHandler);
            this.app.isCallDialog = false;
            showingStatus.set(FloatShowST.HIDEN.ordinal());
            try {
                if (isAdded.get()) {
                    this.wm.removeViewImmediate(this.mCallFloatView);
                    isAdded.set(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void show() {
        logger.debug("FloatSystemCallDialog show()");
        if (this.mCallFloatView == null) {
            logger.debug("FloatSystemCallDialog show  mCallFloatView = null");
            return;
        }
        this.app.registerHandler(this.mHandler);
        this.app.isCallDialog = true;
        updateTalkStatus();
        try {
            if (!isAdded.get()) {
                this.wm.addView(this.mCallFloatView, this.params);
                isAdded.set(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        modifyName();
    }

    public void updateTalkStatus() {
        if (this.mCallFloatView != null) {
            if (BluetoothService.talkingflag) {
                setButtonState(true);
                showingStatus.set(FloatShowST.TALKING.ordinal());
                return;
            }
            setButtonState(false);
            if (BluetoothService.currentCallingType == 1412) {
                showingStatus.set(FloatShowST.INCOMING.ordinal());
            } else if (BluetoothService.currentCallingType == 1413) {
                showingStatus.set(FloatShowST.OUTCALLING.ordinal());
            } else {
                showingStatus.set(FloatShowST.DEFAULT.ordinal());
            }
        }
    }

    public boolean isDestory() {
        if (this.mCallFloatView == null) {
            return true;
        }
        return false;
    }

    class PhoneNumPraseTask extends AsyncTask<String, String, String> {
        PhoneNumPraseTask() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            try {
                if (UtilTools.isNetworkConnected(FloatSystemCallDialog.this.mContext)) {
                    FloatSystemCallDialog.logger.debug("FloatSystemCallDialog PhoneNumPraseTask city: " + "");
                    return "";
                }
                FloatSystemCallDialog.logger.debug("FloatSystemCallDialog PhoneNumPraseTask not net error ! ");
                return FloatSystemCallDialog.this.mContext.getString(R.string.bluetooth_unknow_city);
            } catch (Exception e) {
                e.printStackTrace();
                FloatSystemCallDialog.logger.debug("FloatSystemCallDialog get city error !");
                return "";
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            if (TextUtils.isEmpty(result)) {
                return;
            }
            if (BluetoothService.currentCallingType == 1412 || BluetoothService.currentCallingType == 1413) {
                try {
                    if (BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
                        FloatSystemCallDialog.this.talkingphoneStatus.setText(String.valueOf(FloatSystemCallDialog.this.mContext.getString(R.string.bluetooth_incoming)) + " " + result);
                    } else if (BluetoothService.bluetoothStatus == EPhoneStatus.CALLING_OUT) {
                        FloatSystemCallDialog.this.talkingphoneStatus.setText(String.valueOf(FloatSystemCallDialog.this.mContext.getString(R.string.bluetooth_outgoing)) + " " + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phonenum:
                if (!UtilTools.isForeground(LauncherApplication.mContext, "com.touchus.benchilauncher.Launcher")) {
                    new Thread(new Runnable() {
                        public void run() {
                            new Instrumentation().sendKeyDownUpSync(3);
                            FloatSystemCallDialog.this.app.sendMessage(1013, (Bundle) null);
                        }
                    }).start();
                    return;
                } else {
                    this.app.sendMessage(1013, (Bundle) null);
                    return;
                }
            case R.id.img_dialog_send:
                if (BluetoothService.currentCallingType == 1412) {
                    this.app.btservice.answerCalling();
                    showingStatus.set(FloatShowST.TALKING.ordinal());
                    return;
                }
                return;
            case R.id.img_dialog_end:
                if (!UtilTools.isFastDoubleClick()) {
                    logger.debug("FloatSystemCallDialog cutdownCurrentCalling");
                    try {
                        this.app.btservice.cutdownCurrentCalling();
                        dissShow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.app.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            FloatSystemCallDialog.this.setCallingPhonenumber("-1");
                        }
                    }, 1000);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        Bundle bundle = msg.getData();
        switch (msg.what) {
            case SysConst.IDRVIER_STATE:
                byte mIDRIVERENUM = bundle.getByte(SysConst.IDRIVER_ENUM);
                if (mIDRIVERENUM == Mainboard.EIdriverEnum.PICK_UP.getCode()) {
                    if (BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
                        this.leftButton.performClick();
                        return;
                    }
                    return;
                } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.HANG_UP.getCode()) {
                    if (BluetoothService.bluetoothStatus == EPhoneStatus.TALKING || BluetoothService.bluetoothStatus == EPhoneStatus.CALLING_OUT || BluetoothService.bluetoothStatus == EPhoneStatus.INCOMING_CALL) {
                        this.rightButton.performClick();
                        return;
                    }
                    return;
                } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                    if (this.rightButton.isEnabled()) {
                        this.rightButton.setSelected(true);
                        this.leftButton.setSelected(false);
                        return;
                    }
                    return;
                } else if (mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
                    if (this.leftButton.isEnabled()) {
                        this.leftButton.setSelected(true);
                        this.rightButton.setSelected(false);
                        return;
                    }
                    return;
                } else if (mIDRIVERENUM != Mainboard.EIdriverEnum.PRESS.getCode()) {
                    return;
                } else {
                    if (this.leftButton.isSelected()) {
                        this.leftButton.performClick();
                        return;
                    } else if (this.rightButton.isSelected()) {
                        this.rightButton.performClick();
                        return;
                    } else {
                        return;
                    }
                }
            default:
                return;
        }
    }

    public static class BluetoothHandler extends Handler {
        private WeakReference<FloatSystemCallDialog> target;

        public BluetoothHandler(FloatSystemCallDialog instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.target.get() != null) {
                ((FloatSystemCallDialog) this.target.get()).handlerMsg(msg);
            }
        }
    }
}
