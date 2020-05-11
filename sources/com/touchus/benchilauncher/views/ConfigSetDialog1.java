package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.benchilauncher.views.MyCustomDialog;
import java.lang.ref.WeakReference;

public class ConfigSetDialog1 extends Dialog {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EAUXStutas;
    private Spinner configure_model;
    private RadioGroup connect_type;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public LauncherApplication mApp;
    public ReverseialogHandler mHandler = new ReverseialogHandler(this);
    private Launcher mMMainActivity;
    /* access modifiers changed from: private */
    public SpUtilK mSpUtilK;
    /* access modifiers changed from: private */
    public MyCustomDialog myCustomDialog;
    private RadioGroup original_navi;
    private LinearLayout play_model_layout;
    private RadioGroup play_type;
    private String[] screenType = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    /* access modifiers changed from: private */
    public LinearLayout stripe_layout;
    private int type = 1;

    static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EAUXStutas() {
        int[] iArr = $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EAUXStutas;
        if (iArr == null) {
            iArr = new int[Mainboard.EAUXStutas.values().length];
            try {
                iArr[Mainboard.EAUXStutas.ACTIVATING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Mainboard.EAUXStutas.FAILED.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Mainboard.EAUXStutas.SUCCEED.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EAUXStutas = iArr;
        }
        return iArr;
    }

    public ConfigSetDialog1(Context context2) {
        super(context2);
    }

    public ConfigSetDialog1(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_config_layout1);
        this.mApp = (LauncherApplication) this.context.getApplicationContext();
        initView();
        initData();
        initSetup();
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

    private void initSetup() {
        this.connect_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.connect_BT:
                        ConfigSetDialog1.this.mApp.connectPos = 0;
                        ConfigSetDialog1.this.mApp.connectPos1 = 0;
                        LauncherApplication.isBT = true;
                        break;
                    case R.id.connect_AUX:
                        if (ConfigSetDialog1.this.mApp.connectPos != 1) {
                            ConfigSetDialog1.this.mApp.connectPos1 = 0;
                            ConfigSetDialog1.this.mApp.radioPos = 0;
                            LauncherApplication.isBT = false;
                            ConfigSetDialog1.this.showBluetoothDialog(ConfigSetDialog1.this.context.getString(R.string.string_original_media), ConfigSetDialog1.this.context.getString(R.string.string_exist_aux), ConfigSetDialog1.this.context.getString(R.string.name_have), ConfigSetDialog1.this.context.getString(R.string.name_not_have), true);
                            break;
                        } else {
                            return;
                        }
                    case R.id.connect_USB:
                        ConfigSetDialog1.this.mApp.connectPos = 0;
                        ConfigSetDialog1.this.mApp.connectPos1 = 1;
                        ConfigSetDialog1.this.mApp.radioPos = 1;
                        LauncherApplication.isBT = false;
                        break;
                }
                SysConst.storeData[4] = (byte) ConfigSetDialog1.this.mApp.radioPos;
                ConfigSetDialog1.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_RADIO, ConfigSetDialog1.this.mApp.radioPos);
                SysConst.storeData[6] = (byte) ConfigSetDialog1.this.mApp.connectPos;
                ConfigSetDialog1.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_CONNECT, ConfigSetDialog1.this.mApp.connectPos);
                SysConst.storeData[10] = (byte) ConfigSetDialog1.this.mApp.connectPos1;
                ConfigSetDialog1.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_CONNECT1, ConfigSetDialog1.this.mApp.connectPos1);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            }
        });
        this.original_navi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.navi_have:
                        ConfigSetDialog1.this.mApp.naviPos = 0;
                        break;
                    case R.id.navi_no:
                        ConfigSetDialog1.this.mApp.naviPos = 1;
                        break;
                }
                SysConst.storeData[5] = (byte) ConfigSetDialog1.this.mApp.naviPos;
                ConfigSetDialog1.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_NAVI, ConfigSetDialog1.this.mApp.naviPos);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            }
        });
        this.play_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.play_original:
                        ConfigSetDialog1.this.mApp.playpos = 0;
                        ConfigSetDialog1.this.stripe_layout.setVisibility(0);
                        break;
                    case R.id.play_power_amplifier:
                        ConfigSetDialog1.this.mApp.playpos = 1;
                        ConfigSetDialog1.this.mApp.ismix = true;
                        SysConst.storeData[3] = 0;
                        LauncherApplication.isBT = false;
                        ConfigSetDialog1.this.stripe_layout.setVisibility(8);
                        break;
                }
                SysConst.storeData[12] = (byte) ConfigSetDialog1.this.mApp.playpos;
                ConfigSetDialog1.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_PLAY, ConfigSetDialog1.this.mApp.playpos);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            }
        });
        this.configure_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Mainboard.getInstance().setBenzSize(position);
                if (position == 6) {
                    Mainboard.getInstance().setOldCBTAudio();
                }
                ConfigSetDialog1.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_SCREEN, position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (this.mApp.connectPos < 0 || this.mApp.connectPos > 1) {
            this.mApp.connectPos = 0;
        }
        if (this.mApp.connectPos1 < 0 || this.mApp.connectPos1 > 1) {
            this.mApp.connectPos1 = 0;
        }
        Log.e("", "connect_type.size = " + this.connect_type.getChildCount());
        if (this.mApp.connectPos == 1) {
            ((RadioButton) this.connect_type.getChildAt(2)).setChecked(true);
        } else if (this.mApp.connectPos1 == 1) {
            ((RadioButton) this.connect_type.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) this.connect_type.getChildAt(0)).setChecked(true);
        }
        if (this.mApp.naviPos < 0 || this.mApp.naviPos > 1) {
            this.mApp.naviPos = 0;
        }
        ((RadioButton) this.original_navi.getChildAt(this.mApp.naviPos)).setChecked(true);
        if (this.mApp.playpos == 1) {
            ((RadioButton) this.play_type.getChildAt(this.mApp.playpos)).setChecked(true);
            this.stripe_layout.setVisibility(8);
        } else {
            ((RadioButton) this.play_type.getChildAt(0)).setChecked(true);
            this.stripe_layout.setVisibility(0);
        }
        this.configure_model.setSelection(this.mApp.screenPos);
    }

    private void initView() {
        this.play_model_layout = (LinearLayout) findViewById(R.id.play_model_layout);
        this.stripe_layout = (LinearLayout) findViewById(R.id.stripe_layout);
        this.original_navi = (RadioGroup) findViewById(R.id.original_navi);
        this.play_type = (RadioGroup) findViewById(R.id.play_type);
        this.connect_type = (RadioGroup) findViewById(R.id.connect_type);
        this.configure_model = (Spinner) findViewById(R.id.configure_model);
        this.play_model_layout.setVisibility(0);
    }

    private void initData() {
        this.mMMainActivity = (Launcher) this.context;
        this.mSpUtilK = new SpUtilK((Context) this.mMMainActivity);
        ArrayAdapter<String> screenAdapter = new ArrayAdapter<>(this.context, R.layout.spinner_textview, this.screenType);
        screenAdapter.setDropDownViewResource(R.layout.spinner_textview);
        this.configure_model.setAdapter(screenAdapter);
        this.mApp.connectPos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_CONNECT, 0);
        this.mApp.connectPos1 = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_CONNECT1, 0);
        this.mApp.playpos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_PLAY, 0);
    }

    /* access modifiers changed from: private */
    public void setConnectType() {
        this.mApp.connectPos = 1;
        LauncherApplication.isBT = false;
        SysConst.storeData[6] = (byte) this.mApp.connectPos;
        this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_CONNECT, this.mApp.connectPos);
        Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
    }

    /* access modifiers changed from: private */
    public void showBluetoothDialog(String Messsage, String nextMessage, String btnPosiText, String btnNegaText, final boolean flag) {
        final MyCustomDialog.Builder builder = new MyCustomDialog.Builder(this.context);
        builder.setMessage(Messsage);
        builder.setNextMessage(nextMessage);
        builder.setPositiveButton(btnPosiText, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (flag) {
                    ConfigSetDialog1.this.setConnectType();
                    return;
                }
                Mainboard.getInstance().requestAUXOperate(Mainboard.EAUXOperate.ACTIVATE);
                ConfigSetDialog1.this.showBluetoothDialog(ConfigSetDialog1.this.context.getString(R.string.string_activating_aux), ConfigSetDialog1.this.context.getString(R.string.string_do_not_do_anything), (String) null, (String) null, false);
            }
        });
        builder.setNegativeButton(btnNegaText, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (flag) {
                    ConfigSetDialog1.this.showBluetoothDialog("", ConfigSetDialog1.this.context.getString(R.string.string_activate_aux), ConfigSetDialog1.this.context.getString(R.string.string_start), ConfigSetDialog1.this.context.getString(R.string.string_cancel), false);
                }
            }
        });
        if (this.myCustomDialog != null) {
            this.myCustomDialog.dismiss();
        }
        this.myCustomDialog = builder.create();
        this.myCustomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                builder.unRegisterHandler();
                ConfigSetDialog1.this.myCustomDialog = null;
            }
        });
        setDialogLocation(this.myCustomDialog);
        this.myCustomDialog.show();
    }

    private void setDialogLocation(Dialog myCustomDialog2) {
        Window dialogWindow = myCustomDialog2.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.x = 256;
        layoutParams.y = -30;
        dialogWindow.setAttributes(layoutParams);
    }

    public void unregisterHandlerr() {
        this.mApp.unregisterHandler(this.mHandler);
    }

    static class ReverseialogHandler extends Handler {
        private WeakReference<ConfigSetDialog1> target;

        public ReverseialogHandler(ConfigSetDialog1 activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((ConfigSetDialog1) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        } else if (msg.what == 1048) {
            switch ($SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EAUXStutas()[((Mainboard.EAUXStutas) msg.getData().getSerializable(SysConst.FLAG_AUX_ACTIVATE_STUTAS)).ordinal()]) {
                case 1:
                    if (this.myCustomDialog != null) {
                        this.myCustomDialog.dismiss();
                    }
                    showBluetoothDialog(this.context.getString(R.string.string_activating_aux), this.context.getString(R.string.string_do_not_do_anything), (String) null, (String) null, false);
                    return;
                case 2:
                    if (this.myCustomDialog != null) {
                        this.myCustomDialog.dismiss();
                    }
                    showBluetoothDialog(this.context.getString(R.string.string_oper_success), this.context.getString(R.string.string_wait_restart), (String) null, (String) null, false);
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (ConfigSetDialog1.this.myCustomDialog != null) {
                                ConfigSetDialog1.this.myCustomDialog.dismiss();
                            }
                        }
                    }, 2000);
                    return;
                case 3:
                    if (this.myCustomDialog != null) {
                        this.myCustomDialog.dismiss();
                    }
                    showBluetoothDialog(this.context.getString(R.string.string_oper_fail), "", (String) null, (String) null, true);
                    return;
                default:
                    return;
            }
        }
    }
}
