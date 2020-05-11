package com.touchus.benchilauncher.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BTConnectDialog {
    private static BTConnectDialog instance = null;
    private static AtomicBoolean isAdded = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public static Logger logger = LoggerFactory.getLogger(BTConnectDialog.class);
    /* access modifiers changed from: private */
    public LauncherApplication app;
    private View contentView = null;
    private Context mContext;
    /* access modifiers changed from: private */
    public MyCustomHandler mHandler = new MyCustomHandler(this);
    private String message;
    /* access modifiers changed from: private */
    public Button negativeButton;
    private String negativeButtonText;
    private View outside;
    private WindowManager.LayoutParams params;
    /* access modifiers changed from: private */
    public Button positiveButton;
    private String positiveButtonText;
    int time = 8;
    Timer timer = new Timer();
    Runnable update = new Runnable() {
        public void run() {
            BTConnectDialog.logger.debug("BTConnectDialog time:" + BTConnectDialog.this.time);
            if (BTConnectDialog.this.time != 0) {
                BTConnectDialog bTConnectDialog = BTConnectDialog.this;
                bTConnectDialog.time--;
                BTConnectDialog.this.mHandler.obtainMessage(0).sendToTarget();
                BTConnectDialog.this.mHandler.postDelayed(BTConnectDialog.this.update, 1000);
            } else if (BTConnectDialog.this.app.btConnectDialog) {
                BTConnectDialog.this.app.service.btMusicConnect();
                BTConnectDialog.this.dissShow();
            }
        }
    };
    private WindowManager wm;

    public BTConnectDialog() {
        logger.debug("BTConnectDialog BTConnectDialog()");
    }

    public static BTConnectDialog getInstance() {
        if (instance == null) {
            instance = new BTConnectDialog();
        }
        return instance;
    }

    public void showBTConnectView(Context context) {
        if (this.contentView != null) {
            logger.debug("BTConnectDialog show  contentView != null");
            return;
        }
        try {
            this.mContext = context.getApplicationContext();
            this.app = (LauncherApplication) context.getApplicationContext();
            this.message = this.mContext.getString(R.string.msg_change_audio);
            this.positiveButtonText = this.mContext.getString(R.string.bt_reconnect);
            this.negativeButtonText = this.mContext.getString(R.string.bt_connect_help);
            this.timer = new Timer();
            this.time = 8;
            this.wm = (WindowManager) this.mContext.getSystemService("window");
            this.params = new WindowManager.LayoutParams();
            this.params.format = 1;
            this.params.flags = 40;
            this.params.type = 2003;
            this.contentView = LayoutInflater.from(this.mContext).inflate(R.layout.dialog_bt_connect_layout, (ViewGroup) null);
            this.outside = this.contentView.findViewById(R.id.outside);
            this.positiveButton = (Button) this.contentView.findViewById(R.id.positiveButton);
            this.negativeButton = (Button) this.contentView.findViewById(R.id.negativeButton);
            this.positiveButton.setSelected(true);
            this.positiveButton.setText(this.positiveButtonText);
            this.positiveButton.setText(String.format(this.positiveButtonText, new Object[]{Integer.valueOf(this.time)}));
            this.positiveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BTConnectDialog.this.negativeButton.setSelected(false);
                    BTConnectDialog.this.positiveButton.setSelected(true);
                    BTConnectDialog.this.app.service.btMusicConnect();
                    BTConnectDialog.this.dissShow();
                }
            });
            this.negativeButton.setText(this.negativeButtonText);
            this.negativeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BTConnectDialog.this.negativeButton.setSelected(true);
                    BTConnectDialog.this.positiveButton.setSelected(false);
                    BTConnectDialog.this.app.interBTConnectView();
                    BTConnectDialog.this.dissShow();
                }
            });
            ((TextView) this.contentView.findViewById(R.id.message)).setText(this.message);
            if (!isAdded.get()) {
                this.wm.addView(this.contentView, this.params);
                isAdded.set(true);
            }
            this.outside.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() != 0) {
                        return false;
                    }
                    BTConnectDialog.this.dissShow();
                    return true;
                }
            });
            autoConnect();
        } catch (Exception e) {
            logger.debug("BTConnectDialog Exception:" + e);
            e.printStackTrace();
        }
        this.app.registerHandler(this.mHandler);
        this.app.btConnectDialog = true;
    }

    private void autoConnect() {
        this.mHandler.post(this.update);
    }

    public void dissShow() {
        if (this.contentView != null) {
            if (this.app.btConnectDialog) {
                this.app.btConnectDialog = false;
            }
            this.timer.cancel();
            unRegisterHandler();
            try {
                if (isAdded.get()) {
                    this.wm.removeView(this.contentView);
                    isAdded.set(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.contentView = null;
            isAdded.set(false);
        }
    }

    static class MyCustomHandler extends Handler {
        private WeakReference<BTConnectDialog> target;

        public MyCustomHandler(BTConnectDialog instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.target.get() != null) {
                if (msg.what == 6001) {
                    ((BTConnectDialog) this.target.get()).handlerMsg(msg);
                } else if (msg.what == 0) {
                    ((BTConnectDialog) this.target.get()).handlerMsg(msg);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        Bundle bundle = msg.getData();
        if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.RIGHT.getCode()) {
            right();
        } else if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.LEFT.getCode()) {
            left();
        } else if (bundle.getByte(SysConst.IDRIVER_ENUM) == Mainboard.EIdriverEnum.PRESS.getCode()) {
            press();
        } else if (msg.what == 0) {
            this.positiveButton.setText(String.format(this.positiveButtonText, new Object[]{Integer.valueOf(this.time)}));
        }
    }

    private void left() {
        if (this.negativeButton.isSelected()) {
            this.negativeButton.setSelected(false);
            this.positiveButton.setSelected(true);
        }
    }

    private void right() {
        if (this.positiveButton.isSelected()) {
            this.negativeButton.setSelected(true);
            this.positiveButton.setSelected(false);
        }
    }

    private void press() {
        if (this.positiveButton.isSelected()) {
            this.positiveButton.performClick();
        } else if (this.negativeButton.isSelected()) {
            this.negativeButton.performClick();
        }
        unRegisterHandler();
    }

    public void unRegisterHandler() {
        if (this.app != null) {
            this.app.unregisterHandler(this.mHandler);
        }
    }
}
