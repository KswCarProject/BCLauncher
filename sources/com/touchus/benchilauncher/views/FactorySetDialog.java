package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.ProjectConfig;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.sysconst.PubSysConst;
import java.lang.ref.WeakReference;

public class FactorySetDialog extends Dialog implements View.OnClickListener {
    public int height = 200;
    private Button joinBtn;
    private LauncherApplication mApp;
    private Context mContext;
    public MHandler mHandler = new MHandler(this);
    private EditText pwdEdit;
    public String showMessage = "";
    public String title = "";
    public int width = 500;

    public FactorySetDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public FactorySetDialog(Context context, int themeId) {
        super(context, themeId);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wifi_relationship);
        this.mApp = (LauncherApplication) this.mContext.getApplicationContext();
        TextView showInfo = (TextView) findViewById(R.id.showInfo);
        this.joinBtn = (Button) findViewById(R.id.joinBtn);
        this.pwdEdit = (EditText) findViewById(R.id.pwdEdit);
        this.joinBtn.setOnClickListener(this);
        this.joinBtn.setEnabled(true);
        this.joinBtn.setText(this.mContext.getString(R.string.string_callphone_confirm));
        if (TextUtils.isEmpty(this.showMessage)) {
            this.showMessage = this.mContext.getString(R.string.msg_factory_pwd);
        }
        showInfo.setText(this.showMessage);
        ((TextView) findViewById(R.id.titleName)).setText(this.mContext.getString(R.string.msg_factory_set));
        getWindow().setLayout(this.width, this.height);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.joinBtn) {
            String pwd = this.pwdEdit.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                ToastTool.showLongToast(this.mContext, this.mContext.getString(R.string.msg_input_pwd));
                return;
            } else if (!ProjectConfig.FactoryPwd.contains(pwd)) {
                ToastTool.showLongToast(this.mContext, this.mContext.getString(R.string.msg_factory_err_pwd_unjoin));
                return;
            } else {
                Intent intent = this.mContext.getPackageManager().getLaunchIntentForPackage("com.touchus.factorytest");
                if (intent != null) {
                    intent.addFlags(272629760);
                    intent.putExtra(BenzModel.KEY, BenzModel.benzTpye.getCode());
                    intent.putExtra(BenzModel.SIZE_KEY, BenzModel.benzSize.getCode());
                    intent.putExtra(PubSysConst.KEY_BREAKPOS, this.mApp.breakpos);
                    this.mContext.startActivity(intent);
                }
            }
        }
        dismiss();
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

    static class MHandler extends Handler {
        private WeakReference<FactorySetDialog> target;

        public MHandler(FactorySetDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FactorySetDialog) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.PRESS.getCode()) {
                this.joinBtn.performClick();
            } else if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }
}
