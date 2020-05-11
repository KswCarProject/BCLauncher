package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.ProjectConfig;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.Utiltools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class UpdateDialog extends Dialog implements View.OnClickListener {
    public static final int APP_UPDATE = 4;
    public static final int CANBOX_UPDATE = 3;
    public static final int MCU_UPDATE = 2;
    public static final int SN_UPDATE = 5;
    public static final int SYSTEM_UPDATE = 1;
    /* access modifiers changed from: private */
    public LauncherApplication app;
    private Button cancelBtn;
    /* access modifiers changed from: private */
    public Context context;
    private TextView curInfoTview;
    private TextView curTypeTview;
    public int currentType = 1;
    /* access modifiers changed from: private */
    public String currentUpdatePath;
    private boolean iFinishUpdate = false;
    private boolean iInCancelState = true;
    public MHandler mHandler = new MHandler(this);
    private BroadcastReceiver onlineDownloadReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.e("UpdateDialog", "onlineDownloadReceiver  getAction " + intent.getAction());
            if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                Utiltools.onlineOtaUpdate();
            }
        }
    };
    private Button submitBtn;
    public String url = ProjectConfig.CONFIG_URL;

    public UpdateDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public UpdateDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_update);
        this.app = (LauncherApplication) this.context.getApplicationContext();
        this.curInfoTview = (TextView) findViewById(R.id.curInfo);
        this.curTypeTview = (TextView) findViewById(R.id.curType);
        this.submitBtn = (Button) findViewById(R.id.affirmBtn);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);
        this.submitBtn.setOnClickListener(this);
        this.cancelBtn.setOnClickListener(this);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.x = 400;
        layoutParams.y = 0;
        dialogWindow.setAttributes(layoutParams);
        this.submitBtn.setVisibility(0);
        this.cancelBtn.setVisibility(0);
        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        checkUpdateFile();
        if (this.currentType == 1) {
            this.curTypeTview.setText("SYSTEM UPDATE");
        } else if (this.currentType == 3) {
            this.curTypeTview.setText("CANBOX UPDATE");
        } else if (this.currentType == 2) {
            this.curTypeTview.setText("MCU UPDATE");
        } else if (this.currentType == 4) {
            this.curTypeTview.setText("APP UPDATE");
        } else if (this.currentType == 5) {
            this.curTypeTview.setText("ONLINE CONFIG UPDATE");
        }
        this.app.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.app.unregisterHandler(this.mHandler);
        super.onStop();
    }

    public void dismiss() {
        this.app.currentDialog3 = null;
        this.app.mHomeAndBackEnable = true;
        super.dismiss();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.cancelBtn) {
            dismiss();
        } else if (this.iFinishUpdate) {
            dismiss();
        } else if (this.currentType == 5) {
            getOnlineConfig();
        } else {
            startUpdate();
        }
    }

    private void checkUpdateFile() {
        this.submitBtn.setEnabled(false);
        if (this.currentType == 1) {
            startSystemCheckThread();
        } else if (this.currentType == 3) {
            startCanboxCheckThread();
        } else if (this.currentType == 2) {
            startMcuCheckThread();
        } else if (this.currentType == 4) {
            startAPPCheckThread();
        } else if (this.currentType == 5) {
            startSNCheckThread();
        }
    }

    private void getOnlineConfig() {
        setCanceledOnTouchOutside(false);
        this.submitBtn.setVisibility(8);
        this.cancelBtn.setVisibility(8);
        this.curInfoTview.setText(this.context.getString(R.string.msg_loading));
        this.app.mHomeAndBackEnable = false;
        addOnlineDownloadReceiver();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(this.url));
        getFile();
        request.setDestinationInExternalPublicDir("update", "update_config.sh");
        long enqueue = ((DownloadManager) this.context.getSystemService("download")).enqueue(request);
    }

    private void addOnlineDownloadReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
        this.context.registerReceiver(this.onlineDownloadReceiver, mFilter);
    }

    private boolean getFile() {
        File folder = new File("update");
        if (!folder.exists() || !folder.isDirectory()) {
            return folder.mkdirs();
        }
        return true;
    }

    private void startUpdate() {
        if (!TextUtils.isEmpty(this.currentUpdatePath)) {
            setCanceledOnTouchOutside(false);
            this.submitBtn.setVisibility(8);
            this.cancelBtn.setVisibility(8);
            this.curInfoTview.setText(this.context.getString(R.string.msg_upgrade_copy_file));
            this.app.mHomeAndBackEnable = false;
            if (this.currentType == 1) {
                new Thread(new Runnable() {
                    public void run() {
                        if (UpdateDialog.this.copyFile(UpdateDialog.this.currentUpdatePath, UpdateDialog.this.context.getString(R.string.path_upgrade_system)).booleanValue()) {
                            UpdateDialog.this.androidUpdate();
                        } else {
                            UpdateDialog.this.mHandler.obtainMessage(SysConst.COPY_ERROR).sendToTarget();
                        }
                    }
                }).start();
            } else if (this.currentType == 3) {
                copyFile(this.currentUpdatePath, this.context.getString(R.string.path_upgrade_canbox));
                deleteFile(this.context.getString(R.string.path_upgrade_local_canbox));
                this.app.service.enterIntoUpgradeCanboxCheck();
            } else if (this.currentType == 2) {
                copyFile(this.currentUpdatePath, this.context.getString(R.string.path_upgrade_mcu));
                deleteFile(this.context.getString(R.string.path_upgrade_local_mcu));
                this.app.service.enterIntoUpgradeMcuCheck();
            } else if (this.currentType == 4) {
                new Thread(new Runnable() {
                    public void run() {
                        if (UpdateDialog.this.copyFile(UpdateDialog.this.currentUpdatePath, UpdateDialog.this.context.getString(R.string.path_upgrade_app)).booleanValue()) {
                            Utiltools.updateLauncherAPK();
                        } else {
                            UpdateDialog.this.mHandler.obtainMessage(SysConst.COPY_ERROR).sendToTarget();
                        }
                    }
                }).start();
            }
        }
    }

    private void startSNCheckThread() {
        new Thread(new Runnable() {
            public void run() {
                UpdateDialog.this.mHandler.obtainMessage(SysConst.ONLINE_CONFIG_DATA).sendToTarget();
            }
        }).start();
    }

    private void startSystemCheckThread() {
        new Thread(new Runnable() {
            public void run() {
                UpdateDialog.this.scanUsbAndSdcard(ProjectConfig.systemSoft);
                UpdateDialog.this.mHandler.obtainMessage(SysConst.UPGRADE_CHECK_FINISH).sendToTarget();
            }
        }).start();
    }

    private void startMcuCheckThread() {
        new Thread(new Runnable() {
            public void run() {
                if (UpdateDialog.this.app.playpos == 1) {
                    ProjectConfig.MCUSoft = "BENZ_AMP_MCU.BIN";
                }
                UpdateDialog.this.scanUsbAndSdcard(ProjectConfig.MCUSoft);
                UpdateDialog.this.mHandler.obtainMessage(SysConst.UPGRADE_CHECK_FINISH).sendToTarget();
            }
        }).start();
    }

    private void startCanboxCheckThread() {
        new Thread(new Runnable() {
            public void run() {
                UpdateDialog.this.scanUsbAndSdcard(ProjectConfig.CANSoft);
                UpdateDialog.this.mHandler.obtainMessage(SysConst.UPGRADE_CHECK_FINISH).sendToTarget();
            }
        }).start();
    }

    private void startAPPCheckThread() {
        new Thread(new Runnable() {
            public void run() {
                UpdateDialog.this.scanUsbAndSdcard(ProjectConfig.APPSoft);
                UpdateDialog.this.mHandler.obtainMessage(SysConst.UPGRADE_CHECK_FINISH).sendToTarget();
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void scanUsbAndSdcard(String filename) {
        if (!scanUsbDiskUpdateFile(new File(this.context.getString(R.string.path_upgrade_file)), filename)) {
            int i = 0;
            while (i < 3 && !scanUsbDiskUpdateFile(LauncherApplication.usbpaths.get(i), filename)) {
                i++;
            }
        }
    }

    private boolean scanUsbDiskUpdateFile(File folder, String flag) {
        String[] filenames = folder.list();
        if (filenames == null) {
            return false;
        }
        for (String name : filenames) {
            try {
                File file = new File(folder, name);
                if (file.isFile()) {
                    String filePath = file.getAbsolutePath();
                    if (filePath.contains(flag)) {
                        this.currentUpdatePath = filePath;
                        return true;
                    }
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Boolean copyFile(String oldPath, String newPath) {
        try {
            if (new File(oldPath).exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                int begin = inStream.available();
                byte[] buffer = new byte[8192];
                while (true) {
                    int byteread = inStream.read(buffer);
                    if (byteread == -1) {
                        inStream.close();
                        fs.close();
                        return true;
                    }
                    fs.write(buffer, 0, byteread);
                    int per = (int) (((((double) inStream.available()) * 1.0d) / ((double) begin)) * 100.0d);
                    if (per <= 100 && per >= 0) {
                        this.mHandler.obtainMessage(SysConst.UPGRADE_COPY_PER, Integer.valueOf(100 - per)).sendToTarget();
                    }
                }
            } else {
                Log.d("", "file not  exist...........");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void androidUpdate() {
        new Thread("Reboot") {
            public void run() {
                try {
                    File packageFile = new File("/SDCARD/update.zip");
                    RecoverySystem.handleAftermath();
                    RecoverySystem.installPackage(UpdateDialog.this.context, packageFile);
                } catch (IOException e) {
                }
            }
        }.start();
    }

    private void deleteFile(final String path) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    static class MHandler extends Handler {
        private WeakReference<UpdateDialog> target;

        public MHandler(UpdateDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((UpdateDialog) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || idriverCode == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                this.iInCancelState = true;
            } else if (idriverCode == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || idriverCode == Mainboard.EIdriverEnum.LEFT.getCode()) {
                this.iInCancelState = false;
            } else if (idriverCode == Mainboard.EIdriverEnum.PRESS.getCode()) {
                if (this.iInCancelState) {
                    dismiss();
                } else if (this.currentType == 5) {
                    getOnlineConfig();
                } else {
                    startUpdate();
                }
            } else if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        } else if (msg.what == 1111) {
            if (TextUtils.isEmpty(this.currentUpdatePath)) {
                this.curInfoTview.setText(this.context.getString(R.string.msg_check_no_upgrade_file));
                return;
            }
            this.curInfoTview.setText(this.context.getString(R.string.msg_check_had_upgrade_file));
            this.submitBtn.setEnabled(true);
        } else if (msg.what == 1124) {
            this.curInfoTview.setText(this.context.getString(R.string.msg_check_had_upgrade_file));
            this.submitBtn.setEnabled(true);
        } else if (msg.what == 1128) {
            this.curInfoTview.setText(this.context.getString(R.string.msg_check_out_of_memory));
            setCanceledOnTouchOutside(true);
            this.cancelBtn.setVisibility(0);
        } else if (msg.what == 1129) {
            this.curInfoTview.setText(this.context.getString(R.string.msg_check_upgrade_error));
            setCanceledOnTouchOutside(true);
            this.cancelBtn.setVisibility(0);
        } else if (msg.what == 1131 || msg.what == 1121) {
            this.app.mHomeAndBackEnable = true;
            this.iFinishUpdate = true;
            setCanceledOnTouchOutside(true);
            this.submitBtn.setEnabled(true);
            this.cancelBtn.setEnabled(true);
            this.submitBtn.setVisibility(8);
            this.cancelBtn.setText(R.string.close);
            this.cancelBtn.setVisibility(0);
            this.curInfoTview.setText(this.context.getString(R.string.msg_upgrade_finish));
            if (this.currentType == 2) {
                Mainboard.getInstance().getMCUVersionNumber();
                deleteFile(this.context.getString(R.string.path_upgrade_mcu));
            } else if (this.currentType == 3) {
                Mainboard.getInstance().getModeInfo(Mainboard.EModeInfo.CANBOX_INFO);
                deleteFile(this.context.getString(R.string.path_upgrade_canbox));
            }
            dismiss();
        } else if (msg.what == 1112) {
            this.curInfoTview.setText(String.valueOf(this.context.getString(R.string.msg_upgrade_copy_file)) + "(" + msg.obj + "%)");
        } else if (msg.what == 1113) {
            this.curInfoTview.setText(String.valueOf(this.context.getString(R.string.msg_upgrading)) + msg.getData().getString(SysConst.FLAG_UPGRADE_PER));
        } else {
            int i = msg.what;
        }
    }
}
