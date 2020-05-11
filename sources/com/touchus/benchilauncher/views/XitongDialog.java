package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.ProjectConfig;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;

public class XitongDialog extends Dialog implements View.OnClickListener {
    private TextView androidTview;
    /* access modifiers changed from: private */
    public LauncherApplication app;
    private TextView canboxTview;
    /* access modifiers changed from: private */
    public int clickCount = 0;
    private LinearLayout clickLayout;
    /* access modifiers changed from: private */
    public Context context;
    public MHandler mHandler = new MHandler(this);
    private TextView mcuTview;
    Thread resetClickCountThread = new Thread(new Runnable() {
        public void run() {
            XitongDialog.this.clickCount = 0;
        }
    });
    private TextView snTview;
    private TextView systemTview;
    private TextView typeTview;
    /* access modifiers changed from: private */
    public String url = "";

    public XitongDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public XitongDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_xitong_layout);
        this.app = (LauncherApplication) this.context.getApplicationContext();
        LinearLayout clickLayout2 = (LinearLayout) findViewById(R.id.versionLayout);
        this.typeTview = (TextView) findViewById(R.id.typeTview);
        this.snTview = (TextView) findViewById(R.id.SNTview);
        this.androidTview = (TextView) findViewById(R.id.androidVersionTview);
        this.systemTview = (TextView) findViewById(R.id.systemVersionTview);
        this.mcuTview = (TextView) findViewById(R.id.mcuVersionTview);
        this.canboxTview = (TextView) findViewById(R.id.canboxVersionTview);
        this.typeTview.setText(String.valueOf(ProjectConfig.projectName) + BenzModel.benzName());
        String SN = UtilTools.getIMEI(this.context);
        if (TextUtils.isEmpty(SN)) {
            this.snTview.setVisibility(8);
        }
        this.snTview.setText("SN：" + SN);
        String versionStr = String.valueOf(UtilTools.getVersion(this.context)) + (SysConst.LANGUAGE == 0 ? "" : "h");
        if (SysConst.TURKISH_VERSION) {
            this.androidTview.setText("APP：" + versionStr + "_tu");
        } else {
            this.androidTview.setText("APP：" + versionStr);
        }
        this.systemTview.setText(getVersion());
        this.mcuTview.setText("MCU：" + this.app.curMcuVersion);
        this.canboxTview.setText("CAN：" + this.app.curCanboxVersion);
        clickLayout2.setOnClickListener(this);
        this.typeTview.setOnClickListener(this);
        this.snTview.setOnClickListener(this);
        this.androidTview.setOnClickListener(this);
        this.systemTview.setOnClickListener(this);
        this.mcuTview.setOnClickListener(this);
        this.canboxTview.setOnClickListener(this);
        this.app.registerHandler(this.mHandler);
        Mainboard.getInstance().getMCUVersionNumber();
        Mainboard.getInstance().getModeInfo(Mainboard.EModeInfo.CANBOX_INFO);
        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.clickCount = 0;
        this.mcuTview.setText("MCU：" + this.app.curMcuVersion);
        this.canboxTview.setText("CAN：" + this.app.curCanboxVersion);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    public void unregisterHandlerr() {
        this.app.unregisterHandler(this.mHandler);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.typeTview) {
            this.clickCount = 0;
            showDialog();
        } else if (v.getId() == R.id.SNTview) {
            new Thread(new Runnable() {
                public void run() {
                    if (UtilTools.checkURL(XitongDialog.this.context, String.format(ProjectConfig.CONFIG_URL, new Object[]{XitongDialog.this.app.curMcuVersion}))) {
                        XitongDialog.this.url = String.format(ProjectConfig.CONFIG_URL, new Object[]{XitongDialog.this.app.curMcuVersion});
                    } else {
                        if (UtilTools.checkURL(XitongDialog.this.context, String.format(ProjectConfig.CONFIG_URL, new Object[]{UtilTools.getIMEI(XitongDialog.this.context)}))) {
                            XitongDialog.this.url = String.format(ProjectConfig.CONFIG_URL, new Object[]{UtilTools.getIMEI(XitongDialog.this.context)});
                        } else {
                            Log.e("UpdateDialog", "error url");
                        }
                    }
                    if (!TextUtils.isEmpty(XitongDialog.this.url)) {
                        XitongDialog.this.mHandler.sendEmptyMessage(SysConst.ONLINE_CONFIG_VALID);
                    }
                }
            }).start();
        } else if (v.getId() == R.id.versionLayout) {
        } else {
            if (v.getId() == R.id.androidVersionTview) {
                this.clickCount = 0;
                showDialog(4);
            } else if (v.getId() == R.id.systemVersionTview) {
                this.clickCount = 0;
                showDialog(1);
            } else if (v.getId() == R.id.mcuVersionTview) {
                this.clickCount = 0;
                showDialog(2);
            } else if (v.getId() == R.id.canboxVersionTview) {
                this.clickCount = 0;
                showDialog(3);
            }
        }
    }

    private void showDialog(int type) {
        UpdateDialog dialog = new UpdateDialog(this.context, R.style.Dialog);
        dialog.currentType = type;
        this.app.currentDialog3 = dialog;
        dialog.show();
    }

    private void showDialog(int type, String url2) {
        UpdateDialog dialog = new UpdateDialog(this.context, R.style.Dialog);
        dialog.currentType = type;
        dialog.url = url2;
        this.app.currentDialog3 = dialog;
        dialog.show();
    }

    private void showDialog() {
        FeedbackDialog dialog = new FeedbackDialog(this.context, R.style.Dialog);
        this.app.currentDialog3 = dialog;
        dialog.show();
    }

    public String getVersion() {
        return "SYSTEM：V" + Build.DISPLAY;
    }

    static class MHandler extends Handler {
        private WeakReference<XitongDialog> target;

        public MHandler(XitongDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((XitongDialog) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode != Mainboard.EIdriverEnum.TURN_RIGHT.getCode() && idriverCode != Mainboard.EIdriverEnum.RIGHT.getCode() && idriverCode != Mainboard.EIdriverEnum.TURN_LEFT.getCode() && idriverCode != Mainboard.EIdriverEnum.LEFT.getCode()) {
                if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                    dismiss();
                }
            }
        } else if (msg.what == 1131) {
            this.mcuTview.setText("MCU：" + this.app.curMcuVersion);
        } else if (msg.what == 1121 || msg.what == 1127) {
            this.canboxTview.setText("CAN：" + this.app.curCanboxVersion);
        } else if (msg.what == 1125) {
            showDialog(5, this.url);
        }
    }
}
