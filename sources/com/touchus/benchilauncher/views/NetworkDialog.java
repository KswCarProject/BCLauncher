package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.adapter.AdpWifiAdapter;
import com.touchus.benchilauncher.bean.BeanWifi;
import com.touchus.benchilauncher.utils.MobileDataSwitcher;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.benchilauncher.utils.WifiTool;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Priority;

public class NetworkDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private int CHECK_WIFI_SPACE = Priority.INFO_INT;
    private AdpWifiAdapter adapter;
    private LauncherApplication app;
    private int clickCount = 0;
    /* access modifiers changed from: private */
    public Context context;
    private String currentApName;
    /* access modifiers changed from: private */
    public String currentConnectingWifiSsid;
    private String currentPwd;
    private TextView data;
    private TextView dataTview;
    private ImageView data_switch;
    private CheckBox hadPwdCbox;
    private boolean iDataOpen = false;
    private boolean iWifiOpen = false;
    /* access modifiers changed from: private */
    public boolean iWlanOpen = false;
    private ArrayList<BeanWifi> list;
    public MHandler mHandler = new MHandler(this);
    private ListView mListView;
    private TextView noListDataTview;
    /* access modifiers changed from: private */
    public Runnable refreshWifiListRunnable = new Runnable() {
        public void run() {
            NetworkDialog.this.refreshWifiListViewByConnected();
            if (NetworkDialog.this.iWlanOpen) {
                NetworkDialog.this.mHandler.postDelayed(NetworkDialog.this.refreshWifiListRunnable, 5000);
            } else {
                NetworkDialog.this.mHandler.removeCallbacks(NetworkDialog.this.refreshWifiListRunnable);
            }
        }
    };
    private Button submitBtn;
    private WifiBroadCastReceiver wifiReceiver;
    private TextView wifi_ap;
    private ImageView wifi_ap_switch;
    private RelativeLayout wifi_apset_rl;
    private EditText wifi_name_et;
    private TextView wifi_pwd;
    private EditText wifi_pwd_et;
    private TextView wlan;
    private ImageView wlan_switch;

    public NetworkDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public NetworkDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_network_layout);
        this.app = (LauncherApplication) this.context.getApplicationContext();
        this.wlan = (TextView) findViewById(R.id.wlan);
        this.wifi_ap = (TextView) findViewById(R.id.wifi_ap);
        this.data = (TextView) findViewById(R.id.data);
        this.wlan_switch = (ImageView) findViewById(R.id.wlan_switch);
        this.wifi_ap_switch = (ImageView) findViewById(R.id.wifi_ap_switch);
        this.data_switch = (ImageView) findViewById(R.id.data_switch);
        this.wifi_apset_rl = (RelativeLayout) findViewById(R.id.wifi_apset_rl);
        this.wifi_name_et = (EditText) findViewById(R.id.wifi_name_et);
        this.wifi_pwd = (TextView) findViewById(R.id.wifi_pwd);
        this.wifi_pwd_et = (EditText) findViewById(R.id.wifi_pwd_et);
        this.hadPwdCbox = (CheckBox) findViewById(R.id.hadPwdCbox);
        this.submitBtn = (Button) findViewById(R.id.submitBtn);
        this.mListView = (ListView) findViewById(R.id.wifi_list);
        this.noListDataTview = (TextView) findViewById(R.id.noListDataTview);
        this.dataTview = (TextView) findViewById(R.id.dataTview);
        this.wlan.setOnClickListener(this);
        this.wifi_ap.setOnClickListener(this);
        this.data.setOnClickListener(this);
        this.wlan_switch.setOnClickListener(this);
        this.wifi_ap_switch.setOnClickListener(this);
        this.data_switch.setOnClickListener(this);
        this.wifi_name_et.setOnClickListener(this);
        this.wifi_pwd_et.setOnClickListener(this);
        this.hadPwdCbox.setOnCheckedChangeListener(this);
        this.submitBtn.setOnClickListener(this);
        this.iDataOpen = MobileDataSwitcher.getMobileDataState(this.context);
        if (this.iDataOpen) {
            this.dataTview.setText(this.context.getString(R.string.msg_data_open));
        } else {
            this.dataTview.setText(this.context.getString(R.string.msg_data_close));
        }
        setSwitchImage(this.data_switch, this.iDataOpen);
        String[] idAndKey = this.app.getWifiApIdAndKey();
        this.wifi_name_et.setText(idAndKey[0]);
        this.wifi_pwd_et.setText(idAndKey[1]);
        this.iWlanOpen = WifiTool.iWifiState(this.context);
        if (this.iWlanOpen) {
            this.app.setWifiApState(false);
        }
        this.iWifiOpen = this.app.getWifiApState();
        setSwitchImage(this.wlan_switch, this.iWlanOpen);
        setSwitchImage(this.wifi_ap_switch, this.iWifiOpen);
        this.noListDataTview.setVisibility(0);
        if (this.iWlanOpen) {
            this.wifi_apset_rl.setVisibility(8);
            this.mListView.setVisibility(0);
            this.noListDataTview.setVisibility(8);
            this.dataTview.setVisibility(8);
        } else if (this.iWifiOpen) {
            this.wifi_apset_rl.setVisibility(0);
            this.mListView.setVisibility(8);
            this.noListDataTview.setVisibility(8);
            this.dataTview.setVisibility(8);
        } else if (this.iDataOpen) {
            this.wifi_apset_rl.setVisibility(8);
            this.mListView.setVisibility(8);
            this.noListDataTview.setVisibility(8);
            this.dataTview.setVisibility(0);
        }
        if (this.app.getWifiApHasPwd()) {
            this.hadPwdCbox.setChecked(true);
            this.wifi_pwd.setVisibility(8);
            this.wifi_pwd_et.setVisibility(8);
        } else {
            this.hadPwdCbox.setChecked(false);
            this.wifi_pwd.setVisibility(0);
            this.wifi_pwd_et.setVisibility(0);
        }
        this.mListView.setOnItemClickListener(this);
        this.list = new ArrayList<>();
        this.adapter = new AdpWifiAdapter(this.context, this.list);
        this.mListView.setAdapter(this.adapter);
        registerWifiReceiver();
        enterToWifiSettingView();
        getWindow().setSoftInputMode(18);
        super.onCreate(savedInstanceState);
    }

    private void setSwitchImage(ImageView imageView, boolean isOpen) {
        if (isOpen) {
            imageView.setImageResource(R.drawable.kaiguan_on);
        } else {
            imageView.setImageResource(R.drawable.kaiguan_off);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.app.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.app.unregisterHandler(this.mHandler);
        unregisterWifiReceiver();
        super.onStop();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.data:
                this.dataTview.setVisibility(0);
                if (this.iDataOpen) {
                    this.dataTview.setText(this.context.getString(R.string.msg_data_open));
                } else {
                    this.dataTview.setText(this.context.getString(R.string.msg_data_close));
                }
                this.wifi_apset_rl.setVisibility(8);
                this.mListView.setVisibility(8);
                this.noListDataTview.setVisibility(8);
                return;
            case R.id.data_switch:
                this.dataTview.setVisibility(0);
                if (this.iDataOpen) {
                    this.iDataOpen = false;
                    this.dataTview.setText(this.context.getString(R.string.msg_data_close));
                } else {
                    this.iDataOpen = true;
                    this.dataTview.setText(this.context.getString(R.string.msg_data_open));
                }
                MobileDataSwitcher.setMobileData(this.context, this.iDataOpen);
                setSwitchImage(this.data_switch, this.iDataOpen);
                this.wifi_apset_rl.setVisibility(8);
                this.mListView.setVisibility(8);
                this.noListDataTview.setVisibility(8);
                this.wifi_apset_rl.setVisibility(8);
                return;
            case R.id.wlan:
                if (this.iWlanOpen) {
                    this.mListView.setVisibility(0);
                    this.noListDataTview.setVisibility(8);
                } else {
                    this.mListView.setVisibility(8);
                    this.noListDataTview.setVisibility(0);
                }
                this.wifi_apset_rl.setVisibility(8);
                this.dataTview.setVisibility(8);
                return;
            case R.id.wlan_switch:
                if (this.iWlanOpen) {
                    this.iWlanOpen = false;
                    this.mListView.setVisibility(8);
                    this.noListDataTview.setVisibility(0);
                    unregisterWifiReceiver();
                } else {
                    this.iWlanOpen = true;
                    this.iWifiOpen = false;
                    this.mListView.setVisibility(0);
                    this.noListDataTview.setVisibility(8);
                    registerWifiReceiver();
                    this.app.setWifiApState(false);
                }
                WifiTool.setWifiState(this.context, this.iWlanOpen);
                enterToWifiSettingView();
                this.wifi_apset_rl.setVisibility(8);
                this.dataTview.setVisibility(8);
                setSwitchImage(this.wlan_switch, this.iWlanOpen);
                setSwitchImage(this.wifi_ap_switch, this.iWifiOpen);
                return;
            case R.id.wifi_ap:
                if (this.iWifiOpen) {
                    this.wifi_apset_rl.setVisibility(0);
                    this.noListDataTview.setVisibility(8);
                } else {
                    this.wifi_apset_rl.setVisibility(8);
                    this.noListDataTview.setVisibility(0);
                }
                this.mListView.setVisibility(8);
                this.dataTview.setVisibility(8);
                return;
            case R.id.wifi_ap_switch:
                if (this.iWifiOpen) {
                    this.iWifiOpen = false;
                    WifiTool.closeWifiAp(this.context);
                    this.wifi_apset_rl.setVisibility(8);
                    this.noListDataTview.setVisibility(0);
                } else {
                    this.iWifiOpen = true;
                    this.iWlanOpen = false;
                    unregisterWifiReceiver();
                    this.wifi_apset_rl.setVisibility(0);
                    this.noListDataTview.setVisibility(8);
                    WifiTool.openWifiAp(this.context, WifiTool.getCurrentWifiApConfig(this.context));
                    WifiTool.setWifiState(this.context, this.iWlanOpen);
                }
                this.app.setWifiApState(this.iWifiOpen);
                this.mListView.setVisibility(8);
                this.dataTview.setVisibility(8);
                setSwitchImage(this.wlan_switch, this.iWlanOpen);
                setSwitchImage(this.wifi_ap_switch, this.iWifiOpen);
                return;
            case R.id.submitBtn:
                String name = this.wifi_name_et.getText().toString();
                String pwd = this.wifi_pwd_et.getText().toString();
                if (!UtilTools.stringFilter(name)) {
                    ToastTool.showLongToast(this.context, this.context.getString(R.string.msg_error_input_wifiap));
                    return;
                } else if (TextUtils.isEmpty(name)) {
                    ToastTool.showLongToast(this.context, this.context.getString(R.string.msg_error_input_wifiap1));
                    return;
                } else if (this.hadPwdCbox.isChecked() || pwd.length() >= 8) {
                    if (this.hadPwdCbox.isChecked()) {
                        this.app.setWifiApHasPwd(true);
                        pwd = "";
                    } else {
                        this.app.setWifiApHasPwd(false);
                    }
                    this.app.setWifiApIdAndKey(new String[]{name, pwd});
                    if (WifiTool.iWifiApIsOpen(this.context)) {
                        WifiTool.closeWifiAp(this.context);
                        WifiTool.updateWifiAp(this.context, name, pwd);
                        WifiTool.openWifiAp(this.context, WifiTool.getCurrentWifiApConfig(this.context));
                    } else {
                        WifiTool.updateWifiAp(this.context, name, pwd);
                    }
                    ToastTool.showLongToast(this.context, this.context.getString(R.string.msg_save_ok));
                    return;
                } else {
                    ToastTool.showLongToast(this.context, this.context.getString(R.string.msg_error_pwd_min_8));
                    return;
                }
            default:
                return;
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            this.wifi_pwd_et.setEnabled(false);
            this.currentPwd = this.wifi_pwd_et.getText().toString();
            this.wifi_pwd_et.setHint("");
            this.wifi_pwd_et.setText("");
            this.wifi_pwd.setVisibility(8);
            this.wifi_pwd_et.setVisibility(8);
            return;
        }
        this.wifi_pwd.setVisibility(0);
        this.wifi_pwd_et.setVisibility(0);
        this.wifi_pwd_et.setEnabled(true);
        this.wifi_pwd_et.setHint(this.context.getString(R.string.msg_wifiap_input_pwd));
        this.wifi_pwd_et.setText(this.currentPwd);
    }

    private void registerWifiReceiver() {
        IntentFilter wifiFilter = new IntentFilter();
        wifiFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        wifiFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.wifiReceiver = new WifiBroadCastReceiver();
        this.context.registerReceiver(this.wifiReceiver, wifiFilter);
    }

    private void unregisterWifiReceiver() {
        try {
            this.context.unregisterReceiver(this.wifiReceiver);
        } catch (Exception e) {
        }
    }

    public void disConnectCurWifi() {
        WifiTool.disConnectWifi(this.context);
    }

    public void connectingToSpecifyWifi(String ssid) {
        refreshWifiListViewByConnecting(ssid);
    }

    private void enterToWifiSettingView() {
        this.mHandler.removeCallbacks(this.refreshWifiListRunnable);
        this.mHandler.postDelayed(this.refreshWifiListRunnable, 200);
    }

    public void updateWifiList() {
        refreshWifiListViewByDisconnect();
    }

    /* access modifiers changed from: private */
    public void refreshWifiListViewByConnected() {
        this.list.clear();
        this.list.addAll(WifiTool.getAllCanConectWifi(this.context));
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    private void refreshWifiListViewByConnecting(String ssid) {
        int oldIndex = this.mListView.getFirstVisiblePosition();
        this.list = this.adapter.getListData();
        Iterator<BeanWifi> it = this.list.iterator();
        while (it.hasNext()) {
            BeanWifi temp = it.next();
            if (ssid.equals(temp.getName())) {
                temp.setState(2);
            } else {
                temp.setState(3);
            }
        }
        this.adapter.notifyDataSetInvalidated();
        this.mHandler.postDelayed(new CheckWifiRunnable(ssid), (long) this.CHECK_WIFI_SPACE);
        this.currentConnectingWifiSsid = ssid;
        this.mListView.setSelection(oldIndex);
    }

    /* access modifiers changed from: private */
    public void refreshWifiListViewByDisconnect() {
        int oldIndex = this.mListView.getFirstVisiblePosition();
        this.list = this.adapter.getListData();
        Iterator<BeanWifi> it = this.list.iterator();
        while (it.hasNext()) {
            BeanWifi wifi = it.next();
            if (wifi.getState() != 3) {
                wifi.setState(3);
            }
        }
        this.adapter.notifyDataSetInvalidated();
        this.mListView.setSelection(oldIndex);
    }

    /* access modifiers changed from: private */
    public void refreshWifiSettingView() {
        if (this.iWlanOpen && this.context != null && this.mListView != null && this.list.size() != 0) {
            if (this.mListView.getAdapter().getItem(0) instanceof BeanWifi) {
                try {
                    if (((WifiManager) this.context.getSystemService("wifi")).getConnectionInfo().getNetworkId() < 0) {
                        refreshWifiListViewByDisconnect();
                    } else {
                        refreshWifiListViewByConnected();
                    }
                } catch (Exception e) {
                }
            }
            this.currentConnectingWifiSsid = "";
        }
    }

    class CheckWifiRunnable implements Runnable {
        private String ConnectingWifiSsid;

        public CheckWifiRunnable(String ssid) {
            this.ConnectingWifiSsid = ssid;
        }

        public void run() {
            if (NetworkDialog.this.context != null && this.ConnectingWifiSsid.equals(NetworkDialog.this.currentConnectingWifiSsid)) {
                WifiInfo info = ((WifiManager) NetworkDialog.this.context.getSystemService("wifi")).getConnectionInfo();
                Log.d("launcherlog", "CheckWifiRunnable1 " + info.getSSID());
                if (info.getSSID().length() <= 2) {
                    WifiConfiguration config = WifiTool.getHadSaveWifiConfig(NetworkDialog.this.context, NetworkDialog.this.currentConnectingWifiSsid);
                    if (config != null) {
                        Log.d("launcherlog", "CheckWifiRunnable1 " + config.status + " " + config.disableReason);
                        NetworkDialog.this.refreshWifiListViewByDisconnect();
                        if (config.disableReason == 3) {
                            ToastTool.showLongToast(NetworkDialog.this.context, String.valueOf(NetworkDialog.this.context.getString(R.string.msg_wifi_err_pwd_unjoin)) + NetworkDialog.this.currentConnectingWifiSsid);
                            WifiTool.removeWifiConfig(NetworkDialog.this.context, config.networkId);
                            return;
                        }
                        ToastTool.showLongToast(NetworkDialog.this.context, String.valueOf(NetworkDialog.this.context.getString(R.string.msg_wifi_unjoin)) + this.ConnectingWifiSsid);
                        return;
                    }
                    return;
                }
                NetworkDialog.this.mHandler.postDelayed(new CheckWifiRunnable(this.ConnectingWifiSsid), 2000);
            }
        }
    }

    class WifiBroadCastReceiver extends BroadcastReceiver {
        WifiBroadCastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            NetworkDialog.this.refreshWifiSettingView();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        BeanWifi wifi = (BeanWifi) this.mListView.getAdapter().getItem(position);
        if (TextUtils.isEmpty(wifi.getName()) || wifi.getState() == 2) {
            return;
        }
        if (wifi.getState() != 3 || wifi.isPwd()) {
            showWifiConfigDialog(wifi);
            return;
        }
        disConnectCurWifi();
        WifiConfiguration config = WifiTool.CreateWifiInfo(this.context, wifi.getName(), "", WifiTool.getCurrentWifiPwdType(wifi.getCapabilities()));
        if (!WifiTool.connectToSpecifyWifi(this.context, config)) {
            ToastTool.showBigShortToast(this.context, String.valueOf(this.context.getString(R.string.msg_un_connect_to)) + config.SSID);
        } else {
            connectingToSpecifyWifi(wifi.getName());
        }
    }

    private void showWifiConfigDialog(BeanWifi wifi) {
        DlgWifiRelaDialog wifiialog = new DlgWifiRelaDialog(this.context, R.style.Dialog);
        wifiialog.parent = this;
        wifiialog.width = 500;
        wifiialog.height = 200;
        wifiialog.currentWifiInfo = wifi;
        wifiialog.show();
    }

    static class MHandler extends Handler {
        private WeakReference<NetworkDialog> target;

        public MHandler(NetworkDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((NetworkDialog) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode()) {
                dismiss();
            } else if (idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }
}
