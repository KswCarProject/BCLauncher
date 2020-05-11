package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.benchilauncher.utils.Utiltools;
import com.touchus.benchilauncher.views.MyCustomDialog;
import java.lang.ref.WeakReference;

public class ConfigSetDialog extends Dialog implements View.OnClickListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EAUXStutas;
    private Spinner configure_model;
    /* access modifiers changed from: private */
    public LinearLayout configure_view;
    private RadioGroup connect_type;
    private RadioGroup connect_type1;
    /* access modifiers changed from: private */
    public Context context;
    private TextView help;
    /* access modifiers changed from: private */
    public LauncherApplication mApp;
    public ReverseialogHandler mHandler = new ReverseialogHandler(this);
    private Launcher mMMainActivity;
    /* access modifiers changed from: private */
    public SpUtilK mSpUtilK;
    /* access modifiers changed from: private */
    public MyCustomDialog myCustomDialog;
    private RadioGroup original_navi;
    private RadioGroup play_type;
    private RadioGroup radioset;
    private String[] screenType = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private RadioButton square;
    private LinearLayout square_layout;
    private RadioButton stripe;
    private LinearLayout stripe_layout;
    /* access modifiers changed from: private */
    public int type = 1;
    private String[] usbNum = {"1", "2"};
    private Spinner usb_configure;

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

    public ConfigSetDialog(Context context2) {
        super(context2);
    }

    public ConfigSetDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_config_layout);
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
        this.stripe.setOnClickListener(this);
        this.square.setOnClickListener(this);
        if (!Utiltools.isAvilible(this.context, "com.unibroad.benzuserguide")) {
            this.help.setVisibility(8);
        }
        this.help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.unibroad.help");
                intent.putExtra("page", 10);
                ConfigSetDialog.this.context.startActivity(intent);
            }
        });
        this.play_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.play_original:
                        ConfigSetDialog.this.mApp.playpos = 0;
                        ConfigSetDialog.this.configure_view.setVisibility(0);
                        break;
                    case R.id.play_power_amplifier:
                        ConfigSetDialog.this.mApp.playpos = 1;
                        ConfigSetDialog.this.mApp.ismix = true;
                        SysConst.storeData[3] = 0;
                        LauncherApplication.isBT = false;
                        ConfigSetDialog.this.configure_view.setVisibility(8);
                        break;
                }
                SysConst.storeData[12] = (byte) ConfigSetDialog.this.mApp.playpos;
                ConfigSetDialog.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_PLAY, ConfigSetDialog.this.mApp.playpos);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            }
        });
        this.radioset.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.stripe:
                        ConfigSetDialog.this.mApp.radioPos = 0;
                        break;
                    case R.id.square:
                        ConfigSetDialog.this.mApp.radioPos = 1;
                        break;
                }
                ConfigSetDialog.this.type = 1;
                ConfigSetDialog.this.seclectTrue(ConfigSetDialog.this.mApp.radioPos);
            }
        });
        this.original_navi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.navi_have:
                        ConfigSetDialog.this.mApp.naviPos = 0;
                        break;
                    case R.id.navi_no:
                        ConfigSetDialog.this.mApp.naviPos = 1;
                        break;
                }
                SysConst.storeData[5] = (byte) ConfigSetDialog.this.mApp.naviPos;
                ConfigSetDialog.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_NAVI, ConfigSetDialog.this.mApp.naviPos);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            }
        });
        this.connect_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.connect_BT:
                        ConfigSetDialog.this.mApp.connectPos = 0;
                        LauncherApplication.isBT = true;
                        SysConst.storeData[6] = (byte) ConfigSetDialog.this.mApp.connectPos;
                        ConfigSetDialog.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_CONNECT, ConfigSetDialog.this.mApp.connectPos);
                        Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
                        return;
                    case R.id.connect_AUX:
                        if (ConfigSetDialog.this.mApp.connectPos != 1) {
                            ConfigSetDialog.this.showBluetoothDialog(ConfigSetDialog.this.context.getString(R.string.string_original_media), ConfigSetDialog.this.context.getString(R.string.string_exist_aux), ConfigSetDialog.this.context.getString(R.string.name_have), ConfigSetDialog.this.context.getString(R.string.name_not_have), true);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        });
        this.connect_type1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.connect_BT1:
                        ConfigSetDialog.this.mApp.connectPos1 = 0;
                        LauncherApplication.isBT = true;
                        break;
                    case R.id.connect_usb:
                        ConfigSetDialog.this.mApp.connectPos1 = 1;
                        LauncherApplication.isBT = false;
                        break;
                }
                SysConst.storeData[10] = (byte) ConfigSetDialog.this.mApp.connectPos1;
                ConfigSetDialog.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_CONNECT1, ConfigSetDialog.this.mApp.connectPos1);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            }
        });
        this.configure_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Mainboard.getInstance().setBenzSize(position);
                if (position == 6) {
                    Mainboard.getInstance().setOldCBTAudio();
                }
                ConfigSetDialog.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_SCREEN, position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.usb_configure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SysConst.storeData[7] = (byte) position;
                ConfigSetDialog.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_USB_NUM, position);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        seclectTrue(this.mApp.radioPos);
        if (this.mApp.naviPos < 0 || this.mApp.naviPos > 1) {
            this.mApp.naviPos = 0;
        }
        ((RadioButton) this.original_navi.getChildAt(this.mApp.naviPos)).setChecked(true);
        if (this.mApp.connectPos < 0 || this.mApp.connectPos > 1) {
            this.mApp.connectPos = 0;
        }
        if (this.mApp.connectPos1 < 0 || this.mApp.connectPos1 > 1) {
            this.mApp.connectPos1 = 0;
        }
        Log.e("", "connect_type.size = " + this.connect_type.getChildCount());
        ((RadioButton) this.connect_type.getChildAt(this.mApp.connectPos)).setChecked(true);
        ((RadioButton) this.connect_type1.getChildAt(this.mApp.connectPos1)).setChecked(true);
        if (this.mApp.usbPos < 0 || this.mApp.usbPos > this.usbNum.length) {
            this.mApp.usbPos = 0;
        }
        if (this.mApp.playpos == 1) {
            ((RadioButton) this.play_type.getChildAt(this.mApp.playpos)).setChecked(true);
            this.configure_view.setVisibility(8);
        } else {
            ((RadioButton) this.play_type.getChildAt(0)).setChecked(true);
            this.configure_view.setVisibility(0);
        }
        this.usb_configure.setSelection(this.mApp.usbPos);
        this.configure_model.setSelection(this.mApp.screenPos);
    }

    private void initView() {
        this.configure_view = (LinearLayout) findViewById(R.id.configure_view);
        this.help = (TextView) findViewById(R.id.help);
        this.play_type = (RadioGroup) findViewById(R.id.play_type);
        this.radioset = (RadioGroup) findViewById(R.id.radioset);
        this.original_navi = (RadioGroup) findViewById(R.id.original_navi);
        this.connect_type = (RadioGroup) findViewById(R.id.connect_type);
        this.connect_type1 = (RadioGroup) findViewById(R.id.connect_type1);
        this.stripe = (RadioButton) findViewById(R.id.stripe);
        this.square = (RadioButton) findViewById(R.id.square);
        this.stripe_layout = (LinearLayout) findViewById(R.id.stripe_layout);
        this.square_layout = (LinearLayout) findViewById(R.id.square_layout);
        this.configure_model = (Spinner) findViewById(R.id.configure_model);
        this.usb_configure = (Spinner) findViewById(R.id.usb_configure);
    }

    private void initData() {
        this.mMMainActivity = (Launcher) this.context;
        this.mSpUtilK = new SpUtilK((Context) this.mMMainActivity);
        ArrayAdapter<String> screenAdapter = new ArrayAdapter<>(this.context, R.layout.spinner_textview, this.screenType);
        screenAdapter.setDropDownViewResource(R.layout.spinner_textview);
        this.configure_model.setAdapter(screenAdapter);
        ArrayAdapter<String> usbNumAdapter = new ArrayAdapter<>(this.context, R.layout.spinner_textview, this.usbNum);
        usbNumAdapter.setDropDownViewResource(R.layout.spinner_textview);
        this.usb_configure.setAdapter(usbNumAdapter);
        this.mApp.radioPos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_RADIO, 0);
        this.mApp.naviPos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_NAVI, 0);
        this.mApp.usbPos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_USB_NUM, 0);
        this.mApp.connectPos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_CONNECT, 0);
        this.mApp.screenPos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_SCREEN, 0);
        this.mApp.playpos = this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_PLAY, 0);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stripe:
            case R.id.square:
                this.type = 1;
                seclectTrue(this.mApp.radioPos);
                return;
            default:
                return;
        }
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
                    ConfigSetDialog.this.setConnectType();
                    return;
                }
                Mainboard.getInstance().requestAUXOperate(Mainboard.EAUXOperate.ACTIVATE);
                ConfigSetDialog.this.showBluetoothDialog(ConfigSetDialog.this.context.getString(R.string.string_activating_aux), ConfigSetDialog.this.context.getString(R.string.string_do_not_do_anything), (String) null, (String) null, false);
            }
        });
        builder.setNegativeButton(btnNegaText, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (flag) {
                    ConfigSetDialog.this.showBluetoothDialog("", ConfigSetDialog.this.context.getString(R.string.string_activate_aux), ConfigSetDialog.this.context.getString(R.string.string_start), ConfigSetDialog.this.context.getString(R.string.string_cancel), false);
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
                ConfigSetDialog.this.myCustomDialog = null;
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

    private void pressItem(int position) {
    }

    public void unregisterHandlerr() {
        this.mApp.unregisterHandler(this.mHandler);
    }

    static class ReverseialogHandler extends Handler {
        private WeakReference<ConfigSetDialog> target;

        public ReverseialogHandler(ConfigSetDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((ConfigSetDialog) this.target.get()).handlerMsg(msg);
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
                            if (ConfigSetDialog.this.myCustomDialog != null) {
                                ConfigSetDialog.this.myCustomDialog.dismiss();
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

    /* access modifiers changed from: private */
    public void seclectTrue(int seclectCuttun) {
        if (this.type != 2) {
            if (this.mApp.radioPos == 0) {
                this.square_layout.setVisibility(8);
                this.stripe_layout.setVisibility(0);
            } else {
                this.square_layout.setVisibility(0);
                this.stripe_layout.setVisibility(8);
            }
            if (this.mApp.radioPos < 0 || this.mApp.radioPos >= this.radioset.getChildCount()) {
                this.mApp.radioPos = 0;
            }
            SysConst.storeData[4] = (byte) this.mApp.radioPos;
            Log.e("", "SysConst.storeData = " + SysConst.storeData);
            Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_RADIO, this.mApp.radioPos);
            ((RadioButton) this.radioset.getChildAt(this.mApp.radioPos)).setChecked(true);
        }
    }
}
