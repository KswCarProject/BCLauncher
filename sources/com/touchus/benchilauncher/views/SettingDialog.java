package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.adapter.DialogItemAdapter;
import com.touchus.benchilauncher.utils.DialogUtil;
import com.touchus.benchilauncher.views.BrightnessDialog;
import com.touchus.benchilauncher.views.SoundSetDialog;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SettingDialog extends Dialog {
    public SettingDialog(Context context) {
        super(context);
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    public static class Builder {
        private TextView benzconfig;
        /* access modifiers changed from: private */
        public int clickCount = 0;
        private Context context;
        /* access modifiers changed from: private */
        public int currentSelectIndex = 0;
        private DialogItemAdapter mAdapter;
        /* access modifiers changed from: private */
        public LauncherApplication mApp;
        private SettingDialog mDialog;
        private Launcher mLauncher;
        public SettingHandler mSettingHandler = new SettingHandler(this);
        private ListView mlistView;
        Thread resetClickCountThread = new Thread(new Runnable() {
            public void run() {
                Builder.this.clickCount = 0;
            }
        });
        private List<String> strings;

        public Builder(Context context2) {
            this.context = context2;
        }

        public SettingDialog create() {
            this.mLauncher = (Launcher) this.context;
            this.mDialog = new SettingDialog(this.context, R.style.Dialog);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.dialog_setting01_layout, (ViewGroup) null);
            this.mDialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            this.mlistView = (ListView) layout.findViewById(R.id.list);
            this.benzconfig = (TextView) layout.findViewById(R.id.benzconfig);
            this.strings = new ArrayList();
            this.strings.add(this.context.getString(R.string.name_setting_navi));
            this.strings.add(this.context.getString(R.string.name_brightness));
            this.strings.add(this.context.getString(R.string.name_setting_sound));
            if (Build.MODEL.contains("c200_jly") && !LauncherApplication.isBT) {
                this.strings.add(this.context.getString(R.string.name_setting_effet));
                this.mlistView.setLayoutParams(new LinearLayout.LayoutParams(313, 410));
            }
            this.strings.add(this.context.getString(R.string.name_setting_language));
            this.strings.add(this.context.getString(R.string.name_setting_reverse));
            this.strings.add(this.context.getString(R.string.name_setting_wifi));
            this.strings.add(this.context.getString(R.string.name_setting_system));
            this.strings.add(this.context.getString(R.string.name_setting_version));
            this.mAdapter = new DialogItemAdapter(this.context, this.strings);
            this.mAdapter.setSeclectIndex(this.currentSelectIndex);
            this.mlistView.setAdapter(this.mAdapter);
            this.mlistView.setLayoutParams(new LinearLayout.LayoutParams(313, 410));
            this.mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    Builder.this.currentSelectIndex = arg2;
                    Builder.this.clickItem(Builder.this.currentSelectIndex);
                }
            });
            this.benzconfig.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!UtilTools.isFastDoubleClick()) {
                        Builder.this.clickCount = 0;
                    } else {
                        Builder builder = Builder.this;
                        builder.clickCount = builder.clickCount + 1;
                        if (Builder.this.clickCount >= 2) {
                            Builder.this.clickCount = 0;
                            if (BenzModel.benzCan == BenzModel.EBenzCAN.ZMYT) {
                                Builder.this.artConfig();
                            } else {
                                Builder.this.artConfig1();
                            }
                        }
                    }
                    if (Builder.this.clickCount != 0) {
                        Builder.this.mSettingHandler.removeCallbacks(Builder.this.resetClickCountThread);
                        Builder.this.mSettingHandler.postDelayed(Builder.this.resetClickCountThread, 1000);
                    }
                }
            });
            this.currentSelectIndex = 0;
            seclectTrue();
            this.mApp = (LauncherApplication) this.mLauncher.getApplication();
            RigisterHandler();
            this.mDialog.setContentView(layout);
            return this.mDialog;
        }

        static class SettingHandler extends Handler {
            private WeakReference<Builder> target;

            public SettingHandler(Builder activity) {
                this.target = new WeakReference<>(activity);
            }

            public void handleMessage(Message msg) {
                if (this.target.get() != null) {
                    ((Builder) this.target.get()).handlerMsgUSB(msg);
                }
            }
        }

        public void handlerMsgUSB(Message msg) {
            if (msg.what == 6001) {
                byte code = msg.getData().getByte(SysConst.IDRIVER_ENUM);
                if (code == Mainboard.EIdriverEnum.TURN_RIGHT.getCode()) {
                    if (this.currentSelectIndex < this.strings.size() - 1) {
                        this.currentSelectIndex++;
                    }
                    seclectTrue();
                } else if (code == Mainboard.EIdriverEnum.TURN_LEFT.getCode()) {
                    if (this.currentSelectIndex > 0) {
                        this.currentSelectIndex--;
                    }
                    seclectTrue();
                } else if (code == Mainboard.EIdriverEnum.PRESS.getCode()) {
                    pressBTItem(this.currentSelectIndex);
                } else if (code == Mainboard.EIdriverEnum.BACK.getCode() || code == Mainboard.EIdriverEnum.HOME.getCode() || code == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                    this.mDialog.dismiss();
                } else if (code == Mainboard.EIdriverEnum.DOWN.getCode()) {
                    if (this.currentSelectIndex < this.strings.size() - 1) {
                        this.currentSelectIndex++;
                    }
                    seclectTrue();
                } else if (code == Mainboard.EIdriverEnum.UP.getCode()) {
                    if (this.currentSelectIndex > 0) {
                        this.currentSelectIndex--;
                    }
                    seclectTrue();
                }
            }
        }

        private void pressBTItem(int currentSelectIndex2) {
            if (!UtilTools.isFastDoubleClick()) {
                if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_navi))) {
                    artAppset();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_brightness))) {
                    artBrightness();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_sound))) {
                    artSound();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_language))) {
                    artLanguage();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_reverse))) {
                    artDaoche();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_wifi))) {
                    artNetwork();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_system))) {
                    artSystem();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_version))) {
                    artVersion();
                } else if (TextUtils.equals(this.strings.get(currentSelectIndex2), this.context.getString(R.string.name_setting_effet))) {
                    artYinxiao();
                }
            }
        }

        /* access modifiers changed from: private */
        public void clickItem(int index) {
            this.currentSelectIndex = index;
            seclectTrue();
            pressBTItem(this.currentSelectIndex);
        }

        private void seclectTrue() {
            this.mAdapter.setSeclectIndex(this.currentSelectIndex);
            this.mAdapter.notifyDataSetChanged();
            this.mlistView.setSelection(this.currentSelectIndex);
        }

        public void RigisterHandler() {
            if (this.mApp != null) {
                this.mApp.registerHandler(this.mSettingHandler);
            }
        }

        public void unRigisterHandler() {
            if (this.mApp != null) {
                this.mApp.unregisterHandler(this.mSettingHandler);
            }
        }

        private void artBrightness() {
            final BrightnessDialog.Builder builder = new BrightnessDialog.Builder(this.context);
            BrightnessDialog SettingDialog = builder.create();
            DialogUtil.setDialogLocation(SettingDialog, 300, -20);
            SettingDialog.show();
            this.mApp.currentDialog2 = SettingDialog;
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    builder.unregisterHandlerr();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artSound() {
            final SoundSetDialog.Builder builder = new SoundSetDialog.Builder(this.context);
            SoundSetDialog SettingDialog = builder.create();
            DialogUtil.setDialogLocation(SettingDialog, 300, -20);
            SettingDialog.show();
            this.mApp.currentDialog2 = SettingDialog;
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    builder.unregisterHandlerr();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artYinxiao() {
            EQDialog SettingDialog = new EQDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artLanguage() {
            LanguageDialog SettingDialog = new LanguageDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artSystem() {
            SystemSetDialog SettingDialog = new SystemSetDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        /* access modifiers changed from: private */
        public void artConfig() {
            ConfigSetDialog SettingDialog = new ConfigSetDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        /* access modifiers changed from: private */
        public void artConfig1() {
            ConfigSetDialog1 SettingDialog = new ConfigSetDialog1(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artDaoche() {
            ReverseDialog SettingDialog = new ReverseDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artVersion() {
            final XitongDialog SettingDialog = new XitongDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    SettingDialog.unregisterHandlerr();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artNetwork() {
            NetworkDialog SettingDialog = new NetworkDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }

        private void artAppset() {
            AppSettingDialog SettingDialog = new AppSettingDialog(this.context, R.style.Dialog);
            this.mApp.currentDialog2 = SettingDialog;
            DialogUtil.setDialogLocation(SettingDialog, 400, 0);
            SettingDialog.show();
            unRigisterHandler();
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    Builder.this.RigisterHandler();
                    Builder.this.mApp.currentDialog2 = null;
                }
            });
        }
    }
}
