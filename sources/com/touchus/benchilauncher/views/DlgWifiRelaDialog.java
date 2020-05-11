package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.bean.BeanWifi;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.benchilauncher.utils.WifiTool;

public class DlgWifiRelaDialog extends Dialog implements View.OnClickListener {
    public BeanWifi currentWifiInfo;
    public int height = 200;
    /* access modifiers changed from: private */
    public Button joinBtn;
    private Context mContext;
    public NetworkDialog parent;
    private EditText pwdEdit;
    private TextWatcher pwdTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
            if (text.length() >= 8 && !DlgWifiRelaDialog.this.joinBtn.isEnabled()) {
                DlgWifiRelaDialog.this.joinBtn.setEnabled(true);
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    };
    public String showMessage = "";
    public String title = "";
    public int width = 500;

    public DlgWifiRelaDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public DlgWifiRelaDialog(Context context, int themeId) {
        super(context, themeId);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiConfiguration config = WifiTool.getHadSaveWifiConfig(this.mContext, this.currentWifiInfo.getName());
        if (this.currentWifiInfo.getState() == 3 && config == null) {
            setContentView(R.layout.dialog_wifi_relationship);
            TextView showInfo = (TextView) findViewById(R.id.showInfo);
            this.joinBtn = (Button) findViewById(R.id.joinBtn);
            this.pwdEdit = (EditText) findViewById(R.id.pwdEdit);
            this.joinBtn.setOnClickListener(this);
            this.joinBtn.setEnabled(true);
            this.joinBtn.setText(this.mContext.getString(R.string.name_btn_join));
            if (TextUtils.isEmpty(this.showMessage)) {
                this.showMessage = this.mContext.getString(R.string.msg_wifi_pwd);
            }
            showInfo.setText(this.showMessage);
        } else {
            setContentView(R.layout.dialog_wifi_relationship_1);
            Button canceBtn = (Button) findViewById(R.id.canceBtn);
            Button submitBtn = (Button) findViewById(R.id.submitBtn);
            submitBtn.setOnClickListener(this);
            canceBtn.setOnClickListener(this);
            if (this.currentWifiInfo.getState() == 1) {
                submitBtn.setText(this.mContext.getString(R.string.name_btn_cutconnect));
            } else if (this.currentWifiInfo.getState() == 3) {
                submitBtn.setText(this.mContext.getString(R.string.name_btn_connect));
            }
            canceBtn.setText(this.mContext.getString(R.string.name_btn_cancehadsave));
            this.height = (int) (((double) this.height) * 0.7d);
        }
        ((TextView) findViewById(R.id.titleName)).setText(this.currentWifiInfo.getName());
        getWindow().setLayout(this.width, this.height);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.submitBtn) {
            if (this.currentWifiInfo.getState() == 1) {
                WifiTool.disConnectWifi(this.mContext);
                this.parent.updateWifiList();
            } else {
                WifiConfiguration config = WifiTool.getHadSaveWifiConfig(this.mContext, this.currentWifiInfo.getName());
                if (config != null) {
                    WifiTool.connectToHadSaveConfigWifi(this.mContext, config.networkId);
                    this.parent.disConnectCurWifi();
                    this.parent.connectingToSpecifyWifi(this.currentWifiInfo.getName());
                } else {
                    ToastTool.showLongToast(this.mContext, this.mContext.getString(R.string.msg_wifi_list_err));
                }
            }
        } else if (view.getId() == R.id.canceBtn) {
            WifiConfiguration config2 = WifiTool.getHadSaveWifiConfig(this.mContext, this.currentWifiInfo.getName());
            if (config2 != null) {
                WifiTool.removeWifiConfig(this.mContext, config2.networkId);
                if (this.currentWifiInfo.getState() == 1) {
                    this.parent.updateWifiList();
                }
            }
        } else if (view.getId() == R.id.joinBtn) {
            String pwd = this.pwdEdit.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                ToastTool.showLongToast(this.mContext, this.mContext.getString(R.string.msg_input_pwd));
                return;
            } else if (pwd.length() < 8) {
                ToastTool.showLongToast(this.mContext, this.mContext.getString(R.string.msg_error_pwd_min_8));
                return;
            } else {
                this.parent.connectingToSpecifyWifi(this.currentWifiInfo.getName());
                if (!WifiTool.connectToSpecifyWifi(this.mContext, WifiTool.CreateWifiInfo(this.mContext, this.currentWifiInfo.getName(), pwd, WifiTool.getCurrentWifiPwdType(this.currentWifiInfo.getCapabilities())))) {
                    ToastTool.showLongToast(this.mContext, String.valueOf(this.mContext.getString(R.string.msg_wifi_err_pwd_unjoin)) + this.currentWifiInfo.getName());
                    this.parent.updateWifiList();
                }
            }
        }
        dismiss();
    }
}
