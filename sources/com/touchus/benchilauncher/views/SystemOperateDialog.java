package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import java.lang.ref.WeakReference;

public class SystemOperateDialog extends Dialog implements View.OnClickListener {
    private Button cancelBtn;
    public int height = 200;
    private String itemString;
    private LauncherApplication mApp;
    private Context mContext;
    public MHandler mHandler = new MHandler(this);
    public String showMessage = "";
    private Button submitBtn;
    public String title = "";
    private TextView titleName;
    public int width = 500;

    public SystemOperateDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SystemOperateDialog(Context context, int themeId) {
        super(context, themeId);
        this.mContext = context;
    }

    public void setTitle(String itemString2) {
        this.itemString = itemString2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wifi_relationship_1);
        this.mApp = (LauncherApplication) this.mContext.getApplicationContext();
        this.cancelBtn = (Button) findViewById(R.id.canceBtn);
        this.submitBtn = (Button) findViewById(R.id.submitBtn);
        this.submitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        this.submitBtn.setText(this.mContext.getString(R.string.name_btn_affirm));
        this.cancelBtn.setText(this.mContext.getString(R.string.name_btn_cancel));
        this.height = (int) (((double) this.height) * 0.9d);
        this.titleName = (TextView) findViewById(R.id.titleName);
        if (this.itemString.equals(this.mContext.getString(R.string.string_reboot_system))) {
            this.titleName.setText(R.string.string_reboot_system_title);
        } else if (this.itemString.equals(this.mContext.getString(R.string.string_restore_factory))) {
            this.titleName.setText(R.string.string_restore_factory_title);
        }
        this.cancelBtn.setOnClickListener(this);
        this.submitBtn.setOnClickListener(this);
        this.cancelBtn.setSelected(true);
        this.submitBtn.setSelected(false);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mApp.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mApp.unregisterHandler(this.mHandler);
        super.onStop();
    }

    public void onClick(View view) {
        if (view.getId() != R.id.submitBtn) {
            view.getId();
        } else if (this.itemString.equals(this.mContext.getString(R.string.string_reboot_system))) {
            ((PowerManager) this.mContext.getSystemService("power")).reboot("reboot");
        } else if (this.itemString.equals(this.mContext.getString(R.string.string_restore_factory))) {
            this.mContext.sendBroadcast(new Intent("com.touchus.factorytest.recovery"));
        }
        dismiss();
    }

    static class MHandler extends Handler {
        private WeakReference<SystemOperateDialog> target;

        public MHandler(SystemOperateDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((SystemOperateDialog) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || idriverCode == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                this.cancelBtn.setSelected(true);
                this.submitBtn.setSelected(false);
            } else if (idriverCode == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || idriverCode == Mainboard.EIdriverEnum.LEFT.getCode()) {
                this.cancelBtn.setSelected(false);
                this.submitBtn.setSelected(true);
            } else if (idriverCode == Mainboard.EIdriverEnum.PRESS.getCode()) {
                if (this.submitBtn.isSelected()) {
                    this.submitBtn.performClick();
                } else {
                    this.cancelBtn.performClick();
                }
            } else if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }
}
