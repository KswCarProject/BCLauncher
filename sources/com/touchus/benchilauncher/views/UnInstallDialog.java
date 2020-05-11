package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;

public class UnInstallDialog extends Dialog {
    private LauncherApplication app;
    private String appName = "";
    private Button btnCancel;
    private Button btnConfirm;
    public int height = 200;
    private Context mContext;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            UnInstallDialog.this.clickEvent(v);
        }
    };
    private String pkgName = "";
    public int width = 500;

    public void setPkgName(String pkg, String appName2) {
        this.pkgName = pkg;
        this.appName = appName2;
    }

    public UnInstallDialog(Context context) {
        super(context);
        this.mContext = context;
        this.app = (LauncherApplication) context.getApplicationContext();
    }

    public UnInstallDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.app = (LauncherApplication) context.getApplicationContext();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wifi_relationship_1);
        this.btnConfirm = (Button) findViewById(R.id.submitBtn);
        this.btnCancel = (Button) findViewById(R.id.canceBtn);
        this.btnConfirm.setOnClickListener(this.onClickListener);
        this.btnCancel.setOnClickListener(this.onClickListener);
        this.btnConfirm.setText(this.mContext.getString(R.string.uninstall));
        ((TextView) findViewById(R.id.titleName)).setText(String.format(this.mContext.getString(R.string.uninstall_title), new Object[]{this.appName}));
        this.btnCancel.setText(this.mContext.getString(R.string.name_btn_cancel));
        this.btnCancel.setSelected(true);
        getWindow().setLayout(this.width, this.height);
    }

    /* access modifiers changed from: private */
    public void clickEvent(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                this.btnCancel.setSelected(false);
                this.btnConfirm.setSelected(true);
                unstallApp();
                dismiss();
                return;
            case R.id.canceBtn:
                this.btnCancel.setSelected(true);
                this.btnConfirm.setSelected(false);
                dismiss();
                return;
            default:
                return;
        }
    }

    private void unstallApp() {
        if (TextUtils.isEmpty(this.pkgName)) {
            Log.d("uninstall", "pkgName is empty.");
            return;
        }
        Intent uninstall_intent = new Intent();
        uninstall_intent.setAction("android.intent.action.DELETE");
        uninstall_intent.setData(Uri.parse("package:" + this.pkgName));
        this.mContext.startActivity(uninstall_intent);
    }

    public void dismiss() {
        super.dismiss();
    }
}
