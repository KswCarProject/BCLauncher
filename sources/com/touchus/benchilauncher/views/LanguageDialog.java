package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.publicutils.sysconst.PubSysConst;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class LanguageDialog extends Dialog {
    private Context context;
    private RadioGroup languageType;
    private LauncherApplication mApp;
    public ReverseialogHandler mHandler = new ReverseialogHandler(this);
    private byte mIDRIVERENUM;
    private Launcher mMMainActivity;
    private SpUtilK mSpUtilK;
    private int num;
    /* access modifiers changed from: private */
    public int selectPos = 0;

    public LanguageDialog(Context context2) {
        super(context2);
    }

    public LanguageDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_language_layout);
        this.mApp = (LauncherApplication) this.context.getApplicationContext();
        initView();
        initSetup();
        initData();
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
        this.languageType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < PubSysConst.ids.length; i++) {
                    if (checkedId == PubSysConst.ids[i]) {
                        LanguageDialog.this.selectPos = i;
                    }
                }
                LanguageDialog.this.seclectTrue(LanguageDialog.this.selectPos);
                LanguageDialog.this.press();
                LanguageDialog.this.dismiss();
            }
        });
        seclectTrue(this.selectPos);
    }

    private void initView() {
        this.languageType = (RadioGroup) findViewById(R.id.language_type);
        this.languageType.removeAllViews();
        String[] strings = this.context.getResources().getStringArray(R.array.languages);
        if (SysConst.TURKISH_VERSION) {
            strings = this.context.getResources().getStringArray(R.array.turkish_languages);
            PubSysConst.ids = PubSysConst.turkish_ids;
        }
        if (SysConst.LANGUAGE == 0) {
            this.num = 3;
        } else {
            this.num = PubSysConst.ids.length;
        }
        for (int i = 0; i < this.num; i++) {
            RadioButton rButton = (RadioButton) getLayoutInflater().inflate(R.layout.setting_radiobutton, (ViewGroup) null);
            rButton.setId(PubSysConst.ids[i]);
            rButton.setText(strings[i]);
            rButton.setLayoutParams(new ViewGroup.LayoutParams(400, 53));
            this.languageType.addView(rButton);
        }
    }

    private void initData() {
        this.mMMainActivity = (Launcher) this.context;
        this.mSpUtilK = new SpUtilK((Context) this.mMMainActivity);
        this.selectPos = this.mSpUtilK.getInt(SysConst.FLAG_LANGUAGE_TYPE, 0);
        ((RadioButton) this.languageType.getChildAt(this.selectPos)).setChecked(true);
    }

    public void unregisterHandlerr() {
        this.mApp.unregisterHandler(this.mHandler);
    }

    static class ReverseialogHandler extends Handler {
        private WeakReference<LanguageDialog> target;

        public ReverseialogHandler(LanguageDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((LanguageDialog) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (msg.what == 6001) {
            this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.DOWN.getCode()) {
                if (this.selectPos < this.languageType.getChildCount() - 1) {
                    this.selectPos++;
                }
                seclectTrue(this.selectPos);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.UP.getCode()) {
                if (this.selectPos > 0) {
                    this.selectPos--;
                }
                seclectTrue(this.selectPos);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
                press();
                dismiss();
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.BACK.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.HOME.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }

    /* access modifiers changed from: private */
    public void press() {
        this.mApp.iLanguageType = this.selectPos;
        if (this.selectPos < PubSysConst.ids.length) {
            switch (PubSysConst.ids[this.selectPos]) {
                case R.id.simplified:
                    this.mApp.updateLanguage(Locale.SIMPLIFIED_CHINESE);
                    break;
                case R.id.traditional:
                    this.mApp.updateLanguage(Locale.TRADITIONAL_CHINESE);
                    break;
                case R.id.english:
                    this.mApp.updateLanguage(Locale.US);
                    break;
                case R.id.spanish:
                    this.mApp.updateLanguage(new Locale("es", "ES"));
                    break;
                case R.id.german:
                    this.mApp.updateLanguage(new Locale("de", "DE"));
                    break;
                case R.id.russian:
                    this.mApp.updateLanguage(new Locale("ru", "RU"));
                    break;
                case R.id.french:
                    this.mApp.updateLanguage(new Locale("fr", "FR"));
                    break;
                case R.id.portuguese:
                    this.mApp.updateLanguage(new Locale("pt", "PT"));
                    break;
                case R.id.serbian:
                    this.mApp.updateLanguage(new Locale("sr", "RS"));
                    break;
                case R.id.turkish:
                    this.mApp.updateLanguage(new Locale("tr", "TR"));
                    break;
                case R.id.swedish:
                    this.mApp.updateLanguage(new Locale("sv", "SE"));
                    break;
                case R.id.italian:
                    this.mApp.updateLanguage(new Locale("it", "IT"));
                    break;
                case R.id.polish:
                    this.mApp.updateLanguage(new Locale("pl", "PL"));
                    break;
                case R.id.japanese:
                    this.mApp.updateLanguage(new Locale("ja", "JP"));
                    break;
                case R.id.korean:
                    this.mApp.updateLanguage(new Locale("ko", "KR"));
                    break;
                case R.id.filipino:
                    this.mApp.updateLanguage(new Locale("tl", "PH"));
                    break;
                case R.id.vietnamese:
                    this.mApp.updateLanguage(new Locale("vi", "VN"));
                    break;
                case R.id.thai:
                    this.mApp.updateLanguage(new Locale("th", "TH"));
                    break;
                case R.id.dutch:
                    this.mApp.updateLanguage(new Locale("nl", "NL"));
                    break;
                case R.id.danish:
                    this.mApp.updateLanguage(new Locale("da", "DK"));
                    break;
                case R.id.greek:
                    this.mApp.updateLanguage(new Locale("el", "GR"));
                    break;
                case R.id.hindi:
                    this.mApp.updateLanguage(new Locale("hi", "IN"));
                    break;
            }
            this.mSpUtilK.putInt(SysConst.FLAG_LANGUAGE_TYPE, this.selectPos);
            Mainboard.getInstance().sendLanguageSetToMcu(this.selectPos);
            Mainboard.getInstance().getStoreDataFromMcu();
        }
    }

    /* access modifiers changed from: private */
    public void seclectTrue(int seclectCuttun) {
        for (int i = 0; i < this.languageType.getChildCount(); i++) {
            if (seclectCuttun == i) {
                this.languageType.getChildAt(i).setSelected(true);
            } else {
                this.languageType.getChildAt(i).setSelected(false);
            }
        }
    }
}
